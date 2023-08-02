package com.istlab.datagovernanceplatform.utils;

import lombok.Data;

@Data
public class Result<T> {

    /**
     * 数据
     */
    T data;

    /**
     * 消息
     */
    String message;

    /**
     * 代码
     */
    int code;
}
