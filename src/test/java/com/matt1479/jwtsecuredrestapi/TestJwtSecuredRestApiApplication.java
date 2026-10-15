package com.matt1479.jwtsecuredrestapi;

import org.springframework.boot.SpringApplication;

public class TestJwtSecuredRestApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(JwtSecuredRestApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
