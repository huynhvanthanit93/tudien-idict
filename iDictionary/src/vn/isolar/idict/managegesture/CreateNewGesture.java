package vn.isolar.idict.managegesture;

import vn.isolar.idict.activity.R;
import android.app.Activity;
import android.gesture.Gesture;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



public class CreateNewGesture extends Activity 
{
	private final String keyStateGesture="gesture";//Dung de luu lai va ve lai gesture
	
	EditText edtNameNew;
	GestureOverlayView gestureOverlay;
	Button btnAdd,btnCancel;
	
	Gesture gesture;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addgestures);
		
		edtNameNew=(EditText)findViewById(R.id.btnName);
		gestureOverlay=(GestureOverlayView)findViewById(R.id.gestureOverlay);
		btnAdd=(Button)findViewById(R.id.btnSave);
		btnCancel=(Button)findViewById(R.id.btnCancel);
		btnAdd.setOnClickListener(clickAdd);
		btnAdd.setEnabled(false);
		btnCancel.setOnClickListener(clickCancel);
		
		gestureOverlay.addOnGestureListener(new GestureProcessor());
		
	}
	
	private View.OnClickListener clickCancel=new OnClickListener() 
	{
		
		@Override
		public void onClick(View v) 
		{
			setResult(RESULT_CANCELED);
			finish();
		}
	};
	
	private View.OnClickListener clickAdd=new OnClickListener() 
	{
		
		@Override
		public void onClick(View v) 
		{
			if(gesture!=null)
			{
				if(edtNameNew.length()==0)
				{	edtNameNew.setError("Nhập tên Gesture");
					return ;
				}
				else
				{
					final GestureLibrary ges=ListGestures.getGesLibrary();
					String name=edtNameNew.getText().toString();
					ges.addGesture(name, gesture);
					if(ges.save()) 
					{	
						Toast.makeText(getApplicationContext(), "Thêm thành công", Toast.LENGTH_SHORT);
						Log.d("Save","SUCCESS");
						setResult(RESULT_OK);
					}
				}
			}
			else
			{
				setResult(RESULT_CANCELED);
			}			
			finish();
		}
	};
	
	private class GestureProcessor implements OnGestureListener
	{

		@Override
		public void onGesture(GestureOverlayView overlay, MotionEvent event) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGestureCancelled(GestureOverlayView overlay,
				MotionEvent event) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGestureEnded(GestureOverlayView overlay, MotionEvent event) {
			gesture=overlay.getGesture();
			btnAdd.setEnabled(true);
		}

		@Override
		public void onGestureStarted(GestureOverlayView overlay,
				MotionEvent event) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) 
	{
		super.onSaveInstanceState(outState);
		if(gesture!=null)
		{
			outState.putParcelable(keyStateGesture, gesture);
		}
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) 
	{
		super.onRestoreInstanceState(savedInstanceState);
		gesture=savedInstanceState.getParcelable(keyStateGesture);
		if(gesture!=null)
		{
			gestureOverlay.post(new Runnable() 
			{
				
				@Override
				public void run() 
				{
					gestureOverlay.setGesture(gesture);
				}
			});
		}
	}

}
