package com.mengshujoey.mengshuusersystem.common.config;

/**
 * application name:mengshujoeyPatient - MessageException
 * application describing:
 * copyright:
 * company:
 * time:2023-02-20 15:12:16
 *
 * @author liujingwen
 * @version ver 1.0
 * @since 1.8
 */
public class MessageException extends RuntimeException {
    public MessageException() {
    }

    public MessageException(String message) {
        super(message);
    }

    public MessageException(String message, Throwable cause) {
        super(message, cause);
    }
}

