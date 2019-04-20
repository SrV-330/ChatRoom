package chat.server.entity;

import java.io.Serializable;

import chat.server.util.json.annotation.JsonBody;

@JsonBody
public class UserGroupInfo extends Command implements Serializable{
	
	private String userName;
	private String groupName;

	
	public static final int GROUP_REPEATE=13;
	public static final int NO_THE_GROUP=14;

	

	public UserGroupInfo() {
		super();
	}


	public UserGroupInfo(String userName, String groupName) {
		super();
		this.userName = userName;
		this.groupName = groupName;
	}


	public UserGroupInfo(Integer cmd, String userName, String groupName) {
		super(cmd);
		this.userName = userName;
		this.groupName = groupName;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getGroupName() {
		return groupName;
	}


	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}


	@Override
	public String toString() {
		return "UserGroupInfo [userName=" + userName + ", groupName=" + groupName + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
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
		UserGroupInfo other = (UserGroupInfo) obj;
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
	
	

}
