package BotV1_1_1;

import battlecode.common.*;


public class MinerAI {
    static int turnssincemined = 0;
    static boolean isNearArchon = false;
    static boolean isNotNextToArchon = true;

    /**
     * Run a single turn for a Miner.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    static void runMiner(RobotController rc) throws GameActionException {
        // Try to mine on squares around us.
        //Mining
        Utilities.suicideBooth(rc);
        turnssincemined++;
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


        //movement
        Team ally = rc.getTeam();
        Direction dir;
        MapLocation[] k = rc.senseNearbyLocationsWithLead(rc.getType().visionRadiusSquared);
        int bestPossibleLead = 0;
        int indexBest = -1;
        boolean moveRandomly = true;
        for (int i = 0; i < k.length; i++) {
            int effectiveLeadAtI = (int)Math.round((rc.senseLead(k[i]) * (1 + rc.senseRubble(k[i]) / 10.0)));
            if (effectiveLeadAtI > bestPossibleLead && !rc.isLocationOccupied(k[i])) {
                indexBest = i;
                bestPossibleLead = (int) (rc.senseLead(k[indexBest]) * (1 + rc.senseRubble(k[indexBest]) / 10.0));
                moveRandomly = false;
            }
        }
        //Best possible lead is the amount of lead on the best square, 0 if no lead
        //IndexBest is the index of the best square in K, -1 if no potential lead
        MapLocation t =  new MapLocation(0,0);
        RobotInfo[] z = rc.senseNearbyRobots(1, ally);
        for(int y = 0; y < z.length; y++) {
            if (z[y].getType() == RobotType.ARCHON) {
                t = z[y].getLocation();
                isNotNextToArchon = false;
                break;
            }
        }

        if (moveRandomly) {
            if(!isNotNextToArchon){
                dir = rc.getLocation().directionTo(t).opposite();
                if(!rc.canMove(dir)){
                    dir = RobotPlayer.directions[RobotPlayer.rng.nextInt(RobotPlayer.directions.length)];

                }
            }

            dir = RobotPlayer.directions[RobotPlayer.rng.nextInt(RobotPlayer.directions.length)];

            if (rc.canMove(dir)) {
                rc.move(dir);
            }
            //move only if true

        } else if (!k[indexBest].equals(rc.getLocation())&&  rc.senseLead(k[indexBest]) > 10 + rc.senseLead(rc.getLocation()) || rc.senseLead(rc.getLocation()) == 0) {
            dir = rc.getLocation().directionTo(k[indexBest]);
            if (rc.canMove(dir)) {
                rc.move(dir);
            }
        }

        RobotInfo[] p = rc.senseNearbyRobots(rc.getType().visionRadiusSquared, ally);
        RobotInfo[] z2 = rc.senseNearbyRobots(1, ally);
        for (int i = 0; i < p.length; i++) {
            if (p[i].getType() == RobotType.ARCHON) {
                isNearArchon = true;
                break;
            }
        }
        for(int y = 0; y < z2.length; y++) {
            if (z2[y].getType() == RobotType.ARCHON) {
                isNotNextToArchon = false;
                break;
            }
        }
        if (isNearArchon && turnssincemined > 50 && isNotNextToArchon) {
            rc.disintegrate();
        }

        isNearArchon = false;

    }
}


