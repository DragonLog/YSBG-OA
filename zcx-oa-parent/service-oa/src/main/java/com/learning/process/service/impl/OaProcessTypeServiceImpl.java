package com.learning.process.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.learning.model.process.ProcessTemplate;
import com.learning.model.process.ProcessType;
import com.learning.process.mapper.OaProcessTypeMapper;
import com.learning.process.service.OaProcessTemplateService;
import com.learning.process.service.OaProcessTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 审批类型 服务实现类
 * </p>
 *
 * @author zcx
 * @since 2023-05-14
 */
@Service
public class OaProcessTypeServiceImpl extends ServiceImpl<OaProcessTypeMapper, ProcessType> implements OaProcessTypeService {

    @Autowired
    private OaProcessTemplateService oaProcessTemplateService;

    @Override
    public List<ProcessType> findProcessType() {
        List<ProcessType> processTypeList = baseMapper.selectList(null);
        for (ProcessType processType : processTypeList) {
            Long typeId = processType.getId();
            QueryWrapper<ProcessTemplate> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("process_type_id", typeId);
            queryWrapper.eq("status", 1);

            List<ProcessTemplate> processTemplateList = oaProcessTemplateService.list(queryWrapper);
            processType.setProcessTemplateList(processTemplateList);
        }
        return processTypeList;
    }
}
