package BotV1_2_0;

import battlecode.common.*;

public class ArchonAI {
    static int labCount = 0;

    /**
     * Run a single turn for an Archon.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */

    static void runArchon(RobotController rc) throws GameActionException {
        // Pick a direction to build in.
        //check for a possible repair

        Direction dir = RobotPlayer.directions[RobotPlayer.rng.nextInt(RobotPlayer.directions.length)];


        if (RobotPlayer.turnCount < 500 && rc.readSharedArray(RobotPlayer.ArchonID) == rc.getID() || RobotPlayer.turnCount > 500) {
            if (RobotPlayer.turnCount > 500 && labCount == 0 && rc.getMapHeight() > 30 && rc.getMapWidth() > 30) {
                if (rc.getTeamLeadAmount(rc.getTeam()) >= 950) {
                    if (rc.canBuildRobot(RobotType.BUILDER, rc.getLocation().directionTo(Utilities.findCenter(rc)).opposite())) {
                        rc.buildRobot(RobotType.BUILDER, rc.getLocation().directionTo(Utilities.findCenter(rc)).opposite());
                        labCount++;
                        rc.writeSharedArray(63, 1);
                    }
                }
            } else if (rc.getTeamLeadAmount(rc.getTeam()) >= 180 && (RobotPlayer.turnCount < 500 || rc.readSharedArray(63) == 2 || rc.getMapWidth() < 30 || rc.getMapHeight() < 30)) {
                double randDouble = RobotPlayer.rng.nextDouble();
                if (randDouble > 0.65) {
                    int move = RobotPlayer.rng.nextInt(4);
                    for (int i = 0; i < 4; i++) {
                        if (rc.canBuildRobot(RobotType.MINER, RobotPlayer.DiagonalDirections[move+i])) {
                            rc.buildRobot(RobotType.MINER, RobotPlayer.DiagonalDirections[move+i]);
                            break;
                        }
                    }
                } else if (randDouble > 0.1 && randDouble < 0.65) {
                    int move = RobotPlayer.rng.nextInt(4);
                    for (int i = 0; i < 4; i++) {
                        if (rc.canBuildRobot(RobotType.SOLDIER, RobotPlayer.CardinalDirections[move+i])) {
                            rc.buildRobot(RobotType.SOLDIER, RobotPlayer.CardinalDirections[move+i]);
                            break;
                        }
                    }
                } else if (randDouble < 0.05) {//ensure there aren't more than one builder per archon
                    int move = RobotPlayer.rng.nextInt(4);
                    for (int i = 0; i < 4; i++) {
                        if (rc.canBuildRobot(RobotType.BUILDER, RobotPlayer.CardinalDirections[move+i])) {
                            rc.buildRobot(RobotType.BUILDER, RobotPlayer.CardinalDirections[move+i]);
                            break;
                        }
                    }
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

    public static void writeIDToArray(RobotController rc) throws GameActionException {
        int ID1 = rc.readSharedArray(44);
        if (ID1 == 0) {
            rc.writeSharedArray(44, rc.getID());
        } else if (rc.getID() == ID1) {
            return;
        } else {
            int ID2 = rc.readSharedArray(45);
            if (ID2 == 0) {
                rc.writeSharedArray(45, rc.getID());
            } else if (rc.getID() == ID2) {
                return;
            } else {
                int ID3 = rc.readSharedArray(46);
                if (ID3 == 0) {
                    rc.writeSharedArray(46, rc.getID());
                } else if (rc.getID() == ID3) {
                    return;
                } else {
                    int ID4 = rc.readSharedArray(47);
                    if (ID4 == 0) {
                        rc.writeSharedArray(47, rc.getID());
                    }
                }
            }
        }
    }

    static MapLocation[] readArchonCoords(RobotController rc) throws GameActionException {
        MapLocation[] locs;
        int AX1 = rc.readSharedArray(51);
        int AY1 = rc.readSharedArray(52);
        int AX2 = rc.readSharedArray(53);
        int AY2 = rc.readSharedArray(54);
        if (AX2 == 0 && AY2 == 0) {
            locs = new MapLocation[1];
            locs[0] = new MapLocation(AX1, AY1);
        } else {
            int AX3 = rc.readSharedArray(55);
            int AY3 = rc.readSharedArray(56);
            if (AX3 == 0 && AY3 == 0) {
                locs = new MapLocation[2];
                locs[0] = new MapLocation(AX1, AY1);
                locs[1] = new MapLocation(AX2, AY2);
            } else {
                int AX4 = rc.readSharedArray(57);
                int AY4 = rc.readSharedArray(58);
                if (AX4 == 0 && AY4 == 0) {
                    locs = new MapLocation[3];
                    locs[0] = new MapLocation(AX1, AY1);
                    locs[1] = new MapLocation(AX2, AY2);
                    locs[2] = new MapLocation(AX3, AY3);
                } else {
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