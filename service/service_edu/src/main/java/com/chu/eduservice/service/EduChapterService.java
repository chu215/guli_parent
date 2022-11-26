package com.chu.eduservice.service;

import com.chu.eduservice.entity.EduChapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chu.eduservice.entity.chapter.ChapterVo;
import com.chu.eduservice.entity.vo.CourseInfoVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author chu
 * @since 2022-11-23
 */
public interface EduChapterService extends IService<EduChapter> {

    List<ChapterVo> getChapterVideoByCourseId(String courseId);

    void deleteChapter(String chapterId);

    void removeChapterByCourseId(String courseId);
}
