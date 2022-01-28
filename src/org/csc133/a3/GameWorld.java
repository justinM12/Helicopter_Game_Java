package org.csc133.a3;

import java.util.List;
import java.util.Random;

public class GameWorld {
    private int currentClockTime = 0;
    private int livesRemaining = 3;
    private double elapsedTimeBetweenTicksInSeconds = 0.020;
    private boolean isPlayerTryingToQuitGame = false; /* Keeps track of when the
        x, y, n commands are used. When x used, value is set to true. If true
        and y used then exit game. If true and n used value is set back to
        false.
    */
    private boolean playerReachedLastSky = false;
    private boolean nphReachedLastSky = false;
    private FlightPath flightPath;

    private Game game;

    private Random randomIntGenerator = new Random();
    private Helicopter playerHelicopter;
    private GameObjectCollection gameObjectList;

    private GlassCockpit glassCockpit;
    private MapView mapView;
    private GameSoundPlayer gameSoundPlayer;

    public GameWorld(Game game, GameSoundPlayer gameSoundPlayer){
        this.game = game;
        this.gameSoundPlayer = gameSoundPlayer;
    }


    private GameWorldSettings gwSettings;

    public void init(
            GameWorldSettings gwSettings,
            GlassCockpit glassCockpit,
            MapView mapView){ /* Initializes game world
        and places game objects in the world
    */
        this.glassCockpit = glassCockpit;
        this.mapView = mapView;
        this.gwSettings = gwSettings;

        gameObjectList = new GameObjectCollection();
    }

    public void reInit(){
        playerReachedLastSky = false;
        nphReachedLastSky = false;
        gameObjectList = new GameObjectCollection();
        loadGameObjectsAndStartUITimer();

    }

    public Game getGame(){
        return game;
    }

    public void loadGameObjectsAndStartUITimer(){
        createFlightPath();
        addBirdsToGameObjectList(gwSettings.BIRD_POPULATION_AMOUNT);
        addRefuelBlimpsToGameObjectList(
                gwSettings.REFUEL_BLIMP_POPULATION_AMOUNT);
        addNonPlayerHelicoptersToObjectList();
        addPlayerHelicopterToObjectList();
        game.startUiTimer(gwSettings);
    }

    private void addPlayerHelicopterToObjectList()
    {
        if(PlayerHelicopter.getPlayerHelicopter() != null){
            resetPlayerHelicopterValues(PlayerHelicopter.getPlayerHelicopter());
        }else{
            int startSpeed = 0;
            int startHeading = 0;
            Location startLocation =
                    ((SkyScraper) flightPath.getFirstWayPoint()).getLocation();

            PlayerHelicopter.createPlayerHelicopter(
                    gwSettings,
                    startSpeed,
                    startHeading,
                    startLocation,
                    this);
        }

        playerHelicopter = PlayerHelicopter.getPlayerHelicopter();
        playerHelicopter.setLastSkyScraperReached(
                ((SkyScraper) flightPath.getFirstWayPoint()));
        gameObjectList.add(playerHelicopter);
    }

    private void resetPlayerHelicopterValues(PlayerHelicopter playerHelicopter){
        playerHelicopter.setFuelLevel(gwSettings.HELICOPTER_INITAL_FUEL_LEVEL);
        playerHelicopter.setSpeed(0);
        playerHelicopter.setHeading(0);
        playerHelicopter.setDamageLevel(0);
        playerHelicopter.setLocation(
                ((SkyScraper) flightPath.getFirstWayPoint()).getLocation());
        playerHelicopter.setHeliBladeSpeed(0);
        playerHelicopter.setColor_argb(gwSettings.HELICOPTER_COLOR);
        playerHelicopter.setDisabled(false);
        playerHelicopter.setMaxSpeed(gwSettings.HELICOPTER_MAX_SPEED);
    }

    private void addNonPlayerHelicoptersToObjectList(){
        int startHeading = 0;
        int startSpeed = gwSettings.HELICOPTER_SPEED_AFTER_JUST_SPAWNED;
        Location startLocation;

        int nphPopulation = gwSettings.NON_PLAYER_HELICOPTER_POPULATION_AMOUNT;

        for(int i = 0; i < nphPopulation; i++){
            startLocation = generateRandomLocationNearFirstSkyscraper();
            NonPlayerHelicopter nph = new NonPlayerHelicopter(
                    gwSettings,
                    startSpeed,
                    startHeading,
                    startLocation,
                    game);

            gameObjectList.add(nph);
        }
    }

