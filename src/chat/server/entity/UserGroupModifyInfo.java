package chat.server.entity;

public class UserGroupModifyInfo extends Command {
	
	private UserGroupInfo src;
	private UserGroupInfo tar;
	
	
	
	
	public UserGroupModifyInfo() {
	}
	public UserGroupModifyInfo(UserGroupInfo src, UserGroupInfo tar) {
		this.src = src;
		this.tar = tar;
	}
	public UserGroupInfo getSrc() {
		return src;
	}
	public void setSrc(UserGroupInfo src) {
		this.src = src;
	}
	public UserGroupInfo getTar() {
		return tar;
	}
	public void setTar(UserGroupInfo tar) {
		this.tar = tar;
	}
	@Override
	public String toString() {
		return "UserGroupModifyInfo [src=" + src + ", tar=" + tar + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((src == null) ? 0 : src.hashCode());
		result = prime * result + ((tar == null) ? 0 : tar.hashCode());
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
		return true;
	}
	

}
