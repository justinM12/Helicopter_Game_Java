package org.csc133.a3;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Font;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.geom.Point;

import java.io.IOException;

public class RefuelingBlimp extends Fixed{
    private static final int MAX_SIZE = 10;
    private static final int MIN_SIZE = 5;
    private int fuelCapacity;

    private Image blimpImage;
    private static Image unModifiedBlimpImage;

    public RefuelingBlimp(){
        super();
        fuelCapacity=0;
    }


    public static Image getBlimpImage(){
        return unModifiedBlimpImage;
    }

    public RefuelingBlimp(int size, Location location, int argbColor){
        super(size, location, argbColor);
        this.fuelCapacity = calculateBlimpFuelCapacityFromSize(size);

        if (blimpImage == null){
            initializeBlimpImage();
        }
    }

    private void initializeBlimpImage(){
        try{
            unModifiedBlimpImage = Image.createImage("/refuelblimpCropped.png");
        }catch(IOException e){
            e.printStackTrace();
        }
        changeBlimpImageColor();
    }

    private void changeBlimpImageColor(){
        int[] rgb = unModifiedBlimpImage.getRGB();
        for(int i = 0; i<rgb.length; i++){
            int alpha = ColorUtil.alpha(rgb[i]);
            int red = ColorUtil.red(rgb[i]);
            int green = ColorUtil.green(rgb[i]);
            int blue = ColorUtil.blue(rgb[i]);

            if(red == 255 && green == 255 && blue == 255){
                rgb[i] = ColorUtil.rgb(
                        getRedOfColor(),
                        getGreenOfColor(),
                        getBlueOfColor());
            }
        }
        blimpImage = Image.createImage(
                rgb,
                unModifiedBlimpImage.getWidth(),
                unModifiedBlimpImage.getHeight());

    }

    public RefuelingBlimp(int size, Location location, int argbColor,
                          int fuelCapacity){
        super(size, location, argbColor);
        this.fuelCapacity = calculateBlimpFuelCapacityFromSize(size);
    }

    private int calculateBlimpFuelCapacityFromSize(int size){
        return size;
    }

    private void darkenRefuelBlimpColor(int color){
        int red = getRedOfColor() - 100;
        int green = getGreenOfColor() - 100;
        int blue = getBlueOfColor() - 100;

        if (red < 0)
            red = 0;
        if (green < 0)
            green = 0;
        if (blue < 0)
            blue = 0;

        int newColor = ColorUtil.rgb(red, green, blue);
        setColor_argb(newColor);
        changeBlimpImageColor();
    }

    public void giveAllFuel(){
        fuelCapacity = 0;
        darkenRefuelBlimpColor(getColor_argb());
        setDisabled(true);
    }

    public int getFuelCapacity(){
        return fuelCapacity;
    }

    @Override
    public void updateOnTick(double elapsedTime) {

    }

    @Override
    public void draw(Graphics g, Point gameOriginLocation){
        super.draw(g, gameOriginLocation);

        int drawSize = getSize(); // default size when drawing objects
        //System.out.println("draw() method in GameObject should be overidden.");

        g.setColor(getColor_argb());
        Point drawingLocation = new Point(
                gameOriginLocation.getX() + (int) getLocation().getXPos(),
                gameOriginLocation.getY() - (int) getLocation().getYPos());

        g.drawImage(
                blimpImage,
                drawingLocation.getX() - drawSize/2,
                drawingLocation.getY() - drawSize/2,
                drawSize,
                drawSize);

        g.setColor(ColorUtil.BLACK);
        String fuel = Integer.toString(fuelCapacity);
        Font font = Font.createSystemFont(
                Font.FACE_MONOSPACE,
                Font.STYLE_PLAIN,
                Font.SIZE_SMALL);
        g.drawString(
                fuel,
                drawingLocation.getX() - 5,
                drawingLocation.getY() - 5,
                font.getStyle());
    }

    @Override
    public String toString(){
        String start = "RefuelBlimp: ";
        String location = "loc=" + getLocation().getXPos() + ","
                + getLocation().getYPos() + " ";
        String color = "color=" + "[" + getRedOfColor() +","+ getGreenOfColor()
                + "," + getBlueOfColor() + "] ";
        String size = "size=" + getBoundingShapeWidth() + " ";
        String capacity = "capacity=" + getFuelCapacity() + " ";

        return start + location + color + size + capacity;
    }
}
