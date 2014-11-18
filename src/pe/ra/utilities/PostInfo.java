package pe.ra.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import pe.ra.putio.Init;


public class PostInfo{
	
	
	public static Void post(String add, List<NameValuePair> pairs){
		URL url;
		HttpsURLConnection conn = null;
		OutputStream os = null;
		BufferedWriter writer = null;
		BufferedReader is = null;
		StringBuilder sBuilder = null;
		try {
			url = new URL(add);
		
		conn = (HttpsURLConnection) url.openConnection();
		conn.setReadTimeout(10000);
		conn.setConnectTimeout(15000);
		conn.setRequestMethod("POST");
		conn.setDoInput(true);
		conn.setDoOutput(true);
	    
	    os = conn.getOutputStream();
	    writer = new BufferedWriter(
	            new OutputStreamWriter(os, "UTF-8"));
	    writer.write(Functions.getQuery(pairs));
	    writer.flush();
	    writer.close();
	    os.close();

	    conn.connect();
	    
	    
	    is = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    	sBuilder= new StringBuilder();
    	

    	String inputLine;
    	while ((inputLine = is.readLine()) != null)
    		sBuilder.append(inputLine+ "\n");  
    	is.close();
    
    	String data = sBuilder.toString(); //Lo convertimos a String
    	
    	System.out.println("Delete response: "+data);
	    
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
				
		
		finally {
	          conn.disconnect();
	          os = null;
	          writer = null;
	          is = null;
	          sBuilder = null;
	          conn = null;          
	    }
		
        return null;
	}

}
