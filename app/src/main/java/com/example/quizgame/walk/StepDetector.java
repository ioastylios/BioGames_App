package com.example.quizgame.walk;

public class StepDetector {
    private static final int ACCEL_RING_SIZE = 50;
    private static final int VEL_RING_SIZE = 10;

    // change this threshold according to your sensitivity preferences
    private static final float STEP_THRESHOLD = 20f;
    private static final int STEP_DELAY_NS = 400000000;
    private static final float JUMP_THRESHOLD = 100f;
    private static final int JUMP_DELAY_NS = 500000000;
    private static final float SQUAT_THRESHOLD = 60f;
    private static final int SQUAT_DELAY_NS = 600000000;

    private int accelRingCounter = 0;
    private float[] accelRingX = new float[ACCEL_RING_SIZE];
    private float[] accelRingY = new float[ACCEL_RING_SIZE];
    private float[] accelRingZ = new float[ACCEL_RING_SIZE];
    private int velRingCounter = 0;
    private float[] velRing = new float[VEL_RING_SIZE];
    private long lastStepTimeNs = 0;
    private float oldVelocityEstimate = 0;

    private StepListener listener;

    public void registerListener(StepListener listener) {
        this.listener = listener;
    }


    public void updateAccel(long timeNs, float x, float y, float z,String action) {
        float[] currentAccel = new float[3];
        currentAccel[0] = x;
        currentAccel[1] = y;
        currentAccel[2] = z;
        // First step is to update our guess of where the global z vector is.
        accelRingCounter++;
        accelRingX[accelRingCounter % ACCEL_RING_SIZE] = currentAccel[0];
        accelRingY[accelRingCounter % ACCEL_RING_SIZE] = currentAccel[1];
        accelRingZ[accelRingCounter % ACCEL_RING_SIZE] = currentAccel[2];

        float[] worldZ = new float[3];
        worldZ[0] = SensorFilter.sum(accelRingX) / Math.min(accelRingCounter, ACCEL_RING_SIZE);
        worldZ[1] = SensorFilter.sum(accelRingY) / Math.min(accelRingCounter, ACCEL_RING_SIZE);
        worldZ[2] = SensorFilter.sum(accelRingZ) / Math.min(accelRingCounter, ACCEL_RING_SIZE);

        float normalization_factor = SensorFilter.norm(worldZ);

        worldZ[0] = worldZ[0] / normalization_factor;
        worldZ[1] = worldZ[1] / normalization_factor;
        worldZ[2] = worldZ[2] / normalization_factor;

        float currentZ = SensorFilter.dot(worldZ, currentAccel) - normalization_factor;
        velRingCounter++;
        velRing[velRingCounter % VEL_RING_SIZE] = currentZ;

        float velocityEstimate = SensorFilter.sum(velRing);

        if (velocityEstimate > STEP_THRESHOLD && oldVelocityEstimate <= STEP_THRESHOLD
                && (timeNs - lastStepTimeNs > STEP_DELAY_NS) && action.equals("walk1") ) {
            listener.step(timeNs);
            lastStepTimeNs = timeNs;
        }else if (velocityEstimate > JUMP_THRESHOLD && oldVelocityEstimate <= JUMP_THRESHOLD
                && (timeNs - lastStepTimeNs > JUMP_DELAY_NS) && action.equals("walk2") ) {
            listener.step(timeNs);
            lastStepTimeNs = timeNs;
        }
        else if (velocityEstimate > SQUAT_THRESHOLD && oldVelocityEstimate <= SQUAT_THRESHOLD
                && (timeNs - lastStepTimeNs > SQUAT_DELAY_NS) && (action.equals("walk3") || action.equals("walk4") )) {
            listener.step(timeNs);
            lastStepTimeNs = timeNs;
        }
        oldVelocityEstimate = velocityEstimate;
    }
}
