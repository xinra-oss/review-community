package com.xinra.reviewcommunity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.xinra.reviewcommunity.entity.User;
import com.xinra.reviewcommunity.entity.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class TestUserController {

  private @Autowired MockMvc mvc;
  private @Autowired UserRepository<User> userRepo;
  
  @Test
  public void createUserTwice() throws Exception {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("username", "peter");
    params.add("password", "secret");
    
    // creating a user should work
    mvc.perform(post("/de/api/user").params(params).with(csrf()))
        .andExpect(status().isOk());
    
    assertThat(userRepo.getByName("peter"))
        .as("create user entity")
        .isNotNull();
    
    // creating a user with the same name again should fail
    mvc.perform(post("/de/api/user").params(params).with(csrf()))
        .andExpect(status().isBadRequest());
    
    userRepo.deleteAll();
  }
  
}