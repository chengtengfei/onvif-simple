package com.github.chengtengfei.onvif.model;

public class OnvifDeviceInfo {

    private String ip;
    private String onvifAddress;
    private String username;
    private String password;

    public OnvifDeviceInfo() {}

    private OnvifDeviceInfo(Builder builder) {
        setIp(builder.ip);
        setOnvifAddress(builder.onvifAddress);
        setUsername(builder.username);
        setPassword(builder.password);
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getOnvifAddress() {
        return onvifAddress;
    }

    public void setOnvifAddress(String onvifAddress) {
        this.onvifAddress = onvifAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    @Override
    public String toString() {
        return "OnvifDeviceInfo{" +
                "ip='" + ip + '\'' +
                ", onvifAddress='" + onvifAddress + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public static final class Builder {
        private String ip;
        private String onvifAddress;
        private String username;
        private String password;

        public Builder() {
        }

        public Builder ip(String val) {
            ip = val;
            return this;
        }

        public Builder onvifAddress(String val) {
            onvifAddress = val;
            return this;
        }

        public Builder username(String val) {
            username = val;
            return this;
        }

        public Builder password(String val) {
            password = val;
            return this;
        }

        public OnvifDeviceInfo build() {
            return new OnvifDeviceInfo(this);
        }
    }
}
