package com.yunli.test;

import com.github.chengtengfei.onvif.discovery.SingleIPCDiscovery;
import com.github.chengtengfei.onvif.model.OnvifDeviceInfo;
import com.github.chengtengfei.onvif.model.ProfileInfo;
import com.github.chengtengfei.onvif.service.OnvifService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class SimpleOnvifTest {

    public static void main(String[] args) {


//        try {
//            List<OnvifDeviceInfo> onvifDeviceInfoList = IPCDiscovery.discovery();
//            for(OnvifDeviceInfo onvifDeviceInfo: onvifDeviceInfoList) {
//                if(onvifDeviceInfo.getIp() != null && !onvifDeviceInfo.getIp().equals("")) {
//                    System.out.println(onvifDeviceInfo);
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        try {
//            String oo = SingleIPCDiscovery.probeOnvifByIp("192.168.2.240");
//            System.out.println(oo);
            OnvifDeviceInfo onvifDeviceInfo = new OnvifDeviceInfo();
//            onvifDeviceInfo.setIp("192.168.2.240");//dahua
            onvifDeviceInfo.setIp("192.168.1.65");//hk
//            onvifDeviceInfo.setIp("192.166.8.10");//hk wuhan
            onvifDeviceInfo.setUsername("jiangtao");
            onvifDeviceInfo.setPassword("admin123");
            SingleIPCDiscovery.fillOnvifAddress(onvifDeviceInfo);

            List<ProfileInfo> profileInfoList = OnvifService.getVideoInfo(onvifDeviceInfo);

            for(ProfileInfo profileInfo : profileInfoList) {
                log.debug("{}", profileInfo);
//                System.out.println(profileInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
