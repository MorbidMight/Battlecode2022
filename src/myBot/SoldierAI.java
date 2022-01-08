package myBot;

import battlecode.common.*;

public class SoldierAI {  /**
 * Run a single turn for a Soldier.
 * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
 */
    private static Direction dirOfAttack;
    private static boolean isSurrounded;
static void runSoldier(RobotController rc) throws GameActionException {
    //find and go toward leader soldier
    Direction dir;
    boolean isLeader;
    //make a leader if there is no leader
    MapLocation myLoc = rc.getLocation();
    if(rc.readSharedArray(0) == 0){
        rc.writeSharedArray(0, rc.getID());
        isLeader = true;
        //random dir to leader
        dir = RobotPlayer.directions[RobotPlayer.rng.nextInt(RobotPlayer.directions.length)];
        dirOfAttack = dir;
        //assign ints in the SharedArray to the coordinates of the leader
        rc.writeSharedArray(1, myLoc.x);
        rc.writeSharedArray(2, myLoc.y);
    } 
    else if(rc.readSharedArray(0) == rc.getID()){
        dir = dirOfAttack;
        //change direction if the bot can't move to direction
        int rotations = 0;
        while(!rc.canMove(dir) && rotations < 8) {
            dir = dir.rotateRight();
            rotations++;
        }
        //change leader if leader can't move
        if(rotations == 8){
            rc.writeSharedArray(0, 0);
        }
        //assign ints in the SharedArray to the coordinates of the leader
        rc.writeSharedArray(1, myLoc.x);
        rc.writeSharedArray(2, myLoc.y);
        //change leader if leader is low in health
        if(rc.getHealth() < 20){
            rc.writeSharedArray(0, 0);
        }
    }
    //find the direction of leader and set dir to diection of leader
    else{
        if(myLoc.x < rc.readSharedArray(1)){
            if(myLoc.y < rc.readSharedArray(2)){
                dir = Direction.NORTHWEST; 
            }
            else if(myLoc.y > rc.readSharedArray(2)){
                dir = Direction.SOUTHWEST;
            }
            else{
                dir = Direction.WEST;
            }
        }
        else{
            if(myLoc.y < rc.readSharedArray(2)){
                dir = Direction.NORTHEAST;
            }
            else if(myLoc.y > rc.readSharedArray(2)){
                dir = Direction.SOUTHEAST;
            }
            else{
                dir = Direction.EAST;
            }
        }
    }
    
    
    int k = 0;
    int a = 0;
    // Try to attack someone
    //add scout implementation
    int radius = rc.getType().actionRadiusSquared;
    Team opponent = rc.getTeam().opponent();
    RobotInfo[] enemies = rc.senseNearbyRobots(radius, opponent);
    //Direction dir = RobotPlayer.directions[RobotPlayer.rng.nextInt(RobotPlayer.directions.length)];

    Team ally = rc.getTeam();
    if (enemies.length > 0){
        for (int i = 0; i < enemies.length; i++){
            if (enemies[i].getHealth() < k) {
                a = i;
                k = enemies[i].getHealth();

            }
        }

        MapLocation toAttack = enemies[a].location; // find who to attack
        if (rc.canAttack(toAttack)) {
            rc.attack(toAttack);
        }
        if(enemies[a].getType() != RobotType.ARCHON){
            if (rc.canMove(dir)) {
                rc.move(dir);

            }
        }

    }

    else if (rc.getHealth() < 10) { //low on health, need to run
        RobotInfo[] allies = rc.senseNearbyRobots(radius, ally);
        if (allies.length == 0)
            dir = RobotPlayer.directions[RobotPlayer.rng.nextInt(RobotPlayer.directions.length)];
        else
            dir = rc.getLocation().directionTo(allies[0].location);
        for ( int i = 0; i < allies.length; i ++)
        {
            if(allies[i].getType() == RobotType.ARCHON) {
                dir = rc.getLocation().directionTo(allies[i].location);
                break;
            }
        }

        if (rc.canMove(dir)) {
            rc.move(dir);

        }
    }

    else{
        while(!rc.canMove(dir)){
            dir = dir.rotateLeft();
        }
        if (rc.canMove(dir)) {
            rc.move(dir);

        }
    }
    RobotInfo[] surr = rc.senseNearbyRobots(1, ally);
    if(surr.length == 8)
        rc.disintegrate();
}
}


