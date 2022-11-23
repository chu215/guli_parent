package com.chu.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chu.eduservice.entity.EduSubject;
import com.chu.eduservice.entity.excel.SubjectData;
import com.chu.eduservice.entity.subject.OneSubject;
import com.chu.eduservice.entity.subject.TwoSubject;
import com.chu.eduservice.listener.SubjectExcelListener;
import com.chu.eduservice.mapper.EduSubjectMapper;
import com.chu.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author chu
 * @since 2022-11-22
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    @Override
    public void saveSubject(MultipartFile file, EduSubjectService subjectService) {

        try {
            InputStream inputStream = file.getInputStream();
            EasyExcel.read(inputStream, SubjectData.class, new SubjectExcelListener(subjectService)).sheet().doRead();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<OneSubject> getAllOneTwoSubject() {

        QueryWrapper<EduSubject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", "0");
        List<EduSubject> oneSubjectList = list(queryWrapper);

        List<OneSubject> list = new ArrayList<>();
        for (EduSubject eduSubject : oneSubjectList) {
            OneSubject oneSubject = new OneSubject();
            // oneSubject.setId(eduSubject.getId());
            // oneSubject.setTitle(eduSubject.getTitle());
            BeanUtils.copyProperties(eduSubject, oneSubject);

            // 查询二级目录
            QueryWrapper<EduSubject> twoSubjectQueryWrapper = new QueryWrapper<>();
            twoSubjectQueryWrapper.eq("parent_id", eduSubject.getId());
            List<EduSubject> twoSubjectList = list(twoSubjectQueryWrapper);

            for (EduSubject eduSubject2 : twoSubjectList) {
                TwoSubject twoSubject = new TwoSubject();

                // twoSubject.setId(eduSubject2.getId());
                // twoSubject.setTitle(eduSubject2.getTitle());
                BeanUtils.copyProperties(eduSubject2, twoSubject);

                oneSubject.getChildren().add(twoSubject);
            }

            list.add(oneSubject);
        }

        return list;
    }
}
