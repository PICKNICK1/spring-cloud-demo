package com.lk.common.core.constant.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @date 2018/8/15 社交登录类型
 */
@Getter
@RequiredArgsConstructor
public enum LoginTypeEnum {

    /**
     * 账号密码登录
     */
    PWD("PWD", "账号密码登录"),

    /**
     * 验证码登录
     */
    SMS("SMS", "验证码登录");

    /**
     * 类型
     */
    private final String type;

    /**
     * 描述
     */
    private final String description;

}
