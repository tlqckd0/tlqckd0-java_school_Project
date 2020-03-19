////////////////////
//////����������///////
////////////////////
package ��ü������Ʈ;

import javax.swing.*;

import ��ü������Ʈ.Game1.Timer.TimerThread;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

class MyLabel extends JLabel {
	int x, y;// ��ư��ġ
	int state = 0;// 0 �̸� ��ĭ, 1�̸� ������, 2�̸� ��
	int time;// ���°������ ����(������ ����)
	int size;

	// �����
	boolean mouseOn = false;//

	public MyLabel(int size, int x, int y) {
		this.x = x;
		this.y = y;
		this.size = size;
		setPreferredSize(new Dimension(30, 30));

	}

	public void paintComponent(Graphics g) {
		int x = this.getWidth();
		int y = this.getHeight();
		super.paintComponent(g);

		// �� ����
		if (this.y == 0 && this.x == 0) {
			g.drawLine(x / 2, y / 2, x, y / 2);
			g.drawLine(x / 2, y / 2, x / 2, y);
		} else if (this.y == 0 && this.x == size - 1) {
			g.drawLine(x / 2, y / 2, x, y / 2);
			g.drawLine(x / 2, y / 2, x / 2, 0);
		} else if (this.y == 0 && this.x > 0 && this.x < size - 1) {
			g.drawLine(x / 2, y / 2, x, y / 2);
			g.drawLine(x / 2, 0, x / 2, y);
		} else if (this.y == size - 1 && this.x == 0) {
			g.drawLine(x / 2, y / 2, 0, y / 2);
			g.drawLine(x / 2, y / 2, x / 2, y);
		} else if (this.y > 0 && this.y < size - 1 && this.x == 0) {
			g.drawLine(0, y / 2, x, y / 2);
			g.drawLine(x / 2, y / 2, x / 2, y);
		} else if (this.y == size - 1 && this.x == size - 1) {
			g.drawLine(x / 2, y / 2, 0, y / 2);
			g.drawLine(x / 2, y / 2, x / 2, 0);
		} else if (this.y == size - 1 && this.x > 0 && this.x < size - 1) {
			g.drawLine(0, y / 2, x / 2, y / 2);
			g.drawLine(x / 2, 0, x / 2, y);
		} else if (this.y > 0 && this.y < size - 1 && this.x == size - 1) {
			g.drawLine(0, y / 2, x, y / 2);
			g.drawLine(x / 2, 0, x / 2, y / 2);
		} else {
			g.drawLine(0, y / 2, x, y / 2);
			g.drawLine(x / 2, 0, x / 2, y);
		}

		// ��
		if (state == 0 && mouseOn) {
			g.setColor(Color.BLACK);
			g.drawOval(x / 8, y / 8, (x * 3) / 4, (y * 3) / 4);
			return;
		}

		if (state == 1) {// ������
			g.fillOval(x / 8, y / 8, (x * 3) / 4, (y * 3) / 4);
			g.setColor(Color.WHITE);
			g.drawOval(x / 8, y / 8, (x * 3) / 4, (y * 3) / 4);
			return;
		}
		if (state == 2) {// ��
			g.setColor(Color.WHITE);
			g.fillOval(x / 8, y / 8, (x * 3) / 4, (y * 3) / 4);
			g.setColor(Color.BLACK);
			g.drawOval(x / 8, y / 8, (x * 3) / 4, (y * 3) / 4);
			return;
		}
	}
}

class Player {// �����÷��̾�
	String name;
	int turn;
	boolean win = false;// �̱��true
	boolean black;// �������̸� true �ƴϸ� white
}

public class Game2 extends JFrame {
	JFrame myGame2 = this;
	private ImageIcon image = new ImageIcon("test.jpg");
	Image img = image.getImage();
	int gameSize;// ���ӻ�����

	// ���Ӽ���
	private boolean turn = true;// true������ false��
	private int soo = 0;// ���� �������?
	private int turnTime = 0;// �̰� ���° �� ������?(������ ���)
	private boolean gameFinish = false;// ������ true
	// �����+��
	private MyLabel omok[][];
	// ������ �г�
	private panel gamePanel;

