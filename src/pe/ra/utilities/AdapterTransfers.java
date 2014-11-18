package pe.ra.utilities;

import java.util.ArrayList;

import pe.ra.putio.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AdapterTransfers extends BaseAdapter{
	
	protected Activity activity;
	protected ArrayList<TransElmnt> items;
	
	public AdapterTransfers(Activity activity, ArrayList<TransElmnt> items) {
		this.activity = activity;
		this.items = items;
	}
	
	@Override
	public int getCount() {
		return items.size();
	}
	
	@Override
	public Object getItem(int arg0) {
		return items.get(arg0);
	}
	
	@Override
	public long getItemId(int position) {
		return items.get(position).getPos();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Generamos una convertView por motivos de eficiencia
		View v = convertView;
		//Asociamos el layout de la lista que hemos creado
		if(convertView == null){
			LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inf.inflate(R.layout.transfers_item, null);
		}
		
		// Creamos un objeto directivo
		TransElmnt dir = items.get(position);
		
		//Rellenamos el nombre
				TextView name = (TextView) v.findViewById(R.id.trans_name);
				name.setText(dir.getName());
				
				TextView status = (TextView) v.findViewById(R.id.status);
				TextView speed = (TextView) v.findViewById(R.id.speed);
				TextView eta = (TextView) v.findViewById(R.id.eta);
				RelativeLayout transBack = (RelativeLayout) v.findViewById(R.id.trans_back);
							    
			    Display display = activity.getWindowManager().getDefaultDisplay();
			    Point psize = new Point();
			    display.getSize(psize);
			    int width = psize.x;
			    
			    width = width*Integer.parseInt(dir.getPercent())/100;
		
		if(dir.getStatus().equals("DOWNLOADING")){
			//Lo del verde + speed + eta
			transBack.setBackgroundColor(activity.getResources().getColor(R.color.trans_ok));
			transBack.getLayoutParams().width = width;
			status.setText("Status: "+dir.getPercent()+"%");
			speed.setText(dir.getDownSpeed()+"/s");
			eta.setText(dir.getETA());
		}else if(dir.getStatus().equals("COMPLETED")){
			//Sin verde, sin speed, sin eta
			transBack.getLayoutParams().width = 0;
			status.setText("Status: "+dir.getPercent()+"%");
		}else if(dir.getStatus().equals("ERROR")){
			//En status error, sin speed, sin eta
			transBack.setBackgroundColor(activity.getResources().getColor(R.color.trans_fail));
			status.setText("Status: error");
		}else if(dir.getStatus().equals("IN_QUEUE")){
			status.setText("Status: in queue");
		}else if(dir.getStatus().equals("SEEDING")){
			status.setText("Status: "+dir.getPercent()+"%"+" & seeding");
		}else{
			
			status.setText("Status: "+dir.getPercent()+"%");
		}
		

		TextView size = (TextView) v.findViewById(R.id.size);
		size.setText("Size: "+dir.getSize());
		

		// Retornamos la vista
		return v;
	}
}