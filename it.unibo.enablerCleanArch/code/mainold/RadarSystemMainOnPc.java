package it.unibo.enablerCleanArch.main;

import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.enablers.EnablerAsServer;
import it.unibo.enablerCleanArch.enablers.LedProxyAsClient;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.SonarProxyAsClient;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArchapplHandlers.LedApplHandler;
import it.unibo.enablerCleanArchapplHandlers.RadarApplHandler;
import it.unibo.radar.interfaces.IRadar;

/*
 * Applicazione che va in coppia con RadarSystemDevicesOnRasp
 */

public class RadarSystemMainOnPc implements IApplication{
private IRadarDisplay radar = null;

	
	@Override
	public String getName() {	 
		return "RadarSystemMainOnPc";
	}

	public void setup( String configFile )  {
		if( configFile != null ) RadarSystemConfig.setTheConfiguration(configFile);
		//else {
			RadarSystemConfig.simulation   		= true;
			RadarSystemConfig.raspHostAddr 		= "192.168.1.15"; //"192.168.1.183";
			RadarSystemConfig.SonareRemote 		= true;
			RadarSystemConfig.LedRemote    		= true;
			RadarSystemConfig.ControllerRemote  = false;
			RadarSystemConfig.sonarPort    		= 8012;
			RadarSystemConfig.ledPort      		= 8010;
			RadarSystemConfig.radarGuiPort    	= 8014;
			RadarSystemConfig.withContext  		= true;
			RadarSystemConfig.ctxServerPort		= 8018;
			RadarSystemConfig.protcolType       = ProtocolType.mqtt;
			RadarSystemConfig.testing      		= false;			
			RadarSystemConfig.DLIMIT      		= 12; //55
			RadarSystemConfig.sonarDelay        = 250;
		//}
 	}
	
 	
	@Override
	public void doJob(String configFileName) {
		setup(configFileName);
		executeAllOnPc();
	}
	
	public void executeAllOnPc() {
		//Dispositivi di Input
	    ISonar sonar  = DeviceFactory.createSonar();
	    //Dispositivi di Output
	    ILed  led            = DeviceFactory.createLed();
	    IRadarDisplay radar  = DeviceFactory.createRadarGui();
	    //Controller
	    Controller.activate(led, sonar, radar);	//activates the sonar
	}
	
	public void execute() {
		if( ! RadarSystemConfig.ControllerRemote ) {
			ILed clientLedProxy;
			ISonar clientSonarProxy;
			if( RadarSystemConfig.withContext ) {
				 clientLedProxy = new LedProxyAsClient("clientLedProxy", 
						RadarSystemConfig.raspHostAddr, ""+RadarSystemConfig.ctxServerPort, ProtocolType.tcp );
				 clientSonarProxy = new SonarProxyAsClient("clientSonarProxy", 
		 				RadarSystemConfig.raspHostAddr, ""+RadarSystemConfig.ctxServerPort, ProtocolType.tcp );
				
			}else {
				 clientLedProxy = new LedProxyAsClient("clientLedProxy", 
						RadarSystemConfig.raspHostAddr, ""+RadarSystemConfig.ledPort, ProtocolType.tcp );
				 clientSonarProxy = new SonarProxyAsClient("clientSonarProxy", 
						RadarSystemConfig.raspHostAddr, ""+RadarSystemConfig.sonarPort, ProtocolType.tcp );
			}
			Controller.activate(clientLedProxy, clientSonarProxy, radar); //Activates the sonar
		}else { //radarGui enabler
			EnablerAsServer radarServer  = 
					new EnablerAsServer("radarServer",RadarSystemConfig.radarGuiPort, 
							ProtocolType.tcp,  new RadarApplHandler("radarH", radar) );
			radarServer.start();				
		}
	}

	public void terminate() {
		System.exit(0);
	}

 
 	public IRadarDisplay getRadarGui() {
		return radar;
	}

	
	public static void main( String[] args) throws Exception {
		new RadarSystemMainOnPc().doJob(null);
 	}

}
