package BotV1_1_1;

import battlecode.common.*;

import java.util.Arrays;


public class MinerAI {
    static int turnssincemined = 0;
    static boolean farmer = false; //Once they get settle down they become a farmer and stop moving

    /**
     * Run a single turn for a Miner.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */

    static void runMiner(RobotController rc) throws GameActionException {
        // Try to mine on squares around us.
        //Mining
        //Utilities.suicideBooth(rc);
        turnssincemined++;



        //Determine if it should become a farmer, and if its too cloce to the arcon

            //Find the Archon
            MapLocation a;//location of archon
            int distanceToArchon = Integer.MAX_VALUE;
            Direction dir = Direction.SOUTH;
            RobotInfo[] f = rc.senseNearbyRobots(rc.getType().visionRadiusSquared, rc.getTeam());
            for (RobotInfo i : f) {
                if (i.type.equals(RobotType.ARCHON)) {
                    a = i.getLocation();
                    distanceToArchon = rc.getLocation().distanceSquaredTo(a);
                    dir = rc.getLocation().directionTo(a).opposite();
                }
            }
            //if too close to the archon move away
            if (distanceToArchon < 2)
                rc.move(randomRotate(dir));






        //The actual mining gets done
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

        //If they still arent a farmer this will help them decide where to go
        if (!farmer) {
            //once theyre become a farmer they stop moving
            //if thyre not a farmer they move to the best place
            MapLocation[] SquaresWLead = rc.senseNearbyLocationsWithLead(100);
            MapLocation best = rc.getLocation();
            boolean knowsWhereToGo = false;
            for (MapLocation i : SquaresWLead) {
                if (rc.senseLead(i) > rc.senseLead(best) && !rc.isLocationOccupied(i)&&rc.senseLead(i)>1) {
                    best = i;
                    knowsWhereToGo = true;
                }
            }
            if (!knowsWhereToGo) {
                best = rc.getLocation().subtract(RobotPlayer.DiagonalDirections[RobotPlayer.rng.nextInt(4)]);
            }
             dir = rc.getLocation().directionTo(best);
            dir = randomRotate(dir);
            rc.move(dir);

        }

    }

    //This method takes in a direction, if that direction if diagonal it returns it,
    // otherwise it randomly selectrs one of the adjacent directions and returns it
    static Direction randomRotate(Direction in) {
        int index = 0;
        for (int i = 0; i < 8; i++) {
            if (in.equals(RobotPlayer.directions[i])) {
                index = i;
                break;
            }
        }
        if (index % 2 == 1) {
            return in;
        } else {
            index = index / 2;
            if (RobotPlayer.rng.nextBoolean())
                return RobotPlayer.DiagonalDirections[index];
            else {
                return RobotPlayer.DiagonalDirections[(index - 1) % 4];
            }
        }
    }
}




