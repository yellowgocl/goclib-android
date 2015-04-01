package org.goclib.android.core;

import org.goclib.android.R;
import org.goclib.android.utils.ApplicationUtils;
import org.goclib.android.utils.GoclSimpleWebViewClient;
import org.goclib.android.utils.LogUtil;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.webkit.WebBackForwardList;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewHelper extends ActivityHelper {
	private String url;
	private WebView mWebView=null;
	private WebViewClient mWebViewClient=null;
	public static int DEFAULT_WEBVIEW_ID=org.goclib.android.R.id.goclib_webView;
	
	private boolean useReturnMode=false;
	
	public WebViewHelper(Context context){
		//restore(context);
		super(context);
		
	}
	@Override
	protected void init() {
		// TODO Auto-generated method stub
		super.init();
		setWebView((WebView) findViewById(DEFAULT_WEBVIEW_ID));
		if(getWebView()!=null){
			getDefaultWebSettings(getWebView().getSettings());
		}
		//LogUtil.trace(isInited(),this);
	}
	
	public void load(){
		load(null,null);
	}
	public void load(String url){
		load(url,null);
	}
	
	public void load(WebViewClient client){
		load(null,client);
	}
	
	public void load(String path,WebViewClient client){
		LogUtil.trace("url:"+path,this);
		if(getWebView()!=null){
			if(path!=null)
				setUrl(path);
			String _path=getUrl();
			getWebView().loadUrl(_path);
			getWebView().requestFocus();
			if(client!=null)
				setWebViewClient(client);
			if(getWebViewClient()==null)
				setWebViewClient(new GoclSimpleWebViewClient(getContext()));
		}
	}
	public void load(String path,String baseUrl,WebViewClient client){
		load(path,baseUrl,getContext().getString(R.string.goclib_mimetype_html)
				,getContext().getString(R.string.goclib_encoding_utf8)
				,getContext().getString(R.string.goclib_baseurl),client);
	}
	public void load(String path,String baseUrl,String mimeType,String encoding,String failUrl,WebViewClient client){
		LogUtil.trace("url:"+path+",baseurl:"+baseUrl+",mimeType:"+mimeType+",encodoing:"+encoding+","+failUrl,this);
		if(getWebView()!=null){
			
			if(path!=null)
				setUrl(path);
			if(baseUrl ==null)
				baseUrl="file:///android_asset/";
			if(mimeType==null)
				mimeType=getContext().getString(R.string.goclib_mimetype_html);
			if(encoding==null)
				encoding=getContext().getString(R.string.goclib_encoding_utf8);
			if(failUrl==null)
				failUrl=getContext().getString(R.string.goclib_baseurl);
			String _path=getUrl();
			getWebView().loadDataWithBaseURL(baseUrl, _path, mimeType, encoding, failUrl);
			getWebView().requestFocus();
			//LogUtil.trace("url:"+_path,this);
			if(client!=null)
				setWebViewClient(client);
			if(getWebViewClient()==null)
				setWebViewClient(new GoclSimpleWebViewClient(getContext()));
		}
	}
	
	public void setWebViewClient(WebViewClient value){
		if(getWebView()!=null){
			getWebView().setWebViewClient(value);
			mWebViewClient= value;
		}
	}
	public WebViewClient getWebViewClient(){
		return mWebViewClient;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url){
		setUrl(url,false);
	}
	public void setUrl(String url,WebViewClient client){
		setWebViewClient(client);
		setUrl(url,true);
	}
	public void setUrl(String url,boolean autoLoad) {
		this.url = url;
		if(autoLoad)
			load(url);
	}

	@Override
	public boolean onBackKeyClick() {
		// TODO Auto-generated method stub
		boolean _result=super.onBackKeyClick();
		if(!_result && getBackKeyStatus() == BACK_STATUS.WEBVIEW){
			webViewCanGoBack(getWebView());
			_result=true;
		}
		return _result;
	}
	
	public void webViewCanGoBack(WebView webview){
		if(webview == null)
			return;
		if (webview.canGoBack()) {
			WebBackForwardList mWebBackForwardList = webview
                    .copyBackForwardList();
            //判断当前历史列表是否最顶端,其实canGoBack已经判断过
            if (mWebBackForwardList.getCurrentIndex() > 0) {
                //获取历史列表
                String historyUrl = mWebBackForwardList.getItemAtIndex(
                        mWebBackForwardList.getCurrentIndex() - 1).getUrl();
                //按照自己规则检查是否为可跳转地址
                //注意:这里可以根据自己逻辑循环判断,拿到可以跳转的那一个然后webView.goBackOrForward(steps)
                if (historyUrl.contains("s.click.taobao.com")) {
                    //执行跳转逻辑
                	finishActivity();
                }else{
                	webview.goBack();
                }
            }
		}else{
			finishActivity();
		}
	}
	
	private void finishActivity(){
		if(!isUseReturnMode()){
        	if(getActivity()!=null)
        		getActivity().finish();
    	}else{
    		onBackKeyClick();
    	}
	}

	public WebSettings getDefaultWebSettings(WebSettings outWebviewSetting){
		WebSettings webSettings = outWebviewSetting;
		webSettings.setSavePassword(false);
		webSettings.setSaveFormData(false);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setUseWideViewPort(true);
		return webSettings;
	}
	
	public WebView getWebView() {
		return mWebView;
	}

	public void setWebView(WebView mWebView) {
		this.mWebView = mWebView;
	}
	public boolean isUseReturnMode() {
		return useReturnMode;
	}
	public void setUseReturnMode(boolean useReturnMode) {
		this.useReturnMode = useReturnMode;
	}
}
