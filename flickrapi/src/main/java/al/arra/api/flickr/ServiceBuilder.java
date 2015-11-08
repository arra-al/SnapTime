package al.arra.api.flickr;

import java.io.OutputStream;

import al.arra.api.flickr.api.FlickrAPI;
import al.arra.api.flickr.exception.OAuthException;
import al.arra.api.flickr.model.OAuthConfig;
import al.arra.api.flickr.model.OAuthConstant;
import al.arra.api.flickr.model.SignatureType;
import al.arra.api.flickr.oauth.OAuthService;
import al.arra.api.flickr.util.Validation;

/**
 * Created by Gezim on 11/8/2015.
 */
public class ServiceBuilder {
    private String apiKey;
    private String apiSecret;
    private String callback;
    private FlickrAPI api;
    private String scope;
    private SignatureType signatureType;
    private OutputStream debugStream;

    /**
     * Default constructor
     */
    public ServiceBuilder()
    {
        this.callback = OAuthConstant.OUT_OF_BAND;
        this.signatureType = SignatureType.Header;
        this.debugStream = null;
    }

    /**
     * Configures the {@link FlickrAPI}
     *
     * @param apiClass the class of one of the existent {@link FlickrAPI}s on org.scribe.api package
     * @return the {@link ServiceBuilder} instance for method chaining
     */
    public ServiceBuilder provider(Class<FlickrAPI> apiClass)
    {
        this.api = createApi(apiClass);
        return this;
    }

    private FlickrAPI createApi(Class<FlickrAPI> apiClass)
    {
        Validation.checkNotNull(apiClass, "Api class cannot be null");
        FlickrAPI api;
        try
        {
            api = apiClass.newInstance();
        }
        catch(Exception e)
        {
            throw new OAuthException("Error while creating the Api object", e);
        }
        return api;
    }

    /**
     * Configures the {@link FlickrAPI}
     *
     * Overloaded version. Let's you use an instance instead of a class.
     *
     * @param api instance of {@link FlickrAPI}s
     * @return the {@link ServiceBuilder} instance for method chaining
     */
    public ServiceBuilder provider(FlickrAPI api)
    {
        Validation.checkNotNull(api, "Api cannot be null");
        this.api = api;
        return this;
    }

    /**
     * Adds an OAuth callback url
     *
     * @param callback callback url. Must be a valid url or 'oob' for out of band OAuth
     * @return the {@link ServiceBuilder} instance for method chaining
     */
    public ServiceBuilder callback(String callback)
    {
        Validation.checkNotNull(callback, "Callback can't be null");
        this.callback = callback;
        return this;
    }

    /**
     * Configures the api key
     *
     * @param apiKey The api key for your application
     * @return the {@link ServiceBuilder} instance for method chaining
     */
    public ServiceBuilder apiKey(String apiKey)
    {
        Validation.checkEmptyString(apiKey, "Invalid Api key");
        this.apiKey = apiKey;
        return this;
    }

    /**
     * Configures the api secret
     *
     * @param apiSecret The api secret for your application
     * @return the {@link ServiceBuilder} instance for method chaining
     */
    public ServiceBuilder apiSecret(String apiSecret)
    {
        Validation.checkEmptyString(apiSecret, "Invalid Api secret");
        this.apiSecret = apiSecret;
        return this;
    }

    /**
     * Configures the OAuth scope. This is only necessary in some APIs (like Google's).
     *
     * @param scope The OAuth scope
     * @return the {@link ServiceBuilder} instance for method chaining
     */
    public ServiceBuilder scope(String scope)
    {
        Validation.checkEmptyString(scope, "Invalid OAuth scope");
        this.scope = scope;
        return this;
    }

    /**
     * Configures the signature type, choose between header, querystring, etc. Defaults to Header
     *
     * @param scope The OAuth scope
     * @return the {@link ServiceBuilder} instance for method chaining
     */
    public ServiceBuilder signatureType(SignatureType type)
    {
        Validation.checkNotNull(type, "Signature type can't be null");
        this.signatureType = type;
        return this;
    }

    public ServiceBuilder debugStream(OutputStream stream)
    {
        Validation.checkNotNull(stream, "debug stream can't be null");
        this.debugStream = stream;
        return this;
    }

    public ServiceBuilder debug()
    {
        this.debugStream(System.out);
        return this;
    }

    /**
     * Returns the fully configured {@link OAuthService}
     *
     * @return fully configured {@link OAuthService}
     */
    public OAuthService build()
    {
        Validation.checkNotNull(api, "You must specify a valid api through the provider() method");
        Validation.checkEmptyString(apiKey, "You must provide an api key");
        Validation.checkEmptyString(apiSecret, "You must provide an api secret");
        return api.createService(new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope, debugStream));
    }
}
