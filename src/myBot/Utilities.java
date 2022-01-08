package myBot;

import java.util.*;

import battlecode.common.*;

public class Utilities {

    static final double PI = 3.141592653589;

    static MapLocation pointOnCircle(double r, double x, double y, int thetaDegrees) {
        double theta = (Math.toRadians(thetaDegrees));
        int xPos = (int) (x + r * Math.cos(theta));
        int yPos = (int) (y + r * Math.sin(theta));
        if (xPos < 0 || yPos < 0) {
            return null;
        }
        return (new MapLocation(xPos, yPos));
    }

    public static void main(String[] args) {
        System.out.println(pointOnCircle(6, 0, 0, 45));
    }


    //Takes in two directions and return how far apart they are
    static int compareDirections(Direction d1, Direction d2) {
        int n1 = -1, n2 = -1;
        for (int i = 0; i < 8; i++) {
            if (d1.equals(RobotPlayer.directions[i]))
                n1 = i;
        }

        for (int i = 0; i < 8; i++) {
            if (d2.equals(RobotPlayer.directions[i]))

                n2 = i;
        }
        int out = Math.abs(n1 - n2);
        if (out > 4)
            out = 8 - out;
        return out;
    }

    static Direction oppositeDirection(Direction input) {
        int x=0;
        for (int i = 0; i < 8; i++) {
            if (input.equals(RobotPlayer.directions[i])) {
                x = i;
                break;
            }
        }
        return RobotPlayer.directions[x+4%8];
    }


}

