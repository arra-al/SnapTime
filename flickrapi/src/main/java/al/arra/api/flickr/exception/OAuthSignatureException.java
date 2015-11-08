package al.arra.api.flickr.exception;

/**
 * Created by Gezim on 11/7/2015.
 */
public class OAuthSignatureException extends OAuthException
{
    private static final long serialVersionUID = 1L;
    private static final String MSG = "Error while signing string: %s";

    /**
     * Default constructor
     *
     * @param stringToSign plain string that gets signed (HMAC-SHA, etc)
     * @param e original exception
     */
    public OAuthSignatureException(String stringToSign, Exception e)
    {
        super(String.format(MSG, stringToSign), e);
    }

}
