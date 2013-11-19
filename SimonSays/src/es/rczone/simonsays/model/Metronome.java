package es.rczone.simonsays.model;

import android.os.Handler;
import android.util.Log;

public class Metronome {
    private long millisInFuture;
    private long countDownInterval;
    private long time;
    private boolean status;
    private IPulseListener listener;
    private float epsilon;
    private long currenTimeInMilis;
    
    public Metronome(long pMillisInFuture, long pCountDownInterval) {
            this.millisInFuture = pMillisInFuture;
            this.countDownInterval = pCountDownInterval;
            status = false;
    }

    public void setListener(IPulseListener listener){
    	this.listener = listener;
    }
    
    public void stop() {
        status = false;
    }

    public long getCurrentTime() {
        return millisInFuture;
    }

    public void start() {
        status = true;
        time=0;
        initialize();
    }
    private void initialize() 
    {
        final Handler handler = new Handler();
        final Runnable counter = new Runnable(){

            public void run(){
            	time += countDownInterval;
                long sec = time/1000;
                if(status) {
                    if(time >= millisInFuture) {
                    	listener.onFinish();
                    } else {
                        listener.onPulse(time);
                        handler.postDelayed(this, countDownInterval);
                    }
                    Log.i("metronome", Long.toString(sec));
                } else {
                    Log.i("metronome", Long.toString(sec) + " seconds remain and timer has stopped!");
                    handler.postDelayed(this, countDownInterval);
                }
            }
        };

        handler.postDelayed(counter, countDownInterval);
    }
}
