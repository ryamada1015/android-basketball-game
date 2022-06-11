package edu.sjsu.android.accelorometer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.PowerManager;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "edu.sjsu.android.accelerometer:MainActivity";
    private PowerManager.WakeLock myWakeLock;
    private SimulationView mySimulationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PowerManager myPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
        myWakeLock = myPowerManager.newWakeLock(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, TAG);
        mySimulationView = new SimulationView(this);

        //set to mySimulationView instead of the layout file which sets a static view
        //mySimulationView is used so that the view can be updated however we want
        setContentView(mySimulationView);

    }

    protected void onResume(){
        super.onResume();
        myWakeLock.acquire();
        mySimulationView.startSimulation();
    }

    protected void onPause(){
        super.onPause();
        myWakeLock.release();
        mySimulationView.stopSimulation();
    }
}
