package vn.isolar.idict.activity;

import java.util.Locale;

import android.content.Context;
import android.content.res.Configuration;

public class SetLocale {

	public static void UpdateChangleLocale(Context context, String strLocale) {
		
		Locale locale = new Locale(strLocale);
		Locale.setDefault(locale);
		Configuration configuration = new Configuration();
		configuration.locale = locale;
		context.getResources().updateConfiguration(configuration,null);
	}

}
