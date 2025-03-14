package it.unibo.enablerCleanArch.main;
 
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;

import it.unibo.enablerCleanArch.domain.IApplication;
import it.unibo.enablerCleanArch.domain.IDistance;
import it.unibo.enablerCleanArch.domain.IObserver;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.domain.ISonarObservable;
import it.unibo.enablerCleanArch.domain.SonarObserverFortesting;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.ProxyAsClient;
import it.unibo.enablerCleanArch.enablers.SonarProxyAsClient;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArch.supports.coap.CoapApplObserver;
import it.unibo.enablerCleanArch.supports.coap.CoapApplServer;
import it.unibo.enablerCleanArch.supports.coap.CoapSupport;
import it.unibo.enablerCleanArch.supports.coap.SonarResourceCoap;
import it.unibo.enablerCleanArch.supports.coap.example.ObserverNaive;
import it.unibo.enablerCleanArchapplHandlers.SonarDistanceHandler;

/*
 * Eredita il Sonar da 
 */

public class SonarUsageMainCoap  extends SonarUsageAbstractMain implements IApplication{  

private CoapClient clientObs = null;
private CoapObserveRelation relObs = null;

protected ISonar clientSonarProxy = null;
protected boolean useProxyClient = true;
protected IObserver obsfortesting;

	@Override //IApplication
	public String getName() {
		return "SonarUsageMainCoap";
	}

	 
	public void setUp( String fName ) {
		super.setUp(fName);
 		RadarSystemConfig.sonarObservable = true;		
	}
	
	@Override
	protected void configureTheServer() {
		CoapApplServer.getTheServer();  //singleton; call fatta anche da SonarResourceCoap
		new SonarResourceCoap("sonar", sonar); 
	}

	//Called by the inherited configure
	@Override
	protected void createObservers() {
		if( RadarSystemConfig.sonarObservable ) {
			boolean oneShot         = false;
			obsfortesting           = new SonarObserverFortesting("obsfortesting", oneShot) ; 
// 	 		sonar                   = DeviceFactory.createSonarObservable();
			((ISonarObservable) sonar).register( obsfortesting );
		} 		
		//Attivo un observer CoAP
		boolean withRadar = false;
		relObs = createAnObserverCoap(withRadar);	
	}
	
	@Override
	public void execute() {
		if( useProxyClient ) executeCoapUsingProxyClients();
		else executeCoapUsingCoapSupport();
	}

	protected void executeCoapUsingProxyClients() {
 		String host      = RadarSystemConfig.pcHostAddr;
 		String sonarUri  = CoapApplServer.inputDeviceUri+"/sonar";
 		clientSonarProxy = new SonarProxyAsClient("clientSonarProxy", host, sonarUri, ProtocolType.coap );
		
  		//Attivo il Sonar
		clientSonarProxy.activate();	 
		
		for( int i=1; i<=5; i++) {
			IDistance v = clientSonarProxy.getDistance( );  
			ColorsOut.outappl("SonarUsageMainCoap | execute with proxyClient i=" + i + " getDistance="+v.getVal(), ColorsOut.ANSI_PURPLE);
			Utils.delay(500);
		}	 
 		//Tolgo l'observer CoAP
		relObs.proactiveCancel();
		Utils.delay(300);
		//Tolgo l'observer sul Sonar POJO
		if( RadarSystemConfig.sonarObservable) ((ISonarObservable) sonar).unregister(obsfortesting); 
		Utils.delay(300);
		//Fermo il Sonar
		clientSonarProxy.deactivate();
 	}
	
	protected void executeCoapUsingCoapSupport() {
 		String sonarUri = CoapApplServer.inputDeviceUri+"/sonar";
		CoapSupport cps = new CoapSupport("localhost", sonarUri);
		
		cps.forward("activate");	//messaggi 'semplici'
		for( int i=1; i<=5; i++) {
			String v = cps.request("");  
			ColorsOut.outappl("SonarUsageMainCoap | executeCoap with CoapSupport i=" + i + " getVal="+v, ColorsOut.ANSI_PURPLE);
			Utils.delay(500);
		}	 
		cps.forward("deactivate");	//termina il Sonar
		cps.close();
	}

	/* 
	 * Attiva un ObserverNaive 
	 * oppure
	 * un CoapApplObserver che usa SonarMessageHandler per visualizzare su RadarGui
	 */
	protected CoapObserveRelation createAnObserverCoap( boolean withRadar) {
		String sonarUri   = CoapApplServer.inputDeviceUri+"/sonar";
 		String sonarAddr  = "coap://localhost:5683/"+sonarUri;
		clientObs         =  new CoapClient( sonarAddr );
		CoapHandler obs;
		if( withRadar ) {
 		     obs=new CoapApplObserver( "localhost", sonarUri,new SonarDistanceHandler( "sonarH" ) );	
		}else {
			 obs=new ObserverNaive("obsnaive");}
		CoapObserveRelation relObs = clientObs.observe(obs);
		return relObs;
	}

	public void terminate() {
		ColorsOut.outappl("SonarUsageMainCoap | terminate", ColorsOut.ANSI_PURPLE );
		CoapApplServer.stopTheServer();  
			//Fermo il clientObs
			if( clientObs != null ) {
				ColorsOut.outappl("SonarUsageMainCoap | terminate clientObs="+clientObs, ColorsOut.ANSI_PURPLE );
				clientObs.shutdown();	
			}
			//Chiudo le connessioni
			if( clientSonarProxy != null )  ((ProxyAsClient) clientSonarProxy).close();
 		ColorsOut.outappl("SonarUsageMainCoap | terminate BYE", ColorsOut.ANSI_PURPLE );	
		System.exit(0); //per via di CoapHandler  ...
	}

	@Override
	public void doJob(String fName) {
		setUp(fName);
		configure();
		execute();
		Utils.delay(500);
		terminate();
	}
	
	public static void main( String[] args) throws Exception {
		new SonarUsageMainCoap().doJob("RadarSystemConfig.json");	

	}




}

