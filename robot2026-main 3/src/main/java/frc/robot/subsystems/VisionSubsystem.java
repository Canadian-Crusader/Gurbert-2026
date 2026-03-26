package frc.robot.subsystems;

import java.util.Optional;

import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonPoseEstimator.PoseStrategy;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.VisionConstants.*;

public class VisionSubsystem extends SubsystemBase {

    // ── Shooter camera (on the turret) ──────────────────────────────────────────
    // Used for auto-aim (yaw) and shooter speed (distance).
    // NOT used for pose estimation because it rotates with the turret.
    private final PhotonCamera shooterCam = new PhotonCamera(SHOOTER_CAM_NAME);
    private PhotonTrackedTarget latestTarget = null;

    // ── Driver camera (fixed to robot body) ─────────────────────────────────────
    // Used for field-position estimation via AprilTags.
    // Must have an AprilTag pipeline configured in the PhotonVision web UI.
    private final PhotonCamera driverCam = new PhotonCamera(DRIVER_CAM_NAME);
    private final PhotonPoseEstimator photonEstimator;

    public VisionSubsystem() {
        // Where the driver camera sits on the robot (from robot center at floor level).
        // Tune DRIVER_CAM_* constants in Constants.VisionConstants after measuring.
        Transform3d robotToDriverCam = new Transform3d(
            new Translation3d(DRIVER_CAM_X, DRIVER_CAM_Y, DRIVER_CAM_Z),
            new Rotation3d(DRIVER_CAM_ROLL, DRIVER_CAM_PITCH, DRIVER_CAM_YAW)
        );

        // Load the AprilTag field layout for the current season.
        // <<< UPDATE AprilTagFields.kXXXX if the season changes.
        // <<< UPDATE the field constant each season (k2026RebuiltWelded = 2026 default)
        AprilTagFieldLayout fieldLayout = AprilTagFields.k2026RebuiltWelded.loadAprilTagLayoutField();

        // MULTI_TAG_PNP_ON_COPROCESSOR: PhotonVision solves pose from all visible tags at once.
        // Most accurate strategy — requires "Multi-Tag" enabled in the PhotonVision pipeline settings.
        // PhotonLib 2026: camera is no longer passed in the constructor —
        // the pipeline result is passed to update() instead.
        photonEstimator = new PhotonPoseEstimator(
            fieldLayout,
            PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR,
            robotToDriverCam
        );
    }

    @Override
    public void periodic() {
        // Update shooter-camera target every loop (used by AutoAimCommand / VisionShootCommand)
        var result = shooterCam.getLatestResult();
        latestTarget = result.hasTargets() ? result.getBestTarget() : null;
    }

    // ── Shooter-camera methods (auto-aim + distance) ─────────────────────────────

    /** True if the shooter camera can see a target. */
    public boolean hasTarget() {
        return latestTarget != null;
    }

    /**
     * Horizontal angle to the target in degrees.
     * Positive = target is to the right of center, negative = left.
     * Returns 0 if no target.
     */
    public double getYaw() {
        if (latestTarget == null) return 0.0;
        return latestTarget.getYaw();
    }

    /**
     * Straight-line 3D distance from the shooter camera to the target in meters.
     * Requires the shooter camera to be calibrated in PhotonVision.
     * Returns 0 if no target.
     */
    public double getDistance() {
        if (latestTarget == null) return 0.0;
        return latestTarget.getBestCameraToTarget().getTranslation().getNorm();
    }

    /**
     * Converts a distance (meters) to a shooter motor speed (0.0–1.0).
     * Linearly interpolates between CLOSE_RANGE and FAR_RANGE constants.
     */
    public double getShooterSpeed(double distanceMeters) {
        double t = (distanceMeters - CLOSE_RANGE_METERS) / (FAR_RANGE_METERS - CLOSE_RANGE_METERS);
        t = Math.max(0.0, Math.min(1.0, t));
        return CLOSE_RANGE_SPEED + t * (FAR_RANGE_SPEED - CLOSE_RANGE_SPEED);
    }

    // ── Driver-camera method (field pose estimation) ──────────────────────────────

    /**
     * Returns a vision-based robot pose estimate if the driver camera can see AprilTags.
     * Empty when no tags are visible or when the layout failed to load.
     *
     * Call Drive.addVisionMeasurement() with this result to correct wheel odometry.
     */
    public Optional<EstimatedRobotPose> getEstimatedGlobalPose() {
        // PhotonLib 2026: pass the latest camera result to update()
        return photonEstimator.update(driverCam.getLatestResult());
    }
}
