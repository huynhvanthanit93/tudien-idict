package vn.isolar.idict.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

public class SettingActivity extends PreferenceActivity implements OnPreferenceChangeListener,OnPreferenceClickListener
{
	EditTextPreference editTextPreference;
	ListPreference listPreference,defaulfirstlist;
	SharedPreferences sharedStore;
	Preference preferenceAbout;
	boolean checkChangle=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);		
		SetLocale.UpdateChangleLocale(this, PreferenceManager.getDefaultSharedPreferences(this).getString("selectLanguage","vi"));
		sharedStore= PreferenceManager.getDefaultSharedPreferences(this);					
		addPreferencesFromResource(R.xml.mysetting);					
		editTextPreference= (EditTextPreference)findPreference("setMaxHistory");
		editTextPreference.setSummary(sharedStore.getString("setMaxHistory", ""));
		editTextPreference.setOnPreferenceChangeListener(this);
		listPreference= (ListPreference)findPreference("selectLanguage");		
		listPreference.setSummary(sharedStore.getString("selectLanguage", ""));		
		listPreference.setOnPreferenceChangeListener(this);		
		
		preferenceAbout=(Preference)findPreference("about");
		preferenceAbout.setOnPreferenceClickListener(this);
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		
	    Log.d("pre",preference.getKey());
		if(preference.getKey().equals("setMaxHistory"))
		{
			editTextPreference.setSummary(newValue.toString());
			return true;
		}
		else 
			if(preference.getKey().equals("selectLanguage"))
			{	checkChangle=true;
				listPreference.setSummary(newValue.toString());
				SetLocale.UpdateChangleLocale(this, newValue.toString());
				Intent i= new Intent(this, SettingActivity.class);
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);					
				return true;
			}
			else
			    if(preference.getKey().equals("about"))
			    {
				
				return true;
			    }
			    
			
		return false;
	}
	
	
	
	@Override
	protected Dialog onCreateDialog(int id)
	{
	    AlertDialog.Builder builder=new AlertDialog.Builder(this);
	    builder.setTitle(R.string.about);
	    builder.setMessage(R.string.aboutDetails);
	    builder.setIcon(R.drawable.logo);
	    
	    return builder.create();
	}

	@Override
        public boolean onPreferenceClick(Preference preference)
        {
	    
	    if(preference.getKey().equalsIgnoreCase("about"))
	    {
        	    showDialog(0);
        	    return true;
	    }
	    return false;
	    
        }
	
}
