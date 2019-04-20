package chat.server.entity;

import chat.server.util.json.annotation.JsonBody;

@JsonBody
public class RegisterInfo extends Command {
	
	public static final int EXISTS_USER=21;
	
	private UserInfo userInfo;

	public RegisterInfo() {}


	public RegisterInfo(Integer cmd, UserInfo userInfo) {
		super(cmd);
		this.userInfo = userInfo;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	@Override
	public String toString() {
		return "RegisterInfo [userInfo=" + userInfo + "]";
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
		RegisterInfo other = (RegisterInfo) obj;
		if (userInfo == null) {
			if (other.userInfo != null)
				return false;
		} else if (!userInfo.equals(other.userInfo))
			return false;
		return true;
	}
	
	
	

}
