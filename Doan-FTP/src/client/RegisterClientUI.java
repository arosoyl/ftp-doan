package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.sql.Connection;

import lib.LibraryConnectDb;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JTextField;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;

public class RegisterClientUI extends JFrame {
	static RegisterClientUI frameRe;
	private JPanel contentPane;
	private JTextField textField;
	private JLabel lblPath;
	private JTextField textField_4;
	private JButton btnNewButton;
	private JLabel lblQuyn;
	private JPasswordField passwordField;
	private JPasswordField passwordField_1;
	private JTextField textField_1;
	private JButton btnNewButton_1;
	private JLabel lblNewLabel_1;
	private LibraryConnectDb connectDb;
	private java.sql.Connection conn;
	private Statement st;
	private PreparedStatement pst;
	private ResultSet rs;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frameRe = new RegisterClientUI();
					
					frameRe.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public RegisterClientUI() {
		
		connectDb = new LibraryConnectDb();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 531);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Tên đăng nhập");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel.setBounds(34, 97, 120, 19);
		contentPane.add(lblNewLabel);
		
		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textField.setBounds(173, 93, 240, 31);
		contentPane.add(textField);
		textField.setColumns(10);
		
	
		JLabel lblPassword = new JLabel("Mật khẩu");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblPassword.setBounds(34, 157, 120, 19);
		contentPane.add(lblPassword);
		
		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Tahoma", Font.PLAIN, 15));
		passwordField.setBounds(173, 153, 240, 31);
		contentPane.add(passwordField);
		
		JLabel lblConfirmPassword = new JLabel("Xác nhận mật khẩu");
		lblConfirmPassword.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblConfirmPassword.setBounds(34, 217, 135, 19);
		contentPane.add(lblConfirmPassword);
		
		passwordField_1 = new JPasswordField();
		passwordField_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		passwordField_1.setBounds(173, 213, 240, 31);
		contentPane.add(passwordField_1);
		
		lblPath = new JLabel("Đường dẫn");
		lblPath.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblPath.setBounds(34, 277, 88, 19);
		contentPane.add(lblPath);
		
		textField_4 = new JTextField();
		textField_4.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textField_4.setColumns(10);
		textField_4.setBounds(173, 273, 240, 31);
		contentPane.add(textField_4);
		
		btnNewButton = new JButton("Duyệt");
		btnNewButton.setBounds(436, 276, 67, 23);
		contentPane.add(btnNewButton);
		
		lblQuyn = new JLabel("Quyền");
		lblQuyn.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblQuyn.setBounds(34, 337, 88, 19);
		contentPane.add(lblQuyn);
		
		
		JRadioButton rdbtnNewRadioButton = new JRadioButton("Chỉ tải");
		rdbtnNewRadioButton.setBounds(173, 339, 109, 23);
		contentPane.add(rdbtnNewRadioButton);
		
		JRadioButton rdbtnTonQuyn = new JRadioButton("Toàn quyền");
		rdbtnTonQuyn.setBounds(173, 365, 109, 23);
		contentPane.add(rdbtnTonQuyn);
		
		ButtonGroup group = new ButtonGroup();
		group.add(rdbtnNewRadioButton);
		group.add(rdbtnTonQuyn);
		
		btnNewButton_1 = new JButton("ĐĂNG KÝ");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = textField.getText();
				String password = new String(passwordField.getPassword());
				String repassword = new String(passwordField_1.getPassword());
				String path = textField_4.getText();
				rdbtnNewRadioButton.setActionCommand("onlydown");
				rdbtnTonQuyn.setActionCommand("allrule");
				int role = group.getSelection().getActionCommand().equals("onlydown")?1:0;
				int result = 0;
				
				if (password == repassword) JOptionPane.showMessageDialog(contentPane, "Mật khẩu xác nhận không khớp. Vui lòng nhập lại!" + password + repassword);
				else {
					String sql ="INSERT INTO account(username,password,role,path) VALUES(?, ?, ?, ?)";
					conn = connectDb.getConnectMySql();
					try {
						pst = conn.prepareStatement(sql);
						pst.setString(1, username);
						pst.setString(2, password);
						//pst.setString(3, fullname);
						pst.setInt(3, role);
						pst.setString(4, path);
						result = pst.executeUpdate();
					} catch (SQLException exception) {
						exception.printStackTrace();
					}finally{
						try{
							pst.close();
							conn.close();
						}catch(SQLException exception2){
							exception2.printStackTrace();
						}
					}
					
					if(result==1){
						JOptionPane.showMessageDialog(contentPane, "Đăng ký thành công, mời bạn đăng nhập.");
						frameRe.dispose();
					}else{
						JOptionPane.showMessageDialog(contentPane, "Đăng ký không thành công.");
					}
				}			
			}
		});
		
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnNewButton_1.setBounds(217, 422, 129, 37);
		contentPane.add(btnNewButton_1);
		
		lblNewLabel_1 = new JLabel("Đăng ký tài khoản");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 26));
		lblNewLabel_1.setBounds(173, 11, 259, 42);
		contentPane.add(lblNewLabel_1);
	}
}
