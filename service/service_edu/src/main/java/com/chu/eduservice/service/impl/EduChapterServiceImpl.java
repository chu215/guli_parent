package com.chu.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chu.eduservice.entity.EduChapter;
import com.chu.eduservice.entity.EduVideo;
import com.chu.eduservice.entity.chapter.ChapterVo;
import com.chu.eduservice.entity.chapter.VideoVo;
import com.chu.eduservice.entity.vo.CourseInfoVo;
import com.chu.eduservice.mapper.EduChapterMapper;
import com.chu.eduservice.service.EduChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chu.eduservice.service.EduVideoService;
import com.chu.servicebase.exceptionhandler.GuliException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author chu
 * @since 2022-11-23
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService videoService;

    @Override
    public List<ChapterVo> getChapterVideoByCourseId(String courseId) {

        QueryWrapper<EduChapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.eq("course_id", courseId);
        chapterQueryWrapper.orderByAsc("sort");
        List<EduChapter> chapterList = this.list(chapterQueryWrapper);

        List<ChapterVo> list = new ArrayList<>();
        for (EduChapter chapter : chapterList) {
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(chapter, chapterVo);

            QueryWrapper<EduVideo> videoQueryWrapper = new QueryWrapper<>();
            videoQueryWrapper.eq("course_id", courseId);
            videoQueryWrapper.eq("chapter_id", chapterVo.getId());
            videoQueryWrapper.orderByAsc("sort");
            List<EduVideo> videoList = videoService.list(videoQueryWrapper);

            List<VideoVo> children = new ArrayList<>();
            for (EduVideo video : videoList) {
                VideoVo videoVo = new VideoVo();
                BeanUtils.copyProperties(video, videoVo);

                children.add(videoVo);
            }

            chapterVo.setChildren(children);
            list.add(chapterVo);
        }
        return list;
    }

    @Override
    public void deleteChapter(String chapterId) {
        QueryWrapper<EduVideo> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("chapter_id", chapterId);
        int count = videoService.count(videoQueryWrapper);
        if (count > 0) {
            throw new GuliException(20001, "不能删除");
        } else {
            this.removeById(chapterId);
        }
    }

    @Override
    public void removeChapterByCourseId(String courseId) {
        QueryWrapper<EduChapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.eq("course_id", courseId);
        remove(chapterQueryWrapper);
    }
}
