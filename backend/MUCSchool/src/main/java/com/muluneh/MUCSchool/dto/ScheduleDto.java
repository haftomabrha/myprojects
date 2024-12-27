package com.muluneh.MUCSchool.dto;

import com.muluneh.MUCSchool.domain.Teacher;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleDto {
    private String username;
    private String day;
    private Integer period;
    private String section;

}
