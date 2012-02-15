package vn.isolar.idict.managedatabase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class QueryDB 
{
	//duong dan toi co so du lieu
	private String path;
	private SQLiteDatabase sql;
	
	private String tableDB="data"; 
	private final String word="word";
	private final String mean="mean";
	private final String id="id";

	
	
	
	public QueryDB(String path,String nameTable) 
	{
		this.path=path;
		this.tableDB=nameTable;
	}
	public QueryDB(String path) 
	{
		this.path=path;
	}
	/**
	 *open connect ko cho phep edit du lieu
	 */
	public void open()
	{
		this.sql=SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
	}
	/**
	 * open connect cho phep edit du lieu
	 * @param nnableEdit
	 */
	public void openEnableEdit()
	{
		
		this.sql= SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
	}
	public void close()
	{
		this.sql.close();
	}
	
	/**
	 * Truy van khi khong co tu nao truyen vao
	 * @return tra ve danh sach cac tu dau tien
	 */
	public Cursor emptyQuery()
	{
		Cursor c1;
		String query="SELECT "+this.id+" ,"+this.word+" FROM "+this.tableDB+" LIMIT 0,30";
		c1=this.sql.rawQuery(query, null);
		return c1;
	}
	/**
	 * get tat ca du lieu trong co so du lieu
	 * @param check
	 * @return
	 */
	public Cursor getAlldata()
	{	//this.open();
		Cursor c1;
		String query="SELECT "+this.id+" ,"+this.word+" FROM "+this.tableDB;
		c1=this.sql.rawQuery(query, null);
		return c1;
		
	}
	
	/**
	 * Truy van khi co tu truyen vao
	 * @param word tu truyen vao
	 * @return danh sach cac tu gan giong va id cua cac tu
	 */
	public Cursor wordQuery(String word)
	{
		
		Cursor c2;
		c2=this.sql.query(tableDB, new String[]{this.id,this.word}, this.word+">='"+word+"' LIMIT 0,30", null, null, null, null);
		Log.d("load","db");
		if(c2!=null&&c2.getCount()>0)
		{
			return c2;
		}
		return null;

	}
	
	/**
	 * Truy van khi truyen id vao
	 * @param id 
	 * @return tra ve nghia cua tu co id truyen vao
	 */
	public Cursor checkExist(Integer id) 
	{
		//Cursor c= this.sql.rawQuery("");
		return null;
	}
	public Cursor idQuery(String id)
	{
		Cursor c3;
		
		c3=this.sql.query(tableDB, new String[]{this.mean}, this.id+"="+id, null, null, null, null);
		if(c3!=null&&c3.getCount()>0)
		{
			return c3;
		}
		return null;

	}
	/**
	 * them moi vao co so du lieu
	 * @param id
	 * @param word
	 * @param mean
	 * @param statur
	 * @return
	 */
	public long  insertQuery(int id,String word,int checkout)
	{		
			Cursor c= this.sql.rawQuery("SELECT id FROM " + this.tableDB, null);	
			c.moveToFirst();
			if(c.getCount()>=3)
			{
				
				sql.execSQL("DELETE FROM "+this.tableDB+" WHERE id in(SELECT id FROM "+this.tableDB + " limit 1 )");
				Log.d("ins", "checked");
			}
			ContentValues value= new ContentValues();
			value.put(this.id, id);
			value.put(this.word, word);
			
			Long test= this.sql.insert(tableDB, null, value);
			
			return test;
					
	}
	public long  insertQuery(int id,String word)
	{		this.openEnableEdit();			
			ContentValues value= new ContentValues();
			value.put(this.id, id);
			value.put(this.word, word);
			
			Long test= this.sql.insert(tableDB, null, value);
			this.close();
			return test;
					
	}
	
	/**
	 * delete mot row trong co so du lieu
	 * @param id
	 * @return
	 */
	public boolean deleteQuery(int id)
	{
		try
		{	this.openEnableEdit();
			this.sql.execSQL("DELETE FROM "+this.tableDB+" WHERE id="+id);
			this.close();
			return true;
		}catch (SQLException e) {
			Log.e("check", e.getMessage());
			this.close();
			return false;
		}
	}
	
	/**
	 * xoa toan bo du lieu trong data
	 * @return
	 */	
	
	public boolean deleteAllQuery()
	{
		try
		{ 	
			this.open();
			this.sql.delete(tableDB, null, null);
			this.close();
			return true;
			
		}
		catch (Exception e) {
			return false;
		}
		
	}	
}
