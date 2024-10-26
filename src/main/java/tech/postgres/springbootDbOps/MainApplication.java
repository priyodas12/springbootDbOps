package tech.postgres.springbootDbOps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.github.javafaker.Faker;

import tech.postgres.springbootDbOps.service.AppUserService;
import tech.postgres.springbootDbOps.service.CommentService;
import tech.postgres.springbootDbOps.service.PhotoService;
import tech.postgres.springbootDbOps.service.PostService;
import tech.postgres.springbootDbOps.service.ReactionService;

@ComponentScan (basePackages = "tech.postgres.springbootDbOps")
@SpringBootApplication
public class MainApplication {

  @Autowired
  private AppUserService appUserService;
  @Autowired
  private PostService postService;
  @Autowired
  private ReactionService reactionService;
  @Autowired
  private PhotoService photoService;
  @Autowired
  private CommentService commentService;

  public static void main (String[] args) {
    SpringApplication.run (MainApplication.class, args);
  }

  @Bean
  CommandLineRunner initData () {
    return args -> {
      appUserService.insertUserData ();
      var userBound = appUserService.findLatestIndex ();
      postService.insertPostData (userBound);
      var postBound = postService.findLatestIndex ();
      photoService.insertPhotoData (userBound, postBound);
      commentService.insertCommentData (userBound, postBound);
      reactionService.insertReactionData (userBound, postBound);
    };
  }

  @Bean
  public Faker faker () {
    return Faker.instance ();
  }
}
