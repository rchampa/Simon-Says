package es.rczone.simonsays.model;

import android.os.Handler;
import android.util.Log;

public class Metronome {
    private long millisInFuture;
    private long countDownInterval;
    private long time;
    private boolean status;
    private IPulseListener listener;
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
        Log.v("status", "starting");
        final Runnable counter = new Runnable(){

            public void run(){
                long sec = time/1000;
                if(status) {
                    if(time >= millisInFuture) {
                    	listener.onFinish();
                    } else {
                        listener.onPulse(time);
                        time += countDownInterval;
                        handler.postDelayed(this, countDownInterval);
                    }
                    Log.v("metronome", Long.toString(sec));
                } else {
                    Log.v("metronome", Long.toString(sec) + " seconds remain and timer has stopped!");
                    handler.postDelayed(this, countDownInterval);
                }
            }
        };

        handler.postDelayed(counter, 0);
    }
}
