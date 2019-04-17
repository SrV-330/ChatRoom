package chat.server.util;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import chat.server.entity.FriendInfo;
import chat.server.entity.UserGroupInfo;
import chat.server.entity.UserInfo;
import chat.server.util.json.JsonParser;
import chat.server.util.json.annotation.JsonBody;

public class DBHelper {
	
	private static HashMap<String,UserInfo> users=new HashMap<>();
	private static HashMap<String,UserGroupInfo> userGroups=new HashMap<>();
	private static HashMap<String, FriendInfo> friendList=new HashMap<>();
	
	
	
	public static void loadUsers(){
		
		try(RandomAccessFile raf=new RandomAccessFile("user.dat", "rw");) {
			byte[] buf=new byte[32+32];
			while((raf.read(buf, 0,buf.length))!=-1){
				String username=new String(buf,0,32,"utf-8").trim();
				String userpwd=new String(buf,32,buf.length,"utf-8").trim();
				users.put(username,new UserInfo(username,userpwd));
				
			}
			
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	
	public static UserInfo getUser(String userName){
		return users.get(userName);
	}
	public static void loadUserGroups(){
		try(RandomAccessFile raf=new RandomAccessFile("usergroup.dat", "rw");) {
			byte[] buf=new byte[32+32+32];
			while((raf.read(buf, 0,buf.length))!=-1){
				String username=new String(buf,0,32,"utf-8").trim();
				String friendname=new String(buf,32,32+32,"utf-8").trim();
				String groupname=new String(buf,32+32,buf.length,"utf-8").trim();
				userGroups.put(username+friendname+groupname, 
						new UserGroupInfo(username, friendname, groupname));
				
			}
			
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		
	}
	public static UserGroupInfo getUserGroup(String userName){
		return userGroups.get(userName);
	}
	public static List<UserGroupInfo> getUserGroupByFriendName(String friendName){
		
		List<UserGroupInfo> l=new ArrayList<>();
		for(Entry<String,UserGroupInfo> entry:userGroups.entrySet()){
			UserGroupInfo g=entry.getValue();
			if(g.getFriendName().equals(friendName)){
				l.add(g);
			}
		}
		return l;
	}
	
	public static void loadFriendList(){
		try(RandomAccessFile raf=new RandomAccessFile("friend.dat", "rw");) {
			byte[] buf=new byte[32+32];
			while((raf.read(buf, 0,buf.length))!=-1){
				String username=new String(buf,0,32,"utf-8").trim();
				String friendname=new String(buf,32,buf.length,"utf-8").trim();
				
				friendList.put(username+friendname, new FriendInfo(username, friendname));
				
			}
			
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	public static List<UserGroupInfo> getFriendList(String userName){
		List<UserGroupInfo> l=new ArrayList<>();
		for(Entry<String,FriendInfo> entry:friendList.entrySet()){
			if(entry.getValue().getUserName().equals(userName)){
				return getUserGroupByFriendName(entry.getValue().getFriendName());
			}
		}
		
		return l;
		
	}
	
	public static boolean addUser(UserInfo userInfo){
		
		try(RandomAccessFile raf=new RandomAccessFile("user.dat", "rw");) {
			raf.seek(raf.length());
			byte[] bytes=Arrays.copyOf(userInfo.getUserName().getBytes("utf-8"), 32);
			raf.write(bytes);
			bytes=Arrays.copyOf(userInfo.getUserPwd().getBytes("utf-8"), 32);
			raf.write(bytes);
			
			
			return users.put(userInfo.getUserName(), userInfo)==null;
		} catch (IOException e) {
			
			e.printStackTrace();
			return false;
		}
		
	}
	public static List<UserGroupInfo> getGroupList(String userName){
		
		List<UserGroupInfo> l=new ArrayList<>();
		for(Entry<String,UserGroupInfo> entry:userGroups.entrySet()){
			if(entry.getValue().getUserName().equals(userName)){
				l.add(entry.getValue());
			}
		}
		return l;
	}
	public static boolean deleteFriend(FriendInfo friendInfo){
		
		try(RandomAccessFile raf=new RandomAccessFile("userfriend.dat", "rw");) {
			if(moveCursor(raf, friendInfo.getUserName()+friendInfo.getFriendName(),
					32+32, 32)){
				
				getEndLine(raf, 32+32, raf.getFilePointer());
				overrideLine(raf,
						getEndLine(raf, 32+32,raf.getFilePointer()),
						raf.getFilePointer());
				deleteEndLine(raf, 32+32);
				return friendList.remove(friendInfo.getUserName()+friendInfo.getFriendName())!=null;
			}
			
			
			return false;
		} catch (IOException e) {
			
			e.printStackTrace();
			return false;
		}
		
	}
	public static boolean addUserGroup(UserGroupInfo userGroupInfo){
		
		try(RandomAccessFile raf=new RandomAccessFile("usergroup.dat", "rw");) {
			raf.seek(raf.length());
			byte[] bytes=Arrays.copyOf(userGroupInfo.getUserName().getBytes("utf-8"), 32);
			raf.write(bytes);
			bytes=Arrays.copyOf(userGroupInfo.getFriendName().getBytes("utf-8"), 32);
			raf.write(bytes);
			bytes=Arrays.copyOf(userGroupInfo.getGroupName().getBytes("utf-8"), 32);
			raf.write(bytes);
			
			
			return userGroups.put(userGroupInfo.getUserName()+
					userGroupInfo.getFriendName()+
					userGroupInfo.getGroupName(),
					userGroupInfo)==null;
		} catch (IOException e) {
			
			e.printStackTrace();
			return false;
		}
		
	}
	public static boolean deleteUserGroup(UserGroupInfo userGroupInfo){
		
		try(RandomAccessFile raf=new RandomAccessFile("usergroup.dat", "rw");) {
			if(moveCursor(raf, userGroupInfo.getGroupName(), 32+32+32, 32+32)){
				List<Boolean> bools=new ArrayList<>();
				
				overrideLine(raf,
						getEndLine(raf, 32+32+32,raf.getFilePointer()),
						raf.getFilePointer());
				deleteEndLine(raf, 32+32+32);
				
				bools.add(deleteFriend(new FriendInfo(userGroupInfo.getUserName(),
						userGroupInfo.getFriendName())));
				
				
				return userGroups.remove(userGroupInfo.getUserName()+
						userGroupInfo.getFriendName()+
						userGroupInfo.getGroupName())!=null&&!bools.contains(false);
			}
			return false;
		} catch (IOException e) {
			
			e.printStackTrace();
			return false;
		}
		
	}
	public static boolean moveToGroup(UserGroupInfo src,UserGroupInfo tag) throws IOException{
		
		try(RandomAccessFile raf=new RandomAccessFile("usergroup.dat","rw");
			RandomAccessFile rafr=new RandomAccessFile("usergroup.dat","r");){
			if(moveCursor(raf, src.getGroupName(), 32+32+32, 32+32)&&
				moveCursor(rafr, tag.getGroupName(), 32+32+32, 32+32)){
				byte[] bytes=new byte[32];
				raf.seek(raf.getFilePointer()+32+32);
				rafr.seek(raf.getFilePointer()+32+32);
				rafr.read(bytes);
				raf.write(bytes);
				userGroups.get(src.getUserName()+
						src.getFriendName()+
						src.getGroupName()).setGroupName(tag.getGroupName());
				return true;
			}
			
			return false;
		}catch(IOException e){
			e.printStackTrace();
			return false;
		}
		
		
	}
	public static boolean modifyGroupName(UserGroupInfo src,UserGroupInfo tag){
		try(RandomAccessFile raf=new RandomAccessFile("usergroup.dat","rw");
			RandomAccessFile rafr=new RandomAccessFile("usergroup.dat","r");){
			if(moveCursor(raf, src.getGroupName(), 32+32+32, 32+32)&&
				!moveCursor(rafr, tag.getGroupName(), 32+32+32, 32+32)){
				raf.seek(raf.getFilePointer()+32+32);
				raf.write(Arrays.copyOf(tag.getGroupName().getBytes("utf-8"),32));
				userGroups.get(src.getUserName()+
						src.getFriendName()+
						src.getGroupName()).setGroupName(tag.getGroupName());
				return true;
			}
				
			return false;
		}catch(IOException e){
			e.printStackTrace();
			return false;
		}
		
		
		
	}
	
	public static boolean moveCursor(RandomAccessFile raf,String key,
			int size,int keyoffset) throws IOException{
		if(size<0||keyoffset<0) throw new IOException("size or keyoffset must not be less than 0");
		if(raf.length()>(size+keyoffset)) return false;
		raf.seek(keyoffset);
		
		byte[] bytes=new byte[32];
		while((raf.read(bytes))!=-1){
			String s=new String(bytes,0,32,"utf-8").trim();
			if(s.equals(key)){
				raf.seek(raf.getFilePointer()-32);
				return true;
			}
			raf.seek(raf.getFilePointer()+size-32);
		}
		
		return false;
	}
	public static void overrideLine(RandomAccessFile raf,byte[] data,long cursor) throws IOException{
		if(data==null) return;
		raf.write(data);
		raf.seek(cursor);
		
	}
	public static byte[] getEndLine(RandomAccessFile raf,int size,long cursor) throws IOException{
		if(raf.length()-size<0) return null;
		raf.seek(raf.length()-size);
		byte[] bytes=new byte[size];
		raf.read(bytes);
		raf.seek(cursor);
		return bytes;
		
	}
	public static void deleteEndLine(RandomAccessFile raf,int size) throws IOException{
		if(raf.length()-size<0) return;
		raf.setLength(raf.length()-size);
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
