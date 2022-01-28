package org.csc133.a3;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Component;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;

public class DigitalDamageDisplay extends Component implements IDigitalDisplay{
    private int above50PercentDmgColor = ColorUtil.YELLOW;
    private int above85PercentDmgColor =
            ColorUtil.argb(255, 255, 0, 0);
    private int ledColor;

    private DigitalDisplayComponent digitalDisplayComponent;
    private final int NUM_DIGITS_SHOWING = 2;

    public DigitalDamageDisplay(int color){
        ledColor = color;
        this.digitalDisplayComponent =
                new DigitalDisplayComponent(NUM_DIGITS_SHOWING, color);
    }

    public int getLedColor(){
     return digitalDisplayComponent.getLedColor();
    }

    public void setLedColor(int color){
        digitalDisplayComponent.setLedColor(color);
    }

    public void start(){
        getComponentForm().registerAnimated(this);
    }

    public void stop(){
        getComponentForm().deregisterAnimated(this);
    }

    public void laidOut(){
        start();
    }

    public void matchDigitImagesToValue(){
        digitalDisplayComponent.matchDigitImagesToValue();
    }

    public boolean animate(){
        return true;
    }

    protected Dimension calcPreferredSize(){
        return new Dimension(DigitalDisplayComponent
                .getColonImage()
                .getWidth() * NUM_DIGITS_SHOWING
                , DigitalDisplayComponent.getColonImage().getHeight());
    }

    public void updateDigitalDisplayValue(int newDisplayValue){
        ifValueIsAboveDamageThresholdChangeLedColor(newDisplayValue);
        digitalDisplayComponent.updateDigitalDisplayValue(newDisplayValue);
    }

    private void ifValueIsAboveDamageThresholdChangeLedColor(int value) {
        if(value < 50){
            changeLedColor(ledColor);
        }
        else if(value > 50 && value < 85){
            changeLedColor(above50PercentDmgColor);
        }else if(value > 85){
            changeLedColor(above85PercentDmgColor);
        }
    }

    private void changeLedColor(int color){
        digitalDisplayComponent.setLedColor(color);
    }

    public void paint(Graphics g){
        super.paint(g);
        final int COLOR_PAD = 1; /* used to paint digit background color
            slightly larger than digit image size to prevent color bleed over
        */

        int digitWidth = digitalDisplayComponent.getDigitsImages()[0].getWidth();
        int digitHeight = digitalDisplayComponent.getDigitsImages()[0].getHeight();
        int clockWidth = NUM_DIGITS_SHOWING * digitWidth;

        float scaleFactor = Math.min(
                getInnerHeight()/(float) digitHeight,   /* getInnerHeight()
                    is a method from Component that
                    returns the height of the region that this component
                    has been given to be drawn
                */
                getInnerWidth()/(float) clockWidth); /* We take the min scale
                    to ensure that when the clock images are scaled they fit
                    in the drawing region
                */

        int displayDigitWidth = (int) (scaleFactor * digitWidth);
        int displayDigitHeight = (int) (scaleFactor * digitHeight);
        int displayClockWidth = displayDigitWidth * NUM_DIGITS_SHOWING;

        int displayX = getX() + (getWidth() - displayClockWidth)/ 2; /* getX()
            returns the origin location for the region given to draw this
            component in. displayX is the location to draw this component so
            that it is in the center of the drawing regiob
        */
        int displayY = getY() + (getHeight() - displayDigitHeight) / 2;

        fillClockRegionWithBlackAndOverlayWithLedColor(
                g,
                displayX,
                displayY,
                COLOR_PAD,
                displayClockWidth,
                displayDigitWidth,
                displayDigitHeight);

    }

    private void fillClockRegionWithBlackAndOverlayWithLedColor(
            Graphics g,
            int displayX,
            int displayY,
            int COLOR_PAD,
            int displayClockWidth,
            int displayDigitWidth,
            int displayDigitHeight)
    {
        g.setColor(ColorUtil.BLACK);
        g.fillRect(getX(), getY(), getWidth(), getHeight());

        g.setColor(digitalDisplayComponent.getLedColor());
        g.fillRect(
                displayX + COLOR_PAD,
                displayY + COLOR_PAD,
                displayClockWidth - COLOR_PAD * 2,
                displayDigitHeight - COLOR_PAD * 2);

        for(int digitIndex = 0; digitIndex < NUM_DIGITS_SHOWING; digitIndex++){
            g.drawImage(
                    digitalDisplayComponent.getDigitsBeingDisplayed()[digitIndex],
                    displayX + digitIndex * displayDigitWidth,
                    displayY,
                    displayDigitWidth,
                    displayDigitHeight);
        }
    }


}
