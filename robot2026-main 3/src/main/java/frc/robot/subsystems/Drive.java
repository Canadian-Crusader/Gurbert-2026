package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.estimator.DifferentialDrivePoseEstimator;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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

    // ── Wheel encoders (left/right lead motors only — rear motors follow) ────────
    private final RelativeEncoder leftEncoder;
    private final RelativeEncoder rightEncoder;

    // ── Gyro ─────────────────────────────────────────────────────────────────────
    // Required by DifferentialDrivePoseEstimator for dead-reckoning between vision frames.
    // Swap this one line if you have a different gyro:
    //   NavX:     new com.kauailabs.navx.frc.AHRS(SPI.Port.kMXP)  — needs NavX vendordep
    //   Pigeon 2: new com.ctre.phoenix6.hardware.Pigeon2(canId)    — needs CTRE vendordep
    private final ADXRS450_Gyro gyro = new ADXRS450_Gyro();

    // ── Pose estimator ───────────────────────────────────────────────────────────
    private final DifferentialDriveKinematics kinematics =
        new DifferentialDriveKinematics(TRACK_WIDTH_METERS);

    private final DifferentialDrivePoseEstimator poseEstimator;

    // Vision subsystem reference — used in periodic() to pull AprilTag corrections
    private final VisionSubsystem vision;

    public Drive(VisionSubsystem vision) {
        this.vision = vision;

        SparkMaxConfig baseDriveConfig = new SparkMaxConfig();
        baseDriveConfig.smartCurrentLimit(maxCurrent);
        baseDriveConfig.idleMode(idleMode);
        baseDriveConfig.voltageCompensation(nominalVoltage);
        // Convert motor rotations → wheel travel in meters for odometry
        baseDriveConfig.encoder.positionConversionFactor(DRIVE_POS_FACTOR);

        // yes i can just apply the baseconfig straight to the motor, no i will not do it
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

        // Grab encoder objects after configuration
        leftEncoder  = leftForwardMotor.getEncoder();
        rightEncoder = rightForwardMotor.getEncoder();

        gyro.reset();

        // Start pose at field origin facing forward
        poseEstimator = new DifferentialDrivePoseEstimator(
            kinematics,
            gyro.getRotation2d(),
            getLeftMeters(),
            getRightMeters(),
            new Pose2d()
        );
    }

    @Override
    public void periodic() {
        // Step 1 — update wheel odometry (runs every loop, 50 Hz)
        poseEstimator.update(gyro.getRotation2d(), getLeftMeters(), getRightMeters());

        // Step 2 — fuse vision correction whenever the driver cam sees AprilTags
        vision.getEstimatedGlobalPose().ifPresent(est ->
            poseEstimator.addVisionMeasurement(
                est.estimatedPose.toPose2d(),
                est.timestampSeconds          // latency-compensated automatically
            )
        );

        SmartDashboard.putString("Drive/Pose", getPose().toString());
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

    /** Current best-estimate robot pose on the field. */
    public Pose2d getPose() {
        return poseEstimator.getEstimatedPosition();
    }

    /**
     * Resets odometry to a known pose (e.g. from an auto starting position or vision fix).
     * Call this at the start of autonomous.
     */
    public void resetPose(Pose2d pose) {
        leftEncoder.setPosition(0.0);
        rightEncoder.setPosition(0.0);
        gyro.reset();
        poseEstimator.resetPosition(gyro.getRotation2d(), 0.0, 0.0, pose);
    }

    // ── Private helpers ───────────────────────────────────────────────────────────

    private double getLeftMeters() {
        return leftEncoder.getPosition();
    }

    private double getRightMeters() {
        // Right motor is inverted, so its encoder position is negative when driving forward.
        // Negate so the estimator always sees positive distance when moving forward.
        // If odometry drifts sideways when driving straight, flip this sign.
        return -rightEncoder.getPosition();
    }
}
