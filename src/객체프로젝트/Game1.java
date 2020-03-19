///////////////////////
//////����ã��������///////
///////////////////////
package ��ü������Ʈ;

import javax.swing.*;

import ��ü������Ʈ.Game2.game2FinishFrame.buttonlistener;

import java.lang.Thread;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Random;

class MyButton extends JButton {

	private static int num = 0;
	int x, y;// ��ư ũ������
	int index;// ��ư�� ��ȣ
	int flag = 2; // 0 �̸� ���ǥ�� 1�̸� ����ǥ 2�̸� ǥ�� X
	int surroundMineNum = 0;// �������ִ� ������ ����

	boolean gamefinish = false;// ���ӳ����� true
	boolean clickCheck = false;// ��ư�� Ŭ���ߴ��� Ȯ��
	boolean mine = false;// �������� �ƴ���
	boolean gamewin = false;// ���Ӽ����ϸ� true

	public MyButton() {
		index = num++;
	}

	static public void reset() {
		num = 0;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		x = this.getWidth();
		y = this.getHeight();
		if (clickCheck == true && mine == true && flag == 2) {// ���ڸ� Ŭ��
			this.setBackground(Color.RED);
			g.fillOval(x / 4, y / 4, x / 2, y / 2);
			g.drawLine(x / 4, y / 4, (x * 3) / 4, (y * 3) / 4);
			g.drawLine((x * 3) / 4, y / 4, x / 4, (y * 3) / 4);
		} else if (gamefinish == true && mine == true && flag == 2 && gamewin == false) {// ������ ������ ���
			g.fillOval(x / 4, y / 4, x / 2, y / 2);
			g.drawLine(x / 4, y / 4, (x * 3) / 4, (y * 3) / 4);
			g.drawLine((x * 3) / 4, y / 4, x / 4, (y * 3) / 4);
		} else if (flag == 0 && clickCheck == true && mine == false && gamefinish == true) {// �߸��� ���ǥ��
			g.fillOval(x / 4, y / 4, x / 2, y / 2);
			g.drawLine(x / 4, y / 4, (x * 3) / 4, (y * 3) / 4);
			g.drawLine((x * 3) / 4, y / 4, x / 4, (y * 3) / 4);
			g.setColor(Color.RED);
			g.drawLine(0, 0, x, y);
			g.drawLine(x, 0, 0, y);
		} else if (flag == 0 || (gamefinish == true && mine == true && flag == 2 && gamewin == true)) {// ��� ��ũ
			g.setColor(Color.RED);
			g.fillRect(0, 0, x / 2, y / 2);
			g.setColor(Color.BLACK);
			g.drawLine(x / 2, 0, x / 2, y);
			g.fillRect(0, (y * 3) / 4, x, y);
		} else if (flag == 1) {// ����ǥ ǥ��
			g.setFont(new Font("�ü�ü", Font.BOLD, 30));
			g.drawString("?", x / 3, (y * 3) / 4);
		}
	}
}

public class Game1 extends JFrame {
	JFrame mygame1 = this;

	// �������� 0 �Ϲ� 1���� 2����
	private int state = 0;
	// �����ϴ»��
	private String player;
	// ��ȣ�޸� ��ư ����
	private MyButton button[];
	private Reset resetbutton = new Reset();
	// ������ġ
	private int mineLocation[];
	// ����ũ��, ���ڰ���, ��߰���, �ð�
	private int gameSize;
	private int numOfMine;
	private int numOfFlag;
	private int time = 0;
	private int leftButtonNum;
	// ��ġ ��ư �����ʺ��� �ð�������� ����
	private int buttonSurround[] = new int[8];
	// �����г�
	private JPanel gamePanel;
	// ��������г�
	private JPanel showPanel = new JPanel();
	private JLabel flagLabel = new JLabel();
	// Ÿ�̸�
	Timer timer = new Timer();

	// �¸� ������
	JButton resetButton = new JButton("�ٽ��ϱ�");
	JButton goStart = new JButton("���۸޴�");
	JButton exit = new JButton("����");

