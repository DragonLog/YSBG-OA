package com.learning.process.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learning.common.result.Result;
import com.learning.model.process.Process;
import com.learning.process.service.OaProcessService;
import com.learning.vo.process.ApprovalVo;
import com.learning.vo.process.ProcessQueryVo;
import com.learning.vo.process.ProcessVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 审批类型 前端控制器
 * </p>
 *
 * @author zcx
 * @since 2023-05-17
 */
@RestController
@RequestMapping("/admin/process")
@CrossOrigin
public class OaProcessController {


    @Autowired
    private OaProcessService oaProcessService;

    @ApiOperation("获取分页列表")
    @GetMapping("/{page}/{limit}")
    public Result index(@PathVariable Long page, @PathVariable Long limit, ProcessQueryVo processQueryVo) {
        Page<ProcessVo> pageParam = new Page<>(page, limit);
        Page<ProcessVo> pageModel = oaProcessService.selectPage(pageParam, processQueryVo);
        return Result.ok(pageModel);
    }

    //该接口应该移到ProcessController下
    @GetMapping("/show/{id}")
    public Result show(@PathVariable Long id) {
        Map<String, Object> map = oaProcessService.show(id);
        return Result.ok(map);
    }

    //该接口应该移到ProcessController下
    @ApiOperation("审批")
    @PostMapping("/approve")
    public Result approve(@RequestBody ApprovalVo approvalVo) {
        oaProcessService.approve(approvalVo);
        return Result.ok();
    }

    //该接口应该移到ProcessController下
    @ApiOperation("已处理")
    @GetMapping("/findProcessed/{page}/{limit}")
    public Result findProcessed(@PathVariable Long page, @PathVariable Long limit) {
        Page<Process> pageParam = new Page<>(page, limit);
        Page<ProcessVo> pageModel = oaProcessService.findProcessed(pageParam);
        return Result.ok(pageModel);
    }

    //该接口应该移到ProcessController下
    @ApiOperation("已发起")
    @GetMapping("/findStarted/{page}/{limit}")
    public Result findStarted(@PathVariable Long page, @PathVariable Long limit) {
        Page<ProcessVo> pageParam = new Page<>(page, limit);
        Page<ProcessVo> pageModel = oaProcessService.findStarted(pageParam);
        return Result.ok(pageModel);
    }

}

