package com.saculo.conference.user;

import com.saculo.conference.lecture.Lecture;
import com.saculo.conference.lecture.LectureDao;
import com.saculo.conference.user.message.Message;
import com.saculo.conference.user.message.MessageManager;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private LectureDao lectureDao;
    private UserDao userDao;
    private MessageManager messageManager;

    private User loggedUser;

    public UserService(LectureDao lectureDao, UserDao userDao, MessageManager messageManager) {
        this.lectureDao = lectureDao;
        this.userDao = userDao;
        this.messageManager = messageManager;
    }

    public List<Lecture> getUserPossibleLectures(String login) {
        List<LocalDateTime> dates = getUserLectureDates(login);
        if(!dates.isEmpty()) {
            return lectureDao.findAll()
                    .stream()
                    .filter(Lecture::isNotFull)
                    .filter(lecture -> !dates.contains(lecture.getStartsAt()))
                    .collect(Collectors.toList());
        }
        else
            return lectureDao.findAll();
    }

    List<LocalDateTime> getUserLectureDates(String login) {
        return userDao.findByLogin(login)
                .map(User::getLectures)
                .orElse(Collections.emptyList())
                .stream()
                .map(Lecture::getStartsAt)
                .collect(Collectors.toList());
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(String login) {
        userDao.findByLogin(login)
                .ifPresent(user -> loggedUser = user);
    }

    public void addLectureToUser(String login, Lecture lecture) {
        User user = userDao.findByLogin(login).get();
        user.addLecture(lecture);
        userDao.saveAndFlush(user);
        Message message = new Message(
                user.getEmail(), lecture.getTitle() + " " + lecture.getStartsAt(), LocalDateTime.now());
        messageManager.sendMessage(message);
        loggedUser = user;
    }

    public void removeLectureFromUser(String login, Lecture lecture) {
        User user = userDao.findByLogin(login).get();
        user.removeLecture(lecture);
        userDao.saveAndFlush(user);
    }

    public void changeUserEmail(String email) {
        loggedUser.setEmail(email);
        userDao.saveAndFlush(loggedUser);
    }

}
