package com.chu.eduservice.entity.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class TeacherQuery implements Serializable {

    private String name;

    private Integer level;

    private String begin;

    private String end;
}
