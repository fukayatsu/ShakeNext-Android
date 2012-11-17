package jp.atsfky.shakeNext;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class Communication extends Thread{
	private BluetoothSocket bs;

	Communication(BluetoothDevice device,UUID uuid)
	{
	BluetoothSocket tmp = null;
		try {
			 tmp = device.createRfcommSocketToServiceRecord(uuid);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d("debug","create failed");
			e.printStackTrace();
		}
		bs = tmp;
	}

	public void connect(){
		try {
			bs.connect();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
	public OutputStream getOutputStream(){
		try {
			return bs.getOutputStream();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return null;
		}

	}

}