package pe.ra.putio;

import java.util.ArrayList;
import java.util.List;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pe.ra.utilities.AdapterFiles;
import pe.ra.utilities.FileElmnt;
import pe.ra.utilities.Functions;
import pe.ra.utilities.GetInfo;
import pe.ra.utilities.PostInfo;
import pe.ra.utilities.SearchElmnt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.ActionMode;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
 
public class FilesFragment extends Fragment {
    
	private OnItemSelectedListener listener;

	
    public FilesFragment(){}
    
    String filesUrl;
    String folderUrl;
    String deleteUrl = "https://api.put.io/v2/files/delete";
    String moveUrl = "https://api.put.io/v2/files/move";
    String renameUrl = "https://api.put.io/v2/files/rename";
    JSONArray info;
    static ArrayList<FileElmnt> files;
    ListView list;
    String folderId;
    String selectedId;
    String parentId;
    String contentType;
    FileElmnt selectedItem;
    boolean goOn = false;
    boolean refresh = false;
    boolean longclick = false;
    private AdapterFiles adapter;
    
    private SwipeRefreshLayout swipeContainer;
    Context context;
    ArrayList<String> idsToSend;
    ArrayList<String> names;
    ActionMode aMode;
    MenuItem renameIt;
    MenuItem deleteIt;
    MenuItem moveIt;
    
    PopupWindow popup;
    static ArrayList<FileElmnt> popupFiles;
    ListView popupList;
    TextView popupText;
    Button popupSave;
    ProgressBar pbPopup;
    Point p;
	int width;
	int height;
    
    String finalIds = "";
    boolean delete = false;
    boolean move = false;
    boolean rename = false;
    
    /* Para poner titulo en la popup window */
	ArrayList<String> parentName;
	String selectedName;
	Boolean firstBack = true;	
	ArrayList<ArrayList<FileElmnt>> popupFilesArray;
	TextView popupTitle;	
	ImageView popupBack;
	
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	filesUrl = MainActivity.filesUrl;
    	folderUrl = MainActivity.folderUrl;
    	//deleteUrl = MainActivity.deleteUrl;
    	selectedId = MainActivity.selectedId;
    	//MainActivity.selectedId = "null";
    	info = MainActivity.filesInfo;
    	selectedItem = new FileElmnt();
    	
    	idsToSend = new ArrayList<String>();
    	names = new ArrayList<String>();
    	
    	selectedName = "My Files";    	
    	popupFilesArray = new ArrayList<ArrayList<FileElmnt>>();
    	parentName = new ArrayList<String>();
    	parentName.add(selectedName);
    	   	   	    	
    	if(selectedId.equals("null")){
    		selectedId = "0";
    	}
    	
