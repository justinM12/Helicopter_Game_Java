package org.csc133.a3;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;

public class ToggleSoundCommand extends Command {
    private Game game;
    public ToggleSoundCommand(Game game){
        super("ToggleSound");
        this.game = game;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        GameSoundPlayer gameSoundPlayer = game.getGameSoundPlayer();
        if (gameSoundPlayer.isSoundEnabled()){
            gameSoundPlayer.disableSound();
        }else{
            gameSoundPlayer.enableSound();
        }
    }
}
