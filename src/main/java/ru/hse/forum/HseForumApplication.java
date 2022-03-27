package ru.hse.forum;

import ru.hse.forum.config.SwaggerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@Import(SwaggerConfiguration.class)
public class HseForumApplication {
    public static void main(String[] args) {
        SpringApplication.run(HseForumApplication.class, args);
    }
}
