////////////////////
//////오목프레임///////
////////////////////
package 객체프로젝트;

import javax.swing.*;

import 객체프로젝트.Game1.Timer.TimerThread;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

class MyLabel extends JLabel {
	int x, y;// 버튼위치
	int state = 0;// 0 이면 빈칸, 1이면 검은돌, 2이면 흰돌
	int time;// 몇번째수인지 저장(물리기 구현)
	int size;

	// 시험용
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

		// 판 격자
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

		// 돌
		if (state == 0 && mouseOn) {
			g.setColor(Color.BLACK);
			g.drawOval(x / 8, y / 8, (x * 3) / 4, (y * 3) / 4);
			return;
		}

		if (state == 1) {// 검은돌
			g.fillOval(x / 8, y / 8, (x * 3) / 4, (y * 3) / 4);
			g.setColor(Color.WHITE);
			g.drawOval(x / 8, y / 8, (x * 3) / 4, (y * 3) / 4);
			return;
		}
		if (state == 2) {// 흰돌
			g.setColor(Color.WHITE);
			g.fillOval(x / 8, y / 8, (x * 3) / 4, (y * 3) / 4);
			g.setColor(Color.BLACK);
			g.drawOval(x / 8, y / 8, (x * 3) / 4, (y * 3) / 4);
			return;
		}
	}
}

class Player {// 게임플레이어
	String name;
	int turn;
	boolean win = false;// 이기면true
	boolean black;// 검은돌이면 true 아니면 white
}

public class Game2 extends JFrame {
	JFrame myGame2 = this;
	private ImageIcon image = new ImageIcon("test.jpg");
	Image img = image.getImage();
	int gameSize;// 게임사이즈

	// 게임설정
	private boolean turn = true;// true검은돌 false흰돌
	private int soo = 0;// 지금 몇수인지?
	private int turnTime = 0;// 이건 몇번째 둔 돌인지?(물리기 사용)
	private boolean gameFinish = false;// 끝나면 true
	// 오목알+판
	private MyLabel omok[][];
	// 오목판 패널
	private panel gamePanel;

