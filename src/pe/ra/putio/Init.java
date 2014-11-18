package pe.ra.putio;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

public class Init extends Activity{
	
	public static SharedPreferences pref;
	public static Editor editor;
	
	public static Boolean onBack = false;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        
        if(pref.getBoolean("loggedIn", false)){
        	System.out.println("Logueado");
        	Intent i = new Intent();
            i.setClass(this, MainActivity.class);
            startActivity(i);
        }else{
        	Intent i = new Intent();
            i.setClass(this, Login.class);
            startActivity(i);
        }
        
	}
	
	@Override
	protected void onResume(){
		System.out.println("Resume!!!! "+onBack);
		if(onBack) finish();
		super.onResume();
	}
}