package mapRoomKotlin

import com.andreapivetta.kolor.Color
import it.unibo.actor0.sysUtil

object mapUtil{
 	var state = RobotState(0,0,Direction.DOWN)
	var map   = RoomMap.getRoomMap()


    @JvmStatic fun getMapAndClean() : String{ //(fName : String="storedMap.txt")
		val outS = map.toString()
		RoomMap.resetRoomMap()
		return outS
	}


//=================================================================================
    @JvmStatic fun getMapRep() : String{
        return map.toString()
    }
    @JvmStatic fun getDirection() : String{
        return state.direction.toString()
    }

    fun setObstacleOnCell(){
        sysUtil.colorPrint("setObstacleOnCell ${state.x},${state.y}", Color.RED)
		map.put( state.x,  state.y, Box(true, false, false))
	}
    @JvmStatic
    fun setObstacle() {  //trick!!
        doMove("w")
        setObstacleOnCell()
        //back without cleaning the current cell
        state = state.backward();
        map.put(state.x, state.y, Box(false, false, true))
    }
    @JvmStatic fun doMove(move: String ) {
       val x = state.x
       val y = state.y
//       println("doMove move=$move  dir=${state.direction} x=$x y=$y dimMapX=$map.dimX{} dimMapY=${map.dimY}")
       try {
            when (move) {
                "w" -> {
                     map.put(x, y, Box(false, false, false)) //clean the cell
					 state = state.forward();
                     map.put(state.x, state.y, Box(false, false, true))
                }
                "s" -> {
                     map.put(x, y, Box(false, false, false)) //clean the cell
	                 state = state.backward();
                     map.put(state.x, state.y, Box(false, false, true))
                }
                "a"  -> {
                     map.put(state.x, state.y, Box(false, false, true))
                }
                "l" -> {
					  state = state.turnLeft();
                      map.put(state.x, state.y, Box(false, false, true))
                }
                "d" -> {
                     map.put(state.x, state.y, Box(false, false, true))
                }
                "r" -> {
 					state = state.turnRight();
                    map.put(state.x, state.y, Box(false, false, true))
                }
 
		   }//switch
		   
//		   println( "$map"  )
        } catch (e: Exception) {
            println("doMove: ERROR:" + e.message)
        }
	}

    @JvmStatic fun showMap(){
		sysUtil.colorPrintNoTab( "$map ${state}", Color.CYAN  )
	}
	
}