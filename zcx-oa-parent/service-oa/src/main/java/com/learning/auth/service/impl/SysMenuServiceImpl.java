package com.learning.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.learning.auth.mapper.SysMenuMapper;
import com.learning.auth.service.SysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learning.auth.service.SysRoleMenuService;
import com.learning.auth.util.MenuHelper;
import com.learning.model.system.SysMenu;
import com.learning.model.system.SysRoleMenu;
import com.learning.vo.system.AssginMenuVo;
import com.learning.vo.system.MetaVo;
import com.learning.vo.system.RouterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author zcx
 * @since 2023-05-07
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Override
    public List<SysMenu> findNodes() {
        List<SysMenu> sysMenuList = baseMapper.selectList(null);
        List<SysMenu> resultList = MenuHelper.buildTree(sysMenuList);
        return resultList;
    }

    @Override
    public void removeMenuById(Long id) throws Exception {
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", id);
        Integer count = baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new Exception("菜单不能删除");
        }
        baseMapper.deleteById(id);
    }

    @Override
    public List<SysMenu> findMenuByRoleId(Long roleId) {
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        List<SysMenu> sysMenuList = baseMapper.selectList(queryWrapper);

        QueryWrapper<SysRoleMenu> sysRoleMenuQueryWrapper = new QueryWrapper<>();
        sysRoleMenuQueryWrapper.eq("role_id", roleId);
        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuService.list(sysRoleMenuQueryWrapper);

        List<Long> menuIdList = new ArrayList<>();
        for (SysRoleMenu sysRoleMenu : sysRoleMenuList) {
            menuIdList.add(sysRoleMenu.getMenuId());
        }

        for (SysMenu sysMenu : sysMenuList) {
            if (menuIdList.contains(sysMenu.getId())) {
                sysMenu.setSelect(true);
            } else {
                sysMenu.setSelect(false);
            }
        }

        List<SysMenu> resultList = MenuHelper.buildTree(sysMenuList);
        return resultList  ;
    }

    @Override
    public void doAssign(AssginMenuVo assginMenuVo) {
        QueryWrapper<SysRoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", assginMenuVo.getRoleId());
        sysRoleMenuService.remove(queryWrapper);

        List<Long> menuIdList = assginMenuVo.getMenuIdList();
        for (Long menuId : menuIdList) {
            if (StringUtils.isEmpty(menuId)) {
                continue;
            }
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setMenuId(menuId);
            sysRoleMenu.setRoleId(assginMenuVo.getRoleId());
            sysRoleMenuService.save(sysRoleMenu);
        }
    }

    @Override
    public List<RouterVo> findUserMenuListByUserId(Long userId) throws Exception {
        if (userId == null) {
            throw new Exception("token丢失");
        }
        List<SysMenu> sysMenuList = null;
        if (userId.longValue() == 1) {
            QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("status", 1);
            queryWrapper.orderByAsc("sort_value");
            sysMenuList = baseMapper.selectList(queryWrapper);
        } else {
            sysMenuList = baseMapper.findMenuListByUserId(userId);
        }
        List<SysMenu> sysMenuTreeList = MenuHelper.buildTree(sysMenuList);
        List<RouterVo> routerVoList = this.buildRouter(sysMenuTreeList);
        return routerVoList;
    }

    private List<RouterVo> buildRouter(List<SysMenu> menus) {
        List<RouterVo> routers = new ArrayList<>();

        for (SysMenu menu : menus) {
            RouterVo routerVo = new RouterVo();
            routerVo.setHidden(false);
            routerVo.setAlwaysShow(false);
            routerVo.setPath(getRouterPath(menu));
            routerVo.setComponent(menu.getComponent());
            routerVo.setMeta(new MetaVo(menu.getName(), menu.getIcon()));
            List<SysMenu> children = menu.getChildren();

            if (menu.getType().intValue() == 1) {
                List<SysMenu> hiddenMenuList = new ArrayList<>();
                for (SysMenu sysMenu : children) {
                    if (!StringUtils.isEmpty(sysMenu.getComponent())) {
                        hiddenMenuList.add(sysMenu);
                    }
                }
                for (SysMenu hiddenMenu : hiddenMenuList) {
                    RouterVo hiddenRouterVo = new RouterVo();
                    hiddenRouterVo.setHidden(true);
                    hiddenRouterVo.setAlwaysShow(false);
                    hiddenRouterVo.setPath(getRouterPath(hiddenMenu));
                    hiddenRouterVo.setComponent(hiddenMenu.getComponent());
                    hiddenRouterVo.setMeta(new MetaVo(hiddenMenu.getName(), hiddenMenu.getIcon()));
                    routers.add(hiddenRouterVo);
                }
            } else {
                if (!CollectionUtils.isEmpty(children)) {
                    routerVo.setAlwaysShow(true);
                    routerVo.setChildren(buildRouter(children));
                }
            }
            routers.add(routerVo);
        }
        return routers;
    }

    private String getRouterPath(SysMenu menu) {
        String routerPath = "/" + menu.getPath();
        if (menu.getParentId().intValue() != 0) {
            routerPath = menu.getPath();
        }
        return routerPath;
    }

    @Override
    public List<String> findUserPermsByUserId(Long userId) {
        List<SysMenu> sysMenuList = null;
        if (userId.longValue() == 1) {
            QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("status", 1);
            sysMenuList = baseMapper.selectList(queryWrapper);
        } else {
            sysMenuList = baseMapper.findMenuListByUserId(userId);
        }

        List<String> permsList = new ArrayList<>();
        for (SysMenu sysMenu : sysMenuList) {
            if (sysMenu.getType() == 2) {
                permsList.add(sysMenu.getPerms());
            }
        }


        return permsList;
    }
}
