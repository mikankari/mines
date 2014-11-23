import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Minesweeper extends JFrame implements ActionListener{

	JToggleButton[] board_button;
	JPanel board_panel;
	JButton clear_button;
	JButton option_button;
	JButton exit_button;
	JLabel manes_label;
	JTextField manes_text;
	JPanel manes_panel;
	JPanel buttons_panel;
	String appname = "必殺地雷人 version 0.2 alpha 3";
	boolean isplay;
	boolean debugmode;
	double difficulty = 0.12;

	public Minesweeper(String debugmode){
		this.debugmode = debugmode.equals("true") ? true : false;
		initFrame();
	}

	public void initFrame(){
		board_panel = new JPanel();
		board_panel.setLayout(new GridLayout(10, 10));
		board_button = new JToggleButton[100];
		for(int loop = 0; loop < board_button.length; loop++){
			board_button[loop] = new JToggleButton();
			board_button[loop].addActionListener(this);
			board_panel.add(board_button[loop]);
		}
		buttons_panel = new JPanel();
		manes_panel = new JPanel();
		manes_label = new JLabel("Manes:");
		manes_panel.add(manes_label);
		manes_text = new JTextField(3);
		manes_text.setEditable(false);
		manes_text.setFocusable(false);
		manes_panel.add(manes_text);
		buttons_panel.add(manes_panel);
		clear_button = new JButton("Clear");
		clear_button.setActionCommand("clear");
		clear_button.addActionListener(this);
		buttons_panel.add(clear_button);
		option_button = new JButton("Option");
		option_button.setActionCommand("option");
		option_button.addActionListener(this);
		buttons_panel.add(option_button);
		exit_button = new JButton("Exit");
		exit_button.setActionCommand("exit");
		exit_button.addActionListener(this);
		buttons_panel.add(exit_button);
		getContentPane().add(board_panel, BorderLayout.CENTER);
		getContentPane().add(buttons_panel, BorderLayout.SOUTH);
		
		updateBoard();
		
		setTitle(appname);
		addWindowListener(new WindowAdapter(){
								public void windowClosing(WindowEvent evt){
									onExit();
								}
							});
		String back = board_button[0].getText();
		board_button[0].setText("ば");
		pack();
		board_button[0].setText(back);
		setLocationByPlatform(true);
		setResizable(false);
		setVisible(true);
	}

	private void updateBoard(){
		int manes = 0;
		for(int loop = 0; loop < board_button.length; loop++){
			board_button[loop].setText("");
			board_button[loop].setForeground(new Color(0, 0, 0));
			board_button[loop].setSelected(false);
			board_button[loop].setActionCommand("board:" + loop + ":-");
			board_button[loop].setFocusable(true);
			board_button[loop].setEnabled(true);
		}
		
		while(manes != (int)(difficulty * 100)){
			manes = 0;
			for(int loop = 0; loop < board_button.length; loop++){
				board_button[loop].setActionCommand("board:" + loop + ":-");
				if(Math.random() < difficulty){
					board_button[loop].setActionCommand("board:" + loop + ":*");
					manes++;
				}
			}
		}
		manes_text.setText("" + manes);
		for(int loop = 0; loop < board_button.length; loop++){
			if(board_button[loop].getActionCommand().split(":")[2].equals("-")){
				int[] indexes;
				if(loop + 1 == 1){
					indexes = new int[]{1, 10, 11};
				}
				else if(loop + 1 == 10){
					indexes = new int[]{-1, 9, 10};
				}
				else if(loop + 1 == 91){
					indexes = new int[]{-10, -9, 1};
				}
				else if(loop + 1 == 100){
					indexes = new int[]{-11, -10, -1};
				}
				else if(loop + 1 >= 2 && loop + 1 <= 9){
					indexes = new int[]{-1, 1, 9, 10, 11};
				}
				else if(loop + 1 >= 92 && loop + 1 <= 99){
					indexes = new int[]{-11, -10, -9, -1, 1};
				}
				else if((loop + 1) % 10 == 1 && loop + 1 != 1 && loop + 1 != 91){
					indexes = new int[]{-10, -9, 1, 10, 11};
				}
				else if((loop + 1) % 10 == 0 && loop + 1 != 10 && loop + 1 != 100){
					indexes = new int[]{-11, -10, -1, 9, 10};
				}
				else{
					indexes = new int[]{-11, -10, -9, -1, 1, 9, 10, 11};
				}
				int explosions = 0;
				for(int loop2 = 0; loop2 < indexes.length; loop2++){
					if(board_button[loop + indexes[loop2]].getActionCommand().split(":")[2].equals("*")){
						explosions++;
					}
				}
				board_button[loop].setActionCommand("board:" + loop + ":" + explosions);
			}
		}
		if(debugmode){
			for(int loop = 0; loop < board_button.length; loop++){
				board_button[loop].setText(board_button[loop].getActionCommand().split(":")[2]);
			}
		}
		isplay = false;
	}
	
	public void actionPerformed(ActionEvent evt){
		String command = evt.getActionCommand();
		
		if(command.startsWith("board:")){
			int index = 0;
			try{
				index = Integer.parseInt(command.split(":")[1]);
			}
			catch(NumberFormatException err){
			}
			onBoard(index);
		}
		if(command.equals("clear")){
			onClear();
		}
		if(command.equals("option")){
			onOption();
		}
		if(command.equals("exit")){
			onExit();
		}
	}
	
	private void onBoard(int index){
		board_button[index].setSelected(true);
		board_button[index].setFocusable(false);
		int explosions = 0;
		try{
			explosions = Integer.parseInt(board_button[index].getActionCommand().split(":")[2]);
		}
		catch(NumberFormatException err){
		}
		if(explosions == 1){
			board_button[index].setForeground(new Color(0, 0, 255));
		}
		if(explosions == 2){
			board_button[index].setForeground(new Color(0, 128, 0));
		}
		if(explosions == 3){
			board_button[index].setForeground(new Color(255, 0, 0));
		}
		if(explosions == 4){
			board_button[index].setForeground(new Color(0, 0, 128));
		}
		if(explosions == 5){
			board_button[index].setForeground(new Color(128, 0, 0));
		}
		if(explosions == 6){
			board_button[index].setForeground(new Color(0, 128, 128));
		}
		if(explosions == 7){
			board_button[index].setForeground(new Color(0, 0, 0));
		}
		if(explosions == 8){
			board_button[index].setForeground(new Color(128, 128, 128));
		}
		board_button[index].setText(explosions == 0 ? "" : "" + explosions);
		if(board_button[index].getActionCommand().split(":")[2].equals("0")){
				int[] indexes;
				if(index + 1 == 1){
					indexes = new int[]{1, 10, 11};
				}
				else if(index + 1 == 10){
					indexes = new int[]{-1, 9, 10};
				}
				else if(index + 1 == 91){
					indexes = new int[]{-10, -9, 1};
				}
				else if(index + 1 == 100){
					indexes = new int[]{-11, -10, -1};
				}
				else if(index + 1 >= 2 && index + 1 <= 9){
					indexes = new int[]{-1, 1, 9, 10, 11};
				}
				else if(index + 1 >= 92 && index + 1 <= 99){
					indexes = new int[]{-11, -10, -9, -1, 1};
				}
				else if((index + 1) % 10 == 1 && index + 1 != 1 && index + 1 != 91){
					indexes = new int[]{-10, -9, 1, 10, 11};
				}
				else if((index + 1) % 10 == 0 && index + 1 != 10 && index + 1 != 100){
					indexes = new int[]{-11, -10, -1, 9, 10};
				}
				else{
					indexes = new int[]{-11, -10, -9, -1, 1, 9, 10, 11};
				}
				for(int loop = 0; loop < indexes.length; loop++){
					if(!board_button[index + indexes[loop]].isSelected()){
						onBoard(index + indexes[loop]);
					}
				}
		}
		if(board_button[index].getActionCommand().split(":")[2].equals("*")){
			if(!isplay){
				updateBoard();
				onBoard(index);
				return;
			}
			for(int loop = 0; loop < board_button.length; loop++){
				board_button[loop].setText("");
				board_button[loop].setEnabled(false);
			}
			board_button[0].setText("ば");
			board_button[1].setText("く");
			board_button[2].setText("は");
			board_button[3].setText("つ");
			board_button[4].setText("！");
			board_button[5].setText("×");
			board_button[6].setText("∧");
			board_button[7].setText("×");
			isplay = false;
			return;
		}
			int manes = 0;
			try{
				manes = Integer.parseInt(manes_text.getText());
			}
			catch(NumberFormatException err){
			}
			int pushed = 0;
			for(int loop = 0; loop < board_button.length; loop++){
				if(board_button[loop].isSelected()){
					pushed++;
				}
			}
			if(100 - pushed == manes){
				for(int loop = 0; loop < board_button.length; loop++){
					board_button[loop].setText("");
					board_button[loop].setEnabled(false);
				}
				board_button[0].setText("お");
				board_button[1].setText("め");
				board_button[2].setText("で");
				board_button[3].setText("と");
				board_button[4].setText("！");
				board_button[5].setText("●");
				board_button[6].setText("∀");
				board_button[7].setText("●");
				isplay = false;
				return;
			}
		if(!isplay){
			isplay = true;
		}
	}
	
	private void onClear(){
		updateBoard();
	}
	
	private void onOption(){
		OnOption onoption = new OnOption(this, difficulty, appname);
		double diff = onoption.getDifficulty();
		if(diff != -1){
				difficulty = diff;
		}
	}
	
	private void onExit(){
		if(isplay){
			if(JOptionPane.showConfirmDialog(this, "プレイ中、終了する？", appname, JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) != JOptionPane.OK_OPTION){
				return;
			}
		}
		System.exit(0);
	}

	public static void main(String[] args){
		if(args.length == 1){
			new Minesweeper(args[0]);
		}
		else{
			new Minesweeper("false");
		}
	}
}
