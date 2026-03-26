// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

public final class Constants {

// Drive ================ Drive ================ Drive ================ Drive ================ Drive ================
public static class DriveConstants {
    public static final int leftForwardMotorChannel  = 1;
    public static final int leftRearMotorChannel     = 2;
    public static final int rightForwardMotorChannel = 3;
    public static final int rightRearMotorChannel    = 4;

    public static final SparkLowLevel.MotorType brushless = SparkLowLevel.MotorType.kBrushless;
    public static final ResetMode noReset = ResetMode.kNoResetSafeParameters;
    public static final PersistMode persist = PersistMode.kPersistParameters;
    public static final IdleMode idleMode  = IdleMode.kCoast;
    public static final IdleMode brakeMode = IdleMode.kBrake;

    public static final double slewLimits     = 3.0;
    public static final int    maxCurrent    = 40;
    public static final double nominalVoltage = 12.0;

    // =================== TUNE THESE (odometry) ===================
    // Wheel diameter including tread — measure with a ruler (meters)
    public static final double WHEEL_DIAMETER_METERS = 0.1016; // 4-inch wheels <<< MEASURE

    // Drive gear ratio — motor rotations per one wheel rotation
    public static final double DRIVE_GEAR_RATIO      = 8.45;   // <<< UPDATE when confirmed

    // Center-to-center distance between left and right wheels (meters)
    public static final double TRACK_WIDTH_METERS    = 0.55;   // <<< MEASURE

    // Derived: motor rotations → wheel travel in meters (do not edit directly)
    public static final double DRIVE_POS_FACTOR =
        (Math.PI * WHEEL_DIAMETER_METERS) / DRIVE_GEAR_RATIO;
    // ===============================================================

    public static SparkMaxConfig getBasicMotorConfig() {
        SparkMaxConfig cfg = new SparkMaxConfig();
        cfg.smartCurrentLimit(maxCurrent);
        cfg.idleMode(idleMode);
        cfg.voltageCompensation(nominalVoltage);
        return cfg;
    }
}

// Arm ================ Arm ================ Arm ================ Arm ================ Arm ================
public static class ArmConstants {
    public static final int armMoveMotorChannel = 11; // tbd
    public static final int armMotorChannel     = 15; // tbd
}

// Intake ================ Intake ================ Intake ================ Intake ================
public static class IntakeConstants {
    public static final int intakeMotorChannel  = 10; // tbd
    public static final int bedMotorChannel     = 18; // tbd
    public static final int backendMotorChannel = 21; // tbd
}

// Shooter ================ Shooter ================ Shooter ================ Shooter ================
public static class ShooterConstants {
    // Motor CAN IDs
    public static final int shooterMotorChannel   = 30; // tbd
    public static final int tunnelMotorChannel    = 32; // tbd
    public static final int turntableMotorChannel = 26; // tbd

    // =================== TUNE THESE ===================
    // Flywheel speed for manual shooting (0.0 – 1.0)
    public static final double SHOOTER_SPEED  = 0.8;  // <<< TUNE

    // Tunnel (feeder) speed (0.0 – 1.0)
    public static final double TUNNEL_SPEED   = 0.8;  // <<< TUNE

    // Conveyor belt speed (0.0 – 1.0)
    public static final double CONVEY_SPEED   = 0.7;  // <<< TUNE

    // Flywheel ramp rate — seconds from 0 to full speed (reduces current spike)
    public static final double RAMP_RATE_SEC  = 0.5;  // <<< TUNE
    // ===================================================
}

// Turret ================ Turret ================ Turret ================ Turret ================
public static class TurretConstants {
    // =================== TUNE THESE ===================

    // Gear ratio: motor rotations per one full turret rotation
    // ~8.45:1 estimated — measure on the robot and update before competition
    public static final double GEAR_RATIO = 8.45; // <<< UPDATE when confirmed

    // Soft limits in degrees from center-forward (0°).
    // Positive = right, negative = left.
    // DO NOT exceed physical cable travel or you will damage the wiring.
    public static final double RIGHT_LIMIT_DEG = 90.0;  // <<< cable limits right
    public static final double LEFT_LIMIT_DEG  = 200.0; // <<< cable limits left

    // Manual turret speed when using L1/R1 (0.0 – 1.0)
    public static final double MANUAL_SPEED = 0.5; // <<< TUNE
    // ===================================================
}

// Operator ================ Operator ================ Operator ================
public static class OperatorConstants {
    public static final int driverControllerPort = 0;
}

// Vision ================ Vision ================ Vision ================ Vision ================
public static class VisionConstants {

    // Camera names — must match EXACTLY what is typed in the PhotonVision web UI (case-sensitive)
    public static final String SHOOTER_CAM_NAME = "shooter_cam"; // <<< FILL THIS IN
    public static final String DRIVER_CAM_NAME  = "driver_cam";  // <<< FILL THIS IN

    // =================== TUNE THESE ===================
    // Driver camera position on the robot (robot center/floor = origin).
    // +X = forward, +Y = left, +Z = up.  Angles in radians.
    // These MUST be measured on the physical robot for accurate pose estimation.
    public static final double DRIVER_CAM_X     =  0.20; // meters forward from robot center <<< MEASURE
    public static final double DRIVER_CAM_Y     =  0.00; // meters left from robot center    <<< MEASURE
    public static final double DRIVER_CAM_Z     =  0.50; // meters up from floor             <<< MEASURE
    public static final double DRIVER_CAM_ROLL  =  0.00; // radians
    public static final double DRIVER_CAM_PITCH =  0.00; // radians (tilt up = positive)     <<< MEASURE
    public static final double DRIVER_CAM_YAW   =  0.00; // radians (0 = facing forward)

    // Auto-aim PID — tune AIM_KP first, leave AIM_KI at 0 until the turret is stable.
    // Oscillates (wobbles)?  → lower AIM_KP
    // Too slow to reach target? → raise AIM_KP
    // Overshoots past target?  → raise AIM_KD
    public static final double AIM_KP                = 0.03;  // <<< TUNE
    public static final double AIM_KI                = 0.0;
    public static final double AIM_KD                = 0.001; // <<< TUNE
    public static final double AIM_TOLERANCE_DEGREES = 2.0;   // "on-target" window in degrees

    // Shooter speed at two known distances — everything between is linearly interpolated.
    // Step 1: shoot from CLOSE_RANGE_METERS, raise/lower CLOSE_RANGE_SPEED until it lands.
    // Step 2: shoot from FAR_RANGE_METERS,   raise/lower FAR_RANGE_SPEED  until it lands.
    public static final double CLOSE_RANGE_METERS = 1.5; // <<< TUNE (meters)
    public static final double FAR_RANGE_METERS   = 5.0; // <<< TUNE (meters)
    public static final double CLOSE_RANGE_SPEED  = 0.5; // <<< TUNE (0.0 – 1.0)
    public static final double FAR_RANGE_SPEED    = 1.0; // <<< TUNE (0.0 – 1.0)
    // ===================================================
}

}
