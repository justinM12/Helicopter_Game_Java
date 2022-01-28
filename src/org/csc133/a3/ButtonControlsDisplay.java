package org.csc133.a3;

import com.codename1.components.ScaleImageButton;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Image;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.layouts.Layout;

import java.io.IOException;

public class ButtonControlsDisplay extends Container {
    private Button accelerateButton;
    private Button brakeButton;
    private Button turnLeftButton;
    private Button turnRightButton;

    private Image rightArrow;
    private Image leftArrow;
    private Image upArrow;
    private Image downArrow;

    public ButtonControlsDisplay(){

        getImages();
        createButtons();
        Layout layout = new GridLayout(1,4);
        setLayout(layout);
        addButtonsToContainer();
    }
    private void getImages(){
        try{
            rightArrow = Image.createImage("/arrow.png");
        }catch (IOException e){
            e.printStackTrace();
        }
        leftArrow = rightArrow.flipHorizontally(true);
        downArrow = rightArrow.rotate90Degrees(true);
        upArrow = downArrow.rotate180Degrees(true);

    }

    private void createButtons(){
        accelerateButton = new ScaleImageButton(upArrow);
        brakeButton = new ScaleImageButton(downArrow);
        turnLeftButton = new ScaleImageButton(leftArrow);
        turnRightButton = new ScaleImageButton(rightArrow);
    }

    private void addButtonsToContainer(){
        addComponent(turnLeftButton);
        addComponent(accelerateButton);
        addComponent(brakeButton);
        addComponent(turnRightButton);
    }

    public void laidOut(){
        System.out.println(getWidth() + " p " + getHeight());
    }

    protected Dimension calcPreferredSize(){
        return new Dimension( 1000
                , 100);
    }

    public Button getAccelerateButton() {
        return accelerateButton;
    }

    public Button getBrakeButton() {
        return brakeButton;
    }

    public Button getTurnLeftButton() {
        return turnLeftButton;
    }

    public Button getTurnRightButton(){
        return turnRightButton;
    }
}
