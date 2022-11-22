package com.chu.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.chu.oss.service.OssService;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static com.chu.oss.utils.ConstantPropertiesUtils.*;

@Service
public class OssServiceImpl implements OssService {
    @Override
    public String uploadFileAvatar(MultipartFile file) {

        String endpoint = END_POINT;
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = ACCESS_KEY_ID;
        String accessKeySecret = ACCESS_KEY_SECRET;
        // 填写Bucket名称，例如examplebucket。
        String bucketName = BUCKET_NAME;

        try {
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            InputStream inputStream = file.getInputStream();
            String filename = file.getOriginalFilename();

            // 文件名加上uuid
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            filename = uuid + filename;

            // 把文件按照日期分类
            String datePath = new DateTime().toString("yyyy/MM/dd");
            filename = datePath + "/" + filename;

            ossClient.putObject(bucketName, filename, inputStream);
            ossClient.shutdown();

            String url = "https://" + bucketName + "." + endpoint + "/" + filename;
            return url;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
