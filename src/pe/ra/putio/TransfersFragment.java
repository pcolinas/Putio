package pe.ra.putio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pe.ra.putio.FilesFragment.URLReader;
import pe.ra.utilities.AdapterFiles;
import pe.ra.utilities.AdapterTransfers;
import pe.ra.utilities.FileElmnt;
import pe.ra.utilities.Functions;
import pe.ra.utilities.GetInfo;
import pe.ra.utilities.PostInfo;
import pe.ra.utilities.TransElmnt;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
 
public class TransfersFragment extends Fragment {
     
    public TransfersFragment(){}
    
    String transUrl;
    String deleteTrans = "https://api.put.io/v2/transfers/clean";
    ListView list;
    JSONArray transfers;
    static ArrayList<TransElmnt> files;
    Thread myThread;
    Boolean pause = false;
    
    /*Handler hand = new Handler() {
		  @Override
		  public void handleMessage(Message msg) {
			  transfers = (JSONArray)msg.obj;
			  
			  try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			  stopThread();
			  ready();
		  }
	};*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);
           setHasOptionsMenu(true);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	transUrl = MainActivity.transUrl;
    	
        View rootView = inflater.inflate(R.layout.fragment_transfers, container, false);
        
        list = (ListView)rootView.findViewById(R.id.listViewTrans);     
                
        URLReader request = new URLReader();
        request.execute(transUrl);
        
        /*myThread = new Thread(new getTransfers());
        myThread.start();*/
          
        return rootView;
    }
    
    public void ready(){
    	
    	files = new ArrayList<TransElmnt>();
    	
    	long j = 0;
    	try {
			for(int i = 0; i < transfers.length(); i++){
									
				TransElmnt file = new TransElmnt(transfers.getJSONObject(i).getString("name"),
						transfers.getJSONObject(i).getString("size"), transfers.getJSONObject(i).getString("percent_done"), 
						transfers.getJSONObject(i).getString("status"), transfers.getJSONObject(i).getString("down_speed"),
						transfers.getJSONObject(i).getString("estimated_time"), transfers.getJSONObject(i).getString("id"),j);
				files.add(file);
				j++;
				
			}	
			System.out.println("Num transfers "+files.size());
			Collections.reverse(files);
			AdapterTransfers adapter = new AdapterTransfers(getActivity(), files);
			list.setAdapter(adapter);
			
			
			
    	} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	for(int i=0; i<files.size(); i++){
    		if(files.get(i).getStatus().equals("DOWNLOADING")){ //SOLO ESTO AQUI???
    			myThread = new Thread(new getTransfers());
    			myThread.start();
    			break;
    		}
    	}
    	
    	
    }
    
    class getTransfers implements Runnable{

		@Override
		public void run() {
			try {
				Thread.sleep(5000);
				URLReader request = new URLReader();
		        request.execute(transUrl);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			stopThread();
			/*JSONArray trans = new JSONArray();
			
			try {
				trans = GetInfo.getFolder(transUrl).getJSONArray("transfers");
				System.out.println("ARRAY: "+trans);
				Message msg = new Message();
				msg.obj = trans;
				hand.sendMessage(msg);
			} catch (JSONException e) {
				e.printStackTrace();
			}*/			
			
		}
    	
    }
    
    public void showDeleteDialog(){
    	AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
    	
        /*SpannableString str = new SpannableString("Clean transfers");
        str.setSpan(new ForegroundColorSpan(Color.parseColor("#52ba6c")), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	    
        alertDialog.setTitle(str);
        Dialog d = alertDialog.create();
        int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = d.findViewById(dividerId);
        divider.setBackgroundColor(getResources().getColor(R.color.list_background_pressed));*/
        
        // Nombre de la alerta
        alertDialog.setTitle("Clean transfers");
  
        // Mensaje de la alerta
        alertDialog.setMessage("Are you sure you want to clean all transfers?");
        alertDialog.setIcon(R.drawable.ic_action_discard_b);     
        
  
        // Si presionamos Ok se cierra la alerta
        alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            //Si aceptamos
            	List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            	pairs.add(new BasicNameValuePair("oauth_token", Init.pref.getString("token", null)));
            	
            	PostFiles pf = new PostFiles();
            	pf.execute(deleteTrans, pairs);
            	dialog.cancel();
            }
        });
        
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            //Si cancelamos
            	dialog.cancel();
            }
        });
  
        // Mostrar la alerta
        alertDialog.show();
    }
    
    @Override
    public void onCreateOptionsMenu(
          Menu menu, MenuInflater inflater) {
       inflater.inflate(R.menu.transfers_actions, menu);
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar actions click
        switch (item.getItemId()) {
        /*case R.id.action_settings:
            return false;*/
        case R.id.delete_transfers:
        	showDeleteDialog();
        	System.out.println("Delete ALL");
            return true;

        }
		return false;
    }
    
    public void stopThread(){
		/* Se detiene el hilo */
		 if(myThread != null){
			  myThread.interrupt();
			  myThread = null;
		  }
	}
    
   @Override
    public void onResume(){
	   if(pause){
		   myThread = new Thread(new getTransfers());
		   myThread.start();
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
    
    /**** Tarea asíncrona que realiza la petición ****/
    public class URLReader extends AsyncTask<String, Void, JSONObject>{
      
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

        if (result != null){
        	System.out.println("Hay resultado");
	        	System.out.println("Tengo info");
	        	transfers = new JSONArray();
	        	try {
					transfers = result.getJSONArray("transfers");
					System.out.println("ARRAY: "+transfers);
					ready();
				} catch (JSONException e) {
					e.printStackTrace();
				}

		          
        }else{
        	System.out.println("Vuelvo sin info");
        }

       }            

    }
    
    
    public class PostFiles extends AsyncTask<Object, Void, Void>{

		@SuppressWarnings("unchecked")
		@Override
		protected Void doInBackground(Object... params) {
			
			return PostInfo.post((String)params[0], (List<NameValuePair>)params[1]);
		}
		
		@Override
		protected void onPostExecute(Void result){
			/*stopThread();
			myThread = new Thread(new getTransfers());
			myThread.start();*/
		
		}

	}
}