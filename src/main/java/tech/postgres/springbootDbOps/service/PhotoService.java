package tech.postgres.springbootDbOps.service;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.stream.LongStream;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.github.javafaker.Faker;

import lombok.extern.log4j.Log4j2;
import tech.postgres.springbootDbOps.dao.PhotoDao;
import static tech.postgres.springbootDbOps.enums.BOUND.END;
import static tech.postgres.springbootDbOps.enums.BOUND.START;
import tech.postgres.springbootDbOps.model.Photos;

@Log4j2
@Service
public class PhotoService {

  private final PhotoDao photoDao;
  private final Faker faker;

  public PhotoService (PhotoDao photoDao, Faker faker) {
    this.photoDao = photoDao;
    this.faker = faker;
  }

  public void insertPhotoData (
      Map<String, BigInteger> userBound, Map<String, BigInteger> postBound
                              ) {

    var userStartIndex = userBound.get (START.name ());

    var userEndIndex = userBound.get (END.name ());

    var postStartIndex = postBound.get (START.name ());

    var postEndIndex = postBound.get (END.name ());

    LongStream.rangeClosed (1, 15000)
        .forEach (i -> {
          Photos photo = Photos.builder ()
              .userId (BigInteger.valueOf (
                  new Random ().nextLong (userStartIndex.longValue (), userEndIndex.longValue ())))
              .postId (BigInteger.valueOf (
                  new Random ().nextLong (postStartIndex.longValue (), postEndIndex.longValue ())))
              .url (faker.internet ().image ())
              .created_time (Date.from (Instant.now ()))
              .updated_time (Date.from (Instant.now ()))
              .build ();

          try {
            photoDao.save (photo);
          }
          catch (DataIntegrityViolationException e) {
            System.err.println (
                "Duplicate key violation, skipping photo for post ID: " + photo.getPostId ()
                + " and user ID: " + photo.getUserId ());
          }
          catch (Exception e) {
            log.warn ("Error while saving data: {}: exception: {}", photo, e.getMessage ());
          }
        });
  }

}
