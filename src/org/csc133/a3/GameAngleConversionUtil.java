package org.csc133.a3;

import com.codename1.util.MathUtil;

public class GameAngleConversionUtil {

    public static float convertHeadingAngleToGameWorldAngle(float heading){
        if ((int) heading == 0){
            return 90;
        }

        if (heading < 0)
            heading = convertNegativeHeadingToPositiveHeading(heading);

        if (heading > 0){
            if (heading < 90){  // if heading in first quad
                return 90 - heading;
            }else if(heading > 90 && heading <=180){ // if heading in fourth quad
                return 360 - (90 - (180 - heading));
            }else if(heading > 180 && heading <=270){ // if heading in third quad
                return (90 - (heading - 180)) + 180;
            }else if(heading > 270){ // if heading in second quad
                return (360 - heading) + 90;
            }
        }
        return -1;
    }

    public static float convertNegativeHeadingToPositiveHeading (float heading){
        if (heading > -90){  // if heading in second quad
            return 360 + heading;
        }else if(heading > -180 && heading <= 90){ // if heading in third quad
            return 360 + heading;
        }else if(heading > -270 && heading <= 180){ // if heading in fourth quad
            return 360 + heading;
        }else if(heading < -270){ // if heading in first quad
            return 360 + heading;
        }
        return -1;
    }

    public static float convertXYDistanceToHeadingAngle(
            float xDistance,
            float yDistance){

        float xOverY = xDistance/yDistance;

        float headingRad = (float) MathUtil.atan(xDistance/yDistance);
        float headingDegree = (float) Math.toDegrees(headingRad);
        if (xOverY > 0){ // angle in the first or thrid quadrant
            if(xDistance > 0)   // in first
                return headingDegree;
            else    // in third
                return headingDegree + 180;
        }else{  // angle in the second or fourth quadrant
            if(xDistance > 0)   // in fourth
                return (360 + headingDegree) - 180;
            else    // in second
                return 360 + headingDegree;
        }
    }
}
