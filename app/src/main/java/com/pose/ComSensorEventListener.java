package com.pose;

import android.hardware.SensorEventListener;

public abstract class ComSensorEventListener implements SensorEventListener, ComInterface {

    protected float[] accels = new float[3]; // accelerations from TYPE_ACCELEROMETER sensor

    public ComSensorEventListener() {

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
