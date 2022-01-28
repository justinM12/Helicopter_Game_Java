package org.csc133.a3;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Component;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.geom.Dimension;

import java.util.Calendar;


public class GameClockComponent extends Component{

    private int currentLedColor;
    private int ledColor;
    private int ledColorWhenTimerReaches10Min =
            ColorUtil.argb(255, 255, 0, 0);
    private int ledColorOfTenthsDigit = ColorUtil.GRAY;
    private final int NUM_DIGITS_SHOWING = 6;
    private final int COLON_POSITION = 2;
    private final int DECIMAL_DIGIT_POSITION = 4; // 0 is first pos, 5 is last

    private Image[] clockDigits = new Image[6];;
    private Image[] digitImages;
    private Image colonImage;
    private Image[] digitWithDecimalImages;
    private FiveDigitClockTime startTimeValue;
    private FiveDigitClockTime currentTime;
    private FiveDigitClockTime elapseTime;
    private Boolean isElapsedTimeStopped = true;
    private Boolean isTimeSynced = false;

    private int lastSecond = 0;

    public GameClockComponent(int color){
        ledColor = color;
        currentLedColor = color;
        initializeDigits();

        startElapsedTime(); // temporarily started here for testing
    }

    private void initializeDigits(){
        digitImages = DigitalDisplayComponent.getDigitsImages();
        digitWithDecimalImages =
                DigitalDisplayComponent.getDigitsWithDecimalPointImages();
        colonImage = DigitalDisplayComponent.getColonImage();

        for(int i = 0; i < NUM_DIGITS_SHOWING; i++){
            clockDigits[i] = digitImages[0];
        }
        clockDigits[COLON_POSITION] = colonImage;
        clockDigits[DECIMAL_DIGIT_POSITION] = digitWithDecimalImages[0];
    }

    public void startElapsedTime(){
        currentLedColor = ledColor;
        elapseTime = new FiveDigitClockTime(0,0,0);
        startTimeValue = getStartTime();
        updateClock();
        isElapsedTimeStopped = false;

    }
    public void resetResetElapsedTime(){
        startElapsedTime();
    }

    public void stopElapsedTime(){
        isElapsedTimeStopped = true;
    }

    public FiveDigitClockTime getElapsedTime(){
        return elapseTime;
    }

    private void updateClock(){

        Calendar rightNow = Calendar.getInstance();
        int minuets = rightNow.get(Calendar.MINUTE);
        int seconds = rightNow.get(Calendar.SECOND);
        int milliseconds = rightNow.get(Calendar.MILLISECOND);
        int formattedMilliseconds = (int) ((milliseconds/1000.0)*10.0); // converts to single digit

        if (startTimeValue.getSeconds() != seconds){
            startTimeValue.setSeconds(seconds);
            if(elapseTime.getSeconds() + 1 > 60){
                elapseTime.setSeconds(0);
                elapseTime.setMinuets(elapseTime.getMinuets() + 1);
            }else{
                elapseTime.setSeconds(elapseTime.getSeconds() + 1);
            }
        }
        ifTimerReaches10MinThenChangeLedColor(elapseTime);

        clockDigits[0] = digitImages[elapseTime.getFirstDigit()];
        clockDigits[1] = digitImages[elapseTime.getSecondDigit()];
        clockDigits[3] = digitImages[elapseTime.getThirdDigit()];
        clockDigits[4] = digitWithDecimalImages[elapseTime.getFourthDigit()];
        try{
            clockDigits[5] = digitImages[elapseTime.getFifthDigit()];
        }catch(ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        };
    }

    private void ifTimerReaches10MinThenChangeLedColor(
            FiveDigitClockTime elapsedTime) {
        int minuets = elapsedTime.getMinuets();
        if ( minuets >= 10){
            currentLedColor = ledColorWhenTimerReaches10Min;
        }
    }

    private FiveDigitClockTime getStartTime(){
        Calendar rightNow = Calendar.getInstance();
        int minuets = rightNow.get(Calendar.MINUTE);
        int seconds = rightNow.get(Calendar.SECOND);
        int milliseconds = rightNow.get(Calendar.MILLISECOND);
        int formattedMilliseconds = (int) ((milliseconds/1000.0)*10.0); // converts to single digit

        return new FiveDigitClockTime(minuets, seconds, formattedMilliseconds);
    }

    public void setLedColor(int ledColor){
        this.ledColor = ledColor;
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

    public boolean animate(){
        if (isElapsedTimeStopped){

        }else{
            updateClock();
        }

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

        int digitWidth = clockDigits[0].getWidth();
        int digitHeight = clockDigits[0].getHeight();
        int clockWidth = NUM_DIGITS_SHOWING * digitWidth;

        float scaleFactor = Math.min(
                getInnerHeight()/(float) digitHeight,   /* getInnerHeight()
                    is a method from Component that
                    returns the height of the region that this component
                    has been given to be drawn in
                */
                getInnerWidth()/(float) clockWidth); /* We take the min scale
                    to ensure that when the clock images are scaled they fit
                    in the drawing region
                */

        int displayDigitWidth = (int) (scaleFactor * digitWidth);
        int displayDigitHeight = (int) (scaleFactor * digitHeight);
        int displayClockWidth = displayDigitWidth * NUM_DIGITS_SHOWING;

        int displayX = getX() + (getWidth() - displayClockWidth) / 2; /* getX()
            returns the origin location for the region given to draw this
            component in. displayX is the location to draw this component so
            that it is in the center of the drawing region
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

        g.setColor(currentLedColor);
        g.fillRect(
                displayX + COLOR_PAD,
                displayY + COLOR_PAD,
                displayClockWidth - COLOR_PAD * 2,
                displayDigitHeight - COLOR_PAD * 2);

        g.setColor(ledColorOfTenthsDigit);
        g.fillRect(
                displayX + displayClockWidth - displayDigitWidth,
                displayY + COLOR_PAD,
                displayDigitWidth - COLOR_PAD,
                displayDigitHeight - COLOR_PAD * 2
        );

        for(int digitIndex = 0; digitIndex < NUM_DIGITS_SHOWING; digitIndex++) {
            g.drawImage(
                    clockDigits[digitIndex],
                    displayX + digitIndex * displayDigitWidth,
                    displayY,
                    displayDigitWidth,
                    displayDigitHeight);
        }

    }

}
