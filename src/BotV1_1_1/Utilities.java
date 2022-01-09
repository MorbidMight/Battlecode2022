package BotV1_1_1;

import battlecode.common.*;

import java.util.Map;

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

    public static MapLocation findCenter(RobotController rc) {
        MapLocation k = new MapLocation(rc.getMapWidth() / 2, rc.getMapHeight() / 2);
        return k;
    }

    public static void main(String[] args) {
        MapLocation[] test = {new MapLocation(2, 22), new MapLocation(46, 2)};
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
        int x = 0;
        for (int i = 0; i < 8; i++) {
            if (input.equals(RobotPlayer.directions[i])) {
                x = i;
                break;
            }
        }
        return RobotPlayer.directions[x + 4 % 8];
    }

    static MapLocation[] enemyArchonLocater(MapLocation[] ourArchons, RobotController rc) {
        MapLocation[] enemyArchons = new MapLocation[ourArchons.length];
        boolean nonRotationalSymmetery = false;
        for (int x = 0; x < ourArchons.length; x++) {
            MapLocation Rotation180 = new MapLocation(rc.getMapWidth() - ourArchons[x].x - 1, rc.getMapHeight() - ourArchons[x].y - 1);
            for (int y = 0; y < ourArchons.length; y++) {
                if (Rotation180.equals(ourArchons[y])) {
                    nonRotationalSymmetery = true;
                }
            }
            System.out.println(nonRotationalSymmetery);
            if (nonRotationalSymmetery) {
                enemyArchons[x] = new MapLocation(ourArchons[x].x, rc.getMapHeight() - ourArchons[x].y - 1);
            } else {
                enemyArchons[x] = Rotation180;
            }
        }
        return enemyArchons;
    }

    static void suicideBooth(RobotController rc) throws GameActionException {
        RobotInfo[] K = rc.senseNearbyRobots();
        if (K.length >= 8)
            rc.disintegrate();
    }

    static MapLocation checkers(RobotController rc, MapLocation in) throws GameActionException {
        if (in.x + in.y % 2 == 1) {
            return in;
        } else {
            int j = 0;
            MapLocation[] potential = new MapLocation[4];
            potential[0] = new MapLocation(in.x + 1, in.y);
            potential[1] = new MapLocation(in.x, in.y + 1);
            potential[2] = new MapLocation(in.x - 1, in.y);
            potential[3] = new MapLocation(in.x, in.y - 1);
            for (int i = 0; i < 4; i++) {
                if (!rc.isLocationOccupied(potential[i])) {
                    j++;
                }
            }
            int[] clean = new int[j];
            j = 0;
            for (int i = 0; i < 4; i++) {
                if (!rc.isLocationOccupied(potential[i])) {
                   clean[j]=i;
                }
            }
            return potential[clean[RobotPlayer.rng.nextInt(clean.length)]];
        }
    }
}

