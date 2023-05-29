package com.learning.process.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learning.model.process.ProcessTemplate;
import com.learning.model.process.ProcessType;
import com.learning.process.mapper.OaProcessTemplateMapper;
import com.learning.process.service.OaProcessService;
import com.learning.process.service.OaProcessTemplateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learning.process.service.OaProcessTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 审批模板 服务实现类
 * </p>
 *
 * @author zcx
 * @since 2023-05-14
 */
@Service
public class OaProcessTemplateServiceImpl extends ServiceImpl<OaProcessTemplateMapper, ProcessTemplate> implements OaProcessTemplateService {

    @Autowired
    private OaProcessTypeService oaProcessTypeService;

    @Autowired
    private OaProcessService oaProcessService;

    @Override
    public Page<ProcessTemplate> selectPageProcessTemplate(Page<ProcessTemplate> pageParam) {
        Page<ProcessTemplate> processTemplatePage = baseMapper.selectPage(pageParam, null);
        for (ProcessTemplate processTemplate : processTemplatePage.getRecords()) {
            Long processTypeId = processTemplate.getProcessTypeId();
            QueryWrapper<ProcessType> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", processTypeId);
            ProcessType processType = oaProcessTypeService.getOne(queryWrapper);
            if (processType == null) {
                continue;
            }
            processTemplate.setProcessTypeName(processType.getName());
        }
        return processTemplatePage;
    }

    @Override
    public void publish(Long id) {
        ProcessTemplate processTemplate = baseMapper.selectById(id);
        processTemplate.setStatus(1);
        baseMapper.updateById(processTemplate);

        if (!StringUtils.isEmpty(processTemplate.getProcessDefinitionPath())) {
            oaProcessService.deployByZip(processTemplate.getProcessDefinitionPath());
        }
    }
}
