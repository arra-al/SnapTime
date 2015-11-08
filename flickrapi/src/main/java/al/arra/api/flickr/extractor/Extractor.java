package al.arra.api.flickr.extractor;

import al.arra.api.flickr.model.Token;

/**
 * Created by Gezim on 11/7/2015.
 */
public interface Extractor<R, P> {
    /**
     * Extracts the access token from the contents of an Http Response
     *
     * @param response the contents of the response
     * @return OAuth access token
     */
    public R extract(P ext);
}
