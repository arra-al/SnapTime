package al.arra.api.flickr.crypt;

import javax.xml.bind.DatatypeConverter;

/**
 * Created by Gezim on 11/7/2015.
 */
public class DatatypeConverterEncoder extends Base64Encoder
{
    @Override
    public String encode(byte[] bytes)
    {
        return DatatypeConverter.printBase64Binary(bytes);
    }

    @Override
    public String getType()
    {
        return "DatatypeConverter";
    }
}
