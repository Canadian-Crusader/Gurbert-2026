// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.ShootingControlls;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.conveySubSystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class ConveyCommand extends Command {
  private final conveySubSystem c;
  /** Creates a new ConveyOnCommand. */
   public ConveyCommand(conveySubSystem c) {
    this.c = c;
    addRequirements(c);
    // Use addRequirements() here to declare subsystem dependencies.
  }
  /** Creates a new ConveyOnCommand. */
  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    c.setSpeed(0.7);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    c.setSpeed(0.7);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    c.setSpeed(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }

}
