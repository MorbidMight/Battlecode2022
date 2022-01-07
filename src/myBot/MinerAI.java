package myBot;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

public class MinerAI {

    /**
     * Run a single turn for a Miner.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    static void runMiner(RobotController rc) throws GameActionException {
        // Try to mine on squares around us.
        //Mining
        MapLocation me = rc.getLocation();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                MapLocation mineLocation = new MapLocation(me.x + dx, me.y + dy);
                // Notice that the Miner's action cooldown is very low.
                // You can mine multiple times per turn!
                while (rc.canMineGold(mineLocation)) {
                    rc.mineGold(mineLocation);
                }
                while (rc.canMineLead(mineLocation) && rc.senseLead(mineLocation) > 1) {
                    rc.mineLead(mineLocation);
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
            if (rc.senseLead(k[i]) > bestPossibleLead && !rc.isLocationOccupied(k[i])) {
                indexBest = i;
                bestPossibleLead = rc.senseLead(k[indexBest]);
            }
        }
        //Best possible lead is the amount of lead on the best square, 0 if no lead
        //IndexBest is the index of the best square in K, -1 if no potential lead
        if (indexBest == -1&&rc.senseLead(rc.getLocation())==0) {
            dir = RobotPlayer.directions[RobotPlayer.rng.nextInt(RobotPlayer.directions.length)];
            if (rc.canMove(dir)) {
                rc.move(dir);
            }
        } else if (!k[indexBest].equals(rc.getLocation())&&(rc.senseLead(k[indexBest])>rc.senseLead(rc.getLocation()))) {
            dir = rc.getLocation().directionTo(k[indexBest]);
            if (rc.canMove(dir)) {
                rc.move(dir);
                System.out.println("I moved!");
            }
        }
    }
}


