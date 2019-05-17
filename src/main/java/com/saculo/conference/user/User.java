package com.saculo.conference.user;

import com.saculo.conference.lecture.Lecture;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID uuid = UUID.randomUUID();
    private String login;
    private String email;
    @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinTable(name = "user_lecture",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "lecture_id")
    )
    private List<Lecture> lectures = new ArrayList<>();

    public User(String login, String email) {
        this.login = login;
        this.email = email;
    }

    public Lecture addLecture(Lecture lecture) {
        this.lectures.add(lecture);
        lecture.getUsers().add(this);

        return lecture;
    }

    public void removeLecture(Lecture lecture) {
        this.lectures.remove(lecture);
        lecture.getUsers().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getUuid(), user.getUuid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUuid());
    }
}

