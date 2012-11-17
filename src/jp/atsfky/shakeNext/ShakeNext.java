/*	ToDo
 * ・やっぱレシーバをちゃんと作る
 * ・settingのリストがnullだとタップしたときエラー
 *	・btd保存ってどうやるん  ？もしくはnameとアドレスだけでも可？
 * ・resumeにまわせるものはまわす
 * 色変えたり、バイブレート(許可)
 *
 *
 *
 * */

package jp.atsfky.shakeNext;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import jp.atsfky.shakeNext.R.id;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ShakeNext extends Activity implements OnClickListener, OnLongClickListener, SensorEventListener{
    /** Called when the activity is first created. */
	public static Communication comu=null;
	public static OutputStream output=null;
	private SensorManager sensorManager;

	private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

	Button b1,b2,b3,b4;
	TextView t2,t3;
	View v1;

	int vib1,vib2;

	//timer
	MyTimer mt=new MyTimer();
	Timer timer=new Timer();
	TimerTask task = null;
	Handler handler = new Handler();
	Runnable runnable = new Runnable(){

		@Override
		public void run() {
			Vibrator vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
			// TODO 自動生成されたメソッド・スタブ
			if(mt.sec==0)t2.setTextColor(Color.GREEN);
			if(mt.sec==vib1*60){
				vibrator.vibrate(1000);
				t2.setTextColor(Color.YELLOW);
			}
			else if(mt.sec == vib2*60){
				vibrator.vibrate(2000);
				t2.setTextColor(Color.RED);
			}
			t2.setText(mt.getString());
			mt.tick();
		}

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);


        b1 = (Button) findViewById(id.Button01);
        b1.setOnClickListener(this);
        b2 = (Button) findViewById(id.Button02);
        b2.setOnClickListener(this);
        b3 = (Button) findViewById(id.Button03);
        b3.setOnClickListener(this);
        b4 = (Button) findViewById(id.Button04);
        b4.setOnClickListener(this);

        t2 = (TextView) findViewById(id.TextView02);
        t2.setOnClickListener(this);
        t2.setOnLongClickListener(this);
        t2.setTextColor(Color.GREEN);
        t3 = (TextView) findViewById(id.TextView03);

        v1 =(View) findViewById(id.View01);
        v1.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO 自動生成されたメソッド・スタブ
				if(comu==null){
					Toast.makeText(ShakeNext.this, "Menu -> Select PC", Toast.LENGTH_SHORT).show();
				}else{

					String a=String.valueOf(arg1.getAction());
					String x=String.valueOf(arg1.getX());
					String y=String.valueOf(arg1.getY());

					try {
						output.write(("v1,"+a+","+x+","+y+",").getBytes());
						output.flush();

					} catch (IOException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}
				}
				return true;
			}
		});


    }
    @Override
    protected void onResume(){
    	super.onResume();
    	Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();

    	List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
    	if(sensors.size()>0){
    		sensorManager.registerListener(this, sensors.get(0),SensorManager.SENSOR_DELAY_FASTEST);

    	}
    	
    	
    	if(SN_setting.btd != null)
		{
			Toast.makeText(this, "Connecting...", Toast.LENGTH_SHORT).show();
			comu=new Communication(SN_setting.btd, MY_UUID);
			comu.connect();
			//output = comu.getOutputStream();
			t3 = (TextView) findViewById(id.TextView03);
			t3.setText(SN_setting.btd.getName());
			Toast.makeText(this, "Connected !", Toast.LENGTH_SHORT).show();
		}
		else{
			Toast.makeText(this, "ピアリングされているデバイスがみつかりません", Toast.LENGTH_LONG).show();
		}
    	/*
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
    	vib1=Integer.valueOf(sp.getString("vib1","10"));
    	vib2=Integer.valueOf(sp.getString("vib2","15"));
    	*/

    }
    @Override
    protected void onPause(){
    	super.onPause();
    	sensorManager.unregisterListener(this);

    	if(task != null){
    		task.cancel();
    		task=null;
    	}
    }

  //メニュー
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		super.onCreateOptionsMenu(menu);
		MenuItem item0 = menu.add(Menu.NONE, Menu.FIRST+1,Menu.NONE,"Setting");
		item0.setIcon(android.R.drawable.ic_menu_preferences);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		Intent intent = new Intent(this, SN_setting.class);
		startActivity(intent);
		return true;
	}



	@Override
	public void onClick(View arg0) {
		// TODO 自動生成されたメソッド・スタブ
		//Toast.makeText(ShakeNext.this, "Clicked", Toast.LENGTH_SHORT).show();
		Vibrator vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
		if(comu==null){
			Toast.makeText(ShakeNext.this, "Menu -> Select PC", Toast.LENGTH_SHORT).show();
		}else{
			try {
				if(arg0.getId() == b1.getId()){
					output.write("b1".getBytes());
				}else if(arg0.getId() == b2.getId()){
					output.write("b2".getBytes());
				}else if(arg0.getId() == b3.getId()){
					output.write("b3".getBytes());
				}else if(arg0.getId() == b4.getId()){
					output.write("b4".getBytes());
				}else if(arg0.getId() == t2.getId()){
					//ストップウォッチをタップ
					output.write("t1".getBytes());

					if(task == null){
						task = new TimerTask(){

							@Override
							public void run() {
								// TODO 自動生成されたメソッド・スタブ
								handler.post(runnable);
							}
						};
						Toast.makeText(this, "start", Toast.LENGTH_SHORT).show();
						timer.schedule(task, 0,1000);
						vibrator.vibrate(100);

					}else{
						Toast.makeText(this, "stop", Toast.LENGTH_SHORT).show();
						task.cancel();
						task=null;
						vibrator.vibrate(100);
					}
				}
			}catch(Exception e){
				Toast.makeText(ShakeNext.this, "Error: onClick", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public boolean onLongClick(View arg0) {
		// TODO 自動生成されたメソッド・スタブ
		//Toast.makeText(ShakeNext.this, "LongClicked", Toast.LENGTH_SHORT).show();
		mt.reset();
		t2.setText(mt.getString());
		t2.setTextColor(Color.GREEN);

		try {
			output.write("t2".getBytes());

		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return true;
	}

	//volumeキー
	private boolean onVolumeKey(int keyCode, KeyEvent event,boolean isEnable){
		if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN ){
			try {
				if(isEnable)output.write("l1".getBytes());
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			return true;
		}else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
			try {
				if(isEnable)output.write("l2".getBytes());
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			return true;
		}else{
			return super.onKeyUp(keyCode, event);
		}
	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event){
		return onVolumeKey(keyCode, event,true);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		return onVolumeKey(keyCode, event,false);
	}


	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void onSensorChanged(SensorEvent arg0) {
		// TODO 自動生成されたメソッド・スタブ
		if(comu != null){


			if(arg0.values[0] > 13){
				try {
					output.write("s1".getBytes());
				} catch (IOException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
			}
		}
	}


}