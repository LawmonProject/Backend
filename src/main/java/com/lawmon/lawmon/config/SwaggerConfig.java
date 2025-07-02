package com.lawmon.lawmon.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
  @Value("${app.domain}")
  private String domain;

  @Bean
  public OpenAPI openAPI() {
    Server server = new Server()
      .url(domain)
      .description("배포 서버");

    Server localServer = new Server()
      .url("http://localhost:8080")
      .description("로컬 개발 서버");

    return new OpenAPI()
            .components(new Components()
                    .addSecuritySchemes("JWT", new SecurityScheme()
                            .name("JWT")
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")))
            .addSecurityItem(new SecurityRequirement().addList("JWT"))
            .info(apiInfo())
            .servers(List.of(
                    localServer, server)
            );
  }

  private Info apiInfo() {
    return new Info()
            .title("API Test")
            .version("1.0.0");
  }
}