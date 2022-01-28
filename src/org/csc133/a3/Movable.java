package org.csc133.a3;

import java.util.Random;
import com.codename1.charts.models.Point;

public abstract class Movable extends GameObject{
    private float heading;
    private int speed;
    private static Random randomIntGenerator = new Random();
    private boolean reachedBoundary = false;

    public Movable(){
        super();
    }

    public Movable(Location location){
        super(location);
    }

    public Movable(int size, Location location, int argbColor, int speed){
        super(size, location, argbColor);
        this.speed = speed;
    }

    public Movable(int size, int argbColor){
        super(size, argbColor);
        this.heading=randomIntGenerator.nextInt(360);
        this.speed=randomIntGenerator.nextInt(4);

    }

    public Movable(int size, Location location, int argbColor, float heading
            , int speed){
        super(size, location, argbColor);
        this.heading=heading;
        this.speed=speed;

    }

    public boolean getReachedBoundary(){
        return reachedBoundary;
    }

    public void setReachedBoundary(boolean bool){
        reachedBoundary = bool;
    }

    @Override
    public void updateOnTick(double elapsedTimeInSeconds){
        if (reachedBoundary){
            // reverse heading or make speed = 0
        }
    }

    public void move(double elapsedTimeInSeconds){
        Location location = getLocation();

        Point startingPos = new Point(location.getXPos(), location.getYPos());
        float translatedAngleDegree =
                GameAngleConversionUtil.
                        convertHeadingAngleToGameWorldAngle(heading);
        float translatedAngleRad = (float) Math.toRadians(translatedAngleDegree);

        float xDistanceMoved =
                (float) Math.cos(translatedAngleRad) * speed
                * (float) elapsedTimeInSeconds;
        //System.out.println(xDistanceMoved);
        // axis so +x is now where +y was and +y is now where +x was
        float yDistanceMoved =
                (float) Math.sin(translatedAngleRad) * speed
                * (float) elapsedTimeInSeconds;

        float newXPos = startingPos.getX() + xDistanceMoved;
        float newYPos = startingPos.getY() + yDistanceMoved;
        float yOverXSlope = yDistanceMoved/xDistanceMoved;
        float xOverYSlope = xDistanceMoved/yDistanceMoved;

        float xInterceptForYXSlope =
                startingPos.getX() - startingPos.getY()*yOverXSlope;
        float yInterceptForYXSlope =
                startingPos.getY() - startingPos.getX()*yOverXSlope;
        float xInterceptForXYSlope =
                startingPos.getX() - startingPos.getY()*xOverYSlope;
        float yInterceptForXYSlope =
                startingPos.getY() - startingPos.getX()*xOverYSlope;

        Point endPoint = new Point(newXPos, newYPos);
        int xBoundary = location.getMaxXPos();
        int yBoundary = location.getMaxYPos();

        if (isXYInGameBoundary(endPoint, xBoundary
                , yBoundary)){
            reachedBoundary = false;
            setLocation(new Location(newXPos, newYPos));
            //System.out.println("in BOundary");
        }else{
            reachedBoundary = true;
            Point intersectionPoint = moveObjectToBoundary(
                    startingPos,
                    endPoint,
                    xInterceptForYXSlope,
                    yInterceptForYXSlope,
                    xInterceptForXYSlope,
                    yInterceptForXYSlope,
                    yOverXSlope,
                    xOverYSlope,
                    xDistanceMoved,
                    yDistanceMoved,
                    xBoundary,
                    yBoundary,
                    heading);

            setLocation(new Location(intersectionPoint.getX()
                    , intersectionPoint.getY()));
        }

    }

    private boolean isXYInGameBoundary(Point startPoint, int boundX, int boundY){
        float x = startPoint.getX();
        float y = startPoint.getY();
        if(x <= boundX && x >= 0 && y >= 0 && y <= boundY){
            return true;
        }else {
            return false;
        }
    }

