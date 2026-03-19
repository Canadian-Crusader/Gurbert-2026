// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.DriveConstants.*;
import static frc.robot.Constants.ShooterConstants.*;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;


public class turretShootSub extends SubsystemBase {
  SparkMax turretMotor = new SparkMax(shooterMotorChannel, brushless);
  SparkMax tunnelMotor = new SparkMax(tunnelMotorChannel, brushless);
  // can do manually but add the brake
  // tell to set the id to brake it
  
  /** Creates a new turretShootSub. */


  public turretShootSub() {}

  public void prep(double turret) {
    turretMotor.set(turret);
  }
  
  public double getSpeed() {
    return turretMotor.get();
  }

  public void startTunnel(double Tunnel) {
    tunnelMotor.set(Tunnel);
  }

  public void stopShoot() {
    turretMotor.stopMotor();
    tunnelMotor.stopMotor();
  } // velocity control
    // 
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
