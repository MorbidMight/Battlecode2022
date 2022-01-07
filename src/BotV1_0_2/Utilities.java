package BotV1_0_2;
import java.util.*;
import battlecode.common.*;
public class Utilities {

    static final double PI = 3.141592653589;

    static MapLocation pointOnCircle(double r, double x, double y, int thetaDegrees)
    {
        double theta = (Math.toRadians(thetaDegrees));
        int xPos = (int) (x + r * Math.cos(theta));
        int yPos = (int) (y + r * Math.sin(theta));
        if (xPos < 0 || yPos < 0)
        {
            return null;
        }
        return (new MapLocation(xPos, yPos));
    }
    public static void main(String[] args)
    {
        System.out.println(pointOnCircle(6,0,0,45));
    }
}
