package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SendMyFiles {
	

 public void send(File filename){
		  
	 JSch jsch = new JSch();
     Session session = null;
     try {
    	 String namefull=filename.getAbsolutePath();
    	 System.out.println("namefull "+namefull);
    	 String nameshort=filename.getName();
    	 System.out.println("nameshort =  "+nameshort);
         session = jsch.getSession("lvuser", "10.14.93.2", 22);

         session.setConfig("StrictHostKeyChecking", "no");
         session.setPassword("");
         session.connect();
         
         Channel channel = session.openChannel("sftp");
         channel.connect();
         ChannelSftp sftpChannel = (ChannelSftp) channel;
//  source file, destination file         
         sftpChannel.put(namefull, nameshort);
         sftpChannel.exit();
         session.disconnect();
     } catch (JSchException e) {
         e.printStackTrace();  
     } catch (SftpException e) {
         e.printStackTrace();
     }
	  
	    
	 }
	     
	     
	


}
