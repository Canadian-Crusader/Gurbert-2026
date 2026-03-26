// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.ShootingControlls;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Shooter.turretTurnSub;
import static frc.robot.Constants.TurretConstants.*;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class turnTableCommand extends Command {
  
  turretTurnSub turnSub;
  boolean turnLeft;
  
  /** Creates a new turnTableCommand. */
  public turnTableCommand(turretTurnSub turnSub, boolean turnLeft) {
    this.turnSub = turnSub;
    this.turnLeft = turnLeft;
    addRequirements(turnSub);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    turnSub.stop();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (turnLeft) {
      turnSub.turnLeft(MANUAL_SPEED);
    } else {
      turnSub.turnRight(MANUAL_SPEED);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    turnSub.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
