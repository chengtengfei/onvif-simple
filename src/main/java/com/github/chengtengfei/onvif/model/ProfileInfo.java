package com.github.chengtengfei.onvif.model;


public class ProfileInfo {

    private String name;
    private String token;

    private VideoInfo videoInfo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public VideoInfo getVideoInfo() {
        return videoInfo;
    }

    public void setVideoInfo(VideoInfo videoInfo) {
        this.videoInfo = videoInfo;
    }

    @Override
    public String toString() {
        return "ProfileInfo{" +
                "name='" + name + '\'' +
                ", token='" + token + '\'' +
                ", videoInfo=" + videoInfo.toString() +
                '}';
    }
}
