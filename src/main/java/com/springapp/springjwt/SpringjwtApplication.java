package com.springapp.springjwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

import static com.springapp.springjwt.constant.FileConstant.USER_FOLDER;

@SpringBootApplication
public class SpringjwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringjwtApplication.class, args);
		new File(USER_FOLDER).mkdirs();

	}

}
