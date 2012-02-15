package vn.isolar.idict.activity;

import android.content.Context;
import android.util.Log;


public class HTML
{


	Context context;
	DBAdapter dbAdapter;
	
	public HTML(Context context) 
	{
		this.context=context;
		this.dbAdapter=new DBAdapter(context);
	}
	
	public String convertMeanToHTML(String mean)
	{
		
		String str="<html><body>";
		mean=mean.replaceAll("[@]","<h2>");
		mean=mean.replaceAll("[*]","</h2><hr/><h3 color:red>");
		mean=mean.replaceAll("[-]","</h3><br/>");
		mean=mean.replaceAll("[=]","<br/>");
		mean=mean.replaceAll("[+]","<br/>");
		
		str+=mean+"</body></html>";
		
		return str;
	}
	public static String convertStringToHTML(String mean)
	{
		
		mean=mean.replaceAll("[@]","<div id~'word'>@ ");
		mean=mean.replaceAll("[*]","</div><div id~'style'><b>*   ");
		mean=mean.replaceAll("[-]","</b></div><div id~'mean'>- ");
		mean=mean.replaceAll("[=]","</div><div id~'examle'>~ ");
		//mean=mean.replaceAll("[+]","<div id~'meanss'>+ ");
		mean=mean.replaceAll("[~]", "=");
		
		String str="<html><head>"
				+"<style type=\"text/css\">  body{font-size: 1.4em; background-color:#d3d5db}"   
				+"#page{line-height:1.5em;} #word{} #style{font-weight:bold;} #mean{} #examle{} #meanss{}"
				+"</style></head>"
				+"<body><div id='page'>"
				;
	
		str+=mean+"</div></div></body></html>";
		
		return str;
	}
}
