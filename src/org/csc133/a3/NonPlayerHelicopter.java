package org.csc133.a3;


import java.util.ArrayList;
import java.util.Random;

public class NonPlayerHelicopter extends Helicopter{

    private Game game;
    private AttackPlayerHelicopterStrategy attkPlayerHeliStrat;
    private GoToNextSkyscraperStrategy goToLastReachedSkyscrapStrat;
    private MoveInSquareAroundNextSkyscraperStrategy moveInSquareStrat;

    private INphStrategy currentStrategy;
    private ArrayList<INphStrategy> stratArray = new ArrayList<INphStrategy>();
    private Random randomIntGenerator = new Random();
    private boolean isReversingHeading = false;
    private int currentWaypointNum;

    public NonPlayerHelicopter(
            GameWorldSettings gwSettings,
            int speed,
            int heading,
            Location location,
            Game game){

        super(gwSettings, speed, heading, location, game.getGameWorld());

        this.game = game;
        attkPlayerHeliStrat = new AttackPlayerHelicopterStrategy(this.game);
        goToLastReachedSkyscrapStrat =
                new GoToNextSkyscraperStrategy(this.game);
        moveInSquareStrat =
                new MoveInSquareAroundNextSkyscraperStrategy(this.game);
        addStrategiesToArray();
        setStrategy();
        currentWaypointNum = 0;
    }

    private void addStrategiesToArray(){
        stratArray.add(attkPlayerHeliStrat);
        stratArray.add(goToLastReachedSkyscrapStrat);
        stratArray.add(moveInSquareStrat);
    }

    private INphStrategy pickStrategy(ArrayList<INphStrategy> stratA){
        if (stratA.size() == 1)
            return stratA.get(0);

        int randomNumber = randomIntGenerator.nextInt(stratA.size());
        return stratA.get(randomNumber);
    }

    public void setStrategy(){
        ArrayList<INphStrategy> newArray = new ArrayList<INphStrategy>();
        stratArray.forEach(
                (s)->{
                    if(addStratToArrayIfNotSameAsCurrentStrat(s))
                        newArray.add(s);
                });
        currentStrategy = pickStrategy(newArray);
    }

    public int getCurrentWaypointNum() {
        return currentWaypointNum;
    }

    private boolean addStratToArrayIfNotSameAsCurrentStrat(INphStrategy strat){
        if(strat == currentStrategy)
            return false;
        else
            return true;
    }

    @Override
    public void collidedWithSkyScraper(SkyScraper skyScraper) {
        if (currentStrategy != goToLastReachedSkyscrapStrat){
            return;
        }

        if (currentWaypointNum == skyScraper.getSequenceNumber()){
            currentWaypointNum = currentWaypointNum + 1;
        }
    }

    @Override
    public void setLastSkyScraperReached(SkyScraper sky){

    }

    @Override
    public void setFuelLevel(int fuelLevel) {

    }

    @Override
    public void updateOnTick(double elapsedTime) {
        setSpeed(game.getGameWorldSettings().BIRD_MAX_SPEED);
        currentStrategy.invokeStrategy(this);
        super.updateOnTick(elapsedTime);
    }

    @Override
    public String toString(){
        return super.toString() +
                "CurrentStrategy=" + currentStrategy.toString();

    }
}
