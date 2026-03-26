// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Shooter;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.DriveConstants.*;
import static frc.robot.Constants.ShooterConstants.*;
import static frc.robot.Constants.TurretConstants.*;

public class turretTurnSub extends SubsystemBase {

    SparkMax turntableMotor = new SparkMax(turntableMotorChannel, brushless);
    private final RelativeEncoder encoder;

    public turretTurnSub() {
        encoder = turntableMotor.getEncoder();

        SparkMaxConfig config = new SparkMaxConfig();
        config.smartCurrentLimit(maxCurrent);
        config.idleMode(brakeMode);         // brake so the turret holds position when stopped
        config.voltageCompensation(nominalVoltage);

        // Convert motor rotations → output-shaft degrees.
        // getPosition() will return degrees directly after this is applied.
        // Formula: 1 motor rotation * (1 / GEAR_RATIO) * 360° = degrees per motor rotation
        config.encoder.positionConversionFactor(360.0 / GEAR_RATIO);

        turntableMotor.configure(config, noReset, persist);

        // Zero the encoder assuming the turret starts pointing straight forward.
        // If the robot powers on with the turret off-center, call zeroEncoder() from a button.
        encoder.setPosition(0.0);
    }

    /**
     * Turns the turret left (negative direction).
     * Automatically stops at LEFT_LIMIT_DEG to protect cables.
     *
     * @param speed positive value, 0.0 – 1.0
     */
    public void turnLeft(double speed) {
        if (getPositionDegrees() <= -LEFT_LIMIT_DEG) {
            turntableMotor.stopMotor();
            return;
        }
        turntableMotor.set(-Math.abs(speed));
    }

    /**
     * Turns the turret right (positive direction).
     * Automatically stops at RIGHT_LIMIT_DEG to protect cables.
     *
     * @param speed positive value, 0.0 – 1.0
     */
    public void turnRight(double speed) {
        if (getPositionDegrees() >= RIGHT_LIMIT_DEG) {
            turntableMotor.stopMotor();
            return;
        }
        turntableMotor.set(Math.abs(speed));
    }

    public void stop() {
        turntableMotor.stopMotor();
    }

    /**
     * Returns the turret's current angle in degrees.
     * 0 = forward, positive = right, negative = left.
     */
    public double getPositionDegrees() {
        return encoder.getPosition();
    }

    /**
     * Zeros the encoder at the current position.
     * Call this when the turret is physically pointing straight forward.
     */
    public void zeroEncoder() {
        encoder.setPosition(0.0);
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Turret/PositionDeg",   getPositionDegrees());
        SmartDashboard.putBoolean("Turret/AtRightLimit", getPositionDegrees() >= RIGHT_LIMIT_DEG);
        SmartDashboard.putBoolean("Turret/AtLeftLimit",  getPositionDegrees() <= -LEFT_LIMIT_DEG);
    }
}
