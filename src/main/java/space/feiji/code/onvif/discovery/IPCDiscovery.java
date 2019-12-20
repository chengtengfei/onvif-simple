package space.feiji.code.onvif.discovery;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import space.feiji.code.onvif.util.NetworkUtils;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.Random;
import java.util.UUID;

public class IPCDiscovery {

    private final static Logger LOGGER = LoggerFactory.getLogger(IPCDiscovery.class);

    private static final String MULTICAST_IP = "239.255.255.250";
    private static final Integer MULTICAST_PORT = 3702;

    private static final String SOAP_CONTENT = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<e:Envelope xmlns:e=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:w=\"http://schemas.xmlsoap.org/ws/2004/08/addressing\" \n" +
            "\txmlns:d=\"http://schemas.xmlsoap.org/ws/2005/04/discovery\" xmlns:dn=\"http://www.onvif.org/ver10/network/wsdl\">\n" +
            "\t<e:Header>\n" +
            "\t\t<w:MessageID>" +
//            "uuid:84ede3de-7dec-11d0-c360-f01234567890" +
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
    public static MulticastSocket createMulticastGroupAndJoin(String groupUrl, int port, String localIp) {
        try {
            InetAddress group = InetAddress.getByName(groupUrl);
            MulticastSocket socket = new MulticastSocket(port);
            socket.setNetworkInterface(NetworkInterface.getByInetAddress(InetAddress.getByName(localIp)));
            // 设置组播数据报的发送范围为本地网络
//            socket.setTimeToLive(1);
            // 设置套接字的接收数据报的最长时间
            socket.setSoTimeout(30000);
            // 加入此组播组
            socket.joinGroup(group);
            return socket;
        } catch (Exception e1) {
            LOGGER.error("createMulticastGroupAndJoin----->>>>Error: {}",  e1);
            return null;
        }
    }

    /**
     * 向组播组发送数据的函数
     * @param socket
     * @param data
     * @param groupUrl
     */
    public static void sendData(MulticastSocket socket, byte[] data,String groupUrl, int port) {
        try {
            InetAddress group = InetAddress.getByName(groupUrl);
            // 存储在数组中
            DatagramPacket packet = new DatagramPacket(data, data.length, group, port);
            // 通过MulticastSocket实例端口向组播组发送数据
            socket.send(packet);
            LOGGER.info("------->>>>>以UDP形式发送组播报文");
        } catch (Exception e1) {
            LOGGER.error("sendData------>>>>>Error: {}", e1);
        }
    }

    public static String receiveData(MulticastSocket socket,String groupUrl, int port) {
        String message;
        try {
            InetAddress group=InetAddress.getByName(groupUrl);
            byte[] data = new byte[1024 * 2];
            DatagramPacket packet = new DatagramPacket(data, data.length, group, port);
            // 通过MulticastSocket实例端口从组播组接收数据
            socket.receive(packet);
            // 将接受的数据转换成字符串形式
            message = new String(packet.getData());
        } catch (Exception e1) {
            LOGGER.error("receiveData------>>>>>Error: {}", e1);
            message = "Error: " + e1;
        }
        return message;
    }

    public static void start() throws Exception {
        //加入组播组，设置组播组的监听端口
        Random random = new Random();
        int port = random.nextInt(65535 - 1024 + 1) + 1024;
        String localIp = NetworkUtils.getLocalHostLANAddress().getHostAddress();
        final MulticastSocket socket = createMulticastGroupAndJoin(MULTICAST_IP, port, localIp);
        //向组播组发送数据
        sendData(socket, SOAP_CONTENT.getBytes(), MULTICAST_IP, MULTICAST_PORT);
        while (true) {
            //接收组播组传来的消息
            String message = receiveData(socket, MULTICAST_IP, MULTICAST_PORT);
            LOGGER.info("------------------------------------------->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            LOGGER.info(message);
        }
    }


    public static void main(String[] args) throws Exception {

        start();
    }
}
