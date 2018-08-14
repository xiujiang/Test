package com.digibig.service.person;

import com.digibig.config.Config;
import com.digibig.config.LogUtil;
import org.apache.logging.log4j.core.lookup.MainMapLookup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableFeignClients
@EnableTransactionManagement
@SpringBootApplication(scanBasePackageClasses = {PersonServiceApplication.class, Config.class})
public class PersonServiceApplication {

	public static void main(String[] args) {
		LogUtil.config();
		SpringApplication.run(PersonServiceApplication.class, args);
	}
}
