package space.feiji.code.onvif.util;

import org.apache.commons.codec.binary.Base64;

import java.nio.charset.Charset;

public class HttpAuths {

    public static String baseAuthGenerate(String username, String pwd) {
        String auth = username + ":" + pwd;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("UTF-8")));
        return "Basic " + new String(encodedAuth);
    }

    public static void main(String[] args) {
        System.out.println(baseAuthGenerate("admin", "admin"));
    }
}
