import java.awt.*;

import javax.swing.*;

public class WriteJFrame extends JFrame{

	JFrame writeJFrame = new JFrame("授权码填写");
	Container container = writeJFrame.getContentPane();
	private JLabel licenceLabel = null;
	private JLabel agencyLabel = null;
	private JLabel contactLabel = null;
	private JTextField licenceField = null;
	private JTextField agencyField = null;
	private JTextField contactField = null;
	private JButton checkButton = null;
	private JButton confirmButton = null;
	private JLabel requiredLabel = null; 
	
	JPanel panel = new JPanel();
	
	public WriteJFrame() {
		// TODO Auto-generated constructor stub
		this.setTitle("授权码填写");
		this.setSize(500, 400);
		this.setResizable(false);
		this.setLocationRelativeTo(null);//将窗口的位置设置在正中间
		this.setLayout(null);
		initComp();
		addComp(this);
		setCompPosition();
		
	}
		

	public void initComp() {
		licenceLabel = new JLabel("授权码:");
		licenceLabel.setFont(new Font("黑体", 0, 18));
		agencyLabel = new JLabel("经销商：");
		agencyLabel.setFont(new Font("黑体", 0, 18));
		contactLabel = new JLabel("联系人：");
		contactLabel.setFont(new Font("黑体", 0, 18));
		licenceField = new JTextField();
		agencyField = new JTextField();
		contactField = new JTextField();
		checkButton = new JButton("检测");
		confirmButton = new JButton("确认绑定设备");
		requiredLabel = new JLabel("*");
		requiredLabel.setForeground(Color.red);
	}
	
	public void addComp(WriteJFrame writeJFrame) {
		writeJFrame.add(licenceLabel);
		writeJFrame.add(licenceField);
		writeJFrame.add(checkButton);
		writeJFrame.add(agencyLabel);
		writeJFrame.add(agencyField);
		writeJFrame.add(contactLabel);
		writeJFrame.add(contactField);
		writeJFrame.add(confirmButton);
		writeJFrame.add(requiredLabel);
	}
	
	public void setCompPosition() {
		licenceLabel.setBounds(100, 20, 80, 40);
		licenceField.setBounds(180, 25, 150, 30);
		checkButton.setBounds(350, 25, 80, 30);
		agencyLabel.setBounds(100, 100, 80, 40);
		agencyField.setBounds(180, 105, 150, 30);
		contactLabel.setBounds(100, 175, 80, 40);
		contactField.setBounds(180, 180, 150, 30);
		confirmButton.setBounds(180, 250, 150, 30);
		requiredLabel.setBounds(170, 30, 10, 15);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		WriteJFrame writeJFrame = new WriteJFrame();
		writeJFrame.setVisible(true);
	}

}
