package frc.robot.commands.ShootingControlls;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Shooter.turretShootSub;
import frc.robot.subsystems.VisionSubsystem;

public class VisionShootCommand extends Command {

    private final turretShootSub shooter;
    private final VisionSubsystem vision;

    public VisionShootCommand(turretShootSub shooter, VisionSubsystem vision) {
        this.shooter = shooter;
        this.vision = vision;
        addRequirements(shooter);
    }

    @Override
    public void execute() {
        double targetSpeed;

        if (vision.hasTarget()) {
            // Calculate speed based on distance to target
            targetSpeed = vision.getShooterSpeed(vision.getDistance());
        } else {
            // No target visible — spin up to max so we're ready when target appears
            targetSpeed = 1.0;
        }

        shooter.prep(targetSpeed);

        // Wait for the wheel to get close to target speed before feeding balls
        // getSpeed() returns the current commanded output which ramps up via openLoopRampRate
        if (shooter.getSpeed() >= targetSpeed * 0.9) {
            shooter.startTunnel(0.8);
        }
    }

    @Override
    public void end(boolean interrupted) {
        shooter.stopShoot();
    }

    @Override
    public boolean isFinished() {
        // Runs until button is released
        return false;
    }
}
