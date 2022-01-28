package org.csc133.a3;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Component;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.geom.Dimension;

import java.io.IOException;

public class DigitalDisplayComponent extends Component implements IDigitalDisplay{

    private int ledColor;
    private final int NUM_DIGITS_SHOWING;
    private int digitDisplayValue = -1;
    private Image[] digitsBeingDisplayed;

    private static final Image[] digitsImages = setDigitsImages();
    private static final Image[] digitsWithDecimalPointImages =
            setDigitsWithDecimalPointImages();
    private static final Image colonImage = setColonImage();

    private Label labelComponent = new Label();
    private boolean isInitialized = false;

    public DigitalDisplayComponent(int numDigits, int ledColor){
        NUM_DIGITS_SHOWING = numDigits;
        digitsBeingDisplayed = new Image[NUM_DIGITS_SHOWING];
        initializeDigits();
        this.ledColor = ledColor;
        digitDisplayValue = 0;
    }

    private void initializeDigits(){
        for(int i = 0; i < NUM_DIGITS_SHOWING; i++){
            digitsBeingDisplayed[i] = digitsImages[0];
        }
    }

    private static Image setColonImage(){
        Image image = null;
        try{
            image = Image.createImage("/LED_colon.png");
        } catch (IOException e) {e.printStackTrace();}
        return image;
    }

    private static Image[] setDigitsImages(){
        int numberOfDigitsFromZeroTo9 = 10;
        Image[] images = new Image[numberOfDigitsFromZeroTo9];
        try{
            for(int i=0; i<numberOfDigitsFromZeroTo9; i++){
                images[i] = Image.createImage("/LED_digit_" + i + ".png");
            }
        } catch (IOException e) {e.printStackTrace();}
        return images;
    }

    private static Image[] setDigitsWithDecimalPointImages(){
        int numberOfDigitsFromZeroTo9 = 10;
        Image[] images = new Image[numberOfDigitsFromZeroTo9];
        try{
            for(int i=0; i<numberOfDigitsFromZeroTo9; i++){
                images[i] =
                        Image.createImage(
                                "/LED_digit_" + i + "_with_dot" + ".png");
            }
        } catch (IOException e) {e.printStackTrace();}
        return images;
    }

    public Image[] getDigitsBeingDisplayed(){
        return digitsBeingDisplayed;
    }

    public static Image[] getDigitsImages(){
        return digitsImages;
    }

    public static Image[] getDigitsWithDecimalPointImages(){
        return digitsWithDecimalPointImages;
    }

    public static Image getColonImage(){
        return colonImage;
    }

    public int getLedColor(){
        return ledColor;
    }

    public void setLedColor(int color){
        ledColor = color;
    }

    public void start(){
        getComponentForm().registerAnimated(this);
    }

    public void stop(){
        getComponentForm().deregisterAnimated(this);
    }

    public void laidOut(){
        this.start();
    }

    public void updateDigitalDisplayValue(int newDisplayValue){
        if (digitDisplayValue == -1 || digitsImages == null)
            return;
        digitDisplayValue = newDisplayValue;
        matchDigitImagesToValue();
    }

    public void matchDigitImagesToValue(){
        // possible issue where newDisplayValue has more digits than numDigitsShowing
        int newDisplayValue = digitDisplayValue;
        String valueAsAString = Integer.toString(newDisplayValue);

        if(valueAsAString.length() > NUM_DIGITS_SHOWING){
            System.err.println("New display value for " +
                    "digitalDisplayComponent is too large " +
                    "to fit in the digitalDisplay digits");
            for(int i = 0; i < NUM_DIGITS_SHOWING; i++){
                digitsBeingDisplayed[i] = digitsImages[9];
            }
        }else if(valueAsAString.length() == NUM_DIGITS_SHOWING){
            for(int i = 0; i < valueAsAString.length(); i++) {
                digitsBeingDisplayed[i] =
                        digitsImages[
                                Character.getNumericValue(
                                        valueAsAString.charAt(i))];
            }
        }else if(valueAsAString.length() < NUM_DIGITS_SHOWING) {
            int numberOfZerosToAdd = NUM_DIGITS_SHOWING - valueAsAString.length();
            int counter;
            for (counter = 0; counter < numberOfZerosToAdd; counter++) {
                digitsBeingDisplayed[counter] = digitsImages[0];
                valueAsAString = "0" + valueAsAString;
            }

            for(int i = counter; i < valueAsAString.length(); i++){
                digitsBeingDisplayed[counter] =
                        digitsImages[
                                Character.getNumericValue(
                                        valueAsAString.charAt(i))];
                counter++;

            }
        }
    }

    public boolean animate(){
        if (digitDisplayValue == -1 || digitsImages == null)
            return false;
        return true; // tells system to repaint the clock
    }

    protected Dimension calcPreferredSize(){
        return new Dimension(colonImage.getWidth() * NUM_DIGITS_SHOWING
                , colonImage.getHeight());
    }

    public void paint(Graphics g){
        super.paint(g);
        final int COLOR_PAD = 1; /* used to paint digit background color
            slightly larger than digit image size to prevent color bleed over
        */

        int digitWidth = digitsImages[0].getWidth();
        int digitHeight = digitsImages[0].getHeight();
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

        g.setColor(ledColor);
        g.fillRect(
                displayX + COLOR_PAD,
                displayY + COLOR_PAD,
                displayClockWidth - COLOR_PAD * 2,
                displayDigitHeight - COLOR_PAD * 2);

        for(int digitIndex = 0; digitIndex < NUM_DIGITS_SHOWING; digitIndex++){
            g.drawImage(
                    digitsBeingDisplayed[digitIndex],
                    displayX + digitIndex * displayDigitWidth,
                    displayY,
                    displayDigitWidth,
                    displayDigitHeight);
        }
    }




}
