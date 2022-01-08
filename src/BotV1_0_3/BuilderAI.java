package BotV1_0_3;

import battlecode.common.*;

import java.util.Random;

public class BuilderAI {
    static final Random rng = new Random(6147);

    static void runBuilder(RobotController rc) throws GameActionException {
        //move randomly(change later)

        Direction dir;
        Team ally = rc.getTeam();
        RobotInfo[] allies = rc.senseNearbyRobots(20, ally);

        //repair

        int minerCount = 0;
        boolean alreadyWatchtower = false;
        for (int x = 0; x < allies.length; x++) {
            if (allies[x].getType().equals(RobotType.MINER)) {
                minerCount++;
            }
            if (allies[x].getType().equals(RobotType.WATCHTOWER)) {
                alreadyWatchtower = true;
            }
        }

        for(int x = 0; x < allies.length; x++)
        {
            if(allies[x].getType().equals(RobotType.MINER))
            {
                if (rc.canMove(rc.getLocation().directionTo(allies[x].getLocation()))){
                    rc.move(rc.getLocation().directionTo(allies[x].getLocation()));
                    System.out.println("I moved!");
                }
            }
        }

        if (minerCount >= 4 && !alreadyWatchtower) {//if there are 4 miners in the area and there isn't already a watchtower
            for (int x = 0; x < RobotPlayer.directions.length; x++) {
                dir = RobotPlayer.directions[x];
                if (rc.canBuildRobot(RobotType.WATCHTOWER, dir)) {//look in all directions and see if you can build a watchtower
                    rc.buildRobot(RobotType.WATCHTOWER, dir);//build it
                    if(rc.canRepair(rc.getLocation().add(dir))){
                        rc.repair(rc.getLocation().add(dir));
                    }
                    break;
                }
            }
        }
        RobotInfo lowestTower = null;
        for(int x = 0; x < allies.length; x++)
        {
            if(allies[x].getType().equals(RobotType.WATCHTOWER))
            {
                lowestTower = allies[x];
            }
        }
        if(!(lowestTower.equals(null)))
        {
            if (rc.canRepair(lowestTower.getLocation()))
            {
                rc.repair(lowestTower.getLocation());
            }
        }
    }
}
