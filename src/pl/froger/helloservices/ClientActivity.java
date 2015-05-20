package pl.froger.helloservices;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class ClientActivity extends Activity {
	private Button btnStartService;
	private Button btnStopService;
	private CheckBox advSet;
	private TextView descr_sec;
	
	private EditText secfield;
	private TextView wheredesc;
	private EditText urlid;
	private TextView iddesc;
	private EditText devid;
	private CheckBox inforeq;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		btnStartService = (Button) findViewById(R.id.btnStartService);
		btnStopService = (Button) findViewById(R.id.btnStopService);
		initButtonsOnClick();
		this.advSet = (CheckBox) findViewById (R.id.advst);
		this.advSet.setChecked(false);
		this.advSet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(advSet.isChecked() == false){
					descr_sec.setVisibility(View.INVISIBLE);
					secfield.setVisibility(View.INVISIBLE);
					wheredesc.setVisibility(View.INVISIBLE);
					urlid.setVisibility(View.INVISIBLE);
					iddesc.setVisibility(View.INVISIBLE);
					devid.setVisibility(View.INVISIBLE);
					inforeq.setVisibility(View.INVISIBLE);
				}else{
					descr_sec.setVisibility(View.VISIBLE);
					secfield.setVisibility(View.VISIBLE);
					wheredesc.setVisibility(View.VISIBLE);
					urlid.setVisibility(View.VISIBLE);
					iddesc.setVisibility(View.VISIBLE);
					devid.setVisibility(View.VISIBLE);
					inforeq.setVisibility(View.VISIBLE);
				}
			}
		});
		this.descr_sec = (TextView) findViewById(R.id.coilesecdesc);
		this.descr_sec.setVisibility(View.INVISIBLE);
		
		//secfield EditText
		this.secfield = (EditText) findViewById(R.id.secfield);
		this.secfield.setVisibility(View.INVISIBLE);
		//wheredesc TextView
		this.wheredesc = (TextView) findViewById(R.id.wheredesc);
		this.wheredesc.setVisibility(View.INVISIBLE);
		//urlid EditText
		this.urlid = (EditText) findViewById(R.id.urlid);
		this.urlid.setVisibility(View.INVISIBLE);
		//iddesc TextView
		this.iddesc = (TextView) findViewById(R.id.iddesc);
		this.iddesc.setVisibility(View.INVISIBLE);
		//devid EditText
		this.devid = (EditText) findViewById(R.id.devid);
		this.devid.setVisibility(View.INVISIBLE);
		//inforeq CheckBox
		this.inforeq = (CheckBox) findViewById (R.id.inforeq);
		this.inforeq.setVisibility(View.INVISIBLE);
	}

	private void initButtonsOnClick() {
		OnClickListener listener = new OnClickListener() {
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.btnStartService:
					startMyService();
					break;
				case R.id.btnStopService:
					stopMyService();
					break;
				default:
					break;
				}
			}
		};
		btnStartService.setOnClickListener(listener);
		btnStopService.setOnClickListener(listener);
	}
	
	private void startMyService() {
		Intent serviceIntent = new Intent(this, MyService.class);
		startService(serviceIntent);
	}
	
	private void stopMyService() {
		Intent serviceIntent = new Intent(this, MyService.class);
		stopService(serviceIntent);
	}
}