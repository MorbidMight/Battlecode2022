package Testing;

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

        MapLocation me = rc.getLocation();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                MapLocation mineLocation = new MapLocation(me.x + dx, me.y + dy);
                // Notice that the Miner's action cooldown is very low.
                // You can mine multiple times per turn!
                while (rc.canMineGold(mineLocation)) {
                    rc.mineGold(mineLocation);
                }
                while (rc.canMineLead(mineLocation)) {
                    rc.mineLead(mineLocation);
                }
            }
        }
        Direction dir;
        MapLocation[] k = rc.senseNearbyLocationsWithLead(rc.getType().visionRadiusSquared);

        // hi urav
        int indexOfBestSquareInK = 0;
        for(int i = 0; i<k.length;i++)
        {
            if(rc.senseLead(k[i])>rc.senseLead(k[indexOfBestSquareInK]))
                indexOfBestSquareInK=i;
        }
        if(k.length == 0)
            dir = RobotPlayer.directions[RobotPlayer.rng.nextInt(RobotPlayer.directions.length)];
        else
            dir = rc.getLocation().directionTo(k[indexOfBestSquareInK]);
        if (rc.canMove(dir)) {
            rc.move(dir);
            System.out.println("I moved!");
        }
    }
}
