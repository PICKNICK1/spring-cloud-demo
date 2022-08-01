package com.lk.common.minio.annotation;

import com.lk.common.minio.MinioAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(MinioAutoConfiguration.class)
public @interface EnableMinioClient {
}
