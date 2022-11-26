package com.chu.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chu.eduservice.entity.EduChapter;
import com.chu.eduservice.entity.EduCourse;
import com.chu.eduservice.entity.EduCourseDescription;
import com.chu.eduservice.entity.EduVideo;
import com.chu.eduservice.entity.vo.CourseInfoVo;
import com.chu.eduservice.entity.vo.CoursePublishVo;
import com.chu.eduservice.mapper.EduCourseMapper;
import com.chu.eduservice.service.EduChapterService;
import com.chu.eduservice.service.EduCourseDescriptionService;
import com.chu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chu.eduservice.service.EduVideoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author chu
 * @since 2022-11-23
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Autowired
    private EduCourseDescriptionService courseDescriptionService;

    @Autowired
    private EduVideoService videoService;

    @Autowired
    private EduChapterService chapterService;

    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo, eduCourse);
        this.save(eduCourse);

        String cid = eduCourse.getId();

        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        BeanUtils.copyProperties(courseInfoVo, eduCourseDescription);
        eduCourseDescription.setId(cid);
        courseDescriptionService.save(eduCourseDescription);

        return cid;
    }

    @Override
    public CourseInfoVo getCourseInfo(String courseId) {
        EduCourse course = this.getById(courseId);

        EduCourseDescription description = courseDescriptionService.getById(courseId);

        CourseInfoVo courseInfoVo = new CourseInfoVo();
        BeanUtils.copyProperties(course, courseInfoVo);
        BeanUtils.copyProperties(description, courseInfoVo);

        return courseInfoVo;
    }

    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {
        EduCourse course = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo, course);

        this.updateById(course);

        EduCourseDescription description = new EduCourseDescription();
        BeanUtils.copyProperties(courseInfoVo, description);
        courseDescriptionService.updateById(description);
    }

    @Override
    public CoursePublishVo publishCourseInfo(String courseId) {
        CoursePublishVo coursePublishVo = baseMapper.getPublishCourseInfo(courseId);
        return coursePublishVo;
    }

    @Override
    public void removeCourse(String courseId) {
        // 1.删除小节
        videoService.removeVideoByCourseId(courseId);

        // 2.删除章节
        chapterService.removeChapterByCourseId(courseId);

        // 3.删除描述
        courseDescriptionService.removeById(courseId);

        // 4.删除课程
        this.removeById(courseId);
    }
}
