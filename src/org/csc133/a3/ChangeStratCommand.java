package org.csc133.a3;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;

public class ChangeStratCommand extends Command {
    private Game game;
    public ChangeStratCommand(Game game){
        super("ChangeStrat");
        this.game = game;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        GameObjectCollection gameObjectCollection =
                game.getGameWorld().getGameObjectCollection();
        gameObjectCollection.forEach(
                (gObj) -> {ifGameObjectIsNphInvokeSetStrategy(gObj);}
        );
    }

    private void ifGameObjectIsNphInvokeSetStrategy(GameObject gameObject){
        if(!(gameObject instanceof NonPlayerHelicopter)){
            return;
        }
        ((NonPlayerHelicopter) gameObject).setStrategy();
    }

}


