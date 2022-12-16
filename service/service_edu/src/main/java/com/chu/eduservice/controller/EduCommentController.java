package com.chu.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chu.commonutils.JwtUtils;
import com.chu.commonutils.R;
import com.chu.commonutils.vo.MemberVo;
import com.chu.eduservice.client.UcenterClient;
import com.chu.eduservice.entity.EduComment;
import com.chu.eduservice.service.EduCommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author chu
 * @since 2022-12-16
 */
@RestController
@RequestMapping("/eduservice/educomment")
@CrossOrigin
public class EduCommentController {

    @Autowired
    private EduCommentService commentService;

    @Autowired
    private UcenterClient ucenterClient;

    @PostMapping("/save")
    public R save(@RequestBody EduComment comment, HttpServletRequest request) {
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if (StringUtils.isEmpty(memberId)) {
            return R.error().code(21000).message("请登录");
        }

        comment.setMemberId(memberId);
        MemberVo memberVo = ucenterClient.getInfo(memberId);

        comment.setNickname(memberVo.getNickname());
        comment.setAvatar(memberVo.getAvatar());

        commentService.save(comment);
        return R.ok();
    }

    @GetMapping("/page/{page}/{limit}")
    public R index(@PathVariable long page, @PathVariable long limit,
                   @RequestParam(required = false) String courseId) {

        Page<EduComment> commentPage = new Page<>(page, limit);
        QueryWrapper<EduComment> commentQueryWrapper = new QueryWrapper<>();
        commentQueryWrapper.eq("course_id", courseId);

        commentService.page(commentPage, commentQueryWrapper);

        List<EduComment> commentList = commentPage.getRecords();
        Map<String, Object> map = new HashMap<>();
        map.put("items", commentList);
        map.put("current", commentPage.getCurrent());
        map.put("pages", commentPage.getPages());
        map.put("size", commentPage.getSize());
        map.put("total", commentPage.getTotal());
        map.put("hasNext", commentPage.hasNext());
        map.put("hasPrevious", commentPage.hasPrevious());
        return R.ok().data(map);
    }
}

