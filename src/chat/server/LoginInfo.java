package chat.server;

import chat.server.entity.Command;
import chat.server.entity.UserInfo;

public class LoginInfo extends Command{
	
	
	
	public static final int LOGIN_NO_THE_USER=103;
	
	public LoginInfo(Integer cmd, UserInfo userInfo){
		super(cmd);
		
		this.userInfo=userInfo;
	}
	private UserInfo userInfo;
	public UserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
	@Override
	public String toString() {
		return "LoginInfo [userInfo=" + userInfo + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((userInfo == null) ? 0 : userInfo.hashCode());
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
		LoginInfo other = (LoginInfo) obj;
		if (userInfo == null) {
			if (other.userInfo != null)
				return false;
		} else if (!userInfo.equals(other.userInfo))
			return false;
		return true;
	}

	
}
