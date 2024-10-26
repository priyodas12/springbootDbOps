package tech.postgres.springbootDbOps.service;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.github.javafaker.Faker;

import lombok.extern.log4j.Log4j2;
import tech.postgres.springbootDbOps.dao.CommentDao;
import static tech.postgres.springbootDbOps.enums.BOUND.END;
import static tech.postgres.springbootDbOps.enums.BOUND.START;
import tech.postgres.springbootDbOps.model.Comments;

@Log4j2
@Service
public class CommentService {

  private final CommentDao commentDao;
  private final Faker faker;

  public CommentService (CommentDao commentDao, Faker faker) {
    this.commentDao = commentDao;
    this.faker = faker;
  }


  //insert comments data;
  public void insertCommentData (
      Map<String, BigInteger> userBound, Map<String, BigInteger> postBound
                                ) {

    var userStartIndex = userBound.get (START.name ());

    var userEndIndex = userBound.get (END.name ());

    var postStartIndex = postBound.get (START.name ());

    var postEndIndex = postBound.get (END.name ());

    IntStream.rangeClosed (1, 25000)
        .forEach (i -> {
          Comments comment = Comments.builder ()
              .userId (BigInteger.valueOf (
                  new Random ().nextLong (userStartIndex.longValue (), userEndIndex.longValue ())))
              .postId (BigInteger.valueOf (
                  new Random ().nextLong (postStartIndex.longValue (), postEndIndex.longValue ())))
              .commentDesc (faker.lorem ().paragraph (2))
              .created_time (Date.from (Instant.now ()))
              .updated_time (Date.from (Instant.now ()))
              .build ();

          try {
            commentDao.save (comment);
          }
          catch (DataIntegrityViolationException e) {
            System.err.println (
                "Duplicate key violation, skipping photo for post ID: " + comment.getPostId ()
                + " and user ID: " + comment.getUserId ());
          }
          catch (Exception e) {
            log.warn ("Error while saving data: {}: exception: {}", comment, e.getMessage ());
          }
        });
  }
}
