package com.example.entitywithhash.entity;

import static com.example.entitywithhash.entity.UserPk.computeHash;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user")
@IdClass(UserPk.class)
@Getter
@Setter
@NoArgsConstructor
public class User {

  @Id
  @Column(name = "id_hash", nullable = false)
  private String idHash;

  @Id
  @Column(name = "id", nullable = false)
  private String id;

  @Column(name = "name", nullable = false)
  private String name;

  public User(String id, String name) {
    this.id = id;
    this.idHash = computeHash(id);
    this.name = name;
  }

}
