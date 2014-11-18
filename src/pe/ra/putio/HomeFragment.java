package pe.ra.putio;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pe.ra.utilities.Functions;
import pe.ra.utilities.GetInfo;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
 
public class HomeFragment extends Fragment {
	
	String accountInfo;
	String filesUrl;
	JSONObject info;
	String size = "";
	String totalsize = "";
	
	ProgressBar pb;
	TextView memory;
	TextView total;
     
    public HomeFragment(){}
     
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
          View rootView = inflater.inflate(R.layout.fragment_home, container, false);          
          
          accountInfo = MainActivity.accountInfo;
          filesUrl = MainActivity.filesUrl;
          pb = (ProgressBar)rootView.findViewById(R.id.spinner);
          memory = (TextView)rootView.findViewById(R.id.memory);
          total = (TextView)rootView.findViewById(R.id.total);
          
          URLReader userInfo = new URLReader();
          userInfo.execute(accountInfo, filesUrl);

          
          return rootView;
    }
    
   /* @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onActivityCreated(savedInstanceState);
    	
        URLReader userInfo = new URLReader();
        userInfo.execute(accountInfo);
    	

    }*/
    
    public void ready(){
    	
    	try {
    		System.out.println("Estoy en ready"+info.getString("avail"));
			size = info.getString("avail");
			totalsize = info.getString("size");
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	
    	size = Functions.showSize(size);
    	totalsize = Functions.showSize(totalsize);
    	
    	pb.setVisibility(View.GONE);
    	memory.setText(size);
    	total.setText("Total: "+totalsize);
    	
    	
    }
    
    
    
    
    /**** Tarea asíncrona que realiza la petición ****/
    public class URLReader extends AsyncTask<String, Void, JSONArray>{
      
      /* Método por defecto para realizar acciones antes de la ejecución */
      @Override
      protected void onPreExecute(){
      }
      
      /* Método en el que se realiza la petición */
      @Override
      protected JSONArray doInBackground(String... str)
      {
        return GetInfo.getLiveInfo(str[0], str[1]);
      }
      
      /* Método que se ejecuta después de la petición ya con la información */
      protected void onPostExecute(JSONArray result){  
    	/* Si se han recibido datos */  
    	System.out.println("Estoy en post");  
        if (result != null){
        	System.out.println("Hay resultado");
	        try{
	        	System.out.println("Tengo info");
		          JSONObject jObj = new JSONObject(result.getJSONObject(0).getString("info"));
		          info = new JSONObject(jObj.getString("disk"));
		          MainActivity.filesInfo = new JSONArray(result.getJSONObject(1).getString("files"));
		          ready();
		          		          
	        } catch (JSONException e) {
				e.printStackTrace();
			}
        }else{
        	System.out.println("Vuelvo sin info");
        }
       }
        
      

    }
}