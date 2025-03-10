ckage it.unibo.enablerCleanArch.domain;

import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Colors;

public abstract class SonarModelObservable extends SonarModel implements ISonarObservable  {
	protected IDistanceMeasured observableDistance  ;		
 		
	public static ISonarObservable create() {
		if( RadarSystemConfig.simulation )  return new SonarMockObservable();
		else  return new SonarConcreteObservable();		
	}

//	@Override
//	protected void sonarSetUp() {
//		observableDistance = new DistanceMeasured( );
// 		Colors.out("SonarModelObservable | sonarSetUp curVal="+curVal);
// 		observableDistance.setVal(curVal);
// 	} 	
	
	//Ritorna il valore pi� recente
//	@Override
//	public IDistance getDistance() {
//  		return curVal;
// 	}

 
	
<<<<<<< HEAD
// 	@Override  //from SonarModel
//	protected void updateDistance( int d ) {
//		curVal = new Distance( d );
//		Colors.out("SonarModelObservable | updateDistance "+curVal.getVal(), Colors.GREEN);
//		observableDistance.setVal( curVal );    //notifies the observers 
//		//Non aggiorniamo la coda perch� per un observable non ci sono consumatori
// 		//super.updateDistance(d);	             
//	}
=======
 	@Override  //from SonarModel
	protected void updateDistance( int d ) {
		curVal = new Distance( d );
		Colors.out("SonarModelObservable | updateDistance "+curVal.getVal(), Colors.GREEN);
		observableDistance.setVal( curVal );    //notifies the observers 
		//Non aggiorniamo la coda perch� per un observable non ci sono consumatori MA getState()??
 		//super.updateDistance(d);	  
		//A MENO CHE NON SIA Mqtt
 		if( RadarSystemConfig.protcolType  == ProtocolType.mqtt) {
 			super.updateDistance(d);
 			return;
 		}
 	}
>>>>>>> 478543bed26300eac3db1b6c95108a911c0d3abd

 	@Override
	public void register(IObserver obs) {
		Colors.out("SonarModelObservable | register on observableDistance obs="+obs);
		observableDistance.addObserver(obs);		
	}

	@Override
	public void unregister(IObserver obs) {
		Colors.out("SonarModelObservable | unregister obs="+obs);
		observableDistance.deleteObserver(obs);		
	}
  
}
