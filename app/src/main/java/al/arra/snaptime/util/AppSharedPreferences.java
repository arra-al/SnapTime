package al.arra.snaptime.util;

import android.content.Context;
import android.content.SharedPreferences;

import al.arra.api.flickr.model.Token;

/**
 * Created by Gezim on 12/3/2015.
 */
public class AppSharedPreferences {
    public static final String SHARED_PREF_NAME = "ARRA_SNAPTIME";

    private static final String requestTokenKey = "flickr_request_token";
    private static final String requestTokenSecretKey = "flickr_request_token_secret";
    private Context context;
    private static SharedPreferences SHARED_PREF;
    private static SharedPreferences.Editor EDITOR_SHARED_PREF;

    public AppSharedPreferences(Context context) {
        this.context = context;
        SHARED_PREF = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        EDITOR_SHARED_PREF = SHARED_PREF.edit();
    }

    public void putRequestToken(Token requestToken) {
        EDITOR_SHARED_PREF.putString(requestTokenKey, requestToken.getToken());
        EDITOR_SHARED_PREF.putString(requestTokenSecretKey, requestToken.getSecret());

        EDITOR_SHARED_PREF.commit();
    }

    public Token getRequestToken() {
        String tokenStr = SHARED_PREF.getString(requestTokenKey, null);
        String secret = SHARED_PREF.getString(requestTokenSecretKey, null);
        if(tokenStr != null && secret != null) {
            return  new Token(tokenStr, secret);
        }

        return null;
    }

    public void putString(String key, String value) {
        EDITOR_SHARED_PREF.putString(key, value);
    }

    public <T> T get(String key, Class<T> type) {
        Object value = null;
        if(type.isAssignableFrom(String.class)) {
            value = SHARED_PREF.getString(key, null);
        } else if(type.isAssignableFrom(Boolean.class)) {
            value = SHARED_PREF.getBoolean(key, false);
        } else if(type.isAssignableFrom(Integer.class)) {
            value = SHARED_PREF.getInt(key, Integer.MIN_VALUE);
        } else if(type.isAssignableFrom(Float.class)) {
            value = SHARED_PREF.getFloat(key, Float.MIN_VALUE);
        } else if(type.isAssignableFrom(Long.class)) {
            value = SHARED_PREF.getLong(key, Long.MIN_VALUE);
        }

        return (T)value;
    }
}
