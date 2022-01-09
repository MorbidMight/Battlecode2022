package BotV1_2_0;

import battlecode.common.*;

public class WatchtowerAI {
    static  public final double archpmod = 1;
    static public final double soldpmod = 0.7;
    static public final double minepmod = 0.3;
    static final double buildpmod = 0.4;
    static final double watchpmod = 0.6;
    static final double sagepmod = 0.7;
    static final double labpmod = 0.5;
    static RobotInfo calcprio (RobotInfo[] k) {
        int j = 1; // index holder
        double a = 0; // highest prio holder

        for (int i = 0; i < k.length; i++) {
            if(k[i].getType() == RobotType.ARCHON){
                if(archpmod * ((k[i].getType().getMaxHealth(k[i].getLevel()) - k[i].getHealth()) + 1) > a) {
                    a = archpmod * ((k[i].getType().getMaxHealth(k[i].getLevel()) - k[i].getHealth())+1);
                    j = i;
                }
            }
            else if (k[i].getType() == RobotType.SOLDIER){
                if(soldpmod * ((k[i].getType().getMaxHealth(k[i].getLevel()) - k[i].getHealth())+1) > a) {
                    a = soldpmod * ((k[i].getType().getMaxHealth(k[i].getLevel()) - k[i].getHealth())+1);
                    j = i;
                }
            }
            else if ((k[i].getType() == RobotType.MINER)){
                if(minepmod * ((k[i].getType().getMaxHealth(k[i].getLevel()) - k[i].getHealth())+1) > a) {
                    a = minepmod * ((k[i].getType().getMaxHealth(k[i].getLevel()) - k[i].getHealth())+1);
                    j = i;
                }
            }
            else if ((k[i].getType() == RobotType.BUILDER)){
                if(buildpmod * ((k[i].getType().getMaxHealth(k[i].getLevel()) - k[i].getHealth())+1) > a) {
                    a = buildpmod * ((k[i].getType().getMaxHealth(k[i].getLevel()) - k[i].getHealth())+1);
                    j = i;
                }
            }
            else if ((k[i].getType() == RobotType.WATCHTOWER)){
                if(watchpmod * ((k[i].getType().getMaxHealth(k[i].getLevel()) - k[i].getHealth())+1) > a) {
                    a = watchpmod * ((k[i].getType().getMaxHealth(k[i].getLevel()) - k[i].getHealth())+1);
                    j = i;
                }
            }
            else if ((k[i].getType() == RobotType.SAGE)){
                if(sagepmod * ((k[i].getType().getMaxHealth(k[i].getLevel()) - k[i].getHealth())+1) > a) {
                    a = sagepmod * ((k[i].getType().getMaxHealth(k[i].getLevel()) - k[i].getHealth())+1);
                    j = i;
                }
            }
            else if ((k[i].getType() == RobotType.LABORATORY)){
                if(labpmod * ((k[i].getType().getMaxHealth(k[i].getLevel()) - k[i].getHealth())+1) > a) {
                    a = labpmod * ((k[i].getType().getMaxHealth(k[i].getLevel()) - k[i].getHealth())+1);
                    j = i;
                }
            }

        }
        return k[j];
    }
    static void runWatchtower(RobotController rc) throws GameActionException {
        int k = 0;
        int a = 0;
        // Try to attack someone
        //add scout implementation
        int radius = rc.getType().actionRadiusSquared;
        Team opponent = rc.getTeam().opponent();
        RobotInfo[] enemies = rc.senseNearbyRobots(radius, opponent);
        //Direction dir = RobotPlayer.directions[RobotPlayer.rng.nextInt(RobotPlayer.directions.length)];



            MapLocation toAttack = calcprio(enemies).location; // find who to attack
            if (rc.canAttack(toAttack)) {
                rc.attack(toAttack);
            }
        }
    }

