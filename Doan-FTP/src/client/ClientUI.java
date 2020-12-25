package client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import java.nio.file.Files;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.plaf.ProgressBarUI;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import lib.FileDetails;
import javax.swing.JProgressBar;

public class ClientUI extends JFrame implements ActionListener{
	private static DataInputStream din;
	private static DataOutputStream dout;
	private JPanel contentPane;
	private JTextField txtUser;
	private JPasswordField passwordField;
	private DefaultMutableTreeNode root, root_1;
	

    private DefaultTreeModel treeModel,treeModel_1;
    private File fileRoot, fileRoot_1;
    private JTree tree, tree_1;
    private JScrollPane scrollPane =  new JScrollPane(null);
    static ClientUI frame;
    private JButton btnBrowse,btnDown,btnUp,btnngK,btnNewButton,btnXa,btnDeleteInRemote; 
    private JTextField textField_2;
    private JScrollBar jScrollBar;
    private static Socket socfd;
    private JTextField textField_3;
    private static FTPClient fc;
    private boolean logined;
    private JTextField textField_4;
    private JTextField textField_5;
    private JScrollPane scrollPane_1;
    private JLabel lblLocalSite;
    private String pathLocal ="";
    private String host ="";
    private String pathLocalParent="";
    private String pathRemoteParent="";
    private String pathRootRemote;
    private String strMessage="";
    private  byte data[];
    private JTextPane textPane;
    private JButton btnngXut;
    private int valueProgress;
    private DefaultMutableTreeNode node, node2;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new ClientUI();
					Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
					frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

