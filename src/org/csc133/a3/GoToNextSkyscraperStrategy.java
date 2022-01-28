package org.csc133.a3;

public class GoToNextSkyscraperStrategy implements INphStrategy {

    private Game game;
    private float MAX_DIFFERENCE_IN_POS_AMT = 10;
    public GoToNextSkyscraperStrategy(Game game){
        this.game = game;
    }

    @Override
    public void invokeStrategy(NonPlayerHelicopter heli) {
        goToNextSkyScraper(heli);
    }

    public String toString(){
        return "GoToNextSkyscraperStrategy";
    }

    private void goToNextSkyScraper(NonPlayerHelicopter nph){
        int nextSkyscraperNumber = nph.getCurrentWaypointNum();

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

        setStickAngleTowardsPos(nph, ((SkyScraper) skyScraper));
    }

    private void setStickAngleTowardsPos(NonPlayerHelicopter nph, SkyScraper sky){
        Location nphPos = nph.getLocation();
        float xDistance = sky.getLocation().getXPos() - nphPos.getXPos();
        float yDistance = sky.getLocation().getYPos() - nphPos.getYPos();

        double xOverY = (double) (xDistance/yDistance);
        float newHeadingDegrees =
                GameAngleConversionUtil
                        .convertXYDistanceToHeadingAngle(xDistance, yDistance);

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
