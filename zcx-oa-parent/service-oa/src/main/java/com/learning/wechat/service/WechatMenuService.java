package com.learning.wechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learning.model.wechat.Menu;
import com.learning.vo.wechat.MenuVo;

import java.util.List;

/**
 * <p>
 * 菜单 服务类
 * </p>
 *
 * @author zcx
 * @since 2023-05-24
 */
public interface WechatMenuService extends IService<Menu> {

    List<MenuVo> findMenuInfo();

    void syncMenu();

    void removeMenu();
}
