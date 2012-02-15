package vn.isolar.idict.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SettingActivity extends PreferenceActivity implements OnPreferenceChangeListener{
	EditTextPreference editTextPreference;
	ListPreference listPreference,defaulfirstlist;
	SharedPreferences sharedStore;
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
		
		
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		
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
		return false;
	}	
	
}
