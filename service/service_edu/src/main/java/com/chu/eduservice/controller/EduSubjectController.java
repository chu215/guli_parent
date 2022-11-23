package com.chu.eduservice.controller;


import com.chu.commonutils.R;
import com.chu.eduservice.entity.subject.OneSubject;
import com.chu.eduservice.service.EduSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author chu
 * @since 2022-11-22
 */
@RestController
@RequestMapping("/eduservice/subject")
@CrossOrigin
public class EduSubjectController {

    @Autowired
    private EduSubjectService subjectService;

    @PostMapping("/addSubject")
    public R addSubject(MultipartFile file) {

        subjectService.saveSubject(file, subjectService);
        return R.ok();
    }

    @GetMapping("/getAllSubject")
    public R getAllSubject() {
        List<OneSubject> list = subjectService.getAllOneTwoSubject();
        return R.ok().data("list", list);
    }
}

