package com.app.chatlinks;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PostConstruct;

import java.time.ZoneId;
import java.util.TimeZone;

import static org.springframework.boot.SpringApplication.*;

@SpringBootApplication
@EnableAsync
@EnableJpaRepositories(basePackages = "com.app.chatlinks.mysql.dao")
@EnableMongoRepositories(basePackages = "com.app.chatlinks.bigData")
//@EnableCaching
public class ChatLinks {

	@PostConstruct
	public void started() {
		ZoneId zoneId = ZoneId.of( "Asia/Karachi" );
		TimeZone.setDefault(TimeZone.getTimeZone(zoneId));
	}
	public static void main(String[] args) {
		run(ChatLinks.class, args);
	}
}
