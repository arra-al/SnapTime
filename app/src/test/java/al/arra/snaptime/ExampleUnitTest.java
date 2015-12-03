package al.arra.snaptime;

import org.junit.Test;

import al.arra.api.flickr.ServiceBuilder;
import al.arra.api.flickr.api.FlickrAPI;
import al.arra.api.flickr.model.HTTPMethod;
import al.arra.api.flickr.model.HTTPResponse;
import al.arra.api.flickr.model.OAuthRequest;
import al.arra.api.flickr.model.Token;
import al.arra.api.flickr.model.Verifier;
import al.arra.api.flickr.oauth.OAuthService;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {

    private static final String PROTECTED_RESOURCE_URL = "http://api.flickr.com/services/rest/";
    private static final String authorizationId = "578-369-862";//"419-632-623";

    @Test
    public void flickrAPI() {
        String apiKey = "8c54e8144fd85f6fb3c9b0a62a2f0b50";//al.arra.snaptime.constant.AppConstants.API_KEY;
        String apiSecret = "48719b0913f26618";//al.arra.snaptime.constant.AppConstants.API_SECRET;

        OAuthService service = new ServiceBuilder().provider(FlickrAPI.class).apiKey(apiKey).apiSecret(apiSecret).build();
service.
        System.out.println("=== Flickr's OAuth Workflow ===");
        System.out.println();

        // Obtain the Request Token
        System.out.println("Fetching the Request Token...");
        Token requestToken = service.getRequestToken();
        if(authorizationId == null) {

            System.out.println("Got the Request Token !");
            System.out.println();


            System.out.println("Now go and authorize Snsp Time here:");
            String authorizationUrl = service.getAuthorizationUrl(requestToken);
            System.out.println(authorizationUrl + "&perms=write");
            System.out.println("And paste the verifier here");
            System.out.print(">>");
        }
        Verifier verifier = new Verifier(authorizationId);
        System.out.println();

        // Trade the Request Token and Verfier for the Access Token
        System.out.println("Trading the Request Token for an Access Token...");
        Token accessToken = service.getAccessToken(requestToken, verifier);
        System.out.println("Got the Access Token!");
        System.out.println("(if your curious it looks like this: " + accessToken + " )");
        System.out.println("(you can get the username, full name, and nsid by parsing the rawResponse: " + accessToken.getRawResponse() + ")");
        System.out.println();

        // Now let's go and ask for a protected resource!
        System.out.println("Now we're going to access a protected resource...");
        OAuthRequest request = new OAuthRequest(HTTPMethod.POST, PROTECTED_RESOURCE_URL);
        request.addQuerystringParameter("method", "flickr.test.login");
        service.signRequest(accessToken, request);
        HTTPResponse response = request.send();
        System.out.println("Got it! Lets see what we found...");
        System.out.println();
        System.out.println(response.getBody());

        System.out.println();
    }
}