package org.csc133.a3;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.geom.Point;

import java.io.IOException;
import java.util.Random;

public class Bird extends Movable{
    private int randomHeadingChangeValue;
    private static Image[] birdImages;
    private double timeBetweenImageChange = 0;
    private double maxTimeBetweenImageChange = 0.5/3.0;

    public Bird(){
        System.out.println("Bird cannot be instantiated without parameters");
    }

    public Bird(int size, Location location
            , int argbColor, int randomHeadingChangeValue, int speed ){
        super(size,location, argbColor, speed);
        this.randomHeadingChangeValue = randomHeadingChangeValue;

        if (birdImages == null){
           initBirdImage();
        }
    }

    private void initBirdImage(){
        birdImages = new Image[4];
        try{
            for(int i = 1; i < birdImages.length; i++) {
                birdImages[i] = Image.createImage("/birdCropped" + i + ".png");

                int[] rgb = birdImages[i].getRGB();
                for (int j = 0; j < rgb.length; j++) {
                    int alpha = ColorUtil.alpha(rgb[j]);
                    int red = ColorUtil.red(rgb[j]);
                    int green = ColorUtil.green(rgb[j]);
                    int blue = ColorUtil.blue(rgb[j]);

                    if (red == 255 && green == 255 && blue == 255) {
                        rgb[j] = ColorUtil.rgb(
                                getRedOfColor(),
                                getGreenOfColor(),
                                getBlueOfColor());
                    }
                }
                birdImages[i] = Image.createImage(
                        rgb,
                        birdImages[i].getWidth(),
                        birdImages[i].getHeight());
            }

        }catch(IOException e){
            e.printStackTrace();
        }

    }

    public static Image[] getBirdImage(){
        return birdImages;
    }

    @Override
    public void updateOnTick(double elapsedTime){
        updateTimeBetweenImageChange(elapsedTime);
        if (this.getReachedBoundary()){
            reverseHeading();
        }
        updateHeading();
        move(elapsedTime);
    }

    private void updateTimeBetweenImageChange(double elapsedTime){
        timeBetweenImageChange = timeBetweenImageChange + elapsedTime;
        if (timeBetweenImageChange > 3 * maxTimeBetweenImageChange){
            timeBetweenImageChange = 0;
        }

    }
    private void reverseHeading(){
        if (getHeading() > 0)
            setHeading(getHeading() + 180);
        else
            setHeading(getHeading() - 180);
    }

    private void updateHeading(){
        Random randomIntGenerator = new Random();
        int headingChangeValue =
                randomHeadingChangeValue
                        - randomIntGenerator.nextInt(
                                randomHeadingChangeValue*2);
        // multiply randomHeadingChangeValue by 2 so can get negative values
        this.setHeading(this.getHeading() + headingChangeValue);
    }

    @Override
    public void setColor_argb(int argbColor) {
        System.out.println("Cannot set the color of " + this);
    }

    @Override
    public void draw(Graphics g, Point gameOriginLocation){
        super.draw(g, gameOriginLocation);

        if (birdImages[1] == null){
            return;
        }

        int drawSize = getSize(); // default size when drawing objects
        //System.out.println("draw() method in GameObject should be overidden.");

        g.setColor(getColor_argb());
        Point drawingLocation = new Point(
                gameOriginLocation.getX() + (int) getLocation().getXPos(),
                gameOriginLocation.getY() - (int) getLocation().getYPos());

        drawBirdImage(g, drawingLocation, drawSize);
    }

    private void drawBirdImage(Graphics g, Point drawingLocation, int drawSize){
        Image birdImage = birdImages[1];
        if (timeBetweenImageChange < maxTimeBetweenImageChange){
            birdImage = birdImages[1];
        }else if(timeBetweenImageChange < 2 * maxTimeBetweenImageChange){
            birdImage = birdImages[2];
        }else if(timeBetweenImageChange < 3 * maxTimeBetweenImageChange){
            birdImage = birdImages[3];
        }

        g.rotateRadians(
                (float) Math.toRadians(getHeading()),
                drawingLocation.getX(),
                drawingLocation.getY());
        g.drawImage(
                birdImage,
                drawingLocation.getX() - drawSize/2,
                drawingLocation.getY() - drawSize/2,
                drawSize,
                drawSize);
        g.rotateRadians(
                (float) - Math.toRadians(getHeading()),
                drawingLocation.getX(),
                drawingLocation.getY());
    }

    @Override
    public String toString(){
        String start = "Bird: ";
        String location = "loc=" + getLocation().getXPos() + ","
                + getLocation().getYPos() + " ";
        String color = "color=" + "[" + getRedOfColor() +","+ getGreenOfColor()
                + "," + getBlueOfColor() + "] ";
        String heading = "heading=" + getHeading() +" ";
        String speed = "speed=" + getSpeed() + " ";
        String size = "size=" + getBoundingShapeWidth() + " ";

        return start + location + color + heading + speed + size;
    }
}
