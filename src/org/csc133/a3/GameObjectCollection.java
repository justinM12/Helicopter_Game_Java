package org.csc133.a3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class GameObjectCollection {
    private ArrayList<GameObject> gameObjectArrayList;

    public GameObjectCollection(){
        gameObjectArrayList = new ArrayList<GameObject>();
    }

    public void add(GameObject gameObject){
        gameObjectArrayList.add(gameObject);
    }

    public void remove(){

    }

    public GameObject get(int index){
        return gameObjectArrayList.get(index);
    }

    public int size(){
        return gameObjectArrayList.size();
    }

    public void forEach(Consumer<? super GameObject> consumer){
        gameObjectArrayList.forEach(consumer);
    }

    public List<GameObject> getAllNph(){
        List<GameObject> nphList =
                gameObjectArrayList
                        .stream()
                        .filter((gm)->isNph(gm))
                        .collect(Collectors.toList());
        return nphList;
    }

    private boolean isNph(GameObject gameObject){
        if(gameObject instanceof  NonPlayerHelicopter){
            return true;
        }
        return false;
    }

    public List<GameObject> getAllHelicopters(){
        List<GameObject> heliList =
                gameObjectArrayList
                        .stream()
                        .filter((gm)->isHeli(gm))
                        .collect(Collectors.toList());
        return heliList;
    }

    private Boolean isHeli(GameObject gameObject){
        if(gameObject instanceof  Helicopter){
            return true;
        }
        return false;
    }

    public List<GameObject> getAllBirds(){
        List<GameObject> list =
                gameObjectArrayList
                        .stream()
                        .filter((gm)->isBird(gm))
                        .collect(Collectors.toList());
        return list;
    }

    private Boolean isBird(GameObject gameObject){
        if(gameObject instanceof  Bird){
            return true;
        }
        return false;
    }

    public List<GameObject> getAllRefuelBlimps(){
        List<GameObject> list =
                gameObjectArrayList
                        .stream()
                        .filter((gm)->isRefuelBlimp(gm))
                        .collect(Collectors.toList());
        return list;
    }

    private Boolean isRefuelBlimp(GameObject gameObject){
        if(gameObject instanceof  RefuelingBlimp){
            return true;
        }
        return false;
    }

    public List<GameObject> getAllSkyscrapers(){
        List<GameObject> list =
                gameObjectArrayList
                        .stream()
                        .filter((gm)->isSkyscraper(gm))
                        .collect(Collectors.toList());
        return list;
    }

    private Boolean isSkyscraper(GameObject gameObject){
        if(gameObject instanceof  SkyScraper){
            return true;
        }
        return false;
    }

    public List<GameObject> getAllGameObjects(){
        return gameObjectArrayList;
    }


}
