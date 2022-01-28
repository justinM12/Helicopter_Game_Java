package org.csc133.a3;

import java.util.ArrayList;

public class FlightPath {
    private ArrayList<IWayPoint> wayPointArrayList;
    private IWayPoint firstWayPoint;
    private IWayPoint lastWayPoint;

    public FlightPath(){
        wayPointArrayList = new ArrayList<IWayPoint>();
    }

    public void addWaypoint(IWayPoint wp){
        wayPointArrayList.add(wp);

    }

    public void removeWayPoint(int sequenceNumber) throws NullPointerException{
        wayPointArrayList.remove(sequenceNumber);
    }

    public void removeAllWayPoints(){
        wayPointArrayList.clear();
    }

    public int getFlightPathLength(){
        return wayPointArrayList.size();
    }

    public ArrayList<IWayPoint> getFlightPathWayPoints(){
        return wayPointArrayList;
    }

    public IWayPoint getWaypoint(int sequenceNumber){
        IWayPoint wp = null;
        for(IWayPoint wayPoint : wayPointArrayList){
            if(wayPoint.getSequenceNumber() == sequenceNumber)
                return wayPoint;
        }

        return wp;
    }

    private boolean ifSequenceNumberMatches(IWayPoint wp, int sequenceNum){
        if(wp.getSequenceNumber() == sequenceNum)
            return true;
        else
            return false;
    }

    public IWayPoint getFirstWayPoint() {
        if(getFlightPathLength() == 0){
            throw new IllegalStateException("Flight path has 0 waypoints");
        }else{
            return getWaypoint(0);
        }
    }

    public IWayPoint getLastWayPoint() {
        return wayPointArrayList.get(wayPointArrayList.size() - 1);
    }

}
