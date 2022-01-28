package org.csc133.a3;

public abstract class Fixed extends  GameObject{

    public Fixed(){
        super();
    }

    public Fixed(int size, int argbColor){
        super(size, argbColor);
    }

    public Fixed(int size, Location location, int argbColor){
        super(size, location, argbColor);
    }
}
