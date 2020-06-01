
[中文](README.md)

# onvif-simple

Implement part of the Onvif Profile-S specification. 

## Getting Started

This is a maven-jar project writed by Java, and you can easily use it in your project builded by maven.

```
<dependency>
    <groupId>com.github.chengtengfei</groupId>
    <artifactId>onvif-sample</artifactId>
    <version>0.0.4</version>
</dependency>
```



### How to use it

With import package

```
import com.github.chengtengfei.onvif.discovery.IPCDiscovery;
import com.github.chengtengfei.onvif.discovery.SingleIPCDiscovery;
import com.github.chengtengfei.onvif.model.OnvifDeviceInfo;
import com.github.chengtengfei.onvif.model.ProfileInfo;
import com.github.chengtengfei.onvif.service.OnvifService;
```

Auto discovery onvif device by your defalut computer IP.
```
try {
    List<OnvifDeviceInfo> onvifDeviceInfoList = IPCDiscovery.discovery();
    System.out.println(onvifDeviceInfoList.toString());
} catch (Exception e) {
    e.printStackTrace();
}
```

Auto discovery onvif device by a appointed IP.
```
try {
    List<OnvifDeviceInfo> onvifDeviceInfoList = IPCDiscovery.discovery("192.168.101.1");
    System.out.println(onvifDeviceInfoList.toString());
} catch (Exception e) {
    e.printStackTrace();
}
```

Get onvif device address.
```
try {
    OnvifDeviceInfo onvifDeviceInfo = new OnvifDeviceInfo();
    onvifDeviceInfo.setIp("192.168.101.234");
    onvifDeviceInfo.setUsername("admin");
    onvifDeviceInfo.setPassword("admin");
    SingleIPCDiscovery.fillOnvifAddress(onvifDeviceInfo);
    System.out.println(onvifDeviceInfo);
} catch (Exception e) {
    e.printStackTrace();
}

// Output : OnvifDeviceInfo{ip='192.168.101.234', onvifAddress='http://192.168.101.234:2000/onvif/device_service', username='admin', password='admin'}
```

Get onvif device profile
```
try {
    OnvifDeviceInfo onvifDeviceInfo = new OnvifDeviceInfo();
    onvifDeviceInfo.setIp("192.168.101.234");
    onvifDeviceInfo.setUsername("admin");
    onvifDeviceInfo.setPassword("admin");
    SingleIPCDiscovery.fillOnvifAddress(onvifDeviceInfo);
    List<ProfileInfo> profileInfoList = OnvifService.getVideoInfo(onvifDeviceInfo);
    System.out.println(profileInfoList);
} catch (Exception e) {
    e.printStackTrace();
}

// Output : [ProfileInfo{name='profile0', token='profile0', videoInfo=VideoInfo{videoEncoding='H264', videoWidth=1920, videoHeight=1080, frameRateLimit=25, bitrateLimit=4096, streamUri='rtsp://admin:admin@192.168.101.234:554/av0_0'}}, ProfileInfo{name='profile1', token='profile1', videoInfo=VideoInfo{videoEncoding='H264', videoWidth=704, videoHeight=576, frameRateLimit=25, bitrateLimit=1024, streamUri='rtsp://admin:admin@192.168.101.234:554/av0_1'}}]

```




## Authors

* **Cheng Tengfei**  - [Github](https://github.com/chengtengfei)

## License

This project is licensed under the Apache 2.0 License.

## Acknowledgments

* [Onvif Profile S Specification](https://www.onvif.org/profiles/profile-s/)
* [Onvif Profile Ver 10 XML Content Schema](https://www.onvif.org/ver10/schema/onvif.xsd)
