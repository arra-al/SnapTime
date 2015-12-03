package al.arra.snaptime;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import al.arra.api.flickr.ServiceBuilder;
import al.arra.api.flickr.api.FlickrAPI;
import al.arra.api.flickr.model.HTTPMethod;
import al.arra.api.flickr.model.HTTPResponse;
import al.arra.api.flickr.model.OAuthRequest;
import al.arra.api.flickr.model.Token;
import al.arra.api.flickr.model.Verifier;
import al.arra.api.flickr.oauth.OAuthService;
import al.arra.snaptime.constant.AppConstants;
import al.arra.snaptime.util.OauthAsync;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private OauthAsync oauthTask;
    private DrawerLayout drawer;

    private boolean isLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(isLoggedIn) {
            setActionBarIcon(R.drawable.ic_ab_drawer);

            GridView gridView = (GridView) findViewById(R.id.gridView);
            gridView.setNumColumns(2);
            gridView.setAdapter(new GridViewAdapter());
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String url = (String) view.getTag();
                    Log.i("MainActivity", "url : " + url);
                }
            });

            drawer = (DrawerLayout) findViewById(R.id.drawer);
            drawer.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        } else {
            oauthTask = new OauthAsync(getApplicationContext(), (WebView)findViewById(R.id.webView));
            oauthTask.execute();
            //attach WebViewClient to intercept the callback url

        }
        //dispatchTakePictureIntent();
    }

    @Override
    protected int getLayoutResource() {
        int layout = R.layout.activity_main;
        if(!isLoggedIn) {
            layout = R.layout.authenticate;
        }
        return layout;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Log.i(getClass().getCanonicalName(), "onActivityResult");

            //mImageView.setImageBitmap(imageBitmap);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static class GridViewAdapter extends BaseAdapter {

        @Override public int getCount() {
            return 10;
        }

        @Override public Object getItem(int i) {
            return "Item " + String.valueOf(i + 1);
        }

        @Override public long getItemId(int i) {
            return i;
        }

        @Override public View getView(int i, View view, ViewGroup viewGroup) {

            if (view == null) {
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.grid_item, viewGroup, false);
            }

            String urlStr = "http://lorempixel.com/800/600/sports/" + getItem(i).toString();
            URL url = null;
            try {
                url = new URL(urlStr);
                URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
                url = uri.toURL();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }


            String imageUrl = url.toString();

            view.setTag(imageUrl);

            ImageView image = (ImageView) view.findViewById(R.id.image);
            Picasso picasso = new Picasso.Builder(view.getContext())
                    .listener(new Picasso.Listener() {
                        @Override
                        public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                            Log.d("MainActivity picasso", "image load failed", exception);
                        }
                    })
                    .build();
            picasso.setIndicatorsEnabled(true);
            picasso.setLoggingEnabled(true);
            picasso.load(imageUrl)
                    .placeholder(R.drawable.photo)
                    .error(R.drawable.switch_off)
                    .resize(50, 50)
                    .centerCrop()
                    .into(image);

            return view;
        }
    }


}
