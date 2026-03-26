package frc.robot.subsystems;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonTrackedTarget;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.VisionConstants.*;

public class VisionSubsystem extends SubsystemBase {

    // Shooter camera sits on the turret — used for auto-aim and distance calculation
    private final PhotonCamera shooterCam = new PhotonCamera(SHOOTER_CAM_NAME);

    // Driver camera sits on the robot body — PhotonVision streams it to the dashboard automatically,
    // no extra code needed unless we want to read its data later
    @SuppressWarnings("unused")
    private final PhotonCamera driverCam = new PhotonCamera(DRIVER_CAM_NAME);

    private PhotonTrackedTarget latestTarget = null;

    @Override
    public void periodic() {
        var result = shooterCam.getLatestResult();
        latestTarget = result.hasTargets() ? result.getBestTarget() : null;
    }

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
     * Straight-line 3D distance from the camera to the target in meters.
     * Requires the camera to be calibrated in the PhotonVision UI first.
     * Returns 0 if no target.
     */
    public double getDistance() {
        if (latestTarget == null) return 0.0;
        return latestTarget.getBestCameraToTarget().getTranslation().getNorm();
    }

    /**
     * Converts a distance (meters) into a shooter motor speed (0.0 to 1.0).
     * Linearly interpolates between the two points set in VisionConstants.
     */
    public double getShooterSpeed(double distanceMeters) {
        double t = (distanceMeters - CLOSE_RANGE_METERS) / (FAR_RANGE_METERS - CLOSE_RANGE_METERS);
        t = Math.max(0.0, Math.min(1.0, t)); // clamp so we never go out of range
        return CLOSE_RANGE_SPEED + t * (FAR_RANGE_SPEED - CLOSE_RANGE_SPEED);
    }
}
