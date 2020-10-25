package com.even.lc.result;

public enum ResultCode {
    /**
     * 枚举代码代表意义
     */
    SUCCESS(200),
    FAIL(400),
    UNAUTHORIZED(401),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500);

    public int code;

    ResultCode(int code) {
        this.code=code;
    }

}
