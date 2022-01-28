package org.csc133.a3;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.geom.Point;

import java.io.IOException;
import java.util.Random;

public class Helicopter extends Movable implements ISteerable {

    private int maxHeadingChangePerTick;
    private int maxStickAngleDifferenceFromHeading;
    private double fuelConsumptionRatePerSec;
    private int accelerationRate;
    private int maxDamageLevel;

    private int maximumSpeed;
    private float stickAngle;
    private double fuelLevel;
    private int damageLevel; // 0 = no dmg, maxDamageLevel = completely damaged
    private SkyScraper lastSkyScraperReached;

    private double helicopterBladeAngle;
    private float heliBladeSpeed;
    private float minHelicopterBladeSpeed;
    private float heliBladeAccel;
    private float startUpHeliBladeAccel;
    private double ticksPerSecond;
    private boolean inStartAnimation = false;

    private Image heliImage;
    private static Image unModifiedHeliImage;
    private static Image heliBladeImage;
    private GameWorld gw;



    public Helicopter(){

    }

    public Helicopter(
            GameWorldSettings gwSettings,
            int speed,
            int heading,
            Location location,
            GameWorld gameWorld){

        super(
                gwSettings.HELICOPTER_SIZE,
                location,
                gwSettings.HELICOPTER_COLOR,
                heading,
                speed);

        int maximumSpeed = gwSettings.HELICOPTER_MAX_SPEED;
        int maxHeadingChangePerTick =
                gwSettings.HELICOPTER_MAX_HEADING_CHANGE_PER_TICK;
        int maxStickAngleDifferenceFromHeading =
                gwSettings.HELICOPTER_MAX_STICK_ANGLE_DIFFERENCE_FROM_HEADING;
        double fuelConsumptionRate = gwSettings.HELICOPTER_FUEL_CONSUMPTION_RATE;
        int accelerationRate = gwSettings.HELICOPTER_ACCELERATION_RATE;
        int fuelLevel = gwSettings.HELICOPTER_INITAL_FUEL_LEVEL;
        int maxDamageLevel = gwSettings.HELICOPTER_MAX_DAMAGE_LEVEL;
        gw = gameWorld;


        this.maxHeadingChangePerTick = maxHeadingChangePerTick;
        this.maxStickAngleDifferenceFromHeading =
                maxStickAngleDifferenceFromHeading;
        this.fuelConsumptionRatePerSec = fuelConsumptionRate;
        this.accelerationRate = accelerationRate;
        this.fuelLevel = fuelLevel;
        this.maxDamageLevel = maxDamageLevel;
        this.maximumSpeed=maximumSpeed;
        minHelicopterBladeSpeed = gwSettings.HELICOPTER_MIN_BLADE_SPEED;
        heliBladeAccel = gwSettings.HELICOPTER_BLADE_ACCEL;
        startUpHeliBladeAccel = gwSettings.HELICOPTER_START_UP_BLADE_ACCEL;
        heliBladeSpeed =
                (speed == 0) ? 0 : gwSettings.HELICOPTER_MIN_BLADE_SPEED;
        helicopterBladeAngle = new Random().nextInt(6); // sets blade in random start position
        stickAngle=0;
        damageLevel=0;
        lastSkyScraperReached =
                ((SkyScraper) gwSettings.getFlightPath().getFirstWayPoint());
        ticksPerSecond = gwSettings.TICKS_PER_SECOND;

        if (heliImage == null){
            initHeliImage();
        }
    }

    private void initHeliImage(){
        try{

            unModifiedHeliImage = Image.createImage("/helicopterCropped.png");
            heliBladeImage = Image.createImage("/helicopterBladeCropped.png");
        }catch(IOException e){
            e.printStackTrace();
        }
        changeHeliImageColor();
    }

    private void changeHeliImageColor(){
        int[] rgb = unModifiedHeliImage.getRGB();
        for(int i = 0; i<rgb.length; i++){
            int alpha = ColorUtil.alpha(rgb[i]);
            int red = ColorUtil.red(rgb[i]);
            int green = ColorUtil.green(rgb[i]);
            int blue = ColorUtil.blue(rgb[i]);

            if(red == 255 && green == 255 && blue == 255){
                rgb[i] = ColorUtil.argb(
                        getAlphaOfColor(),
                        getRedOfColor(),
                        getGreenOfColor(),
                        getBlueOfColor());
            }
        }
        heliImage = Image.createImage(
                        rgb,
                        unModifiedHeliImage.getWidth(),
                        unModifiedHeliImage.getHeight());
    }

    public static Image getHeliImage(){
        return unModifiedHeliImage;
    }

