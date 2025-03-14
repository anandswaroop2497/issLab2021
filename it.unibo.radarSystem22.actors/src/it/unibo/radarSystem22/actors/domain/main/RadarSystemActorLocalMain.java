package it.unibo.radarSystem22.actors.domain.main;

import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.kactor.Actor22;
import it.unibo.kactor.ActorBasic;
import it.unibo.radarSystem22.actors.businessLogic.ControllerActor;
import it.unibo.radarSystem22.actors.domain.support.DeviceActorFactory;
import it.unibo.radarSystem22.actors.domain.support.DomainData;
import it.unibo.radarSystem22.domain.interfaces.IRadarDisplay;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
 
 
/*
 * Questo sistema NON usa la infrastruttura Qak per gli attori
 * ma solo Actor22
 */
public class RadarSystemActorLocalMain {
	
	private ActorBasic led ;
	private ActorBasic sonar ;
	private IRadarDisplay radar;
	private ActorBasic controller;
	
 
	public void doJob() {
		ColorsOut.outappl("RadarSystemActorLocalMain | Start", ColorsOut.BLUE);
		configure();
		BasicUtils.aboutThreads("Before execute - ");
		//BasicUtils.waitTheUser();
		execute();
	}
	
	
	protected void configure() {
		led        = DeviceActorFactory.createLed(DomainData.ledName);
		//for( int i=1; i<=3; i++ ) { DeviceActorFactory.createLed("led"+i); }
		sonar      = DeviceActorFactory.createSonar(DomainData.sonarName);
		radar      = DeviceActorFactory.createRadarGui();
		controller = new ControllerActor(DomainData.controllerName, led, sonar,(ActorBasic)radar);
			
	}
	
	protected void execute() {
		//MsgUtil.sendMsg(DomainMsg.controllerActivate, controller, null); //null � continuation.
		Actor22.sendAMsg( DomainData.controllerActivate, controller);
		 
	} 
	
 	
	public static void main( String[] args) {
		DomainSystemConfig.tracing      = false;			
		DomainSystemConfig.sonarDelay   = 150;
		//Su PC
		DomainSystemConfig.simulation   = true;
		DomainSystemConfig.DLIMIT      	= 80;  
		DomainSystemConfig.ledGui       = true;
 
		BasicUtils.aboutThreads("Before start - ");
		new RadarSystemActorLocalMain().doJob();
		if( ! DomainSystemConfig.ledGui ) BasicUtils.delay(5000);   
		BasicUtils.aboutThreads("Before end - ");
	}
/*
 * Threads  
 *    main		1
 *    sonar		1 (simulator from Domain)
 *    ledgui	1
 *    radar   	2
 *    Actor22
 *    In tutto 6
 */
	
}
