package al.arra.api.flickr.oauth;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import al.arra.api.flickr.api.FlickrAPI;
import al.arra.api.flickr.crypt.Base64Encoder;
import al.arra.api.flickr.model.HTTPRequest;
import al.arra.api.flickr.model.HTTPResponse;
import al.arra.api.flickr.model.OAuthConfig;
import al.arra.api.flickr.model.OAuthConstant;
import al.arra.api.flickr.model.OAuthRequest;
import al.arra.api.flickr.model.RequestTracker;
import al.arra.api.flickr.model.Token;
import al.arra.api.flickr.model.Verifier;
import al.arra.api.flickr.util.MapUtil;

/**
 * Created by Gezim on 11/8/2015.
 */
public class FlickrService implements OAuthService{
    private static final String VERSION = "1.0";

    private OAuthConfig config;
    private FlickrAPI api;

    /**
     * Default constructor
     *
     * @param api OAuth1.0a api information
     * @param config OAuth 1.0a configuration param object
     */
    public FlickrService(FlickrAPI api, OAuthConfig config)
    {
        this.api = api;
        this.config = config;
    }

    public Token getRequestToken(int timeout, TimeUnit unit)
    {
        return getRequestToken(new TimeoutTuner(timeout, unit));
    }

    public Token getRequestToken()
    {
        return getRequestToken(2, TimeUnit.SECONDS);
    }

    public Token getRequestToken(RequestTracker tuner)
    {
        config.log("obtaining request token from " + api.getRequestTokenEndpoint());
        OAuthRequest request = new OAuthRequest(api.getRequestTokenHTTPMethod(), api.getRequestTokenEndpoint());

        config.log("setting oauth_callback to " + config.getCallback());
        request.addOAuthParameter(OAuthConstant.CALLBACK, config.getCallback());
        addOAuthParams(request, OAuthConstant.EMPTY_TOKEN);
        appendSignature(request);

        config.log("sending request...");
        HTTPResponse response = request.send(tuner);
        String body = response.getBody();

        config.log("response status code: " + response.getCode());
        config.log("response body: " + body);
        return api.getRequestTokenExtractor().extract(body);
    }

    private void addOAuthParams(OAuthRequest request, Token token)
    {
        request.addOAuthParameter(OAuthConstant.TIMESTAMP, api.getTimestampService().getTimestampInSeconds());
        request.addOAuthParameter(OAuthConstant.NONCE, api.getTimestampService().getNonce());
        request.addOAuthParameter(OAuthConstant.CONSUMER_KEY, config.getApiKey());
        request.addOAuthParameter(OAuthConstant.SIGN_METHOD, api.getSignatureService().getSignatureMethod());
        request.addOAuthParameter(OAuthConstant.VERSION, getVersion());
        if(config.hasScope()) request.addOAuthParameter(OAuthConstant.SCOPE, config.getScope());
        request.addOAuthParameter(OAuthConstant.SIGNATURE, getSignature(request, token));

        config.log("appended additional OAuth parameters: " + MapUtil.toString(request.getOauthParameters()));
    }

    public Token getAccessToken(Token requestToken, Verifier verifier, int timeout, TimeUnit unit)
    {
        return getAccessToken(requestToken, verifier, new TimeoutTuner(timeout, unit));
    }

    public Token getAccessToken(Token requestToken, Verifier verifier)
    {
        return getAccessToken(requestToken, verifier, 2, TimeUnit.SECONDS);
    }

    public Token getAccessToken(Token requestToken, Verifier verifier, RequestTracker tuner)
    {
        config.log("obtaining access token from " + api.getAccessTokenEndpoint());
        OAuthRequest request = new OAuthRequest(api.getAccessTokenHTTPMethod(), api.getAccessTokenEndpoint());
        request.addOAuthParameter(OAuthConstant.TOKEN, requestToken.getToken());
        request.addOAuthParameter(OAuthConstant.VERIFIER, verifier.getValue());

        config.log("setting token to: " + requestToken + " and verifier to: " + verifier);
        addOAuthParams(request, requestToken);
        appendSignature(request);

        config.log("sending request...");
        HTTPResponse response = request.send(tuner);
        String body = response.getBody();

        config.log("response status code: " + response.getCode());
        config.log("response body: " + body);
        return api.getAccessTokenExtractor().extract(body);
    }

    public void signRequest(Token token, OAuthRequest request)
    {
        config.log("signing request: " + request.getCompleteUrl());

        // Do not append the token if empty. This is for two legged OAuth calls.
        if (!token.isEmpty())
        {
            request.addOAuthParameter(OAuthConstant.TOKEN, token.getToken());
        }
        config.log("setting token to: " + token);
        addOAuthParams(request, token);
        appendSignature(request);
    }

    public String getVersion()
    {
        return VERSION;
    }

    public String getAuthorizationUrl(Token requestToken)
    {
        return api.getAuthorizationUrl(requestToken);
    }

    private String getSignature(OAuthRequest request, Token token)
    {
        config.log("generating signature...");
        config.log("using base64 encoder: " + Base64Encoder.type());
        String baseString = api.getBaseStringExtractor().extract(request);
        String signature = api.getSignatureService().getSignature(baseString, config.getApiSecret(), token.getSecret());

        config.log("base string is: " + baseString);
        config.log("signature is: " + signature);
        return signature;
    }

    private void appendSignature(OAuthRequest request)
    {
        switch (config.getSignatureType())
        {
            case Header:
                config.log("using Http Header signature");

                String oauthHeader = api.getHeaderExtractor().extract(request);
                request.addHeader(OAuthConstant.HEADER, oauthHeader);
                break;
            case QueryString:
                config.log("using Querystring signature");

                for (Map.Entry<String, String> entry : request.getOauthParameters().entrySet())
                {
                    request.addQuerystringParameter(entry.getKey(), entry.getValue());
                }
                break;
        }
    }

    private static class TimeoutTuner extends RequestTracker
    {
        private final int duration;
        private final TimeUnit unit;

        public TimeoutTuner(int duration, TimeUnit unit)
        {
            this.duration = duration;
            this.unit = unit;
        }

        @Override
        public void track(HTTPRequest request)
        {
            request.setReadTimeout(duration, unit);
        }
    }
}
