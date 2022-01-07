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
    
    // Try to attack someone
    //add scout implementation
    int radius = rc.getType().actionRadiusSquared;
    Team opponent = rc.getTeam().opponent();
    RobotInfo[] enemies = rc.senseNearbyRobots(radius, opponent);
    Direction dir = RobotPlayer.directions[RobotPlayer.rng.nextInt(RobotPlayer.directions.length)];

    
        if( enemies.length > 0)
            MapLocation toAttack = calcprio(enemies).location; // find who to attack
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

        else if (rc.readSharedArray(10) != 0) {
            MapLocation i = new MapLocation(rc.readSharedArray(10), rc.readSharedArray(11));
            dir = rc.getLocation().directionTo(i);
        }
        else if (enemies.length > 0)
        dir = rc.getLocation().directionTo(enemies[a].location);
        if (rc.canMove(dir)) {
            rc.move(dir);
            System.out.println("I moved!");
        }

}
}


