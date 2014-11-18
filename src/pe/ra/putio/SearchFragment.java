package pe.ra.putio;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pe.ra.utilities.AdapterFiles;
import pe.ra.utilities.AdapterSearch;
import pe.ra.utilities.FileElmnt;
import pe.ra.utilities.Functions;
import pe.ra.utilities.GetInfo;
import pe.ra.utilities.PostInfo;
import pe.ra.utilities.SearchElmnt;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.view.Window.*;
import java.net.HttpURLConnection;

import javax.net.ssl.HttpsURLConnection;
 
public class SearchFragment extends Fragment {
  
	EditText eText;
	String askTorrentz = "http://putioapp.com/pquery/torrent.php?q={{query}}&callback=?";
	String getitMagnet = "http://putioapp.com/pquery/getit.php?url={{url}}&callback=?";
	JSONArray search;
	ListView list;
	String queryUrl;
	String askMagnet;
	ProgressBar pb;
	SearchElmnt selectedItem;
	String torrentUrl;
	ListView popupList;
	static ArrayList<FileElmnt> popupFiles;
	JSONArray info;
	static String selectedId;
	String parentId;
	String torrentName;
	TextView popupText;
	TextView searchText;
	TextView popupTitle;	
	ImageView popupBack;
	Button popupSave;
	String saveUrl = "https://put.io/v2/transfers/add";
	PopupWindow popup;
	ProgressBar pbPopup;
	String magnetUrl;
	
	/* Para poner titulo en la popup window */
	ArrayList<String> parentName;
	String selectedName;
	Boolean firstBack = true;	
	ArrayList<ArrayList<FileElmnt>> popupFilesArray;
	
	static ArrayList<SearchElmnt> files;
	
	Point p;
	int width;
	int height;
	
	
	
    public SearchFragment(){}
     
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	    	
    	info = MainActivity.filesInfo;
    	selectedId = "0";
    	selectedName = "My Files";
    	
    	popupFilesArray = new ArrayList<ArrayList<FileElmnt>>();
    	parentName = new ArrayList<String>();
    	parentName.add(selectedName);
    	
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        
        popup = new PopupWindow(getActivity());
        
        eText = (EditText)rootView.findViewById(R.id.search);
        list = (ListView)rootView.findViewById(R.id.list_search);
        pb = (ProgressBar)rootView.findViewById(R.id.spinner_search);
        searchText = (TextView)rootView.findViewById(R.id.text_search);
        
        
        
        eText.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction()!=KeyEvent.ACTION_DOWN)
                    return true;
				pb.setVisibility(View.VISIBLE);
				String query = eText.getText().toString();
				query = query.replace(" ", "%20");
				System.out.println("Query: "+query);
				queryUrl = askTorrentz.replace("{{query}}", query);
				//queryUrl = askTorrentz.replace("{{query}}", "fargo");
				
				GetTorrents gt = new GetTorrents();
				gt.execute(queryUrl);
				
				InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
					      Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(eText.getWindowToken(), 0);
				
				return true;
			}
        	
        });
        
        getActivity().onWindowFocusChanged(true);
        
        popup.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				System.out.println("Dismiss");
				selectedId = "0";
				selectedName = "My Files";
				
			}
		});
          
        return rootView;
    }
    
    
public class GetTorrents extends AsyncTask<String, Void, JSONObject>{
        
        @Override
        protected void onPreExecute(){
        }
        
        @Override
        protected JSONObject doInBackground(String... str)
        {
          return GetInfo.getSearch(str[0]);
        }
        

