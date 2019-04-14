package chat.server.entity;

public class Command {
	
	public static final int LOGIN=1;
	public static final int REGIST=2;
	public static final int NEW_GROUP=10;
	public static final int REMOVE_GROUP=11;
	public static final int MOVE_TO_GROUP=12;
	public static final int MODIFY_GROUP_NAME=13;
	public static final int SUCCESS=101;
	public static final int FAIL=102;
	
	
	private Integer cmd;
	
	protected Command(){};
	
	protected Command(Integer cmd){
		this.cmd=cmd;
	}

	

	public Integer getCmd() {
		return cmd;
	}

	public void setCmd(Integer cmd) {
		this.cmd = cmd;
	}

	@Override
	public String toString() {
		return "Command [cmd=" + cmd + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cmd == null) ? 0 : cmd.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Command other = (Command) obj;
		if (cmd == null) {
			if (other.cmd != null)
				return false;
		} else if (!cmd.equals(other.cmd))
			return false;
		return true;
	}

	
	

}
