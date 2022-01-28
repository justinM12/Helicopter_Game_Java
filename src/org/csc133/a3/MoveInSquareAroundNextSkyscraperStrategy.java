package org.csc133.a3;


import com.codename1.charts.models.Point;

import java.util.ArrayList;

public class MoveInSquareAroundNextSkyscraperStrategy implements INphStrategy {

    private Game game;
    private int SQUARE_LENGTH = 100;
    private float MAX_DIFFERENCE_IN_POS_AMT = 30;
    private Point currentPoint;
    private SkyScraper currentSkyscraper;
    private ArrayList<Point> pointArrayList;
    private int indexOfCurrentPoint;

    public MoveInSquareAroundNextSkyscraperStrategy(Game game){
        this.game = game;

    }

    public String toString(){
        return "MoveInSquareAroundNextSkyScraperStrategy";
    }

    @Override
    public void invokeStrategy(NonPlayerHelicopter heli) {
        moveAroundNextSkyscraperInSquare(heli);
    }

    private void moveAroundNextSkyscraperInSquare(NonPlayerHelicopter nph){
        int nextSkyscraperNumber =
                game.getGameWorld()
                        .getPlayerHelicopter()
                        .getLastSkyScraperReached()
                        .getSequenceNumber() + 1;
        IWayPoint skyScraper =
                game.getGameWorldSettings()
                        .getFlightPath()
                        .getWaypoint(nextSkyscraperNumber);

        if (skyScraper == null){
            System.out.println(
                    "Error: could not find skyscraper with number: " +
                            nextSkyscraperNumber);
            return;
        }

        if (currentSkyscraper == ((SkyScraper) skyScraper))
            moveInSquareAroundSkyscraper(nph.getLocation());
        else {
            currentSkyscraper = ((SkyScraper) skyScraper);
            getSquarePoints(currentSkyscraper.getLocation());
        }

        setStickAngleTowardsPos(
                nph,
                new Location(currentPoint.getX(), currentPoint.getY()));
    }

    private void moveInSquareAroundSkyscraper(Location heliLoc){
        float x1 = heliLoc.getXPos();
        float y1 = heliLoc.getYPos();
        float x2 = currentPoint.getX();
        float y2 = currentPoint.getY();

        if (Math.abs((Math.abs(x1) - Math.abs(x2))) <
                MAX_DIFFERENCE_IN_POS_AMT
                && (Math.abs((Math.abs(y1) - Math.abs(y2))) <
                MAX_DIFFERENCE_IN_POS_AMT)){

            if (indexOfCurrentPoint + 1 > pointArrayList.size() - 1) {
                indexOfCurrentPoint = 0;
                currentPoint = pointArrayList.get(0);
            }
            else {
                indexOfCurrentPoint = indexOfCurrentPoint + 1;
                currentPoint = pointArrayList.get(indexOfCurrentPoint);
            }
        }



    }
    private void getSquarePoints(Location loc){
        Point point1 = new Point(
                loc.getXPos() + SQUARE_LENGTH,
                loc.getYPos() + SQUARE_LENGTH); // top right
        Point point2 = new Point(
                loc.getXPos() - SQUARE_LENGTH,
                loc.getYPos() + SQUARE_LENGTH); // top left
        Point point3 = new Point(
                loc.getXPos() - SQUARE_LENGTH,
                loc.getYPos() - SQUARE_LENGTH); // bottom left
        Point point4 = new Point(
                loc.getXPos() + SQUARE_LENGTH,
                loc.getYPos() - SQUARE_LENGTH); // bottom right

        pointArrayList = new ArrayList<Point>();
        pointArrayList.add(point1);
        pointArrayList.add(point2);
        pointArrayList.add(point3);
        pointArrayList.add(point4);

        currentPoint = point1;
        indexOfCurrentPoint = 0;
    }

    private void setStickAngleTowardsPos(NonPlayerHelicopter nph, Location pos){
        Location nphPos = nph.getLocation();
        float xDistance = pos.getXPos() - nphPos.getXPos();
        float yDistance = pos.getYPos() - nphPos.getYPos();
        double xOverY = (double) (xDistance/yDistance);
        float newHeadingDegrees =
                GameAngleConversionUtil.convertXYDistanceToHeadingAngle(
                        xDistance,
                        yDistance);

        int headingDifference = (int) (nph.getHeading() - newHeadingDegrees);

        if (headingDifference == 0){
            return;
        }

        if (headingDifference > 0){ // turn counter clock
            nph.setStickAngle(-headingDifference);
        }else if(headingDifference < 0){    // turn clockwise
            nph.setStickAngle(headingDifference);
        }
    }
}
