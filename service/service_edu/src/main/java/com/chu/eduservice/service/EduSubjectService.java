package com.chu.eduservice.service;

import com.chu.eduservice.entity.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chu.eduservice.entity.subject.OneSubject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author chu
 * @since 2022-11-22
 */
public interface EduSubjectService extends IService<EduSubject> {

    void saveSubject(MultipartFile file, EduSubjectService subjectService);

    List<OneSubject> getAllOneTwoSubject();

}
