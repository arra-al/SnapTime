package al.arra.snaptime.util;

import android.app.Activity;
import android.content.Context;

import al.arra.snaptime.constant.AppConstants;

/**
 * Created by Gezim on 11/21/2015.
 */
public class RestCall {
    public static String apiKey = "";
    public static String apiSecret = "";
    public static String fulltoken = "";

    public static void setAuth(Activity activity) {
        apiKey = AppConstants.API_KEY;
        apiSecret = AppConstants.API_SECRET;
    }

}
