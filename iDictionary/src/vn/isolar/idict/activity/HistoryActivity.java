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
import android.widget.Button;

public class HistoryActivity extends ListActivity
{
    
    private File fileHistory;
    private File directory;
    SharedPreferences storeShare;
    StoreFile store;
    Button buttondelete;
    private String pathDB = ManageDB.pathDB;
    AdapterLayoutListView adapter;
    private final String key_language = idictActivity.key_language;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.layout_history);
	
	// android.R.style.Theme_Dialog
	
	display();
	
    }
    
    public boolean onCreateOptionsMenu(Menu menu)
    {
	MenuInflater inflater = new MenuInflater(getApplicationContext());
	inflater.inflate(R.menu.menu_history, menu);
	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item)
    {
	switch (item.getItemId())
	{
	    case R.id.menu_history_del_all:
		    adapter.clear();
		    adapter.notifyDataSetChanged();
		    store.clear();break;
	}
        return super.onMenuItemSelected(featureId, item);
    }
    public void display()
    {
	directory = new File(pathDB + "/history");// kiem tra folder history
	if (!directory.exists())
	{
	    directory.mkdir();
	    
	}
	storeShare = PreferenceManager.getDefaultSharedPreferences(this);
	fileHistory = new File(directory, storeShare.getString(key_language, "anhviet") + ".his");
	
	store = new StoreFile(this, fileHistory, true);
	store.getAllContext();
	adapter = new AdapterLayoutListView(this, store.getListNewWord(), store.getListID(), store.getListDateTime());
	adapter.setKiemtra(false);
	setListAdapter(adapter);
    }
    
}
