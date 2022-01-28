package org.csc133.a3;


public class Location {

    private static int maxXPos = 400;
    private static int maxYPos = 400;

    private float xPos;
    private float yPos;

    public Location(){
        System.out.println("Location() constructor should not be used");
    }

    public Location(GameWorldSettings gameWorldSettings){
        maxXPos = gameWorldSettings.GAME_WORLD_WIDTH;
        maxYPos = gameWorldSettings.GAME_WORLD_HEIGHT;
    }

    public Location(float x, float y){
        xPos=x;
        yPos=y;
    }

    public static int getMaxYPos() {
        return maxYPos;
    }

    public static int getMaxXPos() {
        return maxXPos;
    }

    public static void setMaxYPos(int y){
        maxYPos = y;
    }

    public static void setMaxXPos(int x){
        maxXPos = x;
    }

    public float getXPos() {
        return xPos;
    }

    public float getYPos() {
        return yPos;
    }

    @Override
    public String toString(){
        return "";
    }
}
