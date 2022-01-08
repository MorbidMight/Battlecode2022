package BotV1_1_0;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;

public class LabAI {
    static void runLab(RobotController rc)throws GameActionException {
        if(RobotPlayer.turnCount<1800)
            regularBehavior(rc);
        else
            lateGameBehavior(rc);
    }

    static void regularBehavior(RobotController rc) throws GameActionException {
        if(rc.getTransmutationRate() < 8){
            if(rc.canTransmute()){
                rc.transmute();
            }

        }
    }

    static void lateGameBehavior(RobotController rc) throws GameActionException {
        while(rc.getTransmutationRate() < 10) {
            while(rc.canTransmute()){
                rc.transmute();
            }
        }
    }


}
