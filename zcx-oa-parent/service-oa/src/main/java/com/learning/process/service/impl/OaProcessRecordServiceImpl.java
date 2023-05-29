package com.learning.process.service.impl;

import com.learning.auth.service.SysUserService;
import com.learning.model.process.ProcessRecord;
import com.learning.model.system.SysUser;
import com.learning.process.mapper.OaProcessRecordMapper;
import com.learning.process.service.OaProcessRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learning.security.custom.LoginUserInfoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 审批记录 服务实现类
 * </p>
 *
 * @author zcx
 * @since 2023-05-21
 */
@Service
public class OaProcessRecordServiceImpl extends ServiceImpl<OaProcessRecordMapper, ProcessRecord> implements OaProcessRecordService {

    @Autowired
    private SysUserService sysUserService;

    @Override
    public void record(Long processID, Integer status, String description) {
        Long userId = LoginUserInfoHelper.getUserId();
        SysUser user = sysUserService.getById(userId);

        ProcessRecord processRecord = new ProcessRecord();
        processRecord.setProcessId(processID);
        processRecord.setStatus(status);
        processRecord.setDescription(description);
        processRecord.setOperateUser(user.getName());
        processRecord.setOperateUserId(user.getId());
        baseMapper.insert(processRecord);
    }
}
