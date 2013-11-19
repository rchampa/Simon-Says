package es.rczone.simonsays.model;

import android.util.Log;

public class TraceTimeList {
	
	private long[] traceTimeList;
	private int last;
	private int current;

	
	public TraceTimeList(int size){
		this.current = this.last = 0;
		this.traceTimeList = new long[size];
	}

	public void addTime(long time){
		traceTimeList[current] = time;
		last = current;
		current++;
	}
	
	public boolean isInTraceList(long time, long epsilon){
		
				
		long leftLimit = traceTimeList[last]-epsilon;
		long rightLimit = traceTimeList[last]+epsilon;
		
		Log.i("TraceTimeList", "last : "+traceTimeList[last]);
		Log.i("TraceTimeList", "left : "+leftLimit);
		Log.i("TraceTimeList", "right: "+rightLimit);
		Log.i("TraceTimeList", "time : "+time);
		
		if( leftLimit<=time && time<=rightLimit ){
			Log.i("TraceTimeList", "nailed!");
			return true;
		}

		return false;
	}
	
	public void reset(){
		this.current = this.last = 0;
	}
}
