

package com.lk.authserver.support.handler;

import com.lk.common.core.util.R;
import com.lk.common.core.util.SpringContextHolder;
import com.lk.common.log.event.SysLogEvent;
import com.lk.common.log.util.LogTypeEnum;
import com.lk.common.log.util.SysLogUtils;
import com.lk.upms.api.entity.SysLog;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @date 2022-06-02
 */
@Slf4j
public class AuthenticationFailureEventHandler implements AuthenticationFailureHandler {

    private final MappingJackson2HttpMessageConverter errorHttpResponseConverter = new MappingJackson2HttpMessageConverter();

    /**
     * Called when an authentication attempt fails.
     *
     * @param request   the request during which the authentication attempt occurred.
     * @param response  the response.
     * @param exception the exception which was thrown to reject the authentication
     *                  request.
     */
    @Override
    @SneakyThrows
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) {
        String username = request.getParameter(OAuth2ParameterNames.USERNAME);

        log.info("用户：{} 登录失败，异常：{}", username, exception.getLocalizedMessage());
        SysLog logVo = SysLogUtils.getSysLog();
        logVo.setTitle("登录失败");
        logVo.setType(LogTypeEnum.ERROR.getType());
        logVo.setException(exception.getLocalizedMessage());
        // 发送异步日志事件
        Long startTime = System.currentTimeMillis();
        Long endTime = System.currentTimeMillis();
        logVo.setTime(endTime - startTime);
        logVo.setCreateBy(username);
        logVo.setUpdateBy(username);
        SpringContextHolder.publishEvent(new SysLogEvent(logVo));
        // 写出错误信息
        sendErrorResponse(response, exception);
    }

    private void sendErrorResponse(HttpServletResponse response, AuthenticationException exception) throws IOException {
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        httpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
        this.errorHttpResponseConverter.write(R.failed(exception.getLocalizedMessage()), MediaType.APPLICATION_JSON,
                httpResponse);
    }

}
