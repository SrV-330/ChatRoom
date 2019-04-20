package chat.server.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import chat.server.util.json.annotation.JsonBody;
@JsonBody
public class FriendGroupInfo extends Command implements Serializable{
	
	private List<UserGroupInfo> groups=new ArrayList<>();
	private List<FriendInfo> friends=new ArrayList<>();
	
	
	
	public FriendGroupInfo() {
	}
	public FriendGroupInfo(List<UserGroupInfo> groups, List<FriendInfo> friends) {
		this.groups = groups;
		this.friends = friends;
	}
	public FriendGroupInfo(Integer cmd, List<UserGroupInfo> groups, List<FriendInfo> friends) {
		super(cmd);
		this.groups = groups;
		this.friends = friends;
	}
	public List<UserGroupInfo> getGroups() {
		return groups;
	}
	public void setGroups(List<UserGroupInfo> groups) {
		this.groups = groups;
	}
	public List<FriendInfo> getFriends() {
		return friends;
	}
	public void setFriends(List<FriendInfo> friends) {
		this.friends = friends;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((friends == null) ? 0 : friends.hashCode());
		result = prime * result + ((groups == null) ? 0 : groups.hashCode());
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
		FriendGroupInfo other = (FriendGroupInfo) obj;
		if (friends == null) {
			if (other.friends != null)
				return false;
		} else if (!friends.equals(other.friends))
			return false;
		if (groups == null) {
			if (other.groups != null)
				return false;
		} else if (!groups.equals(other.groups))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "FriendGroupInfo [groups=" + groups + ", friends=" + friends + "]";
	}
	
	
	
	
	

}
