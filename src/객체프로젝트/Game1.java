///////////////////////
//////지최찾기프레임///////
///////////////////////
package 객체프로젝트;

import javax.swing.*;

import 객체프로젝트.Game2.game2FinishFrame.buttonlistener;

import java.lang.Thread;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Random;

class MyButton extends JButton {

	private static int num = 0;
	int x, y;// 버튼 크기저장
	int index;// 버튼의 번호
	int flag = 2; // 0 이면 깃발표시 1이면 물음표 2이면 표시 X
	int surroundMineNum = 0;// 주위에있는 지뢰의 개수

	boolean gamefinish = false;// 게임끝나면 true
	boolean clickCheck = false;// 버튼을 클릭했는지 확인
	boolean mine = false;// 지뢰인지 아닌지
	boolean gamewin = false;// 게임성공하면 true

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
		if (clickCheck == true && mine == true && flag == 2) {// 지뢰를 클릭
			this.setBackground(Color.RED);
			g.fillOval(x / 4, y / 4, x / 2, y / 2);
			g.drawLine(x / 4, y / 4, (x * 3) / 4, (y * 3) / 4);
			g.drawLine((x * 3) / 4, y / 4, x / 4, (y * 3) / 4);
		} else if (gamefinish == true && mine == true && flag == 2 && gamewin == false) {// 졌을때 종료후 출력
			g.fillOval(x / 4, y / 4, x / 2, y / 2);
			g.drawLine(x / 4, y / 4, (x * 3) / 4, (y * 3) / 4);
			g.drawLine((x * 3) / 4, y / 4, x / 4, (y * 3) / 4);
		} else if (flag == 0 && clickCheck == true && mine == false && gamefinish == true) {// 잘못된 깃발표기
			g.fillOval(x / 4, y / 4, x / 2, y / 2);
			g.drawLine(x / 4, y / 4, (x * 3) / 4, (y * 3) / 4);
			g.drawLine((x * 3) / 4, y / 4, x / 4, (y * 3) / 4);
			g.setColor(Color.RED);
			g.drawLine(0, 0, x, y);
			g.drawLine(x, 0, 0, y);
		} else if (flag == 0 || (gamefinish == true && mine == true && flag == 2 && gamewin == true)) {// 깃발 마크
			g.setColor(Color.RED);
			g.fillRect(0, 0, x / 2, y / 2);
			g.setColor(Color.BLACK);
			g.drawLine(x / 2, 0, x / 2, y);
			g.fillRect(0, (y * 3) / 4, x, y);
		} else if (flag == 1) {// 물음표 표기
			g.setFont(new Font("궁서체", Font.BOLD, 30));
			g.drawString("?", x / 3, (y * 3) / 4);
		}
	}
}

public class Game1 extends JFrame {
	JFrame mygame1 = this;

	// 현제상태 0 일반 1성공 2실패
	private int state = 0;
	// 게임하는사람
	private String player;
	// 번호달린 버튼 생성
	private MyButton button[];
	private Reset resetbutton = new Reset();
	// 지뢰위치
	private int mineLocation[];
	// 게임크기, 지뢰개수, 길발갯수, 시간
	private int gameSize;
	private int numOfMine;
	private int numOfFlag;
	private int time = 0;
	private int leftButtonNum;
	// 위치 버튼 오른쪽부터 시계방향으로 빙글
	private int buttonSurround[] = new int[8];
	// 게임패널
	private JPanel gamePanel;
	// 정보출력패널
	private JPanel showPanel = new JPanel();
	private JLabel flagLabel = new JLabel();
	// 타이머
	Timer timer = new Timer();

	// 승리 프레임
	JButton resetButton = new JButton("다시하기");
	JButton goStart = new JButton("시작메뉴");
	JButton exit = new JButton("종료");

