package vn.isolar.idict.managedatabase;

import java.io.File;

import android.os.Environment;

/**
 * Kiem tra xem may' da cai dat du lieu chua?
 * @author XUANTUNG
 *
 */
public class ManageDB
{
	//duong dan toi folder chua co so du lieu
	public static final String pathDBs=Environment.getExternalStorageDirectory()+"/idict/data";
	public static final String pathDB=Environment.getExternalStorageDirectory()+"/idict";
	
	//duong dan toi co so du lieu duoc chon de tra tu
	private static String pathBDSelected;//=Environment.getExternalStorageDirectory()+"/Andict/db/anh_viet/anhviet";
	
	//duong dan toi Gesture
	public static String pathGesture=Environment.getExternalStorageDirectory()+"/idict/gestures";
	
	
	/**
	 * Kiem tra xem trong folder theo duong dan co file hay khong
	 * @return true neu co file else thi khong co' file
	 */
	public boolean checkGesture()
	{
	    File file= new File(pathDB);
	    File listFile[]=file.listFiles();

	    for(File e:listFile)
	    {
		if(e.getName().equals("gestures"))
		    return true;
	    }
	    return false;
	}
	public boolean checkEmpty()
	{
		File file=new File(pathDBs);
		if(!file.exists())
		    return true;
		File[] listFile=file.listFiles();
		if(listFile.length>0) return false;
		else return true;
	}

	public String[] getNameDB()
	{
		File file=new File(pathDBs);
		
		File[] listFile=file.listFiles();
		
		int length=listFile.length;
		String[] s=new String[length];
		for(int i=0;i<length;i++)
		{
			s[i]=listFile[i].getName();
		}
		return s;
	}
	
	public static void setDBSelected(String fileName)
	{
		pathBDSelected=pathDBs+"/"+fileName;
	}
	
	public static String getDBSelected()
	{
		return pathBDSelected;				
	}
	
}
