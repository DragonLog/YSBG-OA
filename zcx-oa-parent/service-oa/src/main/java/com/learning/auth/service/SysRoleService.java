package com.learning.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learning.model.system.SysRole;
import com.learning.vo.system.AssginRoleVo;

import java.util.Map;

public interface SysRoleService extends IService<SysRole> {

    Map<String, Object> findRoleDataByUserId(Long userId);

    void doAssign(AssginRoleVo assginRoleVo);
}
