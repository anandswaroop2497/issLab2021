package it.unibo.enablerCleanArch.main.local;

import java.util.function.Consumer;

import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Utils;

/*
 * Applicazione che va in coppia con RadarSystemDevicesOnRasp
 */

public class RadarSystemMainLocal implements IApplication{
private IRadarDisplay radar;
private ISonar sonar;
private ILed  led ;
private Controller controller;

	@Override
	public String getName() {	 
		return "RadarSystemMainLocal";
	}

	public void setup( String configFile )  {
		if( configFile != null ) RadarSystemConfig.setTheConfiguration(configFile);
		else {
  			RadarSystemConfig.testing      		= false;			
			RadarSystemConfig.sonarDelay        = 200;
			//Su PC
			RadarSystemConfig.simulation   		= true;
			RadarSystemConfig.DLIMIT      		= 40;  
			RadarSystemConfig.ledGui            = true;
			RadarSystemConfig.RadarGuiRemote    = false;
			//Su Raspberry (nel file di configurazione)
//			RadarSystemConfig.simulation   		= false;
//			RadarSystemConfig.DLIMIT      		= 12;  
//			RadarSystemConfig.ledGui            = false;
//			RadarSystemConfig.RadarGuiRemote    = true;
		}
		configure();
 	}
	
 	
	@Override
	public void doJob(String configFileName) {
		setup(configFileName);
		configure();
		//start
	    ActionFunction endFun = (n) -> { System.out.println(n); terminate(); };
		controller.start(endFun, 30);
	}
	
	protected void configure() {
		//Dispositivi di Input
	    sonar      = DeviceFactory.createSonar();
	    //Dispositivi di Output
	    led        = DeviceFactory.createLed();
	    radar      = RadarSystemConfig.RadarGuiRemote ? null : DeviceFactory.createRadarGui();
	    //Controller
	    controller = Controller.create(led, sonar, radar);	 
	}
  
	public void terminate() {
		//Utils.delay(1000);  //For the testing ...
		sonar.deactivate();
		System.exit(0);
	}

 
 	public IRadarDisplay getRadarGui() { return radar; }
 	public ILed getLed() { return led; }
 	public ISonar getSonar() { return sonar; }
 	public Controller getController() { return controller; }
	
	public static void main( String[] args) throws Exception {
		new RadarSystemMainLocal().doJob(null);
 	}

}