	/**
	 * Create the frame.
	 */
	public ClientUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1166, 740);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(127, 62, 751, 17);
		separator.setBackground(Color.LIGHT_GRAY);
		contentPane.add(separator);
		
		JLabel lblHost = new JLabel("Host");
        lblHost.setFont(new Font("Tahoma", Font.ITALIC, 15));
        lblHost.setBounds(10, 11, 51, 30);
        contentPane.add(lblHost);
        
        textField_3 = new JTextField();
        textField_3.setText("192.168.1.16");
        textField_3.setFont(new Font("Tahoma", Font.PLAIN, 15));
        textField_3.setColumns(10);
        textField_3.setBounds(71, 11, 116, 30);
        contentPane.add(textField_3);
        
		JLabel lblNewLabel = new JLabel("Username");
		lblNewLabel.setBounds(208, 11, 70, 30);
		lblNewLabel.setFont(new Font("Tahoma", Font.ITALIC, 15));
		contentPane.add(lblNewLabel);
		
		txtUser = new JTextField();
		txtUser.setText("user1");
		txtUser.setBounds(288, 11, 116, 30);
		txtUser.setFont(new Font("Tahoma", Font.PLAIN, 15));
		contentPane.add(txtUser);
		txtUser.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Password");
		lblNewLabel_1.setBounds(424, 20, 67, 14);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.ITALIC, 15));
		contentPane.add(lblNewLabel_1);
		
		btnNewButton = new JButton("Đăng Nhập");
		btnNewButton.addActionListener(this);
		btnNewButton.setActionCommand("btnNewButton");
		btnNewButton.setBounds(799, 11, 116, 31);
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 15));
		contentPane.add(btnNewButton);
		
		btnngK = new JButton("Đăng Ký");
		btnngK.setBounds(1033, 11, 107, 31);
		btnngK.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnngK.addActionListener(this);
		btnngK.setActionCommand("btnngK");
		contentPane.add(btnngK);
		
		passwordField = new JPasswordField();
		passwordField.setToolTipText("123");
		passwordField.setBounds(501, 11, 116, 30);
		passwordField.setFont(new Font("Tahoma", Font.PLAIN, 15));
		contentPane.add(passwordField);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(127, 234, 751, 8);
		separator_1.setBackground(Color.LIGHT_GRAY);
		contentPane.add(separator_1);
		
		btnBrowse = new JButton("Duyệt");
		btnBrowse.setFont(new Font("Tahoma", Font.ITALIC, 14));
        btnBrowse.addActionListener(this);
        btnBrowse.setActionCommand("btnBrowse");
        btnBrowse.setBounds(478, 268, 194, 48);
        contentPane.add(btnBrowse);
        
        btnDown = new JButton("Download");
        btnDown.setFont(new Font("Tahoma", Font.ITALIC, 14));
        btnDown.addActionListener(this);
        btnDown.setActionCommand("btnDown");
        btnDown.setBounds(478, 358, 194, 48);
        contentPane.add(btnDown);
        
        btnUp = new JButton("Upload");
        btnUp.setFont(new Font("Tahoma", Font.ITALIC, 14));
        btnUp.addActionListener(this);
        btnUp.setActionCommand("btnUp");
        btnUp.setBounds(478, 450, 194, 48);
        contentPane.add(btnUp);
        
        btnXa = new JButton("Delete in Local Site");
        btnXa.setFont(new Font("Tahoma", Font.ITALIC, 14));
        btnXa.addActionListener(this);
        btnXa.setActionCommand("btnXa");
        btnXa.setBounds(478, 538, 194, 48);
        contentPane.add(btnXa);
        
        btnDeleteInRemote = new JButton("Delete in Remote Site");
        btnDeleteInRemote.setFont(new Font("Tahoma", Font.ITALIC, 14));
        btnDeleteInRemote.addActionListener(this);
        btnDeleteInRemote.setActionCommand("btnDeleteInRemote");
        btnDeleteInRemote.setBounds(478, 623, 194, 48);
        contentPane.add(btnDeleteInRemote);
        
        JLabel lblPort = new JLabel("Cổng");
        lblPort.setFont(new Font("Tahoma", Font.ITALIC, 15));
        lblPort.setBounds(642, 17, 67, 19);
        contentPane.add(lblPort);
        
        textField_2 = new JTextField();
        textField_2.setText("21");
        textField_2.setFont(new Font("Tahoma", Font.PLAIN, 15));
        textField_2.setColumns(10);
        textField_2.setBounds(694, 11, 80, 30);
        contentPane.add(textField_2);
        
        JLabel lblNewLabel_2 = new JLabel("Remote site");
        lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblNewLabel_2.setBounds(689, 249, 85, 14);
        contentPane.add(lblNewLabel_2);
        
        textField_4 = new JTextField();
        textField_4.setBounds(764, 247, 376, 20);
        contentPane.add(textField_4);
        textField_4.setColumns(10);
        
        textField_5 = new JTextField();
        textField_5.setColumns(10);
        textField_5.setBounds(70, 247, 390, 20);
        contentPane.add(textField_5);
        
        lblLocalSite = new JLabel("Local site");
        lblLocalSite.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblLocalSite.setBounds(10, 249, 67, 14);
        contentPane.add(lblLocalSite);
        
        JScrollPane scrollPane_2 = new JScrollPane();
        scrollPane_2.setBounds(10, 77, 1130, 146);
        contentPane.add(scrollPane_2);
        
        textPane = new JTextPane();
        textPane.setEditable(false);
        scrollPane_2.setViewportView(textPane);
        
        btnngXut = new JButton("Đăng Xuất");
        btnngXut.setFont(new Font("Tahoma", Font.BOLD, 15));
        btnngXut.addActionListener(this);
        btnngXut.setActionCommand("btnngXut");
        btnngXut.setBounds(916, 11, 116, 31);
        contentPane.add(btnngXut);
        
        
	}
	public void addTree(File fileRoot){
		scrollPane.setVisible(false);
		// returns pathnames for files and directory
		String fileRootParent = fileRoot.getParent();
		fileRoot = new File(fileRoot.getPath());
        root = new DefaultMutableTreeNode(new FileNode(fileRoot));
        treeModel = new DefaultTreeModel(root);

        tree = new JTree(treeModel);
        tree.setBounds(10, 252, 343, 436);
        tree.setShowsRootHandles(true);
        tree.addTreeExpansionListener(new TreeExpansionListener() {
			
			@Override
			public void treeExpanded(TreeExpansionEvent event) {
				// TODO Auto-generated method stub
				TreePath path = event.getPath();
		        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
		        String data = node.getUserObject().toString();
		        System.out.println("Expanded: " + data);
		        //pathLocal+="//"+data;
			}
			
			@Override
			public void treeCollapsed(TreeExpansionEvent event) {
				// TODO Auto-generated method stub
				TreePath path = event.getPath();
		        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
		        String data = node.getUserObject().toString();
		        System.out.println("Collapsed: " + data);
			}
		});
        tree.addTreeSelectionListener(new TreeSelectionListener() {
        	public void valueChanged(TreeSelectionEvent e) {
                node = (DefaultMutableTreeNode) e
                    .getPath().getLastPathComponent();
                
                String jTreeVarSelectedPath = "";
                String pathsSystem = e.getPath().toString();
               // String b =a.substring(1, a.length()-1);
                String[] pathsLocal = pathsSystem.substring(1, pathsSystem.length()-1).split(",");
                for (int i=0;i<pathsLocal.length;i++) {
                	pathsLocal[i]=pathsLocal[i].trim();
                	if(i!=pathsLocal.length-1){
                		
						jTreeVarSelectedPath+=pathsLocal[i]+"\\";
						jTreeVarSelectedPath+="\\";
                	}else{
                		jTreeVarSelectedPath+=pathsLocal[i];
						
                	}
				}
                if(pathLocalParent!=null){
              	  
                	textField_5.setText(pathLocalParent+jTreeVarSelectedPath);
                }else{
              	  jTreeVarSelectedPath=jTreeVarSelectedPath.substring(0,2)+jTreeVarSelectedPath.substring(3,jTreeVarSelectedPath.length());
              	  textField_5.setText(jTreeVarSelectedPath);
                }
              }
          });
       
        scrollPane = new JScrollPane(tree);
        scrollPane.setBounds(10, 278, 451, 410);
        contentPane.add(scrollPane);
        
        
        CreateChildNodes ccn = 
                new CreateChildNodes(fileRoot, root);
        new Thread(ccn).start();
        
        contentPane.revalidate();
        //contentPane.add(tree);
	}
	public void addTreeRemote() {
		fileRoot_1 = new File(pathRootRemote);
        root_1 = new DefaultMutableTreeNode(new FileNode(fileRoot_1));
        treeModel_1 = new DefaultTreeModel(root_1);

        tree_1 = new JTree(treeModel_1);
        tree_1.setBounds(10, 252, 343, 436);
        tree_1.setShowsRootHandles(true);
        tree_1.addTreeExpansionListener(new TreeExpansionListener() {
			
			@Override
			public void treeExpanded(TreeExpansionEvent event) {
				// TODO Auto-generated method stub
				TreePath path = event.getPath();
		        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
		        String data = node.getUserObject().toString();
		        System.out.println("Expanded: " + data);
			}
			
			@Override
			public void treeCollapsed(TreeExpansionEvent event) {
				// TODO Auto-generated method stub
				TreePath path = event.getPath();
		        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
		        String data = node.getUserObject().toString();
		        System.out.println("Collapsed: " + data);
			}
		});
        tree_1.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
             node2 = (DefaultMutableTreeNode) e
                  .getPath().getLastPathComponent();
              System.out.println("You selected " + node2);
              pathRemoteParent = fileRoot_1.getParent();
              if(pathRemoteParent!=null){
	              if(pathRemoteParent.charAt(pathRemoteParent.length()-1)=='\\'){
	            	  pathRemoteParent+="\\";
	    	      }else{
	    	    	  pathRemoteParent = pathRemoteParent.replace("\\","\\\\");
	    	    	  pathRemoteParent+="\\\\";
	    	      }
              }
              System.out.println("root remote "+pathRemoteParent);
              String jTreeVarSelectedPath = "";
              Object[] paths = tree_1.getSelectionPath().getPath();
              /*for (int i=0; i<paths.length; i++) {
                  jTreeVarSelectedPath += paths[i];
                 
                  if (i+1 <paths.length ) {
                      jTreeVarSelectedPath += File.separator;
                      if(i!=0)jTreeVarSelectedPath += File.separator;
                  }
              }*/
              for (int i=0;i<paths.length;i++) {
            	  paths[i]=paths[i].toString().trim();
              	if(i!=paths.length-1){
						jTreeVarSelectedPath+=paths[i]+"\\";
						jTreeVarSelectedPath+="\\";
              	}else{
              		jTreeVarSelectedPath+=paths[i];
						
              	}
				}
              if(pathRemoteParent!=null){
            	  
            	  textField_4.setText(pathRemoteParent+jTreeVarSelectedPath);
              }else{
            	  jTreeVarSelectedPath=jTreeVarSelectedPath.substring(0,2)+jTreeVarSelectedPath.substring(3,jTreeVarSelectedPath.length());
            	  textField_4.setText(jTreeVarSelectedPath);
              }
            }
          });
        scrollPane_1 = new JScrollPane((Component) null);
        scrollPane_1.setBounds(689, 278, 451, 410);
       
        contentPane.add(scrollPane_1);
    
        tree_1.setShowsRootHandles(true);
        scrollPane_1.setViewportView(tree_1);
        
        CreateChildNodes ccn_1 = 
                new CreateChildNodes(fileRoot_1, root_1);
        new Thread(ccn_1).start();
        
        contentPane.revalidate();
        
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//Object source = e.getSource();
		if(logined==true){
			if(!host.equals("")){
				try {
					socfd = new Socket(host,4000);
					din=new DataInputStream(socfd.getInputStream());
		            dout=new DataOutputStream(socfd.getOutputStream());
					//dout.writeUTF(host+"&"+username+"&"+password+"&"+port);
					//dout.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		switch (e.getActionCommand()) {
		case "btnBrowse":
			browseFolder();       	
			break;
		case "btnDown":
			showMessage("Đang download file "+ node2);
			downFile();
			break;
		case "btnUp":
			showMessage("Đang upload file "+node);
			uploadFile();
			break;
		case "btnNewButton":
			login();
			break;
		case "btnngXut":
			logout();
			break;
		case "btnXa":
			delFileOrDirLocal();
			break;
		case "btnDeleteInRemote":
			delFileOrDirRemote();
			break;
		case "btnngK":
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						RegisterClientUI frameRe = new RegisterClientUI();
						Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
						frameRe.setLocation(dim.width/2-frameRe.getSize().width/2, dim.height/2-frameRe.getSize().height/2);
						frameRe.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			break;

		default:
			break;
		}
	}
	private void uploadFile() {
		String pathFileUpload = textField_5.getText();
		String pathDirSave = textField_4.getText();
		try {
			if(logined==true){
				dout.writeUTF("logined&upload&"+pathFileUpload+"&"+pathDirSave);
				
			}
			else{
				JOptionPane.showMessageDialog(contentPane, "Mời bạn đăng nhập");
				return;
			}
			
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		FileDetails details;
		
	    try {
	        File file = new File(pathFileUpload);
	    	
	            System.out.println("Connected to Client...");
	            data = new byte[2048]; // Here you can increase the size also which will send it faster
	            details = new FileDetails();
	            details.setDetails(file.getName(), file.length());

	            // Sending file details to the client
	            System.out.println("Sending file details...");
	            ObjectOutputStream sendDetails = new ObjectOutputStream(socfd.getOutputStream());
	            sendDetails.writeObject(details);
	            sendDetails.flush();
	            // Sending File Data 
	            System.out.println("Sending file data...");
	            FileInputStream fileStream = new FileInputStream(file);
	            BufferedInputStream fileBuffer = new BufferedInputStream(fileStream);
	            OutputStream out = socfd.getOutputStream();
	            int count;
	            int i=0;
	            
	            while ((count = fileBuffer.read(data)) > 0) {
	                System.out.println("Data Sent : " + count);
	                out.write(data, 0, count);
	                out.flush();
	                System.out.println(i++);
	            }
	            JOptionPane.showMessageDialog(contentPane, "Up file "+node+" thành công");
	            out.close();
	            fileBuffer.close();
	            fileStream.close();
	        
	    } catch (Exception e) {
	        System.out.println("Error : " + e.toString());
	    }
	   
	}

	private void downFile() {
		// TODO Auto-generated method stub
		String pathDirSave="";
		try {
			
			if(logined==true){
				pathDirSave = browseFolder();
				String pathSelected = textField_4.getText();
				
				dout.writeUTF("logined&download&"+pathSelected);
			}else{
				JOptionPane.showMessageDialog(contentPane, "Đăng nhập thất bại.");
				return;
			}
			
			System.out.println("path save "+pathDirSave);
			dout.flush();

	        System.out.println("Getting details from Server...");
	        ObjectInputStream getDetails = new ObjectInputStream(socfd.getInputStream());
	        FileDetails details = (FileDetails) getDetails.readObject();
	        System.out.println("Now receiving file...");
	        // Storing file name and sizes

	        String fileName = details.getName();
	        System.out.println("File Name : " + fileName+" size "+details.getSize());
	        data = new byte[2048]; // Here you can increase the size also which will receive it faster
	        FileOutputStream fileOut = new FileOutputStream(pathDirSave+"\\" + fileName);
	        InputStream fileIn = socfd.getInputStream();
	        BufferedOutputStream fileBuffer = new BufferedOutputStream(fileOut);
	        int count;
	        int sum = 0;
	        
	        
	        System.out.println((int)(details.getSize()/1024/1024)+" size");
	        while ((count = fileIn.read(data)) > 0) {
	            sum += count;
	            double v = ((double)sum/details.getSize()*100);
	            
	            System.out.println((int)((double)Math.round(v*100)/100) +" v ne");
	            
	            valueProgress=(int)((double)Math.round(v*100)/100);
	           
	            fileBuffer.write(data, 0, count);
	            System.out.println("Data received : " + sum);
	            //strMessage+=
	            fileBuffer.flush();
	        }
	        System.out.println("File Received...");
	        JOptionPane.showMessageDialog(contentPane, "Tải file "+node2+" thành công");
	        showMessage("Tải file thành công");
	        fileBuffer.close();
	        fileIn.close();
	        getDetails.close();
			//din.close();
			socfd.close();
	    } catch (Exception e) {
	        System.out.println("Error : " + e.toString());
	    }
		addTree(new File(pathDirSave));
		contentPane.repaint();
		
	}

	private String browseFolder(){
		JFileChooser chooser = new JFileChooser(fileRoot);
		//jc.showOpenDialog(jc);
		if(fileRoot==null)
			chooser.setCurrentDirectory(new java.io.File("D:\\"));
		else chooser.setCurrentDirectory(new java.io.File(fileRoot.getPath()));
	    chooser.setDialogTitle("Chọn thư mục");
	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    //
	    // disable the "All files" option.
	    //
	    chooser.setAcceptAllFileFilterUsed(false);
	    //    
	   
	    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { 
	      System.out.println("getCurrentDirectory(): " 
	         +  chooser.getCurrentDirectory());
	      System.out.println("getSelectedFile() : " 
	         +  chooser.getSelectedFile());
	      fileRoot = chooser.getSelectedFile();
	      pathLocal = fileRoot.getPath();
	      pathLocalParent = fileRoot.getParent();
	      //neu la cac o dia
	      if(pathLocalParent!=null){
		      if(pathLocalParent.charAt(pathLocalParent.length()-1)=='\\'){
		    	  pathLocalParent+="\\";
		      }else{
		    	  pathLocalParent = pathLocalParent.replace("\\","\\\\");
		    	  pathLocalParent+="\\\\";
		      }
	      }
	     
	      textField_5.setText(pathLocal);
		   
		    
		    addTree(fileRoot);
		   // contentPane.updateUI();
		    
		    contentPane.repaint();
		    showMessage("Show thư mục "+fileRoot.getName());
		    return fileRoot.getPath();
	      }
	    else {
	      System.out.println("No Selection ");
	      return "";
	      }
	    
	}
	private void login(){
		host = textField_3.getText();
		String username = txtUser.getText();
		String password =new String(passwordField.getPassword());
		String port = textField_2.getText();
		try {
			socfd = new Socket(host,4000);
			din=new DataInputStream(socfd.getInputStream());
            dout=new DataOutputStream(socfd.getOutputStream());
			dout.writeUTF(host+"&"+username+"&"+password+"&"+port);
			dout.flush();
			try {
				String string = din.readUTF();
				String[] words =string.split(",");
				
				if(words[0].equals("login_succeed")) {
					showMessage("Đăng nhập thành công.");
					JOptionPane.showMessageDialog(contentPane, "Đăng nhập thành công.");
					logined = true;
					pathRootRemote=words[1];
					addTreeRemote();
					contentPane.repaint();
				}else{
					JOptionPane.showMessageDialog(contentPane, "Đăng nhập thất bại.");
					showMessage("Đăng nhập thất bại.");
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			dout.close();
			din.close();
			socfd.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	private void logout() {
		
		
		if(logined==true){
			JOptionPane.showMessageDialog(contentPane, "Đã đăng xuất.");
			scrollPane_1.remove(tree_1);
			contentPane.remove(scrollPane_1);
			passwordField.setText("");
			contentPane.revalidate();
			contentPane.repaint();
			logined = false;
			showMessage("Đã đăng xuất.");
		}else{
			JOptionPane.showMessageDialog(contentPane, "Mời đăng nhập");
		}
		
	}
	private void delFileOrDirLocal(){
		String pathFileDelLocal = textField_5.getText();
		
		try {
			if(logined==true){
				dout.writeUTF("logined&dellocal&"+pathFileDelLocal);
				dout.flush();
			}
			else{
				JOptionPane.showMessageDialog(contentPane, "Mời bạn đăng nhập");
				return;
			}
			
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			String response = din.readUTF();
			System.out.println(response);
			String[] words =response.split("\\&");
			if(words[0].equals("accept")){
				File file = new File(words[1]);
				delete(file);
				JOptionPane.showMessageDialog(contentPane, "Xóa thành công.");
				addTree(fileRoot);
				contentPane.revalidate();
				contentPane.repaint();
			}else{
				JOptionPane.showMessageDialog(contentPane, "Mời bạn đăng nhập");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private void delFileOrDirRemote(){
		String pathFileDelRemote = textField_4.getText();
		
		try {
			if(logined==true){
				dout.writeUTF("logined&delremote&"+pathFileDelRemote);
				dout.flush();
			}
			else{
				JOptionPane.showMessageDialog(contentPane, "Mời bạn đăng nhập");
				return;
			}
			
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			String response = din.readUTF();
			System.out.println(response);
			String[] words =response.split("\\&");
			if(words[0].equals("done")){
				JOptionPane.showMessageDialog(contentPane, "Xóa thành công.");
				contentPane.remove(scrollPane_1);
				contentPane.repaint();
				addTreeRemote();
				contentPane.repaint();
			}else{
				JOptionPane.showMessageDialog(contentPane, "Mời bạn đăng nhập");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private void showMessage(String str){
		strMessage+="\n"+str;
		textPane.setText(strMessage);
		
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

