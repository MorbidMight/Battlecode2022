package BotV1_3_0;

import battlecode.common.*;


public class MinerAI {
    static int turnssincemined = 0;
    static int whenLostGo = RobotPlayer.rng.nextInt(4);
    static int turnsSinceMoved = 0;
    static Direction dGoing = RobotPlayer.directions[RobotPlayer.rng.nextInt(8)];
    ;

    /**
     * Run a single turn for a Miner.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    static void runMiner(RobotController rc) throws GameActionException {
        // Try to mine on squares around us.
        //Mining
        turnssincemined++;
        turnsSinceMoved++;
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


        RobotInfo[] AllAdjacentRobots = rc.senseNearbyRobots(2, rc.getTeam());
        for (RobotInfo hh : AllAdjacentRobots) {
            if (hh.getType().equals(RobotType.ARCHON)) {
                Direction dGoing = hh.getLocation().directionTo(rc.getLocation());
                if (!tryToMove(rc, dGoing)) {
                    //   rc.disintegrate();
                }
                break;
            }
        }
        if (rc.senseLead(rc.getLocation())<2) {
            RobotInfo[] AllFriendlyRobots = rc.senseNearbyRobots(rc.getType().visionRadiusSquared, rc.getTeam());
            for (RobotInfo hh : AllFriendlyRobots) {
                if (hh.getType().equals(RobotType.MINER)) {
                    Direction dGoing = rc.getLocation().directionTo(hh.getLocation()).opposite();
                    if (!tryToMove(rc, dGoing)) {
                        //rc.disintegrate();
                    }
                    break;
                }
            }


//find the best lead
            Direction dir;
            MapLocation[] k = rc.senseNearbyLocationsWithLead(rc.getType().visionRadiusSquared);
            int bestPossibleLead = 0;
            int indexBest = 0;
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

            if (!moveRandomly) {
                dGoing = rc.getLocation().directionTo(k[indexBest]);
                tryToMove(rc, dGoing);
            } else {
                tryToMove(rc, dGoing);
            }


        }
    }

    static boolean tryToMove(RobotController rc, Direction d) throws GameActionException {
        if (rc.canMove(d)) {
            rc.move(d);
            return true;
        } else if (rc.canMove(d.rotateRight())) {
            d = d.rotateRight();
            rc.move(d.rotateRight());
            return true;
        } else if (rc.canMove(d.rotateLeft())) {
            d = d.rotateLeft();
            rc.move(dGoing.rotateLeft());
            return true;
        }
        return false;
    }
}




