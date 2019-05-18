package com.saculo.conference;

import com.saculo.conference.lecture.Lecture;
import com.saculo.conference.lecture.LectureDao;
import com.saculo.conference.user.User;
import com.saculo.conference.user.UserDao;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class ConferenceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConferenceApplication.class, args);
	}

    @Bean
    CommandLineRunner runner(LectureDao lectureDao, UserDao userDao) {
        return  args -> {
            List<Lecture> lectures = Arrays.asList(
                    new Lecture("Lecture1", LocalDateTime.of(2019, 6, 1, 10, 0)),
                    new Lecture("Lecture2", LocalDateTime.of(2019, 6, 1, 10, 0)),
                    new Lecture("Lecture3", LocalDateTime.of(2019, 6, 1, 10, 0)),
                    new Lecture("Lecture4", LocalDateTime.of(2019, 6, 1, 12, 0)),
                    new Lecture("Lecture5", LocalDateTime.of(2019, 6, 1, 12, 0)),
                    new Lecture("Lecture6", LocalDateTime.of(2019, 6, 1, 12, 0)),
                    new Lecture("Lecture7", LocalDateTime.of(2019, 6, 2, 10, 0)),
                    new Lecture("Lecture8", LocalDateTime.of(2019, 6, 2, 10, 0)),
                    new Lecture("Lecture9", LocalDateTime.of(2019, 6, 2, 10, 0)),
                    new Lecture("Lecture10", LocalDateTime.of(2019, 6, 2, 12, 0)),
                    new Lecture("Lecture11", LocalDateTime.of(2019, 6, 2, 12, 0)),
                    new Lecture("Lecture12", LocalDateTime.of(2019, 6, 2, 12, 0))

            );
            List<User> users = Arrays.asList(
                    new User("login1", "email@gmail.com"),
                    new User("login2", "email2@gmail.com")
            );
            users.get(0).addLecture(lectures.get(2));
            users.get(1).addLecture(lectures.get(5));
            userDao.saveAll(users);
            lectureDao.saveAll(lectures);
        };
    }

}
