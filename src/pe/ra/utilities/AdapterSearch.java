package pe.ra.utilities;

import java.util.ArrayList;

import pe.ra.putio.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterSearch extends BaseAdapter{
	
	protected Activity activity;
	protected ArrayList<SearchElmnt> items;
	
	public AdapterSearch(Activity activity, ArrayList<SearchElmnt> items) {
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
			v = inf.inflate(R.layout.search_item, null);
		}
		
		// Creamos un objeto directivo
		SearchElmnt dir = items.get(position);

		//Rellenamos el nombre
		TextView name = (TextView) v.findViewById(R.id.search_name);
		name.setText(dir.getName());
		//Rellenamos el cargo
		TextView size = (TextView) v.findViewById(R.id.search_size);
		size.setText("Size: "+dir.getSize());
		//Rellenamos el cargo
		TextView seeds = (TextView) v.findViewById(R.id.seeds);
		seeds.setText("Seeds: "+dir.getSeeds());
		
		TextView peers = (TextView) v.findViewById(R.id.peers);
		peers.setText("Peers: "+dir.getPeers());
		

		// Retornamos la vista
		return v;
	}
}