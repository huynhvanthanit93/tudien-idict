package vn.isolar.idict.activity;

import vn.isolar.idict.activity.Webkit.OnSelectTextListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.ClipboardManager;


/**
 * Lop nay dung de bat' su kien chon mot tu trong webview
 * @author XUANTUNG
 *
 */
public class WebkitSelectedText 
{
	private Context context;
	Webkit webkit;
	Activity activity;
	//kiem tra xem gui Intent kem them tu nao?
	private final String have_word="have_word";
	
	public WebkitSelectedText(Context context,Webkit webkit,Activity activity) 
	{
		this.context=context;
		this.webkit=webkit;
		this.activity=activity;
	}
	
	public void init()
	{
		final ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
		clipboardManager.setText(null);
		setOnSelectTextListener(clipboardManager, webkit);
	}
	
	private void setOnSelectTextListener(final ClipboardManager clipboardManager, final Webkit webkit) {
		webkit.setOnSelectTextListener(new OnSelectTextListener() {
			@Override
			public void onSelectText() {
				final String text = clipboardManager.getText().toString();
				if(text!=null) 
				{
					Intent intent=new Intent(context,idictActivity.class);
					intent.putExtra(have_word, text);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
					
					//context.startActivity(intent);
				} 
			}
		});
	}	

}