	public Game1(int size, int mineNum) {
		setTitle("Game1");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameSize = size;
		numOfMine = mineNum;
		numOfFlag = mineNum;
		leftButtonNum = size * size;

		// ���ڻ���
		MakeMine();
		// ��ư�����δ콺Ȯ�ο�
		MakebuttonSurround();
		setLayout(new BorderLayout());
		// ����ȭ�� ǥ��
		showPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		// Ÿ�̸�
		flagLabel.setText("MINE : " + numOfFlag);
		flagLabel.setFont(new Font("Gothic", Font.PLAIN, 30));
		flagLabel.setBackground(Color.WHITE);
		flagLabel.setOpaque(true);
		showPanel.add(flagLabel);
		// ���Ӹ���
		resetbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				restart();
			}
		});
		showPanel.setBackground(Color.DARK_GRAY);
		showPanel.add(resetbutton);

		showPanel.add(timer);
		// �����гμ���
		gamePanel = new JPanel();
		gamePanel.setLayout(new GridLayout(gameSize, gameSize));
		// ��ư ���� �Է� 1
		inputdata1();
		// ��ư ���� �Է�2
		inputdata2();
		// äũ
		check();
		// �гκ���
		add(showPanel, BorderLayout.NORTH);
		add(gamePanel, BorderLayout.CENTER);
		setSize(gameSize * 45, gameSize * 45);

		setVisible(true);
	}

	// ��ưȮ��(��)
	public void check() {
		System.out.println("");
		int count = 0;
		for (int i = 0; i < gameSize; i++) {
			for (int j = 0; j < gameSize; j++) {
				if (button[count].mine == false)
					System.out.print(button[count].surroundMineNum + "\t");
				else
					System.out.print("mine\t");
				count++;
			}
			System.out.println("");
		}
	}

	// �����!
	public void restart() {
		gamePanel.removeAll();// �����г� �ʱ�ȭ
		timer.finish();// ���� Ÿ�̸� ����
		state = 0;// �����ʱ�ȭ
		time = 0;// �ð��ʱ�ȭ
		leftButtonNum = gameSize * gameSize;
		numOfFlag = numOfMine;
		flagLabel.setText("MINE : " + numOfFlag);
		showPanel.remove(timer);// Ÿ�̸������
		timer = new Timer();// ���� �ٽô� (������ ���θ� ���ؼ� �ʱ�ȭ�ϴ¹���� �𸣰ڽ��ϴ�..)
		showPanel.add(timer);

		MyButton.reset();
		MakeMine();
		inputdata1();
		inputdata2();
		check();
		gamePanel.revalidate();
		timer.revalidate();
	}

	// ���� �δ콺
	public void MakeMine() {
		// ���ڰ���
		mineLocation = new int[numOfMine];
		// ��������(������ġ)
		Random r = new Random();
		for (int i = 0; i < mineLocation.length; i++) {
			mineLocation[i] = r.nextInt(gameSize * gameSize);
			for (int j = 0; j < i; j++) {
				if (mineLocation[i] == mineLocation[j]) {
					i--;
					break;
				}
			}
		}
	}

	// ��ư�����Է� 1
	public void inputdata1() {
		button = new MyButton[gameSize * gameSize];
		for (int i = 0; i < gameSize * gameSize; i++) {
			button[i] = new MyButton();
			// button[i].setPreferredSize(new Dimension(50, 50));
			button[i].setFont(new Font("�ü�ü", Font.BOLD, 10));
			// ��ư�� �����߰�
			for (int k = 0; k < mineLocation.length; k++) {
				if (button[i].index == mineLocation[k])
					button[i].mine = true;
			}
			// äũ
			if (button[i].mine == true) {
				System.out.print(button[i].index + " ");
			}
			button[i].addMouseListener(new MyActionListener());
			gamePanel.add(button[i]);
		}

	}

	// ��ư���� ���ڰ���
	public void inputdata2() {
		for (int m = 0; m < gameSize * gameSize; m++) {
			int num = 0;
			// ��ư ��ġ���� ����� �޶�..
			// buttonSurround[] =
			// {1,gameSize+1,gameSize,gameSize-1,-1,-gameSize-1,-gameSize,-gameSize+1};
			if (button[m].mine == false) {
				// case 1 �»��
				if (m == 0) {
					for (int i = 0; i <= 2; i++) {
						if (button[m + buttonSurround[i]].mine == true)
							num++;
					}
					button[m].surroundMineNum = num;
					continue;
				}
				// case 2 ���
				if ((m >= 1) && (m <= gameSize - 2)) {
					for (int i = 0; i <= 4; i++) {
						if (button[m + buttonSurround[i]].mine == true)
							num++;
					}
					button[m].surroundMineNum = num;
					continue;
				}
				// case 3 ����
				if (m == gameSize - 1) {
					for (int i = 2; i <= 4; i++) {
						if (button[m + buttonSurround[i]].mine == true)
							num++;
					}
					button[m].surroundMineNum = num;
					continue;
				}
				// case 4 ����
				if ((m % gameSize == 0) && m != gameSize * (gameSize - 1)) {
					for (int i = 0; i <= 2; i++) {
						if (button[m + buttonSurround[i]].mine == true)
							num++;
					}
					for (int i = 6; i <= 7; i++) {
						if (button[m + buttonSurround[i]].mine == true)
							num++;
					}
					button[m].surroundMineNum = num;
					continue;
				}
				// case 6 ����
				if (((m % gameSize) == (gameSize - 1)) && (m != gameSize * gameSize - 1)) {
					for (int i = 2; i <= 6; i++) {
						if (button[m + buttonSurround[i]].mine == true)
							num++;
					}
					button[m].surroundMineNum = num;
					continue;
				}
				// case 7 ���ϴ�
				if (m == gameSize * (gameSize - 1)) {
					if (button[m + buttonSurround[0]].mine == true)
						num++;
					for (int i = 6; i <= 7; i++) {
						if (button[m + buttonSurround[i]].mine == true)
							num++;
					}
					button[m].surroundMineNum = num;
					continue;
				}
				// case 8 �ϴ�
				if (m >= gameSize * (gameSize - 1) + 1 && m <= gameSize * gameSize - 2) {
					if (button[m + buttonSurround[0]].mine == true)
						num++;
					for (int i = 4; i <= 7; i++) {
						if (button[m + buttonSurround[i]].mine == true)
							num++;
					}
					button[m].surroundMineNum = num;
					continue;
				}
				// case 9 ���ϴ�
				if (m == gameSize * gameSize - 1) {
					for (int i = 4; i <= 6; i++) {
						if (button[m + buttonSurround[i]].mine == true)
							num++;
					}
					button[m].surroundMineNum = num;
					continue;
				}
				// case 5 �߾�
				for (int i = 0; i <= 7; i++) {
					if (button[m + buttonSurround[i]].mine == true)
						num++;
				}
				button[m].surroundMineNum = num;
			}
		}
	}

	// ��ġ���� ����� 3�ù������ �ð��������
	public void MakebuttonSurround() {
		buttonSurround[0] = 1;
		buttonSurround[1] = gameSize + 1;
		buttonSurround[2] = gameSize;
		buttonSurround[3] = gameSize - 1;
		buttonSurround[4] = -1;
		buttonSurround[5] = -gameSize - 1;
		buttonSurround[6] = -gameSize;
		buttonSurround[7] = -gameSize + 1;
	}

	// ��ư�Է� �̺�Ʈ
	class MyActionListener extends MouseAdapter {
		public void mouseReleased(MouseEvent e) {
			MyButton b = (MyButton) e.getSource();
			if (b.isEnabled() == true) {// �ȴ������¿���..
				if (e.isPopupTrigger() == true) {// ��Ŭ���̺�Ʈ <���, ?>ǥ��
					if (b.flag == 2) {// ���ǥ���ϱ�
						b.flag = 0;
						b.clickCheck = true;
						numOfFlag--;
						flagLabel.setText("MINE : " + numOfFlag);
						winOption();
						repaint();
					} else if (b.flag == 0) {// ����ǥ ǥ���ϱ�
						b.flag = 1;
						numOfFlag++;
						flagLabel.setText("MINE : " + numOfFlag);
						repaint();
					} else {// ó������
						b.flag = 2;
						b.clickCheck = false;
						repaint();
					}
				} else {// �׳� Ŭ��
					if (b.flag == 2) {
						if (b.mine == true) {// ���ڸ� Ŭ��������...
							state = 2;// ����
							b.clickCheck = true;
							clickAllButton();
							timer.finish();
							repaint();
						}
						if (b.mine == false) {
							if (b.surroundMineNum == 0) {// 0�ϰ�� ������ äũ
								b.setEnabled(false);
								b.clickCheck = true;
								leftButtonNum--;
								checkSurround(b.index);
								winOption();
							} else {
								b.setText(b.surroundMineNum + "");
								leftButtonNum--;
								b.clickCheck = true;
								b.setEnabled(false);
								winOption();
							}
						}
					}
				}
			}
			System.out.println("left button:" + leftButtonNum);
		}
	}

	// �¸�����
	public void winOption() {
		int count = 0;
		for (int i = 0; i < gameSize * gameSize; i++) {
			if (button[i].flag == 0 && button[i].mine == true) // �������� ���ǥ���� ���
				count++;
			if ((count + leftButtonNum) == numOfMine) {// �¸����� ���� = ���� ���� + ���� ��ư�Ǽ��� ������ ������ ������
				state = 1;// �¸�����
				repaint();
				timer.finish();
				clickAllButton();
				// �����Է��г�
				new game1FinishFrame();

				count = 0;
				break;
			}
		}
	}

	// ��� ��ư ������(�¸� or �й�)
	public void clickAllButton() {
		for (int i = 0; i < gameSize * gameSize; i++) {// ��� ��ư�� ..
			if (state == 1)
				button[i].gamewin = true;
			button[i].gamefinish = true;// ��������
			if (button[i].surroundMineNum != 0)// ������ ���� ǥ��
				button[i].setText(button[i].surroundMineNum + "");
			button[i].setEnabled(false);
		}
	}

	// {1,gameSize+1,gameSize,gameSize-1,-1,-gameSize-1,-gameSize,-gameSize+1};
	// ��ư�� 0�ϰ�� ����äũ
	// �����Լ� ���� ���̱�
	public void simplecheck(int index, int i) {
		int num = index + buttonSurround[i];

		if (button[num].clickCheck == false) {// �ȴ����� ������
			if (button[num].surroundMineNum == 0) {
				button[num].clickCheck = true;
				button[num].setEnabled(false);
				leftButtonNum--;
				checkSurround(num);
			} else {
				button[num].setText(button[num].surroundMineNum + "");
				button[num].clickCheck = true;
				button[num].setEnabled(false);
				leftButtonNum--;
			}
		} else
			return;
	}

	public void checkSurround(int index) {
		if (index == 0) {// case 1 �»��
			for (int i = 0; i <= 2; i++) {
				simplecheck(index, i);
			}
		} else if ((index >= 1) && (index <= gameSize - 2)) {// case 2 ���
			for (int i = 0; i <= 4; i++) {
				simplecheck(index, i);
			}
		} else if (index == gameSize - 1) {// case 3 ����
			for (int i = 2; i <= 4; i++) {
				simplecheck(index, i);
			}
		} else if ((index % gameSize == 0) && index != 0 && index != gameSize * (gameSize - 1)) {// case 4 ����
			for (int i = 0; i <= 2; i++) {
				simplecheck(index, i);
			}
			for (int i = 6; i <= 7; i++) {
				simplecheck(index, i);
			}
		} else if (((index % gameSize) == (gameSize - 1)) && (index != gameSize * gameSize - 1)
				&& (index != gameSize - 1)) {// case 6 ����
			for (int i = 2; i <= 6; i++) {
				simplecheck(index, i);
			}
		} else if (index == gameSize * (gameSize - 1)) {// case 7 ���ϴ�
			simplecheck(index, 0);
			for (int i = 6; i <= 7; i++) {
				simplecheck(index, i);
			}
		} else if (index >= gameSize * (gameSize - 1) + 1 && index <= gameSize * gameSize - 2) {// case 8 �ϴ�
			simplecheck(index, 0);
			for (int i = 4; i <= 7; i++) {
				simplecheck(index, i);
			}
		} else if (index == gameSize * gameSize - 1) { // case 9 ���ϴ�
			for (int i = 4; i <= 6; i++) {
				simplecheck(index, i);
			}
		} else {// case 5 �߾�
			for (int i = 0; i <= 7; i++) {
				simplecheck(index, i);
			}
		}
	}

	// ���¹�ư // ǥ���׸�
	class Reset extends JButton {
		int x, y;

		public Reset() {
			this.setPreferredSize(new Dimension(48, 48));
		}

		public void paintComponent(Graphics g) {
			x = this.getWidth();
			y = this.getHeight();
			super.paintComponent(g);
			if (state == 0) {// �Ϲ� ǥ��
				g.setColor(Color.YELLOW);
				g.fillOval(0, 0, x, y);
				g.setColor(Color.BLACK);
				g.fillRect(x / 3 - 3, y / 3, 6, 6);
				g.fillRect((x * 2) / 3 - 3, y / 3, 6, 6);
				g.drawArc(x / 3, y / 2, x / 3, y / 3, 180, 180);
			} else if (state == 1) {// ����!! ǥ��
				g.setColor(Color.YELLOW);
				g.fillOval(0, 0, x, y);
				g.setColor(Color.BLACK);
				g.drawArc(x / 6, y / 6, x / 3, y / 3, 0, 180);
				g.drawArc(x / 2, y / 6, x / 3, y / 3, 0, 180);
				g.drawArc(x / 3, y / 2, x / 3, y / 3, 180, 180);
			} else {// ���� ǥ��..
				g.setColor(Color.YELLOW);
				g.fillOval(0, 0, x, y);
				g.setColor(Color.BLACK);
				g.drawLine(x / 6, y / 6, x / 2, y / 2);
				g.drawLine(x / 2, y / 2, (x * 5) / 6, y / 6);
				g.drawLine(x / 6, y / 2, x / 2, y / 6);
				g.drawLine(x / 2, y / 6, (x * 5) / 6, y / 2);
				g.drawLine(x / 3, (y * 5) / 6, (x * 2) / 3, (y * 5) / 6);
			}
		}
	}

	// Ÿ�̸� ����
	class Timer extends JPanel {
		private boolean flag = false;

		public void finish() {
			flag = true;
		}

		public Timer() {
			setLayout(new FlowLayout());
			JLabel timerLabel = new JLabel();
			timerLabel.setFont(new Font("Gothic", Font.PLAIN, 30));
			TimerThread th = new TimerThread(timerLabel);
			add(timerLabel);
			th.start();
		}

		class TimerThread extends Thread {
			private JLabel timerLabel;

			public TimerThread(JLabel timerLabel) {
				this.timerLabel = timerLabel;
			}

			public void run() {
				while (flag == false) {
					timerLabel.setText("TIME : " + Integer.toString(time));
					time++;
					try {
						sleep(1000);
						if (flag == true)
							return;
					} catch (InterruptedException e) {
						return;
					}
				}
			}
		}
	}

	// ���� ������
	class game1FinishFrame extends JFrame {
		JPanel panel[] = new JPanel[3];
		JLabel infoLabel = new JLabel("�̸��� �Է��ϼ���");

		public game1FinishFrame() {
			setTitle("����ã��");
			setLayout(new GridLayout(3, 0));

			// �¸�!! �г�
			panel[0] = new JPanel();
			JLabel winLabel = new JLabel("�¸�!!");
			winLabel.setFont(new Font("����ü", Font.BOLD, 30));
			panel[0].add(winLabel);

			// �̸��Է� �г�
			panel[1] = new JPanel();
			panel[1].add(infoLabel);

			JTextField nameField = new JTextField(20);
			nameField.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JTextField jf = (JTextField) e.getSource();
					player = jf.getText();
					// �̸��� �Է��ؾ� �ٽ��ϱⰡ��
					resetButton.setEnabled(true);

					// "score1.txt"�� ����
					try {
						File f = new File("score1.txt");
						FileWriter fw = new FileWriter(f, true);
						fw.write((time - 1) + " " + player + " " + gameSize + "\r\n");// �ɸ��ð� , �̸� , ���ӻ�����
						fw.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					nameField.setEditable(false);// �̸��� �ѹ��� �Է°���
					infoLabel.setText("����Ǿ����ϴ�.");
				}
			});
			panel[1].add(nameField);

			panel[2] = new JPanel();
			resetButton.addActionListener(new buttonlistener());
			resetButton.setEnabled(false);
			goStart.addActionListener(new buttonlistener());
			exit.addActionListener(new buttonlistener());
			panel[2].add(resetButton);
			panel[2].add(goStart);
			panel[2].add(exit);

			add(panel[0]);
			add(panel[1]);
			add(panel[2]);

			setSize(300, 200);
			setVisible(true);
		}

		class buttonlistener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == resetButton) {
					restart();
					dispose();
				} else if (e.getSource() == goStart) {// ���۸޴�
					new GameStart();
					mygame1.dispose();
					dispose();

				} else
					System.exit(1);
			}

		}
	}
}
