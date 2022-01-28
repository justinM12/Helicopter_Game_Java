package org.csc133.a3;

import com.codename1.ui.*;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.FlowLayout;

import static com.codename1.ui.Component.CENTER;

public class ExitCommand extends Command {
    private Game game;
    public ExitCommand(Game game){
        super("Exit");
        this.game = game;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        Dialog d = new Dialog("Do you want to exit?");

        Button confirmButton = new Button("Yes");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                d.dispose();
                System.exit(0);
            }
        });
        Button cancelButton = new Button("No");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                d.dispose();
            }
        });


        d.setLayout(new BorderLayout());
        Container con = new Container();
        con.setLayout(new FlowLayout(CENTER));

        con.addComponent(confirmButton);
        con.addComponent(cancelButton);

        d.addComponent(BorderLayout.CENTER, con);
        d.show();
    }
}


