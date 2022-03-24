package com.pigmice.frc.robot;

import java.util.List;

import edu.wpi.first.math.MathUtil;

public class ShooterDistances {
    public static class ShooterPoint {
        public double distance, topSpeed, bottomSpeed;
        
        ShooterPoint(double dist, double tSpeed, double bSpeed){
            distance = dist;
            topSpeed = tSpeed;
            bottomSpeed = bSpeed;
        }

		public double getDistance() {
			return distance;
		}

		public void setDistance(double distance) {
			this.distance = distance;
		}

		public double getTopSpeed() {
			return topSpeed;
		}

		public void setTopSpeed(double topSpeed) {
			this.topSpeed = topSpeed;
		}

		public double getBottomSpeed() {
			return bottomSpeed;
		}

		public void setBottomSpeed(double bottomSpeed) {
			this.bottomSpeed = bottomSpeed;
		}
    }

    private static final List<ShooterPoint> pointList = List.of(new ShooterPoint(0,0,0), new ShooterPoint(0,0,0));

    private static ShooterPoint calcSpeeds (double distance) {
        ShooterPoint speeds = new ShooterPoint(distance, 0, 0);
        ShooterPoint lowerBound, upperBound;
        lowerBound = upperBound = speeds;

        for(ShooterPoint i : pointList){
            if(i.getDistance() > distance){
                upperBound = i;
                lowerBound = pointList.get(pointList.indexOf(i) - 1);
                break;
            }
        }

        double betweenPortion = (distance - lowerBound.getDistance()) / (upperBound.getDistance() - lowerBound.getDistance());
        speeds.setTopSpeed(MathUtil.interpolate(lowerBound.getTopSpeed(), upperBound.getTopSpeed(), betweenPortion));
        speeds.setBottomSpeed(MathUtil.interpolate(lowerBound.getBottomSpeed(), upperBound.getBottomSpeed(), betweenPortion));
        
        return speeds;
    }

    public static double findTopSpeed (double distance) {
        return calcSpeeds(distance).getTopSpeed();
    }

    public static double findBottomSpeed (double distance) {
        return calcSpeeds(distance).getBottomSpeed();
    }
}