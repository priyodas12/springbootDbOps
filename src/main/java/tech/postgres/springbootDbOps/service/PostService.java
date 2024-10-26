package tech.postgres.springbootDbOps.service;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.javafaker.Faker;

import lombok.extern.log4j.Log4j2;
import tech.postgres.springbootDbOps.dao.PostDao;
import static tech.postgres.springbootDbOps.enums.BOUND.END;
import static tech.postgres.springbootDbOps.enums.BOUND.START;
import tech.postgres.springbootDbOps.enums.PostType;
import tech.postgres.springbootDbOps.model.Posts;

@Log4j2
@Service
public class PostService {

  private final PostDao postDao;

  private final Faker faker;

  public PostService (PostDao postDao, Faker faker) {
    this.postDao = postDao;
    this.faker = faker;
  }


  //insert users data;
  @Transactional
  public void insertPostData (Map<String, BigInteger> userBound) {

    var userStartIndex = userBound.get (START.name ());

    var userEndIndex = userBound.get (END.name ());

    var latestIndex = findLatestIndex ().get (END.name ());

    var postTypeArray = List.of (
        PostType.MEME.name (),
        PostType.NEWS.name (),
        PostType.POLITICAL.name (),
        PostType.SOCIAL.name (),
        PostType.SPACE.name (),
        PostType.RELIGIOUS.name (),
        PostType.TECHNOLOGY.name (),
        PostType.SPORTS.name ()
                                );

    LongStream.rangeClosed (latestIndex.longValue (), 3000)
        .forEach (i -> {
          Posts post = Posts.builder ()
              .userId (BigInteger.valueOf (new Random ().nextLong (
                  userStartIndex.longValue (),
                  userEndIndex.longValue ()
                                                                  )))
              .contentType (postTypeArray.get (new Random ().nextInt (1, 8)))
              .created_time (Date.from (Instant.now ()))
              .updated_time (Date.from (Instant.now ()))
              .build ();
          try {
            postDao.save (post);
          }
          catch (DataIntegrityViolationException e) {
            System.err.println (
                "Duplicate key violation, skipping post with user ID: " + post.getUserId ());
          }
          catch (Exception e) {
            log.warn ("Error while saving data: {}: exception: {}", post, e.getMessage ());
          }
        });
  }

  public Map<String, BigInteger> findLatestIndex () {

    var list = Optional.of (postDao.findAll ()).orElse (Collections.emptyList ());

    if (list.isEmpty ()) {
      return Map.of (START.name (), BigInteger.ZERO, END.name (), BigInteger.ZERO);
    }

    var sortedPost =
        list.stream ().sorted (Comparator.comparing (Posts::getUserId)).collect (
            Collectors.toCollection (LinkedHashSet::new));

    return Map.of (
        START.name (),
        sortedPost.getFirst ().getUserId (),
        END.name (),
        sortedPost.getLast ().getUserId ()
                  );

  }
}
