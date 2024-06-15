package com.example.entitywithhash.repository;

import com.example.entitywithhash.entity.User;
import com.example.entitywithhash.entity.UserPk;
import com.google.api.gax.rpc.AlreadyExistsException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, UserPk> {

  // memo
  // @Modifyingは更新系のクエリを実行する際に必要
  // @Modifyingを使用する場合、@Transactionalをつけないと、TransactionRequiredExceptionが発生する
  @Modifying
  @Transactional
  @Query(value = "INSERT INTO user (id_hash, id, name) VALUES (:id_hash, :id, :name)", nativeQuery = true)
  void insertUser(@Param("id_hash") String id_hash, @Param("id") String id, @Param("name") String name);

  default void insertUser(User user) {
    try {
      // memo:
      // 自己呼び出し（self-invocation）を使用しているため、@Transactionalをつけているにも関わらず、Transactionは実行されない点に注意
      // 今回は単純なinsertクエリなので問題ないとしているが、更新系のクエリを実行する場合は注意が必要
      insertUser(user.getIdHash(), user.getId(), user.getName());

    } catch (JpaSystemException e) {
      // JpaSystemException の原因がAlreadyExistsExceptionであるかを確認する
      // AlreadyExistsException は、Cloud Spannerにおいて、既に存在するデータを挿入しようとした場合にスローされる例外
      // そのためデータ重複が起きたかどうかを判断するために、この例外がスローされた場合はAlreadyExistsExceptionとして再スローする
      Throwable cause = e.getCause();
      while (cause != null) {
        if (cause instanceof AlreadyExistsException) {
          throw (AlreadyExistsException) cause;
        }
        cause = cause.getCause();
      }
      // 他の例外を再スロー
      throw e;
    }
  }

  default User findById(String id) {
    UserPk userPk = new UserPk(id);
    return findById(userPk).orElse(null);
  }
}
