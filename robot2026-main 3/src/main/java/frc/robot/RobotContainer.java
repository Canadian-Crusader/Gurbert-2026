// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.Autos;
import frc.robot.commands.ExampleCommand;
import frc.robot.commands.ShootingControlls.AutoAimCommand;
import frc.robot.commands.ShootingControlls.ConveyCommand;
import frc.robot.commands.ShootingControlls.VisionShootCommand;
import frc.robot.commands.ShootingControlls.turnTableCommand;
import frc.robot.commands.ArmControlls.armCollectCommand;
import frc.robot.commands.ArmControlls.armDownCommand;
import frc.robot.commands.ArmControlls.armUpCommand;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.VisionSubsystem;
import frc.robot.subsystems.armSub;
import frc.robot.subsystems.conveySubSystem;
import frc.robot.subsystems.Shooter.turretShootSub;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Shooter.turretTurnSub;

import edu.wpi.first.wpilibj.PS5Controller;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class RobotContainer {

  // Subsystems — VisionSubsystem must be declared before Drive (passed into Drive constructor)
  private final ExampleSubsystem m_exampleSubsystem   = new ExampleSubsystem();
  private final VisionSubsystem  visionSubsystem      = new VisionSubsystem();
  private final Drive            drive                = new Drive(visionSubsystem);
  private final turretShootSub   turretShootSubSystem = new turretShootSub();
  private final conveySubSystem  conveySubSystem      = new conveySubSystem();
  private final armSub           armSub               = new armSub();
  private final turretTurnSub    turretTurnSub        = new turretTurnSub();

  // Kept as a field so toggleOnTrue can track the same instance across presses
  private final AutoAimCommand autoAimCommand = new AutoAimCommand(turretTurnSub, visionSubsystem);

  private final PS5Controller controller =
      new PS5Controller(OperatorConstants.driverControllerPort);

  public RobotContainer() {
    configureBindings();

    drive.setDefaultCommand(
        new RunCommand(
            () -> drive.arcadeDrive(
                -controller.getRightX(),
                 controller.getLeftY()
            ),
            drive
        )
    );
  }

  // ALL THE BUTTONS ARE DEFINED HERE ================================================
  private void configureBindings() {

    // TRIGGERS ========================================================================
    // R2 (held): Shoot + Convey
    // VisionShootCommand sets flywheel speed from camera distance.
    // When no target is visible it spins to full power so you're ready to shoot.
    new Trigger(() -> controller.getR2Axis() > 0.2)
        .whileTrue(new ParallelCommandGroup(
            new VisionShootCommand(turretShootSubSystem, visionSubsystem),
            new ConveyCommand(conveySubSystem)
        ));

    // L2 (held): Arm collect
    new Trigger(() -> controller.getL2Axis() > 0.2)
        .whileTrue(new armCollectCommand(armSub));

    // FACE BUTTONS ====================================================================
    // Triangle (toggle): Auto-aim — turret tracks the vision target while active.
    // Press once to lock on, press again to stop tracking.
    new Trigger(controller::getTriangleButton)
        .toggleOnTrue(autoAimCommand);

    // BUMPERS =========================================================================
    // R1 / L1 (held): Manual turret rotation.
    // Blocked when auto-aim is active — vision takes priority over manual turn.
    new Trigger(controller::getR1Button)
        .and(() -> !autoAimCommand.isScheduled())
        .whileTrue(new turnTableCommand(turretTurnSub, false));

    new Trigger(controller::getL1Button)
        .and(() -> !autoAimCommand.isScheduled())
        .whileTrue(new turnTableCommand(turretTurnSub, true));

    // D-PAD ===========================================================================
    // D-pad Up   (POV 0):   Raise arm
    // D-pad Down (POV 180): Lower arm
    new Trigger(() -> controller.getPOV() == 0)
        .whileTrue(new armUpCommand(armSub));

    new Trigger(() -> controller.getPOV() == 180)
        .whileTrue(new armDownCommand(armSub));

    // EXAMPLE =========================================================================
    new Trigger(m_exampleSubsystem::exampleCondition)
        .toggleOnTrue(new ExampleCommand(m_exampleSubsystem));
  }

  public Command getAutonomousCommand() {
    return Autos.exampleAuto(m_exampleSubsystem);
  }
}