    @Override
    public void handleCollision(GameObject otherObject) {
        if (otherObject instanceof Bird){
            gw.helicopterCollidedWithBird(this, (Bird) otherObject);
        }else if (otherObject instanceof RefuelingBlimp){
            gw.helicopterCollidedWithRefuelBlimp(
                    this,
                    (RefuelingBlimp) otherObject);
        }else if (otherObject instanceof SkyScraper){
            gw.helicopterCollidedWithSkySkyscraper(
                    this,
                    (SkyScraper) otherObject);
        }else if (otherObject instanceof Helicopter) {
            gw.helicopterCollidedWithHelicopter(
                    this,
                    (Helicopter) otherObject);
        }
    }

    @Override
    public void modifyHeading(float stickAngleChange){
        this.stickAngle += stickAngleChange;
        if (this.stickAngle > maxStickAngleDifferenceFromHeading){
            this.stickAngle = maxStickAngleDifferenceFromHeading;
        }else if(this.stickAngle < -maxStickAngleDifferenceFromHeading){
            this.stickAngle = -maxStickAngleDifferenceFromHeading;
        }
    }

    @Override
    public void updateOnTick(double elapsedTime){
        if (this.getReachedBoundary()){
            setSpeed(0);
            setHeliBladeSpeed(minHelicopterBladeSpeed);
        }

        if (fuelLevel > 0 && damageLevel < 100) {
            updateHeading();
            move(elapsedTime);
            consumeHelicopterFuel();

        }else{
            setSpeed(0);
        }
    }

    private void consumeHelicopterFuel(){
        fuelLevel = fuelLevel - fuelConsumptionRatePerSec;
    }

    @Override
    public void move(double elapsedTimeInSeconds) {

        if (heliBladeSpeed < minHelicopterBladeSpeed){
            inStartAnimation = true;
            heliBladeSpeed = heliBladeSpeed + startUpHeliBladeAccel;
            return;
        }
        inStartAnimation = false;
        super.move(elapsedTimeInSeconds);
    }

    protected void updateHeading(){
        if (Math.abs(stickAngle) < maxHeadingChangePerTick){
            if (stickAngle > 0){
                setHeading(getHeading() + stickAngle);
            }if (stickAngle < 0){
                setHeading(getHeading() - stickAngle);
            }
        }else{
            if (stickAngle > 0){ // if stickAngle is to right of heli
                stickAngle = stickAngle - maxHeadingChangePerTick;
                setHeading(getHeading() + maxHeadingChangePerTick);
            }else { // if stickAngle is to left of heli
                stickAngle = stickAngle + maxHeadingChangePerTick;
                setHeading(getHeading() - maxHeadingChangePerTick);
            }
        }
        float newHeading = getHeading();
    }

    public void accelerate(){
        int speed = getSpeed();
        if (fuelLevel > 0 && speed < maximumSpeed && !inStartAnimation){
            speed=speed + (accelerationRate-(damageLevel/100));
            increaseHeliBladeSpeed();
            if (speed > maximumSpeed){
                speed = maximumSpeed;
            }
        }
        setSpeed(speed);
    }

    private void increaseHeliBladeSpeed(){
        heliBladeSpeed = heliBladeSpeed + heliBladeAccel;
    }

    public void decelerate(){
        int speed = getSpeed();
        if (fuelLevel > 0 && speed > 0){
            speed = speed - (accelerationRate-(damageLevel/100));
            decreaseHeliBladeSpeed();
            if (speed < 0){
                speed = 0;
            }
        }
        setSpeed(speed);
    }

    private void decreaseHeliBladeSpeed(){
        if (heliBladeSpeed > minHelicopterBladeSpeed){
            heliBladeSpeed = heliBladeSpeed - heliBladeAccel;
            if (heliBladeSpeed < minHelicopterBladeSpeed) {
                heliBladeSpeed = minHelicopterBladeSpeed;
            }
        }
    }

    public void collidedWithSkyScraper(SkyScraper skyScraper)
    {
        if ((skyScraper.getSequenceNumber() - 1)
                == lastSkyScraperReached.getSequenceNumber()){
            lastSkyScraperReached = skyScraper;
        }
    }
    public void collidedWithHelicopter(int damage){
        takeDamage(damage);
    }

    private void takeDamage(int damage) {
        damageLevel = damageLevel + damage;
        if (damageLevel >= maxDamageLevel){
            damageLevel = maxDamageLevel;
            setDisabled(true);
        }
        updatePlayerHelicopterVisualOnDamage(damageLevel);
        updatePlayerHelicopterMaxSpeedOnDamage(damageLevel);
    }

    private void updatePlayerHelicopterMaxSpeedOnDamage(int currentDamageLevel){
        double speedReductionPercent = currentDamageLevel/(maxDamageLevel*3.0); /*
            The maximum slow percent is 33% of maxSpeed. The current damage
            level can be no larger than the maxDamageLevel.
        */

        maximumSpeed = (int)(maximumSpeed - (maximumSpeed * speedReductionPercent));
        if(getSpeed() > maximumSpeed)
            setSpeed(maximumSpeed);
    }

