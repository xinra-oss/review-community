package com.xinra.reviewcommunity.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.xinra.reviewcommunity.SampleContentGenerator;
import com.xinra.reviewcommunity.entity.Category;
import com.xinra.reviewcommunity.repo.CategoryRepository;
import com.xinra.reviewcommunity.service.AuthenticationProviderImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class TestCategoryController {


  private @Autowired MockMvc mvc;
  private @Autowired CategoryRepository<Category> categoryRepo;
  private @Autowired SampleContentGenerator sample;

  @Test
  public void createCategory() throws Exception {

    String content = "{ \"name\": \"sweets\", \"parentSerial\": 3 } ";

    mvc.perform(MockMvcRequestBuilders.post("/de/api/category").contentType("application/json")
        .content(content)
        .with(authentication(AuthenticationProviderImpl.getAuthentication(sample.moderator)))
        .with(csrf()))
        .andExpect(status().isOk());

    assertThat(categoryRepo.findBySerial(6))
        .as("create product entity")
        .isNotNull();
  }

  @Test
  public void getAllCategories() throws Exception {
    mvc.perform(get("/de/api/category")).andExpect(status().isOk());
  }

  @Test
  public void getProductsByCategory() throws Exception {
    mvc.perform(get("/de/api/category/3")).andExpect(status().isOk());
  }
}
