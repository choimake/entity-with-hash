package com.example.entitywithhash.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserPk implements Serializable {
  private String idHash;
  private String id;

  public UserPk(String id) {
    this.idHash = computeHash(id);
    this.id = id;
  }

  static String computeHash(String id) {
    return Hex.encodeHexString(DigestUtils.sha256(id));
  }
}
