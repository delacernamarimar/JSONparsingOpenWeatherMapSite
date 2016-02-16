package com.example.weatherjson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private URL url;
	StringBuffer buffer;
	TextView output,output2;
	private BufferedReader reader;
	ImageView img;

	public void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.activity_main);
	      output = (TextView) findViewById(R.id.textView);
	      output2 = (TextView) findViewById(R.id.textView1);
	      img = (ImageView) findViewById(R.id.imageView1);
	      //access firewall in latest android versions
	      StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
	      StrictMode.setThreadPolicy(policy);
	      
	      //connect and get data from the site
	      DefaultHttpClient   httpclient = new DefaultHttpClient(new BasicHttpParams());
	      HttpPost httppost = new HttpPost("http://api.openweathermap.org/data/2.5/weather?q=Japan&appid=44db6a862fba0b067b1930da0d769e98");
	      InputStream inputStream = null;
	      String result = null;
	      try {
	          HttpResponse response = httpclient.execute(httppost);           
	          HttpEntity entity = response.getEntity();
	          inputStream = entity.getContent();
	          reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
	          StringBuilder sb = new StringBuilder();
	          String line = null;
	          while ((line = reader.readLine()) != null)
	          {
	              sb.append(line + "\n");
	          }
	          result = sb.toString();
	       
	      } catch (Exception e) { 
	          // Oops
	      }
	      finally {
	          try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
	      }
          
	      try {
	    	  //get non array JSONObject
	    	  JSONObject json = new JSONObject(result);
	    	  JSONObject json1 = json.getJSONObject("main");
	    	  String temp = json1.getString("temp");
	    	  output.setText("Temp in F:"+temp+"f");  
	    	
	    	  String name = json.getString("name");	    	  
	    	  output2.setText("Country"+name);
	    	  
	    	  //get a JSONObject in an JSONArray
	    	  JSONArray weather = json.getJSONArray("weather");
	    	  JSONObject j = weather.getJSONObject(0);
	    	  String icon = j.getString("icon");
	    	  
	    	  //get an image in a site
	    	  try {
	    	        InputStream is = (InputStream) new URL("http://openweathermap.org/img/w/"+icon+".png").getContent();
	    	        Drawable d = Drawable.createFromStream(is, "src name");
	    	        img.setImageDrawable(d);
	    	    } catch (Exception e) {
	    	      
	    	    }
	    	  
			} catch (JSONException e) {
				e.printStackTrace();
			}
	      
	      
	  

	   }
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
