package com.chu.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chu.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chu.eduservice.entity.frontvo.CourseFrontVo;
import com.chu.eduservice.entity.frontvo.CourseWebVo;
import com.chu.eduservice.entity.vo.CourseInfoVo;
import com.chu.eduservice.entity.vo.CoursePublishVo;

import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author chu
 * @since 2022-11-23
 */
public interface EduCourseService extends IService<EduCourse> {

    String saveCourseInfo(CourseInfoVo courseInfoVo);

    CourseInfoVo getCourseInfo(String courseId);

    void updateCourseInfo(CourseInfoVo courseInfoVo);

    CoursePublishVo publishCourseInfo(String courseId);

    void removeCourse(String courseId);

    Map<String, Object> getCourseFrontList(Page<EduCourse> coursePage, CourseFrontVo courseFrontVo);

    CourseWebVo getBaseCourseInfo(String courseId);
}
