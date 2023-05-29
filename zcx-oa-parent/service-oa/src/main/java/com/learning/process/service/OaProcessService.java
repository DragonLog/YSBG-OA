package com.learning.process.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.learning.model.process.Process;
import com.learning.vo.process.ApprovalVo;
import com.learning.vo.process.ProcessFormVo;
import com.learning.vo.process.ProcessQueryVo;
import com.learning.vo.process.ProcessVo;

import java.util.Map;

/**
 * <p>
 * 审批类型 服务类
 * </p>
 *
 * @author zcx
 * @since 2023-05-17
 */
public interface OaProcessService extends IService<Process> {

    Page<ProcessVo> selectPage(Page<ProcessVo> pageParam, ProcessQueryVo processQueryVo);

    void deployByZip(String deployPath);

    void startUp(ProcessFormVo processFormVo);


    Page<ProcessVo> findPending(Page<Process> pageParam);

    Map<String, Object> show(Long id);

    void approve(ApprovalVo approvalVo);

    Page<ProcessVo> findProcessed(Page<Process> pageParam);

    Page<ProcessVo> findStarted(Page<ProcessVo> pageParam);

}
