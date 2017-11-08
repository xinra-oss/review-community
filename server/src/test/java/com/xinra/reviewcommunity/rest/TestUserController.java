package com.xinra.reviewcommunity.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.xinra.reviewcommunity.entity.User;
import com.xinra.reviewcommunity.repo.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class TestUserController {

  private @Autowired MockMvc mvc;
  private @Autowired UserRepository<User> userRepo;
  
  @Test
  public void createUserTwice() throws Exception {
	String content = "{\"username\": \"testuser\", \"password\": \"secret\"}";
    
    mvc.perform(post("/api/user")
        .contentType("application/json")
        .content(content)
        .with(csrf()))
        .andExpect(status().isOk());
    
    assertThat(userRepo.findByName("testuser"))
        .as("create user entity")
        .isNotNull();
    
    // creating a user with the same name again should fail
    mvc.perform(post("/api/user")
    	.contentType("application/json")
        .content(content)
        .with(csrf()))
        .andExpect(status().isBadRequest());
    
  }
  
}