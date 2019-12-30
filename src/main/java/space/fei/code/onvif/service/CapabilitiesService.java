package space.fei.code.onvif.service;


import org.apache.commons.lang3.StringUtils;
import space.fei.code.onvif.discovery.SingleIPCDiscovery;
import space.fei.code.onvif.model.OnvifDeviceInfo;
import space.fei.code.onvif.model.ProfileInfo;
import space.fei.code.onvif.model.UsernameToken;
import space.fei.code.onvif.util.OkHttpUtils;
import space.fei.code.onvif.util.XMLUtils;


import java.util.ArrayList;
import java.util.List;

public class CapabilitiesService {

    private static OkHttpUtils okHttpUtils = new OkHttpUtils();

    final private static String GET_CAPABILITIES_XML =
           "<?xml version=\"1.0\" encoding=\"utf - 8\"?>\n" +
           "<s:Envelope\n" +
           "    xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
           "    <s:Body\n" +
           "        xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
           "        xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">\n" +
           "        <GetCapabilities xmlns=\"http://www.onvif.org/ver10/device/wsdl\">\n" +
           "\t\t\t<Category>All</Category>\n" +
           "\t\t</GetCapabilities>\n" +
           "\t</s:Body>\n" +
           "</s:Envelope>";

    private static String getGetCapabilitiesXml(UsernameToken usernameToken) {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<s:Envelope\n" +
                "    xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                "    <s:Header>\n" +
                "        <wsse:Security\n" +
                "            xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\"\n" +
                "            xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\n" +
                "            <wsse:UsernameToken>\n" +
                "                <wsse:Username>" + usernameToken.getUsername() + "</wsse:Username>\n" +
                "                <wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest\">" + usernameToken.getPassword() + "</wsse:Password>\n" +
                "                <wsse:Nonce>" + usernameToken.getNonce() + "</wsse:Nonce>\n" +
                "                <wsu:Created>" + usernameToken.getCreated() + "</wsu:Created>\n" +
                "            </wsse:UsernameToken>\n" +
                "        </wsse:Security>\n" +
                "    </s:Header>\n" +
                "    <s:Body\n" +
                "        xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "        xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">\n" +
                "        <GetCapabilities\n" +
                "            xmlns=\"http://www.onvif.org/ver10/device/wsdl\">\n" +
                "            <Category>All</Category>\n" +
                "        </GetCapabilities>\n" +
                "    </s:Body>\n" +
                "</s:Envelope>";
    }

    private static String getMediaXml(UsernameToken usernameToken) {
        return "<?xml version=\"1.0\" encoding=\"utf - 8\"?>\n" +
                "<s:Envelope\n" +
                "    xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                "    <s:Header>\n" +
                "        <wsse:Security\n" +
                "            xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\"\n" +
                "            xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\n" +
                "            <wsse:UsernameToken>\n" +
                "                <wsse:Username>" + usernameToken.getUsername() + "</wsse:Username>\n" +
                "                <wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest\">" + usernameToken.getPassword() + "</wsse:Password>\n" +
                "                <wsse:Nonce>" + usernameToken.getNonce() + "</wsse:Nonce>\n" +
                "                <wsu:Created>" + usernameToken.getCreated() + "</wsu:Created>\n" +
                "            </wsse:UsernameToken>\n" +
                "        </wsse:Security>\n" +
                "    </s:Header>\n" +
                "    <s:Body\n" +
                "        xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "        xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">\n" +
                "        <GetProfiles\n" +
                "            xmlns=\"http://www.onvif.org/ver10/media/wsdl\"/>\n" +
                "    </s:Body>\n" +
                "</s:Envelope>";
    }

