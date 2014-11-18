package pe.ra.putio;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import pe.ra.utilities.FileElmnt;
import pe.ra.utilities.NavDrawerItem;
import pe.ra.utilities.NavDrawerListAdapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity implements FilesFragment.OnItemSelectedListener{
	
	public static String filesUrl = "https://put.io/v2/files/list?oauth_token={{token}}&parent_id=-1";
	public static String folderUrl = "https://put.io/v2/files/list/{{id}}?oauth_token={{token}}";
	public static String accountInfo = "https://put.io/v2/account/info?oauth_token={{token}}";
	public static String transUrl = "https://put.io/v2/transfers/list?oauth_token={{token}}";
	public static String itemUrl = "https://api.put.io/v2/files/{{id}}/stream?oauth_token={{token}}";
	public static String itemUrlToMp4 = "https://api.put.io/v2/files/{{id}}/mp4/stream?oauth_token={{token}}";
	public static String dwnldUrl = "https://api.put.io/v2/files/{{id}}/download?oauth_token={{token}}";
	public static String convertUrl = "https://api.put.io/v2/files/{{id}}/mp4?oauth_token={{token}}";
	public static String deleteUrl = "https://api.put.io/v2/files/delete?file_ids={{id}}&oauth_token={{token}}";
	
	public static String selectedId = "null";
	//public static String selectedName = "null";
	public static JSONArray filesInfo;
	public static ArrayList<String> parentId;
	//public static ArrayList<String> names;
	public static FileElmnt selectedItem;
	
	private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
 
    // nav drawer title
    private CharSequence mDrawerTitle;
 
    // used to store app title
    private CharSequence mTitle;
 
    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
 
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    
    static Boolean onPopupWndw;
    static Boolean onItemsShared;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTitle = mDrawerTitle = getTitle();
        
        System.out.println("Create");
        
        filesUrl = filesUrl.replace("{{token}}", Init.pref.getString("token", null));
        folderUrl = folderUrl.replace("{{token}}", Init.pref.getString("token", null));
        accountInfo = accountInfo.replace("{{token}}", Init.pref.getString("token", null));
        transUrl = transUrl.replace("{{token}}", Init.pref.getString("token", null));
        itemUrl = itemUrl.replace("{{token}}", Init.pref.getString("token", null));
        itemUrlToMp4 = itemUrlToMp4.replace("{{token}}", Init.pref.getString("token", null));
        dwnldUrl = dwnldUrl.replace("{{token}}", Init.pref.getString("token", null));
        convertUrl = convertUrl.replace("{{token}}", Init.pref.getString("token", null));
        deleteUrl = deleteUrl.replace("{{token}}", Init.pref.getString("token", null));
        
        parentId = new ArrayList<String>();
        //names = new ArrayList<String>();
        onPopupWndw = false;
        
        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
 
        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);
 
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
 
        navDrawerItems = new ArrayList<NavDrawerItem>();
        
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
        
        // adding nav drawer items to array
        // Home
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        // Find People
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        // Photos
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        // Communities, Will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)/*, true, "22"*/));
        // Pages
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        // What's hot, We  will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
         
 
        // Recycle the typed array
        navMenuIcons.recycle();
 
        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);
 
        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
 
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ){
            public void onDrawerClosed(View view) {
            	if(!selectedId.equals("0")){
            		for(int i=0; i<filesInfo.length(); i++){
            			try {
        					if(filesInfo.getJSONObject(i).getString("id").equals(selectedId)){
        						getActionBar().setTitle(filesInfo.getJSONObject(i).getString("name"));
        						break;
        					}
        				} catch (JSONException e) {
        					e.printStackTrace();
        				}
            		}
        		}else{
        			getActionBar().setTitle("My Files");
        		}
                //getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }
 
            public void onDrawerOpened(View drawerView) {
            	getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
 
        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }
    }
    
    /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
            // display view for selected nav drawer item
        	selectedId = "null";
        	//selectedName = "null";
        	parentId.clear();
        	//names.clear();
            displayView(position);
        }
    }
 
     /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
        case 0:
            fragment = new HomeFragment();
            break;
        case 1:
            fragment = new FilesFragment();
            onItemsShared = false;
            break;
        case 2:
            fragment = new SearchFragment();
            break;
        case 3:
            fragment = new TransfersFragment();
            break;
        case 4:
            fragment = new SettingsFragment();
            break;
        case 5:
            logOut();
            break;
 
        default:
            break;
        }
 
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();
 
            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
 
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        /*switch (item.getItemId()) {
        case R.id.action_settings:
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }*/
        return super.onOptionsItemSelected(item);
    }
 
    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        //boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
 
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }
 
    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
 
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
 
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    
    
    @Override
    public void onRssItemSelected(FileElmnt item) {
    	selectedItem = item;
    	selectedId = item.getId();
    	//selectedName = item.getName();
    	Fragment fragment;
    	if(item.getContentType().equals("application/x-directory")){
    		fragment = new FilesFragment();
    		initFragment(fragment);
    	}else if(item.getContentType().equals("video/x-matroska") || item.getContentType().equals("video/x-msvideo") 
    			|| item.getContentType().equals("video/mpeg") || item.getContentType().equals("video/mp4")
    			|| item.getContentType().equals("audio/mpeg") || item.getContentType().equals("image/jpeg")
    			|| item.getContentType().equals("text/plain") || item.getContentType().equals("text/x-c")){
    		fragment = new VideoFragment();
    		initFragment(fragment);
    	}/*else if(item.getContentType().equals("text/plain") || item.getContentType().equals("text/x-c")){
    		String itemUrl = "https://api.put.io/v2/files/"+item.getId()+"/stream?oauth_token=07ccd52091e911e1933d00248126f0ef";
    		Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
	        Uri data = Uri.parse(itemUrl);
	        intent.setDataAndType(data, "text/*");
	        startActivity(intent);
    	}*/
    	
        
        }
    
    public void initFragment(Fragment fragment){
    	
    	FragmentManager fragmentManager = getFragmentManager();
    	FragmentTransaction ft =fragmentManager.beginTransaction();    	
        ft.replace(R.id.frame_container, fragment);       
        ft.commit();
    }
    
    public void logOut(){
    	Init.editor.remove("token");
    	Init.editor.remove("loggedIn");
    	Init.editor.commit();
    	Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_LONG).show();
    	finish();
    	Intent i = new Intent();
        i.setClass(this, Init.class);
        startActivity(i);
        
        
    }
    
    @Override
    public void onBackPressed(){
    	if(parentId.size() != 0){
    		selectedId = parentId.remove(parentId.size()-1);
    		//selectedName = names.remove(names.size()-1);
    		//System.out.println("Name: "+selectedName);
    		if(selectedId.equals("0")){
    			onItemsShared = false;
    		}
    		System.out.println("onItemsShared: "+onItemsShared);
    		Fragment fragment = new FilesFragment();    		  
        	FragmentManager fragmentManager = getFragmentManager();
        	FragmentTransaction ft =fragmentManager.beginTransaction();    	
            ft.replace(R.id.frame_container, fragment);       
            ft.commit();
    	}else{
    		/*Init.onBack = true;
    		finish();*/
    		
    	}

    }


}
