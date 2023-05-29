package com.learning.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learning.model.system.SysMenu;
import com.learning.vo.system.AssginMenuVo;
import com.learning.vo.system.RouterVo;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author zcx
 * @since 2023-05-07
 */
public interface SysMenuService extends IService<SysMenu> {

    List<SysMenu> findNodes();

    void removeMenuById(Long id) throws Exception;

    List<SysMenu> findMenuByRoleId(Long roleId);

    void doAssign(AssginMenuVo assginMenuVo);

    List<RouterVo> findUserMenuListByUserId(Long userId) throws Exception;

    List<String> findUserPermsByUserId(Long userId);

}
