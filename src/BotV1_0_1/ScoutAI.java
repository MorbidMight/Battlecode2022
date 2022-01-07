package BotV1_0_1;

import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;

public class ScoutAI {
    static void runScout(RobotController rc)
    {
    RobotPlayer.friendlys[RobotPlayer.turnCount]  = rc.senseNearbyRobots(rc.getType().visionRadiusSquared,rc.getTeam());
    RobotPlayer.enemies[RobotPlayer.turnCount] = rc.senseNearbyRobots(rc.getType().visionRadiusSquared,rc.getTeam().opponent());

    }
}
