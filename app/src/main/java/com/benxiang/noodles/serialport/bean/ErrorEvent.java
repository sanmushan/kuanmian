package com.benxiang.noodles.serialport.bean;

/**
 * Created by admin on 2017/6/28.
 */

public class ErrorEvent {

    private String buttonText;
    private String errorText;

    public ErrorEvent(String buttonText, String errorText) {
        this.buttonText = buttonText;
        this.errorText = errorText;
    }

    public ErrorEvent(String errorText) {
        this.errorText = errorText;
    }

    public String getButtonText() {
        return buttonText;
    }

    public String getErrorText() {
        return errorText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }
}
