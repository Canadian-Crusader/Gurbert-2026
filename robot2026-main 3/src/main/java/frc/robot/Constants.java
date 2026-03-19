// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;


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
    
    public static final double slewLimits = 3;
    public static final int maxCurrent = 40;
    public static final double nominalVoltage = 12;
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


}
