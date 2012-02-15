package vn.isolar.idict.managegesture;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import vn.isolar.idict.activity.R;
import vn.isolar.idict.managedatabase.ManageDB;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ListGestures extends Activity
{
	public final static String pathFileGesture=ManageDB.pathDB+"/gestures";
	
	Resources rs;
	
	private final static File fileStore=new File(pathFileGesture);
	private final int REQUEST_NEW_GESTURE=1;
	
	private final int STATUS_CANCELLED=1;
	private final int STATUS_NO_STORAGE=2;
	private final int STATUS_SUCCESS=3;
	private final int STATUS_NOT_LOAED=4;
	
	private GestureAdapter gesAdapter;
	private static GestureLibrary gesLibrary; //dung chung cho ca load gesture va them gesture(ben CreateNewGesture)
	private GesturesLoadTask gestureLoad;
	
	private final int ID_RENAME=0;
	private final int ID_REMOVE=1;
	
	private final int CREATE_RENAME_DIALOG=1;
	private final int CREATE_REMOVE_DIALOG=2;
	
	ListView listView;
	EditText edtName;//O edt de nhap ten thay doi cua gesture
	private NameGesture mCurrentRenameGesture; //gesture dang xet de doi ten
	private NameGesture mCurrentRemoveGesture;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_gesture);
		
		listView=(ListView)findViewById(R.id.list_gestures);
		
		gesAdapter=new GestureAdapter(this);
		listView.setAdapter(gesAdapter);
		rs= getResources();
		
		if(gesLibrary==null) gesLibrary=GestureLibraries.fromFile(fileStore);
		loadGestures();
		//dang ky su dung menu context
		registerForContextMenu(listView);
	}
	
	public static GestureLibrary getGesLibrary()
	{
		return gesLibrary; 
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, v, menuInfo);
		
		AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo)menuInfo;
		
		menu.setHeaderTitle(((TextView)info.targetView).getText().toString());
		menu.add(0, ID_RENAME, 0,R.string.gesRename);
		menu.add(0, ID_REMOVE, 0,R.string.gesDelete);		
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) 
	{
		AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		final NameGesture gesture=(NameGesture)info.targetView.getTag();
		mCurrentRemoveGesture=gesture;
		
		switch (item.getItemId()) 
		{
		case ID_RENAME:
			renameGesture(gesture);
			break;
		
		case ID_REMOVE:
			showDialog(CREATE_REMOVE_DIALOG);
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		MenuInflater inflater=new MenuInflater(getApplicationContext());
		inflater.inflate(R.menu.menu_option_gesture, menu);
		return super.onCreateOptionsMenu(menu);
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		
		
		switch (item.getItemId()) 
		{
			case R.id.menuNewGesture:
				startActivityForResult(new Intent("com.khmt2k3.xuantung.managegesture.CreateNewGesture"),REQUEST_NEW_GESTURE);
				break;
	
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) 
	{
		if(id==CREATE_RENAME_DIALOG) return createReNameDialog();
		else if(id==CREATE_REMOVE_DIALOG)
		{
			return createRemoveDialog();
		}
		
		return super.onCreateDialog(id);
	}
	
	private void renameGesture(NameGesture gesture)
	{
		mCurrentRenameGesture=gesture;
		showDialog(CREATE_RENAME_DIALOG);
	}
	
	@Override
	protected void onPrepareDialog(int id, Dialog dialog, Bundle args) 
	{
		super.onPrepareDialog(id, dialog, args);
		if(id==CREATE_RENAME_DIALOG)
		{	//ban dau khi hine len cho Edittext la ten ban dau
			edtName.setText(mCurrentRenameGesture.name);
		}
	}
	private Dialog createReNameDialog()
	{
		final View layout=View.inflate(this, R.layout.rename_dialog, null);
		edtName=(EditText)layout.findViewById(R.id.edtRename);
		
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle(rs.getString(R.string.titleNameGesture));
		builder.setNegativeButton(rs.getString(R.string.strNo), new OnClickListener() 
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				
			}
		});
		
		builder.setPositiveButton(rs.getString(R.string.strYes), new OnClickListener() 
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				changeName();
			}
		});
		builder.setView(layout);
		return builder.create();
	}
	
	private Dialog createRemoveDialog()
	{

		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setTitle(rs.getString(R.string.titleDeleteGesture));
		builder.setPositiveButton(rs.getString(R.string.strYes), new OnClickListener() 
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				removeGesture(mCurrentRemoveGesture);
			}
		});
		builder.setNegativeButton(rs.getString(R.string.strNo), new OnClickListener() 
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		return builder.create();		
	}
	
	
	
	private void changeName()
	{
		String name=edtName.getText().toString();
		if(!TextUtils.isEmpty(name))
		{
			final NameGesture renameGesture=mCurrentRenameGesture;
			final GestureAdapter gesAdap=gesAdapter;
			final int count=gesAdap.getCount();
			
			for(int i=0;i<count;i++)
			{
				
				final NameGesture gesture=gesAdap.getItem(i);				
				if(gesture.gesture.getID()==renameGesture.gesture.getID())
				{
					gesLibrary.removeGesture(gesture.name, gesture.gesture);
					gesture.name=edtName.getText().toString();
					gesLibrary.addGesture(gesture.name, gesture.gesture);
					break;
				}
			}
			gesAdap.notifyDataSetChanged();
		}
		mCurrentRenameGesture=null;
	}
	
	private void removeGesture(NameGesture gesture)
	{
		gesLibrary.removeGesture(gesture.name, gesture.gesture);
		gesLibrary.save();		
		gesAdapter.setNotifyOnChange(false);
		gesAdapter.notifyDataSetChanged();
		gesAdapter.remove(gesture);
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode==RESULT_OK) 
		{
			switch (requestCode) 
			{
				case REQUEST_NEW_GESTURE:
				loadGestures();
				break;
				default:
				break;
			}
		}
	}
	
	/**
	 * Load List cac gesture da co len
	 */
	private void loadGestures()
	{
		if(gestureLoad!=null)
		{
			gestureLoad.cancel(true);
		}
		gestureLoad=(GesturesLoadTask)new GesturesLoadTask().execute();
	}
	
	private class GesturesLoadTask extends AsyncTask<Void,NameGesture,Integer>
	{
		private int thumbnailsInset;
		private int thumbnailsSize;
		private int pathColor;

		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			
			final Resources resouce=getResources();
			thumbnailsInset=(int)resouce.getDimension(R.dimen.inset);
			thumbnailsSize=(int)resouce.getDimension(R.dimen.size);
			pathColor=(int)resouce.getColor(R.color.color);
			
			gesAdapter.setNotifyOnChange(false);
			gesAdapter.clear();
			
		}
		
		@Override
		protected Integer doInBackground(Void... params) 
		{
			if (isCancelled()) return STATUS_CANCELLED;
	            if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) 
	            {
	                return STATUS_NO_STORAGE;
	            }
            final GestureLibrary store=gesLibrary;
            if(store.load())
            {            	
            	for(String name:store.getGestureEntries())
            	{
            		if(isCancelled())break;
            		for(Gesture ges:store.getGestures(name))
            		{

            			final Bitmap bitmap=ges.toBitmap(thumbnailsSize, thumbnailsSize, thumbnailsInset, pathColor);
            			final NameGesture nameGesture=new NameGesture();
            			nameGesture.gesture=ges;
            			nameGesture.name=name;
            			gesAdapter.addBitmap(nameGesture.gesture.getID(), bitmap);
            			
            			publishProgress(nameGesture);
            		}
            	}
            	
            	return STATUS_SUCCESS;
            }
            return STATUS_NOT_LOAED;
        }
		
		@Override
		protected void onProgressUpdate(NameGesture... values)
		{
			super.onProgressUpdate(values);
			
			final GestureAdapter gestureAdapter=gesAdapter;
			gestureAdapter.setNotifyOnChange(false);
			
			for(NameGesture name:values)
			{
				gestureAdapter.add(name);
			}
			gestureAdapter.notifyDataSetChanged();
		}
		
		@Override
		protected void onPostExecute(Integer result) 
		{
			super.onPostExecute(result);
			
		}
		
	}
	
	
	private class GestureAdapter extends ArrayAdapter<NameGesture>
	{
		private final LayoutInflater inflater;
		private final Map<Long,Drawable> Thumbnails=Collections.synchronizedMap(new HashMap<Long, Drawable>());
	
		public GestureAdapter(Context context)
		{
			super(context, 0);
			inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		public void addBitmap(Long id,Bitmap bitmap)
		{
			Thumbnails.put(id, new BitmapDrawable(bitmap));
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			if(convertView==null)
			{
				convertView=inflater.inflate(R.layout.gesture_item, null);
			}
			
			final NameGesture gesture=getItem(position);
			final TextView lbl=(TextView)convertView;
			
			lbl.setTag(gesture);
			lbl.setText(gesture.name);
			lbl.setCompoundDrawablesWithIntrinsicBounds(Thumbnails.get(gesture.gesture.getID()), null, null, null);
			
			return convertView;
			
		}
	
		
	}
	
	private static class NameGesture
	{
		String name;
		Gesture gesture;
	}

}
