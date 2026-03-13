package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveConstants;

import static frc.robot.Constants.DriveConstants.*;


public class Drive extends SubsystemBase {

    SparkMax leftRearMotor = new SparkMax(leftRearMotorChannel, brushless);
    SparkMax rightForwardMotor = new SparkMax(rightForwardMotorChannel, brushless);
    SparkMax rightRearMotor = new SparkMax(rightRearMotorChannel, brushless);
    SparkMax leftForwardMotor = new SparkMax(leftForwardMotorChannel, brushless);
        
    private final DifferentialDrive drive = new DifferentialDrive(leftForwardMotor, rightForwardMotor);
    
    SlewRateLimiter forwardLimits = new SlewRateLimiter(DriveConstants.slewLimits);
    SlewRateLimiter turnLimits = new SlewRateLimiter(DriveConstants.slewLimits);


    // private final static Drive INSTANCE = new Drive();

    // public static Drive getInstance() {
    //     return INSTANCE;
    // }

    public Drive() {

        SparkMaxConfig baseConfig = new SparkMaxConfig();
        baseConfig.smartCurrentLimit(maxCurrent);
        baseConfig.idleMode(idleMode);
        baseConfig.voltageCompensation(nominalVoltage);

        // yes i can just apply the baseconfig straight to the motor, no i will not do it
        SparkMaxConfig leftForwardConfig = new SparkMaxConfig();
        leftForwardConfig.apply(baseConfig);
        leftForwardMotor.configure(leftForwardConfig, noReset, persist);

        SparkMaxConfig leftRearConfig = new SparkMaxConfig();
        leftRearConfig.apply(baseConfig);
        leftRearConfig.follow(leftForwardMotor);
        leftRearMotor.configure(leftRearConfig, noReset, persist);

        SparkMaxConfig rightForwardConfig = new SparkMaxConfig();
        rightForwardConfig.apply(baseConfig);
        rightForwardConfig.inverted(true); 
        rightForwardMotor.configure(rightForwardConfig, noReset, persist);

        SparkMaxConfig rightRearConfig = new SparkMaxConfig();
        rightRearConfig.apply(baseConfig);
        rightRearConfig.follow(rightForwardMotor);
        rightRearMotor.configure(rightRearConfig, noReset, persist);

    }

    public void arcadeDrive(double speed, double rotate){

        // SOMEONE has controller drift
        // apply deadbands
        speed = MathUtil.applyDeadband(speed, 0.08);
        rotate = MathUtil.applyDeadband(rotate, 0.08);

        // squared inputs
        speed = Math.copySign(speed * speed, speed);
        rotate = Math.copySign(rotate * rotate, rotate);

        // slew limiting or whatever that is the guide said so
        speed = forwardLimits.calculate(speed);
        rotate = turnLimits.calculate(rotate);

        drive.arcadeDrive(speed, rotate);

    }

}