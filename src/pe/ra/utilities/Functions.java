package pe.ra.utilities;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import pe.ra.putio.MainActivity;
import pe.ra.putio.R;
import android.app.Activity;
import android.graphics.drawable.Drawable;

public class Functions{

	public static String showSize(String memory){
		int cont = 0;
		double bytes = Double.parseDouble(memory);
		String unit;
		while(bytes > 1024){
				bytes /= 1024;
				cont++;
		}
		if (bytes > 100) {
			
			bytes 	= Math.round(bytes);
			
		}else {
			
			bytes 	= Math.round(bytes*10)/10.0;
	
		}
	
		switch (cont) {
			case 1:
				unit = "K";
				break;
			case 2:
				unit = "M";
				break;
			case 3:
				unit = "G";
				break;
			case 4:
				unit = "T";
				break;
			
			default:
				unit = "B";
				break;
		}
		
		int decimal = (int)bytes;
		System.out.println("Decimal: "+decimal);
		double fractional = bytes- decimal;
		System.out.println("Frac: "+fractional);
		String result;
		if(fractional == 0.0){
			result = Integer.toString(decimal)+unit;
		}else{
			result = Double.toString(bytes)+unit;
		}
		
		
		return result;
	}
	
	public static int getType(String type){
		
		if(type.equals("video/x-matroska") || type.equals("video/x-msvideo") || type.equals("video/mpeg") || type.equals("video/mp4")){
			//Es video
			return R.drawable.ic_movie;
		}else if(type.equals("audio/mpeg")){
			//Es audio
			return R.drawable.ic_music;
		}else if(type.equals("application/x-directory")){
			//Es carpeta
			return R.drawable.ic_folder;
		}else if(type.equals("image/jpeg")){
			//Es img
			return R.drawable.ic_img;
		}else if(type.equals("application/x-iso9660-image")){
			//Es iso
			return R.drawable.ic_file;
		}else if(type.equals("application/zip")){
			//Es zip
			return R.drawable.ic_file;
		}else if(type.equals("application/octet-stream") || type.equals("application/x-rar")){
			//Es rar
			return R.drawable.ic_file;
		}else if(type.equals("application/x-dosexec")){
			//Es lib
			return R.drawable.ic_file;
		}else if(type.equals("text/plain") || type.equals("text/x-c")){
			//Es txt
			return R.drawable.ic_file;
		}else{
			return R.drawable.ic_file;
		}
	}
	
public static int getArrow(String type){
		
		if(type.equals("video/x-matroska") || type.equals("video/x-msvideo") || type.equals("video/mp4") || type.equals("video/mpeg")){
			//Es video
			return R.drawable.ic_play;
		}else if(type.equals("audio/mpeg")){
			//Es audio
			return R.drawable.ic_play;
		}else if(type.equals("application/x-directory")){
			//Es carpeta
			return R.drawable.ic_arrow;
		}else if(type.equals("image/jpeg")){
			//Es img
			return R.drawable.ic_play;
		}else if(type.equals("application/x-iso9660-image")){
			//Es iso
			return R.drawable.ic_arrow_w;
		}else if(type.equals("application/zip")){
			//Es zip
			return R.drawable.ic_arrow_w;
		}else if(type.equals("application/octet-stream") || type.equals("application/x-rar")){
			//Es rar
			return R.drawable.ic_arrow_w;
		}else if(type.equals("application/x-dosexec")){
			//Es lib
			return R.drawable.ic_arrow_w;		
		}else if(type.equals("text/plain") || type.equals("text/x-c")){
			//Es txt
			return R.drawable.ic_arrow;
		}else{
			return R.drawable.ic_arrow_w;
		}
	}


public static String showTime(String seg){
	int cont = 0;
	int secs = Integer.parseInt(seg);
	int sec = secs; 
	int mins = 0;
	int hours = 0;
	int days = 0;
	int hour = 0;
	String time = "";
	while(sec >= 60 && cont < 2){
			if(cont == 0){
				secs = sec%60;
				sec = sec - secs;
				sec /= 60;
				mins = sec; 	
			} 
			if(cont == 1){
				mins = sec%60;
				sec = sec - mins;
				sec /= 60;
				hours = sec;
			} 
						 
			cont++;
	}

	if(hours > 24){
		hour = hours%24;
		hours -= hour;
		hours /= 24;
		days = hours;
	}else hour = hours;
	if(days > 0){
		time += days+"d ";
	}
	if(hour > 0){
		time += hour+"h ";
	}
	if(mins > 0){
		time += mins+"m ";
	}
	if(secs > 0){
		time += secs+"s";
	}

	return time;
}


public static String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException{
    StringBuilder result = new StringBuilder();
    boolean first = true;

    for (NameValuePair pair : params)
    {
        if (first)
            first = false;
        else
            result.append("&");

        result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
        result.append("=");
        result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
    }

    return result.toString();
}


public static ArrayList<FileElmnt> createArray(String id, Activity activity){
	
	JSONArray info = MainActivity.filesInfo;
	ArrayList<FileElmnt> array = new ArrayList<FileElmnt>();
	long j = 0;
	try {
		for(int i = 0; i < info.length(); i++){
			if(info.getJSONObject(i).getString("parent_id").equals(id)){
				System.out.println("Entro en el if");
				
				Drawable icon = activity.getResources().getDrawable(Functions.getType(info.getJSONObject(i).getString("content_type"))); 
				Drawable arrow = activity.getResources().getDrawable(Functions.getArrow(info.getJSONObject(i).getString("content_type")));
				
				if(info.getJSONObject(i).getString("content_type").equals("application/x-directory") && !info.getJSONObject(i).getString("name").equals("items shared with you")){
					FileElmnt popupFile = new FileElmnt(icon, info.getJSONObject(i).getString("name"),
							info.getJSONObject(i).getString("size"), info.getJSONObject(i).getString("id"),
							info.getJSONObject(i).getString("parent_id"), info.getJSONObject(i).getString("content_type"),arrow,
							info.getJSONObject(i).getString("screenshot"), info.getJSONObject(i).getString("is_mp4_available"), j);
					array.add(popupFile);
					j++;
					System.out.println("Añadiendo");
				}
				
				
			}
		}
		return array;
	}catch (JSONException e) {
		e.printStackTrace();
	}
	
	return null;
	
}


}

