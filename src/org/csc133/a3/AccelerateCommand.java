package org.csc133.a3;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;

public class AccelerateCommand extends Command {
    private Game game;

    public AccelerateCommand(Game game){
        super("Accelerate");
        this.game = game;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        game.getGameWorld().acceleratePlayerHelicopter();
    }
}