    private void updatePlayerHelicopterVisualOnDamage(int currentDamageLevel){
        double red;
        if(currentDamageLevel == maxDamageLevel) {
            red = 255; // there is a bug if a is set to 0 so we use a = 1 instead
        }else {
            double damagePercent = currentDamageLevel/(double) maxDamageLevel;
            red = ( 255.0 * (float) damagePercent);
        }
        int green = getGreenOfColor();
        int blue = getBlueOfColor();
        int newColor = ColorUtil.rgb((int) red, green, blue);
        setColor_argb(newColor);
    }

    @Override
    public void setColor_argb(int argbColor) {
        super.setColor_argb(argbColor);
        changeHeliImageColor();
    }

    public void collidedWithRefuelBlimp(int refuelAmt){
        fuelLevel = fuelLevel+refuelAmt;
    }

    public void collidedWithBird(int damage){
        takeDamage(damage);
    }

    public int getMaximumSpeed() {
        return maximumSpeed;
    }

    public float getStickAngle() {
        return stickAngle;
    }

    public void setStickAngle(float stickAngle) {
        this.stickAngle = stickAngle;
    }

    public int getFuelLevel() {
        return (int) fuelLevel;
    }

    public void setFuelLevel(int fuelLevel) {
        this.fuelLevel = fuelLevel;
    }

    public int getDamageLevel() {
        return damageLevel;
    }

    public void setDamageLevel(int damageLevel) {
        this.damageLevel = damageLevel;
    }

    public SkyScraper getLastSkyScraperReached() {
        return lastSkyScraperReached;
    }

    public void setLastSkyScraperReached(SkyScraper lastSkyScraperReached) {
        this.lastSkyScraperReached = lastSkyScraperReached;
    }

    @Override
    public void draw(Graphics g, Point gameOriginLocation){
        super.draw(g, gameOriginLocation);

        int drawSize = getSize(); // default size when drawing objects
        //System.out.println("draw() method in GameObject should be overidden.");

        g.setColor(getColor_argb());
        Point drawingLocation = new Point(
                gameOriginLocation.getX()
                        + Math.round(getLocation().getXPos()),
                gameOriginLocation.getY()
                        - Math.round(getLocation().getYPos()));

        //System.out.println(drawingLocation.getX() + " " + drawingLocation.getY());
        //System.out.println(getLocation().getXPos() + " " + getLocation().getYPos());
        drawHelicopterBody(g, drawingLocation, drawSize);
        drawHelicopterBlade(g, drawingLocation, drawSize);

    }

    private void drawHelicopterBody(
            Graphics g,
            Point drawingLocation,
            int drawSize){

        g.rotateRadians(
                (float) Math.toRadians(getHeading()),
                drawingLocation.getX(),
                drawingLocation.getY());
        g.drawImage(
                heliImage,
                drawingLocation.getX() - drawSize/2,
                drawingLocation.getY() - drawSize/2,
                drawSize,
                drawSize);
        g.rotateRadians(
                (float) -Math.toRadians(getHeading()),
                drawingLocation.getX(),
                drawingLocation.getY());
    }

    private void drawHelicopterBlade(
            Graphics g,
            Point drawingLocation,
            int drawSize){

        g.rotateRadians(
                (float) helicopterBladeAngle,
                drawingLocation.getX(),
                drawingLocation.getY());
        g.drawImage(
                heliBladeImage,
                drawingLocation.getX() - drawSize/2,
                drawingLocation.getY() - drawSize/2,
                drawSize,
                drawSize);
        g.rotateRadians(
                (float) -helicopterBladeAngle,
                drawingLocation.getX(),
                drawingLocation.getY());
        helicopterBladeAngle =
                helicopterBladeAngle +(heliBladeSpeed / ticksPerSecond);

    }

    @Override
    public String toString() {
        String start = "Helicopter: ";
        String location = "loc=" + getLocation().getXPos() + ","
                + getLocation().getYPos() + " ";
        String color = "color=" + "[" + getAlphaOfColor() + ","
                + getRedOfColor() + "," + getGreenOfColor()
                + "," + getBlueOfColor() + "] ";
        String heading = "heading=" + getHeading() + " ";
        String speed = "speed=" + getSpeed() + " ";
        String size = "size=" + getBoundingShapeWidth() + " ";
        String maxSpeed = "maxSpeed=" + getMaximumSpeed() + " ";
        String stickAngle = "stickAngle=" + getStickAngle() + " ";
        String fuelLevel = "fuelLevel=" + getFuelLevel() + " ";
        String damageLevel = "damageLevel=" + getDamageLevel() + " ";

        return start + location + color + heading + speed + size + maxSpeed
                + stickAngle + fuelLevel + damageLevel;
    }

    public float getHeliBladeSpeed() {
        return heliBladeSpeed;
    }

    public void setHeliBladeSpeed(float heliBladeSpeed) {
        this.heliBladeSpeed = heliBladeSpeed;
    }

    public float getStartUpHeliBladeAccel() {
        return startUpHeliBladeAccel;
    }

    public void setMaxSpeed(int speed){
        maximumSpeed = speed;
    }
}
