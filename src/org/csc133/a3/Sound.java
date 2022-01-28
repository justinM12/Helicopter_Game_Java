package org.csc133.a3;

import com.codename1.media.Media;
import com.codename1.media.MediaManager;
import com.codename1.ui.Display;

import java.io.InputStream;

public class Sound {
    private Media sound;

    public Sound(String fileName){

        try{
            System.out.println(fileName);
            InputStream is = Display
                    .getInstance()
                    .getResourceAsStream(
                            getClass(),
                            "/" + fileName);

            sound = MediaManager.createMedia(is, "audio/wav");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Media getSound(){
        return sound;
    }

    public void playSound(){
        sound.setTime(0);
        sound.play();
    }
}
