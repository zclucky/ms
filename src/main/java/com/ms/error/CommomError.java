package com.ms.error;

public interface CommomError {
    public int getErrorCode();
    public String getErrorMsg();
    public CommomError setErrorMsg(String errorMsg);
}
