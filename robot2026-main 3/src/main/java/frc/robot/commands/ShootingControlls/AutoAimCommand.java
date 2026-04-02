package frc.robot.commands.ShootingControlls;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Shooter.turretTurnSub;
import frc.robot.subsystems.VisionSubsystem;
import static frc.robot.Constants.VisionConstants.*;

public class AutoAimCommand extends Command {

    private final turretTurnSub turret;
    private final VisionSubsystem vision;
    private final PIDController pid;

    public AutoAimCommand(turretTurnSub turret, VisionSubsystem vision) {
        this.turret = turret;
        this.vision = vision;
        this.pid = new PIDController(AIM_KP, AIM_KI, AIM_KD);
        pid.setTolerance(AIM_TOLERANCE_DEGREES);
        pid.setSetpoint(0.0); // we want yaw = 0 (target centered)
        addRequirements(turret);
    }

    @Override
    public void execute() {
        if (!vision.hasTarget()) {
            turret.stop();
            return;
        }

        // PID output: negative when target is right (yaw > 0), positive when target is left (yaw < 0)
        // We negate it so: target right → positive output → turn right, target left → negative → turn left
        double output = -pid.calculate(vision.getTurn());
        output = Math.max(-1.0, Math.min(1.0, output)); // clamp to motor range

        if (output > 0) {
            turret.turnRight(output);
        } else if (output < 0) {
            turret.turnLeft(-output); // turnLeft takes a positive speed
        } else {
            turret.stop();
        }
    }

    @Override
    public void end(boolean interrupted) {
        turret.stop();
    }

    @Override
    public boolean isFinished() {
        // Runs until button is released — the button binding (whileTrue) handles stopping
        return false;
    }
}
