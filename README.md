
[English](README-EN.md)

# onvif-simple

实现了部分Onvif Profile S 标准。

## Getting Started

这是一个用Java写的maven jar包，你可以方便的在使用maven构建的项目中使用它。
```
<dependency>
    <groupId>com.github.chengtengfei</groupId>
    <artifactId>onvif-sample</artifactId>
    <version>0.0.4</version>
</dependency>
```



### How to use it

需要引入的包

```
import space.fei.code.onvif.discovery.IPCDiscovery;
import space.fei.code.onvif.discovery.SingleIPCDiscovery;
import space.fei.code.onvif.model.OnvifDeviceInfo;
import space.fei.code.onvif.model.ProfileInfo;
import space.fei.code.onvif.service.OnvifService;
```

使用你计算机的默认IP自动发现设备（会发现和你计算机IP所在同一局域网的设备）。
```
try {
    List<OnvifDeviceInfo> onvifDeviceInfoList = IPCDiscovery.discovery();
    System.out.println(onvifDeviceInfoList.toString());
} catch (Exception e) {
    e.printStackTrace();
}
```

使用指定的IP自动发现设备（适合多网卡计算机，指定其中一个网卡IP来发现同一局域网下的设备）。
```
try {
    List<OnvifDeviceInfo> onvifDeviceInfoList = IPCDiscovery.discovery("192.168.101.1");
    System.out.println(onvifDeviceInfoList.toString());
} catch (Exception e) {
    e.printStackTrace();
}
```

获取Onvif服务地址.
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

获取设备媒体信息。
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
