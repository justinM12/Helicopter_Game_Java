package org.csc133.a3;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;

public class BrakeCommand extends Command {
    private Game game;
    public BrakeCommand(Game game){
        super("Brake");
        this.game = game;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        game.getGameWorld().deceleratePlayerHelicopter();
    }
}


