package com.example.shake;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class WebActivity extends AppCompatActivity implements View.OnClickListener {
    private WebView webView;
    private final String[] webs = new String[]{
            "https://www.baidu.com",
            "https://www.sohu.com",
            "https://www.bjtu.edu.cn",
            "https://www.sina.com.cn",
            "https://www.zhihu.com",
            "https://www.csdn.net",
            "https://www.chinanews.com.cn",
            "https://www.tencent.com",
            "https://www.163.com",
            "https://www.xiaohongshu.com",
    };
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(webs[randSelect()]);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab) {
            // Toast.makeText(WebActivity.this, "Home", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }

    public int randSelect() {
        return (int)(Math.random()*webs.length);
    }

}
