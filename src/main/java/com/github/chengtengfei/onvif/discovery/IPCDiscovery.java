package com.github.chengtengfei.onvif.discovery;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import com.github.chengtengfei.onvif.diagnostics.logging.Logger;
import com.github.chengtengfei.onvif.diagnostics.logging.Loggers;
import com.github.chengtengfei.onvif.model.OnvifDeviceInfo;
import com.github.chengtengfei.onvif.util.NetworkUtils;
import com.github.chengtengfei.onvif.util.RegexUtils;
import com.github.chengtengfei.onvif.util.XMLUtils;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;


public class IPCDiscovery {

    private final static Logger LOGGER = Loggers.getLogger(IPCDiscovery.class);

    private static final String MULTICAST_IP = "239.255.255.250";
    private static final Integer MULTICAST_PORT = 3702;
    private static final int SOCKET_TIMEOUT_MILL_SECONDS = 1000;
    private static final int DISCOVERY_TIMEOUT_MILL_SECONDS = 3000;

    private static final String SOAP_CONTENT = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<e:Envelope xmlns:e=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:w=\"http://schemas.xmlsoap.org/ws/2004/08/addressing\" \n" +
            "\txmlns:d=\"http://schemas.xmlsoap.org/ws/2005/04/discovery\" xmlns:dn=\"http://www.onvif.org/ver10/network/wsdl\">\n" +
            "\t<e:Header>\n" +
            "\t\t<w:MessageID>" +
            "uuid:" + UUID.randomUUID().toString() +
            "</w:MessageID>\n" +
            "\t\t\t<w:To e:mustUnderstand=\"true\">urn:schemas-xmlsoap-org:ws:2005:04:discovery</w:To>\n" +
            "\t\t\t<w:Action a:mustUnderstand=\"true\">http://schemas.xmlsoap.org/ws/2005/04/discovery/Probe</w:Action>\n" +
            "\t</e:Header>\n" +
            "\t<e:Body>\n" +
            "\t\t<d:Probe>\n" +
            "\t\t\t<d:Types>dn:NetworkVideoTransmitter</d:Types>\n" +
            "\t\t</d:Probe>\n" +
            "\t</e:Body>\n" +
            "</e:Envelope>";


    /**
     * 创建一个组播组并加入此组的函数
     * @param groupUrl
     * @param port
     * @return
     */
    private static MulticastSocket createMulticastGroupAndJoin(String groupUrl, int port, String localIp) {
        try {
            InetAddress group = InetAddress.getByName(groupUrl);
            MulticastSocket socket = new MulticastSocket(port);
            socket.setNetworkInterface(NetworkInterface.getByInetAddress(InetAddress.getByName(localIp)));
            // 设置组播数据报的发送范围为本地网络
//            socket.setTimeToLive(1);
            // 设置套接字的接收数据报的最长时间
            socket.setSoTimeout(SOCKET_TIMEOUT_MILL_SECONDS);
            // 加入此组播组
            socket.joinGroup(group);
            return socket;
        } catch (Exception e1) {
            LOGGER.warn("createMulticastGroupAndJoin----->>>>Error: {}",  e1);
            return null;
        }
    }

    /**
     * 向组播组发送数据的函数
     * @param socket
     * @param data
     * @param groupUrl
     */
    private static void sendData(MulticastSocket socket, byte[] data,String groupUrl, int port) {
        try {
            InetAddress group = InetAddress.getByName(groupUrl);
            // 存储在数组中
            DatagramPacket packet = new DatagramPacket(data, data.length, group, port);
            // 通过MulticastSocket实例端口向组播组发送数据
            socket.send(packet);
            // LOGGER.info("------->>>>>以UDP形式发送组播报文");
        } catch (Exception e1) {
            LOGGER.error("sendData------>>>>>Error: {}", e1);
        }
    }

    private static String receiveData(MulticastSocket socket,String groupUrl, int port) {
        String message;
        try {
            InetAddress group=InetAddress.getByName(groupUrl);
            byte[] data = new byte[1024 * 2];
            DatagramPacket packet = new DatagramPacket(data, data.length, group, port);
            // 通过MulticastSocket实例端口从组播组接收数据
            socket.receive(packet);
            // 将接受的数据转换成字符串形式
            message = new String(packet.getData());
        } catch (Exception e) {
            // LOGGER.error("receiveData------>>>>>Error: {}", e);
            return "";
        }
        return message;
    }

    public static List<OnvifDeviceInfo> discovery(String localIp) throws Exception {
        if (StringUtils.isEmpty(localIp) || !RegexUtils.isIp(localIp)) {
            throw new Exception("不正确的IP--" + localIp);
        }
        return start(localIp);
    }

    public static List<OnvifDeviceInfo> discovery() throws Exception {
        return start("");
    }



    private static List<OnvifDeviceInfo> start(String localIp) throws Exception {
        // 加入组播组，设置组播组的监听端口
        Random random = new Random();
        int port = random.nextInt(65535 - 1024 + 1) + 1024;
        if (StringUtils.isEmpty(localIp)) {
            localIp = NetworkUtils.getLocalHostLANAddress().getHostAddress();
            LOGGER.debug("discovery ipc use ip [" + localIp + "]");
        }
        final MulticastSocket socket = createMulticastGroupAndJoin(MULTICAST_IP, port, localIp);
        // 向组播组发送数据
        sendData(socket, SOAP_CONTENT.getBytes(), MULTICAST_IP, MULTICAST_PORT);
        List<String> receiveDataList = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        while (true) {
            //接收组播组传来的消息
            String message = receiveData(socket, MULTICAST_IP, MULTICAST_PORT);
            if (StringUtils.isEmpty(message)) {
                break;
            }
            LOGGER.debug( message);
            receiveDataList.add(message);
            if (System.currentTimeMillis() - startTime > DISCOVERY_TIMEOUT_MILL_SECONDS) {
                break;
            }
        }

        List<OnvifDeviceInfo> onvifDeviceInfoList = new ArrayList<>();
        for (String onvifMessage : receiveDataList) {
            OnvifDeviceInfo onvifDeviceInfo = new OnvifDeviceInfo();
            try {
                String onvifAddress = XMLUtils.parseOnvifAddress(onvifMessage);
                onvifDeviceInfo.setOnvifAddress(onvifAddress);
                onvifDeviceInfo.setIp(RegexUtils.extractIpFromString(onvifAddress));
            } catch (Exception e) {
                LOGGER.info("解析地址[" + onvifMessage + "]出错, " + ExceptionUtils.getStackTrace(e));
                onvifDeviceInfo.setOnvifAddress("");
                onvifDeviceInfo.setIp("");
            }
            onvifDeviceInfoList.add(onvifDeviceInfo);
        }

        return onvifDeviceInfoList;
    }


    public static void main(String[] args) throws Exception {

        System.out.println(discovery("192.168.101.47"));
    }
}