    	if(!selectedId.equals("0")){
    		for(int i=0; i<info.length(); i++){
    			try {
					if(info.getJSONObject(i).getString("id").equals(selectedId)){
						getActivity().getActionBar().setTitle(info.getJSONObject(i).getString("name"));
						break;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
    		}
		}else{
			getActivity().getActionBar().setTitle("My Files");
		}
        View rootView = inflater.inflate(R.layout.fragment_files, container, false);
        
        popup = new PopupWindow(getActivity());
              
        list = (ListView)rootView.findViewById(R.id.listView1);     
        
        swipeContainer = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeContainer);  
        
        
        getActivity().onWindowFocusChanged(true);
        
        popup.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				System.out.println("Dismiss");
				selectedId = "0";
				selectedName = "My Files";
				
			}
		});

        
        ready();        
        
        return rootView;
    }
    
    public interface OnItemSelectedListener {
        public void onRssItemSelected(FileElmnt selectedItem);
    }
    
    @Override
    public void onAttach(Activity activity) {
      super.onAttach(activity);
      if (activity instanceof OnItemSelectedListener) {
        listener = (OnItemSelectedListener) activity;
      } else {
        throw new ClassCastException(activity.toString()
            + " must implemenet MyListFragment.OnItemSelectedListener");
      }
    }
    
    public void ready(){
    	
    	files = new ArrayList<FileElmnt>();
    	long j = 0;
    	try {
			for(int i = 0; i < info.length(); i++){
				if(info.getJSONObject(i).getString("parent_id").equals(selectedId)){
					System.out.println("Entro en el if");
					
					Drawable icon = getResources().getDrawable(Functions.getType(info.getJSONObject(i).getString("content_type"))); 
					Drawable arrow = getResources().getDrawable(Functions.getArrow(info.getJSONObject(i).getString("content_type")));
					
					FileElmnt file = new FileElmnt(icon, info.getJSONObject(i).getString("name"),
							info.getJSONObject(i).getString("size"), info.getJSONObject(i).getString("id"),
							info.getJSONObject(i).getString("parent_id"), info.getJSONObject(i).getString("content_type"),arrow,
							info.getJSONObject(i).getString("screenshot"), info.getJSONObject(i).getString("is_mp4_available"), j);
					files.add(file);
					j++;
				}
			}
			
			adapter = new AdapterFiles(getActivity(), files);
			list.setAdapter(adapter);
			
			list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
	        list.setMultiChoiceModeListener(new MultiChoiceModeListener() {
	        	private int nr = 0;
	        	
	            @Override
	            public void onItemCheckedStateChanged(ActionMode mode, int position,
	                                                  long id, boolean checked) {
	            	deleteIt.setVisible(true);
	            	System.out.println("OnChange");
	            	if(!files.get(position).getName().equals("items shared with you") && 
	            			!MainActivity.onItemsShared) {
		            	if (checked) {
	                        nr++;
	                        adapter.setNewSelection(position, checked);
	                        idsToSend.add(files.get(position).getId());
	                        names.add(files.get(position).getName());
	                    } else {
	                        nr--;
	                        adapter.removeSelection(position);
	                        idsToSend.remove(files.get(position).getId());
	                        names.remove(files.get(position).getName());
	                    }
		            	
		            	if(nr == 1){
		            		renameIt.setVisible(true);
		            	}else{
		            		renameIt.setVisible(false);
		            	}
		            	if(nr == 0){
		            		mode.finish();
		            	}		            		
		            	mode.setTitle(nr + " selected");
	                    
	            	}else{
		            	Toast.makeText(getActivity(), "'"+files.get(position).getName()+"'"+" can't be selected", Toast.LENGTH_LONG).show();
		            	if(nr==0){
		            		mode.finish();
		            	}
		            }
	            	
	            }
                    
	            

				@Override
				public boolean onCreateActionMode(ActionMode mode,
						Menu menu) {
					System.out.println("OnCreate");
					nr = 0;
					idsToSend.clear();
					names.clear();
	                MenuInflater mInflater = getActivity().getMenuInflater();
	                mInflater.inflate(R.menu.files_actions, menu);
	                
	                deleteIt = menu.findItem(R.id.delete);
	                moveIt = menu.findItem(R.id.move);
	                renameIt = menu.findItem(R.id.rename);
	                
	                aMode = mode;
	                return true;
					
				}

				@Override
				public boolean onPrepareActionMode(ActionMode mode,
						Menu menu) {
					System.out.println("OnPrepare");
					return false;
				}

				@Override
				public boolean onActionItemClicked(ActionMode mode,
						MenuItem item) {
					// TODO Auto-generated method stub
					switch (item.getItemId()) {
	                  
                    case R.id.delete:
                    	showDeleteDialog();
                        nr = 0;
                        break;
                        
                    case R.id.move:
                    	showMoveDialog();
                        nr = 0;
                        break;
                        
                    case R.id.rename:
                    	showRenameDialog();
                        nr = 0;
                        break;
                }
                return false;
					
				}

				@Override
				public void onDestroyActionMode(ActionMode mode) {
					// TODO Auto-generated method stub
					idsToSend.clear();
					names.clear();
					adapter.clearSelection();
					
				}

				
			});
			
			
			
			System.out.println("Long :"+longclick);
			
			list.setOnItemClickListener(new OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	            	selectedItem = files.get(position);
	            	folderId = selectedItem.getId();
	            	parentId = selectedItem.getParentId();
	            	contentType = selectedItem.getContentType();
	            	if(selectedItem.getName().equals("items shared with you")) MainActivity.onItemsShared = true;
	            	if(contentType.equals("application/x-directory")){
		            	if(getChildren(folderId) != 0){
			                //if si el id coincide con algun parent, guay, si no, peticion
		            		System.out.println("Entro en el of, hay hijos");
			            	MainActivity.parentId.add(parentId);
			            	//MainActivity.names.add(selectedItem.getName());
			                listener.onRssItemSelected(selectedItem);
		            	}else{
		            		System.out.println("Entré en el else");
		            		System.out.println("url: "+folderUrl+" id: "+folderId);
		            		URLReader filesInfo = new URLReader();
		            		folderUrl = folderUrl.replace("{{id}}", folderId);
		                	MainActivity.selectedId = "null";
		                	//MainActivity.selectedName = "null";
		                	filesInfo.execute(folderUrl);
		            	}
	            	}else if(contentType.equals("video/x-matroska") || contentType.equals("video/x-msvideo") 
	            			|| contentType.equals("video/mpeg") || contentType.equals("video/mp4")
	            			|| contentType.equals("audio/mpeg") || contentType.equals("image/jpeg")
	            			|| contentType.equals("text/plain") || contentType.equals("text/x-c")){
	            		MainActivity.parentId.add(parentId);
	            		//MainActivity.names.add(selectedItem.getName());
		                listener.onRssItemSelected(selectedItem);
	            	}
	            }
	        });
			
			
			
			
			// Setup refresh listener which triggers new data loading
	        swipeContainer.setOnRefreshListener(new OnRefreshListener() {
	            @Override
	            public void onRefresh() {
	            	System.out.println("onItemsShared: "+MainActivity.onItemsShared);
	            	if(!MainActivity.onItemsShared){
		            	refresh = true;
		            	if(aMode!=null)
		            		aMode.finish();
		            	URLReader filesInfo = new URLReader();
	            		filesInfo.execute(filesUrl);
	            	}else{
	            		swipeContainer.setRefreshing(false);
	            		Toast.makeText(getActivity(), "Can't refresh in 'items shared with you'", Toast.LENGTH_LONG).show();
	            	}
	            } 
	        });
	        
	        swipeContainer.setColorScheme(android.R.color.holo_green_dark, 
	                android.R.color.holo_green_light, android.R.color.holo_green_dark,
	                android.R.color.holo_green_light);
			
	        
	        
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public int getChildren(String id){
    	int i = 0;
    	int j = 0;
    	System.out.println("Size: "+files.size()+" id: "+id);
    	while(i < info.length()){    		
    		try {
				if(id.equals(info.getJSONObject(i).getString("parent_id"))){
					j++;
				}
			
			i++;
    		} catch (JSONException e) {
				e.printStackTrace();
			}
    	}
    	return j;
    }
    
    public void showDeleteDialog(){
    	AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
	      
        // Nombre de la alerta
        alertDialog.setTitle("Delete");
  
        // Mensaje de la alerta
        alertDialog.setMessage("Are you sure you want to delete this file?");
        alertDialog.setIcon(R.drawable.ic_action_discard_b);     
        
  
        // Si presionamos Ok se cierra la alerta
        alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            //Si aceptamos
            	delete = true;
            	postFiles(idsToSend, null);
            	adapter.clearSelection();
                aMode.finish();
            	dialog.cancel();
            }
        });
        
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            //Si cancelamos
            	System.out.println("Ids: "+idsToSend);
            	adapter.clearSelection();
                aMode.finish();
            	dialog.cancel();
            }
        });
  
        // Mostrar la alerta
        alertDialog.show();
    }
    
    public void showMoveDialog(){
    	System.out.println("He pasado por move");
    	popupFiles = Functions.createArray("0", getActivity());
    	popupFilesArray.add(popupFiles);
    	
    	if(popupFiles.size() ==0){
    		/*searchText.setText("No results");
    		searchText.setVisibility(View.VISIBLE);
    		pb.setVisibility(View.GONE);*/
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
    
    public void showRenameDialog(){
    	AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
	    //AlertDialog dialog = alertDialog.create();  
        // Nombre de la alerta
        alertDialog.setTitle("Rename");
  
        // Mensaje de la alerta
        alertDialog.setIcon(R.drawable.ic_action_discard_b);
        
        final EditText input = new EditText(getActivity());  
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                              LinearLayout.LayoutParams.MATCH_PARENT,
                              LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setText(names.get(0));
        input.setSelection(input.getText().length());
        alertDialog.setView(input); // uncomment this line
       
        
  
        // Si presionamos Ok se cierra la alerta
        alertDialog.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            //Si aceptamos
            	rename = true;
            	String newName = input.getText().toString();
            	postFiles(idsToSend, newName);
            	System.out.println("New name:"+ newName);
            	adapter.clearSelection();
                aMode.finish();
            	dialog.cancel();
            }
        });
        
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            //Si cancelamos
            	adapter.clearSelection();
                aMode.finish();
            	dialog.cancel();
            }
        });
  
        // Mostrar la alerta
        alertDialog.show();
    	
    	
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
 						
 			/*GetMagnet magnet = new GetMagnet();
 			askMagnet = askMagnet.replace("{{url}}", torrentUrl);
 			
 			magnet.execute(askMagnet);*/
 			move = true;
 			postFiles(idsToSend, null);
 			adapter.clearSelection();
            aMode.finish();
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
    
    public void postFiles(ArrayList<String> ids, String newName){
    	    	
    	for (String i : ids) {
			finalIds = finalIds+i+",";
		}
    	finalIds = finalIds.substring(0,finalIds.length()-1);
    	System.out.println("Final ids: "+finalIds);

    	List<NameValuePair> pairs = new ArrayList<NameValuePair>();
    	if(delete || move){
		    pairs.add(new BasicNameValuePair("file_ids", finalIds));
		    if(move){
		    	pairs.add(new BasicNameValuePair("parent_id", selectedId));
		    }
    	}
    	if(rename){
    		System.out.println("Selected: "+newName);
    		pairs.add(new BasicNameValuePair("file_id", finalIds));
    		pairs.add(new BasicNameValuePair("name", newName));
    	}
		pairs.add(new BasicNameValuePair("oauth_token", Init.pref.getString("token", null)));	    
	    
    	
    	PostFiles pf = new PostFiles();
    	if(delete){
    		pf.execute(deleteUrl, pairs);
    	}else if(move){
    		pf.execute(moveUrl, pairs);
    	}else if(rename){
    		pf.execute(renameUrl, pairs);
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
			if(delete){
				Toast.makeText(getActivity(), "Files deleted!", Toast.LENGTH_LONG).show();
			}else if(move){
				Toast.makeText(getActivity(), "Files moved!", Toast.LENGTH_LONG).show();
				pbPopup.setVisibility(View.GONE);
				popup.dismiss();
			}
			delete = false;
			move = false;
			rename = false;
	        refresh = true;
	        URLReader filesInfo = new URLReader();
    		filesInfo.execute(filesUrl);
	
		
		}

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
    	/*try {
			System.out.println(result.getString("files"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        if (result != null){
        	System.out.println("Hay resultado");
	        	System.out.println("Tengo info");
	        	JSONArray temp = new JSONArray();
	        	JSONArray fin = new JSONArray();
				try {
					temp = new JSONArray(result.getString("files"));
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        	
				if(!refresh){
		          //Añadir al array de mainactivity y decirselo a la activity
		          for (int i=0; i<info.length(); i++){
		        	  try {
						fin.put(info.getJSONObject(i));
					} catch (JSONException e) {
						e.printStackTrace();
					}
		          }
		          
		          
		          for (int i=0; i<temp.length(); i++){
		        	  try {
						fin.put(temp.getJSONObject(i));
					} catch (JSONException e) {
						e.printStackTrace();
					}
		          }
		          MainActivity.filesInfo = fin;
		          MainActivity.parentId.add(parentId);
		          //MainActivity.names.add(selectedItem.getName());
		          listener.onRssItemSelected(selectedItem);
				}else{
					try {
						MainActivity.filesInfo = new JSONArray(result.getString("files"));
						FileElmnt element = new FileElmnt();
						element.setId(files.get(0).getParentId());
						element.setContentType("application/x-directory");
						swipeContainer.setRefreshing(false);
				        listener.onRssItemSelected(element);
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
		          
		          
		          
		          try {
		  			System.out.println(MainActivity.filesInfo.getJSONObject(MainActivity.filesInfo.length()-1));
		  		} catch (JSONException e) {
		  			// TODO Auto-generated catch block
		  			e.printStackTrace();
		  		}
		          
        }else{
        	System.out.println("Vuelvo sin info");
        }
        
        System.out.println("P'alla voy");
        
       }
            

    }
        

    @Override
    public void onDetach() {
      super.onDetach();
      listener = null;
    }
    
    
}