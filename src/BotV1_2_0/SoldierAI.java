package BotV1_2_0;

import battlecode.common.*;

public class SoldierAI {  /**
 * Run a single turn for a Soldier.
 * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
 */
private static Direction dirOfAttack;
    private static boolean isSurrounded;
    static void runSoldier(RobotController rc) throws GameActionException {
        //find and go toward leader soldier

        int radius = rc.getType().actionRadiusSquared;
        Team opponent = rc.getTeam().opponent();

        boolean canSenseArchon = false;


        if (rc.getRoundNum() < 1500) {
            RobotInfo[] enemies = rc.senseNearbyRobots(radius, opponent);
            Direction dir;
            boolean isLeader;
            //make a leader if there is no leader
            MapLocation myLoc = rc.getLocation();
            if (rc.readSharedArray(0) == 0) {
                rc.writeSharedArray(0, rc.getID());
                isLeader = true;
                //random dir to leader
                dir = RobotPlayer.directions[RobotPlayer.rng.nextInt(RobotPlayer.directions.length)];
                dirOfAttack = dir;
                //assign ints in the SharedArray to the coordinates of the leader
                rc.writeSharedArray(1, myLoc.x);
                rc.writeSharedArray(2, myLoc.y);
            } else if (rc.readSharedArray(0) == rc.getID()) {
                dir = dirOfAttack;
                //change direction if the bot can't move to direction
                int rotations = 0;
                while (!rc.canMove(dir) && rotations < 8) {
                    dir = dir.rotateRight();
                    rotations++;
                }
                //change leader if leader can't move
                if (rotations == 8) {
                    rc.writeSharedArray(0, 0);
                }
                //assign ints in the SharedArray to the coordinates of the leader
                rc.writeSharedArray(1, myLoc.x);
                rc.writeSharedArray(2, myLoc.y);
                //change leader if leader is low in health
                if (rc.getHealth() < 20) {
                    rc.writeSharedArray(0, 0);
                }
            }
            //find the direction of leader and set dir to diection of leader
            else {
                if (myLoc.x < rc.readSharedArray(1)) {
                    if (myLoc.y < rc.readSharedArray(2)) {
                        dir = Direction.NORTHWEST;
                    } else if (myLoc.y > rc.readSharedArray(2)) {
                        dir = Direction.SOUTHWEST;
                    } else {
                        dir = Direction.WEST;
                    }
                } else {
                    if (myLoc.y < rc.readSharedArray(2)) {
                        dir = Direction.NORTHEAST;
                    } else if (myLoc.y > rc.readSharedArray(2)) {
                        dir = Direction.SOUTHEAST;
                    } else {
                        dir = Direction.EAST;
                    }
                }
            }
            while(!rc.canMove(dir)){
                dir = RobotPlayer.directions[RobotPlayer.rng.nextInt(RobotPlayer.directions.length)];
            }
            if(rc.canMove(dir))
                rc.move(dir);


            int k = 0;
            int a = 0;
            // Try to attack someone
            //add scout implementation


            //Direction dir = RobotPlayer.directions[RobotPlayer.rng.nextInt(RobotPlayer.directions.length)];

            Team ally = rc.getTeam();
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

            boolean adjMiner = false;
            RobotInfo[] surr = rc.senseNearbyRobots(1, ally);
            for (int i = 0; i < surr.length; i++) {
                if (surr[i].getType() == RobotType.MINER) {
                    adjMiner = true;
                    break;
                }

            }

            if (surr.length == 8 && adjMiner && rc.senseRubble(rc.getLocation()) < 30)
                rc.disintegrate();
        }else if (rc.readSharedArray(13) == 0)
        {
            int k = 0;
            int a = 0;
            // Try to attack someone
            //add scout implementation


            RobotInfo[] enemies = rc.senseNearbyRobots(radius, opponent);
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

            MapLocation enemyArchon = Utilities.enemyArchonLocater(ArchonAI.readArchonCoords(rc), rc)[0];
            Direction dirToAttack = rc.getLocation().directionTo(enemyArchon);
            if(rc.canMove(dirToAttack))
            {
                rc.move(dirToAttack);
            }
            else if (rc.canMove(dirToAttack.rotateRight())){
                rc.move(dirToAttack.rotateRight());
            }
            else if( rc.canMove(dirToAttack.rotateLeft())){
                rc.move(dirToAttack.rotateLeft());
            }
            enemies = rc.senseNearbyRobots(radius, opponent);
            for(RobotInfo i: enemies) {
                if (i.getType().equals(RobotType.ARCHON)){
                    canSenseArchon = true;
                    break;
                }
            }
            if(rc.canSenseLocation(enemyArchon)){
                RobotInfo atLocation = rc.senseRobotAtLocation(enemyArchon);
                if(atLocation != null)
                {
                    if(!(atLocation.getType().equals(RobotType.ARCHON)))
                    {
                        rc.writeSharedArray(13, rc.readSharedArray(13) + 1);
                    }
                }
            }
        }
        else if (rc.readSharedArray(13) == 1){

            int k = 0;
            int a = 0;
            // Try to attack someone
            //add scout implementation


            RobotInfo[] enemies = rc.senseNearbyRobots(radius, opponent);
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
            MapLocation enemyArchon = Utilities.enemyArchonLocater(ArchonAI.readArchonCoords(rc), rc)[1];
            Direction dirToAttack = rc.getLocation().directionTo(enemyArchon);
            if(rc.canMove(dirToAttack))
            {
                rc.move(dirToAttack);
            }
            else if (rc.canMove(dirToAttack.rotateRight())){
                rc.move(dirToAttack.rotateRight());
            }
            else if( rc.canMove(dirToAttack.rotateLeft())){
                rc.move(dirToAttack.rotateLeft());
            }
            enemies = rc.senseNearbyRobots(radius, opponent);
            for(RobotInfo i: enemies) {
                if (i.getType().equals(RobotType.ARCHON)){
                    canSenseArchon = true;
                    break;
                }
            }
            if(rc.canSenseLocation(enemyArchon)){
                RobotInfo atLocation = rc.senseRobotAtLocation(enemyArchon);
                if(atLocation != null)
                {
                    if(!(atLocation.getType().equals(RobotType.ARCHON)))
                    {
                        rc.writeSharedArray(13, rc.readSharedArray(13) + 1);
                    }
                }
            }
        }
        else if (rc.readSharedArray(13) == 2){

            int k = 0;
            int a = 0;
            // Try to attack someone
            //add scout implementation


            RobotInfo[] enemies = rc.senseNearbyRobots(radius, opponent);
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
            MapLocation enemyArchon = Utilities.enemyArchonLocater(ArchonAI.readArchonCoords(rc), rc)[2];
            Direction dirToAttack = rc.getLocation().directionTo(enemyArchon);
            if(rc.canMove(dirToAttack))
            {
                rc.move(dirToAttack);
            }
            else if (rc.canMove(dirToAttack.rotateRight())){
                rc.move(dirToAttack.rotateRight());
            }
            else if( rc.canMove(dirToAttack.rotateLeft())){
                rc.move(dirToAttack.rotateLeft());
            }
            enemies = rc.senseNearbyRobots(radius, opponent);
            for(RobotInfo i: enemies) {
                if (i.getType().equals(RobotType.ARCHON)){
                    canSenseArchon = true;
                    break;
                }
            }
            if(rc.canSenseLocation(enemyArchon)){
                RobotInfo atLocation = rc.senseRobotAtLocation(enemyArchon);
                if(atLocation != null)
                {
                    if(!(atLocation.getType().equals(RobotType.ARCHON)))
                    {
                        rc.writeSharedArray(13, rc.readSharedArray(13) + 1);
                    }
                }
            }
        }
        else if (rc.readSharedArray(13) == 3){

            int k = 0;
            int a = 0;
            // Try to attack someone
            //add scout implementation


            RobotInfo[] enemies = rc.senseNearbyRobots(radius, opponent);
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
            MapLocation enemyArchon = Utilities.enemyArchonLocater(ArchonAI.readArchonCoords(rc), rc)[3];
            Direction dirToAttack = rc.getLocation().directionTo(enemyArchon);
            if(rc.canMove(dirToAttack))
            {
                rc.move(dirToAttack);
            }
            else if (rc.canMove(dirToAttack.rotateRight())){
                rc.move(dirToAttack.rotateRight());
            }
            else if( rc.canMove(dirToAttack.rotateLeft())){
                rc.move(dirToAttack.rotateLeft());
            }
            enemies = rc.senseNearbyRobots(radius, opponent);
            for(RobotInfo i: enemies) {
                if (i.getType().equals(RobotType.ARCHON)){
                    canSenseArchon = true;
                    break;
                }
            }
            if(rc.canSenseLocation(enemyArchon)){
                RobotInfo atLocation = rc.senseRobotAtLocation(enemyArchon);
                if(atLocation != null)
                {
                    if(!(atLocation.getType().equals(RobotType.ARCHON)))
                    {
                        rc.writeSharedArray(13, rc.readSharedArray(13) + 1);
                    }
                }
            }
        }
    }
}


