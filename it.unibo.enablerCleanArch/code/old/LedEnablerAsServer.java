package it.unibo.enablerCleanArch.enablers;

import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.domain.ILed;
 
/*
 * Deve inviare messaggi TCP
 */
public class LedEnablerAsServer  extends EnablerAsServer {  
ILed led;   
	public LedEnablerAsServer( String name,  int port, ProtocolType protocol, ILed led, String handlerClassName  )   {
		super(name, port, protocol, handlerClassName );  
		this.led = led;
 	}

	@Override		//from ApplMessageHandler
	public void elaborate(String message) {
 		System.out.println(name+" | elaborate:" + message);
 		if( message.equals("on")) led.turnOn();
 		else if( message.equals("off") ) led.turnOff();
 		//Added after  TcpContextServer
 		try {
 			ApplMessage msg = new ApplMessage(message);
 			if( msg.msgContent().equals("on")) led.turnOn();
 			else if( msg.msgContent().equals("off")) led.turnOff();
 		}catch( Exception e) {
 		}
	}
}
