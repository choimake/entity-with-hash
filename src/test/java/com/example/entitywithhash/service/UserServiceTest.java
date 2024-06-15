package com.example.entitywithhash.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.entitywithhash.entity.User;
import com.example.entitywithhash.repository.UserRepository;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class UserServiceTest {

  @Autowired
  private UserService userService;

  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  @Transactional
  public void setUp() {
    // memo:
    // 別のテストで発生したデータにより、目的のテストが失敗しないようにするため、テストごとにテーブルをクリアする
    userRepository.deleteAll();
  }

  @Test
  public void insertUser_InsertsUserSuccessfully_WhenNewUser() {
    // Arrange
    String validUserId = "testUserId";
    String validUserName = "testUserName";

    // Act & Assert
    try {
      userService.insertUser(validUserId, validUserName);
    } catch (Exception e) {
      // 例外が投げられた場合はテスト失敗
      // memo:
      // ちょっと雑な判定なので、実際のプロダクトコードでは、想定通りのデータが入っているかを見るのが良い
      // ただ今回はサンプルということで、手軽な確認方法を選択した。
      throw new AssertionError("User insertion failed with exception: " + e.getMessage(), e);
    }

  }

  @Test
  public void insertUser_ThrowsJpaSystemException_WhenUserAlreadyExists() {
    // Arrange
    String validUserId = "testUserId";
    String validUserName = "testUserName";

    // Act
    userService.insertUser(validUserId, validUserName);

    // Assert
    // 2回目の挿入時に、JpaSystemExceptionが投げられることを確認する
    // このJpaSystemExceptionは、主キー制約違反による例外によって発生することが期待されるが、
    // ここでは、その判別はつかない
    JpaSystemException exception = assertThrows(JpaSystemException.class, () -> {
      userService.insertUser(validUserId, validUserName);
    });

    // 例外の原因がStatusRuntimeExceptionであることを確認する
    Throwable rootCause = getRootCause(exception);
    assertThat(rootCause).isInstanceOf(StatusRuntimeException.class);
    StatusRuntimeException statusException = (StatusRuntimeException) rootCause;

    // StatusRuntimeExceptionのステータスがALREADY_EXISTSであることを確認する
    // これにより、主キー制約違反による例外であることを確認する
    assertThat(statusException.getStatus().getCode()).isEqualTo(io.grpc.Status.Code.ALREADY_EXISTS);
  }

  private Throwable getRootCause(Throwable throwable) {
    Throwable cause;
    Throwable result = throwable;
    while ((cause = result.getCause()) != null && result != cause) {
      result = cause;
    }
    return result;
  }

  @Test
  public void whenFindByUserId_thenReturnUser() {
    // Arrange
    String validUserId = "testUserId";
    String validUserName = "testUserName";
    userService.insertUser(validUserId, validUserName);

    // Act
    // 作成したユーザーを検索
    User found = userService.findByUserId(validUserId);

    // Assert
    assertThat(found).isNotNull();
    assertThat(found.getId().getId()).isEqualTo(validUserId);
    assertThat(found.getName()).isEqualTo(validUserName);
  }

  @Test
  public void whenInvalidUserId_thenReturnNull() {
    // Act
    // 存在しないIDを指定して検索
    String invalidId = "invalidId";
    User found = userService.findByUserId(invalidId);

    // Assert
    assertThat(found).isNull();
  }
}
