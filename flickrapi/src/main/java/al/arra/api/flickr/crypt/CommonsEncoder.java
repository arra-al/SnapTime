package al.arra.api.flickr.crypt;

//import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;

import al.arra.api.flickr.exception.OAuthSignatureException;

/**
 * Created by Gezim on 11/7/2015.
 */
public class CommonsEncoder extends Base64Encoder
{

    @Override
    public String encode(byte[] bytes)
    {
/*        byte[] encBytes = Base64.encode(bytes, Base64.DEFAULT);
        try {
            return new String(encBytes, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            throw new OAuthSignatureException("Can't perform base64 encoding", e);
        } catch (NullPointerException e) {
            throw new OAuthSignatureException("Can't perform base64 encoding. Null value", e);
        }*/
        return Base64.encodeBytes(bytes);//encodeToString(bytes, Base64.URL_SAFE);

    }

    @Override
    public String getType()
    {
        return "android.util.Base64";
    }
}
