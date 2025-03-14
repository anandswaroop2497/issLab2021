package it.unibo.enablerCleanArch.concur;
  
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.supports.ApplMsgHandler;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Interaction2021;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.kactor.MsgUtil;
 
 
public class CounterApplHandler extends ApplMsgHandler {
private CounterWithDelay counter;
private CounterActorWithDelay ca;

	public CounterApplHandler( String name, CounterWithDelay counter ) {
		 super(name);
		 this.counter = counter;
 	}
	public CounterApplHandler( String name, CounterActorWithDelay counter ) {
		 super(name);
 		 ca = counter;
	}
	
	@Override
	public void elaborate(String cmd, Interaction2021 conn) {		
		ColorsOut.out(name + " | (not used) elaborate cmd: "+cmd);
	} 
	
	@Override
	public void elaborate( ApplMessage msg, Interaction2021 conn ) {
		if( counter != null ) elaborateForObject( msg );
		if( ca != null ) elaborateForActor( msg );
	}
	
	protected int getDecDelayArg(String cmd) throws Exception{
		Struct cmdT     = (Struct) Term.createTerm(cmd);
		String cmdName  = cmdT.getName();
		if( cmdName.equals("dec")) {
			int delay = Integer.parseInt(cmdT.getArg(0).toString());
			//ColorsOut.outappl(name + " | dec delay="+delay, ColorsOut.GREEN);
			return delay;
 		}else return 0;		
	}
	
	protected void elaborateForObject( ApplMessage msg  ) {
		ColorsOut.outappl(name + " | elaborateForObject ApplMessage: "+msg, ColorsOut.GREEN);		
		String answer=null;
		try {
			String cmd =  msg.msgContent();
 			int delay   = getDecDelayArg(cmd);
 			counter.dec(delay);	
			answer = ""+counter.getVal();
			//ColorsOut.outappl(name + " | elaborate ApplMessage answer: "+answer, ColorsOut.GREEN);
			if( msg.isRequest() ) {
				ApplMessage  reply = Utils.prepareReply(msg, answer);
				sendAnswerToClient(reply.toString());			
			}
		}catch( Exception e) {}	
	}
	
	protected void elaborateForActor( ApplMessage cmd ) {
		ColorsOut.outappl(name + " | elaborateForActor ApplMessage: "+cmd, ColorsOut.GREEN);		
 		try {
 			int delay = getDecDelayArg(cmd.msgContent());
 			String msgContent =  ""+delay;
 			it.unibo.kactor.ApplMessage decCmd = MsgUtil.buildDispatch("main","dec",msgContent,"ca");	
 			ColorsOut.out(name + " | elaborateForActor sends:" + decCmd);
 			MsgUtil.sendMsg(decCmd, ca, null);
			 
		}catch( Exception e) {}	
	}
}
