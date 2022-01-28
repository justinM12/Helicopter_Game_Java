package org.csc133.a3;

import com.codename1.ui.Command;

import java.util.ArrayList;

public class CommandCollection {

    private ArrayList<Command> commandArrayList = new ArrayList<Command>();
    private Game ga;

    private Command accel;
    private Command brake;
    private Command turnR;
    private Command turnL;
    private Command exitCommand;
    private Command changeStrat;
    private Command giveAboutInfo;
    private Command giveHelpInfo;
    private Command pause;
    private Command toggleSound;

    public CommandCollection(Game game){
        ga = game;
        initializeCommands(ga);


    }

    private void initializeCommands(Game game){
        accel = new AccelerateCommand(game);
        brake = new BrakeCommand(game);
        turnR = new RightTurnCommand(game);
        turnL = new LeftTurnCommand(game);
        exitCommand = new ExitCommand(game);
        changeStrat = new ChangeStratCommand(game);
        giveAboutInfo = new GiveAboutInfoCommand(game);
        giveHelpInfo = new GiveHelpInfoCommand(game);
        pause = new PauseCommand(game);
        toggleSound = new ToggleSoundCommand(game);
    }

    public Command getToggleSoundCommand(){return toggleSound;}

    public Command getPauseCommand(){return pause;}

    public Command getAccelCommand() {
        return accel;
    }

    public Command getBrakeCommand() {
        return brake;
    }

    public Command getTurnRCommand() {
        return turnR;
    }

    public Command getTurnLCommand() {
        return turnL;
    }

    public Command getExitCommandCommand() {
        return exitCommand;
    }

    public void setExitCommandCommand(Command exitCommand) {
        this.exitCommand = exitCommand;
    }

    public Command getChangeStratCommand() {
        return changeStrat;
    }

    public Command getGiveAboutInfoCommand() {
        return giveAboutInfo;
    }

    public Command getGiveHelpInfoCommand() {
        return giveHelpInfo;
    }
}
