package game;

import java.awt.Color;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JOptionPane;
import java.awt.Container;
import java.awt.FlowLayout;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.util.concurrent.*;
import static java.util.concurrent.TimeUnit.SECONDS;


/*
 * 1. 타이머로 종료되게
 * 2. 벌은 먹으면 점수 줄어들기
 * 3. 먹으면 아이템도 등장하고 싶음*/
public class VoidEatGame extends JFrame {
	private Image bufferImage;
	private Graphics screenGraphic;
	int count=0;
	
	private Clip clip;
	String resultStr = null;
	
	private Image background = new ImageIcon("src/images/background.png").getImage();
	private Image player = new ImageIcon("src/images/doll1.png").getImage();
	private Image coin = new ImageIcon("src/images/orange1.png").getImage();
	private Image coin2 = new ImageIcon("src/images/orange2.png").getImage();
	private Image coin3 = new ImageIcon("src/images/orange3.png").getImage();
	private Image coin4 = new ImageIcon("src/images/orange4.png").getImage();
	private Image bee = new ImageIcon("src/images/bee.png").getImage(); //이것은 함정
	//속도 올리는것. 
	
	private int playerX, playerY;	// 플레이어 위치
	private int playerWidth = player.getWidth(null);
	private int playerHeight = player.getHeight(null);	// 플레이어 가로, 세로 크기
	private int coinX, coinY;	// 코인 위치
	private int coinWidth = coin.getWidth(null);
	private int coinHeight = coin.getHeight(null);// 코인 가로, 세로 크기
	private int coin2X, coin2Y;	// 코인 위치
	private int coin2Width = coin.getWidth(null);
	private int coin2Height = coin.getHeight(null);
	private int coin3X, coin3Y;	// 코인 위치
	private int coin3Width = coin.getWidth(null);
	private int coin3Height = coin.getHeight(null);
	private int coin4X, coin4Y;	// 코인 위치
	private int coin4Width = coin.getWidth(null);
	private int coin4Height = coin.getHeight(null);
	
	private int score;	// 점수
	int time =60;
	int i =1;
	//int i1=i++;
	
	private boolean up, down, left, right;	// 키 눌림
	
	public VoidEatGame() {
		setTitle("음식 먹기 게임");
		setVisible(true);
		setSize(600, 600);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				switch(e.getKeyCode()) {
				case KeyEvent.VK_UP:
					up = true;
					break;
				case KeyEvent.VK_DOWN:
					down = true;
					break;
				case KeyEvent.VK_LEFT:
					left = true;
					break;
				case KeyEvent.VK_RIGHT:
					right = true;
					break;
				}
			}
			
