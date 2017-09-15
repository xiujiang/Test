package com.digibig.saaserp.person;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableFeignClients
@EnableTransactionManagement
@MapperScan("com.digibig.saaserp.person.mapper")
@EnableDiscoveryClient
@SpringBootApplication
public class PersonServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PersonServiceApplication.class, args);
	}
}
