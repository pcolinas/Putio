package pe.ra.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetInfo{
/*Función que hace la petición y que devuelve un array JSON*/
		public static JSONArray getLiveInfo(String url1, String url2/*, String query*/){
			JSONArray result = new JSONArray();
			String data = new String();
	        BufferedReader in = null;
	        StringBuilder sBuilder = null;
	        HttpsURLConnection urlConnection = null;		
	        
	        try {
	        	System.out.println("Empiezo la peticion");
	        	/*Se configura la conexión*/
	        	URL liveInfoURL = new URL(url1);
	        	System.out.println(liveInfoURL);
	        	urlConnection = (HttpsURLConnection) liveInfoURL.openConnection();
	        	
	        	urlConnection.setRequestMethod("GET");
	        	urlConnection.setReadTimeout(40000);
	        	urlConnection.setConnectTimeout(42000);
	        	System.out.println("Intento conectar");	
	        	urlConnection.connect(); 
	        	System.out.println("Conectado");	
	        	in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
	        	sBuilder= new StringBuilder();
	        	
	             /*Leemos el archivo*/
	        	String inputLine;
	        	while ((inputLine = in.readLine()) != null)
	        		sBuilder.append(inputLine+ "\n");  
	        	in.close();
	        
	        	data = sBuilder.toString(); //Lo convertimos a String
	        
	        	
	        	try { 
	        		/* Se obtiene el objeto JSON con la información */
	        		JSONObject jObject = new JSONObject(data);
	        		result.put(jObject);
	   	 			
	   	 			
	   	 			
	        	} catch (JSONException e) {
					e.printStackTrace();
					return null;
				}
	   	 		
	        	liveInfoURL = new URL(url2);
	        	System.out.println(liveInfoURL);
	        	urlConnection = (HttpsURLConnection) liveInfoURL.openConnection();
	        	
	        	urlConnection.setRequestMethod("GET");
	        	urlConnection.setReadTimeout(40000);
	        	urlConnection.setConnectTimeout(42000);
	        	System.out.println("Intento conectar");	
	        	urlConnection.connect(); 
	        	System.out.println("Conectado");	
	        	in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
	        	sBuilder= new StringBuilder();
	        	
	             /*Leemos el archivo*/
	        	while ((inputLine = in.readLine()) != null)
	        		sBuilder.append(inputLine+ "\n");  
	        	in.close();
	        
	        	data = sBuilder.toString(); //Lo convertimos a String
	        
	        	
	        	try { 
	        		/* Se obtiene el objeto JSON con la información */
	        		JSONObject jObject = new JSONObject(data);
	        		result.put(jObject);
	        		System.out.println("Array: "+result);
	        		return result;
	   	 			
	   	 			
	        	} catch (JSONException e) {
					e.printStackTrace();
					return null;
				}
	   	 		
	        } catch (MalformedURLException e) {
	            e.printStackTrace();
	            return null;
	        } catch (ProtocolException e) {
	        	System.out.println("Carapene");
	            e.printStackTrace();
	            return null;
	        } catch (IOException e) {
	        	System.out.println("Carapene2");
	            e.printStackTrace();
	            return null;
	        }  
	        
	        finally {
	          urlConnection.disconnect();
	          in = null;
	          sBuilder = null;
	          urlConnection = null;          
	        }
			
			/*StringBuilder builder = new StringBuilder();
		    HttpClient client = new DefaultHttpClient();
		    HttpGet httpGet = new HttpGet(url);
		    
		    try {
		        HttpResponse response = client.execute(httpGet);
		        System.out.println("Response: "+response);
		        StatusLine statusLine = response.getStatusLine();
		        int statusCode = statusLine.getStatusCode();
		        if (statusCode == 200) {
		          HttpEntity entity = response.getEntity();
		          InputStream content = entity.getContent();
		          BufferedReader reader = new BufferedReader(new InputStreamReader(content));
		          String line;
		          while ((line = reader.readLine()) != null) {
		            builder.append(line);
		          }
		        } else {
		          System.out.println("Error status code: "+statusCode);
		        }
		      } catch (ClientProtocolException e) {
		        e.printStackTrace();
		      } catch (IOException e) {
		        e.printStackTrace();
		      }
		      String data = builder.toString();
		      System.out.println(data);
		      
		      return null;*/
		      
		}
		
		
		public static JSONObject getFolder(String url){
			
			String data = new String();
	        BufferedReader in = null;
	        StringBuilder sBuilder = null;
	        HttpsURLConnection urlConnection = null;
	        
	        try {
	        	System.out.println("Empiezo la peticion");
	        	/*Se configura la conexión*/
	        	URL liveInfoURL = new URL(url);
	        	System.out.println(liveInfoURL);
	        	urlConnection = (HttpsURLConnection) liveInfoURL.openConnection();
	        	
	        	urlConnection.setRequestMethod("GET");
	        	urlConnection.setReadTimeout(40000);
	        	urlConnection.setConnectTimeout(42000);
	        	System.out.println("Intento conectar");	
	        	urlConnection.connect(); 
	        	System.out.println("Conectado");	
	        	in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
	        	sBuilder= new StringBuilder();
	        	
	             /*Leemos el archivo*/
	        	String inputLine;
	        	while ((inputLine = in.readLine()) != null)
	        		sBuilder.append(inputLine+ "\n");  
	        	in.close();
	        
	        	data = sBuilder.toString(); //Lo convertimos a String
	        
	        	
	        	try { 
	        		/* Se obtiene el objeto JSON con la información */
	        		JSONObject jObject = new JSONObject(data);

	        		return jObject;
	   	 			
	   	 			
	   	 			
	        	} catch (JSONException e) {
					e.printStackTrace();
					return null;
				}
	        } catch (MalformedURLException e) {
	            e.printStackTrace();
	            return null;
	        } catch (ProtocolException e) {
	        	System.out.println("Carapene");
	            e.printStackTrace();
	            return null;
	        } catch (IOException e) {
	        	System.out.println("Carapene2");
	            e.printStackTrace();
	            return null;
	        }
	        
	        finally {
		          //urlConnection.disconnect();
		          in = null;
		          sBuilder = null;
		          urlConnection = null;          
		    }
			
		
		}
		
public static JSONObject getSearch(String url){
			
			String data = new String();
	        BufferedReader in = null;
	        StringBuilder sBuilder = null;
	        HttpURLConnection urlConnection = null;
	        
	        try {
	        	System.out.println("Empiezo la peticion");
	        	/*Se configura la conexión*/
	        	URL liveInfoURL = new URL(url);
	        	System.out.println(liveInfoURL);
	        	urlConnection = (HttpURLConnection) liveInfoURL.openConnection();
	        	
	        	urlConnection.setRequestMethod("GET");
	        	urlConnection.setReadTimeout(40000);
	        	urlConnection.setConnectTimeout(42000);
	        	System.out.println("Intento conectar");	
	        	urlConnection.connect(); 
	        	System.out.println("Conectado");	
	        	in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
	        	sBuilder= new StringBuilder();
	        	
	             /*Leemos el archivo*/
	        	String inputLine;
	        	while ((inputLine = in.readLine()) != null)
	        		sBuilder.append(inputLine+ "\n");  
	        	in.close();
	        
	        	data = sBuilder.toString(); //Lo convertimos a String
	        	
	        	if(!data.equals("")){
		        	data = data.substring(2);
		        	data = data.replace(")", "");
		        	//data = data.substring(2, data.length()-1);
		        	System.out.println("Data: "+data);
	        	}
	        	try { 
	        		
	        		if(!data.equals("")){
	        		/* Se obtiene el objeto JSON con la información */
	        		JSONObject jObject = new JSONObject(data);

	        		return jObject;
	        		}else{
	        			return null;
	        		}
	   	 			
	   	 			
	   	 			
	        	} catch (JSONException e) {
					e.printStackTrace();
					return null;
				}
	        } catch (MalformedURLException e) {
	            e.printStackTrace();
	            return null;
	        } catch (ProtocolException e) {
	        	System.out.println("Carapene");
	            e.printStackTrace();
	            return null;
	        } catch (IOException e) {
	        	System.out.println("Carapene2");
	            e.printStackTrace();
	            return null;
	        }
	        
	        finally {
		          urlConnection.disconnect();
		          in = null;
		          sBuilder = null;
		          urlConnection = null;          
		    }
			
		
		}

public static JSONObject getMagnet(String url){
	
	String data = new String();
    BufferedReader in = null;
    StringBuilder sBuilder = null;
    HttpURLConnection urlConnection = null;
    
    try {
    	System.out.println("Empiezo la peticion");
    	/*Se configura la conexión*/
    	URL liveInfoURL = new URL(url);
    	System.out.println(liveInfoURL);
    	urlConnection = (HttpURLConnection) liveInfoURL.openConnection();
    	
    	urlConnection.setRequestMethod("GET");
    	urlConnection.setReadTimeout(40000);
    	urlConnection.setConnectTimeout(42000);
    	System.out.println("Intento conectar");	
    	urlConnection.connect(); 
    	System.out.println("Conectado");	
    	in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
    	sBuilder= new StringBuilder();
    	
         /*Leemos el archivo*/
    	String inputLine;
    	while ((inputLine = in.readLine()) != null)
    		sBuilder.append(inputLine+ "\n");  
    	in.close();
    
    	data = sBuilder.toString(); //Lo convertimos a String
    	
    	data = data.substring(2);
    	//data = data.replace("(", "");
    	data = data.replace(")", "");
    	//data = data.substring(2, data.length()-1);
    	
    	try { 
    		/* Se obtiene el objeto JSON con la información */
    		JSONObject jObject = new JSONObject(data);

    		return jObject;
	 			
	 			
	 			
    	} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
    } catch (MalformedURLException e) {
        e.printStackTrace();
        return null;
    } catch (ProtocolException e) {
    	System.out.println("Carapene");
        e.printStackTrace();
        return null;
    } catch (IOException e) {
    	System.out.println("Carapene2");
        e.printStackTrace();
        return null;
    }
    
    finally {
          urlConnection.disconnect();
          in = null;
          sBuilder = null;
          urlConnection = null;          
    }
	

}
}