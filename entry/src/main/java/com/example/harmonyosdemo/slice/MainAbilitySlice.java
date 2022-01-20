package com.example.harmonyosdemo.slice;


import com.example.harmonyosdemo.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.TextField;
import ohos.agp.components.webengine.BrowserAgent;
import ohos.agp.components.webengine.JsMessageResult;
import ohos.agp.components.webengine.Navigator;
import ohos.agp.components.webengine.ResourceRequest;
import ohos.agp.components.webengine.WebAgent;
import ohos.agp.components.webengine.WebConfig;
import ohos.agp.components.webengine.WebView;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.ToastDialog;

public class MainAbilitySlice extends AbilitySlice {
    private static final String URL_LOCAL = "dataability://com.example.harmonyosdemo.DataAbility/resources/rawfile/test.html";
    private static final String JS_NAME = "JsCallJava";
    private WebView webview;
    private TextField urlTextField;
    private Navigator navigator;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        initView();
    }

    private void initView() {
        webview = (WebView) findComponentById(ResourceTable.Id_webview);
        urlTextField = (TextField) findComponentById(ResourceTable.Id_textField);
        navigator = webview.getNavigator();
        initButton();
        configWebView();
    }

    private void configWebView() {
        WebConfig webConfig = webview.getWebConfig();

        // 是否支持Javascript，默认值false
        webConfig.setJavaScriptPermit(true);
        webview.setWebAgent(new WebAgent() {
            @Override
            public boolean isNeedLoadUrl(WebView webView, ResourceRequest request) {
                if (request == null || request.getRequestUrl() == null) {
                    return false;
                }
                String url = request.getRequestUrl().toString();
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    webView.load(url);
                    return false;
                } else {
                    return super.isNeedLoadUrl(webView, request);
                }
            }
        });

        webview.setBrowserAgent(new BrowserAgent(this) {
            @Override
            public boolean onJsMessageShow(WebView webView, String url, String message, boolean isAlert, JsMessageResult result) {
                if (isAlert) {
                    new ToastDialog(getApplicationContext()).setText(message).setAlignment(LayoutAlignment.CENTER).show();
                    result.confirm();
                    return true;
                } else {
                    return super.onJsMessageShow(webView, url, message, isAlert, result);
                }
            }
        });

        // 配置JS发来的消息处理
        webview.addJsCallback(JS_NAME, str -> {
            // 处理接收到的Js发送来的消息
            new ToastDialog(this).setText(str).setAlignment(LayoutAlignment.CENTER).show();

            // 返回给Js
            return "Js Call Java Success";
        });
    }

    private void initButton() {
        initLoadUrlButton();
        initGoBackButton();
        initGoForwardButton();
        initLoadLocalUrlButton();
        initCallJsButton();
    }

    private void initCallJsButton() {
        Button callJs = (Button) findComponentById(ResourceTable.Id_callJS);
        callJs.setClickedListener(component -> {
            webview.executeJs("javascript:callJS('这是来自JavaSlice的消息')", msg -> {
                // 在这里处理Java调用Js的方法的返回值
            });
        });
    }

    private void initLoadLocalUrlButton() {
        Button loadLocalUrlButton = (Button) findComponentById(ResourceTable.Id_load_local_url);
        loadLocalUrlButton.setClickedListener(component -> {
            webview.load(URL_LOCAL);
        });
    }

    private void initLoadUrlButton() {
        Button loadUrlButton = (Button) findComponentById(ResourceTable.Id_loadUrl);
        loadUrlButton.setClickedListener(component -> {
            webview.load(urlTextField.getText());
        });
    }

    private void initGoBackButton() {
        Button goBackButton = (Button) findComponentById(ResourceTable.Id_goback);
        goBackButton.setClickedListener(component -> {
            if (navigator.canGoBack()) {
                navigator.goBack();
            }
        });
    }

    private void initGoForwardButton() {
        Button goForwardButton = (Button) findComponentById(ResourceTable.Id_forward);
        goForwardButton.setClickedListener(component -> {
            if (navigator.canGoForward()) {
                navigator.goForward();
            }
        });
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}