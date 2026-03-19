// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.ArmConstants.*;
import static frc.robot.Constants.DriveConstants.*;


public class armSub extends SubsystemBase {

  SparkMax armMoveMotor = new SparkMax(armMoveMotorChannel, brushless);
  SparkMax armMotor = new SparkMax(armMotorChannel, brushless);


  /** Creates a new armSub. */
  public armSub() {}

// THIS IS FOR THE ARM LOWERING ======== THIS IS FOR THE ARM LOWERING ======== THIS IS FOR THE ARM LOWERING ======== THIS IS FOR THE ARM LOWERING

  public void moveArm(double speed) {
    armMoveMotor.set(speed);
  }
  public void stopArm() {
    armMoveMotor.stopMotor();
  }

  // This is for the arm spinning ======== This is for the arm spinning ======== This is for the arm spinning ======== This is for the arm spinning
 
  public void spinArm(double speed) {
    armMotor.set(speed);
  }

  public void stopSpin() {
    armMotor.set(0);
  }


  

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
