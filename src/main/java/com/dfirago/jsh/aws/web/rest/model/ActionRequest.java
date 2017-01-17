package com.dfirago.jsh.aws.web.rest.model;

/**
 * Created by dmfi on 17/01/2017.
 */
public class ActionRequest {

    private String deviceName;
    private String action;

    public ActionRequest() {

    }

    public ActionRequest(String deviceName, String action) {
        this.deviceName = deviceName;
        this.action = action;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "ActionRequest{" +
                "deviceName='" + deviceName + '\'' +
                ", action='" + action + '\'' +
                '}';
    }
}