	class panel extends JPanel {// 뒤에 배경 나무무늬..
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
		}
	}

	private JPanel showPanel;
	// 물리기버튼
	private JButton backButton = new JButton("무르기");
	Player player1 = new Player();
	Player player2 = new Player();
	// 승리
	Player winner = new Player();

	// 밑에 창에 보여주는 기능
	private JLabel showName1, showName2, showTurn;
	private MyLabel show[] = new MyLabel[2];

	// 타이머
	Timer timer = new Timer();
	private int time = 10;// 타이머 10초

	public Game2(int size, String name1, String name2) {
		setTitle("Game2");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameSize = size;
		player1.name = name1;
		player2.name = name2;

		// 왼쪽 검을돌 중앙 오목판 오른쪽 흰돌
		setLayout(new BorderLayout());

		// 순서정하기
		whoIsFirst();
		// 게임창만들기
		madeGamePanel();
		// 표시창만들기
		madeShowPanel();

		// 타이머패널
		JPanel timerPanel = new JPanel();
		timerPanel.setLayout(new FlowLayout());
		timerPanel.add(timer);

		add(timerPanel, BorderLayout.NORTH);
		add(gamePanel, BorderLayout.CENTER);
		add(showPanel, BorderLayout.SOUTH);
		setSize(700, 700);
		setVisible(true);
	}

	// 순서정하기 함수
	public void whoIsFirst() {
		int coin = (int) (Math.random() * 2);

		System.out.println(coin);
		if (coin == 1) {
			System.out.println(player1.name + ": 검은돌 / " + player2.name + ": 흰돌");
			player1.black = true;
			player2.black = false;
		} else {
			System.out.println(player1.name + ": 흰돌 / " + player2.name + ": 검은돌");
			player1.black = false;
			player2.black = true;
		}
	}

	// 게임창만들기
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
							showTurn.setText("Turn : " + soo);// 수 표시
							if (turn == true) {// 검은돌두기
								la.state = 1;
								winCheck(1, la);// 검을돌 승리채크
							} else {// 흰색돌 두기
								la.state = 2;
								winCheck(2, la);// 흰돌 승리채크
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

	// 표시창만들기
	public void madeShowPanel() {
		showPanel = new JPanel();// 검은돌 이용자가 왼쪽표시
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
		// 턴마다 색 표시
		showName1.setBackground(Color.YELLOW);
		showName2.setBackground(Color.YELLOW);
		showName1.setOpaque(true);
		showName2.setOpaque(false);

		// 검은돌 플레이어 > 턴 > 흰돌 플레이어
		showPanel.add(showName1);
		showPanel.add(show[0]);
		showPanel.add(showTurn);
		showPanel.add(show[1]);
		showPanel.add(showName2);
		showPanel.setSize(700, 200);

		backButton.addActionListener(new ActionListener() {// 물리기 버튼
			public void actionPerformed(ActionEvent e) {
				if (soo != 0) {// 버튼활성화상태
					for (int i = 0; i < gameSize; i++) {
						for (int j = 0; j < gameSize; j++) {
							if (omok[i][j].time == turnTime) {// 가장 최근에 둔 돌 찾기
								omok[i][j].state = 0;// 빈칸상태로 만들고
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

	// 턴바꾸기
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
		time = 10;// 시간초기화
	}

	// 승리조건
	public void winCheck(int state, MyLabel omokT) {// 검은돌 1 흰돌 2
		int x = omokT.x;
		int y = omokT.y;
		System.out.println("trun : " + turn);
		check세로(x, y, state);
		check가로(x, y, state);
		check대각1(x, y, state);
		check대각2(x, y, state);
		if (gameFinish == true) {

			timer.finish();// 타이머 멈춤

			if (state == 1) {// 검은돌이 이김
				if (player1.black == true)
					winner = player1;
				else
					winner = player2;
			} else {// 흰돌이 이김
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
				fw.write(winner.name + " " + winner.turn + "\r\n");// 승리자 이름, 턴(수) 입력
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 새로채크
	private void check세로(int x, int y, int state) {// 세로채크
		for (int i = 0; i < (gameSize / 2) + 1; i++) {// 세로확인 위에서 절반
			if (omok[i][y].state == state && omok[i + 1][y].state == state && omok[i + 2][y].state == state
					&& omok[i + 3][y].state == state && omok[i + 4][y].state == state) {
				if (omok[i + 5][y].state == state) {
					System.out.println("6목");
					return;
				}
				System.out.println("Finish" + "state : " + state);
				gameFinish = true;
				return;
			}
		}
		for (int i = gameSize - 1; i > (gameSize / 2) - 1; i--) {// 세로확인 아래서 절반
			if (omok[i][y].state == state && omok[i - 1][y].state == state && omok[i - 2][y].state == state
					&& omok[i - 3][y].state == state && omok[i - 4][y].state == state) {
				if (omok[i - 5][y].state == state) {
					System.out.println("6목");
					return;
				}
				System.out.println("Finish" + "state : " + state);
				gameFinish = true;
				return;
			}
		}
	}

	// 가로채크
	private void check가로(int x, int y, int state) {// 가로체크
		for (int i = 0; i < (gameSize / 2) + 1; i++) {// 가로확인 왼쪽에서 절반
			if (omok[x][i].state == state && omok[x][i + 1].state == state && omok[x][i + 2].state == state
					&& omok[x][i + 3].state == state && omok[x][i + 4].state == state) {
				if (omok[x][i + 5].state == state) {
					System.out.println("6목");
					return;
				}
				System.out.println("Finish" + "state : " + state);
				gameFinish = true;
				return;
			}
		}
		for (int i = gameSize - 1; i > (gameSize / 2) - 1; i--) {// 가로확인 오른쪽에서 절반
			if (omok[x][i].state == state && omok[x][i - 1].state == state && omok[x][i - 2].state == state
					&& omok[x][i - 3].state == state && omok[x][i - 4].state == state) {
				if (omok[x][i - 5].state == state) {
					System.out.println("6목");
					return;
				}
				System.out.println("Finish" + "state : " + state);
				gameFinish = true;
				return;
			}
		}
	}

	// 대각성 1 채크
	private void check대각1(int x, int y, int state) {// </ 방향
		int start = x + y;
		int check;

		if (start < gameSize) {// 좌상단
			if (start == 4) {// 딱 드러맞을때
				if (omok[4][0].state == state && omok[3][1].state == state && omok[2][2].state == state
						&& omok[1][3].state == state && omok[0][4].state == state) {
					System.out.println("Finish" + "state : " + state);
					gameFinish = true;
					return;
				}
			}
			check = (int) Math.ceil((double) (start - 3) / 2);// 방향당 체크횟수
			for (int i = 0; i < check; i++) {// 밑에서 위로
				if (omok[start - i][i].state == state && omok[start - i - 1][i + 1].state == state
						&& omok[start - i - 2][i + 2].state == state && omok[start - i - 3][i + 3].state == state
						&& omok[start - i - 4][i + 4].state == state) {
					if (omok[start - i - 5][i + 5].state == state) {// 6목
						System.out.println("6목");
						return;
					}
					System.out.println("Finish" + "state : " + state);
					gameFinish = true;
					return;
				}
			}
			for (int i = 0; i < check; i++) {// 위에서 아래로
				if (omok[i][start - i].state == state && omok[i + 1][start - i - 1].state == state
						&& omok[i + 2][start - i - 2].state == state && omok[i + 3][start - i - 3].state == state
						&& omok[i + 4][start - i - 4].state == state) {
					if (omok[i + 5][start - i - 5].state == state) {// 6목
						System.out.println("6목");
						return;
					}
					System.out.println("Finish" + "state : " + state);
					gameFinish = true;
					return;
				}
			}

		} else {// 우하단
			if (start > gameSize * 2 - 6)// 오목 발생 X
				return;
			if (start == gameSize * 2 - 6) {// 딱 드러맞을때
				if (omok[gameSize - 5][gameSize - 1].state == state && omok[gameSize - 4][gameSize - 2].state == state
						&& omok[gameSize - 3][gameSize - 3].state == state
						&& omok[gameSize - 2][gameSize - 4].state == state
						&& omok[gameSize - 1][gameSize - 5].state == state) {
					System.out.println("Finish" + "state : " + state);
					gameFinish = true;
					return;
				}
			}

			check = (int) Math.ceil((double) ((gameSize * 2 - 5) - start) / 2);// 방향당 체크횟수
			for (int i = 0; i < check; i++) {// 밑에서 위로
				if (omok[gameSize - 1 - i][start - gameSize + 1 + i].state == state
						&& omok[gameSize - 2 - i][start - gameSize + 2 + i].state == state
						&& omok[gameSize - 3 - i][start - gameSize + 3 + i].state == state
						&& omok[gameSize - 4 - i][start - gameSize + 4 + i].state == state
						&& omok[gameSize - 5 - i][start - gameSize + 5 + i].state == state) {
					if (omok[gameSize - 6 - i][start - gameSize + 6 + i].state == state) {// 6목
						System.out.println("6목");
						return;
					}
					System.out.println("Finish" + "state : " + state);
					gameFinish = true;
					return;
				}
			}
			for (int i = 0; i < check; i++) {// 위에서 아래로
				if (omok[start - gameSize + 1 + i][gameSize - 1 - i].state == state
						&& omok[start - gameSize + 2 + i][gameSize - 2 - i].state == state
						&& omok[start - gameSize + 3 + i][gameSize - 3 - i].state == state
						&& omok[start - gameSize + 4 + i][gameSize - 4 - i].state == state
						&& omok[start - gameSize + 5 + i][gameSize - 5 - i].state == state) {
					if (omok[start - gameSize + 6 + i][gameSize - 6 - i].state == state) {// 6목
						System.out.println("6목");
						return;
					}
					System.out.println("Finish " + " state : " + state);
					gameFinish = true;
					return;
				}
			}

		}
	}

	// 대각선 2 채크
	private void check대각2(int x, int y, int state) {// \>방향
		int start = x - y;
		int check;

		if (start > 0) {// 좌하단
			if (start > gameSize - 5)
				return;
			if (start == gameSize - 5) {// 딱 드러맞을때
				if (omok[gameSize - 5][0].state == state && omok[gameSize - 4][1].state == state
						&& omok[gameSize - 3][2].state == state && omok[gameSize - 2][3].state == state
						&& omok[gameSize - 1][4].state == state) {
					System.out.println("Finish" + "state : " + state);
					gameFinish = true;
					return;
				}
			}
			check = (int) Math.ceil((double) (gameSize - start - 4) / 2);// 방향당 체크횟수
			for (int i = 0; i < check; i++) {// 위에서 아래로
				if (omok[start + i][i].state == state && omok[start + 1 + i][1 + i].state == state
						&& omok[start + 2 + i][2 + i].state == state && omok[start + 3 + i][3 + i].state == state
						&& omok[start + 4 + i][4 + i].state == state) {
					if (omok[start + 5 + i][5 + i].state == state) {
						System.out.println("6목");
						return;
					}
					System.out.println("Finish" + "state : " + state);
					gameFinish = true;
					return;
				}
			}
			for (int i = 0; i < check; i++) {// 아래서 위로
				if (omok[gameSize - 1 - i][gameSize - 1 - start - i].state == state
						&& omok[gameSize - 2 - i][gameSize - 2 - start - i].state == state
						&& omok[gameSize - 3 - i][gameSize - 3 - start - i].state == state
						&& omok[gameSize - 4 - i][gameSize - 4 - start - i].state == state
						&& omok[gameSize - 5 - i][gameSize - 5 - start - i].state == state) {
					if (omok[gameSize - 6 - i][gameSize - 6 - start - i].state == state) {
						System.out.println("6목");
						return;
					}
					System.out.println("Finish" + "state : " + state);
					gameFinish = true;
					return;
				}
			}

		} else {// 우상단
			start = -start;// 위치 반대로
			if (start > gameSize - 5)
				return;
			if (start == gameSize - 5) {// 딱 드러맞을때
				if (omok[0][gameSize - 5].state == state && omok[1][gameSize - 4].state == state
						&& omok[2][gameSize - 3].state == state && omok[3][gameSize - 2].state == state
						&& omok[4][gameSize - 1].state == state) {
					System.out.println("Finish" + "state : " + state);
					gameFinish = true;
					return;
				}
			}
			check = (int) Math.ceil((double) (gameSize - start - 4) / 2);// 방향당 체크횟수
			for (int i = 0; i < check; i++) {// 위에서 아래로
				if (omok[i][start + i].state == state && omok[1 + i][start + 1 + i].state == state
						&& omok[2 + i][start + 2 + i].state == state && omok[3 + i][start + 3 + i].state == state
						&& omok[4 + i][start + 4 + i].state == state) {
					if (omok[5 + i][start + 5 + i].state == state) {
						System.out.println("6목");
						return;
					}
					System.out.println("Finish" + "state : " + state);
					gameFinish = true;
					return;
				}
			}
			for (int i = 0; i < check; i++) {// 아래서 위로
				if (omok[gameSize - 1 - start - i][gameSize - 1 - i].state == state
						&& omok[gameSize - 2 - start - i][gameSize - 2 - i].state == state
						&& omok[gameSize - 3 - start - i][gameSize - 3 - i].state == state
						&& omok[gameSize - 4 - start - i][gameSize - 4 - i].state == state
						&& omok[gameSize - 5 - start - i][gameSize - 5 - i].state == state) {
					if (omok[gameSize - 6 - start - i][gameSize - 6 - i].state == state) {
						System.out.println("6목");
						return;
					}
					System.out.println("Finish" + "state : " + state);
					gameFinish = true;
					return;
				}
			}

		}
	}

	// 게임 끝난후
	class game2FinishFrame extends JFrame {
		JPanel panel[] = new JPanel[3];

		JButton resetButton = new JButton("다시하기");
		JButton goStart = new JButton("시작메뉴");
		JButton exit = new JButton("종료");

		public game2FinishFrame(Player winner) {
			setTitle("오목게임");
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setLayout(new GridLayout(3, 0));
			// 승리자 이름
			panel[0] = new JPanel();
			panel[0].add(new JLabel("승리 : " + winner.name));

			// 몇수만에 이겼는지
			panel[1] = new JPanel();
			panel[1].add(new JLabel(winner.turn + "수 승리!!"));

			// 버튼
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
				} else if (e.getSource() == goStart) {// 시작메뉴
					new GameStart();
					myGame2.dispose();
					dispose();

				} else
					System.exit(1);
			}

		}
	}

	// 타이머 설정
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
