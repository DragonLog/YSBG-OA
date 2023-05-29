package com.learning.process.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learning.model.process.ProcessTemplate;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 审批模板 服务类
 * </p>
 *
 * @author zcx
 * @since 2023-05-14
 */
public interface OaProcessTemplateService extends IService<ProcessTemplate> {

    Page<ProcessTemplate> selectPageProcessTemplate(Page<ProcessTemplate> pageParam);

    void publish(Long id);
}
