package org.csc133.a3;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;

import java.util.ArrayList;
import java.util.List;

public abstract class GameObject implements IDrawable, ICollider{
    private int size;
    private Location location;
    private int color;

    private boolean disabled;

    private List<GameObject> gameObjectCollisionList =
            new ArrayList<GameObject>();

    public GameObject(){
        size=0;
        location=new Location();
        color=ColorUtil.rgb(0,0,0);
    }

    public GameObject(Location location){
        this.location = location;
        size=0;
        color=ColorUtil.rgb(0,0,0);
    }

    public GameObject(int size, int color){
        this.size = size;
        this.location = new Location();
        this.color = color;
    }

    public GameObject(int size, Location location, int color){
        this.size = size;
        this.location = location;
        this.color = color;
    }

    public List<GameObject> getGameObjectCollisionList(){
        return gameObjectCollisionList;
    }

    @Override
    public boolean collidesWith(GameObject otherObject) {
        return doBoundingCirclesIntersect(this, otherObject);
    }

    private boolean doBoundingCirclesIntersect(GameObject g1, GameObject g2){
        Location loc1 = g1.getLocation();
        int radiusOfG1 = g1.getSize()/2;
        Location loc2 = g2.getLocation();
        int radiusOfG2 = g2.getSize()/2;

        double distanceBetweenObjectsSquared =
                Math.pow(loc1.getXPos() - loc2.getXPos(), 2) +
                Math.pow(loc1.getYPos() - loc2.getYPos(), 2);

        double minDistanceSquared = Math.pow(radiusOfG2 + radiusOfG2, 2);

        if (distanceBetweenObjectsSquared < minDistanceSquared){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void handleCollision(GameObject otherObject) {
        if (otherObject instanceof Bird){

        }else if (otherObject instanceof RefuelingBlimp){

        }else if (otherObject instanceof SkyScraper){

        }else if (otherObject instanceof Helicopter){

        }
    }

    public int getBoundingShapeWidth(){
        return size;
    }

    public float getXPos(){
        return location.getXPos();
    }

    public float getYPos(){
        return location.getXPos();
    }

    public Location getLocation() {return location;}

    public void setColor_argb(int argbColor){
        this.color=argbColor;
    }

    public int getColor_argb(){
        return color;
    }

    public int getAlphaOfColor(){
        return ColorUtil.alpha(color);
    }

    public int getRedOfColor(){
        return ColorUtil.red(color);
    }

    public int getGreenOfColor(){
        return ColorUtil.green(color);
    }

    public int getBlueOfColor(){
        return ColorUtil.blue(color);
    }

    public int getSize(){
        return size;
    }

    public void setLocation(Location location){
        this.location = location;
    }

    public void updateOnTick(double elapsedTime){

    };

    public void draw(   // draws boudning box
            Graphics g,
            Point gameOriginLocation){ // gameOriginLoc with respect to MapView parent component

        int drawSize = size; // default size when drawing objects
        //System.out.println("draw() method in GameObject should be overidden.");

        g.setColor(color);
        Point drawingLocation = new Point(
                gameOriginLocation.getX() + (int) location.getXPos(),
                gameOriginLocation.getY() - (int) location.getYPos());
        /*g.drawRect(
                drawingLocation.getX() - drawSize/2,
                drawingLocation.getY() - drawSize/2,
                drawSize,
                drawSize);

         */
    }

    @Override
    public String toString(){
        return "";
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
