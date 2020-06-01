package com.github.chengtengfei.onvif.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import com.github.chengtengfei.onvif.diagnostics.logging.Logger;
import com.github.chengtengfei.onvif.diagnostics.logging.Loggers;
import com.github.chengtengfei.onvif.discovery.SingleIPCDiscovery;
import com.github.chengtengfei.onvif.model.OnvifDeviceInfo;
import com.github.chengtengfei.onvif.model.ProfileInfo;
import com.github.chengtengfei.onvif.model.UsernameToken;
import com.github.chengtengfei.onvif.model.VideoInfo;
import com.github.chengtengfei.onvif.util.EncryptUtils;
import com.github.chengtengfei.onvif.util.HttpAuths;
import com.github.chengtengfei.onvif.util.OkHttpUtils;
import com.github.chengtengfei.onvif.util.RegexUtils;
import java.util.List;

public class OnvifService {

    private final static Logger LOGGER = Loggers.getLogger(OnvifService.class);


    private static OkHttpUtils okHttpUtils = new OkHttpUtils();


    public static List<ProfileInfo> getVideoInfo(OnvifDeviceInfo onvifDeviceInfo) throws Exception  {
        if (StringUtils.isEmpty(onvifDeviceInfo.getIp()) || !RegexUtils.isIp(onvifDeviceInfo.getIp())) {
            throw new Exception("onvif 设备的ip不合法");
        }
        if (StringUtils.isEmpty(onvifDeviceInfo.getUsername()) || StringUtils.isEmpty(onvifDeviceInfo.getPassword())) {
            throw new Exception("onvif设备的用户名或者密码为空");
        }

        try {
            SingleIPCDiscovery.fillOnvifAddress(onvifDeviceInfo);
            UsernameToken usernameToken = EncryptUtils.generate(onvifDeviceInfo.getUsername(), onvifDeviceInfo.getPassword());

            String mediaUrl = CapabilitiesService.getMediaUrl(onvifDeviceInfo, usernameToken);
            List<ProfileInfo> profiles = CapabilitiesService.getProfiles(mediaUrl, usernameToken);
            for (ProfileInfo profile : profiles) {
                String streamUri = CapabilitiesService.getStreamUri(mediaUrl, profile.getToken(), usernameToken);
                if (streamUri.startsWith("rtsp://")) {
                    StringBuilder fillStreamUrl = new StringBuilder(streamUri.substring(0, 7))
                            .append(onvifDeviceInfo.getUsername())
                            .append(":")
                            .append(onvifDeviceInfo.getPassword())
                            .append("@")
                            .append(streamUri.substring(7));
                    VideoInfo videoInfo = profile.getVideoInfo();
                    videoInfo.setStreamUri(fillStreamUrl.toString());
                    profile.setVideoInfo(videoInfo);
                }
            }

            return profiles;
        } catch (Exception e) {
            LOGGER.error("与摄像机[" + onvifDeviceInfo + "]交互通信发生错误, " + ExceptionUtils.getStackTrace(e));
            throw new Exception("与摄像机交互通信发生错误,请检查网络是否连通、摄像机是否支持Onvif协议或者摄像机是否正常工作");
        }

    }

    public static byte[] snap(OnvifDeviceInfo onvifDeviceInfo) throws Exception {
        if (StringUtils.isEmpty(onvifDeviceInfo.getIp()) || !RegexUtils.isIp(onvifDeviceInfo.getIp())) {
            throw new Exception("Ip不合法");
        }
        if (StringUtils.isEmpty(onvifDeviceInfo.getUsername()) || StringUtils.isEmpty(onvifDeviceInfo.getPassword())) {
            throw new Exception("用户名密码不合法");
        }

        byte[] img = new byte[0];
        SingleIPCDiscovery.fillOnvifAddress(onvifDeviceInfo);
        UsernameToken usernameToken = EncryptUtils.generate(onvifDeviceInfo.getUsername(), onvifDeviceInfo.getPassword());

        String mediaUrl = CapabilitiesService.getMediaUrl(onvifDeviceInfo, usernameToken);

        List<ProfileInfo> profiles = CapabilitiesService.getProfiles(mediaUrl, usernameToken);
        String authorization = HttpAuths.baseAuthGenerate(onvifDeviceInfo.getUsername(), onvifDeviceInfo.getPassword());
        for (int i = 0; i < profiles.size(); i++) {
            String snapshotUri = CapabilitiesService.getSnapshotUri(mediaUrl, profiles.get(i).getToken(), usernameToken);
            try {
                img = okHttpUtils.getFile(snapshotUri, authorization);
            } catch (Exception e) {
                LOGGER.error("根据抓拍uri[" + snapshotUri + "]获取图片出错, " + ExceptionUtils.getStackTrace(e));
                if (i == profiles.size() - 1) {
                    throw new Exception(e);
                }
            }

            if (img.length > 0) {
                break;
            }
        }
        return img;
    }

}
