package BotV1_1_1;

import battlecode.common.*;

import java.util.Map;


public class MinerAI {
    static int turnssincemined = 0;
    static boolean isNearArchon = false;
    static boolean isNotNextToArchon = true;
    static boolean farmer = false; //Once they get settle down they become a farmer and stop moving

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
        if (distanceToArchon < 2)
            rc.move(randomRotate(dir));

        if (!farmer) {
            //once theyre become a farmer they stop moving
            int unoccupiedAdjacentSquaresWithLead = 0;
            MapLocation[] adjacentSquaresWLead = rc.senseNearbyLocationsWithLead(2);
            for (MapLocation i : adjacentSquaresWLead) {
                if (!rc.isLocationOccupied(i)) {
                    unoccupiedAdjacentSquaresWithLead++;
                }
            }

            if (unoccupiedAdjacentSquaresWithLead > 1 && distanceToArchon < 2) {
                farmer = true;
                rc.move(Direction.SOUTH);
            }
            adjacentSquaresWLead = rc.senseNearbyLocationsWithLead(100);
            MapLocation best = rc.getLocation();
            boolean knowsWhereToGo = false;
            for (MapLocation i : adjacentSquaresWLead) {
                if (rc.senseLead(i) > rc.senseLead(best) && !rc.isLocationOccupied(i)) {
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




