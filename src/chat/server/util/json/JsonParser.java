package chat.server.util.json;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import chat.server.util.json.annotation.JsonBody;
import chat.server.util.json.annotation.JsonGetter;
import chat.server.util.json.annotation.JsonSetter;
import chat.server.util.json.exception.JsonParserException;
import chat.server.util.json.exception.JsonUnsupportClassException;

public class JsonParser {
	
	private List<Class<?>> classes=new ArrayList<>();
	private List<String> classesName=new ArrayList<>();
	protected Queue<Character> jsonQueue=new LinkedList<>();
	protected Stack<Object> stack=new Stack<>();
	protected Object target;
	public JsonParser() throws ClassNotFoundException{
		
		
		parserAnnotation("");
	}
	public JsonParser(String jsonBody,Object target) throws ClassNotFoundException{
		this();
		Character c=null;
		for(int i=0;i<jsonBody.length();i++){
			c=jsonBody.charAt(i);
			
			jsonQueue.offer(c);
			
		}
		removeSpace();
		this.target=target;
	}
	
	public Object parser(String jsonBody,Object target) throws Exception{
		jsonQueue.clear();
		for(int i=0;i<jsonBody.length();i++){
			jsonQueue.offer(jsonBody.charAt(i));
		}
		removeSpace();
		this.target=target;
		return doParser();
		
		
	}
	
	private Object doParser() throws Exception{
		
		if(jsonQueue.isEmpty()) return null;
		int c=jsonQueue.peek();
		if(c!='{') throw new JsonParserException("json must start with symbol \'{\' ");
		c=jsonQueue.poll();
		stack.push((char)c);
		stack.push(target);
		return parserObject(target);
	}
	private void removeSpace(){
		while(jsonQueue.peek()<=20){
			jsonQueue.poll();
		}
	}
	private Object parserObject(Object tar) throws Exception{
		int c=-1;
		
		while(!jsonQueue.isEmpty()){
			c=jsonQueue.poll();
			if(c=='{'){
				stack.push((char)c);
				if(tar instanceof ArrayList) continue;
				
				stack.push(tar);
				
			
			}else if(c=='['){
				stack.push((char)c);
				stack.push((List<Object>)tar);
			}else if(c==':'){
				
				removeSpace();
				c=jsonQueue.peek();
				if(c=='"'){
					parserField();
				}else if(c=='{'){
					parserField();
				}else if(c=='['){
					parserField();
				}else{
					
					throw new JsonParserException("there must be symbol \'{\',\'[\' or \'\"\' after \':\'");
				}
			}else if(c<=20){
				continue;
			}else if(c=='"'){
				stack.push(parserString());			
			}else if(c==','){
				removeSpace();
			}else if(c==']'){
				List<Object> l=(List<Object>) stack.pop();
				
				c=(char)stack.pop();
				
				if(c=='[')
					return l;
			}else if(c=='}'){
				
				Object o=stack.pop();
				
				c=(char)stack.pop();
				
				if(c=='{'){
					if(stack.isEmpty()) return o;
					else{
						
						
						Object l=null ;
						if((l=stack.peek()) instanceof ArrayList){
							((List<Object>)l).add(o);
							continue;
						}else{
							return o;
						}
					}
				}
				
				
				throw new JsonParserException("json must end with symbol \'}\' ");
				
			}else{
				throw new JsonParserException("can not parser char \'"+c+"\'");
			}
		}
		
		throw new JsonParserException("json must end with symbol \'}\' ");

	}

