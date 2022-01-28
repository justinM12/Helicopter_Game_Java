package org.csc133.a3;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.FlowLayout;

public class GlassCockpit extends Container implements IGameView {

    private GameClockComponent gameTimeComponent;
    private DigitalDisplayComponent fuelDisplayComponent;
    private DigitalDamageDisplay damageDisplayComponent;
    private DigitalDisplayComponent livesDisplayComponent;
    private DigitalDisplayComponent lastSkyscraperReachedDisplayComponent;
    private DigitalDisplayComponent headingDisplayComponent;

    private GameWorld gameWorld;

    public GlassCockpit(GameWorld gw){
        gameWorld = gw;
        this.setLayout(new FlowLayout());
        initializeComponents();
        addAllComponents();
    }

    private void initializeComponents(){
        damageDisplayComponent = new DigitalDamageDisplay(ColorUtil.GREEN);
        fuelDisplayComponent =
                new DigitalDisplayComponent(4, ColorUtil.GREEN);
        livesDisplayComponent =
                new DigitalDisplayComponent(1, ColorUtil.CYAN);
        lastSkyscraperReachedDisplayComponent =
                new DigitalDisplayComponent(1, ColorUtil.CYAN);
        headingDisplayComponent =
                new DigitalDisplayComponent(3, ColorUtil.YELLOW);
        gameTimeComponent = new GameClockComponent(ColorUtil.CYAN);
    }
    private void addAllComponents(){
        addComponent(buildContainerForDigitalComponentWithLabel(
                gameTimeComponent, "Game Timer"));
        addComponent(buildContainerForDigitalComponentWithLabel(
                fuelDisplayComponent, "Fuel"));
        addComponent(buildContainerForDigitalComponentWithLabel(
                damageDisplayComponent, "Damage"));
        addComponent(buildContainerForDigitalComponentWithLabel(
                livesDisplayComponent, "Lives"));
        addComponent(buildContainerForDigitalComponentWithLabel(
                lastSkyscraperReachedDisplayComponent, "Last"));
        addComponent(buildContainerForDigitalComponentWithLabel(
                headingDisplayComponent, "Heading"));
    }

    private Container buildContainerForDigitalComponentWithLabel(
            Component comp, String labelText) {

        Container containerForDigitalDisplay = new Container();
        containerForDigitalDisplay.setLayout(new BorderLayout());

        containerForDigitalDisplay.addComponent(BorderLayout.CENTER, comp);
        containerForDigitalDisplay.addComponent(
                BorderLayout.NORTH, new Label(labelText));

        return containerForDigitalDisplay;
    }

    public void updateView(){
        Helicopter playerHelicopter = gameWorld.getPlayerHelicopter();
        int livesRemaining = gameWorld.getLivesRemaining();
        int fuelLeft = playerHelicopter.getFuelLevel();
        int damageLevel = playerHelicopter.getDamageLevel();
        SkyScraper lastSkyscrapReached =
                playerHelicopter.getLastSkyScraperReached();
        float heading = playerHelicopter.getHeading();

        fuelDisplayComponent.updateDigitalDisplayValue(fuelLeft);
        damageDisplayComponent.updateDigitalDisplayValue(damageLevel);
        livesDisplayComponent.updateDigitalDisplayValue(livesRemaining);
        lastSkyscraperReachedDisplayComponent
                .updateDigitalDisplayValue(
                        lastSkyscrapReached.getSequenceNumber());
        headingDisplayComponent.updateDigitalDisplayValue((int) heading);

    }

    public void startGameTime(){
        gameTimeComponent.startElapsedTime();
    }

    public void stopGameTime(){
        gameTimeComponent.stopElapsedTime();
    }

    public String getGameTime(){
        FiveDigitClockTime time = gameTimeComponent.getElapsedTime();
        return time.getTimeAsString();
    }

}
