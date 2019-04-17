package chat.server.util.json;

import java.util.ArrayList;
import java.util.List;

import chat.server.util.json.annotation.JsonBody;
@JsonBody
public class User {
	
	private String name;
	private int age;
	private Birth birth;
	private List<Birth> list;
	public User() {
		this.birth=new Birth();
	}
	public User(String name, int age, Birth birth) {
		this.name = name;
		this.age = age;
		this.birth = birth;
		this.list=new ArrayList<>();
		list.add(new Birth("1","2","3"));
		list.add(new Birth("4","5","6"));
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Birth getBirth() {
		return birth;
	}
	public void setBirth(Birth birth) {
		this.birth = birth;
	}
//	@Override
//	public String toString() {
//		return "User [name=" + name + ", age=" + age + ", birth=" + birth + "]";
//	}
	
	
	@Override
	public String toString() {
		return "{\"name\":\"" + name + "\",\"age\":\"" + age + "\",\"birth\":" + birth + ",\"list\":" + list + "}";
	}
	

	
}
