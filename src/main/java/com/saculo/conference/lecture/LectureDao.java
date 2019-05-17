package com.saculo.conference.lecture;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureDao extends JpaRepository<Lecture, Long> {
}
