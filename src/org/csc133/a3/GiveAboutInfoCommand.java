package org.csc133.a3;

import com.codename1.ui.Command;
import com.codename1.ui.Dialog;
import com.codename1.ui.events.ActionEvent;

public class GiveAboutInfoCommand extends Command {
    private Game game;
    public GiveAboutInfoCommand(Game game){ super("GiveAboutInfo");
    this.game = game;}

    @Override
    public void actionPerformed(ActionEvent e){
        Dialog.show(
                "About Info",
                "Justin Moua, CSC133, Version 1",
                "Ok",
                "" );
    }
}

