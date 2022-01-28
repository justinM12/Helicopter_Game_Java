package org.csc133.a3;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.util.UITimer;


public class Game extends Form implements Runnable{
    private GameWorld gw;
    private GameWorldSettings gwSettings;
    private UITimer uiTimer;
    private CommandCollection commandCollection;
    private GameSoundPlayer gameSoundPlayer;
    private boolean isAllSoundLoaded = false;

    Button accel;
    Button brake;
    Button turnL;
    Button turnR;

    private boolean isGamePaused = false;

    public Game() {

        commandCollection = new CommandCollection(this);

        setUpGameViewLayout();
        gwSettings = GameWorldSettingsBuilder
                .createDefaultGameWorldSettings(); /* gameWorldSettings contains
                public Final fields that contains initialization parameters,
                such as how big the gameWorld is and how many birds there are
                */

        gameSoundPlayer = new GameSoundPlayer();

        gw = new GameWorld(this, gameSoundPlayer);
        createGameViewComponentsAndInitializeGame(gwSettings, gw);
        play();
        show();
    }

    public GameSoundPlayer getGameSoundPlayer(){
        return gameSoundPlayer;
    }

    public boolean isGamePaused(){
        return isGamePaused;
    }

    public void pauseGame(){
        isGamePaused = true;
        gameSoundPlayer.soundPaused();
        removeKeyCommands();
        enableButtons(false);
        stopUITimer();
        gw.gamePaused();
    }

    public void unPauseGame(){
        if (!isGamePaused){
            return;
        }
        gameSoundPlayer.soundUnpaused();
        gw.gameUnpaused();
        startUiTimer(gwSettings);
        isGamePaused = false;
        assignKeyCommands();
        enableButtons(true);
    }

    public void startUiTimer(GameWorldSettings gameWorldSettings){
        uiTimer = new UITimer(this);
        uiTimer.schedule(
                (int) (gameWorldSettings.TIME_BETWEEN_TICK_IN_SECONDS * 1000),
                true,
                this);
    }

    public void stopUITimer(){
        uiTimer.cancel();
    }

    private void setUpGameViewLayout(){
        setLayout(new BorderLayout());
    }

    private void createGameViewComponentsAndInitializeGame(
            GameWorldSettings gwSettings,
            GameWorld gw){

        GlassCockpit glassCockpit = new GlassCockpit(gw);
        MapView mapView = new MapView(gwSettings, gw, getWidth(), getHeight());
        ButtonControlsDisplay buttonControlsDisplay =
                new ButtonControlsDisplay();
        assignButtonCommands(buttonControlsDisplay);
        assignKeyCommands();
        assignMenuCommands();

        addComponent(
                BorderLayout.NORTH,
                centerFlowLayoutWithComp(glassCockpit));
        addComponent(
                BorderLayout.SOUTH,
                buttonControlsDisplay);
        addComponent(
                BorderLayout.CENTER,
                mapView);

        System.out.println(
                glassCockpit.getWidth() + " " +
                buttonControlsDisplay.getWidth() + " " + mapView.getWidth());
        gw.init(gwSettings, glassCockpit, mapView);
    }

    private Container centerFlowLayoutWithComp(Container con){
        Container container = new Container(new FlowLayout(CENTER));
        container.addComponent(con);
        return container;
    }

    private void assignButtonCommands(ButtonControlsDisplay buttonControlsDisplay){
        accel = buttonControlsDisplay.getAccelerateButton();
        brake = buttonControlsDisplay.getBrakeButton();
        turnL = buttonControlsDisplay.getTurnLeftButton();
        turnR = buttonControlsDisplay.getTurnRightButton();

        accel.setCommand(commandCollection.getAccelCommand());
        brake.setCommand(commandCollection.getBrakeCommand());
        turnL.setCommand(commandCollection.getTurnLCommand());
        turnR.setCommand(commandCollection.getTurnRCommand());
    }

    private void enableButtons(boolean bool){
        accel.setEnabled(bool);
        brake.setEnabled(bool);
        turnL.setEnabled(bool);
        turnR.setEnabled(bool);

        Toolbar toolbar = getToolbar();
        toolbar.findCommandComponent(
                commandCollection.getExitCommandCommand()).setEnabled(bool);
        toolbar.findCommandComponent(
                commandCollection.getChangeStratCommand()).setEnabled(bool);
        toolbar.findCommandComponent(
                commandCollection.getGiveAboutInfoCommand()).setEnabled(bool);
        toolbar.findCommandComponent(
                commandCollection.getGiveHelpInfoCommand()).setEnabled(bool);
    }

    private void assignMenuCommands(){
        Toolbar toolbar = getToolbar();
        toolbar.addCommandToSideMenu(commandCollection.getExitCommandCommand());
        toolbar.addCommandToSideMenu(commandCollection.getChangeStratCommand());
        toolbar.addCommandToSideMenu(commandCollection.getGiveAboutInfoCommand());
        toolbar.addCommandToSideMenu(commandCollection.getGiveHelpInfoCommand());
        toolbar.addCommandToSideMenu(commandCollection.getPauseCommand());
        toolbar.addCommandToSideMenu(commandCollection.getToggleSoundCommand());

    }
    private void assignKeyCommands(){
        this.addKeyListener('a', commandCollection.getAccelCommand());
        this.addKeyListener('b', commandCollection.getBrakeCommand());
        this.addKeyListener('l', commandCollection.getTurnLCommand());
        this.addKeyListener('r', commandCollection.getTurnRCommand());
        this.addKeyListener(
                'x', commandCollection.getExitCommandCommand());
    }

    private void removeKeyCommands(){
        this.removeKeyListener('a', commandCollection.getAccelCommand());
        this.removeKeyListener('b', commandCollection.getBrakeCommand());
        this.removeKeyListener('l', commandCollection.getTurnLCommand());
        this.removeKeyListener('r', commandCollection.getTurnRCommand());
        this.removeKeyListener(
                'x', commandCollection.getExitCommandCommand());
    }

    private void play() {
        // code here to accept and
        // execute user commands that
        // operate on the game world
        //(refer to “Appendix - CN1
        //Notes” for accepting
        //keyboard commands via a text
        //field located on the form)

        this.show();
    }

    public void run(){
        gw.gameClockHasTicked();
        if(!gameSoundPlayer.isAllSoundLoaded()){
            return;
        }
        gameSoundPlayer.run();
    }

    public GameWorld getGameWorld(){
        return gw;
    }
    public GameWorldSettings getGameWorldSettings(){
        return gwSettings;
    }

}

