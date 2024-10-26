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
@Entity (name = "users")
public class AppUser {

  @Id
  @GeneratedValue (strategy = GenerationType.SEQUENCE)
  private BigInteger userId;
  private String username;
  private String email;
  private String password;
  private Date created_time;
  private Date updated_time;
}
