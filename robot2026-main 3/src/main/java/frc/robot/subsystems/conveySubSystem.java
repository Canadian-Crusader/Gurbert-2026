// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import static frc.robot.Constants.DriveConstants.*;
import static frc.robot.Constants.IntakeConstants.*;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class conveySubSystem extends SubsystemBase {
  SparkMax bedMotor = new SparkMax(bedMotorChannel, brushless);
 


  /** Creates a new conveySubSystem. */
  public conveySubSystem() {
    bedMotor.setInverted(true); // change before final ================================ change before final
    
 
  }
// Change after test to check the ideal speed for the motors ======================================= change before final
  public void setSpeed(double speed) { // ERROR HERE IDK HOW TO PUT VOLT TO SET TO 0 ON EVERYTHING, CHANGE BEFORE FINAL ======
    bedMotor.set(speed); // this is a placeholder change before final =============================== change before final
   
  }

  public void stop() { // this can coast but I'm unsure if we need it too
    bedMotor.set(0); // this is a placeholder, change before final ================================ change before final
     
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
