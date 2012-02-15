package vn.isolar.idict.activity;

import java.io.File;

import vn.isolar.idict.managedatabase.ManageDB;
import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class FavouriteActivity extends ListActivity
{
    private File fileFavourite;
    private File directory;
    SharedPreferences storeShare;
    public StoreFile store;
   
    private String pathDB = ManageDB.pathDB;
    AdapterLayoutListFavourite adapter;
    private final String key_language = idictActivity.key_language;
    
    TextView tv;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.layout_history);
	
	tv=(TextView)findViewById(R.id.titleHistory);
	tv.setText(R.string.txtFavourite);
	// kiem tra folder history
	directory = new File(pathDB + "/favourite");
	if (!directory.exists())
	{
	    directory.mkdir();
	}
	storeShare = PreferenceManager.getDefaultSharedPreferences(this);
	fileFavourite = new File(directory, storeShare.getString(key_language, "anhviet") + ".fav");
	store = new StoreFile(this, fileFavourite, false);
	// end kiem tra /
	store.getAllContext();
	adapter = new AdapterLayoutListFavourite(this, store.getListNewWord(), store.getListID());
	adapter.setEnableSelectText(true);
	setListAdapter(adapter);
	
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
	MenuInflater inflater = new MenuInflater(getApplicationContext());
	inflater.inflate(R.menu.menu_fav, menu);
	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
	
	switch (item.getItemId())
	{
	    case R.id.menuDeleteAll:
		
		adapter.clear();
		adapter.notifyDataSetChanged();
		store.clear();
		
		break;
	    
	    case R.id.menuDeleteSelect:
		if (item.getTitle().equals("Delete Select"))
		{
		    adapter.setEnableSelectText(false);
		    item.setTitle("Cancel");
		}
		else
		{
		    
		    adapter.setEnableSelectText(true);
		    item.setTitle("Delete Select");
		}
		
		break;
	}
	return super.onOptionsItemSelected(item);
    }
}
