// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.ArmControlls;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.armSub;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class armMotorCommand extends Command {
  armSub armSubsystem;
  boolean move;
  boolean spin;
  boolean down;
  /** Creates a new armMotorCommand. */
  public armMotorCommand(armSub armSubSystem, boolean move, boolean spin, boolean down) {
    this.armSubsystem = armSubSystem;
    addRequirements(armSubsystem);
    // Use addRequirements() here to declare subsystem dependencies.
  }
//POSSIBLE BREAKING POINT ========= POSSIBLE BREAKING POINT ========== 
// the if statements could mess with it
  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if (move == true) {
      armSubsystem.moveArm(0.5);
  }
  if (spin == true) {
    if (down == false){
    armSubsystem.spinArm(0.5);
    }
    if (down == true) {
      armSubsystem.spinArm(-0.5);
    }
  }
}
 // POSSIBLE BREAKING POINT ========= POSSIBLE BREAKING POINT ========== POSSIBLE BREAKING POINT ========= POSSIBLE BREAKING POINT ========== 
  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (move == true) {
      armSubsystem.moveArm(0.5);
  }
  if (spin == true) {
    if (down == false){
    armSubsystem.spinArm(0.5);
    }
    if (down == true) {
      armSubsystem.spinArm(-0.5);
    }
  }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    armSubsystem.stopArm();
    armSubsystem.spinArm(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
