package com.muluneh.MUCSchool.repository;

import com.muluneh.MUCSchool.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule,Integer> {
List<Schedule> findByTeacherId(Integer integer);
}
