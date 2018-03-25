package com.nguyen.wechat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@Slf4j
@SpringBootApplication
public class WechatApplication implements CommandLineRunner {

    @Autowired
    private Environment env;

	public static void main(String[] args) {
		SpringApplication.run(WechatApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
        log.info("wechat service on at {} environment", env.getActiveProfiles());
	}
}
