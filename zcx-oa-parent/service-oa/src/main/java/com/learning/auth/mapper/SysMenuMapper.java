package com.learning.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.learning.model.system.SysMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author zcx
 * @since 2023-05-07
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<SysMenu> findMenuListByUserId(@Param("userId") Long userId);
}
