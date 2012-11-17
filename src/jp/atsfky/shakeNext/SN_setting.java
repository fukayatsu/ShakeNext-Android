package jp.atsfky.shakeNext;


import java.util.Set;
import java.util.Vector;



import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;
import android.widget.Toast;

public class SN_setting extends PreferenceActivity implements OnPreferenceChangeListener{


	//private static final UUID MY_UUID = UUID.fromString(uuid);

	//public Communication comu=null;
	Vector<BluetoothDevice> devices = new Vector<BluetoothDevice>();
	public static BluetoothDevice btd=null;


	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref);


		BluetoothAdapter bt_adapter = BluetoothAdapter.getDefaultAdapter();
		if(bt_adapter==null)
		{
			Toast.makeText(this, "���̒[���ł�Bluetooth�͗��p�ł��܂���B", Toast.LENGTH_LONG).show();
			finish();
		}
		if(!bt_adapter.isEnabled())
    	{

    		Intent enable_bt = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    		startActivity(enable_bt);
    	}
    	//����Bluetooth��ON�ɍŏ�����Ȃ��Ă�����
    	else
    	{

    		//getBoundedDevices�ɂ���ăs�A�����O�����ɂȂ���Ă���f�o�C�X���擾����
    		Set<BluetoothDevice> paired_devices = bt_adapter.getBondedDevices();
    		//BluetoothDevice�̃C���X�^���X��devices�C���X�^���X�ɒǉ����Ă���
    		for(BluetoothDevice device : paired_devices)
    		{
    			devices.addElement(device);
    		}
    		//devices�ɕۑ����ꂽBluetoothDevice�C���X�^���X�𗘗p���ď��Ԃɏ������O�ɏo�͂��Ă���
    		//for(int i=0;i<devices.size();i++) Log.d("debug",devices.elementAt(i).getName()+":"+devices.elementAt(i).getAddress());
    	}

		//PC�̑I��

		final ListPreference lp = (ListPreference) this.findPreference("select_pc");

		//String[] entries =new String[5];
		//String[] entryValues = new String[5];
		Vector<CharSequence> entries = new Vector<CharSequence>();
		Vector<CharSequence> entryValues = new Vector<CharSequence>();


		for(int i=0;i<devices.size();i++) {
			entries.addElement(devices.elementAt(i).getName());
			entryValues.addElement(devices.elementAt(i).getAddress());
			//Toast.makeText(this,entries.elementAt(i).toString(), Toast.LENGTH_SHORT).show();
		}
		String a[] = new String[devices.size()];
		String b[] = new String[devices.size()];
		entries.copyInto(a);
		entryValues.copyInto(b);
		lp.setEntries(a);
		lp.setEntryValues(b);
		lp.setOnPreferenceChangeListener(new OnPreferenceChangeListener(){

			@Override
			public boolean onPreferenceChange(Preference preference,
					Object newValue) {
				// TODO �����������ꂽ���\�b�h�E�X�^�u
				int index =lp.findIndexOfValue((String) newValue);
				btd = devices.elementAt(index);
				//Toast.makeText(SN_setting.this, lp.getEntryValues()[index].toString(), Toast.LENGTH_SHORT).show();

				return true;
			}

		});

	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		return true;
	}


}
