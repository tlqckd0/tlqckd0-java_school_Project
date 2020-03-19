package 객체프로젝트;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.*;

public class InfoFrame extends JFrame {
	// 0이면 지뢰찾기, 1이면 오목
	int type;
	// 게임정보
	int gameSize = 0;
	int numOfMine = 0;
	String player1 = "player1", player2 = "player2";

	// 게임제목
	JLabel title;
	JLabel error = new JLabel();

	// 시작버튼
	JButton jbn = new JButton("게임실행");

	// 사용자 이름
	JLabel name1 = new JLabel("이름 : ");
	JTextField nameField1 = new JTextField(10);
	JLabel name2 = new JLabel("이름 : ");
	JTextField nameField2 = new JTextField(10);

	// 지뢰 갯수
	JLabel mineNumLabel = new JLabel("지뢰 개수 : ");
	JTextField mineNumField = new JTextField(10);
	JSlider sizeSlider;
	// 게임크기
	JLabel sizeLabel = new JLabel();
	JTextField sizeField = new JTextField(10);

	// 오목설정하는것
	JLabel nameLabel[] = new JLabel[2];
	JRadioButton front[] = new JRadioButton[2];
	ButtonGroup bg = new ButtonGroup();
	JPanel topPanel, middlePanel, bottomPanel;
	JButton throwButton = new JButton("던지기!");
	boolean whoIsFront;// player1이 앞 골랐으면 true player2가 앞 골랐으면 false

	public InfoFrame(int selected) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		type = selected;

		jbn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (type == 0) {// type = 0 지뢰찾기
					if (numOfMine == 0) {
						error.setText("알맞는 정보를 입력해주세요");
						return;
					} else {// 지뢰찾기 실행!
						Game1 game1 = new Game1(gameSize, numOfMine);
						dispose();
					}
				} else {// 오목 실행!
					if (player1.equals("") || player2.equals("")) {
						error.setText("알맞는 정보를 입력해주세요");
						return;
					} else {
						Game2 game2 = new Game2(gameSize, player1, player2);
						dispose();
					}
				}
			}
		});

		if (type == 0) {// 지뢰찾기 설정패널
			setTitle("지뢰찾기 설정");
			setLayout(new FlowLayout());

			// 제목
			title = new JLabel("지뢰찾기 설정 ");
			JLabel sizeinfo = new JLabel("크기는 10 ~ 50");
			add(title);
			add(sizeinfo);
			// 사이즈페널
			setSizePanel sizePanel = new setSizePanel();
			add(sizePanel);

			// 지뢰겟수패널
			minePanel minePanel = new minePanel();
			add(minePanel);

			add(error);
			// 실행버튼
			add(jbn);
		} else { // 오목 설정패널
			setTitle("오목설정");
			setLayout(new FlowLayout());

			// 제목
			title = new JLabel("오목 설정 ");
			JLabel sizeinfo = new JLabel("크기는 20 ~ 50");
			add(title);
			add(sizeinfo);

			// 사이즈 설정
			setSizePanel sizePanel = new setSizePanel();
			add(sizePanel);

			playerPanel playpanel = new playerPanel();
			add(playpanel);

			add(error);

			// 실행버튼
			add(jbn);
		}
		setVisible(true);
		setSize(260, 300);
	}

	// 크기설정패널
	class setSizePanel extends JPanel {

		public setSizePanel() {
			setLayout(new GridLayout(2, 0));
			if (type == 0) {
				sizeSlider = new JSlider(JSlider.HORIZONTAL, 10, 50, 15);
				gameSize = 15;// 초기값
			} else {
				sizeSlider = new JSlider(JSlider.HORIZONTAL, 20, 50, 25);
				gameSize = 25;// 초기값
			}
			sizeLabel.setText("게임크기 : " + gameSize);
			sizeSlider.setPaintLabels(true);
			sizeSlider.setPaintTicks(true);
			sizeSlider.setPaintTrack(true);
			sizeSlider.setMajorTickSpacing(10);
			sizeSlider.setMinorTickSpacing(1);
			sizeSlider.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					gameSize = sizeSlider.getValue();
					sizeLabel.setText("게임크기 : " + gameSize);
				}
			});
			add(sizeLabel);
			add(sizeSlider);
			setVisible(true);
		}
	}

	// 지뢰갯수 설정패널
	class minePanel extends JPanel {
		public minePanel() {
			setLayout(new GridLayout());
			mineNumField.addActionListener(new mylistener());
			add(mineNumLabel);
			add(mineNumField);
			setVisible(true);
		}

		class mylistener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				JTextField f = (JTextField) e.getSource();
				int mine = Integer.parseInt(f.getText());
				if (mine > gameSize * gameSize / 2) {// 최대 지뢰개수 = size*size/2
					numOfMine = 0;
				} else if (mine == 0) {// 지뢰 0 개는 안됨
					numOfMine = 0;
				} else {
					numOfMine = mine;
				}
			}
		}
	}

	// 오목사용자입력패널
	class playerPanel extends JPanel {

		public playerPanel() {

			if (type == 0) {// 오목일 경우
				setLayout(new GridLayout(1, 2));
				nameField1.setText("player1");
				nameField1.addActionListener(new mylistener());
				add(name1);
				add(nameField1);
				setVisible(true);
			} else {
				setLayout(new GridLayout(2, 2));
				nameField1.setText("player1");
				nameField1.addActionListener(new mylistener());
				add(name1);
				add(nameField1);

				nameField2.setText("player2");
				nameField2.addActionListener(new mylistener());
				add(name2);
				add(nameField2);
				setVisible(true);
			}
		}

		class mylistener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				JTextField f = (JTextField) e.getSource();
				if (f == nameField1) {
					player1 = f.getText();
				} else {
					player2 = f.getText();
				}
			}
		}
	}
}
