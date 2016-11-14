package com.zh.xiche.utils;

import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.RenderPriority;

public class WebViewUtil {

	public static void comSetting(WebView webView) {
		// 关闭横向滚动条
		webView.setHorizontalScrollBarEnabled(false);
		webView.setWebChromeClient(new WebChromeClient());
		// 初始化页面比例
//		webView.setInitialScale(48);
		WebSettings webSettings = webView.getSettings();
		// 启用插件
		webSettings.setPluginState(PluginState.ON);
		// 提高渲染等级
		webSettings.setRenderPriority(RenderPriority.HIGH);
		// 自动适用屏幕
		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(true);
		// 支持js
		webSettings.setJavaScriptEnabled(true);

		// 支持js打开页面
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		// 设置可以访问文件
		webSettings.setAllowFileAccess(true);
		// 支持触摸缩放
		webSettings.setBuiltInZoomControls(true);
		// 是否显示放大缩小按钮
		webSettings.setDisplayZoomControls(false);
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);

		/*webSettings.setLoadWithOverviewMode(true);
		webSettings.setBlockNetworkLoads(true);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		webSettings.setNeedInitialFocus(true);*/
//		webSettings.setTextZoom(300);
//
	}
}
