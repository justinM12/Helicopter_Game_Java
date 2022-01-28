package org.csc133.a3;

public class GameSoundPlayer implements Runnable{
    private BGSound backGroundMusic;
    private Sound heliCollideWithHeliSound;
    private Sound playerHeliDestroyedSound;
    private Sound refuelSound;
    private Boolean isSoundEnabled = true;
    private Boolean isSoundPaused = false;
    private Boolean isAllSoundLoaded = false;

    private final String BACKGROUND_MUSIC_FILE_NAME =
            "One_Step_Closer_Song.wav";
    private final String HELI_COLLIDE_HELI_FILE_NAME =
            "heliHeliCollisionSound.wav";
    private final String PLAYER_HELI_DESTROYED_FILE_NAME =
            "playerHelicopterDestroyedSound.wav";
    private final String REFUEL_FILE_NAME = "refuelSound.wav";

    public GameSoundPlayer(){
        System.out.print("Soundddddddd");
        Thread thread = new Thread(){
            public void run(){
                backGroundMusic = new BGSound(BACKGROUND_MUSIC_FILE_NAME);
                heliCollideWithHeliSound = new Sound(HELI_COLLIDE_HELI_FILE_NAME);
                playerHeliDestroyedSound = new Sound(PLAYER_HELI_DESTROYED_FILE_NAME);
                refuelSound = new Sound(REFUEL_FILE_NAME);
                isAllSoundLoaded = true;
                System.out.println("Done loading sounds");
            }
        };
        thread.start();
    }

    public boolean isSoundEnabled(){
        return isSoundEnabled;
    }
    public boolean isAllSoundLoaded(){ return isAllSoundLoaded;}

    public void playHeliHeliCollisionSound(){
        if(!isSoundEnabled || !isAllSoundLoaded())
            return;
        heliCollideWithHeliSound.playSound();
    }

    public void playPlayerHeliDestroyedSound(){
        if(!isSoundEnabled || !isAllSoundLoaded())
            return;
        playerHeliDestroyedSound.playSound();
    }

    public void playRefuelSound(){
        if(!isSoundEnabled || !isAllSoundLoaded())
            return;
        refuelSound.playSound();
    }

    public void disableSound(){
        isSoundEnabled = false;
        backGroundMusic.pause();
    }

    public void enableSound(){
        isSoundEnabled = true;
        backGroundMusic.playSound();
    }

    public void soundPaused(){
        backGroundMusic.pause();
        isSoundPaused = true;
    }

    public void soundUnpaused(){
        isSoundPaused = false;
    }

    @Override
    public void run() {
        if(!isSoundEnabled || isSoundPaused || !isAllSoundLoaded()){
            return;
        }
        backGroundMusic.run();
    }
}
