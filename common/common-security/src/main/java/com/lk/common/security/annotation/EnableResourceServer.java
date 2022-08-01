

package com.lk.common.security.annotation;

import com.lk.common.security.component.CustomResourceServerAutoConfiguration;
import com.lk.common.security.component.CustomResourceServerConfiguration;
import com.lk.common.security.feign.CustomFeignClientConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import java.lang.annotation.*;

/**
 * @date 2022-06-04
 * <p>
 * 资源服务注解
 */
@Documented
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Import({CustomResourceServerAutoConfiguration.class, CustomResourceServerConfiguration.class,
        CustomFeignClientConfiguration.class})
public @interface EnableResourceServer {

}