	class panel extends JPanel {// �ڿ� ��� ��������..
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
		}
	}

	private JPanel showPanel;
	// �������ư
	private JButton backButton = new JButton("������");
	Player player1 = new Player();
	Player player2 = new Player();
	// �¸�
	Player winner = new Player();

	// �ؿ� â�� �����ִ� ���
	private JLabel showName1, showName2, showTurn;
	private MyLabel show[] = new MyLabel[2];

	// Ÿ�̸�
	Timer timer = new Timer();
	private int time = 10;// Ÿ�̸� 10��

	public Game2(int size, String name1, String name2) {
		setTitle("Game2");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameSize = size;
		player1.name = name1;
		player2.name = name2;

		// ���� ������ �߾� ������ ������ ��
		setLayout(new BorderLayout());

		// �������ϱ�
		whoIsFirst();
		// ����â�����
		madeGamePanel();
		// ǥ��â�����
		madeShowPanel();

		// Ÿ�̸��г�
		JPanel timerPanel = new JPanel();
		timerPanel.setLayout(new FlowLayout());
		timerPanel.add(timer);

		add(timerPanel, BorderLayout.NORTH);
		add(gamePanel, BorderLayout.CENTER);
		add(showPanel, BorderLayout.SOUTH);
		setSize(700, 700);
		setVisible(true);
	}

	// �������ϱ� �Լ�
	public void whoIsFirst() {
		int coin = (int) (Math.random() * 2);

		System.out.println(coin);
		if (coin == 1) {
			System.out.println(player1.name + ": ������ / " + player2.name + ": ��");
			player1.black = true;
			player2.black = false;
		} else {
			System.out.println(player1.name + ": �� / " + player2.name + ": ������");
			player1.black = false;
			player2.black = true;
		}
	}

	// ����â�����
	public void madeGamePanel() {
		gamePanel = new panel();
		gamePanel.setLayout(new GridLayout(gameSize, gameSize));

		omok = new MyLabel[gameSize][gameSize];
		for (int i = 0; i < gameSize; i++) {
			for (int j = 0; j < gameSize; j++) {
				omok[i][j] = new MyLabel(gameSize, i, j);
				omok[i][j].addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						MyLabel la = (MyLabel) e.getSource();
						if (la.state == 0) {
							turnTime++;
							soo++;
							showTurn.setText("Turn : " + soo);// �� ǥ��
							if (turn == true) {// �������α�
								la.state = 1;
								winCheck(1, la);// ������ �¸�äũ
							} else {// ����� �α�
								la.state = 2;
								winCheck(2, la);// �� �¸�äũ
							}
							changeTurn();
							System.out.println("\n");
							la.time = turnTime;
							backButton.setEnabled(true);
							repaint();
						}
					}

					public void mouseEntered(MouseEvent e) {
						MyLabel la = (MyLabel) e.getSource();
						la.mouseOn = true;
						repaint();
					}

					public void mouseExited(MouseEvent e) {
						MyLabel la = (MyLabel) e.getSource();
						la.mouseOn = false;
						repaint();
					}
				});
				gamePanel.add(omok[i][j]);
				repaint();
			}
		}
	}

	// ǥ��â�����
	public void madeShowPanel() {
		showPanel = new JPanel();// ������ �̿��ڰ� ����ǥ��
		show[0] = new MyLabel(0, 999, 999);
		show[0].state = 1;
		show[1] = new MyLabel(0, 999, 999);
		show[1].state = 2;
		showTurn = new JLabel("Turn : " + soo);
		if (player1.black == true) {
			showName1 = new JLabel(player1.name);
			showName2 = new JLabel(player2.name);
		} else {
			showName1 = new JLabel(player2.name);
			showName2 = new JLabel(player1.name);
		}
		// �ϸ��� �� ǥ��
		showName1.setBackground(Color.YELLOW);
		showName2.setBackground(Color.YELLOW);
		showName1.setOpaque(true);
		showName2.setOpaque(false);

		// ������ �÷��̾� > �� > �� �÷��̾�
		showPanel.add(showName1);
		showPanel.add(show[0]);
		showPanel.add(showTurn);
		showPanel.add(show[1]);
		showPanel.add(showName2);
		showPanel.setSize(700, 200);

		backButton.addActionListener(new ActionListener() {// ������ ��ư
			public void actionPerformed(ActionEvent e) {
				if (soo != 0) {// ��ưȰ��ȭ����
					for (int i = 0; i < gameSize; i++) {
						for (int j = 0; j < gameSize; j++) {
							if (omok[i][j].time == turnTime) {// ���� �ֱٿ� �� �� ã��
								omok[i][j].state = 0;// ��ĭ���·� �����
								omok[i][j].time = -1;
								changeTurn();
								soo--;
								showTurn.setText("Turn : " + soo);
								repaint();
							}
						}
					}
					backButton.setEnabled(false);
				}
			}
		});
		backButton.setEnabled(false);
		showPanel.add(backButton);
	}

	// �Ϲٲٱ�
	public void changeTurn() {
		if (turn == true) {
			showName1.setOpaque(false);
			showName2.setOpaque(true);
			turn = false;
		} else {
			showName1.setOpaque(true);
			showName2.setOpaque(false);
			turn = true;
		}
		System.out.println("change turn");
		time = 10;// �ð��ʱ�ȭ
	}

	// �¸�����
	public void winCheck(int state, MyLabel omokT) {// ������ 1 �� 2
		int x = omokT.x;
		int y = omokT.y;
		System.out.println("trun : " + turn);
		check����(x, y, state);
		check����(x, y, state);
		check�밢1(x, y, state);
		check�밢2(x, y, state);
		if (gameFinish == true) {

			timer.finish();// Ÿ�̸� ����

			if (state == 1) {// �������� �̱�
				if (player1.black == true)
					winner = player1;
				else
					winner = player2;
			} else {// ���� �̱�
				if (player1.black == false)
					winner = player1;
				else
					winner = player2;
			}
			winner.turn = soo;
			this.setEnabled(false);
			new game2FinishFrame(winner);

			try {
				File f = new File("score2.txt");
				FileWriter fw = new FileWriter(f, true);
				fw.write(winner.name + " " + winner.turn + "\r\n");// �¸��� �̸�, ��(��) �Է�
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// ����äũ
	private void check����(int x, int y, int state) {// ����äũ
		for (int i = 0; i < (gameSize / 2) + 1; i++) {// ����Ȯ�� ������ ����
			if (omok[i][y].state == state && omok[i + 1][y].state == state && omok[i + 2][y].state == state
					&& omok[i + 3][y].state == state && omok[i + 4][y].state == state) {
				if (omok[i + 5][y].state == state) {
					System.out.println("6��");
					return;
				}
				System.out.println("Finish" + "state : " + state);
				gameFinish = true;
				return;
			}
		}
		for (int i = gameSize - 1; i > (gameSize / 2) - 1; i--) {// ����Ȯ�� �Ʒ��� ����
			if (omok[i][y].state == state && omok[i - 1][y].state == state && omok[i - 2][y].state == state
					&& omok[i - 3][y].state == state && omok[i - 4][y].state == state) {
				if (omok[i - 5][y].state == state) {
					System.out.println("6��");
					return;
				}
				System.out.println("Finish" + "state : " + state);
				gameFinish = true;
				return;
			}
		}
	}

	// ����äũ
	private void check����(int x, int y, int state) {// ����üũ
		for (int i = 0; i < (gameSize / 2) + 1; i++) {// ����Ȯ�� ���ʿ��� ����
			if (omok[x][i].state == state && omok[x][i + 1].state == state && omok[x][i + 2].state == state
					&& omok[x][i + 3].state == state && omok[x][i + 4].state == state) {
				if (omok[x][i + 5].state == state) {
					System.out.println("6��");
					return;
				}
				System.out.println("Finish" + "state : " + state);
				gameFinish = true;
				return;
			}
		}
		for (int i = gameSize - 1; i > (gameSize / 2) - 1; i--) {// ����Ȯ�� �����ʿ��� ����
			if (omok[x][i].state == state && omok[x][i - 1].state == state && omok[x][i - 2].state == state
					&& omok[x][i - 3].state == state && omok[x][i - 4].state == state) {
				if (omok[x][i - 5].state == state) {
					System.out.println("6��");
					return;
				}
				System.out.println("Finish" + "state : " + state);
				gameFinish = true;
				return;
			}
		}
	}

	// �밢�� 1 äũ
	private void check�밢1(int x, int y, int state) {// </ ����
		int start = x + y;
		int check;

		if (start < gameSize) {// �»��
			if (start == 4) {// �� �巯������
				if (omok[4][0].state == state && omok[3][1].state == state && omok[2][2].state == state
						&& omok[1][3].state == state && omok[0][4].state == state) {
					System.out.println("Finish" + "state : " + state);
					gameFinish = true;
					return;
				}
			}
			check = (int) Math.ceil((double) (start - 3) / 2);// ����� üũȽ��
			for (int i = 0; i < check; i++) {// �ؿ��� ����
				if (omok[start - i][i].state == state && omok[start - i - 1][i + 1].state == state
						&& omok[start - i - 2][i + 2].state == state && omok[start - i - 3][i + 3].state == state
						&& omok[start - i - 4][i + 4].state == state) {
					if (omok[start - i - 5][i + 5].state == state) {// 6��
						System.out.println("6��");
						return;
					}
					System.out.println("Finish" + "state : " + state);
					gameFinish = true;
					return;
				}
			}
			for (int i = 0; i < check; i++) {// ������ �Ʒ���
				if (omok[i][start - i].state == state && omok[i + 1][start - i - 1].state == state
						&& omok[i + 2][start - i - 2].state == state && omok[i + 3][start - i - 3].state == state
						&& omok[i + 4][start - i - 4].state == state) {
					if (omok[i + 5][start - i - 5].state == state) {// 6��
						System.out.println("6��");
						return;
					}
					System.out.println("Finish" + "state : " + state);
					gameFinish = true;
					return;
				}
			}

		} else {// ���ϴ�
			if (start > gameSize * 2 - 6)// ���� �߻� X
				return;
			if (start == gameSize * 2 - 6) {// �� �巯������
				if (omok[gameSize - 5][gameSize - 1].state == state && omok[gameSize - 4][gameSize - 2].state == state
						&& omok[gameSize - 3][gameSize - 3].state == state
						&& omok[gameSize - 2][gameSize - 4].state == state
						&& omok[gameSize - 1][gameSize - 5].state == state) {
					System.out.println("Finish" + "state : " + state);
					gameFinish = true;
					return;
				}
			}

			check = (int) Math.ceil((double) ((gameSize * 2 - 5) - start) / 2);// ����� üũȽ��
			for (int i = 0; i < check; i++) {// �ؿ��� ����
				if (omok[gameSize - 1 - i][start - gameSize + 1 + i].state == state
						&& omok[gameSize - 2 - i][start - gameSize + 2 + i].state == state
						&& omok[gameSize - 3 - i][start - gameSize + 3 + i].state == state
						&& omok[gameSize - 4 - i][start - gameSize + 4 + i].state == state
						&& omok[gameSize - 5 - i][start - gameSize + 5 + i].state == state) {
					if (omok[gameSize - 6 - i][start - gameSize + 6 + i].state == state) {// 6��
						System.out.println("6��");
						return;
					}
					System.out.println("Finish" + "state : " + state);
					gameFinish = true;
					return;
				}
			}
			for (int i = 0; i < check; i++) {// ������ �Ʒ���
				if (omok[start - gameSize + 1 + i][gameSize - 1 - i].state == state
						&& omok[start - gameSize + 2 + i][gameSize - 2 - i].state == state
						&& omok[start - gameSize + 3 + i][gameSize - 3 - i].state == state
						&& omok[start - gameSize + 4 + i][gameSize - 4 - i].state == state
						&& omok[start - gameSize + 5 + i][gameSize - 5 - i].state == state) {
					if (omok[start - gameSize + 6 + i][gameSize - 6 - i].state == state) {// 6��
						System.out.println("6��");
						return;
					}
					System.out.println("Finish " + " state : " + state);
					gameFinish = true;
					return;
				}
			}

		}
	}

	// �밢�� 2 äũ
	private void check�밢2(int x, int y, int state) {// \>����
		int start = x - y;
		int check;

		if (start > 0) {// ���ϴ�
			if (start > gameSize - 5)
				return;
			if (start == gameSize - 5) {// �� �巯������
				if (omok[gameSize - 5][0].state == state && omok[gameSize - 4][1].state == state
						&& omok[gameSize - 3][2].state == state && omok[gameSize - 2][3].state == state
						&& omok[gameSize - 1][4].state == state) {
					System.out.println("Finish" + "state : " + state);
					gameFinish = true;
					return;
				}
			}
			check = (int) Math.ceil((double) (gameSize - start - 4) / 2);// ����� üũȽ��
			for (int i = 0; i < check; i++) {// ������ �Ʒ���
				if (omok[start + i][i].state == state && omok[start + 1 + i][1 + i].state == state
						&& omok[start + 2 + i][2 + i].state == state && omok[start + 3 + i][3 + i].state == state
						&& omok[start + 4 + i][4 + i].state == state) {
					if (omok[start + 5 + i][5 + i].state == state) {
						System.out.println("6��");
						return;
					}
					System.out.println("Finish" + "state : " + state);
					gameFinish = true;
					return;
				}
			}
			for (int i = 0; i < check; i++) {// �Ʒ��� ����
				if (omok[gameSize - 1 - i][gameSize - 1 - start - i].state == state
						&& omok[gameSize - 2 - i][gameSize - 2 - start - i].state == state
						&& omok[gameSize - 3 - i][gameSize - 3 - start - i].state == state
						&& omok[gameSize - 4 - i][gameSize - 4 - start - i].state == state
						&& omok[gameSize - 5 - i][gameSize - 5 - start - i].state == state) {
					if (omok[gameSize - 6 - i][gameSize - 6 - start - i].state == state) {
						System.out.println("6��");
						return;
					}
					System.out.println("Finish" + "state : " + state);
					gameFinish = true;
					return;
				}
			}

		} else {// ����
			start = -start;// ��ġ �ݴ��
			if (start > gameSize - 5)
				return;
			if (start == gameSize - 5) {// �� �巯������
				if (omok[0][gameSize - 5].state == state && omok[1][gameSize - 4].state == state
						&& omok[2][gameSize - 3].state == state && omok[3][gameSize - 2].state == state
						&& omok[4][gameSize - 1].state == state) {
					System.out.println("Finish" + "state : " + state);
					gameFinish = true;
					return;
				}
			}
			check = (int) Math.ceil((double) (gameSize - start - 4) / 2);// ����� üũȽ��
			for (int i = 0; i < check; i++) {// ������ �Ʒ���
				if (omok[i][start + i].state == state && omok[1 + i][start + 1 + i].state == state
						&& omok[2 + i][start + 2 + i].state == state && omok[3 + i][start + 3 + i].state == state
						&& omok[4 + i][start + 4 + i].state == state) {
					if (omok[5 + i][start + 5 + i].state == state) {
						System.out.println("6��");
						return;
					}
					System.out.println("Finish" + "state : " + state);
					gameFinish = true;
					return;
				}
			}
			for (int i = 0; i < check; i++) {// �Ʒ��� ����
				if (omok[gameSize - 1 - start - i][gameSize - 1 - i].state == state
						&& omok[gameSize - 2 - start - i][gameSize - 2 - i].state == state
						&& omok[gameSize - 3 - start - i][gameSize - 3 - i].state == state
						&& omok[gameSize - 4 - start - i][gameSize - 4 - i].state == state
						&& omok[gameSize - 5 - start - i][gameSize - 5 - i].state == state) {
					if (omok[gameSize - 6 - start - i][gameSize - 6 - i].state == state) {
						System.out.println("6��");
						return;
					}
					System.out.println("Finish" + "state : " + state);
					gameFinish = true;
					return;
				}
			}

		}
	}

	// ���� ������
	class game2FinishFrame extends JFrame {
		JPanel panel[] = new JPanel[3];

		JButton resetButton = new JButton("�ٽ��ϱ�");
		JButton goStart = new JButton("���۸޴�");
		JButton exit = new JButton("����");

		public game2FinishFrame(Player winner) {
			setTitle("�������");
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setLayout(new GridLayout(3, 0));
			// �¸��� �̸�
			panel[0] = new JPanel();
			panel[0].add(new JLabel("�¸� : " + winner.name));

			// ������� �̰����
			panel[1] = new JPanel();
			panel[1].add(new JLabel(winner.turn + "�� �¸�!!"));

			// ��ư
			panel[2] = new JPanel();
			resetButton.addActionListener(new buttonlistener());
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
					new Game2(gameSize, player1.name, player2.name);
					myGame2.dispose();
					dispose();
				} else if (e.getSource() == goStart) {// ���۸޴�
					new GameStart();
					myGame2.dispose();
					dispose();

				} else
					System.exit(1);
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
					time--;
					try {
						if (time == 0)
							changeTurn();
						sleep(1000);
					} catch (InterruptedException e) {
						return;
					}
				}
			}
		}
	}
}
