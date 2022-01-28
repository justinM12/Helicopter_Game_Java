package org.csc133.a3;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Container;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;

import java.util.List;

public class MapView extends Container implements IGameView {
    private int viewWidth;
    private int viewHeigth;

    private int laidOutCounter = 2; // 3 components in border layout so laidout gets called 3 times, load game objects on third call

    private Point gameOriginLocationForPaint; // orgin location is bottom left corner
    private GameWorld gameWorld;

    private boolean isDrawAllowed = false;
    private boolean hasInitialized = false;

    public MapView(GameWorldSettings gwSettings, GameWorld gw, int width, int height){
        viewWidth = width;
        viewHeigth = height;
        gameWorld = gw;
        isDrawAllowed = true;
    }

    public int getViewWidth(){
        return viewWidth;
    }

    public int getViewHeight(){
        return viewHeigth;
    }

    public void updateView(){
        repaint();
    }

    public void start(){
        getComponentForm().registerAnimated(this);
    }

    public void stop(){
        getComponentForm().deregisterAnimated(this);
    }

    public void laidOut(){
        if (laidOutCounter == 0){
            if (!hasInitialized){
                setMapWidthAndHeightAndTellGameWorldToLoadGameObjects();
                hasInitialized = true;
                this.start();
            }
        }else{
            laidOutCounter--;
        }

        System.out.println(getWidth() + " " + getHeight());
        System.out.println(getX() + " " + getY());
        System.out.println(getParent());
    }

    private void setMapWidthAndHeightAndTellGameWorldToLoadGameObjects(){
        viewWidth = Math.abs(getWidth());
        viewHeigth = Math.abs(getHeight());
        Location.setMaxXPos((int) viewWidth);
        Location.setMaxYPos((int) viewHeigth);
        gameOriginLocationForPaint = new Point(getX(), getY() + viewHeigth);
        gameWorld.loadGameObjectsAndStartUITimer();
    }

    public boolean animate(){

        if (!isDrawAllowed && !hasInitialized && haveGameObjectImagesLoaded()){
            return false;
        }
        return true; // tells system to repaint the clock
    }

    protected Dimension calcPreferredSize(){
        return new Dimension(viewWidth
                , viewHeigth);
    }

    private boolean haveGameObjectImagesLoaded(){
        if(SkyScraper.getSkyScraperImages() == null
                && RefuelingBlimp.getBlimpImage() == null
                && Bird.getBirdImage() == null
                && Helicopter.getHeliImage() == null
        ){
            return false;
        }

        return true;
    }

    @Override
    public void paint(Graphics g){
        super.paint(g);
        drawBackGround(g, viewWidth, viewHeigth);
        drawAllGameObjects(g, gameOriginLocationForPaint);
    }

    private void drawBackGround(Graphics g, int width, int height){
        g.setColor(ColorUtil.LTGRAY);
        g.fillRect(getX(), getY(), viewWidth, viewHeigth);

    }

    private void drawAllGameObjects(Graphics g, Point pGameOriginLocationForPaint){

        try{
            GameObjectCollection gameObjectArrayList =
                    gameWorld.getGameObjectCollection();
            drawSkyScrapers(
                    gameObjectArrayList,
                    g,
                    pGameOriginLocationForPaint);
            drawRefuelBlimps(
                    gameObjectArrayList,
                    g,
                    pGameOriginLocationForPaint);
            drawHelicopters(
                    gameObjectArrayList,
                    g,
                    pGameOriginLocationForPaint);
            drawBirds(gameObjectArrayList, g, pGameOriginLocationForPaint);



        }catch(NullPointerException e){
            e.printStackTrace();
        }

    }

    private void drawHelicopters(
            GameObjectCollection gObjectList,
            Graphics g,
            Point pGameOriginLocationForPaint){

        List<GameObject> list = gObjectList.getAllHelicopters();
        list.forEach(
                (obj)->{obj.draw(g, pGameOriginLocationForPaint);});
    }

    private void drawBirds(GameObjectCollection gObjectList,
                           Graphics g,
                           Point pGameOriginLocationForPaint){

        List<GameObject> list = gObjectList.getAllBirds();
        list.forEach(
                (obj)->{obj.draw(g, pGameOriginLocationForPaint);});

    }

    private void drawRefuelBlimps(GameObjectCollection gObjectList,
                                  Graphics g,
                                  Point pGameOriginLocationForPaint){

        List<GameObject> list = gObjectList.getAllRefuelBlimps();
        list.forEach(
                (obj)->{obj.draw(g, pGameOriginLocationForPaint);});

    }

    private void drawSkyScrapers(
            GameObjectCollection gObjectList,
            Graphics g,
            Point pGameOriginLocationForPaint){

        List<GameObject> list = gObjectList.getAllSkyscrapers();
        list.forEach(
                (obj)->{obj.draw(g, pGameOriginLocationForPaint);});

    }
}