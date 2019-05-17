package com.saculo.conference.ui;

import com.saculo.conference.lecture.Lecture;
import com.saculo.conference.lecture.LectureDao;
import com.saculo.conference.user.User;
import com.saculo.conference.user.UserDao;
import com.saculo.conference.user.UserService;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;

@SpringUI
public class MainUI extends UI {

    private UserService userService;
    private LectureDao lectureDao;
    private UserDao userDao;

    private Grid<Lecture> users = new Grid<>();
    private ComboBox<Lecture> select = new ComboBox<>("My Select");
    private TextField emailEdit = new TextField("Email");
    private TextField userEmail = new TextField("Email");
    private VerticalLayout root;

    public MainUI(LectureDao lectureDao, UserDao userDao, UserService userService) {
        this.lectureDao = lectureDao;
        this.userDao = userDao;
        this.userService = userService;
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        addLayout();
        addLoginForm();
        createBookingForm();
        showUserLectures();
        createEmailEditForm();
    }

    private void createEmailEditForm() {
        if(userService.getLoggedUser() != null) {
            HorizontalLayout emailEditForm = new HorizontalLayout();
            Button email = new Button("Change email");
            emailEditForm.addComponents(emailEdit, email);
            emailEdit.setValue(userService.getLoggedUser().getEmail());
            root.addComponent(emailEditForm);
            email.addClickListener(click -> {
                userService.changeUserEmail(emailEdit.getValue());
                refreshEmail();
            });
        }

    }

    private void refreshEmail() {
        String loggedUserEmail = userService.getLoggedUser().getEmail();
        emailEdit.setValue(loggedUserEmail);
        userEmail.setValue(loggedUserEmail);
    }

    void showUserLectures() {
        if(userService.getLoggedUser() != null)
            createUserLecturesGrid(userService.getLoggedUser().getLogin());
    }

    private void addLayout() {
        root = new VerticalLayout();
        root.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        setContent(root);
    }

    private void createUserLecturesGrid(String login) {
        users.removeAllColumns();
        if (userService.getLoggedUser() != null) {
            users.setItems(userService.getLoggedUser().getLectures());
            users.addColumn(Lecture::getTitle).setCaption("LECTURES");
            users.addComponentColumn(lecture -> createDeleteButton(login, userService.getLoggedUser(), lecture));

            root.replaceComponent(users, users);
        }
        else {
            Notification.show("Login nie istnieje");
            root.removeComponent(users);
        }
    }

    private void createBookingForm() {
        VerticalLayout bookingForm = new VerticalLayout();
        TextField userLogin = new TextField("Login");
        Button book = new Button("Book");

        if(userService.getLoggedUser() != null) {
            userLogin.setValue(userService.getLoggedUser().getLogin());
            userEmail.setValue(userService.getLoggedUser().getEmail());
            userLogin.setEnabled(false);
            userEmail.setEnabled(false);
            select.setItems(userService.getUserPossibleLectures(userService.getLoggedUser().getLogin()));
            book.addClickListener(click -> {
                userService.addLectureToUser(userService.getLoggedUser().getLogin(), select.getValue());
                Page.getCurrent().reload();
            });
        }
        else {
            select.setItems(lectureDao.findAll());
            book.addClickListener(click -> {
                if(userDao.existsByLogin(userLogin.getValue())) {
                    Notification.show("Login is already taken");
                }
                else {
                    User user = new User(userLogin.getValue(), userEmail.getValue());
                    userDao.save(user);
                    userService.setLoggedUser(user.getLogin());
                    userService.addLectureToUser(userService.getLoggedUser().getLogin(), select.getValue());
                    Page.getCurrent().reload();
                }
            });
        }

        bookingForm.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        select.setItemCaptionGenerator(Lecture::getTitle);
        select.setTextInputAllowed(false);
        select.setPlaceholder("CHOOSE LECTURE");

        bookingForm.addComponents(userLogin, userEmail, select, book);

        root.addComponent(bookingForm);
    }

    private void addLoginForm() {
        HorizontalLayout loginLayout = new HorizontalLayout();

        TextField loginInput = new TextField();
        Button login = new Button("Login");

        loginLayout.addComponents(login, loginInput);

        login.addClickListener(clickEvent -> {
            if(userDao.existsByLogin(loginInput.getValue())) {
                userService.setLoggedUser(loginInput.getValue());
            }
            Page.getCurrent().reload();
        });

        root.addComponent(loginLayout);
    }

    private Button createDeleteButton(String login, User user, Lecture lecture) {
        Button button = new Button("Delete!");
        button.addClickListener(click -> {
            ListDataProvider<Lecture> dataProvider = (ListDataProvider<Lecture>) users
                    .getDataProvider();
            userService.removeLectureFromUser(user.getLogin(), lecture);
            dataProvider.getItems().remove(lecture);
            dataProvider.refreshAll();
            select.setPlaceholder("CHOOSE LECTURE");
            select.setItems(userService.getUserPossibleLectures(userService.getLoggedUser().getLogin()));
        });
        return button;
    }
}
