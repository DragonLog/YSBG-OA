package com.learning.auth;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.learning.auth.mapper.SysRoleMapper;
import com.learning.model.system.SysRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class TestMpDemo1 {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Test
    public void getAll() {
        List<SysRole> sysRoleList = sysRoleMapper.selectList(null);
        System.out.println(sysRoleList);
    }

    @Test
    public void add() {
        SysRole sysRole = new SysRole();
        sysRole.setRoleName("角色管理员");
        sysRole.setRoleCode("role");
        sysRole.setDescription("测试数据");
        sysRoleMapper.insert(sysRole);
    }

    @Test
    public void update() {
        SysRole sysRole = sysRoleMapper.selectById(9);
        sysRole.setRoleName("zcx");
        sysRoleMapper.updateById(sysRole);
    }

    @Test
    public void deleteId() {
        sysRoleMapper.deleteById(9);
    }

    @Test
    public void deleteIds() {
        sysRoleMapper.deleteBatchIds(Arrays.asList(1, 2));
    }

    @Test
    public void testQuery1() {
        QueryWrapper<SysRole> sysRoleQueryWrapper = new QueryWrapper<>();
        sysRoleQueryWrapper.eq("role_name", "总经理");
        System.out.println(sysRoleMapper.selectList(sysRoleQueryWrapper));
    }

}
