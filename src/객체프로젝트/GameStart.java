///////////////////////
//////���Ӽ���������///////
///////////////////////
package ��ü������Ʈ;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
//��ư
import java.util.HashMap;
import java.util.Iterator;

//����ã�� ����
class game1Score {
	int time;
	int gameSize;
	String name;
}

//���� ����
class game2Score {
	int time;
	String name;
}

public class GameStart extends JFrame {
	private GameStart myGameStart = this;
	// �г�
	private JPanel selectPanel, topPanel, bottomPanel;
	// ��ư
	private JButton game1 = new JButton("����ã��");
	private JButton game2 = new JButton("����");
	private JButton score = new JButton("����Ȯ��");
	private JButton close = new JButton("����");

	public GameStart() {
		setTitle("Game Selection");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		game1.setFont(new Font("����ü", Font.PLAIN, 50));
		game2.setFont(new Font("����ü", Font.PLAIN, 50));
		// �����г�
		selectPanel = new JPanel();
		selectPanel.setLayout(new GridLayout(0, 2));
		game1.addActionListener(new buttonListener());
		game2.addActionListener(new buttonListener());
		selectPanel.add(game1);
		selectPanel.add(game2);

		// �����г�
		topPanel = new JPanel();
		JLabel jl = new JLabel("������ �����ϼ���");
		topPanel.add(jl);

		// ����, ����Ȯ�� �г�
		bottomPanel = new JPanel();
		score.addActionListener(new buttonListener());
		close.addActionListener(new buttonListener());
		bottomPanel.add(score);
		bottomPanel.add(close);

		// �г��߰�
		add(selectPanel, BorderLayout.CENTER);
		add(topPanel, BorderLayout.NORTH);
		add(bottomPanel, BorderLayout.SOUTH);
		setSize(500, 300);
		setVisible(true);
	}

	// ���Ӽ����̺�Ʈ
	class buttonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton jb = (JButton) e.getSource();
			if (jb == game1) {// 1������(����ã��)
				new InfoFrame(0);
				myGameStart.setVisible(false);
			} else if (jb == game2) {// 2������(����)
				// new InfoFrame(1);
				new InfoFrame(1);
				myGameStart.setVisible(false);
			} else if (jb == score) {// ����Ȯ��
				new scorePanel();

			} else
				System.exit(1);
		}
	}

	static public void main(String[] args) {
		GameStart game = new GameStart();
	}

	// ��������г�
	class scorePanel extends JFrame {

		JPanel select = new JPanel();
		JPanel score = new JPanel();
		JPanel topPanel = new JPanel();
		JTextArea game1ScoreArea = new JTextArea();
		JTextArea game2ScoreArea = new JTextArea();

		JButton returnButton = new JButton("���ư���");
		JButton resetScore = new JButton("���� �ʱ�ȭ");

		public TreeMap<Integer, game1Score> score1 = new TreeMap<Integer, game1Score>();
		public TreeMap<Integer, game2Score> score2 = new TreeMap<Integer, game2Score>();

		public scorePanel() {
			setTitle("����");
			setLayout(new BorderLayout());
			game1ScoreArea.setEditable(false);
			game2ScoreArea.setEditable(false);
			returnButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {// ����â �ݱ�
					dispose();
				}
			});
			resetScore.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {// ���� �ʱ�ȭ
					File f1, f2;
					f1 = new File("score1.txt");
					f2 = new File("score2.txt");
					FileWriter fw;
					try {
						fw = new FileWriter(f1);
						fw.write("");
						fw = new FileWriter(f2);
						fw.write("");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					game1ScoreArea.setText("");
					game2ScoreArea.setText("");
				}
			});
			select.add(resetScore);
			select.add(returnButton);

			score.setLayout(new GridLayout(1, 2));
			readScore();// text���Ͽ��� ���� �д� �޼ҵ�

			topPanel.setLayout(new GridLayout(2, 2));// ���� �˸��ǰ�����..
			topPanel.add(new JLabel("����ã��"));
			topPanel.add(new JLabel("����"));
			topPanel.add(new JLabel("no. name           time         size"));
			topPanel.add(new JLabel("no. name            turn"));

			score.add(game1ScoreArea);
			score.add(game2ScoreArea);
			add(topPanel, BorderLayout.NORTH);
			add(score, BorderLayout.CENTER);
			add(select, BorderLayout.SOUTH);

			setSize(450, 400);
			setVisible(true);
		}

		public void readScore() {
			File f1 = new File("score1.txt"), f2 = new File("score2.txt");
			Object[] obj;
			game1Score temp1;
			game2Score temp2;
			try {// ����ã�� ���� �б�
				obj = new Object[3];
				FileReader fr = new FileReader(f1);
				BufferedReader BF = new BufferedReader(fr);
				String line = "";
				while ((line = BF.readLine()) != null) {
					temp1 = new game1Score();
					obj = line.split(" ");
					temp1.time = Integer.parseInt((String) obj[0]);
					temp1.name = (String) obj[1];
					temp1.gameSize = Integer.parseInt((String) obj[2]);

					score1.put(temp1.time, temp1);// ����ã�� Ʈ���� Ű�� �ð�
				}
				fr.close();
				// �� �������� �б�
				obj = new Object[2];
				fr = new FileReader(f2);
				BF = new BufferedReader(fr);
				line = "";
				while ((line = BF.readLine()) != null) {
					temp2 = new game2Score();
					obj = line.split(" ");
					temp2.name = (String) obj[0];
					temp2.time = Integer.parseInt((String) obj[1]);

					score2.put(temp2.time, temp2);// ���� Ʈ���� Ű�� ����
				}
				fr.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			Iterator<Integer> treeMapIter = score1.keySet().iterator();
			int i = 0;
			while (treeMapIter.hasNext()) {
				i++;
				Integer key = treeMapIter.next();
				game1Score value = score1.get(key);
				game1ScoreArea.append(i + " : " + value.name + "\t" + key + "��           " + value.gameSize + "\r\n");

			}

			treeMapIter = score2.keySet().iterator();
			i = 0;
			while (treeMapIter.hasNext()) {
				i++;
				Integer key = treeMapIter.next();
				game2Score value = score2.get(key);
				game2ScoreArea.append(i + " : " + value.name + "\t" + key + "��\r\n");

			}
		}
	}

}
