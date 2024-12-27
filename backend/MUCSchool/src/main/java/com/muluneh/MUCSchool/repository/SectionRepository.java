package com.muluneh.MUCSchool.repository;

import com.muluneh.MUCSchool.domain.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section, Integer> {
    Optional<Section>findByTeacherIdAndSection(Integer teacherId,String section);
    List<Section> findByTeacherId(Integer teacherId);
}
