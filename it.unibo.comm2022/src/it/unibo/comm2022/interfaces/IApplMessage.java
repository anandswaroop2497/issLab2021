package it.unibo.comm2022.interfaces;

public interface IApplMessage {
    public String msgId();
    public String msgType();
    public String msgSender();
    public String msgReceiver();
    public String msgContent();
    public String msgNum();
    public boolean isEvent();
    public boolean isDispatch();
    public boolean isRequest();
    public boolean isInvitation();
    public boolean isReply();

}
