package al.arra.api.flickr.model;

import al.arra.api.flickr.util.Validation;

/**
 * Created by Gezim on 11/8/2015.
 */
public class Verifier {
    private final String value;

    /**
     * Default constructor.
     *
     * @param value verifier value
     */
    public Verifier(String value)
    {
        Validation.checkNotNull(value, "Must provide a valid string as verifier");
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }
}
