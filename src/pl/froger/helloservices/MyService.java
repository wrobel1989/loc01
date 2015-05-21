package pl.froger.helloservices;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service implements LocationListener {
	private Toast toast;
	private Timer timer;
	private TimerTask timerTask;
	private String baseURL = "http://wrobel731.ovh.org/loc/loc.php";
	private double last_lon = -666;
	private double last_lat = -666;
	private double current_lon = -666;
	private double current_lat = -666;
	private double delta_for_request = 10 * 1000;//10s
	private long previous_timestamp = -666666666;
	
	private class MyTimerTask extends TimerTask {
		
		private String URL;
		
		@Override
		public synchronized void run() {
			long this_timestamp = System.currentTimeMillis();
			if(shouldMakeRequestToServer(this_timestamp)){
				new RequestTask().execute(baseURL + "?iddev=qqa123&lon="
						+current_lon+"&lat="+current_lat+"&timestmp="+this_timestamp);
				last_lon=current_lon;
				last_lat=current_lat;
			}
			showToast("Your service is still working. PING! "+last_lon+shouldMakeRequestToServer(this_timestamp)+current_lat+" last"+last_lat);
			previous_timestamp = this_timestamp;
		}
	}
	
	private void showToast(String text) {
		toast.setText(text);
		toast.show();
	}
	
	private boolean shouldMakeRequestToServer(long t){
		boolean should_req = (this.current_lat > -665 && this.current_lon > -665);
		boolean should_reqd = //((t-this.previous_timestamp)/1000.0>0.5*delta_for_request)
				should_req &&
				((Math.abs(current_lon-last_lon)>1e-13 && Math.abs(current_lat-last_lat)>1e-13) /*&& 
						(this.last_lat > -665 && this.last_lon > -665)*/);
		return should_reqd;
	}
	
	private void writeToLogs(String message) {
		Log.d("HelloServices", message);
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		writeToLogs("Called onCreate() method.");
		timer = new Timer(this.baseURL +"?iddev="+ System.currentTimeMillis() );
		toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 35000, 10, this);

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		writeToLogs("Called onStartCommand() methond");
		clearTimerSchedule();
		initTask();
		timer.scheduleAtFixedRate(timerTask, (int)delta_for_request, (int)delta_for_request);
		showToast("Your service has been started");
		return super.onStartCommand(intent, flags, startId);
	}

	private void clearTimerSchedule() {
		if(timerTask != null) {
			timerTask.cancel();
			timer.purge();
		}
	}
	
	private void initTask() {
		timerTask = new MyTimerTask();
	}

	@Override
	public void onDestroy() {
		writeToLogs("Called onDestroy() method");
		clearTimerSchedule();
		showToast("Your service has been stopped");
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public synchronized void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
		last_lon=current_lon;
		last_lat=current_lat;
		current_lat = location.getLatitude();
	    current_lon = location.getLongitude();
	    //showToast("NEW LOCATION FOUND lon:"+last_lon);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
}









class RequestTask extends AsyncTask<String, String, String>{

    @Override
    protected String doInBackground(String... uri) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        try {
            response = httpclient.execute(new HttpGet(uri[0]));
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                responseString = out.toString();
                out.close();
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            //TODO Handle problems..
        } catch (IOException e) {
            //TODO Handle problems..
        }
        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        //Do anything with response..
    }
}