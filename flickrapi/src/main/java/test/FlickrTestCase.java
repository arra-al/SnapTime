package test;

import java.util.Scanner;

import al.arra.api.flickr.ServiceBuilder;
import al.arra.api.flickr.api.FlickrAPI;
import al.arra.api.flickr.model.HTTPMethod;
import al.arra.api.flickr.model.HTTPResponse;
import al.arra.api.flickr.model.OAuthRequest;
import al.arra.api.flickr.model.Token;
import al.arra.api.flickr.model.Verifier;
import al.arra.api.flickr.oauth.OAuthService;

/**
 * Created by Gezim on 11/8/2015.
 */
public class FlickrTestCase {
    public static final String API_KEY = "";
    public static final String API_SECRET = "";
    private static final String PROTECTED_RESOURCE_URL = "https://api.flickr.com/services/rest/";

    public static void main(String[] args) {
        // Replace these with your own api key and secret
        OAuthService service = new ServiceBuilder().provider(FlickrAPI.class).apiKey(API_KEY).apiSecret(API_SECRET).build();


        System.out.println("=== Flickr's OAuth Workflow ===");
        System.out.println();

        // Obtain the Request Token
        System.out.println("Fetching the Request Token...");
        Token requestToken = service.getRequestToken();
        System.out.println("Got the Request Token !");
        System.out.println();


        System.out.println("Now go and authorize Snsp Time here:");
        String authorizationUrl = service.getAuthorizationUrl(requestToken);
        System.out.println(authorizationUrl + "&perms=write");
        System.out.println("And paste the verifier here");
        System.out.print(">>");
        Scanner in = new Scanner(System.in);
        Verifier verifier = new Verifier(in.nextLine());
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
        OAuthRequest request = new OAuthRequest(HTTPMethod.GET, PROTECTED_RESOURCE_URL);
        request.addQuerystringParameter("method", "flickr.test.login");
        service.signRequest(accessToken, request);
        HTTPResponse response = request.send();
        System.out.println("Got it! Lets see what we found...");
        System.out.println();
        System.out.println(response.getBody());

        System.out.println();
    }

    private void searchPhotoByTag(OAuthService service, String tags) {
        OAuthRequest request = new OAuthRequest(HTTPMethod.GET, PROTECTED_RESOURCE_URL);
        request.addQuerystringParameter("method", "flickr.photos.search");
        request.addQuerystringParameter("tags", tags);



    }
}
