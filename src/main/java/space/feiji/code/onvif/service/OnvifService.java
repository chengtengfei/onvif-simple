package space.feiji.code.onvif.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import space.feiji.code.onvif.discovery.SingleIPCDiscovery;
import space.feiji.code.onvif.model.OnvifDeviceInfo;
import space.feiji.code.onvif.model.ProfileInfo;
import space.feiji.code.onvif.model.UsernameToken;
import space.feiji.code.onvif.model.VideoInfo;
import space.feiji.code.onvif.util.EncryptUtils;
import space.feiji.code.onvif.util.HttpAuths;
import space.feiji.code.onvif.util.OkHttpUtils;
import space.feiji.code.onvif.util.RegexUtils;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * @author: tengfei.cheng
 * @date: 10:29 2019/6/12
 * @since: 0.0.1
 * @description: 获取onvif设备信息
 */
public class OnvifService {

    private final static Logger LOGGER = LoggerFactory.getLogger(OnvifService.class);


    private OkHttpUtils okHttpUtils = new OkHttpUtils();


    public void getVideoInfo(OnvifDeviceInfo onvifDeviceInfo) throws Exception  {
        if (StringUtils.isEmpty(onvifDeviceInfo.getIp()) || !RegexUtils.isIp(onvifDeviceInfo.getIp())) {
            throw new Exception("onvif 设备的ip不合法");
        }
        if (StringUtils.isEmpty(onvifDeviceInfo.getUsername()) || StringUtils.isEmpty(onvifDeviceInfo.getPassword())) {
            throw new Exception("onvif设备的用户名或者密码为空");
        }

        JSONArray dataArray = new JSONArray();

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

            if (profiles.size() > 0) {
                for (ProfileInfo profileInfo : profiles) {
                    JSONObject dataObject = new JSONObject();
                    dataObject.put("video_profile", profileInfo.getName() + "(" + profileInfo.getToken() + ")");
                    dataObject.put("video_encoding", profileInfo.getVideoInfo().getVideoEncoding());
                    dataObject.put("video_frame_rate_limit", profileInfo.getVideoInfo().getFrameRateLimit());
                    dataObject.put("video_bitrate_limit", profileInfo.getVideoInfo().getBitrateLimit());
                    dataObject.put("video_stream_uri", profileInfo.getVideoInfo().getStreamUri());
                    if (!StringUtils.isEmpty(profileInfo.getVideoInfo().getStreamUri())) {
                        Integer videoWidth = profileInfo.getVideoInfo().getVideoWidth();
                        Integer videoHeight = profileInfo.getVideoInfo().getVideoHeight();
                        if (videoWidth == null || videoHeight == null || videoWidth <=0 || videoHeight <= 0) {
                            dataObject.put("video_width", 0);
                            dataObject.put("video_height", 0);
                        } else {
                             dataObject.put("video_width", profileInfo.getVideoInfo().getVideoWidth());
                             dataObject.put("video_height", profileInfo.getVideoInfo().getVideoHeight());
                        }
                    }
                    dataArray.add(dataObject);
                }

            }
        } catch (Exception e) {
            LOGGER.error("与摄像机{}交互通信发生错误{}", onvifDeviceInfo, ExceptionUtils.getStackTrace(e));
            throw new Exception("与摄像机交互通信发生错误,请检查网络是否连通、摄像机是否支持Onvif协议或者摄像机是否正常工作");
        }

    }

    public byte[] snap(OnvifDeviceInfo onvifDeviceInfo) throws Exception {
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
                LOGGER.error("根据抓拍uri{}获取图片出错,{}", snapshotUri, ExceptionUtils.getStackTrace(e));
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
