package com.matt1479.jwtsecuredrestapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.matt1479.jwtsecuredrestapi.config.RsaKeyProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class JwtSecuredRestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtSecuredRestApiApplication.class, args);
	}

}
