package it.unibo.enablerCleanArch.main;
import it.unibo.enablerCleanArch.domain.Controller;
import it.unibo.enablerCleanArch.domain.DeviceFactory;
import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.domain.IRadarDisplay;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.enablers.*;
import it.unibo.enablerCleanArch.enablers.devices.SonarProxyAsClient;

/*
 * Main program for the RapberryPi and real devices
 * TODO: creare un file di configurazione o di properties
 */

public class RadarSystemMainOnRasp {
  	
	public static void main( String[] args) throws Exception {
 		RadarSystemConfig.setTheConfiguration(   );
				
		ISonar sonar    = DeviceFactory.createSonar();
		ILed   led      = DeviceFactory.createLed();
		
		
		//TODO: create the servers
/*				
		if( RadarSystemConfig.ControllerRemote ) {  //Controller on PC
 			new LedEnablerAsServerHandler( "LedServer", RadarSystemConfig.ledPort, ProtocolType.tcp, led );
			Thread.sleep(RadarSystemConfig.applStartdelay);  //Give time to start the application  on the PC
			new SonarEnablerAsClient( "SonarClient", RadarSystemConfig.pcHostAddr, RadarSystemConfig.sonarPort, ProtocolType.tcp, sonar );
			new RadarGuiClient( "RadarGuiClient", RadarSystemConfig.pcHostAddr, RadarSystemConfig.radarGuiPort, ProtocolType.tcp ); 
		}else { //Controller on Rasp
			System.out.println("Controller on Rasp sonar=" + sonar);
 			IRadarDisplay radar =  new RadarGuiClient( "RadarGuiClient", RadarSystemConfig.pcHostAddr, RadarSystemConfig.radarGuiPort, ProtocolType.tcp ); 
 			//Control
			Controller.activate( led, sonar, radar );
 		}
		if( sonar != null ) sonar.activate();
 */		
	}

}
