

package com.lk.authserver.support.handler;

import cn.hutool.core.collection.CollUtil;
import com.lk.common.core.util.SpringContextHolder;
import com.lk.common.core.util.WebUtils;
import com.lk.common.log.event.SysLogEvent;
import com.lk.common.log.util.SysLogUtils;
import com.lk.upms.api.entity.SysLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.stereotype.Component;

/**
 * @date 2022-06-02
 * <p>
 * 事件机制处理退出相关
 */
@Slf4j
@Component
public class LogoutSuccessEventHandler implements ApplicationListener<LogoutSuccessEvent> {

    @Override
    public void onApplicationEvent(LogoutSuccessEvent event) {
        Authentication authentication = (Authentication) event.getSource();
        if (CollUtil.isNotEmpty(authentication.getAuthorities())) {
            handle(authentication);
        }
    }

    /**
     * 处理退出成功方法
     * <p>
     * 获取到登录的authentication 对象
     *
     * @param authentication 登录对象
     */
    public void handle(Authentication authentication) {
        log.info("用户：{} 退出成功", authentication.getPrincipal());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        SysLog logVo = SysLogUtils.getSysLog();
        logVo.setTitle("退出成功");
        // 发送异步日志事件
        Long startTime = System.currentTimeMillis();
        Long endTime = System.currentTimeMillis();
        logVo.setTime(endTime - startTime);

        // 设置对应的token
        WebUtils.getRequest().ifPresent(request -> logVo.setParams(request.getHeader(HttpHeaders.AUTHORIZATION)));

        // 这边设置ServiceId
        if (authentication instanceof OAuth2Authorization) {
            OAuth2Authorization auth2Authentication = (OAuth2Authorization) authentication;
            logVo.setServiceId(auth2Authentication.getRegisteredClientId());
        }
        logVo.setCreateBy(authentication.getName());
        logVo.setUpdateBy(authentication.getName());
        SpringContextHolder.publishEvent(new SysLogEvent(logVo));
    }

}
