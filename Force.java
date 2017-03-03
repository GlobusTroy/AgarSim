/**
 * Created by troymartin on 3/1/17.
 */
public class Force {

    private double magnitude;
    private double angle;

    public Force(double mag, double ang) {
        magnitude = mag;
        angle = ang;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public double getAngle() {
        return angle;
    }
}