	private void parserField() throws Exception{
		int c=-1;
		c=jsonQueue.peek();
		if(c=='"'){
			jsonQueue.poll();
			Object o=stack.pop();
			String fieldName="";
			if(!(o instanceof String)) throw new JsonParserException("can not cast class "+o+" to String");
			
			fieldName=(String)o;
			
			Object tar=stack.peek();
			
			if(tar==null){
				System.out.println("null:"+fieldName);
				tar=findClassByField(fieldName);
			}else if((tar.getClass() ==char.class||tar.getClass() ==Character.class)
					&&((Character)tar=='{')){
				
				tar=findClassByField(fieldName);
				
				stack.push(tar);
			}
			if(tar==null)throw new JsonParserException("parsered class must have annottation @JsonBody");
			
			
			Class<?> clazz= tar.getClass();
			
			Field field=getField(fieldName, clazz,JsonSetter.class);//clazz.getDeclaredField(fieldName);
			//field.setAccessible(true);
			//System.out.println(fieldName);
			Class<?> fieldClass=field.getType();
			
			if(fieldClass==Long.class||fieldClass==long.class){
				field.set(tar,Long.parseLong(parserString()));
			}else if(fieldClass==Integer.class||fieldClass==int.class){
				field.set(tar,Integer.parseInt(parserString()));
			}else if(fieldClass==Short.class||fieldClass==short.class){
				field.set(tar,Short.parseShort(parserString()));
			}else if(fieldClass==Byte.class||fieldClass==byte.class){
				field.set(tar,(byte)Short.parseShort(parserString()));
			}else if(fieldClass==Boolean.class||fieldClass==boolean.class){
				field.set(tar,Boolean.parseBoolean(parserString()));
			}else if(fieldClass==Double.class||fieldClass==double.class){
				field.set(tar,Double.parseDouble(parserString()));
			}else if(fieldClass==Float.class||fieldClass==float.class){
				field.set(tar,Float.parseFloat(parserString()));
			}else{
				field.set(tar, parserString());
			}
			
		}else if(c=='{'){
			Object o=stack.pop();
			String fieldName="";
			if(!(o instanceof String)) throw new JsonParserException("can not cast class "+o+" to String");
			
			fieldName=(String)o;
			
			Object tar=stack.peek();
			Class<?> clazz= tar.getClass();
			
			Field field=getField(fieldName, clazz,JsonSetter.class);//clazz.getDeclaredField(fieldName);
			//field.setAccessible(true);
			
			field.set(tar, parserObject(field.get(tar)));
		}else if(c=='['){
			
			Object o=stack.pop();
			String fieldName="";
			if(!(o instanceof String)) throw new JsonParserException("can not cast class "+o+" to String");
			fieldName=(String)o;
			Object tar=stack.peek();
			Class<?> clazz= tar.getClass();
			
			Field field=getField(fieldName, clazz,JsonSetter.class);//clazz.getDeclaredField(fieldName);
			//field.setAccessible(true);
			
			List<Object> list=(List<Object>)field.get(tar);
			list=ArrayList.class.newInstance();

			
			field.set(tar, parserObject(list));
			
		}else {
			throw new JsonParserException("there must be symbol \'{\',\'[\' or \'\"\' after \':\'");
		}
		
		
		
	}
	private Object findClassByField(String fieldName){
		
		Object o=null;
		for(Class clazz:classes){
			System.out.println(clazz.getName());
			try {
				clazz.getDeclaredField(fieldName);
				o=clazz.newInstance();
				return o;
			} catch (Exception e) {
				continue;
			}
		}
		return null;
	}
	private String parserString() {
		
		Character c=0;
		StringBuilder sb=new StringBuilder("");
		while((c=jsonQueue.poll())!='"'&&!jsonQueue.isEmpty()){
			sb.append(c);
		}
		
		if(jsonQueue.isEmpty()) throw new JsonParserException("miss symbol \'\"\' in the end");
		if(c=='"'){
			return sb.toString();
		}else{
			throw new JsonParserException("miss symbol \'\"\' in the end");
		}
		
		
	}
	
	private Field getField(String fieldName,Class clazz,Class gs){
		
		Field f=null;
		if(clazz==Object.class) return null;
		try{
			f= clazz.getDeclaredField(fieldName);
			f.setAccessible(true);
			
			if(clazz.isAnnotationPresent(JsonBody.class)||f.isAnnotationPresent(gs)){
				if((f.getModifiers()&Modifier.STATIC)!=Modifier.STATIC){
					return f;
				}
			}
			return null;
			
		}catch(Exception e){
			clazz=clazz.getSuperclass();
			return getField(fieldName,clazz,gs);
		}
		
	}
	private void getFields(Class clazz,List<Field> fieldList,Class gs){
		if(clazz==Object.class) return;
		Field[] fields=clazz.getDeclaredFields();
		for(Field field:fields){
			if(clazz.isAnnotationPresent(JsonBody.class)||field.isAnnotationPresent(gs)){
				if((field.getModifiers()&Modifier.STATIC)!=Modifier.STATIC){
					fieldList.add(field);
				}
			}
		}
		getFields(clazz.getSuperclass(), fieldList,gs);
		
	}
	
