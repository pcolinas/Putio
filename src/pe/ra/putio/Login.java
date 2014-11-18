package pe.ra.putio;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class Login extends Activity{
	
	WebView webView;
	public static String serverUrl = "https://api.put.io/v2/oauth2/authenticate?client_id={{client_id}}&response_type=token&redirect_uri=http://putioapp.com/pquery/callback.php";

	String clientId = "1701";
	String comparingUrl = "http://putioapp.com/pquery/callback.php#access_token=";
	String sUrl = "http://putioapp.com/pquery/callback2.php";
	String token;
	Boolean first = true;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
      //Get webview 
        webView = (WebView) findViewById(R.id.webViewLogin);
        
        /*webView.clearCache(true); 
    	webView.clearHistory(); */
        
        CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    	
        serverUrl = serverUrl.replace("{{client_id}}", clientId);
        
        startWebView(serverUrl);
	}
 
	
	
	private void startWebView(String url) {
        
		System.out.println("START");
        //Create new webview Client to show progress dialog
        //When opening a url or click on link
         
        webView.setWebViewClient(new WebViewClient() {      
            //ProgressDialog progressDialog;
          
            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {              
            		
            	view.loadUrl(url);
                return true;
            }
        
            //Show loader on url load
            public void onLoadResource (WebView view, String url) {
            	System.out.println("LOAD");
                /*if (progressDialog == null) {
                    // in standard case YourActivity.this
                    progressDialog = new ProgressDialog(Login.this);                   
                    //progressDialog.setMessage("Loading...");
                    progressDialog.show();
                }*/
            	if(webView.getUrl().startsWith(comparingUrl)){
            		if(first){
	            		token = webView.getUrl().replace(comparingUrl, "");
	            		System.out.println("TOKEN :"+token);
	            		Init.editor.putString("token", token);
	            		Init.editor.putBoolean("loggedIn", true);
	            		Init.editor.commit();
	            		
	            		finish();
	            		
	            		Intent i = new Intent();
	                    i.setClass(getApplicationContext(), Init.class);
	                    startActivity(i);
	                    
	                   Toast.makeText(getApplicationContext(),"Succesfully logged in" , Toast.LENGTH_LONG).show();
            		}
            		first = false;
            		
            	}
            	
            }
            
            public void onPageFinished(WebView view, String url) {
                try{
                //if (progressDialog.isShowing()) {
                	System.out.println("Page finished");
                	System.out.println("URL: "+webView.getUrl());
                	
                   /* progressDialog.dismiss();
                    progressDialog = null;*/
                //}
                }catch(Exception exception){
                    exception.printStackTrace();
                }
            }
             
        }); 
          
         // Javascript inabled on webview  
        webView.getSettings().setJavaScriptEnabled(true); 
         
        // Other webview options
        /*
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
        webView.getSettings().setBuiltInZoomControls(true);
        */
         
        /*
         String summary = "<html><body>You scored <b>192</b> points.</body></html>";
         webview.loadData(summary, "text/html", null); 
         */
         
        //Load url in webview
        webView.loadUrl(url);
        
        String webUrl = webView.getUrl();
        //System.out.println("URL: "+webUrl);
          
          
    }
     
    // Open previous opened link from history on webview when back button pressed
     
    @Override
    // Detect when the back button is pressed
    public void onBackPressed() {
        if(webView.canGoBack()) {
            webView.goBack();
        } else {
        	Init.onBack = true;
    		finish();
        }
    }
    
    @Override
    
    public void onDestroy(){
    	
    	webView.clearCache(true); 
    	webView.clearHistory(); 
    	webView.destroy(); 
    	super.onDestroy();
    }
	
}