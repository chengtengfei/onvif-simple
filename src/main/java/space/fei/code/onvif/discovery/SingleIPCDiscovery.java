package space.fei.code.onvif.discovery;

import org.apache.commons.lang3.StringUtils;
import space.fei.code.onvif.diagnostics.logging.Logger;
import space.fei.code.onvif.diagnostics.logging.Loggers;
import space.fei.code.onvif.model.OnvifDeviceInfo;
import space.fei.code.onvif.util.RegexUtils;
import space.fei.code.onvif.util.XMLUtils;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;
import java.util.UUID;


public class SingleIPCDiscovery {

    private final static Logger LOGGER = Loggers.getLogger(SingleIPCDiscovery.class);

    /**
     * 设置接收数据的超时时间
     */
    private static final int TIMEOUT = 3000;
    /**
     * 设置重发数据的最多次数
     */
    private static final int MAXNUM = 2;
    private static final String SOAP_CONTENT =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<e:Envelope xmlns:e=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:w=\"http://schemas.xmlsoap.org/ws/2004/08/addressing\"" +
            "    xmlns:d=\"http://schemas.xmlsoap.org/ws/2005/04/discovery\" xmlns:dn=\"http://www.onvif.org/ver10/network/wsdl\">" +
            "   <e:Header>" +
            "       <w:MessageID>" +
            "           uuid:" + UUID.randomUUID().toString() +
            "       </w:MessageID>" +
            "       <w:To e:mustUnderstand=\"true\">urn:schemas-xmlsoap-org:ws:2005:04:discovery</w:To>" +
            "       <w:Action a:mustUnderstand=\"true\">http://schemas.xmlsoap.org/ws/2005/04/discovery/Probe</w:Action>" +
            "   </e:Header>" +
            "   <e:Body>" +
            "   <d:Probe>" +
            "       <d:Types>dn:NetworkVideoTransmitter</d:Types>" +
            "   </d:Probe>" +
            "   </e:Body>" +
            "</e:Envelope>";

    private static String getOnvifAddressByIp(String ip) throws Exception {
        byte[] buf = new byte[10240];
        // 客户端在随机端口监听接收到的数据
        Random random = new Random();
        DatagramSocket ds = new DatagramSocket(random.nextInt(65535 - 1024 + 1) + 1024);
        InetAddress ipcIp = InetAddress.getByName(ip);
        //定义用来发送数据的DatagramPacket实例
        DatagramPacket dpSend= new DatagramPacket(SOAP_CONTENT.getBytes(), SOAP_CONTENT.length(), ipcIp,3702);
        //定义用来接收数据的DatagramPacket实例
        DatagramPacket dpReceive = new DatagramPacket(buf, 10240);
        //设置接收数据时阻塞的最长时间
        ds.setSoTimeout(TIMEOUT);
        //重发数据的次数
        int tries = 0;
        //是否接收到数据的标志位
        boolean receivedResponse = false;
        //直到接收到数据，或者重发次数达到预定值，则退出循环
        while(!receivedResponse && tries<MAXNUM){
            //发送数据
            ds.send(dpSend);
            try{
                //接收从服务端发送回来的数据
                ds.receive(dpReceive);
                //如果接收到的数据不是来自目标地址，则抛出异常
                if(!dpReceive.getAddress().equals(ipcIp)){
                    throw new IOException("Received packet from an umknown source");
                }
                //如果接收到数据。则将receivedResponse标志位改为true，从而退出循环
                receivedResponse = true;
            }catch(InterruptedIOException e){
                //如果接收数据时阻塞超时，重发并减少一次重发的次数
                tries += 1;
                LOGGER.debug("Time out, " + (MAXNUM - tries) + " more tries...");
            }
        }
        String receiveContent = "";
        if(receivedResponse){
            //如果收到数据，则打印出来
            receiveContent = new String(dpReceive.getData(),0,dpReceive.getLength());
        }else{
            //如果重发MAXNUM次数据后，仍未获得服务器发送回来的数据，则打印如下信息
            LOGGER.debug("No response -- give up.");
        }
        ds.close();

        return receiveContent;
    }

    public static void fillOnvifAddress(OnvifDeviceInfo onvifDeviceInfo) throws Exception {
        if (StringUtils.isEmpty(onvifDeviceInfo.getIp()) || !RegexUtils.isIp(onvifDeviceInfo.getIp())) {
            throw new RuntimeException("onvif 设备的ip不合法");
        }

        String onvifAddress = null;
        String xmlContent = getOnvifAddressByIp(onvifDeviceInfo.getIp());
        String onvifAddressAll = XMLUtils.parseOnvifAddress(xmlContent);
        onvifAddress = onvifAddressAll.trim().split(" ")[0];

        onvifDeviceInfo.setOnvifAddress(onvifAddress);
//        return onvifDeviceInfo;
    }

    public static String probeOnvifByIp(String ip) throws Exception {
        if (StringUtils.isEmpty(ip) || !RegexUtils.isIp(ip)) {
            throw new Exception("不正确的IP--" + ip);
        }

        return XMLUtils.parseOnvifAddress(getOnvifAddressByIp(ip));
    }


}
