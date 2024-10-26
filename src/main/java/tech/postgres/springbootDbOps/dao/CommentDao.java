package tech.postgres.springbootDbOps.dao;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tech.postgres.springbootDbOps.model.Comments;

@Repository
public interface CommentDao extends JpaRepository<Comments, BigInteger> {
}
