package BotV1_4_0;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;

public class LabAI {
    static void runLab(RobotController rc) throws GameActionException {
        int goldDif = rc.getTeamGoldAmount(rc.getTeam().opponent()) - rc.getTeamGoldAmount(rc.getTeam());
        if (goldDif>-1)
            catchUp(rc);
       /* } else if (RobotPlayer.turnCount < 1800)
            regularBehavior(rc);
        else
           lateGameBehavior(rc); */
    }

    static void regularBehavior(RobotController rc) throws GameActionException {
        if (rc.getTransmutationRate() < 8 && rc.getTeamLeadAmount(rc.getTeam()) > 400) {
            if (rc.canTransmute()) {
                rc.transmute();
            }

        }
    }

    static void catchUp(RobotController rc) throws GameActionException {


            if (rc.canTransmute()) {
                rc.transmute();
            }

    }

    static void lateGameBehavior(RobotController rc) throws GameActionException {
        if (rc.getTransmutationRate() < 11) {
            if (rc.canTransmute()) {
                rc.transmute();
            }
        }
    }


}
