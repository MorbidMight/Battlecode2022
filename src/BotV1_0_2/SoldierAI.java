package BotV1_0_2;

import battlecode.common.*;

public class SoldierAI {  /**
 * Run a single turn for a Soldier.
 * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
 */
static void runSoldier(RobotController rc) throws GameActionException {
    int k = 0;
    int a = 0;
    // Try to attack someone
    //add scout implementation
    int radius = rc.getType().actionRadiusSquared;
    Team opponent = rc.getTeam().opponent();
    RobotInfo[] enemies = rc.senseNearbyRobots(radius, opponent);
    Direction dir = RobotPlayer.directions[RobotPlayer.rng.nextInt(RobotPlayer.directions.length)];

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
        if (rc.canMove(dir)) {
            rc.move(dir);
            System.out.println("I moved!");
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


