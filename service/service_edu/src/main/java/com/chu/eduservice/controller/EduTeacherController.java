package com.chu.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chu.commonutils.R;
import com.chu.eduservice.entity.EduTeacher;
import com.chu.eduservice.entity.vo.TeacherQuery;
import com.chu.eduservice.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-11-17
 */
@RestController
@RequestMapping("/eduservice/teacher")
@CrossOrigin
public class EduTeacherController {

    @Autowired
    private EduTeacherService teacherService;

    @GetMapping("/findAll")
    public R findAllTeacher() {
        List<EduTeacher> list = teacherService.list(null);

        return R.ok().data("items", list);
    }

    @DeleteMapping("/{id}")
    public R removeById(@PathVariable("id") String id) {
        boolean flag = teacherService.removeById(id);

        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @GetMapping("/pageTeacher/{current}/{limit}")
    public R pageList(@PathVariable("current") long current, @PathVariable("limit") long limit) {

        Page<EduTeacher> teacherPage = new Page<>(current, limit);
        teacherService.page(teacherPage, null);

        List<EduTeacher> records = teacherPage.getRecords();
        long total = teacherPage.getTotal();

        return R.ok().data("total", total).data("rows", records);
    }

    @PostMapping("/pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(@PathVariable("current") long current,
                                  @PathVariable("limit") long limit,
                                  @RequestBody(required = false) TeacherQuery teacherQuery) {
        Page<EduTeacher> page = new Page<>(current, limit);

        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        if (!StringUtils.isEmpty(name)) {
            wrapper.like("name", name);
        }
        if (!StringUtils.isEmpty(level)) {
            wrapper.eq("level", level);
        }
        if (!StringUtils.isEmpty(begin)) {
            wrapper.ge("gmt_create", begin);
        }
        if (!StringUtils.isEmpty(end)) {
            wrapper.le("gmt_create", end);
        }

        wrapper.orderByDesc("gmt_modified");

        teacherService.page(page, wrapper);
        List<EduTeacher> records = page.getRecords();
        long total = page.getTotal();

        return R.ok().data("total", total).data("rows", records);
    }

    @PostMapping("/addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher) {
        boolean save = teacherService.save(eduTeacher);

        if (save) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @GetMapping("/getTeacher/{id}")
    public R getTeacher(@PathVariable("id") String id) {
        EduTeacher teacher = teacherService.getById(id);

        return R.ok().data("teacher", teacher);
    }

    @PostMapping("/updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher) {
        boolean flag = teacherService.updateById(eduTeacher);

        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }
}

