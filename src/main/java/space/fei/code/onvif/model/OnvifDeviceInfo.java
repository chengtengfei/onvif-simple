package space.fei.code.onvif.model;

public class OnvifDeviceInfo {

    private String ip;
    private String onvifAddress;
    private String username;
    private String password;

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
}
