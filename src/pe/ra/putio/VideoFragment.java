package pe.ra.putio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import pe.ra.utilities.FileElmnt;
import pe.ra.utilities.GetInfo;
import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

 
public class VideoFragment extends Fragment {
	
	ImageView imgVid;
	ImageView imgPic;
	FileElmnt item;
	Bitmap image;
	ProgressBar pb;
	TextView name;
	Button dwnld;
	Button dwnld2;
	Button dwnld3;
	Button cancel;
	Button cancel2;
	Button cancel3;
	Button mp4;
	Button play;
	TextView converting;
	String itemUrl;
	String itemUrlToMp4;
	String dwnldUrl;
	String convertUrl;
	String textResult = "null";
	DownloadManager dm;
	long enqueue;
	long downloadId;
	JSONObject mp4Info;
	Thread wait;
	Boolean pause = false;
	Boolean convertion = false;
	
	FrameLayout frameLayout;
	LayoutInflater layIn;
	ViewGroup cont;
	View rootView;

     
    public VideoFragment(){}
     
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	itemUrl = MainActivity.itemUrl;
    	itemUrlToMp4 = MainActivity.itemUrlToMp4;
    	dwnldUrl = MainActivity.dwnldUrl;
    	convertUrl = MainActivity.convertUrl;
    	
    	layIn = inflater;
    	cont = container;
    	frameLayout = new FrameLayout(getActivity());
    	
    	if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
    		rootView = inflater.inflate(R.layout.fragment_video, container, false);
    	}else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
    		rootView = inflater.inflate(R.layout.fragment_video_land, container, false);
    	}
        frameLayout .addView(rootView);
        
        item = MainActivity.selectedItem;        

		getActivity().getActionBar().setTitle(item.getName());
        
        start();
        
        return frameLayout;
    }
    
