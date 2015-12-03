package al.arra.api.flickr.crypt;

/**
 * Created by Gezim on 11/7/2015.
 */
public abstract class Base64Encoder {
    private static Base64Encoder instance;

    public static synchronized Base64Encoder getInstance()
    {
        if (instance == null)
        {
            instance = createEncoderInstance();
        }
        return instance;
    }

    private static Base64Encoder createEncoderInstance()
    {
        return new CommonsEncoder();
    }

    public static String type()
    {
        return getInstance().getType();
    }

    public abstract String encode(byte[] bytes);

    public abstract String getType();
}
