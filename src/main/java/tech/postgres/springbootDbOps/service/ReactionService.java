package tech.postgres.springbootDbOps.service;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import tech.postgres.springbootDbOps.dao.ReactionDao;
import static tech.postgres.springbootDbOps.enums.BOUND.END;
import static tech.postgres.springbootDbOps.enums.BOUND.START;
import tech.postgres.springbootDbOps.enums.ReactionType;
import tech.postgres.springbootDbOps.model.Reactions;

@Log4j2
@Service
public class ReactionService {

  private final ReactionDao reactionDao;

  public ReactionService (ReactionDao reactionDao) {
    this.reactionDao = reactionDao;
  }

  //insert reactions data;
  public void insertReactionData (
      Map<String, BigInteger> userBound, Map<String, BigInteger> postBound
                                 ) {

    var userStartIndex = userBound.get (START.name ());

    var userEndIndex = userBound.get (END.name ());

    var postStartIndex = postBound.get (START.name ());

    var postEndIndex = postBound.get (END.name ());

    var reactionTypes = List.of (
        ReactionType.ANGRY.name (),
        ReactionType.SAD.name (),
        ReactionType.LAUGH.name (),
        ReactionType.UP_VOTE.name (),
        ReactionType.DOWN_VOTE.name (),
        ReactionType.LIKE.name (),
        ReactionType.LOVE.name ()
                                );
    IntStream.rangeClosed (1, 30000)
        .forEach (i -> {
          Reactions reaction = Reactions.builder ()
              .userId (BigInteger.valueOf (
                  new Random ().nextLong (userStartIndex.longValue (), userEndIndex.longValue ())))
              .postId (BigInteger.valueOf (
                  new Random ().nextLong (postStartIndex.longValue (), postEndIndex.longValue ())))
              .reactionType (reactionTypes.get (new Random ().nextInt (1, 7)))
              .created_time (Date.from (Instant.now ()))
              .updated_time (Date.from (Instant.now ()))
              .build ();

          try {
            reactionDao.saveAndFlush (reaction);
          }
          catch (DataIntegrityViolationException e) {
            System.err.println (
                "Duplicate key violation, skipping reaction for post ID: " + reaction.getPostId ()
                + " and user ID: " + reaction.getUserId ());
          }
          catch (Exception e) {
            log.warn ("Error while saving data: {}: exception: {}", reaction, e.getMessage ());
          }
        });
  }
}
