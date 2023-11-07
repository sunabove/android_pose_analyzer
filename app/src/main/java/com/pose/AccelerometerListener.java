package com.pose;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.util.Log;
import android.widget.EditText;

public class AccelerometerListener extends ComSensorListener {

    int eventIndex = 0;

    EditText gravity_abs;
    EditText gravityView[] = new EditText[3];

    EditText linearAccAbs;
    EditText linearAccView[] = new EditText[3];

    public AccelerometerListener(MainActivity activity) {
        this.gravity_abs = activity.findViewById(R.id.gravity_abs);
        this.gravityView[0] = activity.findViewById(R.id.gravity_00);
        this.gravityView[1] = activity.findViewById(R.id.gravity_01);
        this.gravityView[2] = activity.findViewById(R.id.gravity_02);

        this.linearAccAbs = activity.findViewById(R.id.linear_acc_abs);
        this.linearAccView[0] = activity.findViewById(R.id.linear_acc_00);
        this.linearAccView[1] = activity.findViewById(R.id.linear_acc_01);
        this.linearAccView[2] = activity.findViewById(R.id.linear_acc_02);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // alpha is calculated as t / (t + dT),
        // where t is the low-pass filter's time-constant and
        // dT is the event delivery rate.

        this.eventIndex++;

        final float alpha = 0.8f;

        float gravity[] = new float[3];
        float linear_accels[] = new float[3];

        acceleration = event.values.clone();

        // Isolate the force of gravity with the low-pass filter.
        gravity[0] = alpha * gravity[0] + (1 - alpha) * acceleration[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * acceleration[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * acceleration[2];

        // Remove the gravity contribution with the high-pass filter.
        linear_accels[0] = acceleration[0] - gravity[0];
        linear_accels[1] = acceleration[1] - gravity[1];
        linear_accels[2] = acceleration[2] - gravity[2];

        this.gravity_abs.setText(String.format("%.2f", getVectorLength(acceleration)));
        this.gravityView[0].setText(String.format("%.2f", acceleration[0]));
        this.gravityView[1].setText(String.format("%.2f", acceleration[1]));
        this.gravityView[2].setText(String.format("%.2f", acceleration[2]));

        this.linearAccAbs.setText(String.format("%.2f", getVectorLength(linear_accels)));
        this.linearAccView[0].setText(String.format("%.2f", linear_accels[0]));
        this.linearAccView[1].setText(String.format("%.2f", linear_accels[1]));
        this.linearAccView[2].setText(String.format("%.2f", linear_accels[2]));

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