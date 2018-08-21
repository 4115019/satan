package he.bao;

import sun.misc.BASE64Encoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by huangpin on 17/7/21.
 */
public class TestMain {
    public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec keySpec = new SecretKeySpec("wek4tLBpb05R07A7XXGV6mnJcnZfKZx2".trim().getBytes("UTF-8"), "HmacSHA1");
        Mac localMac = Mac.getInstance("HmacSHA1");
        localMac.init(keySpec);
        System.out.println(new BASE64Encoder().encode(localMac.doFinal("<HEAD><TXNCD>900000</TXNCD><SERLNO>1500604045058</SERLNO><PLAT>32</PLAT><UA>Android</UA><VERSION>1.1.26</VERSION><SOURCE>10000</SOURCE></HEAD><BODY></BODY>".getBytes("UTF-8"))));
        System.out.println(new BASE64Encoder().encode(localMac.doFinal("<HEAD><TXNCD>900110</TXNCD><SERLNO>1500603444370</SERLNO><SESSIONID>c22f98a7-b967-4698-9aee-7fccad277bbd</SESSIONID><PLAT>32</PLAT><UA>Android</UA><VERSION>1.1.26</VERSION><SOURCE>10000</SOURCE></HEAD><BODY><USRMBLNO>18210036590</USRMBLNO><USRMBLNO>18210036590</USRMBLNO></BODY>".getBytes("UTF-8"))));
    }
}
