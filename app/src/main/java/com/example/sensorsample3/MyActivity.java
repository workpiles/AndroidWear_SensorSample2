package com.example.sensorsample3;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

public class MyActivity extends Activity {
    private final String TAG = MyActivity.class.getName();

    private TextView mTextStep;
    private TextView mTextStepCounter;
    private TextView mTextMotion;
    private TextView mTextTilt;
    private TextView mTextHeart;

    private SensorManager mSensorManager;
    private Sensor mMotion;
    private Random mR;


    final SensorEventListener mStepListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            Log.d(TAG, "Step Sensor Changed : " + event.values[0]);
            if (mTextStep != null) {
                mTextStep.setText(String.valueOf(event.values[0]));
                mTextStep.setBackgroundColor(0xFF000000+mR.nextInt(0xFFFFFF));
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    final SensorEventListener mStepCountListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            Log.d(TAG, "Step Counter Changed : " + event.values[0]);
            if (mTextStepCounter != null) {
                mTextStepCounter.setText(String.valueOf(event.values[0]));
                mTextStepCounter.setBackgroundColor(0xFF000000+mR.nextInt(0xFFFFFF));
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    final TriggerEventListener mMotionListener = new TriggerEventListener() {
        @Override
        public void onTrigger(TriggerEvent event) {
            Log.d(TAG, "Motion Sensor Changed");
            if (mTextMotion != null) {
                mTextMotion.setText(String.valueOf(event.values[0]) + "/" + event.timestamp);
                mTextMotion.setBackgroundColor(0xFF000000+mR.nextInt(0xFFFFFF));
            }
        }
    };

    final SensorEventListener mTiltListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            Log.d(TAG, "Tilt Sensor Changed : " + event.values[0]);
            if (mTextTilt != null) {
                mTextTilt.setText(String.valueOf(event.values[0]));
                mTextTilt.setBackgroundColor(0xFF000000+mR.nextInt(0xFFFFFF));
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    final SensorEventListener mHeartListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            Log.d(TAG, "Heart Rate Changed : " + event.values[0]);
            if (mTextHeart != null) {
                mTextHeart.setText(String.valueOf(event.values[0]));
                mTextHeart.setBackgroundColor(0xFF000000+mR.nextInt(0xFFFFFF));
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            Log.d(TAG, "onAccuracyChanged : " + accuracy);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextStep = (TextView) stub.findViewById(R.id.textView_step);
                mTextStepCounter = (TextView) stub.findViewById(R.id.textView_stepCounter);
                mTextMotion = (TextView) stub.findViewById(R.id.textView_motion);
                mTextTilt = (TextView) stub.findViewById(R.id.textView_tilt);
                mTextHeart = (TextView) stub.findViewById(R.id.textView_heart);
            }
        });

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        for (Sensor sensor : sensorList){
            Log.d(TAG, sensor.getName() + "/" + sensor.getType() + "/" + sensor.getVendor() + "/" + sensor.getMaximumRange() + "/" + sensor.getMinDelay() + "/" + sensor.getPower() + "/" + sensor.getResolution());
        }

    }



    @Override
    protected void onResume() {
        super.onResume();

        Sensor step = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        Sensor stepCount = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        mMotion = mSensorManager.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION);
        Sensor tilt = mSensorManager.getDefaultSensor(65536);
        Sensor heart = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
//        Sensor heart = mSensorManager.getDefaultSensor(65562);

        mSensorManager.registerListener(mStepListener, step, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(mStepCountListener, stepCount, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(mTiltListener, tilt, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(mHeartListener, heart, 3);
        mSensorManager.requestTriggerSensor(mMotionListener,mMotion);

        mR = new Random(System.currentTimeMillis());
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mStepListener);
        mSensorManager.unregisterListener(mStepCountListener);
        mSensorManager.unregisterListener(mTiltListener);
        mSensorManager.unregisterListener(mHeartListener);
        mSensorManager.cancelTriggerSensor(mMotionListener, mMotion);
    }

}

