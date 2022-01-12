package BotV1_3_0;

import battlecode.common.*;

import java.util.Map;
import java.util.Random;

public class BuilderAI {
    static final Random rng = new Random(6147);

    static void runBuilder(RobotController rc) throws GameActionException {
        //move randomly(change later)


        Direction dir = RobotPlayer.directions[0];
        Team ally = rc.getTeam();
        RobotInfo[] allies = rc.senseNearbyRobots(20, ally);
        MapLocation[] locations = rc.senseNearbyLocationsWithLead(rc.getType().visionRadiusSquared);
        double mapAverage = (rc.getMapHeight() + rc.getMapWidth());
        int distanceFromArchon = (int) Math.round(mapAverage/20);
        MapLocation[] ourArchons = ArchonAI.readArchonCoords(rc);
        RobotInfo watchTower = null;
        RobotInfo lab = null;
        boolean alreadyWatchtower = false;
        for(int x = 0; x < allies.length; x++)
        {
            if(allies[x].getType().equals(RobotType.WATCHTOWER))
            {
                alreadyWatchtower = true;
            }
            if(allies[x].getType().equals(RobotType.WATCHTOWER) && allies[x].getHealth() < allies[x].getType().getMaxHealth(1))
            {
                watchTower = allies[x];
            }
        }
        for(int x = 0; x < allies.length; x++)
        {
            if(allies[x].getType().equals(RobotType.LABORATORY) && allies[x].getHealth() < allies[x].getType().getMaxHealth(1))
            {
                lab = allies[x];
            }
        }
        if(watchTower != null)
        {
            while(rc.canRepair(watchTower.getLocation()))
            {
                rc.repair(watchTower.getLocation());
            }
        }
        if(lab != null)
        {
            while (rc.canRepair(lab.getLocation()))
            {
                rc.repair(lab.getLocation());
            }
        }
        Direction labDir = RobotPlayer.directions[0];
        for(int x = 0; x < RobotPlayer.directions.length; x++)
        {
            if(rc.canBuildRobot(RobotType.LABORATORY, RobotPlayer.directions[x]))
            {
                labDir = RobotPlayer.directions[x];
            }
        }
        if(rc.readSharedArray(63) == 1) {
            if (allies.length < 2) {
                if (rc.canBuildRobot(RobotType.LABORATORY, labDir)) {
                    rc.buildRobot(RobotType.LABORATORY, labDir);
                    rc.writeSharedArray(63, 2);
                    rc.writeSharedArray(61, rc.getLocation().add(labDir).x);
                    rc.writeSharedArray(62, rc.getLocation().add(labDir).y);
                }
            }
        }
        for(int x = 0; x < RobotPlayer.directions.length; x++)
        {
            if(rc.canBuildRobot(RobotType.WATCHTOWER, RobotPlayer.directions[x]))
            {
                dir = RobotPlayer.directions[x];
            }
        }
        MapLocation watchTowerBuildSpot = rc.getLocation().add(dir);
        boolean distanceIsGood = true;
        for(int x = 0; x < ourArchons.length; x++)
        {
            if(ourArchons[x].distanceSquaredTo(watchTowerBuildSpot) <= Math.pow(distanceFromArchon, 2))
            {
                distanceIsGood = false;
            }
        }
        if(locations.length >= 4 && alreadyWatchtower == false && distanceIsGood)
        {
            if(rc.canBuildRobot(RobotType.WATCHTOWER, dir))
            {
                rc.buildRobot(RobotType.WATCHTOWER, dir);
            }
        }

        Direction moveDir = RobotPlayer.DiagonalDirections[rng.nextInt(RobotPlayer.DiagonalDirections.length)];
        if(watchTower == null && lab == null) {
            if (rc.canMove(moveDir)) {
                rc.move(moveDir);
            }
        }else if(watchTower != null)
        {
           moveDir = rc.getLocation().directionTo(watchTower.getLocation());
            if (rc.canMove(moveDir)) {
                rc.move(moveDir);
            }
        }else if(lab != null)
        {
            moveDir = rc.getLocation().directionTo(lab.getLocation());
            if (rc.canMove(moveDir)) {
                rc.move(moveDir);
            }
        }


        //repair


        /*
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
                }
            }
        }
        MapLocation q = new MapLocation(rc.readSharedArray(61), rc.readSharedArray(62));
        if (minerCount >= 4 && !alreadyWatchtower && !rc.canSenseLocation(q)) {//if there are 4 miners in the area and there isn't already a watchtower
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
            if(allies[x].getType().equals(RobotType.WATCHTOWER) || allies[x].getType().equals((RobotType.LABORATORY)))
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
        }*/
    }
}