			public void keyReleased(KeyEvent e) {
				switch(e.getKeyCode()) {
				case KeyEvent.VK_UP:
					up = false;
					break;
				case KeyEvent.VK_DOWN:
					down = false;
					break;
				case KeyEvent.VK_LEFT:
					left = false;
					break;
				case KeyEvent.VK_RIGHT:
					right = false;
					break;
				}
			}
		});	// 키보드 움직임 처리를 위한 키리스너 부착
		init();	// 게임 초기화
		while (true) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			keyProcess();
			crashCheck();
		}
	}
	
	
	public void init() {
		score = 0;
		
		playerX = (600 - playerWidth)/2;
		playerY = (600 - playerHeight)/2;
		
		coinX = (int)(Math.random()*(601-playerWidth));
		coinY = (int)(Math.random()*(601-playerHeight-30))+30;	// 점수 초기화, 플레이어와 코인 위치 설정
		coin2X = (int)(Math.random()*(601-playerWidth));
		coin2Y = (int)(Math.random()*(601-playerHeight-30))+30;	// 점수 초기화, 플레이어와 코인 위치 설정
		coin3X = (int)(Math.random()*(601-playerWidth));
		coin3Y = (int)(Math.random()*(601-playerHeight-30))+30;	// 점수 초기화, 플레이어와 코인 위치 설정
		coin4X = (int)(Math.random()*(601-playerWidth));
		coin4Y = (int)(Math.random()*(601-playerHeight-30))+30;	// 점수 초기화, 플레이어와 코인 위치 설정
				
		//playSound("src/audio/backgroundMusic.wav", true);
	}
	
	public void keyProcess() {
		if (up && playerY - 3 > 30) playerY-=3*3;
		if (down && playerY + playerHeight + 3 < 600) playerY+=3*3;
		if (left && playerX - 3 > 0) playerX-=3*3;
		if (right && playerX + playerWidth + 3 < 600) playerX+=3*3;
	}	// 플레이어 움직임
	
	public void crashCheck() {
		if (playerX + playerWidth > coinX && coinX + coinWidth > playerX && playerY + playerHeight > coinY && coinY + coinHeight > playerY
				||playerX + playerWidth > coin2X && coin2X + coin2Width > playerX && playerY + playerHeight > coin2Y && coin2Y + coin2Height > playerY) {
			score+=100;
			count++;
			if(count ==30) {
				JOptionPane.showMessageDialog(null, "게임끝 당신의 점수"+score);
				System.exit(0);
			}
						
			//playSound("src/audio/getCoin.wav", false);
			coinX = (int)(Math.random()*(601-playerWidth));
			coinY = (int)(Math.random()*(601-playerHeight-30))+30;
			coin2X = (int)(Math.random()*(601-playerWidth));
			coin2Y = (int)(Math.random()*(601-playerHeight-30))+30;
			
			
		}else if(playerX + playerWidth > coin3X && coin3X + coin3Width > playerX && playerY + playerHeight > coin3Y && coin3Y + coin3Height > playerY
				||playerX + playerWidth > coin4X && coin4X + coin2Width > playerX && playerY + playerHeight > coin4Y && coin4Y + coin4Height > playerY) {
			score+=150;
			count++;
			if(count ==30) {
				JOptionPane.showMessageDialog(null, "게임끝 당신의 점수"+score);
				System.exit(0);
				
			}
			
			coin3X = (int)(Math.random()*(601-playerWidth));
			coin3Y = (int)(Math.random()*(601-playerHeight-30))+30;
			coin4X = (int)(Math.random()*(601-playerWidth));
			coin4Y = (int)(Math.random()*(601-playerHeight-30))+30;
			
		}
		
	}	// 플레이어와 코인 충돌 체크
	
	
//	public void playSound(String pathName, boolean isLoop) {
//		try {
//			clip = AudioSystem.getClip();
//			File audioFile = new File(pathName);
//			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
//			clip.open(audioStream);
//			clip.start();
//			if (isLoop)
//				clip.loop(Clip.LOOP_CONTINUOUSLY);
//		} catch (LineUnavailableException e) {
//			e.printStackTrace();
//		} catch (UnsupportedAudioFileException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}	// 오디오 재생
	
	public void paint(Graphics g) {
		bufferImage = createImage(600, 600);
		screenGraphic = bufferImage.getGraphics();
		screenDraw(screenGraphic);
		g.drawImage(bufferImage, 0, 0, null);
	}	// 더블 버퍼링
	
	public void starttimer() {
		Timer t = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
		        for(int i = 0;i<=60;i++) {
		            System.out.println(i);
		          }
				
			}
			
		};
		t.schedule(task, 60000);
	}
class TimerRunnable implements Runnable{
		private JLabel timerLabel;
		
		public TimerRunnable(JLabel timerLabel) {
			this.timerLabel=timerLabel;
		}
		final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        final Runnable runnable = new Runnable() {
            int countdownStarter = 20;

            public void run() {

                System.out.println(countdownStarter);
                countdownStarter--;

                if (countdownStarter < 0) {
                    System.out.println("Timer Over!");
                    scheduler.shutdown();
                }
            }
        };
        scheduler.scheduleAtFixedRate(runnable, 0, 1, SECONDS);
    		
	}
	public void screenDraw(Graphics g) {	
		g.drawImage(background, 0, 0, null);
		g.drawImage(coin, coinX, coinY, null);
		g.drawImage(coin2, coin2X, coin2Y, null);
		g.drawImage(coin3, coin3X, coin3Y, null);
		g.drawImage(coin4, coin4X, coin4Y, null);
		g.drawImage(player, playerX, playerY, null);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 40));
		g.drawString("SCORE : " + score, 30, 80);
		g.drawString("TIMES:"+count,30,120);
		Container c = getContentPane();
		c.setLayout(new FlowLayout());
		
		JLabel timerLabel = new JLabel();
		timerLabel.setFont(new Font("Gothic", Font.ITALIC, 80));
		c.add(timerLabel);
		
		//g.drawString("timer: "+timerLabel, 30 ,160);
		this.repaint();
		
		TimerRunnable runnable = new TimerRunnable(timerLabel);
		Thread th = new Thread(runnable);
	}
	public static void main(String[] args) {
		new VoidEatGame();
		
	}

}