package com.pose;

import android.hardware.SensorEventListener;

public abstract class ComSensorEventListener implements SensorEventListener, ComInterface {

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
