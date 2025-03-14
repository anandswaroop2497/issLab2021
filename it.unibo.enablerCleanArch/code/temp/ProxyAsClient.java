package it.unibo.enablerCleanArch.enablers;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Interaction2021;
import it.unibo.enablerCleanArch.supports.TcpClientSupport;
import it.unibo.enablerCleanArch.supports.coap.CoapSupport;
import it.unibo.enablerCleanArch.supports.mqtt.MqttSupport;
import it.unibo.enablerCleanArchapplHandlers.ClientApplHandlerMqtt;


public class ProxyAsClient {
private Interaction2021 conn; 
protected String name ;		//could be a uri
protected ProtocolType protocol ;

/*
 * Realizza la connessione di tipo Interaction2021 (concetto astratto)
 * in modo diverso, a seconda del protocollo indicato (tecnologia specifica)
 */
 
	public ProxyAsClient( String name, String host, String entry, ProtocolType protocol ) {
		try {
			this.name     = name;
			this.protocol = protocol;			 
			setConnection(host,  entry,  protocol);
			Colors.out(name+"  | STARTED conn=" + conn );
		} catch (Exception e) {
			Colors.outerr( name+"  |  ERROR " + e.getMessage());		}
	}
	
	protected void setConnection( String host, String entry, ProtocolType protocol  ) throws Exception {
		switch( protocol ) {
			case tcp : {
				int port = Integer.parseInt(entry);
				conn = TcpClientSupport.connect(host,  port, 10); //10 = num of attempts
				//Colors.out(name + " |  setConnection "  + conn );		
				break;
			}
			case coap : {
				conn = new CoapSupport("CoapSupport_"+name, host,  entry);  //entry is uri path
				break;
			}
			case mqtt : {
				Colors.out(name+"  | ProxyAsClient connect MQTT entry=" + entry );
				conn = MqttSupport.getSupport();
				((MqttSupport) conn).connect(name, entry, RadarSystemConfig.mqttBrokerAddr);  //Serve solo per spedire
				ClientApplHandlerMqtt h = new ClientApplHandlerMqtt(name+"Handler",conn); //prior to connecting
				((MqttSupport) conn).subscribe(name, entry+name+"answer", h);	
				break;
			}
				
		}
 
	}
  	
	public void sendCommandOnConnection( String cmd )  {
 		//Colors.out( name+"  | sendCommandOnConnection " + cmd + " conn=" + conn );
		try {
			conn.forward(cmd);
		} catch (Exception e) {
			Colors.outerr( name+"  | sendCommandOnConnection ERROR=" + e.getMessage()  );
		}
	}
	public String sendRequestOnConnection( String request )  {
 		//Colors.out( name+"  | sendRequestOnConnection request=" + request + " conn=" + conn );
		try {
			String answer = conn.request(request);
			Colors.out( name+"  | sendRequestOnConnection-answer=" + answer  );
			return answer;			
		} catch (Exception e) {
			Colors.outerr( name+"  | sendRequestOnConnection ERROR=" + e.getMessage()  );
			return null;
		}
 	}	
	public Interaction2021 getConn() {
		return conn;
	}
	
	public void close() {
		try {
			conn.close();
			Colors.out(name + " |  CLOSED " + conn  );
		} catch (Exception e) {
			Colors.outerr( name+"  | sendRequestOnConnection ERROR=" + e.getMessage()  );		}
	}
}
