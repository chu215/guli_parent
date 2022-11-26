package com.chu.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chu.commonutils.R;
import com.chu.eduservice.entity.EduCourse;
import com.chu.eduservice.entity.vo.CourseInfoVo;
import com.chu.eduservice.entity.vo.CoursePublishVo;
import com.chu.eduservice.entity.vo.CourseQuery;
import com.chu.eduservice.service.EduCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author chu
 * @since 2022-11-23
 */
@RestController
@RequestMapping("/eduservice/course")
@CrossOrigin
public class EduCourseController {

    @Autowired
    private EduCourseService courseService;

    //条件查询带分页
    @PostMapping("/pageCourseCondition/{current}/{limit}")
    public R pageCourseCondition(
            @PathVariable("current") long current,
            @PathVariable("limit") long limit,
            @RequestBody(required = false) CourseQuery courseQuery) {
        Page<EduCourse> coursePage = new Page<>(current, limit);
        QueryWrapper<EduCourse> courseQueryWrapper = new QueryWrapper<>();

        String title = courseQuery.getTitle();
        String status = courseQuery.getStatus();
        if (!StringUtils.isEmpty(title)) {
            courseQueryWrapper.like("title", title);
        }
        if (!StringUtils.isEmpty(status)) {
            courseQueryWrapper.eq("status", status);
        }

        IPage<EduCourse> page = courseService.page(coursePage, courseQueryWrapper);
        List<EduCourse> list = page.getRecords();

        return R.ok().data("list", list);
    }


    @GetMapping()
    public R getCourseList() {
        List<EduCourse> list = courseService.list(null);
        return R.ok().data("list", list);
    }

    @PostMapping("/addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfoVo courseInfoVo) {
        String id = courseService.saveCourseInfo(courseInfoVo);
        return R.ok().data("courseId", id);
    }

    @GetMapping("/getCourseInfo/{courseId}")
    public R getCourseInfo(@PathVariable("courseId") String courseId) {
        CourseInfoVo courseInfoVo = courseService.getCourseInfo(courseId);

        return R.ok().data("courseInfoVo", courseInfoVo);
    }

    @PostMapping("/updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo) {
        courseService.updateCourseInfo(courseInfoVo);
        return R.ok();
    }

    @GetMapping("/getPublishCourseInfo/{courseId}")
    public R getPublishCourseInfo(@PathVariable String courseId) {
        CoursePublishVo coursePublishVo = courseService.publishCourseInfo(courseId);
        return R.ok().data("publishCourse", coursePublishVo);
    }

    @PostMapping("/publishCourse/{id}")
    public R publishCourse(@PathVariable String id) {
        EduCourse course = new EduCourse();
        course.setId(id);
        course.setStatus("Normal");

        // 覆盖原来的status
        courseService.updateById(course);
        return R.ok();
    }

    @DeleteMapping("/{courseId}")
    public R deleteCourse(@PathVariable String courseId) {
        courseService.removeCourse(courseId);
        return R.ok();
    }
}

