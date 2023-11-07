package com.pose;

import android.hardware.SensorEventListener;

public abstract class ComSensorListener implements SensorEventListener, ComInterface {

    protected static float [] acceleration = null; // acceleration from TYPE_ACCELEROMETER
    protected static float [] omega = null;  // angular velocity from Sensor.TYPE_GYROSCOPE)

    public ComSensorListener() {
        super();
    }

    public static void initSensorData() {
        acceleration = null ;
        omega = null ;
    }

    public final double getVectorLength(float[] vector) {
        double length = 0;

        for (float element : vector) {
            length += element * element;
        }

        return Math.sqrt(length);
    }

    public final double toDegree(double angrad) {
        return Math.toDegrees( angrad );
    }
}
