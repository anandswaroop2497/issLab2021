package it.unibo.enablerCleanArch.domain;

import it.unibo.enablerCleanArch.useCases.LedAlarmUsecase;
import it.unibo.enablerCleanArch.useCases.RadarGuiUsecase;

/*
 * Il Controller riceve dati dal sonar e attiva gli use cases
 */
public class Controller {
	
	public static void activate( ILed led, ISonar sonar,IRadarGui radar) {
		new Thread() {
			public void run() { 
				try {
					while( true ) {
						int d = sonar.getVal(); //ricevo dato dal sonar come message emitter. Bloccante?
						System.out.println("Controller | sonar data=" + d);
						LedAlarmUsecase.doUseCase( led, d  );
						RadarGuiUsecase.doUseCase( radar,d );
						Thread.sleep(1000);   //Remove se sonar.getVal � bloccante
					}
				} catch (Exception e) {
		 			e.printStackTrace();
				}					
			}
		}.start();
		
	}
 

}
