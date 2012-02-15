package vn.isolar.idict.activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter 
{


	private final String DATABASE_NAME="search";
	private final String DATABASE_TABLE="data";
	private Context context;
	private SQLiteDatabase db;
	SQLHelper sql;
	
	public DBAdapter(Context context) 
	{
		this.context=context;
		sql=new SQLHelper(context);
	}
	
	public void open()
	{
		db=sql.getWritableDatabase();
	}
	public void close()
	{
		sql.close();
	}
	
	public long insert(String word,String mean)
	{
		ContentValues content=new ContentValues();
		content.put("word", word);
		content.put("mean", mean);
		return db.insert(DATABASE_TABLE, null, content);		
	}
	
	/**
	 * Tra lai nghia cua tu
	 * @param word tu can tra
	 * @return nghia cua tu
	 */
	public Cursor query(String word)
	{
		Cursor c;
		c=db.query(DATABASE_TABLE, new String[]{"mean"}, "word='"+word+"'", null, null, null, null);
		return c;
	}
	
	private class SQLHelper extends SQLiteOpenHelper
	{

		public SQLHelper(Context context)
		{
			super(context, DATABASE_NAME, null, 1);
		}

		@Override
		public void onCreate(SQLiteDatabase db) 
		{
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
		{
			
		}
		
	}
	


}
