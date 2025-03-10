/*
============================================================
ApplMsgs

============================================================
*/
package it.unibo.robotService

import it.unibo.actor0.ApplMessage
import it.unibo.actor0.MsgUtil

/*
{"robotmove":"MOVE", "time":T}
{"endmove":"RESULT", "move":MOVE}
{ "sonarName": "sonarName", "distance": 1, "axis": "x" }
{ "collision" : "false", "move": "moveForward"}
 */

object ApplMsgs {
    val forwardMsg = "{\"robotmove\":\"moveForward\", \"time\": 350}"
    val backwardMsg = "{\"robotmove\":\"moveBackward\", \"time\": 350}"
    val microStepMsg = "{\"robotmove\":\"moveForward\", \"time\": 5}"
    val littleBackwardMsg = "{\"robotmove\":\"moveBackward\", \"time\": 5}"
    val turnLeftMsg = "{\"robotmove\":\"turnLeft\", \"time\": 300}"
    val turnRightMsg = "{\"robotmove\":\"turnRight\", \"time\": 300}"
    val haltMsg = "{\"robotmove\":\"alarm\", \"time\": 20}"
    val goBackMsg = "{\"goBack\":\"goBack\" }"
    val resumeMsg = "{\"resume\":\"resume\" }"
    val forwardstepMsg = "{\"robotmove\":\"moveForward\", \"time\": 350}"

    val activateId = "activate"
    val activateMsg = "{\"ID\":\"ARGS\" }".replace("ID", activateId)

    val executorStartId = "executorstart"
    val executorstartMsg = "{\"ID\":\"PATHTODO\" }".replace("ID", executorStartId)
    val executorDoneId = "executorDone"
    val executorFailId = "executorFail"
    val executorendokMsg = "{\"ID\":\"ok\" }".replace("ID", executorDoneId)
    val executorendkoMsg = "{\"ID\":\"PATHDONE\" }".replace("ID", executorFailId)

    val robotMoveId     = "robotmove"
    val robotMoveTimeId = "time"
    val robotMoveMsg = "{\"MOVEID\":\"MOVE\",\"TIMEID\":TIME}"
        .replace("MOVEID", robotMoveId).replace("TIMEID", robotMoveTimeId)

    val endMoveId = "endmove"
    val endMoveMsg = "{\"ID\":\"ENDMOVE\" }".replace("ID", endMoveId)
    val executorEndId = "executorend"

    //
    val runawyEndId = "runawayend"
    val runawyEndMsg = "{\"ID\":\"RESULT\" }".replace("ID", runawyEndId)
    val runawyStartId = "runawaystart"
    val runawyStartMsg = "{\"ID\":\"PATHTODO\" }".replace("ID", runawyStartId)

    //
    val stepId      = "step"
    val stepMsg     = "{\"$stepId\":\"TIME\" }"
    val stepDoneId  = "stepDone"
    val stepDoneMsg = "{\"$stepDoneId\":\"ok\" }"
    val stepFailId  = "stepFail"
    val stepFailMsg = "{\"$stepFailId\":\"TIME\" }"

    val robotMovecmdId  = "robotmovecmd"
    val robotMovecmdMsg = "{\"$robotMovecmdId\":\"MOVE\" }"

    val startAnyId  = "start"
    val startMsgStr = "{\"$startAnyId\":\"ok\" }"
    val endMsgStr = "{\"end\":\"ok\" }"

    fun shortMove( move : String ) : String{
        when (move) {
            "turnLeft"      -> return "l"
            "turnRight"     -> return "r"
            "moveForward"   -> return "w"
            "moveBackward"  -> return "s"
            "haltMsg"       -> return "h"
            else            -> return "h"
        }
    }


    fun stepRobot_w(caller:String, time: String="350") : ApplMessage {
        return MsgUtil.buildDispatch(caller, "move", forwardMsg.replace("350",time), "stepRobot")
    }
    fun stepRobot_s(caller:String, time: String="350") : ApplMessage {
        return MsgUtil.buildDispatch(caller, "move", backwardMsg.replace("350",time), "stepRobot")
    }
    fun stepRobot_h(caller:String, time: String="20") : ApplMessage {
        return MsgUtil.buildDispatch(caller, "move", haltMsg.replace("20",time), "stepRobot")
    }
    fun stepRobot_l(caller:String) : ApplMessage {
        return MsgUtil.buildDispatch(caller, "move", turnLeftMsg, "stepRobot")
    }
    fun stepRobot_r(caller:String) : ApplMessage {
        return MsgUtil.buildDispatch(caller, "move", turnRightMsg, "stepRobot")
    }
    fun stepRobot_turn(caller:String, dir: String="r") : ApplMessage {
        val turnMsg = if (dir=="l")  turnLeftMsg else turnRightMsg //no ternary op in kotlin
        return MsgUtil.buildDispatch(caller, "move", turnMsg, "stepRobot")
    }
    fun stepRobot_step(caller:String, time:String="350") : ApplMessage {
        return MsgUtil.buildDispatch(caller, "step", stepMsg.replace("TIME",time), "stepRobot")
    }

    fun startAny( caller:String) : ApplMessage {
        return MsgUtil.buildDispatch(caller, startAnyId, startMsgStr, "any")
    }
    fun endAny( caller:String) : ApplMessage {
        return MsgUtil.buildDispatch(caller, startAnyId, endMsgStr, "any")
    }

    fun executorOkEnd( caller:String, pathDone:String="", dest:String="any" ) :  ApplMessage {
        return MsgUtil.buildDispatch(caller, executorEndId, executorendokMsg.replace("ok",pathDone), dest)
    }
    fun executorFailEnd( caller:String, pathDone: String ) :  ApplMessage {
        val answer = executorendkoMsg.replace("PATHDONE", pathDone)
        return MsgUtil.buildDispatch(caller, executorEndId, answer, "any")
    }

    fun testCreateMsg( ) :  ApplMessage {
        return MsgUtil.buildDispatch("tester", "cmd", robotMoveMsg, "any")
    }



}