package com.example.entitywithhash.repository;

import com.example.entitywithhash.entity.User;
import com.example.entitywithhash.entity.UserPk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UserPk> {

  default User findById(String id) {
    // memo:
    // UserPkをService層で呼びたくない（Service層でUserPkを意識するのは避けたい）という拘りを出した実装
    // JpaRepositoryのお作法的な意味での是非は不明
    UserPk userPk = new UserPk(id);
    return findById(userPk).orElse(null);
  }
}
