package jp.atsfky.shakeNext;

public class MyTimer {
	public int sec=0;

	public void tick(){
		sec++;
	}

	public String getString(){
		return String.format("%02d:%02d:%02d", sec/3600,(sec/60)%60, sec%60);
	}

	public void reset(){
		sec = 0;
	}
}
