package vn.isolar.idict.activity;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import vn.isolar.idict.managedatabase.ManageDB;
import vn.isolar.idict.managedatabase.QueryDB;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdapterLayoutListView extends BaseAdapter
{
    ArrayList<Newwords> listWord = new ArrayList<Newwords>();
    ArrayList<Integer> listId = new ArrayList<Integer>();
    ArrayList<String> dateTime = new ArrayList<String>();
    
    LayoutInflater inflater;
    Context context;
    HTML html;// Chuyen doi? nghia sang dinh dang HTML
    // xu dung de doc tu
    
    SharedPreferences storeShare;
    File fileHistory;
    
    int maxHistory = 20;
    
    Boolean kiemtra = false;
    private final String key_language = idictActivity.key_language;
    
    public void setKiemtra(Boolean kiemtra)
    {
	this.kiemtra = kiemtra;
    }
    
    StoreFile storeFile;
    
    private String pathDB = ManageDB.pathDB;
    File directory ;
    public AdapterLayoutListView(Context context, ArrayList<Newwords> listWord, ArrayList<Integer> listId)
    {
	this.context = context;
	this.listId = listId;
	this.listWord = listWord;
	this.html = new HTML(context);
	this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	
	// Khoi tao .this o day la OninitLisner
	
	// voi moi tu dien se co mot file log rieng. duoc quyet dinh bang loai
	// tu dien do
	
	storeShare = PreferenceManager.getDefaultSharedPreferences(context);
	
	kiemtra = true;// neu kt ==true thi la activiti main else la khong
	
	directory= new File(pathDB + "/history");// kiem tra folder
	                                               // history
	if (!directory.exists())
	{
	    directory.mkdir();
	}	
	fileHistory = new File(directory, storeShare.getString(key_language, "null") + ".his");
	 Log.d("DB1", storeShare.getString(key_language, "khong co"));
	
	storeFile = new StoreFile(context, fileHistory, true);
    }
    
    public void setStoreFile(String strname)
    {
	fileHistory = new File(directory,strname + ".his");
	 Log.d("DB2xxxx", strname);
	storeFile = new StoreFile(context, fileHistory, true);
    }
    
    public AdapterLayoutListView(Context context, ArrayList<Newwords> listWord, ArrayList<Integer> listId, ArrayList<String> dateTime)
    {
	this.context = context;
	this.listId = listId;
	this.listWord = listWord;
	this.dateTime = dateTime;
	this.html = new HTML(context);
	this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	
	// Khoi tao .this o day la OninitLisner
	
	// voi moi tu dien se co mot file log rieng. duoc quyet dinh bang loai
	// tu dien do
	
	storeShare = PreferenceManager.getDefaultSharedPreferences(context);
	
	kiemtra = false;// neu kt ==true thi la activiti main else la khong
	
	File directory = new File(pathDB + "/history");// kiem tra folder
	                                               // history
	if (!directory.exists())
	{
	    directory.mkdir();
	}
	// Log.d("DB1", ManageDB.getDBSelected() + "db");
	fileHistory = new File(storeShare.getString(key_language, "null") + ".his");
	// Log.d("DB1", storeShare.getString(key_language, "anhviet"));
	
	storeFile = new StoreFile(context, fileHistory, true);
    }
    
    @Override
    public int getCount()
    {
	return listId.size();
    }
    
    @Override
    public Object getItem(int arg0)
    {
	return null;
    }
    
    @Override
    public long getItemId(int arg0)
    {
	return 0;
    }
    
    public void clear()
    {
	listId = new ArrayList<Integer>();
	listWord = new ArrayList<Newwords>();
    }
    
    @Override
    public View getView(final int position, View viewConvert, ViewGroup arg2)
    {
	
	if (viewConvert == null)
	{
	    viewConvert = inflater.inflate(R.layout.customrow_listview, null);
	}
	if (kiemtra == false)
	{
	    TextView textDate = (TextView) viewConvert.findViewById(R.id.txtDate);
	    textDate.setVisibility(View.VISIBLE);
	    textDate.setText(dateTime.get(position));
	    
	}
	
	TextView txtWord = (TextView) viewConvert.findViewById(R.id.txtWord);
	txtWord.setText(listWord.get(position).word);
	
	txtWord.setOnClickListener(new OnClickListener()
	{
	    
	    @Override
	    public void onClick(View v)
	    {
		
		/*
	         * Uri
	         * uri=Uri.withAppendedPath(EditTextChangeProvider.CONTENT_URI_ID
	         * , listId.get(position).toString()); Cursor
	         * c=context.getContentResolver().query(uri, null, null, null,
	         * null);
	         */
		String mean = "";
		QueryDB qd;
		qd = new QueryDB(ManageDB.getDBSelected());
		qd.open();
		Cursor c = qd.idQuery(listId.get(position).toString());
		
		if (c != null && c.getCount() > 0)
		{
		    c.moveToFirst();
		    mean = c.getString(0);
		    // listWord.get(position).mean=html.convertMeanToHTML(mean);
		}
		qd.close();
		
		// webMean.loadDataWithBaseURL("", , "text/html", "UTF-8",
		// null);
		
		// dua tu vao file history.
		
		Intent i = new Intent(context, ViewContextActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Bundle bundle = new Bundle();
		
		bundle.putString("mean", mean);
		bundle.putString("word", listWord.get(position).word);
		bundle.putInt("id", listId.get(position));
		i.putExtras(bundle);
		
		// luu vao dataBase
		/*
	         * qd= new QueryDB(ManageDB.getDBSelected(),"history");
	         * qd.openEnableEdit(); if(qd.insertQuery(listId.get(position),
	         * listWord.get(position).word,20)!=-1) { Log.d("thuc hien",
	         * "da insert"); } else Log.d("thuc hien", "not insert");
	         * qd.close(); <<<<<<< .mine
	         */
		if (kiemtra == true)
		{
		    long l = System.currentTimeMillis();
		    Date datecurent = new Date(l);
		    String strdate = new SimpleDateFormat("dd-MM-yyyy").format(datecurent);
		    storeFile.appendText(listId.get(position) + "", listWord.get(position).word, strdate);
		    Log.d("file history name", fileHistory.getPath());
		}
		context.startActivity(i);
		
		// luu vao file
		
	    }
	    
	});
	return viewConvert;
    }
    
}
