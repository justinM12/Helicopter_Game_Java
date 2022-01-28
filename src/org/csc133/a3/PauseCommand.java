package org.csc133.a3;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;

public class PauseCommand extends Command {
    private Game game;
    public PauseCommand(Game game){
        super("Pause");
        this.game = game;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(!game.isGamePaused()) {
            game.pauseGame();
        }else{
            game.unPauseGame();
        }
    }
}