    private Location generateRandomLocationNearFirstSkyscraper(){
        Location skyLoc =
                ((SkyScraper) flightPath.getFirstWayPoint()).getLocation();

        Random random = new Random();
        float xPos = 100 + random.nextInt(200);
        float yPos = 100 + random.nextInt(200);

        return new Location(xPos + skyLoc.getXPos(), yPos + skyLoc.getYPos());

    }

    private void addRefuelBlimpsToGameObjectList(int amtToAdd){
        int maxBlimpSize = gwSettings.REFUEL_BLIMP_MAX_SIZE;
        int minBlimpSize = gwSettings.REFUEL_BLIMP_MIN_SIZE;

        for(int i=0; i<amtToAdd; i++){
            Location randLocation = generateRandomLocationInGameWorld();
            int blimpSize = minBlimpSize + randomIntGenerator.nextInt(
                    maxBlimpSize-minBlimpSize);
            RefuelingBlimp blimp = new RefuelingBlimp(blimpSize
                    ,randLocation, gwSettings.REFUEL_BLIMP_COLOR);
            gameObjectList.add(blimp);
        }
    }

    private void addBirdsToGameObjectList(int amtToAdd){
        int maxBirdSize = gwSettings.BIRD_MAX_SIZE;
        int minBirdSize = gwSettings.BIRD_Min_SIZE;
        int maxSpeed = gwSettings.BIRD_MAX_SPEED;
        int minSpeed = gwSettings.BIRD_MIN_SPEED;

        for(int i=0; i<amtToAdd; i++){
            Location randLocation = generateRandomLocationInGameWorld();
            int birdSize = minBirdSize + randomIntGenerator.nextInt(
                    maxBirdSize-minBirdSize);
            int birdSpeed = minSpeed + randomIntGenerator.nextInt(
                    maxSpeed-minSpeed);
            Bird bird = new Bird(birdSize, randLocation, gwSettings.BIRD_COLOR
                    , gwSettings.BIRD_RANDOM_HEADING_CHANGE_VALUE, birdSpeed);
            gameObjectList.add(bird);
        }

    }

    private void createFlightPath(){
        flightPath = new FlightPath();
        int spawnPadding = 150;
        int amountOfSkyscrapers = gwSettings.FLIGHT_PATH_MIN_AMOUNT_SKYSCRAPERS;

        SkyScraper firstSky = new SkyScraper(
                gwSettings.SKYSCRAPER_SIZE,
                new Location((float) spawnPadding,(float) spawnPadding),
                gwSettings.SKYSCRAPER_COLOR,
                0);

        SkyScraper lastSky = new SkyScraper(
                gwSettings.SKYSCRAPER_SIZE,
                new Location(
                        (float) Location.getMaxXPos() - spawnPadding,
                        (float) Location.getMaxYPos() - spawnPadding),
                gwSettings.SKYSCRAPER_COLOR,
                amountOfSkyscrapers);

        flightPath.addWaypoint(firstSky);
        gameObjectList.add(firstSky);
        gameObjectList.add(lastSky);
        int amountOfSkyscrapersToAdd = amountOfSkyscrapers - 2;

        SkyScraper prevSky = null;
        for(int i=1; i<=1 + amountOfSkyscrapersToAdd; i++){
            SkyScraper sky;
            do{
                Location randLocation =
                        generateRandomLocationInGameWorldWithPadding();

                sky = new SkyScraper(
                        gwSettings.SKYSCRAPER_SIZE,
                        randLocation,
                        gwSettings.SKYSCRAPER_COLOR,
                        i);

                if(prevSky == null){
                    break;
                }
            }while(doesSkyCollideWithOtherSky(sky));

            flightPath.addWaypoint(sky);
            gameObjectList.add(sky);
            prevSky = sky;
        }
        flightPath.addWaypoint(lastSky);
        gwSettings.setFlightPath(flightPath);
    }

