package chat.server.entity;

import java.io.Serializable;

import chat.server.util.json.annotation.JsonBody;

@JsonBody
public class UserGroupModifyInfo extends Command implements Serializable{
	
	
	private String userName;
	private String src;
	private String tar;
	
	
	public UserGroupModifyInfo(String userName, String src, String tar) {
		this.userName = userName;
		this.src = src;
		this.tar = tar;
	}
	
	public UserGroupModifyInfo(Integer cmd, String userName, String src, String tar) {
		super(cmd);
		this.userName = userName;
		this.src = src;
		this.tar = tar;
	}

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public String getTar() {
		return tar;
	}
	public void setTar(String tar) {
		this.tar = tar;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((src == null) ? 0 : src.hashCode());
		result = prime * result + ((tar == null) ? 0 : tar.hashCode());
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
		UserGroupModifyInfo other = (UserGroupModifyInfo) obj;
		if (src == null) {
			if (other.src != null)
				return false;
		} else if (!src.equals(other.src))
			return false;
		if (tar == null) {
			if (other.tar != null)
				return false;
		} else if (!tar.equals(other.tar))
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
		return "UserGroupModifyInfo [userName=" + userName + ", src=" + src + ", tar=" + tar + "]";
	}
	
	
	
	
	
	
	

}
