package com.xinra.reviewcommunity;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest("reviewcommunity.multi-market-mode=path")
public class TestMultiMarketModePath {

  private @Autowired MockMvc mvc;
  
  @Test
  public void marketAware() throws Exception {
    mvc.perform(get("/de/test/marketAware"))
        .andExpect(status().isOk())
        .andExpect(content().string("de"));
  }
  
  @Test
  public void marketAwareInvalidMarket() throws Exception {
    mvc.perform(get("/es/test/marketAware"))
        .andExpect(status().isNotFound());
  }
  
  @Test
  public void marketAgnostic() throws Exception {
    mvc.perform(get("/test/marketAgnostic"))
        .andExpect(status().isOk());
  }
  
}
