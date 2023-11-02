package com.pose;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.EditText;

public class GyroListener extends ComSensorEventListener {

    int eventIndex = 0;

    final float NS2S = 1.0f / 1000000000.0f;
    final float EPSILON = 1e-6f;
    final float[] deltaRotationVector = new float[4];
    float timestamp;

    EditText angularVelocityAbs;
    EditText angularVelocity[] = new EditText[3];

    public GyroListener(MainActivity activity) {
        this.angularVelocityAbs = activity.findViewById(R.id.angular_velocity_abs);
        this.angularVelocity[0] = activity.findViewById(R.id.angular_velocity_00);
        this.angularVelocity[1] = activity.findViewById(R.id.angular_velocity_01);
        this.angularVelocity[2] = activity.findViewById(R.id.angular_velocity_02);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        this.eventIndex++;

        // This timestep's delta rotation to be multiplied by the current rotation
        // after computing it from the gyro sample data.
        if (timestamp != 0) {
            final float dT = (event.timestamp - timestamp) * NS2S;

            // Axis of the rotation sample, not normalized yet.
            float [] w = event.values ;

            float axisX = event.values[0];
            float axisY = event.values[1];
            float axisZ = event.values[2];

            // Calculate the angular speed of the sample
            double omegaMagnitude = Math.sqrt(axisX*axisX + axisY*axisY + axisZ*axisZ);

            // Normalize the rotation vector if it's big enough to get the axis
            // (that is, EPSILON should represent your maximum allowable margin of error)
            if (omegaMagnitude > EPSILON) {
                axisX /= omegaMagnitude;
                axisY /= omegaMagnitude;
                axisZ /= omegaMagnitude;
            }

            // Integrate around this axis with the angular speed by the timestep
            // in order to get a delta rotation from this sample over the timestep
            // We will convert this axis-angle representation of the delta rotation
            // into a quaternion before turning it into the rotation matrix.
            double thetaOverTwo = omegaMagnitude * dT / 2.0f;
            double sinThetaOverTwo = Math.sin(thetaOverTwo);
            double cosThetaOverTwo = Math.cos(thetaOverTwo);

            deltaRotationVector[0] = (float) sinThetaOverTwo * axisX;
            deltaRotationVector[1] = (float) sinThetaOverTwo * axisY;
            deltaRotationVector[2] = (float) sinThetaOverTwo * axisZ;
            deltaRotationVector[3] = (float) cosThetaOverTwo;

            this.angularVelocityAbs.setText(String.format("%+4.1f", toDegree( omegaMagnitude ) ) );
            this.angularVelocity[0].setText(String.format("%+4.1f", toDegree( w[0] ) ) );
            this.angularVelocity[1].setText(String.format("%+4.1f", toDegree( w[1] ) ) );
            this.angularVelocity[2].setText(String.format("%+4.1f", toDegree( w[2] ) ) );
        }

        this.timestamp = event.timestamp;

        float[] deltaRotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, deltaRotationVector);
        // User code should concatenate the delta rotation we computed with the current rotation
        // in order to get the updated rotation.
        // rotationCurrent = rotationCurrent * deltaRotationMatrix;

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