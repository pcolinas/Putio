package pe.ra.utilities;

import java.util.ArrayList;
import java.util.HashMap;

import pe.ra.putio.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterFiles extends BaseAdapter{
	
	protected Activity activity;
	protected ArrayList<FileElmnt> items;
	private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();
	
	public AdapterFiles(Activity activity, ArrayList<FileElmnt> items) {
		this.activity = activity;
		this.items = items;
	}
	
	public void setNewSelection(int position, boolean value) {
        mSelection.put(position, value);
        notifyDataSetChanged();
    }
	
	public void removeSelection(int position) {
        mSelection.remove(position);
        notifyDataSetChanged();
    }
	
	public void clearSelection() {
        mSelection = new HashMap<Integer, Boolean>();
        notifyDataSetChanged();
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
			v = inf.inflate(R.layout.file_list_item, null);
		}
		
		// Creamos un objeto directivo
		FileElmnt dir = items.get(position);
		//Rellenamos la fotografía
		ImageView pic = (ImageView) v.findViewById(R.id.fileimg);
		pic.setImageDrawable(dir.getPic());
		//Rellenamos el nombre
		TextView name = (TextView) v.findViewById(R.id.filename);
		name.setText(dir.getName());
		//Rellenamos el cargo
		TextView size = (TextView) v.findViewById(R.id.filesize);
		size.setText(dir.getSize());
		
		ImageView arrow = (ImageView) v.findViewById(R.id.arrow);
		arrow.setImageDrawable(dir.getArrow());
		// Retornamos la vista
		v.setBackgroundColor(activity.getResources().getColor(android.R.color.background_light));
		if (mSelection.get(position) != null) {
            v.setBackgroundColor(activity.getResources().getColor(R.color.list_background_pressed));// this is a selected position so make it red
        }
		return v;
	}
}