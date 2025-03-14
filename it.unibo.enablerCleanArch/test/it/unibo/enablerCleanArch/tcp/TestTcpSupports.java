package it.unibo.enablerCleanArch.tcp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.unibo.enablerCleanArch.supports.tcp.TcpServer;

public class TestTcpSupports {
private TcpServer server;
public static final int testPort = 8112; 


	@Before
	public void up() {
		System.out.println(" =============== ACTIVATING SERVER  " );
		server = new TcpServer("tcpServer",testPort, new NaiveHandler("naiveH") );
		server.activate();		
	}
	
	@After
	public void down() {
		if( server != null ) server.deactivate();
		System.out.println(" =============== SERVER DEACTIVATED" );
	}	
		
	@Test 
	public void testClientNoServer() {
		server.deactivate();
		System.out.println(" ---------------- testClientNoServer" );
  		new ClientForTest().doWorkWithServerOff( "clientNoServer", 3  );	
	}
	
	
	@Test 
	public void testSingleClient() {
		//startTheServer("oneClientServer");
		System.out.println(" ---------------- tesSingleClient");
		//Create a connection
 		new ClientForTest().doWorkWithServerOn( "client1",10  );		
		System.out.println("tesSingleClient BYE");
	}
	
	@Test 
	public void testManyClients() {
 		System.out.println(" ---------------- testManyClients");
  		new ClientForTest().doWorkWithServerOn("client1",10  );
		new ClientForTest().doWorkWithServerOn("client2",1 );
		new ClientForTest().doWorkWithServerOn("client3",1 );
		System.out.println("testManyClients BYE");
	}	
	

 
	
}
