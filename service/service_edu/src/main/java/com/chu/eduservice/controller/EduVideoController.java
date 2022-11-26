package com.chu.eduservice.controller;


import com.chu.commonutils.R;
import com.chu.eduservice.entity.EduVideo;
import com.chu.eduservice.service.EduVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author chu
 * @since 2022-11-23
 */
@RestController
@RequestMapping("/eduservice/video")
@CrossOrigin
public class EduVideoController {

    @Autowired
    private EduVideoService videoService;

    @PostMapping("/addVideo")
    public R addVideo(@RequestBody EduVideo video) {
        videoService.save(video);

        return R.ok();
    }

    // TODO 删除小节的时候，同时把里面的视频删除
    @DeleteMapping("/{videoId}")
    public R deleteVideo(@PathVariable String videoId) {
        videoService.removeById(videoId);

        return R.ok();
    }

    @GetMapping("/getVideoInfo/{videoId}")
    public R getVideoById(@PathVariable String videoId) {
        EduVideo video = videoService.getById(videoId);
        return R.ok().data("video", video);
    }

    @PostMapping("/updateVideo")
    public R updateVideo(@RequestBody EduVideo video) {
        videoService.updateById(video);
        return R.ok();
    }
}