    private boolean doesSkyCollideWithOtherSky(SkyScraper sky){
        for(int i = 0; i < flightPath.getFlightPathLength(); i++){
            if(sky.collidesWith((SkyScraper)flightPath.getWaypoint(i))){
                return true;
            }
        }
        return false;
    }

    private Location generateRandomLocationInGameWorld(){
        Random random = new Random();
        float xPos = random.nextInt(Location.getMaxXPos());
        float yPos = random.nextInt(Location.getMaxYPos());
        return new Location(xPos, yPos);
    }

    private Location generateRandomLocationInGameWorldWithPadding(){
        Random random = new Random();
        float xPos = 150 + random.nextInt(Location.getMaxXPos() - 300);
        float yPos = 150 + random.nextInt(Location.getMaxYPos() - 300);
        return new Location(xPos, yPos);
    }

    public void gamePaused(){
        glassCockpit.stopGameTime();
    }

    public void gameUnpaused(){
        glassCockpit.startGameTime();
    }

    public GameObjectCollection getGameObjectCollection(){
        return gameObjectList;
    }

    public Helicopter getPlayerHelicopter() {
        return playerHelicopter;
    }

    public int getLivesRemaining(){
        return livesRemaining;
    }

    public void acceleratePlayerHelicopter(){
        playerHelicopter.accelerate();
    }

    public void deceleratePlayerHelicopter(){
        playerHelicopter.decelerate();
    }

    public void changePlayerStickAngle5DegreesLeft(){
        playerHelicopter.modifyHeading(-5);
    }

    public void changePlayerStickAngle5DegreesRight(){
        playerHelicopter.modifyHeading(5);
    }

    private void notifyObserversThatGameWorldHasChanged(){
        glassCockpit.updateView();
        mapView.updateView();
    }

    private void ifPlayerHelicopterCannotMoveLoseLifeAndReInitGameWorld(){
        if(playerHelicopter.getDamageLevel()
                == gwSettings.HELICOPTER_MAX_DAMAGE_LEVEL){

            System.out.println("Player Helicopter at Max Damage, lose life, "
                    + "Reinitializing GameWorld");
            livesRemaining = livesRemaining-1;
            ifNoLivesRemainingEndGame();
            reInitializeGameWorldAndViews();
            gameSoundPlayer.playPlayerHeliDestroyedSound();

        }else if(playerHelicopter.getFuelLevel() == 0){
            System.out.println("Player Helicopter is out of fuel, lose life, "
                    + "Reinitializing GameWorld");
            livesRemaining = livesRemaining-1;
            ifNoLivesRemainingEndGame();
            reInitializeGameWorldAndViews();
            gameSoundPlayer.playPlayerHeliDestroyedSound();

        }
    }

    public void nphReachedLastSkyscraperLoseLifeAndReinit(){
        System.out.println("Non Player Helicopter reached last Skyscraper, "
                + "lose life," + " Reinitializing GameWorld");
        livesRemaining = livesRemaining-1;
        ifNoLivesRemainingEndGame();
        reInitializeGameWorldAndViews();
    }

    private void reInitializeGameWorldAndViews(){
        game.stopUITimer();
        this.reInit();
    }

    private void ifNoLivesRemainingEndGame(){
        if(livesRemaining <= 0){
            System.out.println("Game Over, better luck next time!");
            System.exit(0);
        }
    }

    private void ifLastSkyScraperReached(Helicopter heli){
        if(heli instanceof PlayerHelicopter){
            if(heli.getLastSkyScraperReached().getSequenceNumber() ==
                    flightPath.getLastWayPoint().getSequenceNumber()){
                playerReachedLastSky = true;
            }
        }else if(heli instanceof NonPlayerHelicopter){
            if (((NonPlayerHelicopter) heli).getCurrentWaypointNum() >
                    flightPath.getLastWayPoint().getSequenceNumber())
            nphReachedLastSky = true;
        }

    }

