package BotV1_2_0;

import battlecode.common.*;


public class MinerAI {
    static int turnssincemined = 0;
    static boolean isNearArchon = false;
    static boolean isNextToArchon = false;
    static int whenLostGo = RobotPlayer.rng.nextInt(4);
    static int turnsSinceMoved = 0;

    /**
     * Run a single turn for a Miner.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    static void runMiner(RobotController rc) throws GameActionException {
        // Try to mine on squares around us.
        //Mining
        turnssincemined++;
        turnsSinceMoved++;
        if (turnssincemined > 50 && turnsSinceMoved > 20) {
            rc.disintegrate();
        }
        MapLocation me = rc.getLocation();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                MapLocation mineLocation = new MapLocation(me.x + dx, me.y + dy);
                // Notice that the Miner's action cooldown is very low.
                // You can mine multiple times per turn!
                while (rc.canMineGold(mineLocation)) {
                    rc.mineGold(mineLocation);
                    turnssincemined = 0;
                }
                while (rc.canMineLead(mineLocation) && rc.senseLead(mineLocation) > 1) {
                    rc.mineLead(mineLocation);
                    turnssincemined = 0;
                }
            }
        }


        RobotInfo[] AllFriendlyRobots = rc.senseNearbyRobots(rc.getType().visionRadiusSquared, rc.getTeam());
        RobotInfo[] AllAdjacentRobots = rc.senseNearbyRobots(2, rc.getTeam());
        MapLocation locationOfArchon = new MapLocation(0, 0);
        for (RobotInfo info : AllFriendlyRobots) {
            if (info.getType().equals(RobotType.ARCHON)) {
                isNearArchon = true;
                locationOfArchon = info.getLocation();
                break;
            }
        }
        for (RobotInfo robotInfo : AllAdjacentRobots) {
            if (robotInfo.getType().equals(RobotType.ARCHON)) {
                isNextToArchon = true;
                if (rc.canMove(robotInfo.getLocation().directionTo(rc.getLocation()))) {
                    rc.move(Utilities.randomRotate(robotInfo.getLocation().directionTo(rc.getLocation())));
                    turnsSinceMoved = 0;
                }
                break;
            }
        }


        //movement
        Direction dir;
        MapLocation[] k = rc.senseNearbyLocationsWithLead(rc.getType().visionRadiusSquared);
        int bestPossibleLead = 0;
        int indexBest = -1;
        boolean moveRandomly = true;
        for (int i = 0; i < k.length; i++) {
            int effectiveLeadAtI = (int) Math.round((rc.senseLead(k[i]) * (1 + rc.senseRubble(k[i]) / 10.0)));
            if (effectiveLeadAtI > bestPossibleLead && !rc.isLocationOccupied(k[i])) {
                indexBest = i;
                bestPossibleLead = (int) (rc.senseLead(k[indexBest]) * (1 + rc.senseRubble(k[indexBest]) / 10.0));
                moveRandomly = false;
            }
        }

        //Best possible lead is the amount of lead on the best square, 0 if no lead
        //IndexBest is the index of the best square in K, -1 if no potential lead


        if (moveRandomly) {
            for (int i = 0; i < 4; i++) {
                if (rc.canMove(RobotPlayer.DiagonalDirections[(whenLostGo + 1) % 4])) {
                    rc.move(Utilities.randomRotate(RobotPlayer.DiagonalDirections[(whenLostGo + 1) % 4]));
                    turnsSinceMoved = 0;
                    break;
                    //move only if true
                }
            }

        } else if (!k[indexBest].
                equals(rc.getLocation()) && rc.senseLead(k[indexBest]) > 10 + rc.senseLead(rc.getLocation()) || rc.senseLead(rc.getLocation()) == 0) {
            dir = rc.getLocation().directionTo(k[indexBest]);
            if (rc.canMove(Utilities.randomRotate(dir))) {
                rc.move(Utilities.randomRotate(dir));
                turnsSinceMoved = 0;
            } else {
                for (int i = 0; i < 4; i++) {
                    if (rc.canMove(RobotPlayer.DiagonalDirections[(whenLostGo + 1) % 4])) {
                        rc.move(Utilities.randomRotate(RobotPlayer.DiagonalDirections[(whenLostGo + 1) % 4]));
                        turnsSinceMoved = 0;
                    }
                }
            }
        }
    }
}



