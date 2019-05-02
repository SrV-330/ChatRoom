package chat.server.entity;

import java.io.Serializable;

import chat.server.util.json.annotation.JsonBody;

@SuppressWarnings("serial")
@JsonBody
public class SendMessageInfo extends Command implements Serializable,Cloneable{
	
	private String sendUserName;
	private String receiveUserName;
	private String message;
	
	
	
	
	public SendMessageInfo() {
	}
	public SendMessageInfo(String sendUserName, String receiveUserName, String message) {
		this.sendUserName = sendUserName;
		this.receiveUserName = receiveUserName;
		this.message = message;
	}
	public SendMessageInfo(Integer cmd, String sendUserName, String receiveUserName, String message) {
		super(cmd);
		this.sendUserName = sendUserName;
		this.receiveUserName = receiveUserName;
		this.message = message;
	}
	public String getSendUserName() {
		return sendUserName;
	}
	public void setSendUserName(String sendUserName) {
		this.sendUserName = sendUserName;
	}
	public String getReceiveUserName() {
		return receiveUserName;
	}
	public void setReceiveUserName(String receiveUserName) {
		this.receiveUserName = receiveUserName;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "SendMessageInfo [sendUserName=" + sendUserName + ", receiveUserName=" + receiveUserName + ", message="
				+ message + "]";
	}
	
	public SendMessageInfo copy(){
		try {
			return (SendMessageInfo) this.clone();
		} catch (CloneNotSupportedException e) {
			return null;
			
		}
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((receiveUserName == null) ? 0 : receiveUserName.hashCode());
		result = prime * result + ((sendUserName == null) ? 0 : sendUserName.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SendMessageInfo other = (SendMessageInfo) obj;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (receiveUserName == null) {
			if (other.receiveUserName != null)
				return false;
		} else if (!receiveUserName.equals(other.receiveUserName))
			return false;
		if (sendUserName == null) {
			if (other.sendUserName != null)
				return false;
		} else if (!sendUserName.equals(other.sendUserName))
			return false;
		return true;
	}
	
	
	
}
