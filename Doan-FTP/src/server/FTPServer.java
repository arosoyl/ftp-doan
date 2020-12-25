package server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import lib.FileDetails;
import lib.LibraryConnectDb;

public class FTPServer extends Thread
{
	private LibraryConnectDb connectDb;
	private java.sql.Connection conn;
	private Statement st;
	private PreparedStatement pst;
	private ResultSet rs;
	Socket ClientSoc;
    DataInputStream din;
    DataOutputStream dout;  
    private boolean logined;
    private  byte data[];
    public FTPServer(Socket soc)
    {
        try
        {
            ClientSoc=soc;                        
            din=new DataInputStream(ClientSoc.getInputStream());
            dout=new DataOutputStream(ClientSoc.getOutputStream());
            System.out.println("FTP Client Connected ...");
            connectDb = new LibraryConnectDb();
            start();
            
        }
        catch(Exception ex)
        {
        }        
    }
    void SendFile() throws Exception
    {        
        String filename=din.readUTF();
        File f=new File(filename);
        if(!f.exists())
        {
            dout.writeUTF("File Not Found");
            return;
        }
        else
        {
            dout.writeUTF("READY");
            FileInputStream fin=new FileInputStream(f);
            int ch;
            do
            {
                ch=fin.read();
                dout.writeUTF(String.valueOf(ch));
            }
            while(ch!=-1);    
            fin.close();    
            dout.writeUTF("File Receive Successfully");                            
        }
    }
    
   

   

    
    public void run()
    {
    	String sReq="";
    	try {
    		sReq = din.readUTF();
			System.out.println(sReq);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	String[] words =sReq.split("\\&");
    	String loginedCmd= words[0];
    	String resquestCmd = words[1];
    	String fileResquest = words[2];
    	
    	if(!loginedCmd.equals("logined")){
			try {
				logined=checkLogin(sReq);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}else{
    		switch (resquestCmd) {
			case "download":
				downloadFile(fileResquest);
				break;
			case "upload":
				String pathDirSave = words[3];
				uploadFile(fileResquest,pathDirSave);
				break;
			case "dellocal":
				delFileLocal(loginedCmd,fileResquest);
				break;
			case "delremote":
				delFileRemote(loginedCmd,fileResquest);
				break;

			default:
				break;
			}
    			
    	}
    }
	
	private void downloadFile(String fileResquest) {
		// TODO Auto-generated method stub
		FileDetails details;
	    
	    try {
	        File file = new File(fileResquest);

	        // Getting file name and size
	        if (file.length() > Integer.MAX_VALUE) {
	            System.out.println("File size exceeds 2 GB");
	        } else {
	         
	            data = new byte[2048]; // Here you can increase the size also which will send it faster
	            details = new FileDetails();
	            details.setDetails(file.getName(), file.length());

	            // Sending file details to the client
	            System.out.println("Sending file details...");
	            ObjectOutputStream sendDetails = new ObjectOutputStream(ClientSoc.getOutputStream());
	            sendDetails.writeObject(details);
	            sendDetails.flush();
	            // Sending File Data 
	            System.out.println("Sending file data...");
	            FileInputStream fileStream = new FileInputStream(file);
	            BufferedInputStream fileBuffer = new BufferedInputStream(fileStream);
	            OutputStream out = ClientSoc.getOutputStream();
	            int count;
	            while ((count = fileBuffer.read(data)) > 0) {
	                System.out.println("Data Sent : " + count);
	                out.write(data, 0, count);
	                out.flush();
	            }
	            out.close();
	            fileBuffer.close();
	            fileStream.close();
	        }
	    } catch (Exception e) {
	        System.out.println("Error : " + e.toString());
	    }
		
	}
	private boolean checkLogin(String sReq) throws SQLException {
		// TODO Auto-generated method stub
		String[] words =sReq.split("\\&");
		String username = words[1];
		String password = words[2];
		System.out.println(username+" va "+password);
		boolean check = false;
		//String sql ="SELECT * FROM users WHERE username = ? AND password = ? ";
		String sql ="SELECT * FROM users WHERE username = '"+username+"' AND password = '"+password+"'";
		conn = connectDb.getConnectMySql();
		System.out.println(sql);
		try {
			/*pst = conn.prepareStatement(sql);
			pst.setString(1, username);
			pst.setString(2, password);
			result = pst.executeQuery();*/
			
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			
			if(rs.next()){
				dout.writeUTF("login_succeed,"+rs.getString("path"));
				System.out.println(rs.getString("path")+" o dia");
				//dout.writeUTF(rs.getString("path"));
				check = true;
			}else {
				dout.writeUTF("login_fail");
				check = false;
			}
			dout.flush();
		} catch (SQLException exception) {
			exception.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try{
				st.close();
				conn.close();
			}catch(SQLException exception2){
				exception2.printStackTrace();
			}
		}
		return check;
	}
	public void uploadFile(String fileResquest, String pathDirSave){
		
		try {
			String pathSelected = pathDirSave;
			//dout.writeUTF("logined&download&"+pathSelected);
			System.out.println("path save "+pathDirSave);
			//dout.flush();

	        System.out.println("Getting details from Server...");
	        ObjectInputStream getDetails = new ObjectInputStream(ClientSoc.getInputStream());
	        FileDetails details = (FileDetails) getDetails.readObject();
	        System.out.println("Now receiving file...");
	        // Storing file name and sizes

	        String fileName = details.getName();
	        System.out.println("File Name : " + fileName);
	        data = new byte[2048]; // Here you can increase the size also which will receive it faster
	        FileOutputStream fileOut = new FileOutputStream(pathSelected+"\\" + fileName);
	        InputStream fileIn = ClientSoc.getInputStream();
	        BufferedOutputStream fileBuffer = new BufferedOutputStream(fileOut);
	        int count;
	        long sum = 0;
	        int i=0;
	        while ((count = fileIn.read(data)) > 0) {
	            sum += count;
	            fileBuffer.write(data, 0, count);
	            System.out.println("Data received : " + sum);
	            fileBuffer.flush();
	            System.out.println(i++);
	        }
	        System.out.println("File Received...");
	        fileBuffer.close();
	        fileIn.close();
	        getDetails.close();
			//din.close();
	        ClientSoc.close();
	    } catch (Exception e) {
	        System.out.println("Error : " + e.toString());
	    }
		
	}
	private void delFileLocal(String loginedCmd,String fileResquest) {
		// TODO Auto-generated method stub
		try {
			if(loginedCmd.equals("logined")){
				dout.writeUTF("accept&"+fileResquest);
			}else{
				dout.writeUTF("deny&"+fileResquest);
			}
			
			dout.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private void delFileRemote(String loginedCmd,String fileResquest) {
		// TODO Auto-generated method stub
		try {
			if(loginedCmd.equals("logined")){
				delete(new File(fileResquest));
				dout.writeUTF("done&"+fileResquest);
			}else{
				dout.writeUTF("deny&"+fileResquest);
			}
			
			dout.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private void delete(File file)
	    	throws IOException{
	 
	    	if(file.isDirectory()){
	 
	    		//directory is empty, then delete it
	    		if(file.list().length==0){
	    			
	    		   file.delete();
	    		   System.out.println("Directory is deleted : " 
	                                                 + file.getAbsolutePath());
	    		}else{
	    			
	    		   //list all the directory contents
	        	   String files[] = file.list();
	     
	        	   for (String temp : files) {
	        	      //construct the file structure
	        	      File fileDelete = new File(file, temp);
	        		 
	        	      //recursive delete
	        	     delete(fileDelete);
	        	   }
	        		
	        	   //check the directory again, if empty then delete it
	        	   if(file.list().length==0){
	           	     file.delete();
	        	     System.out.println("Directory is deleted : " 
	                                                  + file.getAbsolutePath());
	        	   }
	    		}
	    		
	    	}else{
	    		//if file, then delete it
	    		file.delete();
	    		System.out.println("File is deleted : " + file.getAbsolutePath());
	    	}
	    }
}