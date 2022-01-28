package org.csc133.a3;

public class AttackPlayerHelicopterStrategy implements INphStrategy{

    private Game game;
    public AttackPlayerHelicopterStrategy(Game game){
        this.game = game;
    }

    public String toString(){
        return "AttackPlayerHelicopterStrategy";
    }

    @Override
    public void invokeStrategy(NonPlayerHelicopter heli) {
        goToPlayerHeliPosition(heli);
    }

    private void goToPlayerHeliPosition(NonPlayerHelicopter nph){
        Helicopter playerHeli = game.getGameWorld().getPlayerHelicopter();
        Location playerPos = playerHeli.getLocation();
        setStickAngleTowardsPos(nph, playerPos);

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
