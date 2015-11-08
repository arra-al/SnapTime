package al.arra.api.flickr.api;

import al.arra.api.flickr.crypt.HMACSha1Signature;
import al.arra.api.flickr.crypt.TimestampService;
import al.arra.api.flickr.extractor.BaseStringExtractor;
import al.arra.api.flickr.extractor.HeaderExtractor;
import al.arra.api.flickr.extractor.TokenExtractor;
import al.arra.api.flickr.model.HTTPMethod;
import al.arra.api.flickr.model.OAuthConfig;
import al.arra.api.flickr.model.Token;
import al.arra.api.flickr.oauth.FlickrService;
import al.arra.api.flickr.oauth.OAuthService;

public class FlickrAPI {

    public static final String ACCESS_TOKEN_ENDPOINT = "https://www.flickr.com/services/oauth/access_token";
    public static final String REQUEST_TOKEN_ENDPOINT = "https://www.flickr.com/services/oauth/request_token";
    public static final String AUTHORIZATION_URL = "https://www.flickr.com/services/oauth/authorize?oauth_token";

    public String getAccessTokenEndpoint()
    {
        return ACCESS_TOKEN_ENDPOINT;
    }

    public String getAuthorizationUrl(Token requestToken)
    {
        StringBuilder sb = new StringBuilder(AUTHORIZATION_URL);
        sb.append("=");
        sb.append(requestToken.getToken());
        return sb.toString();
    }

    public String getRequestTokenEndpoint()
    {
        return REQUEST_TOKEN_ENDPOINT;
    }

    /**
     * Returns the access token extractor.
     *
     * @return access token extractor
     */
    public TokenExtractor getAccessTokenExtractor()
    {
        return new TokenExtractor();
    }

    /**
     * Returns the base string extractor.
     *
     * @return base string extractor
     */
    public BaseStringExtractor getBaseStringExtractor()
    {
        return new BaseStringExtractor();
    }

    /**
     * Returns the header extractor.
     *
     * @return header extractor
     */
    public HeaderExtractor getHeaderExtractor()
    {
        return new HeaderExtractor();
    }

    /**
     * Returns the request token extractor.
     *
     * @return request token extractor
     */
    public TokenExtractor getRequestTokenExtractor()
    {
        return new TokenExtractor();
    }

    /**
     * Returns the signature service.
     *
     * @return signature service
     */
    public HMACSha1Signature getSignatureService()
    {
        return new HMACSha1Signature();
    }

    /**
     * Returns the timestamp service.
     *
     * @return timestamp service
     */
    public TimestampService getTimestampService()
    {
        return new TimestampService();
    }

    /**
     * Returns the HTTPMethod for the access token endpoint (defaults to POST)
     *
     * @return access token endpoint HTTPMethod
     */
    public HTTPMethod getAccessTokenHTTPMethod()
    {
        return HTTPMethod.POST;
    }

    /**
     * Returns the HTTPMethod for the request token endpoint (defaults to POST)
     *
     * @return request token endpoint HTTPMethod
     */
    public HTTPMethod getRequestTokenHTTPMethod()
    {
        return HTTPMethod.POST;
    }

    public FlickrService createService(OAuthConfig config)
    {
        return new FlickrService(this, config);
    }
}
