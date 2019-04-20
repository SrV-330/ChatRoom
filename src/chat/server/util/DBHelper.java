package chat.server.util;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import chat.server.entity.FriendInfo;
import chat.server.entity.UserGroupInfo;
import chat.server.entity.UserInfo;


public class DBHelper {
	

	public static UserInfo getUser(String userName){
		
		try (RandomAccessFile raf=new RandomAccessFile("user.dat", "rw")){
			int size=32*2;
			int offset=0;
			if(moveCursor(raf,userName, size, offset)){
				byte[] b=new byte[32];
				raf.read(b);
				userName=new String(b,"utf-8").trim();
				raf.read(b);
				String userPwd=new String(b,"utf-8").trim();
				UserInfo userInfo=new UserInfo(userName, userPwd);
				return userInfo;
				
			}
			return null;
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
		
	}
	
	public static boolean addUser(UserInfo userInfo){
		
		try(RandomAccessFile raf=new RandomAccessFile("user.dat", "rw");) {
			raf.seek(raf.length());
			byte[] bytes=Arrays.copyOf(userInfo.getUserName().getBytes("utf-8"), 32);
			raf.write(bytes);
			bytes=Arrays.copyOf(userInfo.getUserPwd().getBytes("utf-8"), 32);
			raf.write(bytes);
			
			
			return true;
		} catch (IOException e) {
			
			e.printStackTrace();
			return false;
		}
	}
	
	public static FriendInfo getFriend(FriendInfo friendInfo){
		final int size=32*3;
		final int nameOffset=0;
		final int friendOffset=32*2;
		try(RandomAccessFile raf=new RandomAccessFile("userfrined.dat","rw")){
			
			final FriendInfo f=new FriendInfo();
			moveCursorByKeys(raf, 
					new String[]{
							friendInfo.getUserName(),
							friendInfo.getFriendName()}, 
					size, 
					new Integer[]{
							nameOffset,
							friendOffset},
					new DBHandler() {
						
						@Override
						public void execute(RandomAccessFile raf, String[] keys, int size, Integer[] keyoffsets,int mod) throws IOException {
							byte[] b=new byte[32];
							raf.write(b);
							f.setUserName(new String(b,"utf-8").trim());
							raf.write(b);
							f.setGroupName(new String(b,"utf-8").trim());
							raf.write(b);
							f.setFriendName(new String(b,"utf-8").trim());
							raf.seek(raf.getFilePointer()-32*3);
							
							
						}
					},DBHandler.GET);
			if(f.getGroupName()==null||f.getGroupName().equals("")){
				return null;
			}
			return f;
			
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static List<UserGroupInfo> getGroups(String userName){
		List<UserGroupInfo> l=new ArrayList<>();
		try(RandomAccessFile raf=new RandomAccessFile("usergroup.dat","rw")){
			final int size=32*2;
			final int offset=0;
			final UserGroupInfo g=new UserGroupInfo();
			moveCursorByKeys(raf, 
					new String[]{
							userName
							},	
					size, 
					new Integer[]{
							offset,
							},
					new DBHandler() {
						
						@Override
						public void execute(RandomAccessFile raf, String[] keys, int size, Integer[] keyoffsets,int mod) throws IOException {
							byte[] b=new byte[32];
							raf.write(b);
							g.setUserName(new String(b,"utf-8").trim());
							raf.write(b);
							g.setGroupName(new String(b,"utf-8").trim());
							
							raf.seek(raf.getFilePointer()-32*2);
							l.add(g);
							
						}
					},DBHandler.GET);
			
			return l;
			
		}catch(Exception e){
			e.printStackTrace();
			return l;
		}
		
	}
	public static boolean deleteFriend(FriendInfo friendInfo){
		
		try(RandomAccessFile raf=new RandomAccessFile("userfriend.dat", "rw");) {
			final int size=32*3;
			final int offset=32;
			if(moveCursor(raf, friendInfo.getUserName()+friendInfo.getFriendName(),
					size, offset)){
				
				getEndLine(raf, size, raf.getFilePointer());
				overrideLine(raf,
						getEndLine(raf, size,raf.getFilePointer()),
						raf.getFilePointer());
				deleteEndLine(raf, size);
				return true;
			}
			
			
			return false;
		} catch (IOException e) {
			
			e.printStackTrace();
			return false;
		}
		
	}
	public static boolean deleteFriends(UserGroupInfo userGroupInfo){
		final int size=32*3;
		final int nameOffset=0;
		final int groupOffset=32;
		try(RandomAccessFile raf=new RandomAccessFile("userfrined.dat","rw")){
			moveCursorByKeys(raf, 
					new String[]{
							userGroupInfo.getUserName(),
							userGroupInfo.getGroupName()}, 
					size, 
					new Integer[]{
							nameOffset,
							groupOffset},
					new DBHandler() {
						
						@Override
						public void execute(RandomAccessFile raf, String[] keys, int size, Integer[] keyoffsets,int mod) throws IOException {
							overrideLine(raf,
									getEndLine(raf, size,raf.getFilePointer()),
									raf.getFilePointer());
							deleteEndLine(raf, size);
							
							
						}
					},DBHandler.DELETE);
			
			return true;
			
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
		
	}
	public static boolean addFriend(FriendInfo friendInfo){
		try(RandomAccessFile raf=new RandomAccessFile("userfrined.dat","rw")){
			
			raf.seek(raf.length());
			
			raf.write(Arrays.copyOf(friendInfo.getUserName().getBytes("utf-8"),32));
			raf.write(Arrays.copyOf(friendInfo.getGroupName().getBytes("utf-8"),32));
			raf.write(Arrays.copyOf(friendInfo.getFriendName().getBytes("utf-8"),32));
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public static List<FriendInfo> getFriendList(UserInfo userInfo){
		List<FriendInfo> l= new ArrayList<>();
		try(RandomAccessFile raf=new RandomAccessFile("userfriend.dat", "rw");) {
			final int size=32*3;
			final int nameOffset=0;
			
			moveCursorByKeys(raf, 
					new String[]{
							userInfo.getUserName(),
							},
					size, 
					new Integer[]{nameOffset,
							},
					new DBHandler() {
						
						@Override
						public void execute(RandomAccessFile raf, String[] keys, int size, Integer[] keyoffsets,int mod) throws IOException {
							byte[] b=new byte[32];
							raf.read(b);
							String userName=new String(b,"utf-8").trim();
							raf.read(b);
							String groupName=new String(b,"utf-8").trim();
							raf.read(b);
							String friendName=new String(b,"utf-8").trim();
							l.add(new FriendInfo(userName, friendName, groupName));
							raf.seek(raf.getFilePointer()-32*3);
						}
					},DBHandler.GET);
			
			return l;
		}catch(Exception e){
			e.printStackTrace();
			return l;
		}

		
	}
	public static boolean addUserGroup(UserGroupInfo userGroupInfo){
		
		try(RandomAccessFile raf=new RandomAccessFile("usergroup.dat", "rw");) {
			raf.seek(raf.length());
			byte[] bytes=Arrays.copyOf(userGroupInfo.getUserName().getBytes("utf-8"), 32);
			raf.write(bytes);
			
			bytes=Arrays.copyOf(userGroupInfo.getGroupName().getBytes("utf-8"), 32);
			raf.write(bytes);
			return true;
		} catch (IOException e) {
			
			e.printStackTrace();
			return false;
		}
		
	}
	public static boolean deleteUserGroup(UserGroupInfo userGroupInfo){
		
		try(RandomAccessFile raf=new RandomAccessFile("usergroup.dat", "rw");) {
			final int size=32*3;
			final int nameOffset=0;
			final int groupOffset=32;
			moveCursorByKeys(raf, 
					new String[]{
							userGroupInfo.getUserName(),
							userGroupInfo.getGroupName()},size, 
					new Integer[]{nameOffset,
							groupOffset},
					new DBHandler() {
						
						@Override
						public void execute(RandomAccessFile raf, String[] keys, int size, Integer[] keyoffsets,int mod) throws IOException {
							
							overrideLine(raf,
									getEndLine(raf, size,raf.getFilePointer()),
									raf.getFilePointer());
							deleteEndLine(raf, size);
						}
					},DBHandler.DELETE);
			
				
				
			
			
			
			return true;
			
			
		} catch (IOException e) {
			
			e.printStackTrace();
			return false;
		}
		
	}
	
	public static UserGroupInfo getGroup(String groupName){
		
		try(RandomAccessFile raf=new RandomAccessFile("usergroup.dat","rw");){
			final int size=32*2;
			final int offset=32;
			byte[] b=new byte[32];
			if(moveCursor(raf, groupName, size, offset)){
				raf.read(b);
				String userName=new String(b,"utf-8").trim();
				raf.read(b);
				groupName=new String(b,"utf-8").trim();
				return new UserGroupInfo(userName, groupName);
				
			}
			return null;
			
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	}
	public static boolean moveToGroup(UserGroupInfo src,UserGroupInfo tag) throws IOException{
		
		try(RandomAccessFile raf=new RandomAccessFile("userfriend.dat","rw");){
			 final int size=32*3;
			 final int nameOffset=0;
			 final int groupOffset=32*2;
			 
			moveCursorByKeys(raf, new String[]{
					src.getUserName(),
					src.getGroupName()
				}, 
				size, 
				new Integer[]{
						nameOffset,
						groupOffset
				}, new DBHandler() {
					
					@Override
					public void execute(RandomAccessFile raf, String[] keys, int size, Integer[] keyoffsets,int mod) throws IOException {
						
						raf.seek(raf.getFilePointer()+groupOffset);
						raf.write(Arrays.copyOf(tag.getGroupName().getBytes("utf-8"), 32));
						raf.seek(raf.getFilePointer()-groupOffset-32);
					}
				},DBHandler.MODIFY);
				
			
			return false;
		}catch(IOException e){
			e.printStackTrace();
			return false;
		}
		
		
	}
	public static boolean modifyGroupName(UserGroupInfo src,UserGroupInfo tag){
		try(RandomAccessFile raf=new RandomAccessFile("userfriend.dat","rw");){
			 final int size=32*3;
			 final int nameOffset=0;
			 final int groupOffset=32*2;
			 
			moveCursorByKeys(raf, new String[]{
					src.getUserName(),
					src.getGroupName()
				}, 
				size, 
				new Integer[]{
						nameOffset,
						groupOffset
				}, new DBHandler() {
					
					@Override
					public void execute(RandomAccessFile raf, String[] keys, int size, Integer[] keyoffsets,int mod) throws IOException {
						
						raf.seek(raf.getFilePointer()+groupOffset);
						raf.write(Arrays.copyOf(tag.getGroupName().getBytes("utf-8"), 32));
						raf.seek(raf.getFilePointer()-groupOffset-32);
					}
				},DBHandler.MODIFY);
				
			
			return false;
		}catch(IOException e){
			e.printStackTrace();
			return false;
		}
		
		
		
	}
	
	private static boolean moveCursor(RandomAccessFile raf,String key,
			int size,int keyoffset) throws IOException{
		if(size<0||keyoffset<0) throw new IOException("size or keyoffset must not be less than 0");
		if(raf.length()<(raf.getFilePointer()+keyoffset)) return false;
		raf.seek(raf.getFilePointer()+keyoffset);
		
		byte[] bytes=new byte[32];
		while((raf.read(bytes))!=-1){
			String s=new String(bytes,0,32,"utf-8").trim();
			if(s.equals(key)){
				raf.seek(raf.getFilePointer()-keyoffset-32);
				return true;
			}
			if(raf.getFilePointer()+size-32-keyoffset>=raf.length()) return false;
			raf.seek(raf.getFilePointer()+size-32-keyoffset);
		}
		
		return false;
	}
	
	private static void moveCursorByKeys(RandomAccessFile raf,String[] keys,
			int size,Integer[] keyoffsets,DBHandler handler,int mod) throws IOException{
		
		for(int i=0;i<keys.length;i++){
			Integer keyoffset=keyoffsets[i];
			
			if(raf.length()<=(raf.getFilePointer()+keyoffset)) return;
			
			
		}
		
		
		
		
		for(long pos=0;pos<raf.length();){
			
			if(compareLine(raf, keys, size, keyoffsets)){
				
				handler.execute(raf, keys, size, keyoffsets,mod);
				if(mod==DBHandler.DELETE) continue;
				else ;
				
			}
			pos=(raf.getFilePointer()+size);
			if(pos>=raf.length()){
				break;
			}else{
				raf.seek(pos);
			}
			
		}
		
		
	}
	private static boolean compareLine(RandomAccessFile raf,String[] keys,
			int size,Integer[] keyoffsets) throws IOException{
		byte[] b=new byte[32];
		int keyoffset=0;
		String key="";
		for(int i=0;i<keys.length;i++){
			keyoffset=keyoffsets[i];
			key=keys[i];
			
			raf.seek(raf.getFilePointer()+keyoffset);
			raf.read(b);
			String s=new String(b,"utf-8").trim();
			if(!s.equals(key)){

				raf.seek(raf.getFilePointer()-keyoffset-32);
				
				return false;
			}
			raf.seek(raf.getFilePointer()-keyoffset-32);
			
		}
		
		
		
		return true;
	}
	private static void overrideLine(RandomAccessFile raf,byte[] data,long cursor) throws IOException{
		if(data==null) return;
		raf.write(data);
		raf.seek(cursor);
		
	}
	private static byte[] getEndLine(RandomAccessFile raf,int size,long cursor) throws IOException{
		if(raf.length()-size<0) return null;
		raf.seek(raf.length()-size);
		byte[] bytes=new byte[size];
		raf.read(bytes);
		raf.seek(cursor);
		return bytes;
		
	}
	private static void deleteEndLine(RandomAccessFile raf,int size) throws IOException{
		if(raf.length()-size<0) return;
		raf.setLength(raf.length()-size);
		
		
	}
	
	
	private  interface DBHandler{
		final int DELETE=10004;
		final int MODIFY=10003;
		final int GET=10002;
		
		void execute(RandomAccessFile raf,String[] keys,int size,Integer[] keyoffsets,int mod) throws IOException;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	

}
