// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Shooter;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.DriveConstants.*;
import static frc.robot.Constants.ShooterConstants.*;

public class turretShootSub extends SubsystemBase {

    SparkMax turretMotor = new SparkMax(shooterMotorChannel, brushless);
    SparkMax tunnelMotor = new SparkMax(tunnelMotorChannel, brushless);

    public turretShootSub() {
        // Flywheel motor — coast so it spins down freely; ramp rate limits current spike on spin-up
        SparkMaxConfig shooterConfig = new SparkMaxConfig();
        shooterConfig.smartCurrentLimit(maxCurrent);
        shooterConfig.idleMode(idleMode);            // coast
        shooterConfig.voltageCompensation(nominalVoltage);
        shooterConfig.openLoopRampRate(RAMP_RATE_SEC); // tune in ShooterConstants
        turretMotor.configure(shooterConfig, noReset, persist);

        // Tunnel (feeder) motor — brake so balls don't drift back when idle
        SparkMaxConfig tunnelConfig = new SparkMaxConfig();
        tunnelConfig.smartCurrentLimit(maxCurrent);
        tunnelConfig.idleMode(brakeMode);            // brake
        tunnelConfig.voltageCompensation(nominalVoltage);
        tunnelMotor.configure(tunnelConfig, noReset, persist);
        turretMotor.setInverted(true);
    }

    /** Spins the flywheel at the given speed (0.0 – 1.0). */
    public void prep(double speed) {
        turretMotor.set(speed);
    }

    /** Returns the last commanded flywheel output (0.0 – 1.0). */
    public double getSpeed() {
        return turretMotor.get();
    }

    /** Runs the tunnel/feeder motor at the given speed (0.0 – 1.0). */
    public void startTunnel(double speed) {
        tunnelMotor.set(speed);
    }

    /** Stops both the flywheel and the tunnel. */
    public void stopShoot() {
        turretMotor.stopMotor();
        tunnelMotor.stopMotor();
    }

    @Override
    public void periodic() {}
}
