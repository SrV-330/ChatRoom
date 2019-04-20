package chat.server.entity;

import java.io.Serializable;

import chat.server.util.json.annotation.JsonBody;

@JsonBody
public class UserGroupModifyInfo extends Command implements Serializable{
	
	private String src;
	private String tar;
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
	public UserGroupModifyInfo(String src, String tar) {
		this.src = src;
		this.tar = tar;
	}
	public UserGroupModifyInfo() {
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
