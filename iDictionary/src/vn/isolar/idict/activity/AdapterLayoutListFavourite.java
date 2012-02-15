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

public class AdapterLayoutListFavourite extends BaseAdapter{

	ArrayList<Newwords>listWord=new ArrayList<Newwords>();
	ArrayList<Integer>listId=new ArrayList<Integer>();		
	LayoutInflater inflater;
	Context context;	
	SharedPreferences storeShare;
	File fileHistory;
	private boolean checkdelete=true;
	int maxHistory=20;
	private final String key_language=idictActivity.key_language;
	StoreFile storeFile;
	
	StoreFile storeFileFav;
	private String pathDB=ManageDB.pathDB;

	
	public AdapterLayoutListFavourite(Context context,ArrayList<Newwords>listWord,ArrayList<Integer>listId) 
	{	
		this.context=context;
		this.listId=listId;
		this.listWord=listWord;		
		this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);				
		storeShare= PreferenceManager.getDefaultSharedPreferences(context);						
		File directory= new File(pathDB+"/history");// kiem tra folder history
		if(!directory.exists())
		{
			directory.mkdir();
		}
		fileHistory= new File(directory,storeShare.getString(key_language, "null")+".his");				
		storeFile= new StoreFile(context, fileHistory,true);
		
		//luu tru Store file fav
		directory= new File(pathDB+"/favourite");
		fileHistory= new File(directory, storeShare.getString(key_language,
				"null") + ".fav");
		storeFileFav = new StoreFile(context, fileHistory, false);
		
	}
	
	public void setEnableSelectText (boolean b) {
		this.checkdelete=b;
	}
	public ArrayList<Integer> getListId() {
		return listId;
	}
	public ArrayList<Newwords> getListWord() {
		return listWord;
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
		return arg0;
	}
	public void deleteSelected() {
		// TODO Auto-generated method stub

	}	
	public void clear() 
	{
		listId.clear();
		listWord.clear();
	}
	@Override
	public View getView(final int position, View viewConvert, ViewGroup arg2) 
	{
		
	
		if(viewConvert==null)
		{
			viewConvert=inflater.inflate(R.layout.customrow_favourite, null);					
		}
		TextView txtWord=(TextView)viewConvert.findViewById(R.id.txtWord);
				
		txtWord.setText(listWord.get(position).word);								
		txtWord.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				String mean="";
				QueryDB qd;
				qd=new QueryDB(ManageDB.getDBSelected());
				qd.open();
				Cursor c=qd.idQuery(listId.get(position).toString());
				
				if(c!=null && c.getCount()>0)
				{	
					c.moveToFirst();
					mean=c.getString(0);	
					//listWord.get(position).mean=html.convertMeanToHTML(mean);
				}
				qd.close();
				
				//webMean.loadDataWithBaseURL("", , "text/html", "UTF-8", null);
				
				if(checkdelete==true)
				{
					
					// dua tu vao file history.
					Log.d("Checl","true");
					Intent i= new Intent(context,ViewContextActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Bundle bundle= new Bundle();
					
					
					bundle.putString("mean", mean);
					bundle.putString("word", listWord.get(position).word);
					bundle.putInt("id", listId.get(position));
					
					i.putExtras(bundle);
					
					
					context.startActivity(i);
										
					long l= System.currentTimeMillis();
					Date datecurent= new Date(l);
					String strdate= new SimpleDateFormat("dd-MM-yyyy").format(datecurent);
					
					storeFile.appendText(listId.get(position)+"",listWord.get(position).word,strdate);
				}
				else
				{									

					storeFileFav.removeById(listId.get(position).toString());
					listWord.remove(position);
					listId.remove(position);
					notifyDataSetChanged();
					
				}
				
			}				
			
		});		
		return viewConvert;
	}

	
	
}
