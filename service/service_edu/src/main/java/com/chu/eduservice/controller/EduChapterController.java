package com.chu.eduservice.controller;


import com.chu.commonutils.R;
import com.chu.eduservice.entity.EduChapter;
import com.chu.eduservice.entity.chapter.ChapterVo;
import com.chu.eduservice.entity.vo.CourseInfoVo;
import com.chu.eduservice.service.EduChapterService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/eduservice/chapter")
@CrossOrigin
public class EduChapterController {

    @Autowired
    private EduChapterService chapterService;

    @GetMapping("/getChapterVideo/{courseId}")
    public R getChapterVideo(@PathVariable("courseId") String courseId) {
        List<ChapterVo> list = chapterService.getChapterVideoByCourseId(courseId);
        return R.ok().data("allChapterVideo", list);
    }

    @PostMapping("/addChapter")
    public R addChapter(@RequestBody EduChapter chapter) {
        chapterService.save(chapter);
        return R.ok();
    }

    @GetMapping("/getChapterInfo/{chapterId}")
    public R getChapterInfo(@PathVariable String chapterId) {
        EduChapter chapter = chapterService.getById(chapterId);
        return R.ok().data("chapter", chapter);
    }

    @PostMapping("/updateChapter")
    public R updateChapter(@RequestBody EduChapter chapter) {
        chapterService.updateById(chapter);
        return R.ok();
    }

    @DeleteMapping("/{chapterId}")
    public R deleteChapter(@PathVariable String chapterId) {
        chapterService.deleteChapter(chapterId);
        return R.ok();
    }
}

