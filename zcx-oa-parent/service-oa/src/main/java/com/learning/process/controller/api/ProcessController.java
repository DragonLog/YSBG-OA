package com.learning.process.controller.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learning.auth.service.SysUserService;
import com.learning.common.result.Result;
import com.learning.model.process.Process;
import com.learning.model.process.ProcessTemplate;
import com.learning.model.process.ProcessType;
import com.learning.process.service.OaProcessService;
import com.learning.process.service.OaProcessTemplateService;
import com.learning.process.service.OaProcessTypeService;
import com.learning.vo.process.ProcessFormVo;
import com.learning.vo.process.ProcessVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api("审批流管理")
@RestController
@RequestMapping("/admin/process")
@CrossOrigin
public class ProcessController {

    @Autowired
    private OaProcessTypeService oaProcessTypeService;

    @Autowired
    private OaProcessTemplateService oaProcessTemplateService;

    @Autowired
    private OaProcessService oaProcessService;

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/findProcessType")
    public Result findProcessType() {
        List<ProcessType> list = oaProcessTypeService.findProcessType();
        return Result.ok(list);
    }

    @GetMapping("/getProcessTemplate/{processTemplateId}")
    public Result getProcessTemplate(@PathVariable Long processTemplateId) {
        ProcessTemplate processTemplate = oaProcessTemplateService.getById(processTemplateId);
        return Result.ok(processTemplate);
    }

    @ApiOperation("启动流程")
    @PostMapping("/startUp")
    public Result startUp(@RequestBody ProcessFormVo processFormVo) {
        oaProcessService.startUp(processFormVo);
        return Result.ok();
    }

    @ApiOperation("待处理")
    @GetMapping("/findPending/{page}/{limit}")
    public Result findPending(@PathVariable Long page, @PathVariable Long limit) {
        Page<Process> pageParam = new Page<>(page, limit);
        Page<ProcessVo> pageModel = oaProcessService.findPending(pageParam);
        return Result.ok(pageModel);
    }

    @GetMapping("/getCurrentUser")
    public Result getCurrentUser() {
        Map<String, Object> map = sysUserService.getCurrentUser();
        return Result.ok(map);
    }
}
