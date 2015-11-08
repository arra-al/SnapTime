package al.arra.api.flickr.exception;

import al.arra.api.flickr.model.OAuthRequest;

/**
 * Created by Gezim on 11/7/2015.
 */
public class OAuthParametersMissingException extends OAuthException {

    private static final String MSG = "Could not find oauth parameters in request: %s. "
            + "OAuth parameters must be specified with the addOAuthParameter() method";

    /**
     * Default constructor.
     *
     * @param request OAuthRequest that caused the error
     */
    public OAuthParametersMissingException(OAuthRequest request)
    {
        super(String.format(MSG, request));
    }
}
