package al.arra.api.flickr.exception;

/**
 * Created by Gezim on 11/6/2015.
 */
public class OAuthConnectionException  extends OAuthException {
    private static final String MSG = "There was a problem while creating a connection to the remote service.";

    public OAuthConnectionException(Exception e)
    {
        super(MSG, e);
    }
}
