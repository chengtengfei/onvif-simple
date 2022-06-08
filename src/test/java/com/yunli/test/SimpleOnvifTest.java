package com.yunli.test;

import com.github.chengtengfei.onvif.discovery.IPCDiscovery;
import com.github.chengtengfei.onvif.discovery.SingleIPCDiscovery;
import com.github.chengtengfei.onvif.model.OnvifDeviceInfo;
import com.github.chengtengfei.onvif.model.ProfileInfo;
import com.github.chengtengfei.onvif.service.OnvifService;

import java.util.List;

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
            onvifDeviceInfo.setIp("192.168.2.240");
            onvifDeviceInfo.setUsername("jiangtao");
            onvifDeviceInfo.setPassword("admin123");
            SingleIPCDiscovery.fillOnvifAddress(onvifDeviceInfo);

            List<ProfileInfo> profileInfoList = OnvifService.getVideoInfo(onvifDeviceInfo);

            for(ProfileInfo profileInfo : profileInfoList) {
                System.out.println(profileInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
