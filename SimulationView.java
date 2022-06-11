package edu.sjsu.android.accelorometer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;

public class SimulationView extends View implements SensorEventListener {
    private Display display;
    private SensorManager sensorManager;
    private Sensor sensor;

    private Bitmap field;
    private Bitmap ballBitmap;
    private Bitmap hoop;
    private Bitmap bitmap;
    private static final int BALL_SIZE = 64;
    private static final int HOOP_SIZE = 80;

    private Particle ball = new Particle();

    private float xOrigin, yOrigin;
    private float sensorX, sensorY, sensorZ;
    private float horizontalBound, verticalBound;
    private long myTimestamp;

    public SimulationView(Context context) {
        super(context);

        //initialize
        Bitmap ball = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
        ballBitmap = Bitmap.createScaledBitmap(ball, BALL_SIZE, BALL_SIZE, true);
        hoop = BitmapFactory.decodeResource(getResources(), R.drawable.hoop);
        hoop = Bitmap.createScaledBitmap(hoop, HOOP_SIZE, HOOP_SIZE, true);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        field = BitmapFactory.decodeResource(getResources(), R.drawable.field, options);

        //initialize display
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();

        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void onSizeChanged(int w, int h, int oldw, int oldh){
        Bitmap ball = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
        bitmap = Bitmap.createScaledBitmap(ball, BALL_SIZE, BALL_SIZE, true);
        Bitmap hoop = BitmapFactory.decodeResource(getResources(), R.drawable.hoop);
        hoop = Bitmap.createScaledBitmap(hoop, HOOP_SIZE, HOOP_SIZE, true);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        field = BitmapFactory.decodeResource(getResources(), R.drawable.field, options);

        xOrigin = w * 0.5f;
        yOrigin = h * 0.5f;

        horizontalBound = (w-BALL_SIZE) * 0.5f;
        verticalBound = (h-BALL_SIZE) * 0.5f;

    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        int orientation = display.getRotation();

        switch (orientation) {
            case Surface.ROTATION_0:
                sensorX = event.values[0];
                sensorY = event.values[1];
                break;
            case Surface.ROTATION_90:
                sensorX = -event.values[1];
                sensorY = event.values[0];
                break;
            case Surface.ROTATION_180:
                sensorX = -event.values[0];
                sensorY = -event.values[1];
                break;
            case Surface.ROTATION_270:
                sensorX = event.values[1];
                sensorY = -event.values[0];
                break;
        }
        sensorZ = event.values[2];
        myTimestamp = event.timestamp;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) { }

    protected void startSimulation(){
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void stopSimulation(){
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        canvas.drawBitmap(field, -550, -50, null);
        canvas.drawBitmap(hoop, xOrigin - HOOP_SIZE / 2, yOrigin - HOOP_SIZE / 2, null);

        ball.updatePosition(sensorX, sensorY, sensorZ, myTimestamp);
        ball.resolveCollisionWithBounds(horizontalBound, verticalBound);

        canvas.drawBitmap(bitmap, (xOrigin-BALL_SIZE/2) + ball.posX,
        (yOrigin-BALL_SIZE/2) - ball.posY, null);

        invalidate();
    }

}
