package space.fei.code;

import space.fei.code.onvif.discovery.IPCDiscovery;
import space.fei.code.onvif.discovery.SingleIPCDiscovery;
import space.fei.code.onvif.model.OnvifDeviceInfo;
import space.fei.code.onvif.model.ProfileInfo;
import space.fei.code.onvif.service.OnvifService;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) throws Exception {
        Logger.getGlobal().setLevel(Level.INFO);
        List<OnvifDeviceInfo> onvifDeviceInfoList = IPCDiscovery.discovery("192.168.101.47");
        for (OnvifDeviceInfo onvifDeviceInfo : onvifDeviceInfoList) {
            System.out.println(onvifDeviceInfo);
        }
        System.out.println(SingleIPCDiscovery.probeOnvifByIp("192.168.88.56"));
        OnvifDeviceInfo onvifDeviceInfo = new OnvifDeviceInfo();
        onvifDeviceInfo.setIp("192.168.88.56");
        onvifDeviceInfo.setOnvifAddress(SingleIPCDiscovery.probeOnvifByIp("192.168.88.56"));
        onvifDeviceInfo.setUsername("admin");
        onvifDeviceInfo.setPassword("tc123456");
        List<ProfileInfo> profileInfoList = OnvifService.getVideoInfo(onvifDeviceInfo);
        for (ProfileInfo profileInfo : profileInfoList) {
            System.out.println(profileInfo);
        }
    }
}
