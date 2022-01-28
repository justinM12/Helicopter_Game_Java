package org.csc133.a3;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;

public class RightTurnCommand extends Command {
    private Game game;
    public RightTurnCommand(Game game){ super("TurnRight");
    this.game = game;}

    @Override
    public void actionPerformed(ActionEvent e){
        game.getGameWorld().changePlayerStickAngle5DegreesRight();
    }
}


