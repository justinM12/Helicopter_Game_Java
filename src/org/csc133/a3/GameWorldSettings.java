package org.csc133.a3;

import com.codename1.charts.util.ColorUtil;

public class GameWorldSettings {
    public final double TIME_BETWEEN_TICK_IN_SECONDS = 0.02;
    public final double TICKS_PER_SECOND = 1/ TIME_BETWEEN_TICK_IN_SECONDS;

    public final int GAME_WORLD_WIDTH;
    public final int GAME_WORLD_HEIGHT;
    public final int GAME_INITAL_NUMBER_OF_LIVES;

    public final int BIRD_COLOR;
    public final int SKYSCRAPER_COLOR;
    public final int REFUEL_BLIMP_COLOR;
    public final int HELICOPTER_COLOR;

    public final int FLIGHT_PATH_MIN_AMOUNT_SKYSCRAPERS;
    public final int FLIGHT_PATH_MAX_AMOUNT_SKYSCRAPERS;

    public final int SKYSCRAPER_SIZE;

    public final int HELICOPTER_SIZE;
    public final int HELICOPTER_SPEED_AFTER_JUST_SPAWNED;
    public final int HELICOPTER_MAX_HEADING_CHANGE_PER_TICK;
    public final int HELICOPTER_MAX_STICK_ANGLE_DIFFERENCE_FROM_HEADING;
    public final int HELICOPTER_MAX_SPEED ;
    public final int HELICOPTER_INITAL_FUEL_LEVEL;
    public final double HELICOPTER_FUEL_CONSUMPTION_RATE;
    public final int HELICOPTER_ACCELERATION_RATE;
    public final int HELICOPTER_DEACCELERATION_RATE;
    public final int HELICOPTER_MAX_DAMAGE_LEVEL;
    public final float HELICOPTER_MIN_BLADE_SPEED;
    public final float HELICOPTER_BLADE_ACCEL;
    public final float HELICOPTER_START_UP_BLADE_ACCEL;

    public final int NON_PLAYER_HELICOPTER_POPULATION_AMOUNT;

    public final int REFUEL_BLIMP_POPULATION_AMOUNT;
    public final int REFUEL_BLIMP_MAX_SIZE;
    public final int REFUEL_BLIMP_MIN_SIZE;

    public final int BIRD_POPULATION_AMOUNT;
    public final int BIRD_MAX_SPEED;
    public final int BIRD_MIN_SPEED;
    public final int BIRD_MAX_SIZE;
    public final int BIRD_Min_SIZE;
    public final int BIRD_RANDOM_HEADING_CHANGE_VALUE;

    public final int COLLISION_WITH_HELICOPTER_DAMAGE;
    public final int COLLISION_WITH_BIRD_DAMAGE;

    private FlightPath flightPath;

    private Location defaultPlayerHelicopterSpawn; // is set to first skyScraper
    // in defaultFlightPathCreater()

    public GameWorldSettings(){

        GAME_WORLD_WIDTH = 1024;
        GAME_WORLD_HEIGHT = 768;
        GAME_INITAL_NUMBER_OF_LIVES = 3;

        BIRD_COLOR = ColorUtil.rgb(255,0,0);
        SKYSCRAPER_COLOR = ColorUtil.rgb(0,255,0);
        REFUEL_BLIMP_COLOR = ColorUtil.rgb(0,0,255);
        HELICOPTER_COLOR = ColorUtil.rgb(0,0,0);

        SKYSCRAPER_SIZE = 80;


        FLIGHT_PATH_MAX_AMOUNT_SKYSCRAPERS = 9;
        FLIGHT_PATH_MIN_AMOUNT_SKYSCRAPERS = 4;

        HELICOPTER_SIZE = 70;
        HELICOPTER_SPEED_AFTER_JUST_SPAWNED = 3;
        HELICOPTER_MAX_HEADING_CHANGE_PER_TICK = 5;
        HELICOPTER_MAX_STICK_ANGLE_DIFFERENCE_FROM_HEADING = 40;
        HELICOPTER_MAX_SPEED = 150;
        HELICOPTER_INITAL_FUEL_LEVEL = 150;
        HELICOPTER_FUEL_CONSUMPTION_RATE = 10 / TICKS_PER_SECOND ; // fuel unit per second
        HELICOPTER_ACCELERATION_RATE = 10;
        HELICOPTER_DEACCELERATION_RATE = 10;
        HELICOPTER_MAX_DAMAGE_LEVEL = 100;
        HELICOPTER_MIN_BLADE_SPEED = (float) (6*Math.PI); // revolution per second
        HELICOPTER_BLADE_ACCEL = (float) (Math.PI/2);
        HELICOPTER_START_UP_BLADE_ACCEL =
                (float) (HELICOPTER_MIN_BLADE_SPEED / TICKS_PER_SECOND); // takes 1 second for blade to reach min speed

        NON_PLAYER_HELICOPTER_POPULATION_AMOUNT = 3;

        REFUEL_BLIMP_POPULATION_AMOUNT = 2;
        REFUEL_BLIMP_MAX_SIZE = 70;
        REFUEL_BLIMP_MIN_SIZE = 50;

        BIRD_POPULATION_AMOUNT = 2;
        BIRD_MAX_SPEED = 100;
        BIRD_MIN_SPEED = 80;
        BIRD_MAX_SIZE = 70;
        BIRD_Min_SIZE = 50;
        BIRD_RANDOM_HEADING_CHANGE_VALUE = 5;

        COLLISION_WITH_HELICOPTER_DAMAGE = 20;
        COLLISION_WITH_BIRD_DAMAGE = COLLISION_WITH_HELICOPTER_DAMAGE/2;
    }

    public FlightPath getFlightPath() {
        return flightPath;
    }

    public void setFlightPath(FlightPath flightPath) {
        this.flightPath = flightPath;
    }
}
