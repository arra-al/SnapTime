package al.arra.api.flickr.extractor;

import al.arra.api.flickr.exception.OAuthParametersMissingException;
import al.arra.api.flickr.model.OAuthRequest;
import al.arra.api.flickr.model.ParameterList;
import al.arra.api.flickr.util.OAuthEncoder;
import al.arra.api.flickr.util.Validation;

/**
 * Created by Gezim on 11/7/2015.
 */
public class BaseStringExtractor implements Extractor<String, OAuthRequest> {

    private static final String AMPERSAND_SEPARATED_STRING = "%s&%s&%s";

    @Override
    public String extract(OAuthRequest request)
    {
        checkPreconditions(request);
        String verb = OAuthEncoder.encode(request.getHttpMethod().name());
        String url = OAuthEncoder.encode(request.getSanitizedUrl());
        String params = getSortedAndEncodedParams(request);
        return String.format(AMPERSAND_SEPARATED_STRING, verb, url, params);
    }

    private String getSortedAndEncodedParams(OAuthRequest request)
    {
        ParameterList params = new ParameterList();
        params.addAll(request.getQueryStringParams());
        params.addAll(request.getBodyParams());
        params.addAll(new ParameterList(request.getOauthParameters()));
        return params.sort().asOauthBaseString();
    }

    private void checkPreconditions(OAuthRequest request)
    {
        Validation.checkNotNull(request, "Cannot extract base string from a null object");

        if (request.getOauthParameters() == null || request.getOauthParameters().size() <= 0)
        {
            throw new OAuthParametersMissingException(request);
        }
    }
}
