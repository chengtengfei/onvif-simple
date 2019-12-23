package space.fei.code.onvif.util;

import org.apache.commons.lang3.exception.ExceptionUtils;
import space.fei.code.onvif.diagnostics.logging.Logger;
import space.fei.code.onvif.diagnostics.logging.Loggers;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class NetworkUtils {

    private final static Logger LOGGER = Loggers.getLogger(NetworkUtils.class);

    @SuppressWarnings("unchecked")
    public static Map getAllNetAddress() {
        Map<String, String> all = new HashMap();
        try {
            // 获得本机的所有网络接口
            Enumeration<NetworkInterface> nifs = NetworkInterface.getNetworkInterfaces();
            while (nifs.hasMoreElements()) {
                NetworkInterface nif = nifs.nextElement();
                // 获得与该网络接口绑定的 IP 地址，一般只有一个
                Enumeration<InetAddress> addresses = nif.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    all.put(address.getHostAddress(), nif.getName());
                }
            }
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return all;
    }

    /**
     * 正确的IP拿法，即优先拿site-local地址
     * @return
     * @throws UnknownHostException
     */
    public static InetAddress getLocalHostLANAddress() throws UnknownHostException {
        try {
            InetAddress candidateAddress = null;
            // 遍历所有的网络接口
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                // 在所有的接口下再遍历IP
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    // 排除loopback类型地址
                    if (!inetAddr.isLoopbackAddress()) {
                        if (inetAddr.isSiteLocalAddress()) {
                            // 如果是site-local地址，就是它了
                            return inetAddr;
                        } else if (candidateAddress == null) {
                            // site-local类型的地址未被发现，先记录候选地址
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress;
            }
            // 如果没有发现 non-loopback地址.只能用最次选的方案
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
            }
            return jdkSuppliedAddress;
        } catch (Exception e) {
            UnknownHostException unknownHostException = new UnknownHostException(
                    "Failed to determine LAN address: " + e);
            unknownHostException.initCause(e);
            throw unknownHostException;
        }
    }

    public static long getMachineId()  {

        try {
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            long id;
            if (network == null) {
                id = 1;
            } else {
                byte[] mac = network.getHardwareAddress();
                id =
                        ((0x000000FF & (long) mac[mac.length - 1]) |
                                (0x0000FF00 & (((long) mac[mac.length - 2]) << 8))) >> 6;
            }
            return id;
        } catch (Exception e) {
            LOGGER.error("获取机器ID出错, " + ExceptionUtils.getStackTrace(e));
            return 10080L;
        }
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        Map<String, String> all = getAllNetAddress();
        for (Map.Entry<String, String> entry : all.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }

        InetAddress address = InetAddress.getLocalHost();
        System.out.println("Local HostAddress: " + address.getHostAddress() + ", Local host name: " +  address.getHostName());

        System.out.println("getLocalHostLANAddress:" + getLocalHostLANAddress().getHostAddress());

        System.out.println("machineId = " + getMachineId());
    }
}
