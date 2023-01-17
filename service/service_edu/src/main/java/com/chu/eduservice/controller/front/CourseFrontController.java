package com.chu.eduservice.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chu.commonutils.JwtUtils;
import com.chu.commonutils.R;
import com.chu.commonutils.vo.CourseWebVoOrder;
import com.chu.eduservice.client.OrdersClient;
import com.chu.eduservice.entity.EduCourse;
import com.chu.eduservice.entity.chapter.ChapterVo;
import com.chu.eduservice.entity.frontvo.CourseFrontVo;
import com.chu.eduservice.entity.frontvo.CourseWebVo;
import com.chu.eduservice.service.EduChapterService;
import com.chu.eduservice.service.EduCourseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eduservice/coursefront")
@CrossOrigin
public class CourseFrontController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduChapterService chapterService;

    @Autowired
    private OrdersClient ordersClient;

    @PostMapping("/getFrontCourseList/{page}/{limit}")
    public R getFrontCourseList(@PathVariable long page, @PathVariable long limit,
                                @RequestBody(required = false) CourseFrontVo courseFrontVo) {
        Page<EduCourse> coursePage = new Page<>(page, limit);
        Map<String, Object> map = courseService.getCourseFrontList(coursePage, courseFrontVo);
        return R.ok().data(map);
    }

    @GetMapping("/getFrontCourseInfo/{courseId}")
    public R getFrontCourseInfo(@PathVariable String courseId, HttpServletRequest request) {
        // 根据课程id，编写sql语句查询课程信息
        CourseWebVo courseWebVo = courseService.getBaseCourseInfo(courseId);

        // 根据课程id查询章节和小节
        List<ChapterVo> chapterVoList = chapterService.getChapterVideoByCourseId(courseId);

        // 根据课程id和用户id查询当前课程是否已经支付过了
        boolean buyCourse = ordersClient.isBuyCourse(courseId, JwtUtils.getMemberIdByJwtToken(request));

        return R.ok().data("courseWebVo", courseWebVo).data("chapterVoList", chapterVoList)
                .data("isBuy", buyCourse);
    }

    // 根据课程id查询课程信息
    @PostMapping("/getCourseInfoOrder/{id}")
    public CourseWebVoOrder getCourseInfoOrder(@PathVariable String id) {
        CourseWebVo courseInfo = courseService.getBaseCourseInfo(id);
        CourseWebVoOrder courseWebVoOrder = new CourseWebVoOrder();
        BeanUtils.copyProperties(courseInfo, courseWebVoOrder);

        return courseWebVoOrder;
    }
}
