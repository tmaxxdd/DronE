package com.czterysery.hop.drone;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by tmax0 on 03.10.2017.
 */

public class CheckListActivity extends Activity {
    private ConnectionManager connectionManager;
    private LayoutWorker layoutWorker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checklist_layout);

        //Initialize utils class
        connectionManager = new ConnectionManager(this);
        layoutWorker = new LayoutWorker(this);

        //check if is online
        if (connectionManager.isOnline()){

            //Use Google Docs Viewer to read your pdf online
            WebView webview = (WebView) findViewById(R.id.webview);
            webview.getSettings().setJavaScriptEnabled(true);
            webview.setWebViewClient(new AppWebViewClients());
            String pdf = getString(R.string.checklist_link);//Link for file
            webview.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + pdf);
        }else{
            layoutWorker.toast("No internet connection. Cannot open page.");
            finish();
        }

    }
}

class AppWebViewClients extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // TODO Auto-generated method stub
        view.loadUrl(url);
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        // TODO Auto-generated method stub
        super.onPageFinished(view, url);

    }
}
