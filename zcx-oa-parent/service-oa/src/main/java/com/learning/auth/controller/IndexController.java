package com.learning.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.learning.auth.service.SysMenuService;
import com.learning.auth.service.SysUserService;
import com.learning.common.jwt.JwtHelper;
import com.learning.common.result.Result;
import com.learning.common.util.MD5;
import com.learning.model.system.SysUser;
import com.learning.vo.system.LoginVo;
import com.learning.vo.system.RouterVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "后台登录管理")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuService sysMenuService;

    @PostMapping("/login")
    public Result login(@RequestBody LoginVo loginVo) throws Exception {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", loginVo.getUsername());
        SysUser user = sysUserService.getOne(queryWrapper);
        if (user == null) {
            throw new Exception("用户不存在");
        }

        String password = user.getPassword();
        String password_input = MD5.encrypt(loginVo.getPassword());
        if (!password.equals(password_input)) {
            throw new Exception("密码错误");
        }

        if (user.getStatus().intValue() == 0) {
            throw new Exception("用户被禁用");
        }

        String token = JwtHelper.createToken(user.getId(), user.getUsername());
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        return Result.ok(result);
    }

    @GetMapping("/info")
    public Result info(HttpServletRequest request) throws Exception {
        String token = request.getHeader("token");

        Long userId = JwtHelper.getUserId(token);

        SysUser sysUser = sysUserService.getById(userId);

        List<RouterVo> routerVoList = sysMenuService.findUserMenuListByUserId(userId);

        List<String> permList = sysMenuService.findUserPermsByUserId(userId);

        Map<String, Object> map = new HashMap<>();
        map.put("roles", "[admin]");
        map.put("name", sysUser.getName());
        map.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");

        map.put("routers", routerVoList);
        map.put("buttons", permList);
        return Result.ok(map);
    }

    @PostMapping("/logout")
    public Result logout() {
        return Result.ok();
    }

}
