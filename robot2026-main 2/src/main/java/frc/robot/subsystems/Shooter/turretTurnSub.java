// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.DriveConstants.*;
import static frc.robot.Constants.ShooterConstants.*;

import com.revrobotics.spark.SparkMax;

public class turretTurnSub extends SubsystemBase {
  SparkMax turntableMotor = new SparkMax(turntableMotorChannel, brushless);


  /** Creates a new turretTurnSub. */
  public turretTurnSub() {

  }
  //public void turn(double speed) {
  //  turntableMotor.set(speed);
  //}

  public void turnLeft(double speed) {
    turntableMotor.set(-speed);
  }

  public void turnRight(double speed) {
    turntableMotor.set(speed);
  }

  public void stop() {
    turntableMotor.stopMotor();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
