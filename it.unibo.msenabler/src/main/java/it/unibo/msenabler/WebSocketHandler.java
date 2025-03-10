package it.unibo.msenabler;

import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import it.unibo.enablerCleanArch.domain.IObserver;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.CopyOnWriteArrayList;


/*
In Spring we can create a customized handler by extends abstract class
AbstractWebSocketHandler or one of it's subclass,
either TextWebSocketHandler or BinaryWebSocketHandler:
 */
public class WebSocketHandler extends AbstractWebSocketHandler implements IObserver{
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private static WebSocketHandler handler = null;
    
    public static WebSocketHandler getWebSocketHandler() {
    	if(handler == null) { handler = new WebSocketHandler(); }
    	return handler;
    }
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        ColorsOut.outappl("WebSocketHandler | Added the session:" + session, ColorsOut.ANSI_PURPLE);
        super.afterConnectionEstablished(session);
        //sendToAll("WELCOME!");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        ColorsOut.outappl("WebSocketHandler | Removed the session:" + session, ColorsOut.ANSI_PURPLE);
        super.afterConnectionClosed(session, status);
    }
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        ColorsOut.outappl("WebSocketHandler | New Text Message Received: " + message, ColorsOut.ANSI_PURPLE);
         //Send to all the connected clients
        sendToAll(message);
    }
    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws IOException {
        ColorsOut.outappl("WebSocketHandler | New Binary Message Received " + message.getPayloadLength(), ColorsOut.ANSI_PURPLE );
         //Send to all the connected clients
        Iterator<WebSocketSession> iter = sessions.iterator();
        while( iter.hasNext() ){
            iter.next().sendMessage(message);
        }
    }
    public void sendToAll(String message) throws IOException{
    	ColorsOut.out("WebSocketHandler | sendToAll message=" + message  );
    	sendToAll( new TextMessage(message) );
     }
    public void sendBinaryToAll( byte[] message) throws IOException{
        sendToAllBinary( new BinaryMessage(message) );
    }

    protected void sendToAll(TextMessage message) throws IOException{
        ColorsOut.out("WebSocketHandler | sendToAll " + sessions.size() + " " + message  );
        Iterator<WebSocketSession> iter = sessions.iterator();
        while( iter.hasNext() ){
            iter.next().sendMessage(message);
        }
    }

    protected void sendToAllBinary(BinaryMessage message) throws IOException{
        ColorsOut.out("WebSocketHandler | sendToAllBinary " + sessions.size() + " " + message  );
        Iterator<WebSocketSession> iter = sessions.iterator();
        while( iter.hasNext() ){
            iter.next().sendMessage( message ); //message
        }
    }
	@Override
	public void update(Observable o, Object arg) {
		ColorsOut.out("WebSocketHandler | update " + arg );
		update(""+arg);
		
	}
	@Override
	public void update(String value) {
		try {
			sendToAll(value);
		} catch (IOException e) {
 			ColorsOut.outerr("WebSocketHandler | update ERROR:"+e.getMessage());;
		}	
	}

}