    public void gameClockHasTicked(){ /* tells all gameObjects in
        gameObjectList to do their updates
    */
        currentClockTime = currentClockTime + 1;
        gameObjectList.forEach(
                (obj) -> {
                    obj.updateOnTick(gwSettings.TIME_BETWEEN_TICK_IN_SECONDS);
                }
        );
        checkForCollisionsAndHandleCollisions();
        notifyObserversThatGameWorldHasChanged();
        ifPlayerHelicopterCannotMoveLoseLifeAndReInitGameWorld();
    }

    private void checkForCollisionsAndHandleCollisions() {
        List<GameObject> gObjList = gameObjectList.getAllGameObjects();
        for (int i = 0; i < gObjList.size(); i++) {
            GameObject obj = gObjList.get(i);
            if (obj instanceof Helicopter) {
                handleGameObjectsCollisions(obj, gObjList);
            }
        }
        handlePlayerReachedLastSkyOrNPHReachedLastSky();
    }

    private void handleGameObjectsCollisions(
            GameObject obj,
            List<GameObject> gObjList){

        for(int i = 0; i < gObjList.size(); i++){
            GameObject otherObject = gObjList.get(i);
            List<GameObject> objCollisionList = obj.getGameObjectCollisionList();

            if(obj == otherObject || obj.isDisabled() || otherObject.isDisabled()){
                continue;
            }

            if(obj.collidesWith(otherObject)){
                if (!objCollisionList.contains(otherObject)) {
                    obj.handleCollision(otherObject);
                    objCollisionList.add(otherObject);
                    otherObject.getGameObjectCollisionList().add(obj);
                }
            }else{
                if (objCollisionList.contains(otherObject)) {
                    objCollisionList.remove(otherObject);
                    otherObject.getGameObjectCollisionList().remove(obj);
                }
            }
        }
    }

    private void handlePlayerReachedLastSkyOrNPHReachedLastSky(){
        if (playerReachedLastSky){
            System.out.println("Game Over, you win! " + "Total Time: "
                    + glassCockpit.getGameTime());
            System.exit(0);
        }else if(nphReachedLastSky){
            nphReachedLastSkyscraperLoseLifeAndReinit();
        }
    }

    public void helicopterCollidedWithBird(Helicopter heli, Bird bird){
        int damage = gwSettings.COLLISION_WITH_BIRD_DAMAGE;
        heli.collidedWithBird(damage);
    }

    public void helicopterCollidedWithSkySkyscraper(
            Helicopter heli,
            SkyScraper skyScraper){

        heli.collidedWithSkyScraper(skyScraper);
        ifLastSkyScraperReached(heli);
    }

    public void helicopterCollidedWithHelicopter(
            Helicopter heli1,
            Helicopter heli2){

        int damage = gwSettings.COLLISION_WITH_HELICOPTER_DAMAGE;
        gameSoundPlayer.playHeliHeliCollisionSound();
        heli1.collidedWithHelicopter(damage);
        heli2.collidedWithHelicopter(damage);

    }

    public void helicopterCollidedWithRefuelBlimp(
            Helicopter heli,
            RefuelingBlimp refuelingBlimp){

        heli.collidedWithRefuelBlimp(refuelingBlimp.getFuelCapacity());
        refuelingBlimp.giveAllFuel();
        gameSoundPlayer.playRefuelSound();
        addRefuelBlimpsToGameObjectList(1);
    }

    public void displayCurrentGameStateValues(){
        String numberLives = "[Number of Lives Left: " + livesRemaining+"] ";
        String gameClockValue = "[Game Clock Value: " + currentClockTime+"] ";
        String lastSkyScrapperReached = "[Last SkyScraper Reached: "
                + playerHelicopter.getLastSkyScraperReached().getSequenceNumber()
                + "] ";
        String playerHeliFuelLevel = "[Player Heli Fuel Level: "
                + playerHelicopter.getFuelLevel() + "] ";
        String playerHeliDmgLevel = "[Player Heli Dmg Level: "
                + playerHelicopter.getDamageLevel() + "] ";

        System.out.println(numberLives + gameClockValue + lastSkyScrapperReached
                + playerHeliFuelLevel + playerHeliDmgLevel);
    }

    public void showGameWorldMapAsText(){
        gameObjectList.forEach(
                (obj) -> {
                    System.out.println(obj.toString());}
        );
        System.out.println("Finished Listing GameObjects.");
    }

}