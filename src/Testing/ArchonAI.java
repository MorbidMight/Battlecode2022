package Testing;

import battlecode.common.*;

public class ArchonAI {
    /**
     * Run a single turn for an Archon.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */

    static void runArchon(RobotController rc, int numMiners) throws GameActionException {
        // Pick a direction to build in.
//check for a possible repair
        Team ally = rc.getTeam();
        Direction dir = RobotPlayer.directions[RobotPlayer.rng.nextInt(RobotPlayer.directions.length)];
        RobotInfo[] k = rc.senseNearbyRobots(20, ally);
        //for loop to check which possible heal is lowest on health
        int q = 100; // holder variable for lowest possible health to heal
        int w = 0; // holder variable for index of lowest possible heal
        if(k.length >= 1) {
            for (int a = 0; a < k.length; a++) {
                if(k[a].getHealth()< q) {
                    q = k[a].getHealth();
                    w = a;
                }
            }
            if(q < 15 && rc.canRepair(k[w].getLocation()))
                rc.repair(k[w].getLocation());
        }


        if (RobotPlayer.rng.nextBoolean()) {
            // Let's try to build a miner.
            System.out.println(numMiners);
            rc.setIndicatorString("Trying to build a miner");
            if (rc.canBuildRobot(RobotType.MINER, dir)) {
                rc.buildRobot(RobotType.MINER, dir);

            }
        } else  {
            // Let's try to build a soldier.
            rc.setIndicatorString("Trying to build a soldier");
            if (rc.canBuildRobot(RobotType.SOLDIER, dir)) {
                rc.buildRobot(RobotType.SOLDIER, dir);

            }
        }
    }
}
