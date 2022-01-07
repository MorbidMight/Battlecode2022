package BotV1_0_2;

import battlecode.common.*;

public class SoldierAI {  /**
 * Run a single turn for a Soldier.
 * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
 */
static  public final double archpmod = 1;
static public final double soldpmod = 0.6;
static public final double minepmod = 0.3;
static final double buildpmod = 0.4;
static final double watchpmod = 0.5;
static final double sagepmod = 0.7;
static final double labpmod = 0.5;
private static Direction dirOfAttack;

static RobotInfo calcprio (RobotInfo[] k) {
    int j = 0; // index holder
    double a = 0; // highest prio holder

    for (int i = 0; i < k.length; i++) {
        if(k[i].getType() == RobotType.ARCHON){
            if(archpmod * (k[i].getType().getMaxHealth(k[i].getLevel()) - k[i].getHealth()) > a) {
                a = archpmod * (k[i].getType().getMaxHealth(k[i].getLevel()) - k[i].getHealth());
                j = i;
            }
        }
        else if (k[i].getType() == RobotType.SOLDIER){
            if(soldpmod * (k[i].getType().getMaxHealth(k[i].getLevel()) - k[i].getHealth()) > a) {
                a = soldpmod * (k[i].getType().getMaxHealth(k[i].getLevel()) - k[i].getHealth());
                j = i;
            }
        }
        else if ((k[i].getType() == RobotType.MINER)){
            if(minepmod * (k[i].getType().getMaxHealth(k[i].getLevel()) - k[i].getHealth()) > a) {
                a = minepmod * (k[i].getType().getMaxHealth(k[i].getLevel()) - k[i].getHealth());
                j = i;
            }
        }

    }
    return k[j];
}
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
    
    // Try to attack someone
    //add scout implementation
    int radius = rc.getType().actionRadiusSquared;
    Team opponent = rc.getTeam().opponent();
    RobotInfo[] enemies = rc.senseNearbyRobots(radius, opponent);
    Team ally = rc.getTeam();
    MapLocation toAttack;
    if( enemies.length > 0) {
        toAttack = calcprio(enemies).location; // find who to attack
        if (rc.canAttack(toAttack)) {
            rc.attack(toAttack);
        }
        
    }

    if (rc.getHealth() < 10) { //low on health, need to run
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
            System.out.println("I moved!");
        }
    }

   /* else if (rc.readSharedArray(10) != 0) {
        MapLocation i = new MapLocation(rc.readSharedArray(10), rc.readSharedArray(11));
        dir = rc.getLocation().directionTo(i);
    }*/
    while(!rc.canMove(dir)){
        dir = dir.rotateLeft();
    }
    if (rc.canMove(dir)) {
        rc.move(dir);
        System.out.println("I moved!");
    }

}
}


