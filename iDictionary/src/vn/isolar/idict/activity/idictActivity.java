package vn.isolar.idict.activity;

import java.util.ArrayList;

import vn.isolar.idict.managedatabase.ManageDB;
import vn.isolar.idict.managedatabase.QueryDB;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class idictActivity extends Activity implements OnGesturePerformedListener
{
    // Khai bao cao View
    
    ListView listView;
    Handler handler;
    Runnable runnable;
    EditText autoComplete;
    GestureOverlayView gestureOverlay;
    Button btgetDict;
    TextView txtEmptyDB;
    // ----
    public static String member;
    private ArrayList<Newwords> listWord = new ArrayList<Newwords>();
    private ArrayList<Integer> listId = new ArrayList<Integer>();// list cac' id
	                                                         // cua cac tu
    ArrayList<Boolean> check = new ArrayList<Boolean>();
    SharedPreferences sp;
    AdapterLayoutListView adapter;
    
    GestureLibrary gesLibrary;
    // duong dan toi file gesture
    String pathGesture = ManageDB.pathGesture;
    
    // xac dinh xem tu dien dang duoc chon la gi
    public static final String key_language = "language";
    private int checkSelect = 0;// xu ly chon tu dien
    private final String have_word = "have_word";
    private String EV;
    private String VE;
    private String VV;
    
    private final String nameDBEV = "anhviet";
    private final String nameDBVE = "vietanh";
    private final String nameDBVV = "vietviet";
    private String[] tempName;
    
    // thuc hien cac truy van tra tu dien
    QueryDB qdb;
    SQLiteDatabase db;
    
    String listDict[];
    
    String test[] = { "trinh", "phu ", "luc" };
    
    boolean[] testcheck = new boolean[test.length];
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
	
	// kiem tra ngon ngu de update
	super.onCreate(savedInstanceState);
	sp = PreferenceManager.getDefaultSharedPreferences(this);
	SetLocale.UpdateChangleLocale(this, sp.getString("selectLanguage", "en"));
	setContentView(R.layout.main);
	
	Log.d("check update", PreferenceManager.getDefaultSharedPreferences(this).getString("selectLanguage", "en"));
	autoComplete = (EditText) findViewById(R.id.autoComplete);
	listView = (ListView) findViewById(R.id.listView);
	gestureOverlay = (GestureOverlayView) findViewById(R.id.gesture_overlay);
	btgetDict = (Button) findViewById(R.id.btgetDict);
	txtEmptyDB = (TextView) findViewById(R.id.txtEmptyDB);
	
	EV = getResources().getString(R.string.txtEV);
	VE = getResources().getString(R.string.txtVE);
	VV = getResources().getString(R.string.txtVV);
	
	listDict = setAdapterForSpinner();
	btgetDict.setOnClickListener(new View.OnClickListener()
	{
	    
	    @Override
	    public void onClick(View v)
	    {
		
		createDialog().show();
		
	    }
	});
	autoComplete.addTextChangedListener(addTextChange);
	autoComplete.setOnKeyListener(new View.OnKeyListener()
	{
	    
	    @Override
	    public boolean onKey(View v, int keyCode, KeyEvent event)
	    {
		if (keyCode == KeyEvent.KEYCODE_ENTER)
		    return true;
		return false;
	    }
	});
	// kiem tra DB de xu ly tiep theo
	checkDB();
    }
    
    public Dialog createDialog()
    {
	AlertDialog.Builder t = new AlertDialog.Builder(this);
	t.setTitle(R.string.titleSelectDict);
	t.setItems(tempName, new DialogInterface.OnClickListener()
	{
	    
	    @Override
	    public void onClick(DialogInterface dialog, int which)
	    {
		btgetDict.setText(listDict[which]);
		
		String languageFile = (String) btgetDict.getText();
		
		if (languageFile.equalsIgnoreCase(EV))
		    languageFile = nameDBEV;
		else
		    if (languageFile.equalsIgnoreCase(VE))
			languageFile = nameDBVE;
		    else
			languageFile = nameDBVV;
		// su dung co so du lieu nhu da chon
		setLanguageForDic(languageFile);
		// xoa het cac tu o tu dien cu khoi apdapter
		listId.clear();
		listWord.clear();
		check.clear();
		autoComplete.setText(autoComplete.getText());
		adapter.setStoreFile(sp.getString(key_language, ""));
		adapter.notifyDataSetChanged();
	    }
	});
	return t.create();
	
    }
    
    public TextWatcher addTextChange = new TextWatcher()
    {
	
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count)
	{
	    
	    handler.removeCallbacks(runnable);
	    handler.postDelayed(runnable, 0);
	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after)
	{
	    
	}
	
	@Override
	public void afterTextChanged(Editable s)
	{
	}
    };
    
    private void loadWord(String word)
    {
	qdb.open();
	Cursor c = qdb.wordQuery(word);
	Log.d("run", "2");
	
	if (c != null && c.getCount() > 0)
	{
	    listId.clear();
	    listWord.clear();
	    check.clear();
	    c.moveToFirst();
	    do
	    {
		String w = c.getString(1);
		Newwords W = new Newwords(w, "");
		listWord.add(W);
		int id = c.getInt(0);
		listId.add(id);
		adapter.notifyDataSetChanged();
	    }
	    while (c.moveToNext());
	    c.close();
	}
	qdb.close();
	
    }
    
    /**
     * Su kien khi click vao spinner chon ngon ngu
     */
    
    // ham nay de nhan biet gesture
    @Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture)
    {
	ArrayList<Prediction> predictions = gesLibrary.recognize(gesture);
	Prediction pre = predictions.get(0);
	
	for (Prediction prediction : predictions)
	{
	    if (prediction.score > 1.0 && prediction.score > pre.score)
	    {
		pre = prediction;
	    }
	}
	if (pre != null && pre.score > 1.3)
	{
	    autoComplete.append(pre.name);
	}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
	ManageDB managedb = new ManageDB();
	if (!managedb.checkEmpty())
	{
	    MenuInflater inflater = new MenuInflater(getApplicationContext());
	    inflater.inflate(R.menu.menu_option, menu);
	}
	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item)
    {
	switch (item.getItemId())
	{
	    case R.id.itemSetting:
		startActivityForResult(new Intent(this, SettingActivity.class), 1);
		
		break;
	    case R.id.itemManageGesture:
		startActivity(new Intent("com.khmt2k3.xuantung.managegesture.ListGestures"));
		break;
	    case R.id.itemChecked:
		startActivity(new Intent(this, HistoryActivity.class));
		break;
	    case R.id.itemFavourite:
		startActivity(new Intent(this, FavouriteActivity.class));
		break;
	    default:
		break;
	}
	return super.onMenuItemSelected(featureId, item);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
	super.onActivityResult(requestCode, resultCode, data);
	if (requestCode == 1)
	{
	    Log.e("resultCode", resultCode + "");
	    Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
	    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    startActivity(i);
	}
    }
    
    /**
     * Trong folder ngon ngu co file db nao thi hien thi ten len Spinner
     */
    private String[] setAdapterForSpinner()
    {
	ManageDB m = new ManageDB();
	String[] temp = m.getNameDB();
	tempName = new String[temp.length];
	if (temp.length != 0)
	{
	    
	    String s = sp.getString(key_language, "null");
	    
	    // kiem tra xem tu dien duoc chon khi truoc la gi thi khi mo lai lan
	    // sau
	    // chon dung tu dien do'
	    if (!s.equalsIgnoreCase("null"))
	    {
		setLanguageForDic(s);
		for (int i = 0; i < temp.length; i++)
		{
		    if (temp[i].equalsIgnoreCase(s))
		    {
			String t = temp[0];
			temp[0] = s;
			temp[i] = t;
		    }
		}
		
	    }
	    else
	    {
		setLanguageForDic(temp[0]);
	    }
	    
	    // xet lai de hien thi len spinner cho gon
	    for (int i = 0; i < temp.length; i++)
	    {
		if (temp[i].equalsIgnoreCase(nameDBEV))
		{
		    temp[i] = getResources().getString(R.string.txtEV);
		    tempName[i] = getResources().getString(R.string.txtDicEV);
		}
		
		if (temp[i].equalsIgnoreCase(nameDBVE))
		{
		    temp[i] = getResources().getString(R.string.txtVE);
		    tempName[i] = getResources().getString(R.string.txtDicVE);
		}
		if (temp[i].equalsIgnoreCase(nameDBVV))
		{
		    temp[i] = getResources().getString(R.string.txtVV);
		    tempName[i] = getResources().getString(R.string.txtDicVV);
		}
	    }
	    
	    return temp;
	    
	}
	return null;
    }
    
    /**
     * cai dat ngon ngu ma tu dien su dung de tra
     */
    private void setLanguageForDic(String lang)
    {
	ManageDB.setDBSelected(lang);
	qdb = new QueryDB(ManageDB.getDBSelected());
	
	SharedPreferences.Editor editor = sp.edit();
	editor.remove(key_language);
	editor.putString(key_language, lang);
	editor.commit();

	

	
    }
    
    /**
     * Kiem tra xem co co so du lieu o the nho hay khong?
     */
    private void checkDB()
    {
	ManageDB manageDB = new ManageDB();
	if (manageDB.checkEmpty())
	{
	    txtEmptyDB.setText(Html.fromHtml(getResources().getString(R.string.txtEmptyDB)));
	    txtEmptyDB.setMovementMethod(LinkMovementMethod.getInstance());
	    txtEmptyDB.setVisibility(View.VISIBLE);
	    gestureOverlay.setEnabled(false);
	    autoComplete.setEnabled(false);
	    btgetDict.setEnabled(false);
	    autoComplete.setVisibility(View.GONE);
	    btgetDict.setVisibility(View.GONE);
	}
	else
	{
	    String keylanguage = sp.getString(key_language, "");
	    if (keylanguage.equals(nameDBEV))
		btgetDict.setText(R.string.txtEV);
	    else
		if (keylanguage.equals(nameDBVE))
		    btgetDict.setText(R.string.txtVE);
		else
		    if (keylanguage.equals(nameDBVV))
			btgetDict.setText(R.string.txtVV);
	    // load file thu vien cua gesture len
	    gesLibrary = GestureLibraries.fromFile(pathGesture);
	    gesLibrary.load();
	    // ---
	    // set cac loai ngon ngu co cho spinner
	    setAdapterForSpinner();
	    // xu ly su kien khi ve len man hinh
	    gestureOverlay.addOnGesturePerformedListener(this);
	    
	    // kiem tra neu cho phep su dung gesture hay khong
	    if (sp.getBoolean("checkEnableGesture", true))
	    {
		if (manageDB.checkGesture())
		    gestureOverlay.setEnabled(true);
		else
		    gestureOverlay.setEnabled(false);
	    }
	    else
		gestureOverlay.setEnabled(false);
	    // ----*/
	    
	    handler = new Handler();
	    runnable = new Runnable()
	    {
		
		@Override
		public void run()
		{
		    String word = autoComplete.getText().toString();
		    loadWord(word);
		}
	    };
	    
	    adapter = new AdapterLayoutListView(getApplicationContext(), listWord, listId);
	    
	    listView.setAdapter(adapter);
	    // kiem tra xem activity nay co nhan duoc mot tu de tra tu mot
	    // intent khac hay khong?
	    String s = getIntent().getStringExtra(have_word);
	    if (s != null)
	    {
		autoComplete.setText(s);
	    }
	    else
		autoComplete.setText("");
	}
    }
}
