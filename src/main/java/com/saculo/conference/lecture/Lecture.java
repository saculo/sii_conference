package com.saculo.conference.lecture;

import com.saculo.conference.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID uuid = UUID.randomUUID();
    private String title;
    private LocalDateTime startsAt;
    @ManyToMany(mappedBy = "lectures", fetch = FetchType.EAGER)
    private List<User> users = new ArrayList<>();

    public Lecture(String title, LocalDateTime startsAt) {
        this.title = title;
        this.startsAt = startsAt;
    }

    @Override
    public String toString() {
        return "Lecture{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", startsAt=" + startsAt +
                '}';
    }

    public boolean isNotFull() {
        return users.size() < 5;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lecture)) return false;
        Lecture lecture = (Lecture) o;
        return Objects.equals(getUuid(), lecture.getUuid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUuid());
    }

}
