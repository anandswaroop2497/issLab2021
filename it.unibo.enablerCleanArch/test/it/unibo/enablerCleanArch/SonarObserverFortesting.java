package it.unibo.enablerCleanArch;

import static org.junit.Assert.assertTrue;

import java.util.Observable;

import it.unibo.enablerCleanArch.domain.IObserver;
import it.unibo.enablerCleanArch.domain.ISonarObservable;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ColorsOut;

class SonarObserverFortesting implements IObserver{
	private String name;
	private boolean oneShot = false;
	private int v0          = -1;
	private int delta       =  1;
	private ISonarObservable sonar;
	
	public SonarObserverFortesting(String name, ISonarObservable sonar, boolean oneShot) {
		this.name    = name;
		this.sonar   = sonar;
		this.oneShot = oneShot;
	}
	@Override
	public void update(Observable source, Object data) {
		 //Colors.out( name + " | update data=" + data ); //+ " from " + source	
		 update( data.toString() );
	}

	@Override
	public void update(String vs) {
 		 if(oneShot) {
 			 ColorsOut.out( name + "| oneShot value=" + vs, ColorsOut.ANSI_YELLOW );  
 			 assertTrue(  vs.equals( ""+RadarSystemConfig.testingDistance) );	
 		 }else {
 			 int value = Integer.parseInt(vs);
 			 if( v0 == -1 ) {	//set the first value observed
 				v0 = value;
 				ColorsOut.out( name + "| v0=" + v0, ColorsOut.ANSI_YELLOW);
 			 }else {
 				ColorsOut.out( name + "| value=" + value, ColorsOut.ANSI_YELLOW );  
  				int vexpectedMin = v0-delta;
 				int vexpectedMax = v0+delta;
 				assertTrue(  value <= vexpectedMax && value >= vexpectedMin );
 				v0 = value;			 
 				//if( v0 == 30 && name.equals("obs1")) sonar.unregister(this);
 			 }
 		 }
	}
	
}

