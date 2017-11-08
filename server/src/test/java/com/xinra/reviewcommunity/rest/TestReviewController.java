package com.xinra.reviewcommunity.rest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.xinra.reviewcommunity.entity.Review;
import com.xinra.reviewcommunity.repo.ReviewRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class TestReviewController {

  private @Autowired MockMvc mvc;
  private @Autowired ReviewRepository<Review> reviewRepo;

  @Test
  public void createReview() throws Exception {

    String content = "{" +
            "\"title\": \"My review.\"," +
            "\"rating\": 4," +
            "\"text\": \"I like this product sooo much, bla bla bla...\"" +
            "}";

    mvc.perform(post("/de/api/product/1/review").contentType("application/json").content(content)
            .with(authentication(new TestingAuthenticationToken("jon", "snow", "CREATE_REVIEW")))
            .with(csrf()))
            .andExpect(status().isOk());
  }
  //TODO write remaining tests
}
