package al.arra.api.flickr.extractor;

import java.util.Map;

import al.arra.api.flickr.exception.OAuthParametersMissingException;
import al.arra.api.flickr.model.OAuthConstant;
import al.arra.api.flickr.model.OAuthRequest;
import al.arra.api.flickr.util.OAuthEncoder;
import al.arra.api.flickr.util.Validation;

/**
 * Created by Gezim on 11/7/2015.
 */
public class HeaderExtractor implements Extractor<String, OAuthRequest> {
    private static final String PARAM_SEPARATOR = ", ";
    private static final String PREAMBLE = "OAuth ";
    public static final int ESTIMATED_PARAM_LENGTH = 20;

    @Override
    public String extract(OAuthRequest request)
    {
        checkPreconditions(request);
        Map<String, String> parameters = request.getOauthParameters();
        StringBuilder header = new StringBuilder(parameters.size() * ESTIMATED_PARAM_LENGTH);
        header.append(PREAMBLE);
        for (Map.Entry<String, String> entry : parameters.entrySet())
        {
            if(header.length() > PREAMBLE.length())
            {
                header.append(PARAM_SEPARATOR);
            }
            header.append(String.format("%s=\"%s\"", entry.getKey(), OAuthEncoder.encode(entry.getValue())));
        }

        if (request.getRealm() != null && !request.getRealm().isEmpty())
        {
            header.append(PARAM_SEPARATOR);
            header.append(String.format("%s=\"%s\"", OAuthConstant.REALM, request.getRealm()));
        }

        return header.toString();
    }

    private void checkPreconditions(OAuthRequest request)
    {
        Validation.checkNotNull(request, "Cannot extract a header from a null object");

        if (request.getOauthParameters() == null || request.getOauthParameters().size() <= 0)
        {
            throw new OAuthParametersMissingException(request);
        }
    }
}
