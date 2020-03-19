package ��ü������Ʈ;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.*;

public class InfoFrame extends JFrame {
	// 0�̸� ����ã��, 1�̸� ����
	int type;
	// ��������
	int gameSize = 0;
	int numOfMine = 0;
	String player1 = "player1", player2 = "player2";

	// ��������
	JLabel title;
	JLabel error = new JLabel();

	// ���۹�ư
	JButton jbn = new JButton("���ӽ���");

	// ����� �̸�
	JLabel name1 = new JLabel("�̸� : ");
	JTextField nameField1 = new JTextField(10);
	JLabel name2 = new JLabel("�̸� : ");
	JTextField nameField2 = new JTextField(10);

	// ���� ����
	JLabel mineNumLabel = new JLabel("���� ���� : ");
	JTextField mineNumField = new JTextField(10);
	JSlider sizeSlider;
	// ����ũ��
	JLabel sizeLabel = new JLabel();
	JTextField sizeField = new JTextField(10);

	// �������ϴ°�
	JLabel nameLabel[] = new JLabel[2];
	JRadioButton front[] = new JRadioButton[2];
	ButtonGroup bg = new ButtonGroup();
	JPanel topPanel, middlePanel, bottomPanel;
	JButton throwButton = new JButton("������!");
	boolean whoIsFront;// player1�� �� ������� true player2�� �� ������� false

	public InfoFrame(int selected) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		type = selected;

		jbn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (type == 0) {// type = 0 ����ã��
					if (numOfMine == 0) {
						error.setText("�˸´� ������ �Է����ּ���");
						return;
					} else {// ����ã�� ����!
						Game1 game1 = new Game1(gameSize, numOfMine);
						dispose();
					}
				} else {// ���� ����!
					if (player1.equals("") || player2.equals("")) {
						error.setText("�˸´� ������ �Է����ּ���");
						return;
					} else {
						Game2 game2 = new Game2(gameSize, player1, player2);
						dispose();
					}
				}
			}
		});

		if (type == 0) {// ����ã�� �����г�
			setTitle("����ã�� ����");
			setLayout(new FlowLayout());

			// ����
			title = new JLabel("����ã�� ���� ");
			JLabel sizeinfo = new JLabel("ũ��� 10 ~ 50");
			add(title);
			add(sizeinfo);
			// ���������
			setSizePanel sizePanel = new setSizePanel();
			add(sizePanel);

			// ���ڰټ��г�
			minePanel minePanel = new minePanel();
			add(minePanel);

			add(error);
			// �����ư
			add(jbn);
		} else { // ���� �����г�
			setTitle("������");
			setLayout(new FlowLayout());

			// ����
			title = new JLabel("���� ���� ");
			JLabel sizeinfo = new JLabel("ũ��� 20 ~ 50");
			add(title);
			add(sizeinfo);

			// ������ ����
			setSizePanel sizePanel = new setSizePanel();
			add(sizePanel);

			playerPanel playpanel = new playerPanel();
			add(playpanel);

			add(error);

			// �����ư
			add(jbn);
		}
		setVisible(true);
		setSize(260, 300);
	}

	// ũ�⼳���г�
	class setSizePanel extends JPanel {

		public setSizePanel() {
			setLayout(new GridLayout(2, 0));
			if (type == 0) {
				sizeSlider = new JSlider(JSlider.HORIZONTAL, 10, 50, 15);
				gameSize = 15;// �ʱⰪ
			} else {
				sizeSlider = new JSlider(JSlider.HORIZONTAL, 20, 50, 25);
				gameSize = 25;// �ʱⰪ
			}
			sizeLabel.setText("����ũ�� : " + gameSize);
			sizeSlider.setPaintLabels(true);
			sizeSlider.setPaintTicks(true);
			sizeSlider.setPaintTrack(true);
			sizeSlider.setMajorTickSpacing(10);
			sizeSlider.setMinorTickSpacing(1);
			sizeSlider.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					gameSize = sizeSlider.getValue();
					sizeLabel.setText("����ũ�� : " + gameSize);
				}
			});
			add(sizeLabel);
			add(sizeSlider);
			setVisible(true);
		}
	}

	// ���ڰ��� �����г�
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
				if (mine > gameSize * gameSize / 2) {// �ִ� ���ڰ��� = size*size/2
					numOfMine = 0;
				} else if (mine == 0) {// ���� 0 ���� �ȵ�
					numOfMine = 0;
				} else {
					numOfMine = mine;
				}
			}
		}
	}

	// ���������Է��г�
	class playerPanel extends JPanel {

		public playerPanel() {

			if (type == 0) {// ������ ���
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
