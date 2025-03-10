package it.unibo.enablerCleanArch.main;
import it.unibo.enablerCleanArch.domain.Controller;
import it.unibo.enablerCleanArch.domain.DeviceFactory;
import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.domain.IRadarDisplay;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.enablers.*;
import it.unibo.enablerCleanArch.supports.coap.LedResourceCoap;
import it.unibo.enablerCleanArch.supports.coap.SonarResourceCoap;
import it.unibo.enablerCleanArch.supports.coap.DeviceType;

/*
 * Main program for the RapberryPi and real devices
 * TODO: creare un file di configurazione o di properties
 */

public class RadarSystemMainOnPcLikeRaspCoap {
  	
	public static void main( String[] args) throws Exception {
 		RadarSystemConfig.setTheConfiguration( "RadarSystemConfigPcSimulatedDevices.json"  );
 				
		ISonar sonar    = DeviceFactory.createSonar();
		ILed   led      = DeviceFactory.createLed();
		if( RadarSystemConfig.ControllerRemote ) {  //Controller on PC
 			 new LedResourceCoap("led", led);
 			 //Thread.sleep(RadarSystemConfig.applStartdelay);  //Give time to start the application  on the PC
			 new SonarResourceCoap("sonar",sonar) ;    			
		}else { //Controller on Rasp
			System.out.println("Controller on PcLikeRasp  "  );
 			IRadarDisplay radar =  
 					new RadarGuiProxyAsClient( "RadarGuiClient", RadarSystemConfig.pcHostAddr, ""+RadarSystemConfig.radarGuiPort, ProtocolType.tcp ); 
 			//Control
			Controller.activate( led, sonar, radar );
 		}
 		
	}

}
