package BotV1_1_0;

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
        Team ally = rc.getTeam();
        Direction dir = RobotPlayer.directions[RobotPlayer.rng.nextInt(RobotPlayer.directions.length)];
        RobotInfo[] k = rc.senseNearbyRobots(20, ally);
        //for loop to check which possible heal is lowest on health
        int q = 100; // holder variable for lowest possible health to heal
        int w = 0; // holder variable for index of lowest possible heal
        if (k.length >= 1) {
            for (int a = 0; a < k.length; a++) {
                if (k[a].getHealth() < q) {
                    q = k[a].getHealth();
                    w = a;
                }
            }
            if (q < 15 && rc.canRepair(k[w].getLocation()))
                rc.repair(k[w].getLocation());
        }
        if(RobotPlayer.turnCount > 500 && labCount == 0 && rc.getMapHeight() > 30 && rc.getMapWidth() > 30){
            if(rc.getTeamLeadAmount(rc.getTeam()) >= 950){
                if(rc.canBuildRobot(RobotType.BUILDER, rc.getLocation().directionTo(Utilities.findCenter(rc)).opposite())){
                    rc.buildRobot(RobotType.BUILDER, rc.getLocation().directionTo(Utilities.findCenter(rc)).opposite());
                    labCount++;
                    rc.writeSharedArray(63, 1);
                }
            }
        }
        else if (rc.getTeamLeadAmount(rc.getTeam()) >= 180 && (RobotPlayer.turnCount < 500 || rc.readSharedArray(63) == 2 || rc.getMapWidth() < 30 || rc.getMapHeight() < 30)) {
            double randDouble = Math.random();
            if (randDouble > 0.65) {
                // Let's try to build a miner.
                rc.setIndicatorString("Trying to build a miner");
                if (rc.canBuildRobot(RobotType.MINER, dir)) {
                    rc.buildRobot(RobotType.MINER, dir);

                }
            } else if (randDouble > 0.1 && randDouble < 0.65) {
                // Let's try to build a soldier.
                rc.setIndicatorString("Trying to build a soldier");
                if (rc.canBuildRobot(RobotType.SOLDIER, dir)) {
                    rc.buildRobot(RobotType.SOLDIER, dir);

                }
            }
            else if (randDouble < 0.05) {//ensure there aren't more than one builder per archon
                rc.setIndicatorString("Trying to build a builder");
                if (rc.canBuildRobot(RobotType.BUILDER, dir)) {
                    rc.buildRobot(RobotType.BUILDER, dir);
                }
            }
        }
    }
}
