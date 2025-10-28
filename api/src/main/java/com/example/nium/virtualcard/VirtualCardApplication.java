package com.example.nium.virtualcard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
//@ComponentScan(basePackages = {"com.example.nium.virtualcard.core"})
//@EntityScan(basePackages = "com.example.nium.virtualcard.core.entity")
//@EnableJpaRepositories(basePackages = "com.example.nium.virtualcard.core.repository")
public class VirtualCardApplication {

	public static void main(String[] args) {
		SpringApplication.run(VirtualCardApplication.class, args);
	}

}
