package org.csc133.a3;

import com.codename1.media.Media;
import com.codename1.media.MediaManager;
import com.codename1.ui.Display;

import java.io.InputStream;

public class BGSound implements Runnable{
    private Media bgSound;
    private boolean isPaused = false;

    public BGSound(String fileName){
        try{
            System.out.println(fileName);
            InputStream is = Display
                    .getInstance()
                    .getResourceAsStream(
                            getClass(),
                            "/" + fileName);

            bgSound = MediaManager.createMedia(is, "audio/wav");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Media getSound(){
        return bgSound;
    }

    public void playSound(){
        bgSound.setTime(0);
        bgSound.play();
    }

    public void pause(){
        bgSound.pause();
    }

    @Override
    public void run() {
        if(bgSound.isPlaying() || isPaused){
            return;
        }

        playSound();
    }
}
