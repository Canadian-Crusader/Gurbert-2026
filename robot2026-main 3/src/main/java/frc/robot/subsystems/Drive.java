package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.DriveConstants.*;

public class Drive extends SubsystemBase {

    // ── Motors ───────────────────────────────────────────────────────────────────
    SparkMax leftForwardMotor  = new SparkMax(leftForwardMotorChannel,  brushless);
    SparkMax leftRearMotor     = new SparkMax(leftRearMotorChannel,     brushless);
    SparkMax rightForwardMotor = new SparkMax(rightForwardMotorChannel, brushless);
    SparkMax rightRearMotor    = new SparkMax(rightRearMotorChannel,    brushless);

    private final DifferentialDrive drive =
        new DifferentialDrive(leftForwardMotor, rightForwardMotor);

    SlewRateLimiter forwardSlewLimits = new SlewRateLimiter(slewLimits);
    SlewRateLimiter turnSlewLimits    = new SlewRateLimiter(slewLimits);

    public Drive() {
        SparkMaxConfig baseDriveConfig = new SparkMaxConfig();
        baseDriveConfig.smartCurrentLimit(maxCurrent);
        baseDriveConfig.idleMode(idleMode);
        baseDriveConfig.voltageCompensation(nominalVoltage);

        SparkMaxConfig leftForwardConfig = new SparkMaxConfig();
        leftForwardConfig.apply(baseDriveConfig);
        leftForwardMotor.configure(leftForwardConfig, noReset, persist);

        SparkMaxConfig leftRearConfig = new SparkMaxConfig();
        leftRearConfig.apply(baseDriveConfig);
        leftRearConfig.follow(leftForwardMotor);
        leftRearMotor.configure(leftRearConfig, noReset, persist);

        SparkMaxConfig rightForwardConfig = new SparkMaxConfig();
        rightForwardConfig.apply(baseDriveConfig);
        rightForwardConfig.inverted(true);
        rightForwardMotor.configure(rightForwardConfig, noReset, persist);

        SparkMaxConfig rightRearConfig = new SparkMaxConfig();
        rightRearConfig.apply(baseDriveConfig);
        rightRearConfig.follow(rightForwardMotor);
        rightRearMotor.configure(rightRearConfig, noReset, persist);
    }

    public void arcadeDrive(double speed, double rotate) {

        // SOMEONE has controller drift
        speed  = MathUtil.applyDeadband(speed,  0.08);
        rotate = MathUtil.applyDeadband(rotate, 0.08);

        // squared inputs
        speed  = Math.copySign(speed  * speed,  speed);
        rotate = Math.copySign(rotate * rotate, rotate);

        // slew limiting
        speed  = forwardSlewLimits.calculate(speed);
        rotate = turnSlewLimits.calculate(rotate);

        drive.arcadeDrive(speed, rotate);
    }
}
