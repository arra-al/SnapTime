package al.arra.api.flickr.extractor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import al.arra.api.flickr.exception.OAuthException;
import al.arra.api.flickr.model.Token;
import al.arra.api.flickr.util.Validation;

/**
 * Created by Gezim on 11/7/2015.
 */
public class JsonTokenExtractor implements Extractor<Token, String> {
    private Pattern accessTokenPattern = Pattern.compile("\"access_token\":\\s*\"(\\S*?)\"");

    @Override
    public Token extract(String response)
    {
        Validation.checkEmptyString(response, "Cannot extract a token from a null or empty String");
        Matcher matcher = accessTokenPattern.matcher(response);
        if(matcher.find())
        {
            return new Token(matcher.group(1), "", response);
        }
        else
        {
            throw new OAuthException("Cannot extract an access token. Response was: " + response);
        }
    }
}
