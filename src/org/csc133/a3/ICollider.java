package org.csc133.a3;

public interface ICollider {
    public boolean collidesWith(GameObject otherObject);
    public void handleCollision(GameObject otherObject);
}
