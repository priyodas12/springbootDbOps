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
@Entity (name = "posts")
public class Posts {

  @Id
  @GeneratedValue (strategy = GenerationType.SEQUENCE)
  private BigInteger postId;
  private BigInteger userId;
  private String contentType;
  private Date created_time;
  private Date updated_time;
}
