package com.xinra.reviewcommunity.frontend;

import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class RegisterDto {

  @NotBlank
  @Length(min = 3, max = 15)
  @Pattern(regexp = "[a-zA-Z][a-zA-Z0-9_-]*")
  private String username;
  
  @Email
  private String email;
  
  @NotBlank
  private String password;
  
}
