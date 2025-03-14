package it.unibo.comm2022.udp;

import it.unibo.comm2022.interfaces.IApplMsgHandler;
import it.unibo.comm2022.interfaces.Interaction2021;
import it.unibo.comm2022.utils.ColorsOut;

/*
 * Ente attivo per la ricezione di messaggi su una connessione Interaction2021
 */
public class UdpApplMessageHandler extends Thread{
private  IApplMsgHandler handler ;
private Interaction2021 conn;

public UdpApplMessageHandler(  IApplMsgHandler handler, Interaction2021 conn ) {
		this.handler = handler;
		this.conn    = conn;
 		this.start();
	}
 	
	@Override 
	public void run() {
		String name = handler.getName();
		try {
			ColorsOut.outappl( "UdpApplMessageHandler.java | STARTS with handler=" + name + " conn=" + conn, ColorsOut.BLUE );
			while( true ) {
				//ColorsOut.out(name + " | waits for message  ...");
			    String msg = conn.receiveMsg();
			    ColorsOut.outappl(name + "  | UdpApplMessageHandler.java received:" + msg + " handler="+handler.getName(), ColorsOut.GREEN );
			    if( msg == null ) {
			    	conn.close();
			    	break;
			    } else{ handler.elaborate( msg, conn ); }
			}
			ColorsOut.out(name + " | BYE"   );
		}catch( Exception e) {
			ColorsOut.outerr( name + "UdpApplMessageHandler  | ERROR:" + e.getMessage()  );
		}	
	}
}
