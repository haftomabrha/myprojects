package com.muluneh.MUCSchool.service;

import com.muluneh.MUCSchool.domain.Account;
import com.muluneh.MUCSchool.domain.Schedule;
import com.muluneh.MUCSchool.domain.Section;
import com.muluneh.MUCSchool.domain.Teacher;
import com.muluneh.MUCSchool.dto.ScheduleDto;
import com.muluneh.MUCSchool.repository.AccountRepository;
import com.muluneh.MUCSchool.repository.ScheduleRepository;
import com.muluneh.MUCSchool.repository.SectionRepository;
import com.muluneh.MUCSchool.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private SectionRepository sectionRepository;
    public List<Schedule> setSchedule(ScheduleDto scheduleDto){
        Optional<Account> accountOptional=accountRepository.findByUsername(scheduleDto.getUsername());
        Account account=accountOptional.get();
        Optional<Teacher> teacherOptional = teacherRepository.findByAccount(account);
            Teacher teacher=teacherOptional.get();
                Schedule schedule=new Schedule();
                schedule.setTeacher(teacher);
                schedule.setDay(scheduleDto.getDay());
                schedule.setPeriod(scheduleDto.getPeriod());
                schedule.setSection(scheduleDto.getSection());
                scheduleRepository.save(schedule);

        return scheduleRepository.findAll();

    }
    public List<Section>getTeachersAndSections(){

        return sectionRepository.findAll();
    }
    public List<Schedule>getTeacherSchedule(Account account){
       Optional<Teacher> teacherOptional=teacherRepository.findByAccount(account);
       if(teacherOptional.isPresent()) {
           Teacher teacher = teacherOptional.get();
           List<Schedule> schedules = scheduleRepository.findByTeacherId(teacher.getId());
           return schedules;
       }
       else{
           return new ArrayList<>();
       }
    }

}
