package bluepanda.fireandroid;

import android.app.Activity;
import android.os.Build;
import android.provider.Settings;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;


public class MainActivity extends Activity {
    private static final int REQUEST_PERMISSION = 0;
    private WebView webView;
    public String myandroidID = "";
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Activity thisActivity = this;
        CookieManager.getInstance().setAcceptCookie(true);
        webView = (WebView)findViewById(R.id.webView1);
        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.addJavascriptInterface(new WebAppInterface(this), "Android");


        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            webView.getSettings().setAllowUniversalAccessFromFileURLs(true);

        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        myandroidID = androidId;
        System.out.println("AndroidID : " + androidId + "\n");

        String URL = "http://alan.dev.ladb.tv/fire_tv/home?id="+androidId;
        System.out.println("URL : " + URL + "\n");
        CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(webView.getContext());
        cookieSyncManager.sync();

        webView.loadUrl(URL);



    }

    public class WebAppInterface {
        Activity mActivity;

        /** Instantiate the interface and set the activity */
        WebAppInterface(Activity activity) {
            mActivity = activity;
        }

        /** Finish activity from the web page */
        @JavascriptInterface
        public void finishActivity() {
            Log.d("EndApp", "Ending App.");
            mActivity.finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        boolean handled = false;
        System.out.println(keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            webView.goBack();
            return true;
        }

        return handled || super.onKeyDown(keyCode, event);
    }


}