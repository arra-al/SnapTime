package al.arra.api.flickr.extractor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import al.arra.api.flickr.exception.OAuthException;
import al.arra.api.flickr.model.Token;
import al.arra.api.flickr.util.OAuthEncoder;
import al.arra.api.flickr.util.Validation;

/**
 * Created by Gezim on 11/7/2015.
 */
public class TokenExtractor implements Extractor<Token, String> {
    private static final Pattern TOKEN_REGEX = Pattern.compile("oauth_token=([^&]+)");
    private static final Pattern SECRET_REGEX = Pattern.compile("oauth_token_secret=([^&]*)");

    @Override
    public Token extract(String response)
    {
        Validation.checkEmptyString(response, "Response body is incorrect. Can't extract a token from an empty string");
        String token = extract(response, TOKEN_REGEX);
        String secret = extract(response, SECRET_REGEX);
        return new Token(token, secret, response);
    }

    private String extract(String response, Pattern p)
    {
        Matcher matcher = p.matcher(response);
        if (matcher.find() && matcher.groupCount() >= 1)
        {
            return OAuthEncoder.decode(matcher.group(1));
        }
        else
        {
            throw new OAuthException("Response body is incorrect. Can't extract token and secret from this: '" + response + "'", null);
        }
    }
}
