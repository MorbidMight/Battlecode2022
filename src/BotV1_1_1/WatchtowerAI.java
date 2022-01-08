package BotV1_1_1;

import battlecode.common.*;

public class WatchtowerAI {
    static void runWatchtower(RobotController rc) throws GameActionException {
        int k = 0;
        int a = 0;
        // Try to attack someone
        //add scout implementation
        int radius = rc.getType().actionRadiusSquared;
        Team opponent = rc.getTeam().opponent();
        RobotInfo[] enemies = rc.senseNearbyRobots(radius, opponent);
        //Direction dir = RobotPlayer.directions[RobotPlayer.rng.nextInt(RobotPlayer.directions.length)];

        if (enemies.length > 0) {
            for (int i = 0; i < enemies.length; i++) {
                if (enemies[i].getHealth() < k) {
                    a = i;
                    k = enemies[i].getHealth();
                }
            }

            MapLocation toAttack = enemies[a].location; // find who to attack
            if (rc.canAttack(toAttack)) {
                rc.attack(toAttack);
            }
        }
    }
}