	public String parseToJson(Object o) throws Exception{
		
		StringBuilder sbJson=new StringBuilder("{");
		
		Class<?> clazz=o.getClass();
		//Field[] fields=clazz.getDeclaredFields();
		List<Field> fields=new ArrayList<>();
		getFields(clazz, fields,JsonGetter.class);
		List<String> ls=new ArrayList<String>();
		List<String> ls1=new ArrayList<String>();
		List<String> ls2=new ArrayList<String>();
		List<String> ls3=new ArrayList<>();
		
		for(Field field:fields){
			field.setAccessible(true);
			Class<?> fieldClazz=field.getType();
			if(fieldClazz==Integer.class||fieldClazz==Long.class||
					fieldClazz==Short.class||fieldClazz==Boolean.class||
					fieldClazz==Byte.class||fieldClazz==Double.class||
					fieldClazz==Float.class||fieldClazz==Character.class||
					fieldClazz==String.class){
				ls.add("\""+field.getName()+"\":\""+field.get(o)+"\"");
				
			}else if(fieldClazz.isPrimitive()){
				ls.add("\""+field.getName()+"\":\""+field.get(o)+"\"");
				
			}else if(fieldClazz==List.class){
				List<Object> l=(List<Object>)field.get(o);
				StringBuilder s=new StringBuilder();
				s.append("\""+field.getName()+"\":[");
				for(Object o1:l){
					
					ls1.add(parseToJson(o1));
				}
				s.append(String.join(",", ls1));
				s.append("]");
				ls3.add(s.toString());
			}else if(classesName.contains(fieldClazz.getName())){
				ls2.add("\""+field.getName()+"\":"+parseToJson(field.get(o)));
			}else{
				throw new JsonUnsupportClassException("json unsupport the class:"+fieldClazz);
			}
			
		}
		if(!ls.isEmpty()){
			sbJson.append(String.join(",", ls));
		}
		if(!ls.isEmpty()&&!ls3.isEmpty()){
			sbJson.append(",");
		}
		if(!ls3.isEmpty()){
			sbJson.append(String.join(",", ls3));
		}
		if((!ls.isEmpty()||!ls3.isEmpty())&&!ls2.isEmpty()){
			sbJson.append(",");
		}
		sbJson.append(String.join(",", ls2));
		
		

		return sbJson.append("}").toString();
	}
	
	
	
	private void parserAnnotation(String path) throws ClassNotFoundException{
		
		List<String> classList=new ArrayList<>();
		obtainClassNames("src/"+path.replace(".", "/"), classList);
		
		for(String s:classList){
			
			Class<?> clazz=Class.forName(s.replace("src/", ""));
			if(clazz.isAnnotationPresent(JsonBody.class)){
				classes.add(clazz);
				classesName.add(clazz.getName());
			}
		}
		
	}
	private void obtainClassNames(String path,List<String> classList){
		File root=new File(path);
		File[] files=root.listFiles();
		
		for(File file:files){
			if(file.isFile()){
				String s=(path+"/"+file.getName()).replace(".java","")
						.replace("src/", "")
						.replace("/", ".");
				
				if(s.startsWith(".")) s=s.substring(s.indexOf(".")+1);
				classList.add(s);
				
				
				
			}else{
				obtainClassNames(path+"/"+file.getName(), classList);
			}
		}
		
		
		
		
	}
	public static void main(String[] args) throws Exception {
		
		/**
		 * test
		 */
		JsonParser jp=new JsonParser();
		Birth birth=new Birth("1997","07","27");
		User user=new User("SrV-330",22,birth);
		user.setCmd(101);
		System.out.println(user);
		String s= jp.parseToJson(user);
		System.out.println(s);
		User u1=new User();
		Birth b1=new Birth();
		System.out.println(jp.parser(s,u1));
		System.out.println(u1.getCmd());
		
//		LoginInfo loginInfo=new LoginInfo();
//		
//		Class<?> clazz=loginInfo.getClass();
//		Method m=clazz.getMethod("setCmd",Integer.class );
//		m.invoke(loginInfo, 2);
//		System.out.println(loginInfo.getCmd());
//		String s="cmd";
//		s="set"+Character.toString(Character.toUpperCase(s.charAt(0)))+s.substring(1);
//		System.out.println(s);
		
		
		
		
		
		
	}
	

}
