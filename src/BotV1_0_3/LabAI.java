package BotV1_0_3;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;

public class LabAI {
    static void runLab(RobotController rc)throws GameActionException {
        if(RobotPlayer.turnCount<1800)
            regularBehavior(rc);
        else
            lateGameBehavior(rc);
    }

    static void regularBehavior(RobotController rc){

    }

    static void lateGameBehavior(RobotController rc){

    }


}
