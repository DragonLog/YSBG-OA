package com.learning.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learning.model.system.SysUser;

import java.util.Map;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author zcx
 * @since 2023-05-06
 */
public interface SysUserService extends IService<SysUser> {

    void updateStatus(Long id, Integer status);

    SysUser getUserByUsername(String username);

    Map<String, Object> getCurrentUser();
}
