package org.csc133.a3;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;

public class LeftTurnCommand extends Command {
    private Game game;
    public LeftTurnCommand(Game game){ super("TurnLeft");
    this.game = game;}

    @Override
    public void actionPerformed(ActionEvent e){
        game.getGameWorld().changePlayerStickAngle5DegreesLeft();
    }
}


