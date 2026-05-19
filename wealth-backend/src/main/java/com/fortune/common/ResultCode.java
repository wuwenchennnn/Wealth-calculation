package com.fortune.common;

public interface ResultCode {
    int PARAM_ERROR = 400;
    int TOO_MANY_REQUESTS = 429;
    int NOT_FOUND = 404;
    int SERVER_ERROR = 500;
}
