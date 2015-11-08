package al.arra.api.flickr.crypt;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import al.arra.api.flickr.exception.OAuthSignatureException;
import al.arra.api.flickr.util.OAuthEncoder;
import al.arra.api.flickr.util.Validation;

/**
 * Created by Gezim on 11/7/2015.
 */
public class HMACSha1Signature {
    private static final String EMPTY_STRING = "";
    private static final String CARRIAGE_RETURN = "\r\n";
    private static final String UTF8 = "UTF-8";
    private static final String HMAC_SHA1 = "HmacSHA1";
    private static final String METHOD = "HMAC-SHA1";

    /**
     * {@inheritDoc}
     */
    public String getSignature(String baseString, String apiSecret, String tokenSecret)
    {
        try
        {
            Validation.checkEmptyString(baseString, "Base string cant be null or empty string");
            Validation.checkEmptyString(apiSecret, "Api secret cant be null or empty string");
            return doSign(baseString, OAuthEncoder.encode(apiSecret) + '&' + OAuthEncoder.encode(tokenSecret));
        }
        catch (Exception e)
        {
            throw new OAuthSignatureException(baseString, e);
        }
    }

    private String doSign(String toSign, String keyString) throws Exception
    {
        SecretKeySpec key = new SecretKeySpec((keyString).getBytes(UTF8), HMAC_SHA1);
        Mac mac = Mac.getInstance(HMAC_SHA1);
        mac.init(key);
        byte[] bytes = mac.doFinal(toSign.getBytes(UTF8));
        return bytesToBase64String(bytes).replace(CARRIAGE_RETURN, EMPTY_STRING);
    }

    private String bytesToBase64String(byte[] bytes)
    {
        return Base64Encoder.getInstance().encode(bytes);
    }

    /**
     * {@inheritDoc}
     */
    public String getSignatureMethod()
    {
        return METHOD;
    }
}
