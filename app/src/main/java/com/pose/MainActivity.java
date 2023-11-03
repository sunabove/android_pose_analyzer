package com.pose;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.hardware.Sensor;
import android.hardware.SensorManager;

public class MainActivity extends AppCompatActivity implements ComInterface {

    private SensorManager sensorManager;
    private Sensor gravitySensor;
    private Sensor rotationSensor;
    private Sensor motionSensor;
    private Sensor stepSensor;
    private Sensor acceleroMeter;
    private Sensor gyroSensor;

    private SensorEventListener accelerometerListener;
    private SensorEventListener gyroListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        this.acceleroMeter = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        this.motionSensor = sensorManager.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION);
        this.stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        this.gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        this.accelerometerListener = new AcceleroMeterListener( this ) ;
        this.gyroListener = new GyroListener( this );
    } // onCreate

    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(accelerometerListener, acceleroMeter, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(gyroListener, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();

        sensorManager.unregisterListener(accelerometerListener);
        sensorManager.unregisterListener(gyroListener);
    }
}