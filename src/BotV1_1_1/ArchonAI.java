package BotV1_1_1;

import battlecode.common.*;

import java.io.Console;

public class ArchonAI {
    static int labCount = 0;
    /**
     * Run a single turn for an Archon.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */

    static void runArchon(RobotController rc) throws GameActionException {
        // Pick a direction to build in.
        //check for a possible repair
        Team ally = rc.getTeam();
        Direction dir = RobotPlayer.directions[RobotPlayer.rng.nextInt(RobotPlayer.directions.length)];
        RobotInfo[] k = rc.senseNearbyRobots(20, ally);
        //for loop to check which possible heal is lowest on health
        int q = 100; // holder variable for lowest possible health to heal
        int w = 0; // holder variable for index of lowest possible heal
        if (k.length >= 1) {
            for (int a = 0; a < k.length; a++) {
                if (k[a].getHealth() < q) {
                    q = k[a].getHealth();
                    w = a;
                }
            }
            if (q < 15 && rc.canRepair(k[w].getLocation()))
                rc.repair(k[w].getLocation());
        }
        if(RobotPlayer.turnCount > 500 && labCount == 0 && rc.getMapHeight() > 30 && rc.getMapWidth() > 30){
            if(rc.getTeamLeadAmount(rc.getTeam()) >= 950){
                if(rc.canBuildRobot(RobotType.BUILDER, rc.getLocation().directionTo(Utilities.findCenter(rc)).opposite())){
                    rc.buildRobot(RobotType.BUILDER, rc.getLocation().directionTo(Utilities.findCenter(rc)).opposite());
                    labCount++;
                    rc.writeSharedArray(63, 1);
                }
            }
        }
        else if (rc.getTeamLeadAmount(rc.getTeam()) >= 180 && (RobotPlayer.turnCount < 500 || rc.readSharedArray(63) == 2 || rc.getMapWidth() < 30 || rc.getMapHeight() < 30)) {
            double randDouble = Math.random();
            if (randDouble > 0.65) {
                // Let's try to build a miner.
                dir= RobotPlayer.directions[RobotPlayer.rng.nextInt(4)*2];
                rc.setIndicatorString("Trying to build a miner");
                if (rc.canBuildRobot(RobotType.MINER, dir)) {
                    rc.buildRobot(RobotType.MINER, dir);

                }
            } else if (randDouble > 0.1 && randDouble < 0.65) {
                // Let's try to build a soldier.
                rc.setIndicatorString("Trying to build a soldier");
                if (rc.canBuildRobot(RobotType.SOLDIER, dir)) {
                    rc.buildRobot(RobotType.SOLDIER, dir);

                }
            }
            else if (randDouble < 0.05) {//ensure there aren't more than one builder per archon
                rc.setIndicatorString("Trying to build a builder");
                if (rc.canBuildRobot(RobotType.BUILDER, dir)) {
                    rc.buildRobot(RobotType.BUILDER, dir);
                }
            }
        }
    }
    public static void writeCoordsToArray(RobotController rc) throws GameActionException {   //This code doesn't work if an archon is at 0,0
        int AX1 = rc.readSharedArray(51);
        int AY1 = rc.readSharedArray(52);
        if (AX1 == 0 && AY1 == 0) {
            rc.writeSharedArray(51, rc.getLocation().x);
            rc.writeSharedArray(52, rc.getLocation().y);
        } else if (rc.getLocation().x == AX1 && rc.getLocation().y == AY1) {
            return;
        } else {
            int AX2 = rc.readSharedArray(53);
            int AY2 = rc.readSharedArray(54);
            if (AX2 == 0 && AY2 == 0) {
                rc.writeSharedArray(53, rc.getLocation().x);
                rc.writeSharedArray(54, rc.getLocation().y);
            } else if (rc.getLocation().x == AX2 && rc.getLocation().y == AY2) {
                return;
            } else {
                int AX3 = rc.readSharedArray(55);
                int AY3 = rc.readSharedArray(56);
                if (AX3 == 0 && AY3 == 0) {
                    rc.writeSharedArray(55, rc.getLocation().x);
                    rc.writeSharedArray(56, rc.getLocation().y);
                } else if (rc.getLocation().x == AX3 && rc.getLocation().y == AY3) {
                    return;
                } else {
                    int AX4 = rc.readSharedArray(57);
                    int AY4 = rc.readSharedArray(58);
                    if (AX4 == 0 && AY4 == 0) {
                        rc.writeSharedArray(57, rc.getLocation().x);
                        rc.writeSharedArray(58, rc.getLocation().y);
                    }
                }
            }
        }
    }
    static MapLocation[] readArchonCoords(RobotController rc) throws GameActionException
    {
        MapLocation[] locs;
        int AX1 = rc.readSharedArray(51);
        int AY1 = rc.readSharedArray(52);
        int AX2 = rc.readSharedArray(53);
        int AY2 = rc.readSharedArray(54);
        if(AX2 == 0 && AY2 == 0) {
            locs = new MapLocation[1];
            locs[0] = new MapLocation(AX1,AY1);
        }else
        {
            int AX3 = rc.readSharedArray(55);
            int AY3 = rc.readSharedArray(56);
            if(AX3 == 0 && AY3 == 0) {
                locs = new MapLocation[2];
                locs[0] = new MapLocation(AX1,AY1);
                locs[1] = new MapLocation(AX2, AY2);
            }else
            {
                int AX4 = rc.readSharedArray(57);
                int AY4 = rc.readSharedArray(58);
                if(AX4 == 0 && AY4 == 0) {
                    locs = new MapLocation[3];
                    locs[0] = new MapLocation(AX1, AY1);
                    locs[1] = new MapLocation(AX2, AY2);
                    locs[2] = new MapLocation(AX3, AY3);
                }else
                {
                    locs = new MapLocation[4];
                    locs[0] = new MapLocation(AX1, AY1);
                    locs[1] = new MapLocation(AX2, AY2);
                    locs[2] = new MapLocation(AX3, AY3);
                    locs[3] = new MapLocation(AX4, AY4);
                }
            }
        }
        return locs;
    }
}