	public Game1(int size, int mineNum) {
		setTitle("Game1");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameSize = size;
		numOfMine = mineNum;
		numOfFlag = mineNum;
		leftButtonNum = size * size;

		// 지뢰생성
		MakeMine();
		// 버튼주위인댁스확인용
		MakebuttonSurround();
		setLayout(new BorderLayout());
		// 설정화면 표시
		showPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		// 타이머
		flagLabel.setText("MINE : " + numOfFlag);
		flagLabel.setFont(new Font("Gothic", Font.PLAIN, 30));
		flagLabel.setBackground(Color.WHITE);
		flagLabel.setOpaque(true);
		showPanel.add(flagLabel);
		// 게임리셋
		resetbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				restart();
			}
		});
		showPanel.setBackground(Color.DARK_GRAY);
		showPanel.add(resetbutton);

		showPanel.add(timer);
		// 게임패널설정
		gamePanel = new JPanel();
		gamePanel.setLayout(new GridLayout(gameSize, gameSize));
		// 버튼 정보 입력 1
		inputdata1();
		// 버튼 정보 입력2
		inputdata2();
		// 채크
		check();
		// 패널부착
		add(showPanel, BorderLayout.NORTH);
		add(gamePanel, BorderLayout.CENTER);
		setSize(gameSize * 45, gameSize * 45);

		setVisible(true);
	}

	// 버튼확인(핵)
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

	// 재시작!
	public void restart() {
		gamePanel.removeAll();// 게임패널 초기화
		timer.finish();// 현제 타이머 종료
		state = 0;// 상태초기화
		time = 0;// 시간초기화
		leftButtonNum = gameSize * gameSize;
		numOfFlag = numOfMine;
		flagLabel.setText("MINE : " + numOfFlag);
		showPanel.remove(timer);// 타이머지우고
		timer = new Timer();// 새거 다시담 (스래드 공부를 덜해서 초기화하는방법을 모르겠습니다..)
		showPanel.add(timer);

		MyButton.reset();
		MakeMine();
		inputdata1();
		inputdata2();
		check();
		gamePanel.revalidate();
		timer.revalidate();
	}

	// 지뢰 인댁스
	public void MakeMine() {
		// 지뢰갯수
		mineLocation = new int[numOfMine];
		// 난수생성(지뢰위치)
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

	// 버튼정보입력 1
	public void inputdata1() {
		button = new MyButton[gameSize * gameSize];
		for (int i = 0; i < gameSize * gameSize; i++) {
			button[i] = new MyButton();
			// button[i].setPreferredSize(new Dimension(50, 50));
			button[i].setFont(new Font("궁서체", Font.BOLD, 10));
			// 버튼에 지뢰추가
			for (int k = 0; k < mineLocation.length; k++) {
				if (button[i].index == mineLocation[k])
					button[i].mine = true;
			}
			// 채크
			if (button[i].mine == true) {
				System.out.print(button[i].index + " ");
			}
			button[i].addMouseListener(new MyActionListener());
			gamePanel.add(button[i]);
		}

	}

	// 버튼주위 지뢰갯수
	public void inputdata2() {
		for (int m = 0; m < gameSize * gameSize; m++) {
			int num = 0;
			// 버튼 위치마다 계산이 달라서..
			// buttonSurround[] =
			// {1,gameSize+1,gameSize,gameSize-1,-1,-gameSize-1,-gameSize,-gameSize+1};
			if (button[m].mine == false) {
				// case 1 좌상단
				if (m == 0) {
					for (int i = 0; i <= 2; i++) {
						if (button[m + buttonSurround[i]].mine == true)
							num++;
					}
					button[m].surroundMineNum = num;
					continue;
				}
				// case 2 상단
				if ((m >= 1) && (m <= gameSize - 2)) {
					for (int i = 0; i <= 4; i++) {
						if (button[m + buttonSurround[i]].mine == true)
							num++;
					}
					button[m].surroundMineNum = num;
					continue;
				}
				// case 3 우상단
				if (m == gameSize - 1) {
					for (int i = 2; i <= 4; i++) {
						if (button[m + buttonSurround[i]].mine == true)
							num++;
					}
					button[m].surroundMineNum = num;
					continue;
				}
				// case 4 좌측
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
				// case 6 우측
				if (((m % gameSize) == (gameSize - 1)) && (m != gameSize * gameSize - 1)) {
					for (int i = 2; i <= 6; i++) {
						if (button[m + buttonSurround[i]].mine == true)
							num++;
					}
					button[m].surroundMineNum = num;
					continue;
				}
				// case 7 좌하단
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
				// case 8 하단
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
				// case 9 우하단
				if (m == gameSize * gameSize - 1) {
					for (int i = 4; i <= 6; i++) {
						if (button[m + buttonSurround[i]].mine == true)
							num++;
					}
					button[m].surroundMineNum = num;
					continue;
				}
				// case 5 중앙
				for (int i = 0; i <= 7; i++) {
					if (button[m + buttonSurround[i]].mine == true)
						num++;
				}
				button[m].surroundMineNum = num;
			}
		}
	}

	// 위치정보 만들기 3시방향부터 시계방향으로
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

	// 버튼입력 이벤트
	class MyActionListener extends MouseAdapter {
		public void mouseReleased(MouseEvent e) {
			MyButton b = (MyButton) e.getSource();
			if (b.isEnabled() == true) {// 안눌린상태여만..
				if (e.isPopupTrigger() == true) {// 우클릭이벤트 <깃발, ?>표기
					if (b.flag == 2) {// 깃발표시하기
						b.flag = 0;
						b.clickCheck = true;
						numOfFlag--;
						flagLabel.setText("MINE : " + numOfFlag);
						winOption();
						repaint();
					} else if (b.flag == 0) {// 물음표 표시하기
						b.flag = 1;
						numOfFlag++;
						flagLabel.setText("MINE : " + numOfFlag);
						repaint();
					} else {// 처음으로
						b.flag = 2;
						b.clickCheck = false;
						repaint();
					}
				} else {// 그냥 클릭
					if (b.flag == 2) {
						if (b.mine == true) {// 지뢰를 클릭했을때...
							state = 2;// 실패
							b.clickCheck = true;
							clickAllButton();
							timer.finish();
							repaint();
						}
						if (b.mine == false) {
							if (b.surroundMineNum == 0) {// 0일경우 주위를 채크
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

	// 승리조건
	public void winOption() {
		int count = 0;
		for (int i = 0; i < gameSize * gameSize; i++) {
			if (button[i].flag == 0 && button[i].mine == true) // 지뢰위에 깃발표시할 경우
				count++;
			if ((count + leftButtonNum) == numOfMine) {// 승리조건 만족 = 위의 조건 + 남은 버튼의수가 지뢰의 갯수와 같을때
				state = 1;// 승리상태
				repaint();
				timer.finish();
				clickAllButton();
				// 정보입력패널
				new game1FinishFrame();

				count = 0;
				break;
			}
		}
	}

	// 모든 버튼 누르기(승리 or 패배)
	public void clickAllButton() {
		for (int i = 0; i < gameSize * gameSize; i++) {// 모든 버튼을 ..
			if (state == 1)
				button[i].gamewin = true;
			button[i].gamefinish = true;// 게임종료
			if (button[i].surroundMineNum != 0)// 주위의 지뢰 표시
				button[i].setText(button[i].surroundMineNum + "");
			button[i].setEnabled(false);
		}
	}

	// {1,gameSize+1,gameSize,gameSize-1,-1,-gameSize-1,-gameSize,-gameSize+1};
	// 버튼이 0일경우 주위채크
	// 밑의함수 길이 줄이기
	public void simplecheck(int index, int i) {
		int num = index + buttonSurround[i];

		if (button[num].clickCheck == false) {// 안눌려져 있을때
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
		if (index == 0) {// case 1 좌상단
			for (int i = 0; i <= 2; i++) {
				simplecheck(index, i);
			}
		} else if ((index >= 1) && (index <= gameSize - 2)) {// case 2 상단
			for (int i = 0; i <= 4; i++) {
				simplecheck(index, i);
			}
		} else if (index == gameSize - 1) {// case 3 우상단
			for (int i = 2; i <= 4; i++) {
				simplecheck(index, i);
			}
		} else if ((index % gameSize == 0) && index != 0 && index != gameSize * (gameSize - 1)) {// case 4 좌측
			for (int i = 0; i <= 2; i++) {
				simplecheck(index, i);
			}
			for (int i = 6; i <= 7; i++) {
				simplecheck(index, i);
			}
		} else if (((index % gameSize) == (gameSize - 1)) && (index != gameSize * gameSize - 1)
				&& (index != gameSize - 1)) {// case 6 우측
			for (int i = 2; i <= 6; i++) {
				simplecheck(index, i);
			}
		} else if (index == gameSize * (gameSize - 1)) {// case 7 좌하단
			simplecheck(index, 0);
			for (int i = 6; i <= 7; i++) {
				simplecheck(index, i);
			}
		} else if (index >= gameSize * (gameSize - 1) + 1 && index <= gameSize * gameSize - 2) {// case 8 하단
			simplecheck(index, 0);
			for (int i = 4; i <= 7; i++) {
				simplecheck(index, i);
			}
		} else if (index == gameSize * gameSize - 1) { // case 9 우하단
			for (int i = 4; i <= 6; i++) {
				simplecheck(index, i);
			}
		} else {// case 5 중앙
			for (int i = 0; i <= 7; i++) {
				simplecheck(index, i);
			}
		}
	}

	// 리셋버튼 // 표정그림
	class Reset extends JButton {
		int x, y;

		public Reset() {
			this.setPreferredSize(new Dimension(48, 48));
		}

		public void paintComponent(Graphics g) {
			x = this.getWidth();
			y = this.getHeight();
			super.paintComponent(g);
			if (state == 0) {// 일반 표정
				g.setColor(Color.YELLOW);
				g.fillOval(0, 0, x, y);
				g.setColor(Color.BLACK);
				g.fillRect(x / 3 - 3, y / 3, 6, 6);
				g.fillRect((x * 2) / 3 - 3, y / 3, 6, 6);
				g.drawArc(x / 3, y / 2, x / 3, y / 3, 180, 180);
			} else if (state == 1) {// 성공!! 표정
				g.setColor(Color.YELLOW);
				g.fillOval(0, 0, x, y);
				g.setColor(Color.BLACK);
				g.drawArc(x / 6, y / 6, x / 3, y / 3, 0, 180);
				g.drawArc(x / 2, y / 6, x / 3, y / 3, 0, 180);
				g.drawArc(x / 3, y / 2, x / 3, y / 3, 180, 180);
			} else {// 실패 표정..
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

	// 게임 끝난후
	class game1FinishFrame extends JFrame {
		JPanel panel[] = new JPanel[3];
		JLabel infoLabel = new JLabel("이름을 입력하세요");

		public game1FinishFrame() {
			setTitle("지뢰찾기");
			setLayout(new GridLayout(3, 0));

			// 승리!! 패널
			panel[0] = new JPanel();
			JLabel winLabel = new JLabel("승리!!");
			winLabel.setFont(new Font("굴림체", Font.BOLD, 30));
			panel[0].add(winLabel);

			// 이름입력 패널
			panel[1] = new JPanel();
			panel[1].add(infoLabel);

			JTextField nameField = new JTextField(20);
			nameField.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JTextField jf = (JTextField) e.getSource();
					player = jf.getText();
					// 이름을 입력해야 다시하기가능
					resetButton.setEnabled(true);

					// "score1.txt"에 저장
					try {
						File f = new File("score1.txt");
						FileWriter fw = new FileWriter(f, true);
						fw.write((time - 1) + " " + player + " " + gameSize + "\r\n");// 걸린시간 , 이름 , 게임사이즈
						fw.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					nameField.setEditable(false);// 이름은 한번만 입력가능
					infoLabel.setText("저장되었습니다.");
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
				} else if (e.getSource() == goStart) {// 시작메뉴
					new GameStart();
					mygame1.dispose();
					dispose();

				} else
					System.exit(1);
			}

		}
	}
}
