package tech.postgres.springbootDbOps.model;


import java.math.BigInteger;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity (name = "comments")
public class Comments {

  @Id
  @GeneratedValue (strategy = GenerationType.SEQUENCE)
  private BigInteger commentUniqueId;
  private BigInteger userId;
  private BigInteger postId;
  private String commentDesc;
  private Date created_time;
  private Date updated_time;
}
