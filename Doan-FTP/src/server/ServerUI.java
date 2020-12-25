package server;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import lib.LibraryConnectDb;

import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ServerSocket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ServerUI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static ServerSocket socfd;
	private static FTPServer t;
	private JPanel contentPane;
	private LibraryConnectDb connectDb;
	private java.sql.Connection conn;
	private Statement st;
	private PreparedStatement pst;
	private ResultSet rs;
	
	private DataInputStream din;
	private DataOutputStream dout;   

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerUI frame = new ServerUI();
					Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
					frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
					frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		try {
			socfd = new ServerSocket(4000);
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        System.out.println("FTP Server Started on Port Number 21");
        while(true)
        {
            System.out.println("Waiting for Connection ...");
            try {
				t=new FTPServer(socfd.accept());
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}            
        }
	}
	/**
	 * Create the frame.
	 */
	public ServerUI() {
		connectDb = new LibraryConnectDb();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(700, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewButton = new JButton("Đăng nhập tài khoản");
		btnNewButton.setBounds(48, 86, 135, 58);
		contentPane.add(btnNewButton);
		
		JLabel lblNewLabel = new JLabel("FTP ADMIN");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 36));
		lblNewLabel.setBounds(127, 11, 199, 43);
		contentPane.add(lblNewLabel);
		
		JButton btnManagerAccount = new JButton("Quản lý tài khoản");
		btnManagerAccount.setBounds(254, 86, 135, 58);
		contentPane.add(btnManagerAccount);
		btnManagerAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sql = "SELECT * FROM account WHERE 1";
				conn = connectDb.getConnectMySql();
				try {
					pst = conn.prepareStatement(sql);
				} catch (SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				try {
					rs = pst.executeQuery();
				} catch (SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			try {
				while (rs.next()) {
					System.out.print("Username"+rs.getString("username") + "Password" + rs.getString("password"));
					
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
				
			}
		});
		
		JButton btnExit = new JButton("Thoát");
		btnExit.setBounds(300, 216, 89, 34);
		contentPane.add(btnExit);
	}
	
}
	

