package chat.server.entity;

import java.io.Serializable;

import chat.server.util.json.annotation.JsonBody;

@JsonBody
public class FriendInfo extends Command implements Serializable{
	
	private String userName;
	private String friendName;
	private String groupName;
	
	
	
	
	public FriendInfo() {
		super();
	}
	public FriendInfo(String userName, String friendName) {
		super();
		this.userName = userName;
		this.friendName = friendName;
		
	}
	public FriendInfo(String userName, String friendName, String groupName) {
		super();
		this.userName = userName;
		this.friendName = friendName;
		this.groupName = groupName;
	}

	public FriendInfo(Integer cmd, String userName, String friendName, String groupName) {
		super(cmd);
		this.userName = userName;
		this.friendName = friendName;
		this.groupName = groupName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFriendName() {
		return friendName;
	}
	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((friendName == null) ? 0 : friendName.hashCode());
		result = prime * result + ((groupName == null) ? 0 : groupName.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
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
		FriendInfo other = (FriendInfo) obj;
		if (friendName == null) {
			if (other.friendName != null)
				return false;
		} else if (!friendName.equals(other.friendName))
			return false;
		if (groupName == null) {
			if (other.groupName != null)
				return false;
		} else if (!groupName.equals(other.groupName))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FriendInfo [userName=" + userName + ", friendName=" + friendName + ", groupName=" + groupName + "]";
	}
	
	
	
	
	
	

}
