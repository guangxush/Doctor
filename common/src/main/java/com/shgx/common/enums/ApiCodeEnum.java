package com.shgx.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author: guangxush
 * @create: 2019/08/02
 */
public enum ApiCodeEnum {
    /**
     * 成功
     */
    SUCCESS("T200"),
    /**
     * 失败
     */
    FAILURE("T403"),
    /**
     * 未找到资源
     */
    NOT_FOUND("T404"),
    /**
     * 认证服务暂时关闭
     */
    PASSPORT_SERVICE_CLOSED("T403"),
    /**
     * 服务暂时关闭
     */
    SERVICE_CLOSED("T500"),
    /**
     * 内部错误
     */
    INTERN_ERROR("E400")
    ;

    private String code;

    ApiCodeEnum(String code) {
        this.code = code;
    }

    /**
     * Get code of the response
     *
     * @return
     */
    @JsonValue
    public String getCode() {
        return this.code;
    }
}
