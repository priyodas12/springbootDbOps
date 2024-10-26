package tech.postgres.springbootDbOps.service;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.javafaker.Faker;

import lombok.extern.log4j.Log4j2;
import tech.postgres.springbootDbOps.dao.AppUserDao;
import static tech.postgres.springbootDbOps.enums.BOUND.END;
import static tech.postgres.springbootDbOps.enums.BOUND.START;
import tech.postgres.springbootDbOps.model.AppUser;

@Log4j2
@Service
public class AppUserService {

  private final AppUserDao appUserDao;

  private final Faker faker;

  public AppUserService (AppUserDao appUserDao, Faker faker) {
    this.appUserDao = appUserDao;
    this.faker = faker;
  }


  //insert users data;
  @Transactional
  public void insertUserData () {

    var latestIndex = findLatestIndex ();

    var end = latestIndex.get (END.name ());

    LongStream.rangeClosed (end.longValue (), 200L)
        .forEach (i -> {
          AppUser appUser = AppUser.builder ()
              .username (faker.funnyName ().name ())
              .password (RandomStringUtils.randomAlphabetic (32))
              .email (faker.internet ().emailAddress ().toLowerCase ())
              .created_time (Date.from (Instant.now ()))
              .updated_time (Date.from (Instant.now ()))
              .build ();

          try {
            appUserDao.save (appUser);
          }
          catch (Exception e) {
            log.warn ("Error while saving data: {}: exception: {}", appUser, e.getMessage ());
          }
        });
  }

  public Map<String, BigInteger> findLatestIndex () {

    var list = Optional.of (appUserDao.findAll ()).orElse (Collections.emptyList ());

    if (list.isEmpty ()) {
      return Map.of (START.name (), BigInteger.ZERO, END.name (), BigInteger.ZERO);
    }

    var sortedAppUser =
        list.stream ().sorted (Comparator.comparing (AppUser::getUserId)).collect (
            Collectors.toCollection (LinkedHashSet::new));

    return Map.of (
        START.name (),
        sortedAppUser.getFirst ().getUserId (),
        END.name (),
        sortedAppUser.getLast ().getUserId ()
                  );
  }

}
