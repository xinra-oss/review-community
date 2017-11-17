package com.xinra.reviewcommunity.rest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.xinra.reviewcommunity.SampleContentGenerator;
import com.xinra.reviewcommunity.service.AuthenticationProviderImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class TestReviewController {

  private @Autowired MockMvc mvc;
  private @Autowired SampleContentGenerator sample;
  
  // TODO confirm actions at database level!

  @Test
  public void createReview() throws Exception {

    String content = "{"
        + "\"title\": \"My review.\","
        + "\"rating\": 4,"
        + "\"text\": \"I like this product sooo much, bla bla bla...\""
        + "}";

    mvc.perform(post("/de/api/product/1/review").contentType("application/json").content(content)
      .with(authentication(AuthenticationProviderImpl.getAuthentication(sample.user)))
      .with(csrf()))
      .andExpect(status().isOk());
  }

  @Test
  public void getAllReviews() throws Exception {
    mvc.perform(get("/de/api/product/3/review?orderBy=RATING")
            .with(authentication(AuthenticationProviderImpl.getAuthentication(sample.moderator)))
            .with(csrf()))
            .andExpect(status().isOk());
  }

  @Test
  public void deleteReview() throws Exception {
    mvc.perform(delete("/de/api/product/3/review/2")
            .with(authentication(AuthenticationProviderImpl.getAuthentication(sample.moderator)))
            .with(csrf()))
            .andExpect(status().isOk());
  }

  @Test
  public void createReviewComment() throws Exception {
    String content = "{\"text\": \"Wow, thank you for your Review.\"}";

    mvc.perform(post("/de/api/product/1/review").contentType("application/json").content(content)
            .with(authentication(AuthenticationProviderImpl.getAuthentication(sample.user)))
            .with(csrf()))
            .andExpect(status().isOk());
  }

  @Test
  public void getAllReviewComments() throws Exception {
    mvc.perform(get("/de/api/product/3/review/1/comment")
            .with(authentication(AuthenticationProviderImpl.getAuthentication(sample.moderator))))
            .andExpect(status().isOk());
  }

  @Test
  public void deleteComment() throws Exception {
    mvc.perform(delete("/de/api/product/3/review/1/comment/1")
            .with(authentication(AuthenticationProviderImpl.getAuthentication(sample.moderator)))
            .with(csrf()))
            .andExpect(status().isOk());
  }

  @Test
  public void vote() throws Exception {
    String content = "{ \"upvote\": true }";

    mvc.perform(post("/de/api/product/3/review/1/vote").contentType("application/json")
            .content(content)
            .with(authentication(AuthenticationProviderImpl.getAuthentication(sample.user)))
            .with(csrf()))
            .andExpect(status().isOk());
  }
}
