// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.ShootingControlls;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.subsystems.Shooter.turretShootSub;


/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class ShootCommand extends Command {
turretShootSub turretShootSubSystem;
public static boolean shoot;
  /** Creates a new ShootCommand. */
  public ShootCommand(turretShootSub turretShootSubSystem, boolean shoot) {
    this.turretShootSubSystem = turretShootSubSystem;
    this.shoot = shoot;
    addRequirements(turretShootSubSystem);

    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
 public void initialize() {
    turretShootSubSystem.prep(0.8);
   
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    if (turretShootSubSystem.getSpeed() > 0.75) {
      turretShootSubSystem.startTunnel(0.8);
      
    }

  } 

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    turretShootSubSystem.stopShoot();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
