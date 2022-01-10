package BotV1_2_0;

import battlecode.common.*;

public class SoldierAI {  /**
 * Run a single turn for a Soldier.
 * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
 */
    static int turnsAsLead = 0; 
    static void runSoldier(RobotController rc) throws GameActionException {
	int radius = rc.getType().actionRadiusSquared; Team opponent = rc.getTeam().opponent();
    RobotInfo[] enemies = rc.senseNearbyRobots(radius, opponent);
    Direction dir = RobotPlayer.directions[RobotPlayer.rng.nextInt(RobotPlayer.directions.length)];
    //make a leader if there is no leader
    MapLocation myLoc = rc.getLocation();
	MapLocation[] enemyArchons = Utilities.enemyArchonLocater(ArchonAI.readArchonCoords(rc), rc);
    MapLocation[] ourArchons = ArchonAI.readArchonCoords(rc);
    if(myLoc.distanceSquaredTo(enemyArchons[0]) > (ourArchons[0].distanceSquaredTo(enemyArchons[0]) * 7)/ 10){
        int dirX= enemyArchons[0].x;
        int dirY= enemyArchons[0].y;
        if(myLoc.x < dirX){
            if(myLoc.y < dirY){
                dir = Direction.NORTHEAST;
            }
            else if(myLoc.y > dirY){
                dir = Direction.SOUTHEAST;
            }
            else{
                dir = Direction.EAST;
            }
            if(!rc.canMove(dir)){
                dir = dir.rotateRight();
            }
        }
        else if(myLoc.x > dirX){
            if(myLoc.y < dirY){
                dir = Direction.NORTHWEST; 
            }
            else if(myLoc.y > dirY){
                dir = Direction.SOUTHWEST;
            }
            else{
                dir = Direction.WEST;
            }
            if(!rc.canMove(dir)){
                dir = dir.rotateLeft();
            }
        }
        else{
            if(myLoc.y > dirY){
                dir = Direction.SOUTH;
            }
            else{
                dir = Direction.NORTH;
            }
            if(!rc.canMove(dir)){
                dir = dir.rotateRight();
            }
	}
	}

    //attack enemies
    if (enemies.length > 0){
        MapLocation toAttack = enemies[0].location; // find who to attack
        if (rc.canAttack(toAttack)) {
            rc.attack(toAttack);
            dir = rc.getLocation().directionTo(enemies[0].location);
        }

    }
    while(!rc.canMove(dir)){
	dir = RobotPlayer.directions[RobotPlayer.rng.nextInt(RobotPlayer.directions.length)];
    }
        rc.move(dir);
}
}