public void start(){
	
    
    identifyLayoutItems();
    
                   
    /*cancel.setVisibility(View.GONE);
    cancel2.setVisibility(View.GONE);
    cancel3.setVisibility(View.GONE);*/
    
    itemUrl = itemUrl.replace("{{id}}", item.getId());
    itemUrlToMp4 = itemUrlToMp4.replace("{{id}}", item.getId());
    
           
    
    if(item.getContentType().equals("video/x-matroska") || item.getContentType().equals("video/x-msvideo") || item.getContentType().equals("video/mpeg")){
    	name.setText(item.getName());
    	dwnld2.setVisibility(View.GONE);
    	dwnld3.setVisibility(View.GONE);
    	play.setVisibility(View.GONE);
    	SetImg set = new SetImg();
        set.execute();
    }else if(item.getContentType().equals("video/mp4")){
    	name.setText(item.getName());
    	dwnld2.setVisibility(View.GONE);
    	dwnld.setVisibility(View.GONE);
    	mp4.setVisibility(View.GONE);
    	play.setVisibility(View.GONE);
    	
    	SetImg set = new SetImg();
        set.execute();
    }else if(item.getContentType().equals("image/jpeg")){
    	name.setText(item.getName());
    	mp4.setVisibility(View.GONE);        	
    	dwnld.setVisibility(View.GONE);
    	dwnld3.setVisibility(View.GONE);
    	play.setVisibility(View.GONE);
    	SetImg set = new SetImg();
        set.execute();
        
    }else if(item.getContentType().equals("audio/mpeg")){
    	name.setText(item.getName());
		System.out.println("Entro en audio");
		imgVid.setImageResource(R.drawable.ic_music);			
		mp4.setVisibility(View.GONE);        	
    	dwnld2.setVisibility(View.GONE);
    	dwnld3.setVisibility(View.GONE);
    	pb.setVisibility(View.GONE);
    	imgPic.setVisibility(View.GONE);
    	
    	play.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
		        Uri data = Uri.parse(itemUrl);
		        intent.setDataAndType(data, "audio/*");
		        startActivity(intent);
				
			}
		});
		
		
	}else if(item.getContentType().equals("text/plain") || item.getContentType().equals("text/x-c")){
		dwnld.setVisibility(View.GONE);
    	play.setVisibility(View.GONE);
    	dwnld2.setVisibility(View.GONE);
    	dwnld3.setVisibility(View.GONE);
    	pb.setVisibility(View.GONE);
    	imgPic.setVisibility(View.GONE);
    	mp4.setVisibility(View.GONE);
    	imgVid.setVisibility(View.GONE);
    	
    	name.setMaxLines(Integer.MAX_VALUE);
    	name.setVerticalScrollBarEnabled(true);
    	name.setMovementMethod(new ScrollingMovementMethod());
    	
    	GetFile gt = new GetFile();
    	gt.execute();
    	
    	
	}
    System.out.println("Is Mp4? "+item.getIsMp4());
    if(item.getIsMp4().equals("true") && !item.getContentType().equals("video/mp4")){
    	System.out.println("Equals true!!");
    	dwnld.setVisibility(View.GONE);
    	mp4.setVisibility(View.GONE);
    	dwnld3.setVisibility(View.VISIBLE);

    }
    
    GetMp4Status status = new GetMp4Status();
    convertUrl = convertUrl.replace("{{id}}", item.getId());
    status.execute(convertUrl);
    
    setBtn1(dwnld);
    setBtn2(dwnld2);
    setBtn3(dwnld3);
    convert(mp4);
    cancelDownload1(cancel);
    cancelDownload2(cancel2);
    cancelDownload3(cancel3);
    
    
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            downloadId = intent.getLongExtra(
                    DownloadManager.EXTRA_DOWNLOAD_ID, 0);
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                
                Query query = new Query();
                query.setFilterById(enqueue);
                Cursor c = dm.query(query);
                if (c.moveToFirst()) {
                    int columnIndex = c
                            .getColumnIndex(DownloadManager.COLUMN_STATUS);
                    if (DownloadManager.STATUS_SUCCESSFUL == c
                            .getInt(columnIndex)) {

                        /*ImageView view = (ImageView) findViewById(R.id.imageView1);
                        String uriString = c
                                .getString(c
                                        .getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                        view.setImageURI(Uri.parse(uriString));*/
                    }
                }
            }
        }
    };

    getActivity().registerReceiver(receiver, new IntentFilter(
            DownloadManager.ACTION_DOWNLOAD_COMPLETE));
      
}
    
    
    public void identifyLayoutItems(){
    	name = (TextView)rootView.findViewById(R.id.name);
        imgVid = (ImageView)rootView.findViewById(R.id.video_img);
        imgPic = (ImageView)rootView.findViewById(R.id.pic_img);
        pb = (ProgressBar)rootView.findViewById(R.id.spinner1);
        dwnld = (Button)rootView.findViewById(R.id.btn_dwn);
        dwnld2 = (Button)rootView.findViewById(R.id.btn_dwn2);
        dwnld3 = (Button)rootView.findViewById(R.id.btn_dwn3);
        cancel = (Button)rootView.findViewById(R.id.btn_cancel);
        cancel2 = (Button)rootView.findViewById(R.id.btn_cancel2);
        cancel3 = (Button)rootView.findViewById(R.id.btn_cancel3);
        mp4 = (Button)rootView.findViewById(R.id.btn_mp4);
        play = (Button)rootView.findViewById(R.id.btn_play);
        converting = (TextView)rootView.findViewById(R.id.converting);
    }
    
    public void startTiming(){
    	wait = new Thread(new Wait());
		wait.start();
    }
    
    
    public class SetImg extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			try
	        {
	        //URL url = new URL( "http://a3.twimg.com/profile_images/670625317/aam-logo-v3-twitter.png");


	        image = downloadBitmap(item.getScreenshot());
	        }
	        catch(Exception e)
	        {
	            e.printStackTrace();
	        }
	        return null;
		}
		
		@SuppressWarnings("deprecation")
		@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
		@Override
	    protected void onPostExecute(Void result) {
	        // TODO Auto-generated method stub
	        super.onPostExecute(result);
	        if(image!=null)
	        {
				BitmapDrawable bd = new BitmapDrawable(getActivity().getResources(), image);
	            //iv.setImageBitmap(image);
				
				System.out.println("TYPE: "+item.getContentType());
				if(item.getContentType().equals("video/x-matroska") || item.getContentType().equals("video/x-msvideo") || item.getContentType().equals("video/mp4") || item.getContentType().equals("video/mpeg")){
					imgPic.setVisibility(View.GONE);
					int sdk = android.os.Build.VERSION.SDK_INT;
					if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
					    imgVid.setBackgroundDrawable(bd);
					} else {
					    imgVid.setBackground(bd);
					}
					pb.setVisibility(View.GONE);
					
					if(item.getIsMp4().equals("true") || item.getContentType().equals("video/mp4")){
						imgVid.setImageResource(R.drawable.play);
						
						
						imgVid.setOnClickListener(new OnClickListener(){
							
							@Override
							public void onClick(View v) {
								Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
								Uri data;
								if(!item.getContentType().equals("video/x-msvideo") || !item.getContentType().equals("video/mpeg")){
									data = Uri.parse(itemUrl);
								}else{
									System.out.println("Soy avi por mp4 "+itemUrlToMp4);
									data = Uri.parse(itemUrlToMp4);
								}
								System.out.println("Url: " + data);
						        intent.setDataAndType(data, "video/*");
						        startActivity(intent);
								
							}
						});
					}
				}else if(item.getContentType().equals("image/jpeg")){
					imgVid.setVisibility(View.GONE);
					imgPic.setImageDrawable(bd);
					
					imgPic.setOnClickListener(new OnClickListener(){
						
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
					        Uri data = Uri.parse(itemUrl);
					        intent.setDataAndType(data, "image/*");
					        startActivity(intent);
							
						}
					});
				}
	        }

	    } 
		
		private Bitmap downloadBitmap(String url) {
		     // initilize the default HTTP client object
		     final DefaultHttpClient client = new DefaultHttpClient();

		     //forming a HttoGet request 
		     final HttpGet getRequest = new HttpGet(url);
		     try {

		         HttpResponse response = client.execute(getRequest);

		         //check 200 OK for success
		         final int statusCode = response.getStatusLine().getStatusCode();

		         if (statusCode != HttpStatus.SC_OK) {
		        	 System.out.println("ImageDownloader error code: "+statusCode);
		             return null;

		         }

		         final HttpEntity entity = response.getEntity();
		         if (entity != null) {
		             InputStream inputStream = null;
		             try {
		                 // getting contents from the stream 
		                 inputStream = entity.getContent();

		                 // decoding stream data back into image Bitmap that android understands
		                 image = BitmapFactory.decodeStream(inputStream);


		             } finally {
		                 if (inputStream != null) {
		                     inputStream.close();
		                 }
		                 entity.consumeContent();
		             }
		         }
		     } catch (Exception e) {
		         // You Could provide a more explicit error message for IOException
		         getRequest.abort();
		         System.out.println("ImageDownloader error: "+e.toString());		         
		     } 

		     return image;
		 }
    }
    
    
    public class GetFile extends AsyncTask<Void, Void, String>{

		@Override
		protected String doInBackground(Void... params) {
			
			URL url;
			try {
				url = new URL(itemUrl);
			

            // Read all the text returned by the server
            BufferedReader in = new BufferedReader(new     InputStreamReader(url.openStream()));
            String str;
            StringBuilder sb = new StringBuilder();
            while ((str = in.readLine()) != null) {
                sb.append(str);
                // str is one line of text; readLine() strips the newline character(s)
            }
            in.close();
            textResult = sb.toString();
            
            return textResult;
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
	        return null;
		}
		
		@Override
	    protected void onPostExecute(String result) {
	        // TODO Auto-generated method stub
	        
	        if(!result.equals("null")){
	        	
	            name.setText(result);
	        }else{
	        	name.setText("Error. Try again later, please.");
	        }
	        
	     }

	}
    
    public void setBtn1(final Button btn){
    	
    	btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				DownloadFile down = new DownloadFile();
				dwnldUrl = dwnldUrl.replace("{{id}}", item.getId());
				down.execute(dwnldUrl);
				btn.setVisibility(View.GONE);
				cancel.setVisibility(View.VISIBLE);
				
			}
});
    	
    }
    
    public void setBtn2(final Button btn){
    	
    	btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				DownloadFile down = new DownloadFile();
				dwnldUrl = dwnldUrl.replace("{{id}}", item.getId());
				down.execute(dwnldUrl);
				btn.setVisibility(View.GONE);
				cancel2.setVisibility(View.VISIBLE);
				
			}
    	});
    	
    }
    
    public void setBtn3(final Button btn){
    	
    	
    	btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				DownloadFile down = new DownloadFile();
				dwnldUrl = dwnldUrl.replace("{{id}}", item.getId());
				down.execute(dwnldUrl);
				btn.setVisibility(View.GONE);
				cancel3.setVisibility(View.VISIBLE);
				
			}
    	});
    }

    
    public void convert(Button btn){
    	btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				ConvertMp4 toMp4 = new ConvertMp4();
				convertUrl = convertUrl.replace("{{id}}", item.getId());
				toMp4.execute(convertUrl);

				
			}
		});
    }
    
    public void cancelDownload1(Button btn){
    	btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				System.out.println("Le di a cancel");
				dm.remove(enqueue);
				cancel.setVisibility(View.GONE);
				dwnld.setVisibility(View.VISIBLE);
				
			}
		});
    }
    
    public void cancelDownload2(Button btn){
    	btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				System.out.println("Le di a cancel");
				dm.remove(enqueue);
				cancel2.setVisibility(View.GONE);
				dwnld2.setVisibility(View.VISIBLE);
				
			}
		});
    }
    
    public void cancelDownload3(Button btn){
    	btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				System.out.println("Le di a cancel");
				dm.remove(enqueue);
				cancel3.setVisibility(View.GONE);
				dwnld3.setVisibility(View.VISIBLE);
				
			}
		});
    }
    
    public class DownloadFile extends AsyncTask<String, Void, Void>{

		@Override
		protected Void doInBackground(String... params) {
			
			dm = (DownloadManager) getActivity().getSystemService(getActivity().DOWNLOAD_SERVICE);
	        Request request = new Request(
	                Uri.parse(params[0]));

	     // only download via WIFI
	        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
	        request.setTitle(item.getName());
	        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
	        //request.setDescription("Downloading a very large zip");
	        
	        request.setDestinationInExternalPublicDir("/putiodown", item.getName());
	        
	        enqueue = dm.enqueue(request);           
			
	        return null;
		}

	}
    
    public class ConvertMp4 extends AsyncTask<String, Void, Integer>{

		@Override
		protected Integer doInBackground(String... params) {
			
			// Create a new HttpClient and Post Header
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost(params[0]);

		    try {
		        // Execute HTTP Post Request
		        HttpResponse response = httpclient.execute(httppost);
		        
		        StatusLine statusLine = response.getStatusLine();
		        int code = statusLine.getStatusCode();
		        
		        System.out.println("CODE: "+code);

		    return code;    
		    } catch (ClientProtocolException e) {
		        // TODO Auto-generated catch block
		    } catch (IOException e) {
		        // TODO Auto-generated catch block
		    }          
			
	        return -1;
		}
		
		@Override
		protected void onPostExecute(Integer result){
			
			if(result == 200){
	        	//Todo ok
	        	//Quitar botón y poner converting...
				dwnld.setVisibility(View.GONE);
				dwnld2.setVisibility(View.GONE);
				dwnld3.setVisibility(View.VISIBLE);
				mp4.setVisibility(View.GONE);
				converting.setVisibility(View.VISIBLE);	
				startTiming();
				
				
				
	        }
		}

	}
    
    public class GetMp4Status extends AsyncTask<String, Void, JSONObject>{
        
        @Override
        protected void onPreExecute(){
        }
        
        @Override
        protected JSONObject doInBackground(String... str)
        {
          return GetInfo.getFolder(str[0]);
        }
        

        protected void onPostExecute(JSONObject result){  

      	System.out.println("Estoy en post");        	
         if(result!=null){
        	 
        	 try {
				mp4Info = new JSONObject(result.getString("mp4"));
				if(mp4Info.getString("status").equals("CONVERTING")){
					convertion = true;
					dwnld.setVisibility(View.GONE);
					dwnld2.setVisibility(View.GONE);
					dwnld3.setVisibility(View.VISIBLE);
					mp4.setVisibility(View.GONE);
					converting.setVisibility(View.VISIBLE);					
					converting.setText("Converting... "+mp4Info.getString("percent_done")+"%");
					startTiming();
					
				}else if(mp4Info.getString("status").equals("COMPLETED")){
					if(convertion){
						dwnld.setVisibility(View.GONE);
						dwnld2.setVisibility(View.GONE);
						dwnld3.setVisibility(View.VISIBLE);
						mp4.setVisibility(View.GONE);
						converting.setVisibility(View.GONE);
						pb.setVisibility(View.GONE);
						imgVid.setImageResource(R.drawable.play);
						
						
						imgVid.setOnClickListener(new OnClickListener(){
							
							@Override
							public void onClick(View v) {
								Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
								Uri data;
								if(!item.getContentType().equals("video/x-msvideo")|| !item.getContentType().equals("video/mpeg")){
									data = Uri.parse(itemUrl);
								}else{
									System.out.println("Soy avi por mp4 "+itemUrlToMp4);
									data = Uri.parse(itemUrlToMp4);
								}
								System.out.println("Url: " + data);
						        intent.setDataAndType(data, "video/*");
						        startActivity(intent);
								
							}
						});
					}
					
				}else if(mp4Info.getString("status").equals("IN_QUEUE")){
					startTiming();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
         }
              

      }
        
    }
    
    public class Wait implements Runnable{

		@Override
		public void run() {
			try {
				Thread.sleep(5000);
				GetMp4Status status = new GetMp4Status();
				status.execute(convertUrl);
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			stopThread();
			
		}
    	
    }
    
    public void stopThread(){
		/* Se detiene el hilo */
		 if(wait != null){
			  wait.interrupt();
			  wait = null;
		  }
	}
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
        	frameLayout. removeAllViews();

            //LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //view = inflater.inflate(R.layout.testone, null);
        	rootView = layIn.inflate(R.layout.fragment_video_land, cont, false); 
            frameLayout .addView(rootView);
            start();
            
        }
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
        	//View rootView = layIn.inflate(R.layout.fragment_video, cont, false);
        	frameLayout. removeAllViews();
        	rootView = layIn.inflate(R.layout.fragment_video, cont, false); 
            frameLayout .addView(rootView);
            start();

        }
    }
       
    
    @Override
    public void onResume(){
	   if(pause){
		   wait = new Thread(new Wait());
		   wait.start();
	   }
    	super.onResume();
    	System.out.println("RESUME");
    }
    
    
    @Override
    public void onPause(){
    	System.out.println("PAUSE");
    	pause = true;
    	stopThread();
    	super.onPause();
    }
    
    @Override
    
    public void onDestroy(){
    	stopThread();
    	super.onDestroy();  
    }
}