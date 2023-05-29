package com.learning.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learning.auth.mapper.SysRoleMapper;
import com.learning.auth.service.SysRoleService;
import com.learning.auth.service.SysUserRoleService;
import com.learning.model.system.SysRole;
import com.learning.model.system.SysUserRole;
import com.learning.vo.system.AssginRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Override
    public Map<String, Object> findRoleDataByUserId(Long userId) {

        List<SysRole> sysRoleList = baseMapper.selectList(null);

        QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<SysUserRole> sysUserRoleList = sysUserRoleService.list(queryWrapper);

        List<Long> existUserRoleIdList = new ArrayList<>();
        for (SysUserRole sysUserRole : sysUserRoleList) {
            existUserRoleIdList.add(sysUserRole.getId());
        }

        List<SysRole> assignRoleList = new ArrayList<>();
        for (SysRole sysRole : sysRoleList) {
            if (existUserRoleIdList.contains(sysRole.getId())) {
                assignRoleList.add(sysRole);
            }
        }

        Map<String, Object> roleMap = new HashMap<>();
        roleMap.put("assignRoleList", assignRoleList);
        roleMap.put("allRolesList", sysRoleList);
        return roleMap;
    }

    @Override
    public void doAssign(AssginRoleVo assginRoleVo) {
        QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", assginRoleVo.getUserId());
        sysUserRoleService.remove(queryWrapper);
        for(Long roleId : assginRoleVo.getRoleIdList()) {
            if (StringUtils.isEmpty(roleId)) {
                continue;
            }
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(assginRoleVo.getUserId());
            sysUserRole.setRoleId(roleId);
            sysUserRoleService.save(sysUserRole);
        }
    }


}
