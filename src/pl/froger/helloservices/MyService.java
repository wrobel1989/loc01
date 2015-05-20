package pl.froger.helloservices;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service {
	private Toast toast;
	private Timer timer;
	private TimerTask timerTask;
	private String baseURL = "http://wrobel731.ovh.org/loc/loc.php";
	private class MyTimerTask extends TimerTask {
		
		private String URL;
		
		@Override
		public void run() {
			new RequestTask().execute("http://wrobel731.ovh.org/loc/loc.php?iddev=qqa123&timestmp="+System.currentTimeMillis());
			showToast("Your service is still working. PING!");
		}
	}
	
	private void showToast(String text) {
		toast.setText(text);
		toast.show();
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
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		writeToLogs("Called onStartCommand() methond");
		clearTimerSchedule();
		initTask();
		timer.scheduleAtFixedRate(timerTask, 4 * 1000, 4 * 1000);
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