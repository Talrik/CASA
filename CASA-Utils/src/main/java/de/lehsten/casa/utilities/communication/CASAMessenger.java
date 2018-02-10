package de.lehsten.casa.utilities.communication;

import de.lehsten.casa.utilities.interfaces.Messenger;

public abstract interface CASAMessenger
  extends Messenger
{
  public abstract void setReceiver(String paramString);
  
  public abstract void setLocalReceiver(String paramString);
  
  public abstract void setRemoteReceiver(String paramString);
  
  public abstract void setOwner(String paramString);
  
  public abstract void send(Object paramObject);
  
  public abstract void sendText(String paramString);
  
  public abstract Object request(Object paramObject);
}
