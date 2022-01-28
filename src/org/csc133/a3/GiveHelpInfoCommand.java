package org.csc133.a3;

import com.codename1.ui.Command;
import com.codename1.ui.Dialog;
import com.codename1.ui.events.ActionEvent;

public class GiveHelpInfoCommand extends Command {
    private Game game;
    public GiveHelpInfoCommand(Game game){ super("GiveHelpInfo");
    this.game = game;}

    @Override
    public void actionPerformed(ActionEvent e){
        Dialog.show("Help Page", "Help Info", "Ok", "" );

    }
}


