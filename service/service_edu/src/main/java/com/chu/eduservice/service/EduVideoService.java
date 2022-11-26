package com.chu.eduservice.service;

import com.chu.eduservice.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author chu
 * @since 2022-11-23
 */
public interface EduVideoService extends IService<EduVideo> {

    void removeVideoByCourseId(String courseId);
}
