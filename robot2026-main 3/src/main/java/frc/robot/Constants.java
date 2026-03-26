// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;


/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
// Drive ================ Drive ================ Drive ================ Drive ================ Drive ================ Drive =====

public static class DriveConstants {
    public static final int leftForwardMotorChannel = 1;
    public static final int leftRearMotorChannel = 2;
    public static final int rightForwardMotorChannel = 3;
    public static final int rightRearMotorChannel = 4;

    public static final SparkLowLevel.MotorType brushless = SparkLowLevel.MotorType.kBrushless;
    public static final ResetMode noReset = ResetMode.kNoResetSafeParameters;
    public static final PersistMode persist = PersistMode.kPersistParameters;
    public static final IdleMode idleMode = IdleMode.kCoast;
    public static final IdleMode brakeMode = IdleMode.kBrake;
    
    public static final double kTurretSlewRate = 1.0; 
    public static final double slewLimits = 3;
    public static final int maxCurrent = 40;
    public static final double nominalVoltage = 12;

    public static SparkMaxConfig getBasicMotorConfig() {
    SparkMaxConfig baseConfig = new SparkMaxConfig();
        baseConfig.smartCurrentLimit(maxCurrent);
        baseConfig.idleMode(idleMode);
        baseConfig.voltageCompensation(nominalVoltage);
        return baseConfig;
    }
}
// Arm ================ Arm ================ Arm ================ Arm ================ Arm ================ Arm =================
  public static class ArmConstants {
    public static final int armMoveMotorChannel = 11; // tbd
    public static final int armMotorChannel = 15; // tbd
    
  
  }

// Intake ================ Intake ================ Intake ================ Intake ================ Intake ================ Intake
  public static class IntakeConstants {
    public static final int intakeMotorChannel = 10; // tbd
    public static final int bedMotorChannel = 18; // tbd
    public static final int backendMotorChannel = 21; // tbd
    

  }
// Shooter ================ Shooter ================ Shooter ================ Shooter ================ Shooter ========== Shooter
  public static class ShooterConstants {
    public static final int shooterMotorChannel = 30; //   tbd
    public static final int tunnelMotorChannel = 32; //    tbd
    public static final int turntableMotorChannel = 26; // tbd
  }
// Operator Constants =============== Operator Constants =============== Operator Constants =============== Operator Constants ==
  public static class OperatorConstants {
    public static final int driverControllerPort = 0;
  }

// Vision ================ Vision ================ Vision ================ Vision ================ Vision ================ Vision
  public static class VisionConstants {

    // Camera names — must match exactly what is typed in the PhotonVision web UI (case sensitive)
    public static final String SHOOTER_CAM_NAME = "shooter_cam"; // <<< FILL THIS IN
    public static final String DRIVER_CAM_NAME  = "driver_cam";  // <<< FILL THIS IN

    // Auto-aim PID — tune AIM_KP first, leave AIM_KI at 0 until the turret is stable
    // If the turret oscillates (wobbles back and forth), lower AIM_KP
    // If the turret is too slow to reach the target, raise AIM_KP
    public static final double AIM_KP                = 0.03;  // <<< TUNE
    public static final double AIM_KI                = 0.0;
    public static final double AIM_KD                = 0.001; // <<< TUNE (reduces overshoot)
    public static final double AIM_TOLERANCE_DEGREES = 2.0;   // within this many degrees = "on target"

    // Shooter speed curve — measured at two distances, everything in between is interpolated
    // Step 1: shoot from CLOSE_RANGE_METERS, adjust CLOSE_RANGE_SPEED until the ball lands
    // Step 2: shoot from FAR_RANGE_METERS,   adjust FAR_RANGE_SPEED  until the ball lands
    public static final double CLOSE_RANGE_METERS = 1.5; // <<< TUNE (meters)
    public static final double FAR_RANGE_METERS   = 5.0; // <<< TUNE (meters)
    public static final double CLOSE_RANGE_SPEED  = 0.5; // <<< TUNE (0.0 to 1.0)
    public static final double FAR_RANGE_SPEED    = 1.0; // <<< TUNE (0.0 to 1.0)
  }


}
