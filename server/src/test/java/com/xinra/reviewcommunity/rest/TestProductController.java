package com.xinra.reviewcommunity.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.xinra.reviewcommunity.SampleContentGenerator;
import com.xinra.reviewcommunity.entity.Product;
import com.xinra.reviewcommunity.repo.ProductRepository;
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
public class TestProductController {

  private @Autowired MockMvc mvc;
  private @Autowired ProductRepository<Product> productRepo;
  private @Autowired SampleContentGenerator sample;

  @Test
  public void createProduct() throws Exception {

    String content = "{ "
        + "    \"name\": \"Oreo\", "
        + "    \"description\": \"they're cookies\","
        + "    \"categorySerial\": 1,"
        + "    \"brandSerial\": 1"
        + "}";

    // creating a product should work
    mvc.perform(post("/de/api/product").contentType("application/json").content(content)
        .with(authentication(AuthenticationProviderImpl.getAuthentication(sample.user)))
        .with(csrf()))
        .andExpect(status().isOk());

    assertThat(productRepo.findBySerial(1))
        .as("create product entity")
        .isNotNull();
  }

  @Test
  public void getProduct() throws Exception {
    mvc.perform(get("/de/api/product/1")).andExpect(status().isOk());
  }

}
