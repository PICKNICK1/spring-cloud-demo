

package com.lk.authserver.support.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

/**
 * @date 2022-06-02
 * <p>
 * 表单登录成功处理逻辑
 */
@Slf4j
public class FormAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    public FormAuthenticationSuccessHandler(String targetUrlParameter) {
        super.setTargetUrlParameter(targetUrlParameter);
    }
}
