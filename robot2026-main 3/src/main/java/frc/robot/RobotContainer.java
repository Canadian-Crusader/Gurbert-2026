// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.Autos;
import frc.robot.commands.ExampleCommand;
import frc.robot.commands.ShootingControlls.AutoAimCommand;
import frc.robot.commands.ShootingControlls.ConveyCommand;
import frc.robot.commands.ShootingControlls.ShootCommand;
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
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;


/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here... (Just Drive for command)
  private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();
  private final Drive drive = new Drive();
  private final turretShootSub turretShootSubSystem = new turretShootSub();
  private final conveySubSystem conveySubSystem = new conveySubSystem();
  private final armSub armSub = new armSub();
  private final turretTurnSub turretTurnSub = new turretTurnSub();
  private final VisionSubsystem visionSubsystem = new VisionSubsystem();

  // Replace with CommandPS4Controller or CommandJoystick if needed
  private final PS5Controller controller =
    new PS5Controller(OperatorConstants.driverControllerPort);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();

   // controller.R1.whileTrue(new ShootCommand(conveySubSystem, true));
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

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */




   // ALL THE BUTTONS ARE DEFINED HERE ============= ALL BUTTONS ARE DEFINED HERE ============= ALL BUTTONS ARE DEFINED HERE ============= ALL BUTTONS ARE DEFINED HERE
  private void configureBindings() {
    // Schedule `ExampleCommand` when `exampleCondition` changes to `true`
    // TRIGGERS ========= TRIGGERS ========== TRIGGERS ========= TRIGGERS ========== TRIGGERS ========= TRIGGERS ==========
    new Trigger(() -> controller.getR2Axis() > 0.2)
    .whileTrue(new ParallelCommandGroup( // fixxed this
        new ShootCommand(turretShootSubSystem, true),
        new ConveyCommand(conveySubSystem)
    ));
    new Trigger(() -> controller.getL2Axis() > 0.2)
     .whileTrue(new armCollectCommand(armSub));
    

    new Trigger(m_exampleSubsystem::exampleCondition)
        .toggleOnTrue(new ExampleCommand(m_exampleSubsystem));
// BUMPERS ======== BUMPERS ======== BUMPERS ======== BUMPERS ======== BUMPERS ======== BUMPERS ======== BUMPERS ===========
    // Triangle = auto aim turret + auto power shooter based on distance
    new Trigger(controller::getTriangleButton)
        .whileTrue(new ParallelCommandGroup(
            new AutoAimCommand(turretTurnSub, visionSubsystem),
            new VisionShootCommand(turretShootSubSystem, visionSubsystem)
        ));

    new Trigger (controller::getR1Button)
        .whileTrue(new turnTableCommand(turretTurnSub, false));

    new Trigger (controller::getL1Button)
        .whileTrue(new turnTableCommand(turretTurnSub, true));
    
// DPAD ============== DPAD ============== DPAD ============== DPAD ============== DPAD ============== DPAD ================ 
    //Create a trigger for the "Down" D-pad button (180 degrees)
   new Trigger (() -> controller.getPOV() == 1)
       .whileTrue(new armUpCommand(armSub));
    new Trigger(() -> controller.getPOV() == 180)
        .whileTrue(new armDownCommand(armSub));

    

    
    // Schedule `exampleMethodCommand` when the Xbox controller's B button is pressed,
    // cancelling on release.
    if(controller.getCrossButton()){

    }
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand(){
    // An example command will be run in autonomous
    // new SequentialCommandGroup( 

    //  new armDownCommand(armSub).withTimeout(1.0),
      
    //   new RunCommand (() -> drive.arcadeDrive(0.5,0), drive).withTimeout(5)
  //  );
  


    return Autos.exampleAuto(m_exampleSubsystem);
  }
}

