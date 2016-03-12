package com.pps1.guiame.guiame.controlador;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.pps1.guiame.guiame.R;

public class DescargaInfo extends AppCompatActivity {

    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descarga_info);

        WebView webView = (WebView) this.findViewById(R.id.webView);
        webView.setWebChromeClient(new MyWebViewClient());
        progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.setMax(100);
        webView.clearCache(true);

        String url = "http://simiungs.esy.es/infoDescarga.html";
        if (validateUrl(url)) {
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(url);

            DescargaInfo.this.progress.setProgress(0);
        }
        //webView.loadUrl(url);
    }

    private boolean validateUrl(String url) {
        return true;
    }

    private class MyWebViewClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            DescargaInfo.this.setValue(newProgress);
            super.onProgressChanged(view, newProgress);
        }
    }

    public void setValue(int progress) {
        this.progress.setProgress(progress);
    }

    public void onBackPressed()
    {
        Intent start = new Intent(DescargaInfo.this,Principal.class);
        startActivity(start);
        finish();
    }

}
