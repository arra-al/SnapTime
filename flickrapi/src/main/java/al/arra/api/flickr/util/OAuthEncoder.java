package al.arra.api.flickr.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import al.arra.api.flickr.exception.OAuthException;

/**
 * Created by Gezim on 11/6/2015.
 */
public class OAuthEncoder {
    private static String CHARSET = "UTF-8";
    private static final Map<String, String> ENCODING_RULES;

    static
    {
        Map<String, String> rules = new HashMap<>();
        rules.put("*", "%2A");
        rules.put("+", "%20");
        rules.put("%7E", "~");
        ENCODING_RULES = Collections.unmodifiableMap(rules);
    }

    private OAuthEncoder(){}

    /**
     * url encoder
     * @param plain
     * @return
     */
    public static String encode(String plain)
    {
        Validation.checkNotNull(plain, "Cannot encode null object");
        String encoded = "";
        try
        {
            encoded = URLEncoder.encode(plain, CHARSET);
        }
        catch (UnsupportedEncodingException uee)
        {
            throw new OAuthException("Charset not found while encoding string: " + CHARSET, uee);
        }
        for(Map.Entry<String, String> rule : ENCODING_RULES.entrySet())
        {
            encoded = applyRule(encoded, rule.getKey(), rule.getValue());
        }
        return encoded;
    }

    private static String applyRule(String encoded, String toReplace, String replacement)
    {
        return encoded.replaceAll(Pattern.quote(toReplace), replacement);
    }

    /**
     * url decoder
     * @param encoded
     * @return
     */
    public static String decode(String encoded)
    {
        Validation.checkNotNull(encoded, "Cannot decode null object");
        try
        {
            return URLDecoder.decode(encoded, CHARSET);
        }
        catch(UnsupportedEncodingException uee)
        {
            throw new OAuthException("Charset not found while decoding string: " + CHARSET, uee);
        }
    }
}
