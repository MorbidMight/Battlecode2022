package BotV1_2_0;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;

import java.awt.*;

public class LabAI {
    static void runLab(RobotController rc) throws GameActionException {
        int goldDif = rc.getTeamGoldAmount(rc.getTeam().opponent()) - rc.getTeamGoldAmount(rc.getTeam());
        if (goldDif>-1)
            if (rc.canTransmute()) {
                rc.transmute();
            }
    }
}
