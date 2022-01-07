package BotV1_0_1;

import battlecode.common.*;

public class ScoutAI {
    static void runScout(RobotController rc) throws GameActionException {
        RobotPlayer.friendlys[RobotPlayer.turnCount] = rc.senseNearbyRobots(rc.getType().visionRadiusSquared, rc.getTeam());
        RobotPlayer.enemies[RobotPlayer.turnCount] = rc.senseNearbyRobots(rc.getType().visionRadiusSquared, rc.getTeam().opponent());

        for (int i = 0; i < RobotPlayer.enemies[RobotPlayer.turnCount].length; i++) {
            if (RobotPlayer.enemies[RobotPlayer.turnCount][i].type.equals(RobotType.ARCHON)) {
                MapLocation ArchonLocation = RobotPlayer.enemies[RobotPlayer.turnCount][i].location;
                rc.writeSharedArray(10, ArchonLocation.x);
                rc.writeSharedArray(11, ArchonLocation.y);

            }

        }
    }
}
