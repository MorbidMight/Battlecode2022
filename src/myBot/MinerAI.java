package myBot;

import battlecode.common.*;
import battlecode.common.Clock;

public class MinerAI {
static int turnssincemined = 0;
static boolean isNearArchon = false;

    /**
     * Run a single turn for a Miner.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    static void runMiner(RobotController rc) throws GameActionException {
        // Try to mine on squares around us.
        //Mining
        turnssincemined++;
        MapLocation me = rc.getLocation();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                MapLocation mineLocation = new MapLocation(me.x + dx, me.y + dy);
                // Notice that the Miner's action cooldown is very low.
                // You can mine multiple times per turn!
                while (rc.canMineGold(mineLocation)) {
                    rc.mineGold(mineLocation);
                    turnssincemined=0;
                }
                while (rc.canMineLead(mineLocation) && rc.senseLead(mineLocation) > 1) {
                    rc.mineLead(mineLocation);
                    turnssincemined=0;
                }
            }
        }



        //movement
        Direction dir;
        MapLocation[] k = rc.senseNearbyLocationsWithLead(rc.getType().visionRadiusSquared);
        //            dir = RobotPlayer.directions[RobotPlayer.rng.nextInt(RobotPlayer.directions.length)];
        int bestPossibleLead = 0;
        int indexBest = -1;
        for (int i = 0; i < k.length; i++) {
            int effectiveLeadAtI = (int)(rc.senseLead(k[i])*(1+rc.senseRubble(k[i])/10.0));
            if (effectiveLeadAtI > bestPossibleLead && !rc.isLocationOccupied(k[i])) {
                indexBest = i;
                bestPossibleLead = effectiveLeadAtI;
            }
        }
        //Best possible lead is the amount of lead on the best square, 0 if no lead
        //IndexBest is the index of the best square in K, -1 if no potential lead
        boolean bestSpotIsCurrentSpot = k[indexBest].equals(rc.getLocation());
        boolean bestSpotHasMoreLeadThanCurrentSpot = rc.senseLead(k[indexBest])>10+rc.senseLead(rc.getLocation());
        boolean currentSpotHasNoLead = rc.senseLead(rc.getLocation())==0;
        if (indexBest == -1&&rc.senseLead(rc.getLocation())==0) {
            dir = RobotPlayer.directions[RobotPlayer.rng.nextInt(RobotPlayer.directions.length)];
            if (rc.canMove(dir)) {
                rc.move(dir);
            }
            //move only if true
        } else if (!bestSpotIsCurrentSpot&&bestSpotHasMoreLeadThanCurrentSpot||currentSpotHasNoLead) {
            dir = rc.getLocation().directionTo(k[indexBest]);
            if (rc.canMove(dir)) {
                rc.move(dir);
            }
        }
        Team ally = rc.getTeam();
        RobotInfo[] p = rc.senseNearbyRobots(rc.getType().visionRadiusSquared, ally);

        for(int i = 0; i< p.length; i++){
            if(p[i].getType() == RobotType.ARCHON){
                isNearArchon = true;
                break;
            }
        }
        if(isNearArchon && turnssincemined > 50)
            rc.disintegrate();
        isNearArchon = false;

    }
}


