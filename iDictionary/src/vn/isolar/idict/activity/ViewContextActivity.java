package vn.isolar.idict.activity;

import java.io.File;
import java.util.Locale;

import vn.isolar.idict.activity.Webkit.OnSelectTextListener;
import vn.isolar.idict.managedatabase.ManageDB;
import vn.isolar.idict.managedatabase.QueryDB;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewContextActivity extends Activity  {
	
	TextView tv;
	WebView webView;
	Button btspeak;
	Webkit webkit;
	LinearLayout linearlayoutWeb;
	
	String mean;
	QueryDB qd;
	Integer id;
	String word;
	CheckBox checkLike;
	Boolean checkFirt=false;
	
	SharedPreferences storeShare;
	StoreFile store;
	File filexml;
	private String pathDB=Environment.getExternalStorageDirectory()+"/idict/";
	
	TextToSpeech tts;
	private File directory;
	private File fileFavourite;
	
	private final String key_language=idictActivity.key_language;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_view_context);
		LinearLayout ll= (LinearLayout)findViewById(R.id.layout_view);
		linearlayoutWeb=(LinearLayout)findViewById(R.id.linearlayoutWeb);
		btspeak= (Button)findViewById(R.id.imgSpeak);
		checkLike= (CheckBox)findViewById(R.id.checkFavourite);
		checkLike.setOnCheckedChangeListener(check);
		tv=(TextView)findViewById(R.id.txtViewWord);
		

		
		webkit=new Webkit(this);
		linearlayoutWeb.addView(webkit);
		WebkitSelectedText webkitSelectedText=new WebkitSelectedText(getApplicationContext(), webkit);
		webkitSelectedText.init();
		
		ll.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		
		
		// kiem tra folder history
		directory= new File(pathDB+"/favourite");
		if(!directory.exists())
		{ 
			directory.mkdir();
		}
		storeShare= PreferenceManager.getDefaultSharedPreferences(this);		
		fileFavourite= new File(directory,storeShare.getString(key_language, "null")+".fav");		
		store= new StoreFile(this, fileFavourite,false);
		//end kiem tra   /
										
						
		btspeak.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tts.speak(word, TextToSpeech.QUEUE_FLUSH, null);
			}
		});
		
		
						
		tts=new TextToSpeech(this, new OnInitListener() {
			
			@Override
			public void onInit(int status) {
				// TODO Auto-generated method stub
				
			}
		});
		tts.setLanguage(Locale.US);		
		Bundle bundle= getIntent().getExtras();
		/*
		((LinearLayout)findViewById(R.id.linearlayoutWeb)).addView(webkit);
		tv=(TextView)findViewById(R.id.txtViewWord);*/
		
		
		qd= new QueryDB(ManageDB.getDBSelected(), "favourite");
		if(bundle!=null)
		{	word=bundle.getString("word");
			tv.setText(word);
			mean=bundle.getString("mean");
			webkit.loadDataWithBaseURL("",HTML.convertStringToHTML(mean), "text/html", "UTF-8", null);
			id= bundle.getInt("id");			
			    
						
		}		
	}
	
	OnCheckedChangeListener check= new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub
			
			checkFirt=store.checkKey(id.toString());			
		}
	};
	
	@Override
	protected void onDestroy() {	
		super.onDestroy();
		
		if(checkLike.isChecked()==true)
		{
			if(checkFirt==false)
			{
				store.appendText(id.toString(),word);
				Log.d("da luu", "save:"+ word);
			}
		}
	}	
	
	/**
	 * Lop nay dung de bat' su kien chon mot tu trong webview
	 * @author XUANTUNG
	 *
	 */
	public class WebkitSelectedText
	{
		private Context context;
		Webkit webkit;
		//kiem tra xem gui Intent kem them tu nao?
		private final String have_word="have_word";
		
		public WebkitSelectedText(Context context,Webkit webkit) 
		{
			this.context=context;
			this.webkit=webkit;
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
						finish();
						context.startActivity(intent);
					} 
				}
			});
		}	

	}

}