        protected void onPostExecute(JSONObject result){  

      	System.out.println("GetSearch: "+result +" "+ result.length());        	
         if(result!=null){
        	 
        	 try {
				search = result.getJSONArray("results");
				System.out.println("Search: "+search+" "+search.length());
				if(search!=null && search.length()!=0){
					searchText.setVisibility(View.GONE);
					list.setVisibility(View.VISIBLE);
					long j = 0;
					files = new ArrayList<SearchElmnt>();
					for (int i = 0; i < search.length()-1; i++) {
						SearchElmnt file = new SearchElmnt(search.getJSONObject(i).getString("titulo"), 
								search.getJSONObject(i).getString("size"), search.getJSONObject(i).getString("seeds"), 
								search.getJSONObject(i).getString("peers"), search.getJSONObject(i).getString("url"), j);
						
						files.add(file);
						j++;
						
						pb.setVisibility(View.GONE);
						AdapterSearch adapter = new AdapterSearch(getActivity(), files);
						list.setAdapter(adapter);
						
						
						list.setOnItemClickListener(new OnItemClickListener() {
				            @Override
				            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				            	
				            	System.out.println("Selected: "+selectedId);
				            	selectedItem = files.get(position);
				            	torrentUrl = selectedItem.getUrl();
				            	torrentName = selectedItem.getName();
				            	System.out.println("Torrent: "+torrentUrl);
				            	
				            	popupFiles = Functions.createArray(selectedId, getActivity());
				            	popupFilesArray.add(popupFiles);
				            	
				            	if(popupFiles.size() ==0){
				            		searchText.setText("No results. Please, try again.");
				            		searchText.setVisibility(View.VISIBLE);
				            		pb.setVisibility(View.GONE);
				            	}
				        			       			
				            	Display display = getActivity().getWindowManager().getDefaultDisplay();
							    p = new Point();
							    display.getSize(p);
							    width = p.x;
							    height = p.y;
							    p.x = p.x/2;
							    p.y = p.y/2;
							 
				            	
				            	if (p != null){
				            	       showPopup(getActivity(), p);
				            	      /* AdapterFiles adapter = new AdapterFiles(getActivity(), popupFiles);
				            	       popupList.setAdapter(adapter);*/
				            	}
				            	
				            }
				            
				        });
					}
				}else{
					System.out.println("Estoy en else porque search vacio");
					//Poner un textview de que no hay resultados
					searchText.setText("No results. Please, try again.");
            		searchText.setVisibility(View.VISIBLE);
            		pb.setVisibility(View.GONE);
            		list.setVisibility(View.GONE);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
         }else{
        	//Poner un textview de que no hay resultados
        	searchText.setText("No results");
     		searchText.setVisibility(View.VISIBLE);
     		pb.setVisibility(View.GONE);
     		list.setVisibility(View.GONE);
         }
              

      }
        
    }


private void showPopup(final Activity context, Point p) {
	   MainActivity.onPopupWndw = true;
	 
	   // Inflate the popup_layout.xml
	   LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
	   LayoutInflater layoutInflater = (LayoutInflater) context
	     .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	   View layout = layoutInflater.inflate(R.layout.popup_layout, viewGroup);
	   
	   popupList = (ListView)layout.findViewById(R.id.list_popup);
	   popupText = (TextView)layout.findViewById(R.id.text_popup);
	   popupSave = (Button)layout.findViewById(R.id.btn_save_here);
	   pbPopup = (ProgressBar)layout.findViewById(R.id.spinner_popup);
	   popupTitle = (TextView)layout.findViewById(R.id.pop_title);
	   popupBack = (ImageView)layout.findViewById(R.id.pop_left);
	   
	   popupTitle.setText("My Files");
	 
	   // Creating the PopupWindow
	   
	   popup.setContentView(layout);
	   popup.setWidth(width-100);
	   popup.setHeight(height-300);
	   popup.setFocusable(true);
	 
	   // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
	   //int OFFSET_X = 30;
	   //int OFFSET_Y = 30;
	 
	   // Clear the default translucent background
	   //popup.setBackgroundDrawable(new BitmapDrawable());
	 
	   // Displaying the popup at the specified location, + offsets.
	   popup.showAtLocation(layout, Gravity.CENTER_HORIZONTAL, 0, 200);
	   
	   AdapterFiles adapterF = new AdapterFiles(getActivity(), popupFiles);
       popupList.setAdapter(adapterF);
	 
	   // Getting a reference to Close button, and close the popup when clicked.
	   
	   popupList.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			parentId = selectedId;			
			System.out.println(parentId);
			selectedId = popupFiles.get(position).getId();
			selectedName = popupFiles.get(position).getName();
			parentName.add(selectedName);
			System.out.println("SelId "+selectedId);
						
			popupTitle.setText(popupFiles.get(position).getName());
						
			popupFiles = Functions.createArray(selectedId, getActivity());
			popupFilesArray.add(popupFiles);
			if(popupFiles.size() == 0){
				popupText.setText("This folder doesn't contain any folder");
				popupText.setVisibility(View.VISIBLE);
			}
			AdapterFiles adapterF = new AdapterFiles(getActivity(), popupFiles);
		    popupList.setAdapter(adapterF);
		}
	 
	     
	   });
	   
	   popupSave.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			//Envío POST
			//System.out.println("Parent: "+parentId);
			//if(parentId.equals(null)) parentId= "0";
						
			GetMagnet magnet = new GetMagnet();
			askMagnet = getitMagnet.replace("{{url}}", torrentUrl);
			
			System.out.println("Torrent: "+askMagnet);
			
			magnet.execute(askMagnet);
			pbPopup.setVisibility(View.VISIBLE);

						
		}
		
		
	});
	   
	popupBack.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			System.out.println("Back ");
			popupText.setVisibility(View.GONE);
			if(popupFilesArray.size()!=0){
				if(firstBack){
					popupFiles = popupFilesArray.remove(popupFilesArray.size()-1);
					popupTitle.setText(parentName.remove(parentName.size()-1));
				}
				popupFiles = popupFilesArray.remove(popupFilesArray.size()-1);
				popupTitle.setText(parentName.remove(parentName.size()-1));
				AdapterFiles adapterF = new AdapterFiles(getActivity(), popupFiles);
			    popupList.setAdapter(adapterF);
			    firstBack = false;
			}
			
		}
	});   
	   
	}


public class GetMagnet extends AsyncTask<String, Void, JSONObject>{
    
    @Override
    protected void onPreExecute(){
    }
    
    @Override
    protected JSONObject doInBackground(String... str)
    {
      return GetInfo.getMagnet(str[0]);
    }
    

    protected void onPostExecute(JSONObject result){  

  	System.out.println("Estoy en post");        	
     if(result!=null){
    	 try {
    		 System.out.println("Respuesta magnet: "+result);
			magnetUrl = result.getString("url");
			
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		    pairs.add(new BasicNameValuePair("url", magnetUrl));
		    pairs.add(new BasicNameValuePair("save_parent_id", selectedId));
		    pairs.add(new BasicNameValuePair("extract", "true"));
		    pairs.add(new BasicNameValuePair("oauth_token", Init.pref.getString("token", null)));
			
		    System.out.println("Save: "+magnetUrl+" "+selectedId+ " "+ Init.pref.getString("token", null));
			Save sv = new Save();
			sv.execute(saveUrl, pairs);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	 
    	 
     }
          

  }
    
}

	
	
	
	
	public class Save extends AsyncTask<Object, Void, Void>{

		@SuppressWarnings("unchecked")
		@Override
		protected Void doInBackground(Object... params) {

			return PostInfo.post((String)params[0], (List<NameValuePair>)params[1]);
			
		}
		
		@Override
		
		protected void onPostExecute(Void result){
			pbPopup.setVisibility(View.GONE);
			popup.dismiss();
		}


	}
}