    private static String getStreamUri(String profileToken, UsernameToken usernameToken) {
        return "<?xml version=\"1.0\" encoding=\"utf - 8\"?>\n" +
                "<s:Envelope\n" +
                "    xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                "    <s:Header>\n" +
                "        <wsse:Security\n" +
                "            xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\"\n" +
                "            xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\n" +
                "            <wsse:UsernameToken>\n" +
                "                <wsse:Username>" + usernameToken.getUsername() + "</wsse:Username>\n" +
                "                <wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest\">" + usernameToken.getPassword() + "</wsse:Password>\n" +
                "                <wsse:Nonce>" + usernameToken.getNonce() + "</wsse:Nonce>\n" +
                "                <wsu:Created>" + usernameToken.getCreated() + "</wsu:Created>\n" +
                "            </wsse:UsernameToken>\n" +
                "        </wsse:Security>\n" +
                "    </s:Header>\n" +
                "    <s:Body\n" +
                "        xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "        xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">\n" +
                "        <GetStreamUri\n" +
                "            xmlns=\"http://www.onvif.org/ver10/media/wsdl\">\n" +
                "            <StreamSetup>\n" +
                "                <Stream\n" +
                "                    xmlns=\"http://www.onvif.org/ver10/schema\">RTP-Unicast</Stream>\n" +
                "                <Transport\n" +
                "                    xmlns=\"http://www.onvif.org/ver10/schema\">\n" +
                "                    <Protocol>UDP</Protocol>\n" +
                "                </Transport>\n" +
                "            </StreamSetup>\n" +
                "            <ProfileToken>" + profileToken + "</ProfileToken>\n" +
                "        </GetStreamUri>\n" +
                "    </s:Body>\n" +
                "</s:Envelope>";
    }

    private static String getSnapshotUri(String profileToken, UsernameToken usernameToken) {
        return "<env:Envelope\n" +
                "    xmlns:env=\"http://www.w3.org/2003/05/soap-envelope\"\n" +
                "    xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\"\n" +
                "    xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\n" +
                "    <env:Header>\n" +
                "        <wsse:Security>\n" +
                "            <wsse:UsernameToken>\n" +
                "                <wsse:Username>" + usernameToken.getUsername() + "</wsse:Username>\n" +
                "                <wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest\">" + usernameToken.getPassword() + "</wsse:Password>\n" +
                "                <wsse:Nonce EncodingType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary\">" + usernameToken.getNonce() + "</wsse:Nonce>\n" +
                "                <wsu:Created>" + usernameToken.getCreated() + "</wsu:Created>\n" +
                "            </wsse:UsernameToken>\n" +
                "        </wsse:Security>\n" +
                "    </env:Header>\n" +
                "    <env:Body>\n" +
                "        <GetSnapshotUri\n" +
                "            xmlns=\"http://www.onvif.org/ver10/media/wsdl\">\n" +
                "            <ProfileToken>" + profileToken + "</ProfileToken>\n" +
                "        </GetSnapshotUri>\n" +
                "    </env:Body>\n" +
                "</env:Envelope>";
    }


    public static String getMediaUrl(OnvifDeviceInfo onvifDeviceInfo, UsernameToken usernameToken) throws Exception{

        String mediaUrl = "";
        if (StringUtils.isEmpty(onvifDeviceInfo.getOnvifAddress())) {
            SingleIPCDiscovery.fillOnvifAddress(onvifDeviceInfo);
        }
        String returnXmlContent = okHttpUtils.okHttp3XmlPost(onvifDeviceInfo.getOnvifAddress(), getGetCapabilitiesXml(usernameToken));
        mediaUrl = XMLUtils.parseMediaUrl(returnXmlContent);
        return mediaUrl;
    }

    public static List<ProfileInfo> getProfiles(String mediaUrl, UsernameToken usernameToken) throws Exception {
        List<ProfileInfo> profiles = new ArrayList<>();
        if (StringUtils.isEmpty(usernameToken.getUsername()) || StringUtils.isEmpty(usernameToken.getPassword())) {
            throw new RuntimeException("onvif设备的用户名或者密码有误");
        }

        String sendXml = getMediaXml(usernameToken);
        String returnXmlContent = okHttpUtils.okHttp3XmlPost(mediaUrl, sendXml);
        profiles = XMLUtils.parseProfiles(returnXmlContent);

        return profiles;
    }

    public static String getStreamUri(String mediaUrl, String profile, UsernameToken usernameToken) throws Exception {
        String streamUri = "";
        String sendXml = getStreamUri(profile, usernameToken);
        String returnXmlContent = okHttpUtils.okHttp3XmlPost(mediaUrl, sendXml);
        streamUri = XMLUtils.parseStreamUri(returnXmlContent);
        return streamUri;
    }

    public static String getSnapshotUri(String mediaUrl, String profile, UsernameToken usernameToken) throws Exception {
        String snapshotUrl = "";
        String sendXml = getSnapshotUri(profile, usernameToken);
        String returnXmlContent = okHttpUtils.okHttp3XmlPost(mediaUrl, sendXml);
        snapshotUrl = XMLUtils.parseSnapshotUri(returnXmlContent);
        return snapshotUrl;
    }

}