package maJong;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Screen extends JFrame implements ActionListener {

	public static void main(String[] args) {
//		-----------initial environment setting start from here--------------
		JFrame frame = new JFrame();
		frame.setSize(920, 750);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close screen setting
		frame.setTitle("麻將三缺一");
		frame.setLayout(null); // figure position setting
		frame.getContentPane().setBackground(Color.WHITE); // background color .setBackground(Color.WHITE)

		// show players

		// front player
		ImageIcon front_player = new ImageIcon();
		try {
			front_player = new ImageIcon(ImageIO
					.read(new File("D:\\Users\\USER\\eclipse-workspace\\MaJong\\src\\maJong\\mahjong_pic\\back.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		front_player.setImage(front_player.getImage().getScaledInstance(35, 55, Image.SCALE_DEFAULT));
		
		JLabel[] lb = new JLabel[17];
		for (int i = 0; i < 17; i++) {
			lb[i] = new JLabel(front_player);
			frontplayer_handcard_coordination(lb[i], i);
			frame.add(lb[i]);
		}

		// right player
		ImageIcon right_player = new ImageIcon();
		try {
			right_player = new ImageIcon(ImageIO.read(
					new File("D:\\Users\\USER\\eclipse-workspace\\MaJong\\src\\maJong\\mahjong_pic\\backrotate.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		right_player.setImage(right_player.getImage().getScaledInstance(50, 30, Image.SCALE_DEFAULT));

		JLabel[] lb2 = new JLabel[17];
		for (int i = 0; i < 17; i++) {
			lb2[i] = new JLabel(right_player);
			rightplayer_handcard_coordination(lb2[i], i);
			frame.add(lb2[i]);
		}

		// left player
		ImageIcon left_player = new ImageIcon();
		try {
			left_player = new ImageIcon(ImageIO.read(
					new File("D:\\Users\\USER\\eclipse-workspace\\MaJong\\src\\maJong\\mahjong_pic\\backrotate.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		left_player.setImage(left_player.getImage().getScaledInstance(50, 30, Image.SCALE_DEFAULT));

		JLabel[] lb3 = new JLabel[17];
		for (int i = 0; i < 17; i++) {
			lb3[i] = new JLabel(left_player);
			leftplayer_handcard_coordination(lb3[i], i);
			frame.add(lb3[i]);
		}
		
		//eat pong gong hoo button
		JButton btn_eat = new JButton();
		btn_eat.setBounds(780, 600, 50, 50);
		btn_eat.setBackground(Color.BLUE);
		btn_eat.setBorderPainted(false);
		btn_eat.setText("吃");
		btn_eat.setFont(new Font("吃", Font.BOLD, 16));
		btn_eat.setForeground(Color.WHITE);
		
		JButton btn_gong = new JButton();
		btn_gong.setBounds(780, 655, 50, 50);
		btn_gong.setBackground(Color.ORANGE);
		btn_gong.setBorderPainted(false);
		btn_gong.setText("槓");
		btn_gong.setFont(new Font("槓", Font.BOLD, 16));
		btn_gong.setForeground(Color.WHITE);
		
		JButton btn_pong = new JButton();
		btn_pong.setBounds(835, 600, 50, 50);
		btn_pong.setBackground(Color.GREEN);
		btn_pong.setBorderPainted(false);
		btn_pong.setText("碰");
		btn_pong.setFont(new Font("碰", Font.BOLD, 16));
		btn_pong.setForeground(Color.WHITE);
		
		JButton btn_hoo = new JButton();
		btn_hoo.setBounds(835, 655, 50, 50);
		btn_hoo.setBackground(Color.RED);
		btn_hoo.setBorderPainted(false);
		btn_hoo.setText("胡");
		btn_hoo.setFont(new Font("胡", Font.BOLD, 16));
		btn_hoo.setForeground(Color.WHITE);
		
		frame.add(btn_eat);
		frame.add(btn_gong);
		frame.add(btn_pong);
		frame.add(btn_hoo);

//      ---------------------------------------------------
		// put discard card to pool
		JPanel pool = new JPanel(); // discard cards pool
		pool.setSize(450, 320);
		pool.setLocation(250, 160);
		String[] test_discard_card = { "DW" }; // just testing, please input the pattern of String type!!!

		for (int i = 0; i < test_discard_card.length; i++) {
			card_to_pool(pool, test_discard_card[i]);
		}
		frame.add(pool);

//		-----initial environment setting end------------

		// eat,pong,gong show up
		String[] popcard = { "D4", "D5", "D3", "D3" }; // just testing, please input pattern of String[] type of
														// eat/pong/gong card
		hint_showup(popcard, frame);
		sort_eat(popcard, frame);

//		-----------------------------------------------------------

		// just testing(handcard), in reverse order, do this whenever you want to sort
		// handcard
		String[] handcard = { "nn", "B2", "B4", "B5", "B6", "C5", "C8", "C9", "D1", "D6", "D9", "WW", "WS", "DG", "DG",
				"DW", "WN" };

		sort_handcard(handcard, frame);
//		------------------------------------------------------------

		frame.setVisible(true); // show screen

	}// main end

	// get address array
	public static String find_address(String handcard) {
		Map<String, String> map_B = new HashMap<>();
		Map<String, String> map_C = new HashMap<>();
		Map<String, String> map_D = new HashMap<>();
		Map<String, String> map_W = new HashMap<>();

		try {
			map_B.put("B1", "MaJong\\src\\maJong\\mahjong_pic\\B1.jpg");
			map_B.put("B2", "MaJong\\src\\maJong\\mahjong_pic\\B2.jpg");
			map_B.put("B3", "MaJong\\src\\maJong\\mahjong_pic\\B3.jpg");
			map_B.put("B4", "MaJong\\src\\maJong\\mahjong_pic\\B4.jpg");
			map_B.put("B5", "MaJong\\src\\maJong\\mahjong_pic\\B5.jpg");
			map_B.put("B6", "MaJong\\src\\maJong\\mahjong_pic\\B6.jpg");
			map_B.put("B7", "MaJong\\src\\maJong\\mahjong_pic\\B7.jpg");
			map_B.put("B8", "MaJong\\src\\maJong\\mahjong_pic\\B8.jpg");
			map_B.put("B9", "MaJong\\src\\maJong\\mahjong_pic\\B9.jpg");
			map_C.put("C1", "MaJong\\src\\maJong\\mahjong_pic\\C1.jpg");
			map_C.put("C2", "MaJong\\src\\maJong\\mahjong_pic\\C2.jpg");
			map_C.put("C3", "MaJong\\src\\maJong\\mahjong_pic\\C3.jpg");
			map_C.put("C4", "MaJong\\src\\maJong\\mahjong_pic\\C4.jpg");
			map_C.put("C5", "MaJong\\src\\maJong\\mahjong_pic\\C5.jpg");
			map_C.put("C6", "MaJong\\src\\maJong\\mahjong_pic\\C6.jpg");
			map_C.put("C7", "MaJong\\src\\maJong\\mahjong_pic\\C7.jpg");
			map_C.put("C8", "MaJong\\src\\maJong\\mahjong_pic\\C8.jpg");
			map_C.put("C9", "MaJong\\src\\maJong\\mahjong_pic\\C9.jpg");
			map_D.put("D1", "MaJong\\src\\maJong\\mahjong_pic\\D1.jpg");
			map_D.put("D2", "MaJong\\src\\maJong\\mahjong_pic\\D2.jpg");
			map_D.put("D3", "MaJong\\src\\maJong\\mahjong_pic\\D3.jpg");
			map_D.put("D4", "MaJong\\src\\maJong\\mahjong_pic\\D4.jpg");
			map_D.put("D5", "MaJong\\src\\maJong\\mahjong_pic\\D5.jpg");
			map_D.put("D6", "MaJong\\src\\maJong\\mahjong_pic\\D6.jpg");
			map_D.put("D7", "MaJong\\src\\maJong\\mahjong_pic\\D7.jpg");
			map_D.put("D8", "MaJong\\src\\maJong\\mahjong_pic\\D8.jpg");
			map_D.put("D9", "MaJong\\src\\maJong\\mahjong_pic\\D9.jpg");
			map_D.put("DR", "MaJong\\src\\maJong\\mahjong_pic\\DR.jpg");
			map_D.put("DW", "MaJong\\src\\maJong\\mahjong_pic\\DW.png");
			map_D.put("DG", "MaJong\\src\\maJong\\mahjong_pic\\DG.jpg");
			map_W.put("WW", "MaJong\\src\\maJong\\mahjong_pic\\WW.jpg");
			map_W.put("WE", "MaJong\\src\\maJong\\mahjong_pic\\WE.jpg");
			map_W.put("WN", "MaJong\\src\\maJong\\mahjong_pic\\WN.jpg");
			map_W.put("WS", "MaJong\\src\\maJong\\mahjong_pic\\WS.jpg");
		} catch (Exception e) {
			e.printStackTrace();
		}

		String address = null;

		if (handcard.charAt(0) == 'B')
			address = map_B.get(handcard);
		else if (handcard.charAt(0) == 'C')
			address = map_C.get(handcard);
		else if (handcard.charAt(0) == 'D')
			address = map_D.get(handcard);
		else if (handcard.charAt(0) == 'W')
			address = map_W.get(handcard);
		else if (handcard.charAt(0) == 'n')
			address = "nn";

		return address;

	}

	// get button array
	public static JButton get_button(String address) {
		JButton btn = new JButton();
		if (address.equals("nn")) // empty space return null
			btn = null;
		else {
			try {
				ImageIcon icon = new ImageIcon(ImageIO.read(new File(address)));
				icon.setImage(icon.getImage().getScaledInstance(40, 60, Image.SCALE_DEFAULT)); // 改變icon大小
				btn = new JButton(icon);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return btn;
	}

	// initial handcard location display
	public static void display(JButton btn, JFrame frame, int i) {
		handcard_coordination(btn, i);
		frame.add(btn);
	}

	// hint(eat,pong,gong) pop up
	public static void pop_up(JButton btn, JFrame frame, int i) {
		hint_coordination(btn, i);
		frame.add(btn);
	}

	// hint(eat,pong,gong) sort
	public static void eat_show(JButton btn, JFrame frame, int i) {
		eat_coordination(btn, i);
		frame.add(btn);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	// coordination of front player hand card
	public static void frontplayer_handcard_coordination(JLabel lb, int i) {
		int[][] coordinate1 = { { 160, 5 }, { 195, 5 }, { 230, 5 }, { 265, 5 }, { 300, 5 }, { 335, 5 }, { 370, 5 },
				{ 405, 5 }, { 440, 5 }, { 475, 5 }, { 510, 5 }, { 545, 5 }, { 580, 5 }, { 615, 5 }, { 650, 5 },
				{ 685, 5 }, { 720, 5 }, { 755, 5 } };
		lb.setBounds(coordinate1[i][0], coordinate1[i][1], 40, 60);
	}

	// coordination of right player hand card
	public static void rightplayer_handcard_coordination(JLabel lb, int i) {
		int[][] coordinate1 = { { 840, 80 }, { 840, 110 }, { 840, 140 }, { 840, 170 }, { 840, 200 }, { 840, 230 },
				{ 840, 260 }, { 840, 290 }, { 840, 320 }, { 840, 350 }, { 840, 380 }, { 840, 410 }, { 840, 440 },
				{ 840, 470 }, { 840, 500 }, { 840, 530 }, { 840, 560 }, { 840, 590 } };
		lb.setBounds(coordinate1[i][0], coordinate1[i][1], 60, 40);
	}

	// coordination of left player hand card
	public static void leftplayer_handcard_coordination(JLabel lb, int i) {
		int[][] coordinate1 = { { 5, 80 }, { 5, 110 }, { 5, 140 }, { 5, 170 }, { 5, 200 }, { 5, 230 }, { 5, 260 },
				{ 5, 290 }, { 5, 320 }, { 5, 350 }, { 5, 380 }, { 5, 410 }, { 5, 440 }, { 5, 470 }, { 5, 500 },
				{ 5, 530 }, { 5, 560 }, { 5, 590 } };
		lb.setBounds(coordinate1[i][0], coordinate1[i][1], 60, 40);
	}

	// initial coordination of player card(from R to L)
	public static void handcard_coordination(JButton btn, int i) {
		int[][] coordinate1 = {  { 720, 650 }, { 680, 650 }, { 640, 650 }, { 600, 650 }, { 560, 650 },
				{ 520, 650 }, { 480, 650 }, { 440, 650 }, { 400, 650 }, { 360, 650 }, { 320, 650 }, { 280, 650 },
				{ 240, 650 }, { 200, 650 }, { 160, 650 }, { 120, 650 }, { 80, 650 },{40,650} };
		btn.setBounds(coordinate1[i][0], coordinate1[i][1], 40, 60);
	}

	// coordination of hint player card
	public static void hint_coordination(JButton btn, int i) {
		int[][] coordinate1 = { { 680, 580 }, { 640, 580 }, { 600, 580 }, { 560, 580 }, { 520, 580 },
				{ 480, 580 }, { 440, 580 }, { 400, 580 }, { 360, 580 }, { 320, 580 }, { 280, 580 },{240,580} };
		btn.setBounds(coordinate1[i][0], coordinate1[i][1], 40, 60);
	}

	// eat coordination
	public static void eat_coordination(JButton btn, int i) {
		int[][] coordinate1 = { { 680, 515 }, { 640, 515 }, { 600, 515 }, { 560, 515 }, { 520, 515 },
				{ 480, 515 }, { 440, 515 }, { 400, 515 }, { 360, 515 }, { 320, 515 }, { 280, 515 }, { 240, 515 },
				{ 200, 515 }, { 160, 515 }, { 120, 515 }, { 80, 515 }, { 40, 515 },{0,515} };
		btn.setBounds(coordinate1[i][0], coordinate1[i][1], 40, 60);
	}

	// put discard card to pool
	public static JPanel card_to_pool(JPanel pool, String pattern) {
		JLabel[][] discard = new JLabel[12][6]; // 72 discard cards capacity
		String[][] discard_address = { { "0", "0", "0", "0", "0", "0" }, { "0", "0", "0", "0", "0", "0" },
				{ "0", "0", "0", "0", "0", "0" }, { "0", "0", "0", "0", "0", "0" }, { "0", "0", "0", "0", "0", "0" },
				{ "0", "0", "0", "0", "0", "0" }, { "0", "0", "0", "0", "0", "0" }, { "0", "0", "0", "0", "0", "0" },
				{ "0", "0", "0", "0", "0", "0" }, { "0", "0", "0", "0", "0", "0" }, { "0", "0", "0", "0", "0", "0" },
				{ "0", "0", "0", "0", "0", "0" } };

		for (int i = 0; i < discard_address.length; i++) {
			int count = 0; // count if column is full
			for (int j = 0; j < discard_address[i].length; j++) {
				if (discard_address[i][j].equals("0")) {
					discard_address[i][j] = find_address(pattern); // "WW" represents discard card in String type
					ImageIcon temp = new ImageIcon();
					try {
						temp = new ImageIcon(ImageIO.read(new File(discard_address[i][j])));
						temp.setImage(temp.getImage().getScaledInstance(30, 45, Image.SCALE_DEFAULT));
					} catch (IOException e) {
						e.printStackTrace();
					}
					discard[i][j] = new JLabel(temp);
					pool.add(discard[i][j]);
					break;
				} else
					count++;
			}
			if (count == 5)
				continue;
			else
				break;
		}
		return pool;
	}

	// eat,pong,gong show up
	public static void hint_showup(String[] popcard, JFrame frame) {
		// get popcard address
		String[] temp_address = new String[popcard.length];
		for (int i = 0; i < popcard.length; i++)
			temp_address[i] = find_address(popcard[i]);

		// get button array
		JButton[] temp_btn = new JButton[popcard.length];
		for (int i = 0; i < temp_address.length; i++)
			temp_btn[i] = get_button(temp_address[i]);

		// initial handcard display(reverse)
		for (int i = 0; i < temp_btn.length; i++)
			pop_up(temp_btn[i], frame, i);
	}

	// sort hand card
	public static void sort_handcard(String[] handcard, JFrame frame) {
		// get handcard address
		String[] address = new String[handcard.length]; // address array
		for (int i = 0; i < handcard.length; i++)
			address[i] = find_address(handcard[i]);

		// get button array
		JButton[] btn = new JButton[handcard.length];
		for (int i = 0; i < address.length; i++)
			btn[i] = get_button(address[i]);

		// initial handcard display(reverse)
		int count = 0;
		for (int i = 0; i < btn.length; i++) {
			if (i == 0) // keep the right empty place
				count++;
			else if (btn[i] == null) // skip empty space
				continue;
			else {
				display(btn[i], frame, count);
				count++;
			}
		}
	}

	// sort eat,pong,gong
	public static void sort_eat(String[] handcard, JFrame frame) {
		// get handcard address
		String[] address = new String[handcard.length]; // address array
		for (int i = 0; i < handcard.length; i++)
			address[i] = find_address(handcard[i]);

		// get button array
		JButton[] btn = new JButton[handcard.length];
		for (int i = 0; i < address.length; i++)
			btn[i] = get_button(address[i]);

		// initial handcard display(reverse)
		int count = 0;
		for (int i = 0; i < btn.length; i++) {
			if (btn[i] == null) // skip empty space
				continue;
			else {
				eat_show(btn[i], frame, count);
				count++;
			}
		}
	}

}
