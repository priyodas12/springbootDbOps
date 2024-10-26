package tech.postgres.springbootDbOps.dao;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tech.postgres.springbootDbOps.model.Posts;

@Repository
public interface PostDao extends JpaRepository<Posts, BigInteger> {

}
