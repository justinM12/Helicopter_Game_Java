package org.csc133.a3;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.geom.Point;

import java.io.IOException;

public class SkyScraper extends Fixed implements IWayPoint {
    private int sequenceNumber;

    private final int MAX_NUMBER_OF_SKYSCRAPERS = 9;
    private static Image[] skyScraperImages;
    private boolean hasSkyBeenReached = false;

    public SkyScraper(){
        System.out.println("SkyScraper cannot be " +
                "instantiated without parameters");
    }

    public SkyScraper(int size, Location location, int argbColor,
                          int sequenceNumber){
        super(size, location, argbColor);
        this.sequenceNumber=sequenceNumber;

        if (skyScraperImages == null) {
            getSkyscraperImages();
        }
    }

    private void getSkyscraperImages(){
        skyScraperImages = new Image[MAX_NUMBER_OF_SKYSCRAPERS+1];
        try{
            for(int i = 0; i <= MAX_NUMBER_OF_SKYSCRAPERS; i++){
                skyScraperImages[i] =
                        Image.createImage("/skyscraperCropped" + i + ".png");

                int[] rgb = skyScraperImages[i].getRGB();
                for(int j = 0; j<rgb.length; j++){
                    int alpha = ColorUtil.alpha(rgb[j]);
                    int red = ColorUtil.red(rgb[j]);
                    int green = ColorUtil.green(rgb[j]);
                    int blue = ColorUtil.blue(rgb[j]);

                    if(red == 255 && green == 255 && blue == 255){
                        rgb[j] = ColorUtil.rgb(
                                getRedOfColor(),
                                getGreenOfColor(),
                                getBlueOfColor());
                    }
                }
                skyScraperImages[i] =
                        Image.createImage(
                                rgb,
                                skyScraperImages[i].getWidth(),
                                skyScraperImages[i].getHeight());
            }

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static Image[] getSkyScraperImages(){
        return skyScraperImages;
    }

    public int getSequenceNumber(){
        return sequenceNumber;
    }

    public void setHasSkyBeenReachedTrue(){
        hasSkyBeenReached = true;
    }

    @Override
    public void setColor_argb(int argbColor) {
        System.out.println("Cannot set the color of " + this);
    }

    @Override
    public void updateOnTick(double elapsedTime) {

    }

    @Override
    public void draw(Graphics g, Point gameOriginLocation){
        super.draw(g, gameOriginLocation);

        int drawSize = getSize(); // default size when drawing objects
        //System.out.println("draw() method in GameObject should be overidden.");
        Point drawingLocation = new Point(
                gameOriginLocation.getX() + (int) getLocation().getXPos(),
                gameOriginLocation.getY() - (int) getLocation().getYPos());

        if (hasSkyBeenReached){
            g.setColor(ColorUtil.rgb(0,0,0));
            g.setAlpha(100);
            g.fillRect(
                    drawingLocation.getX() - drawSize/2,
                    drawingLocation.getY() - drawSize/2,
                    drawSize,
                    drawSize);
            g.setAlpha(255);
        }else{
            g.setColor(getColor_argb());
            g.drawImage(
                    skyScraperImages[sequenceNumber],
                    drawingLocation.getX() - drawSize/2,
                    drawingLocation.getY() - drawSize/2,
                    drawSize,
                    drawSize);
        }


    }

    @Override
    public String toString(){
        String start = "SkyScraper: ";
        String location = "loc=" + getLocation().getXPos() + ","
                + getLocation().getYPos() + " ";
        String color = "color=" + "[" + getRedOfColor() +","+ getGreenOfColor()
                + "," + getBlueOfColor() + "] ";
        String size = "size=" + getBoundingShapeWidth() + " ";
        String sequenceNum = "seqNum=" + getSequenceNumber() + " ";

        return start + location + color + size + sequenceNum;
    }
}
