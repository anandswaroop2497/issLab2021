/* Generated by AN DISI Unibo */ 
package it.unibo.ledmock

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Ledmock ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		 var Ledvalue = "off"  
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("ledmock start")
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("work") { //this:State
					action { //it:State
					}
					 transition(edgeName="t03",targetState="ledupdate",cond=whenEvent("ledchange"))
				}	 
				state("ledupdate") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("ledchange(V)"), Term.createTerm("ledchange(V)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 Ledvalue = payloadArg(0)  
						}
						if(  Ledvalue =="on"  
						 ){println("%%%%% LED ON")
						}
						else
						 {println("%%%%% LED OFF")
						 }
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
			}
		}
}
