package it.unibo.enablerCleanArch.concur;

import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.ProxyAsClient;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArch.supports.context.TcpContextServer;

/*
 * Un oggetto contatore di nome 'counter' (classe CounterWithDelay) con valore iniziale 2 
 * esegue l'operazione dec rilasciando il controllo per un certo tempo.
 * Questo contatore viene reso capace di gestire messaggi da un CounterApplHandler che lo incapsula.
 * Due client inviano il comando (dispatch) dec a 'counter', che per� non va a 0
 * Il sistema attiva 4 thread (main, TcpContextSerer e due client)
 */
public class SharedCounterExampleMain  {
private int ctxServerPort   = 7070;
private String delay        = "1000"; //con delay = 0 funziona

ApplMessage msgDec = new ApplMessage(
	      "msg( dec, dispatch, main, counter, dec(DELAY), 1 )"
	      .replace("DELAY", delay));
 
	public void configure(  ) {
 		Utils.aboutThreads("Before configure - ");
		CounterWithDelay counter         = new CounterWithDelay("counter");
 		TcpContextServer contextServer   = new TcpContextServer("TcpContextServer",  ctxServerPort );
		CounterApplHandler counterH      = new CounterApplHandler("counterH", counter);
		contextServer.addComponent(counter.getName(),counterH);	
 		contextServer.activate();    
 		Utils.aboutThreads("After configure - ");
 	}
	
	public void execute() throws Exception {
		ProxyAsClient client1 = new ProxyAsClient("client1","localhost", ""+ctxServerPort, ProtocolType.tcp);
 		ProxyAsClient client2 = new ProxyAsClient("client2","localhost", ""+ctxServerPort, ProtocolType.tcp);
 		client1.sendCommandOnConnection(msgDec.toString());  
 		client2.sendCommandOnConnection(msgDec.toString());		
 		Utils.aboutThreads("After client send - ");
	}
 
 
	public static void main( String[] args) throws Exception {	
		SharedCounterExampleMain sys = new SharedCounterExampleMain();
		RadarSystemConfig.withContext = true;
		RadarSystemConfig.tracing     = false;
		
		sys.configure();
		sys.execute();
 		Thread.sleep(2500);
 		Utils.aboutThreads("Before end - ");
		System.exit(0);
	}
}
