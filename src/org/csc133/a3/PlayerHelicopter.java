package org.csc133.a3;

public class PlayerHelicopter extends Helicopter{

    private static PlayerHelicopter playerHelicopter = null;


    private PlayerHelicopter(
            GameWorldSettings gwSettings,
            int speed,
            int heading,
            Location location,
            GameWorld gw){

        super(gwSettings, speed, heading, location, gw);
    }

    public static void createPlayerHelicopter(
            GameWorldSettings gwSettings,
            int speed,
            int heading,
            Location location,
            GameWorld gw)
    {
        if (playerHelicopter != null){
            return;
        }

        playerHelicopter = new PlayerHelicopter(
                gwSettings,
                speed,
                heading,
                location,
                gw);

    }

    public static PlayerHelicopter getPlayerHelicopter(){
        return playerHelicopter;
    }
}
