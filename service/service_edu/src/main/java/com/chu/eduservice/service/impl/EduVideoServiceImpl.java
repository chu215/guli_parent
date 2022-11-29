package com.chu.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chu.eduservice.client.VodClient;
import com.chu.eduservice.entity.EduVideo;
import com.chu.eduservice.mapper.EduVideoMapper;
import com.chu.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author chu
 * @since 2022-11-23
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    @Autowired
    private VodClient vodClient;

    // TODO 删除对应视频文件
    @Override
    public void removeVideoByCourseId(String courseId) {
        QueryWrapper<EduVideo> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("course_id", courseId);

        List<EduVideo> videoList = this.list(videoQueryWrapper);
        List<String> videoIds = videoList.stream()
                .map(EduVideo::getVideoSourceId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (videoIds.size() > 0) {
            vodClient.deleteBatch(videoIds);
        }

        remove(videoQueryWrapper);
    }
}
