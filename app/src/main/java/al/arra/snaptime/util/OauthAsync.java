package al.arra.snaptime.util;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONException;
import org.json.JSONObject;

import al.arra.api.flickr.ServiceBuilder;
import al.arra.api.flickr.api.FlickrAPI;
import al.arra.api.flickr.model.HTTPMethod;
import al.arra.api.flickr.model.HTTPResponse;
import al.arra.api.flickr.model.OAuthRequest;
import al.arra.api.flickr.model.Token;
import al.arra.api.flickr.model.Verifier;
import al.arra.api.flickr.oauth.OAuthService;
import al.arra.snaptime.constant.AppConstants;

/**
 * Created by Gezim on 11/30/2015.
 */
public class OauthAsync extends AsyncTask<Void, Integer, Token> {
    private static final String TAG = "OauthAsync";
    private static final String flickrPerm = "&perms=write";
    private Context context;
    private OAuthService service;

    private WebView webView;

    private AppSharedPreferences appPref;

    public OauthAsync(Context context, WebView webView) {
        this.context = context;
        this.webView = webView;

        this.appPref = new AppSharedPreferences(context);
    }


    @Override
    protected Token doInBackground(Void... params) {
        service = new ServiceBuilder().flickrService(AppConstants.API_KEY, AppConstants.API_SECRET);
        final Token requestToken = service.getRequestToken();
        Log.i(TAG, "Got the request token: " + requestToken.toString());
        //save request token
        //context.getSharedPreferences(AppConstants.SHARED_PREF_NAME);
        appPref.putRequestToken(requestToken);

        return requestToken;
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Token httpResponse) {
        Log.i(TAG, "on post exec : " + httpResponse);

        webView.setVisibility(View.VISIBLE);
        String authorizationUrl = service.getAuthorizationUrl(httpResponse) + flickrPerm;
        shouldOverrideURLLoading(authorizationUrl, httpResponse);

        super.onPostExecute(httpResponse);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(Token httpResponse) {
        super.onCancelled(httpResponse);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    private void shouldOverrideURLLoading(String url, final Token requestToken) {
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                //check for our custom callback protocol otherwise use default behavior
                if(url.startsWith("oauth")){
                    //authorization complete hide webview for now.
                    webView.setVisibility(View.GONE);

                    Uri uri = Uri.parse(url);
                    String verifier = uri.getQueryParameter("oauth_verifier");
                    Verifier v = new Verifier(verifier);

                    //save this token for practical use.
                    Token accessToken = service.getAccessToken(requestToken, v);


                    if(uri.getHost().equals("flickr")){
                        OAuthRequest req = new OAuthRequest(HTTPMethod.GET, FlickrAPI.PROTECTED_RESOURCE_URL);
                        req.addQuerystringParameter("method", "flickr.test.login");
                        req.addQuerystringParameter("format", "json");
                        service.signRequest(accessToken, req);
                        HTTPResponse response = req.send();
                        try {
                            JSONObject json = new JSONObject(response.getBody());
                            Log.i(TAG, json.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    return true;
                }


                return super.shouldOverrideUrlLoading(view, url);
            }
        });


        //send user to authorization page
        webView.loadUrl(url);
    }
}
