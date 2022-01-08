package BotV1_1_1;

import battlecode.common.*;

public class SoldierAI {  /**
 * Run a single turn for a Soldier.
 * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
 */
    static int turnsAsLead = 0; static void runSoldier(RobotController rc) throws GameActionException { int radius = rc.getType().actionRadiusSquared; Team opponent = rc.getTeam().opponent();
    RobotInfo[] enemies = rc.senseNearbyRobots(radius, opponent);
    Direction dir = RobotPlayer.directions[RobotPlayer.rng.nextInt(RobotPlayer.directions.length)];
    //make a leader if there is no leader
    MapLocation myLoc = rc.getLocation();
    MapLocation attackLoc = new MapLocation(rc.readSharedArray(1), rc.readSharedArray(2));
   MapLocation[] enemyArchons = Utilities.enemyArchonLocater(ArchonAI.readArchonCoords(rc), rc);
    //make new leader
    //if(enemies.length > 0 && rc.readSharedArray(0) == 0){
    //    rc.writeSharedArray(0, rc.getID());
    //    //random dir to leader
    //    dir = rc.getLocation().directionTo(enemies[0].getLocation()).opposite();
    //    //assign ints in the SharedArray to the coordinates of the leader
    //    rc.writeSharedArray(1, myLoc.x);
    //    rc.writeSharedArray(2, myLoc.y);
    //} */
    ////code for existing leader
    //else if(rc.readSharedArray(0) == rc.getID() /*+ turnsAsLead*/){
    //    //turnsAsLead++;
    //    //rc.writeSharedArray(0, rc.getID() + turnsAsLead);
    //    //change direction if the bot can't move to direction
    //    int rotations = 0;
    //    while(!rc.canMove(dir) && rotations < 8) {
    //        dir = dir.rotateRight();
    //        rotations++;
    //    }
    //    if(rotations == 8){
    //        rc.writeSharedArray(0, 0);
    //    }
    //    //change leader if leader is low in health
    //    if(rc.getHealth() < 15 || enemies.length < 1){
    //        rc.writeSharedArray(0, 0);
    //    }
    //}
    ///*else if(rc.readSharedArray(0) == rc.getID() + turnsAsLead - 1){
    //    rc.writeSharedArray(0,0);

    //}*/
    ////find the direction of destination and set dir to diection of destination
    //else if(rc.readSharedArray(0) != 0 && myLoc.distanceSquaredTo(attackLoc) < 1000){
    //    
    //    int dirX= rc.readSharedArray(1);
    //    int dirY= rc.readSharedArray(2);
    //    if(myLoc.x < dirX){
    //        if(myLoc.y < dirY){
    //            dir = Direction.NORTHEAST;
    //        }
    //        else if(myLoc.y > dirY){
    //            dir = Direction.SOUTHEAST;
    //        }
    //        else{
    //            dir = Direction.EAST;
    //        }
    //        if(!rc.canMove(dir)){
    //            dir = dir.rotateRight();
    //        }
    //    }
    //    else if(myLoc.x > dirX){
    //        if(myLoc.y < dirY){
    //            dir = Direction.NORTHWEST; 
    //        }
    //        else if(myLoc.y > dirY){
    //            dir = Direction.SOUTHWEST;
    //        }
    //        else{
    //            dir = Direction.WEST;
    //        }
    //        if(!rc.canMove(dir)){
    //            dir = dir.rotateLeft();
    //        }
    //    }
    //    else{
    //        if(myLoc.y > dirY){
    //            dir = Direction.SOUTH;
    //        }
    //        else{
    //            dir = Direction.NORTH;
    //        }
    //        if(!rc.canMove(dir)){
    //            dir = dir.rotateRight();
    //        }
    //    }
    //}*/
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

    //attack enemies
    if (enemies.length > 0){
        MapLocation toAttack = enemies[0].location; // find who to attack
        if (rc.canAttack(toAttack)) {
            rc.attack(toAttack);
            dir = rc.getLocation().directionTo(enemies[0].location);
        }

    }
    while(rc.canMove(dir)){
        rc.move(dir);
    }
}
}


