package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.conveySubSystem;
import frc.robot.subsystems.Shooter.turretShootSub;
import static frc.robot.Constants.ShooterConstants.*;

public final class Autos {

    // =================== TUNE THESE ===================
    // How fast to drive backward (0.0 – 1.0, positive = backward here)
    private static final double AUTO_DRIVE_SPEED   = 0.5;  // <<< TUNE
    // How long to drive backward in seconds
    private static final double AUTO_DRIVE_SECONDS = 1.0;  // <<< TUNE
    // How long to run the shooter before feeding (flywheel spin-up time)
    private static final double AUTO_SPINUP_SECONDS = 1.5; // <<< TUNE
    // How long to feed the game piece into the shooter
    private static final double AUTO_SHOOT_SECONDS  = 4; // <<< TUNE
    // ===================================================

    /**
     * Drive backward, then shoot the preloaded game piece.
     *
     * Sequence:
     *   1. Drive backward for AUTO_DRIVE_SECONDS
     *   2. Stop driving
     *   3. Spin up flywheel for AUTO_SPINUP_SECONDS
     *   4. Feed tunnel + convey for AUTO_SHOOT_SECONDS
     *   5. Stop everything
     */
    public static Command driveBackAndShoot(Drive drive, turretShootSub shooter, conveySubSystem conveyor) {
        return Commands.sequence(
            // Step 1: Drive backward
            Commands.run(() -> drive.arcadeDrive(-AUTO_DRIVE_SPEED, 0.0), drive)
                .withTimeout(AUTO_DRIVE_SECONDS),

            // Step 2: Stop driving
            Commands.runOnce(() -> drive.arcadeDrive(0.0, 0.0), drive),

            // Step 3: Spin up flywheel
            Commands.run(() -> shooter.prep(SHOOTER_SPEED), shooter)
                .withTimeout(AUTO_SPINUP_SECONDS),

            // Step 4: Feed once flywheel is at speed
            Commands.run(() -> {
                shooter.prep(SHOOTER_SPEED);
                conveyor.setSpeed(CONVEY_SPEED);
                if (shooter.getSpeed() >= SHOOTER_SPEED * 0.95) {
                    shooter.startTunnel(TUNNEL_SPEED);
                }
            }, shooter, conveyor).withTimeout(AUTO_SHOOT_SECONDS),

            // Step 5: Stop everything
            Commands.runOnce(() -> {
                shooter.stopShoot();
                conveyor.stop();
            }, shooter, conveyor)
        );
    }

    private Autos() {
        throw new UnsupportedOperationException("This is a utility class!");
    }
}
