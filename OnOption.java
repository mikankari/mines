import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class OnOption extends JDialog implements ActionListener{
	
	JLabel difficulty_label;
	JTextField difficulty_text;
	JPanel difficulty_panel;
	JLabel appname_label;
	JPanel appname_panel;
	JPanel option_panel;
	JButton ok_button;
	JButton cancel_button;
	JPanel buttons_panel;
	double difficulty_setting;
	String description = "低品質マインスイーパー";
	
	public OnOption(Frame owner, double difficulty, String appname){
		super(owner);
		initDialog(difficulty, appname);
	}
	
	private void initDialog(double difficulty, String appname){
		option_panel = new JPanel();
		option_panel.setLayout(new GridLayout(2, 1));
		difficulty_panel = new JPanel();
		difficulty_label = new JLabel("難易度・爆弾数（0～100）:");
		difficulty_text = new JTextField(3);
		difficulty_panel.add(difficulty_label);
		difficulty_panel.add(difficulty_text);
		option_panel.add(difficulty_panel);
		appname_panel = new JPanel();
		appname_label = new JLabel("<html>AppName: " + appname + "<br>Description: " + description);
		appname_panel.add(appname_label);
		option_panel.add(appname_panel);
		buttons_panel = new JPanel();
		ok_button = new JButton("了解");
		ok_button.setActionCommand("ok");
		ok_button.addActionListener(this);
		cancel_button = new JButton("取消し");
		cancel_button.setActionCommand("cancel");
		cancel_button.addActionListener(this);
		buttons_panel.add(ok_button);
		buttons_panel.add(cancel_button);
		getContentPane().add(option_panel, BorderLayout.CENTER);
		getContentPane().add(buttons_panel, BorderLayout.SOUTH);

		difficulty_text.setText("" + (int)(difficulty * 100));
		
		setTitle("Option");
		addWindowListener(new WindowAdapter(){
								public void windowClosing(WindowEvent evt){
									onCancel();
								}
							});
		pack();
		setLocationByPlatform(true);
		setResizable(false);
		setModal(true);
		setVisible(true);
	}
	
	public double getDifficulty(){
		return difficulty_setting;
	}
	
	public void actionPerformed(ActionEvent evt){
		String command = evt.getActionCommand();
		
		if(command.equals("ok")){
			onOk();
		}
		if(command.equals("cancel")){
			onCancel();
		}
	}
	
	private void onOk(){
		int i = 0;
		try{
			i = Integer.parseInt(difficulty_text.getText());
		}
		catch(NumberFormatException err){
			JOptionPane.showMessageDialog(this, "それは整数でない、入力をやり直してください");
			return;
		}
		if(!(i >= 0 && i <= 100)){
			JOptionPane.showMessageDialog(this, "指定は範囲外、入力をやり直してください");
			return;
		}
		difficulty_setting = i * 0.01;
		dispose();
	}
	
	private void onCancel(){
		difficulty_setting = -1;
		dispose();
	}
}
