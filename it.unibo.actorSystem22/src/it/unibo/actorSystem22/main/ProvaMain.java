package it.unibo.actorSystem22.main;

import it.unibo.actorComm.ProtocolType;
import it.unibo.actorComm.proxy.ProxyAsClient;
import it.unibo.actorSystem22.context.TcpCtxServer;
import it.unibo.kactor.Actor22;
import it.unibo.kactor.IApplMessage;
import it.unibo.kactor.MsgUtil;
import it.unibo.kactor.sysUtil;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;


/*
 * Impostare anche i supporti con attori si pu� fare se 
 * gli attori del supporto sono attivati con 
 * confined= false e ioBound = true
 * 
 * TODO: creare un client in Python
 */

public class ProvaMain {
	private ProxyAsClient client; 
	private IApplMessage cmdOn ,cmdOff ;
	private int port = 8013;
	
	public ProvaMain() {
		startTheServer();
		cmdOn  = MsgUtil.buildDispatch("main", "cmd", "turnOn",  "led");
		cmdOff = MsgUtil.buildDispatch("main", "cmd", "turnOff", "led");
		sysUtil.INSTANCE.setTrace(true) ;
	}
	public void doJob() {
		DomainSystemConfig.ledGui = true;
		Actor22 led = new LedMockActor("led");
		Actor22.sendAMsg(cmdOff, led);
		client = new ProxyAsClient("client", "localhost", ""+port, ProtocolType.tcp);
		
		client.sendCommandOnConnection( cmdOn.toString() );
		BasicUtils.delay(1000);
		client.sendCommandOnConnection( cmdOff.toString() );
	}
	
	public void startTheServer() {
		new TcpCtxServer("server",port);
	}
	public static void main( String[] args) {
		new ProvaMain().doJob();
	}
}
