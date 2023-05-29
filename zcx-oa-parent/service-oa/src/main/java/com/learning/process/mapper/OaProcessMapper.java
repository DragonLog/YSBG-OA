package com.learning.process.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learning.model.process.Process;
import com.learning.vo.process.ProcessQueryVo;
import com.learning.vo.process.ProcessVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 审批类型 Mapper 接口
 * </p>
 *
 * @author zcx
 * @since 2023-05-17
 */
public interface OaProcessMapper extends BaseMapper<Process> {

    Page<ProcessVo> selectPage(Page<ProcessVo> pageParam, @Param("vo") ProcessQueryVo processQueryVo);

}
