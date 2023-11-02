package com.pose;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;
import android.widget.EditText;

public class AcceleroMeterListener implements SensorEventListener, ComInterface {

    int eventIndex = 0;

    EditText gravity_abs;
    EditText gravity[] = new EditText[3];

    EditText linearAccAbs;
    EditText linearAcc[] = new EditText[3];

    public AcceleroMeterListener(MainActivity activity) {
        this.gravity_abs = activity.findViewById(R.id.gravity_abs);
        this.gravity[0] = activity.findViewById(R.id.gravity_00);
        this.gravity[1] = activity.findViewById(R.id.gravity_01);
        this.gravity[2] = activity.findViewById(R.id.gravity_02);

        this.linearAccAbs = activity.findViewById(R.id.linear_acc_abs);
        this.linearAcc[0] = activity.findViewById(R.id.linear_acc_00);
        this.linearAcc[1] = activity.findViewById(R.id.linear_acc_01);
        this.linearAcc[2] = activity.findViewById(R.id.linear_acc_02);
    }

    public double getVectorLength(float[] vector) {
        double length = 0;

        for (float element : vector) {
            length += element * element;
        }

        return Math.sqrt(length);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // alpha is calculated as t / (t + dT),
        // where t is the low-pass filter's time-constant and
        // dT is the event delivery rate.

        this.eventIndex++;

        final float alpha = 0.8f;

        float gravity[] = new float[3];
        float linear_acceleration[] = new float[3];
        float[] g = new float[3];
        System.arraycopy(event.values, 0, g, 0, 3);

        // Isolate the force of gravity with the low-pass filter.
        gravity[0] = alpha * gravity[0] + (1 - alpha) * g[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * g[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * g[2];

        // Remove the gravity contribution with the high-pass filter.
        linear_acceleration[0] = g[0] - gravity[0];
        linear_acceleration[1] = g[1] - gravity[1];
        linear_acceleration[2] = g[2] - gravity[2];

        this.gravity_abs.setText(String.format("%.2f", getVectorLength(g)));
        this.gravity[0].setText(String.format("%.2f", g[0]));
        this.gravity[1].setText(String.format("%.2f", g[1]));
        this.gravity[2].setText(String.format("%.2f", g[2]));

        this.linearAccAbs.setText(String.format("%.2f", getVectorLength(linear_acceleration)));
        this.linearAcc[0].setText(String.format("%.2f", linear_acceleration[0]));
        this.linearAcc[1].setText(String.format("%.2f", linear_acceleration[1]));
        this.linearAcc[2].setText(String.format("%.2f", linear_acceleration[2]));

        Log.d(TAG, "");
        Log.d(TAG, LINE);
        for (int i = 0, iLen = 3; i < iLen; i++) {
            Log.d(TAG, String.format("[%6d] SensorEvent [%d] = %f", eventIndex, i, event.values[0]));
        }
        Log.d(TAG, LINE);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

}