    private Point moveObjectToBoundary(
            Point startPoint,
            Point endPoint,
            float xInterceptYX,
            float yInterceptYX,
            float xInterceptXY,
            float yInterceptXY,
            float yxSlope,
            float xySlope,
            float xDist,
            float yDist,
            float rightXLineFn,
            float topYLineFn,
            float heading){

        // figure out which axis was crossed first
        // then find the intersection between line and each boundary axis
        // then choose the intersection point that is closest to
        // the initial point

        float x = endPoint.getX();
        float y = endPoint.getY();
        Point intersectionPoint;

        float xLine;
        float yLine;

        if (yxSlope > 0){
            if (xDist > 0){
                xLine = rightXLineFn;
                yLine = topYLineFn;
            }else{
                xLine = 0;
                yLine = 0;
            }
        }else{
            if (xDist > 0){
                xLine = rightXLineFn;
                yLine = 0;
            }else{
                xLine = 0;
                yLine = topYLineFn;
            }
        }

        intersectionPoint =
                findBoundaryAndHeadingPointOfIntersection(
                        startPoint,
                        yxSlope,
                        xySlope,
                        xInterceptYX,
                        yInterceptYX,
                        xInterceptXY,
                        yInterceptXY,
                        xLine,
                        yLine);

        return intersectionPoint;
    }

    private Point findBoundaryAndHeadingPointOfIntersection(
            Point startingPoint,
            float yxSlope,
            float xySlope,
            float xInterceptYX,
            float yInterceptYX,
            float xInterceptXY,
            float yInterceptXY,
            float xLineFn,
            float yLineFn){

        float xCordOfIntersectionBetweenHeadLineAndBoundYLine;
        float yCordOfIntersectionBetweenHeadLineAndBoundYLine;
        float xCordOfIntersectionBetweenHeadLineAndBoundXLine;
        float yCordOfIntersectionBetweenHeadLineAndBoundXLine;

        float initialXMinusIntersectXSquared;
        float initialYMinusIntersectYSquared;

        float distanceFromInitialPointToIntersectYBoundLinePoint;
        float distanceFromInitialPointToIntersectXBoundLinePoint;

        xCordOfIntersectionBetweenHeadLineAndBoundYLine =
                yLineFn*xySlope+xInterceptXY;
        yCordOfIntersectionBetweenHeadLineAndBoundYLine = yLineFn;

        initialXMinusIntersectXSquared =
                (float) Math.pow(
                        startingPoint.getX()
                        -xCordOfIntersectionBetweenHeadLineAndBoundYLine, 2);
        initialYMinusIntersectYSquared =
                (float) Math.pow(
                        startingPoint.getY()
                        -yCordOfIntersectionBetweenHeadLineAndBoundYLine, 2);
        distanceFromInitialPointToIntersectYBoundLinePoint =
                (float) Math.sqrt(
                        initialXMinusIntersectXSquared
                        +initialYMinusIntersectYSquared);

        xCordOfIntersectionBetweenHeadLineAndBoundXLine = xLineFn;
        yCordOfIntersectionBetweenHeadLineAndBoundXLine =
                xLineFn*yxSlope+yInterceptYX;

        initialXMinusIntersectXSquared =
                (float) Math.pow(
                        startingPoint.getX()
                        - xCordOfIntersectionBetweenHeadLineAndBoundXLine,2);
        initialYMinusIntersectYSquared =
                (float) Math.pow(
                        startingPoint.getY()
                        - yCordOfIntersectionBetweenHeadLineAndBoundXLine,2);
        distanceFromInitialPointToIntersectXBoundLinePoint =
                (float) Math.sqrt(
                        initialXMinusIntersectXSquared
                        +initialYMinusIntersectYSquared);

        switch (Float.compare(
                distanceFromInitialPointToIntersectXBoundLinePoint,
                distanceFromInitialPointToIntersectYBoundLinePoint)){
            case 0: // distances are equal
            case -1: // intersection point with X boundary line is closer to start pos
                return new Point(xLineFn
                        , yCordOfIntersectionBetweenHeadLineAndBoundXLine);
            case 1: // intersection point with X boundary line if farther from start pos
                return new Point(xCordOfIntersectionBetweenHeadLineAndBoundYLine
                        , yLineFn);
            default:
                return new Point(-1,-1);
        }
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed){
        this.speed = speed;
    }

    public float getHeading() {
        if (heading > 0){
            return heading;
        }
        return 360 + heading;
    }

    public void setHeading(float head) {
        float newHeading;
        if (Math.abs(heading) > 360){
            newHeading = (float) (Math.abs(heading) - 360);
            if (heading > 0)
                this.heading = newHeading;
            else
                this.heading = -newHeading;
            return;
        }
        this.heading = head;
    }
}
