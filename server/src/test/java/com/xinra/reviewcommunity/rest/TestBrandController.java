package com.xinra.reviewcommunity.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.xinra.reviewcommunity.entity.Brand;
import com.xinra.reviewcommunity.repo.BrandRepository;
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
public class TestBrandController {

  private @Autowired MockMvc mvc;
  private @Autowired BrandRepository<Brand> brandRepo;

  @Test
  public void createBrand() throws Exception {

    String content = "{ \"name\": \"Bosch\" } ";

    mvc.perform(post("/de/api/brand").contentType("application/json").content(content)
        .with(authentication(new TestingAuthenticationToken("jon", "snow", "CREATE_BRAND")))
        .with(csrf()))
        .andExpect(status().isOk());

    assertThat(brandRepo.findBySerial(3))
        .as("create product entity")
        .isNotNull();
  }

  @Test
  public void getBrands() throws Exception {
    mvc.perform(get("/de/api/brand")).andExpect(status().isOk());
  }

  @Test
  public void getProductsByBrand() throws Exception {
    mvc.perform(get("/de/api/brand/1")).andExpect(status().isOk());
  }
}
