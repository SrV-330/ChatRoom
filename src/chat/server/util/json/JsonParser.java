package chat.server.util.json;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import chat.server.util.json.exception.JsonParserException;

public class JsonParser<T> {
	protected static final HashMap<String,Class<?>> classMap=new HashMap<>();
	protected Queue<Character> jsonQueue=new LinkedList<>();
	protected Stack<Object> stack=new Stack<>();
	protected Object target;
	public JsonParser(){}
	public JsonParser(String jsonBody,Object target){
		for(int i=0;i<jsonBody.length();i++){
			jsonQueue.offer(jsonBody.charAt(i));
		}
		
		this.target=target;
	}
	
	public T parser() throws Exception{
		return (T)doParser();
		
		
	}
	private Object doParser() throws Exception{
		
		if(jsonQueue.isEmpty()) return null;
		int c=jsonQueue.peek();
		if(c!='{') throw new JsonParserException("json must start with symbol \'{\' ");
		
		
		return parserObject(target);
	}
	private Object parserObject(Object tar) throws Exception{
		int c=-1;
		
		while(!jsonQueue.isEmpty()&&(c=jsonQueue.poll())!='}'){
			if(c=='{'){
				
				
				Object o=null;
				if(!stack.isEmpty()&&(o=stack.peek())!=null){
					if(o instanceof String) {
						o=stack.pop();
						String objectName=(String)o;
						Class<?> clazz=tar.getClass();
						Field field=clazz.getDeclaredField(objectName);
						field.setAccessible(true);
						Object tarField=field.get(tar);
						clazz=tarField.getClass();
						tarField=clazz.newInstance();
						field.set(tar, parserObject(tarField));
						
						stack.push(tar);
						continue;
					}
					
				}
				stack.push(c);
				
			}else if(c=='['){
				stack.push(c);
				stack.push(parserArray(tar));
				
			}else if(c==':'){
				
				while((c=jsonQueue.peek())<=20){
					jsonQueue.poll();
				}
				
				if(c=='"'){
					stack.push(parserField(tar));
				}else if(c=='{'){
					stack.push(parserObject(tar));
				}else if(c=='['){
					stack.push(parserObject(tar));
				}else{
				
					throw new JsonParserException("there must be symbol \'{\',\'[\' or \'\"\' after \':\'");
				}
			}else if(c<=20){
				continue;
			}else if(c=='"'){
				stack.push(parserString());			
			}else if(c==','){
				while((c=jsonQueue.peek())<=20){
					jsonQueue.poll();
				}
			}else{
				throw new JsonParserException("can not parser char \'"+c+"\'");
			}
		}
		
		if(c=='}'){
			if(stack.size()>=2){
				return stack.pop();
			}else{
				return null;
			}
		}else{
			throw new JsonParserException("json must end with symbol \'}\' ");
		}
			
		
	}

	protected Object parserField(Object tar) throws Exception{
		Object o=stack.pop();
		String fieldName="";
		if(!(o instanceof String)) throw new JsonParserException("can not cast class "+o+" to String");
		
		fieldName=(String)o;
		Class<?> clazz= tar.getClass();
		
		Field field=clazz.getDeclaredField(fieldName);
		field.setAccessible(true);
		if(jsonQueue.poll()=='"')
		field.set(tar, parserString());
		return tar;
		
		
	}
	protected Object[] parserArray(Object tar) throws Exception {
		
		int c=-1;
		List<Object> l=new ArrayList<>();
		while((c=jsonQueue.poll())!=']'){
			l.add(parserObject(tar));
		}
				
		
		
		
		return l.toArray();
		
		
	}

	protected static Class<?> getJsonClass(String className){
		
		return classMap.get(className);
	}
	protected String parserString() {
		
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
	
	private StringBuilder sbJson=new StringBuilder("{");
	public String parseToJson(Object o) throws Exception{
		
		Class<?> clazz=o.getClass();
		Field[] fields=clazz.getDeclaredFields();
		
		for(Field field:fields){
			Class<?> fieldClazz=field.getType();
			if(fieldClazz==Integer.class||fieldClazz==Long.class||
					fieldClazz==Short.class||fieldClazz==Boolean.class||
					fieldClazz==Byte.class||fieldClazz==Double.class||
					fieldClazz==Float.class||fieldClazz==Character.class||
					fieldClazz==String.class){
				sbJson.append("\""+field.getName()+"\":\""+field.get(o)+"\",");
				
			}else if(fieldClazz==List.class){
				List<Object> l=(List<Object>)field.get(o);
				sbJson.append("[");
				for(Object o1:l){
					parseToJson(o1);
				}
				sbJson.append("]");
			}else if(fieldClazz==List.class){
				
			}
		}
		
		
		return sbJson.append("}").toString();
	}
	

}
