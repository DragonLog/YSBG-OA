package com.learning.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learning.model.process.ProcessRecord;

/**
 * <p>
 * 审批记录 服务类
 * </p>
 *
 * @author zcx
 * @since 2023-05-21
 */
public interface OaProcessRecordService extends IService<ProcessRecord> {

    void record(Long processID, Integer status, String description);

}
