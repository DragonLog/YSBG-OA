package com.learning.auth;

import com.learning.auth.service.SysRoleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestMpDemo2 {

    @Autowired
    private SysRoleService sysRoleService;

    @Test
    public void getAll() {
        System.out.println(sysRoleService.list());
    }

}
