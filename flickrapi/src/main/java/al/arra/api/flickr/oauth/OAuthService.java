package al.arra.api.flickr.oauth;

import al.arra.api.flickr.model.OAuthRequest;
import al.arra.api.flickr.model.Token;
import al.arra.api.flickr.model.Verifier;

/**
 * Created by Gezim on 11/8/2015.
 */
public interface OAuthService {
    /**
     * Retrieve the request token.
     *
     * @return request token
     */
    public Token getRequestToken();

    /**
     * Retrieve the access token
     *
     * @param requestToken request token (obtained previously)
     * @param verifier verifier code
     * @return access token
     */
    public Token getAccessToken(Token requestToken, Verifier verifier);

    /**
     * Signs am OAuth request
     *
     * @param accessToken access token (obtained previously)
     * @param request request to sign
     */
    public void signRequest(Token accessToken, OAuthRequest request);

    /**
     * Returns the OAuth version of the service.
     *
     * @return oauth version as string
     */
    public String getVersion();

    /**
     * Returns the URL where you should redirect your users to authenticate
     * your application.
     *
     * @param requestToken the request token you need to authorize
     * @return the URL where you should redirect your users
     */
    public String getAuthorizationUrl(Token requestToken);
}
