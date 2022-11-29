package com.chu.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.chu.commonutils.R;
import com.chu.servicebase.exceptionhandler.GuliException;
import com.chu.vod.service.VodService;
import com.chu.vod.utils.InitVodClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.chu.vod.utils.ConstantVodUtils.ACCESS_KEY_ID;
import static com.chu.vod.utils.ConstantVodUtils.ACCESS_KEY_SECRET;

@Service
public class VodServiceImpl implements VodService {
    @Override
    public String uploadVideoAly(MultipartFile file) {

        try {
            String fileName = file.getOriginalFilename();
            String title = fileName.substring(0, fileName.lastIndexOf("."));

            InputStream inputStream = file.getInputStream();

            UploadStreamRequest request = new UploadStreamRequest(ACCESS_KEY_ID, ACCESS_KEY_SECRET,
                    title, fileName, inputStream);

            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);

            String videoId = null;
            if (response.isSuccess()) {
                videoId = response.getVideoId();
            } else {
                videoId = response.getVideoId();
            }

            return videoId;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void removeMoreAlyVideo(List<String> videoIdList) {
        try {
            DefaultAcsClient client = InitVodClient.initVodClient(ACCESS_KEY_ID, ACCESS_KEY_SECRET);

            DeleteVideoRequest request = new DeleteVideoRequest();

            String videoIds = StringUtils.join(videoIdList.toArray(), ",");
            request.setVideoIds(videoIds);
            client.getAcsResponse(request);
        } catch (ClientException e) {
            e.printStackTrace();
            throw new GuliException(20001, "删除视频失败");
        }
    }
}
