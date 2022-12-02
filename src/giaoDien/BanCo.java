package giaoDien;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.Timer;


public class BanCo implements ActionListener {
	
	private JPanel gamePlay;
	private JPanel welcomePanel;
	private JPanel contPanel;
	private JPanel banCo;
	private JPanel tieuDe;
	private JPanel dieuHuong;
	CardLayout card;
	private int N = 8;
	private int[][] banco = new int[N][N];
	private JButton buttons[][] = new JButton[N][N];
	private JButton clearBanCo;
	JButton startButton;
	private JButton autoSolve;
	private int top = 0;
	private int timer = 500;
	List<OCo> dsNgua = new ArrayList<OCo>();
	List<OCo> dsO = new ArrayList<OCo>();
	int xMove[] = { 2, 1, -1, -2, -2, -1, 1, 2 };
    int yMove[] = { 1, 2, 2, 1, -1, -2, -2, -1 };
    private Color green = new Color(0x5d7e51),white = new Color(0xeeeed2), chon = new Color(0xf6f669),gray = new Color(0xababab);
    boolean autosolve = false;
    private int soln[][] = new int[N][N];
 
	
	ImageIcon knightIcon = new ImageIcon("knightll.png");
	
	BanCo(){
		JFrame frame = new JFrame();
		frame.setSize(800, 680);
		frame.setResizable(false);
		
		welcomePanel = new JPanel();
		contPanel = new JPanel();
		card = new CardLayout();
		
		//	icon
		frame.setIconImage(knightIcon.getImage());
		Image image = knightIcon.getImage(); // transform it 
		Image newimg = image.getScaledInstance(60,60,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
		knightIcon = new ImageIcon(newimg);  // transform it back
		
		
		//SETTING CHO PANEL GAMEPLAY
		gamePlay = new JPanel();
		gamePlay.setBackground(chon);
		gamePlay.setLayout(new BorderLayout());
		
		banCo = new JPanel();
		tieuDe = new JPanel();
		dieuHuong = new JPanel();
		
		//TIEUDE
		tieuDe.setBackground(Color.gray);
		tieuDe.setPreferredSize(new Dimension(1,30));
		
		JLabel tenTieuDe = new JLabel("Tro Choi Ma Di Tuan");
		tenTieuDe.setFont(new Font("Arial",Font.BOLD,15));
		tenTieuDe.setForeground(white);
		tieuDe.add(tenTieuDe);
		
		//BANCO
		
		banCo.setLayout(new GridLayout(N,N,0,0));
		banCo.setBorder(BorderFactory.createLineBorder(green));
		
		
		for(int i=0; i<N; i++)
			for(int j=0; j<N; j++) {
				buttons[i][j] = new JButton();
				if ((i+j)%2==0) buttons[i][j].setBackground(white);
				else buttons[i][j].setBackground(gray);
//				buttons[i][j].setBorder(BorderFactory.createEmptyBorder());
				buttons[i][j].addActionListener(this);
				buttons[i][j].setFocusable(false);
				//buttons[i][j]
				banCo.add(buttons[i][j]);
				
			}
					
		
		//DIEUHUONG
		dieuHuong.setBackground(Color.gray);
		dieuHuong.setPreferredSize(new Dimension(150,0));
		dieuHuong.setLayout(new FlowLayout());
		
		//Clear button
		clearBanCo = new JButton("Clear");
		clearBanCo.setFocusable(false);
		clearBanCo.setPreferredSize(new Dimension(100,25));
		clearBanCo.setBackground(Color.pink);
		clearBanCo.addActionListener(this);
		
		// AUTO SOLVE
		autoSolve = new JButton("AutoSolve");
		autoSolve.setFocusable(false);
		autoSolve.setPreferredSize(new Dimension(100,25));
		autoSolve.setBackground(Color.pink);
		autoSolve.addActionListener(this);
		
		dieuHuong.add(clearBanCo);
		dieuHuong.add(autoSolve);
		
		gamePlay.add(tieuDe, BorderLayout.NORTH);
		gamePlay.add(banCo, BorderLayout.CENTER);
		gamePlay.add(dieuHuong, BorderLayout.EAST);

		//WELCOME PANEL SETTING
		welcomePanel.setBackground(Color.yellow);
		welcomePanel.setLayout(null);
		JLabel bg = new JLabel();
		ImageIcon background = new ImageIcon("knight_bg.png");
		bg.setIcon(background);
		bg.setForeground(green);
		bg.setBackground(green);
		bg.setText("Hello");
		bg.setForeground(Color.white);
		bg.setBounds(0, 0, 800, 680);
		
		startButton = new JButton("START");
		startButton.setBackground(Color.blue);
		startButton.setFont(new Font("Aria",Font.BOLD,20));
		startButton.setBorder(BorderFactory.createEmptyBorder());
		startButton.setOpaque(false);
		startButton.setFocusable(false);
		startButton.setPreferredSize(new Dimension(100,50));
		startButton.setForeground(Color.white);
		startButton.addActionListener(this);
		
		JPanel startOptionPanel = new JPanel();
		startOptionPanel.setBounds(0,450,800,100);
		startOptionPanel.setOpaque(false);
		startOptionPanel.add(startButton);
		bg.add(startOptionPanel);
		
		
		welcomePanel.add(bg);
		welcomePanel.setVisible(true);
		
		//CONTAINER PANEL SETTING
		
		contPanel.setLayout(card);
		contPanel.add(welcomePanel,"1");
		contPanel.add(gamePlay, "2");
		card.show(contPanel, "1");	
		
		//ADD
		frame.add(contPanel);
		frame.setVisible(true);
	}

	
	public void khoiTao(int banco[][]) {
		int i,j;
		for(i=0;i<N;i++) {
			for(j=0;j<N;j++) {
				banco[i][j]=0;
				soln[i][j]=0;
			}	
		}
	}
	
	//tao danh sach cac nuoc di cua ngua
	public List<OCo> TaoDsO(OCo OCo){
		int k,nextX,nextY;
		dsO.clear();
		for(k=0;k<N;k++) {
			nextX = OCo.getX() + xMove[k];
			nextY = OCo.getY() + yMove[k];
			if(nextX >=0 && nextX < N && nextY >=0 && nextY < N) {
				if(banco[nextX][nextY]== 0) {
					dsO.add(new OCo(nextX,nextY));
				}
			}
		}
		return dsO;
	}
	
	// kiem tra nuoc di co phai nuoc hop le ko
	public boolean hopLe(int x,int y) {
		int i;
		for(i=0;i<dsO.size();i++) {
			if(dsO.get(i).getX()==x && dsO.get(i).getY()==y  )
				return true;
		}
		return false;
	}
		
	private boolean isSafe(int x,int y,int soln[][]){
        if (x >= 0 && x < N && y >= 0 && y < N  )
        	if(soln[x][y]== 0 )
            return true;
        return false;
    }
	
	public  boolean knightTour( int i, int j, int stepCount, int xMove[], int yMove[]) throws InterruptedException {
	    if (stepCount   == N*N + 1 )
	      return true;
	    for(int k=0; k<8; k++) {
	      int nextI = i+xMove[k];
	      int nextJ = j+yMove[k];
	      if(isSafe(nextI, nextJ, soln)) {
	        soln[nextI][nextJ] = stepCount;
	        banco[nextI][nextJ]= 1;
	        buttons[nextI][nextJ].setIcon(knightIcon);
	        if((nextI+nextJ)%2==0) buttons[nextI][nextJ].setBackground(white);
	        else buttons[nextI][nextJ].setBackground(gray);
	        buttons[nextI][nextJ].setText(null);
	        top++;
	        dsNgua.add(new OCo(nextI,nextJ));
//	        Thread.sleep(timer);
			int t=dsNgua.size()-1;
			int a=dsNgua.get(t).getX();
			int b=dsNgua.get(t).getY();
			if(banco[a][b]== 1) {
				buttons[a][b].setIcon(null);
				buttons[a][b].setText(String.valueOf(soln[a][b]));
				buttons[a][b].setFont(new Font("Arial", Font. PLAIN, 24));
				buttons[a][b].setBackground(chon);
			}
			
	        if (knightTour( nextI, nextJ, stepCount+1, xMove, yMove))
	        	return true;
	        	soln[nextI][nextJ] = 0; // backtracking
	      }
	    }

	    return false;
	  }

	  public  boolean startKnightTour(int x ,int y) throws InterruptedException {

	    for(int i=1; i<N; i++) {
	      for(int j=1; j<N; j++) {
	        soln[i][j] = 0;
	      }
	    }

	    int xMove[] = {2, 1, -1, -2, -2, -1, 1, 2};
	    int yMove[] = {1, 2, 2, 1, -1, -2, -2, -1};

	    soln[x][y] = 1; 
	    top++;
	    buttons[x][y].setIcon(knightIcon);
	    if (knightTour( x, y,top+1, xMove, yMove)) {
	    	for(int i =0;i<N;i++) {
	    		for(int j =0 ; j<N;j++) {
	    			System.out.print(soln[i][j]+"\t");
	    		}
	    		System.out.println();
	    	}
	      return true;
	    }
	    return false;
	  }
	
	// to mau cac o  cua ban co
	public void toMau(int banCo[][]) {
		int i,j;
		for(i=0;i<N;i++) {
			for(j=0;j<N;j++) {
				//cac nuoc ngua chua di 
				if(banCo[i][j]== 0) { 
					if((i+j)%2==0)
						buttons[i][j].setBackground(white);
					else buttons[i][j].setBackground(gray);
					buttons[i][j].setText(null);
					buttons[i][j].setIcon(null);
				}
				//To mau cac o ngua đa di qua
				if( top > 1 ) {
					int k=dsNgua.size()-1;
					int a=dsNgua.get(k-1).getX();
					int b=dsNgua.get(k-1).getY();
					if(banco[a][b]== 1) {
						buttons[a][b].setIcon(null);
						buttons[a][b].setText(String.valueOf(soln[a][b]));
						buttons[a][b].setFont(new Font("Arial", Font. PLAIN, 24));
						buttons[a][b].setBackground(chon);
					}
				}
				// cac nuoc ngua co the di 
				for(int t=0;t<dsO.size();t++) {
					int x=dsO.get(t).getX();
					int y=dsO.get(t).getY();
					buttons[x][y].setBackground(chon);;
				}		
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		// Start
		if(e.getSource()==startButton) {
			card.show(contPanel,"2");
			System.out.println("hello");
		}
		
		// Clear bàn cờ
		if (e.getSource()==clearBanCo) {
			 for(int i=0;i<N;i++)
				 for(int j=0;j<N;j++) {
					 khoiTao(banco);
					 khoiTao(soln);
					 dsNgua.clear();
					 dsO.clear();
					 top=0;
					 toMau(banco);
				 }
			 
		}
		
		// AutoSolve 
		if(e.getSource() == autoSolve) {
			SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

				@Override
				protected Void doInBackground() throws Exception {
					autosolve=false;
					if(top == 0)
						startKnightTour(0,0);
					else {
						int a=dsNgua.get(top-1).getX();
						int b=dsNgua.get(top-1).getY();
						System.out.println(top);
						startKnightTour(a,b);
					}
					autosolve=false;
					return null;
				}
			};
			worker.execute();
			
		}
		
		// đánh cờ
		if(!autosolve) {
			for(int i=0;i<N;i++) 
				for(int j=0;j<N;j++) {
					if (e.getSource() == buttons[i][j]) {
						// nước đi đầu tiên
						if( isSafe(i,j,soln) && top == 0){
							System.out.println(soln[i][j]);
							buttons[i][j].setIcon(knightIcon);
							banco[i][j]=1;
							dsNgua.add(new OCo(i,j));
							List<OCo> dsO = TaoDsO(new OCo(i,j));
							top++;
							soln[i][j]=top;
						}
						// Lùi lại sau 1 bước
						else if(banco[i][j]==1 && soln[i][j] == top) {
							dsNgua.remove(top-1);// xoa ô đó ra khỏi ds Ngua
							banco[i][j]=0;
							// set lại Ô trước đó
							if(top >1) {
								int a = dsNgua.get(top-2).getX(); // lấy  vị trí x, y  của ô đã đi trước đó
								int b = dsNgua.get(top-2).getY();
								buttons[a][b].setIcon(knightIcon);
								buttons[a][b].setText(null);
								if((a+b)%2==0) buttons[a][b].setBackground(white);
								else buttons[a][b].setBackground(gray);
								dsO = TaoDsO(new OCo(a,b));
							}
							else {
								dsO.clear();
							}
							top--;
						}
						// nếu nước đi thuộc trong các nước được đi
						else if(hopLe(i,j)) {
							buttons[i][j].setIcon(knightIcon);
							if((i+j)%2==0) buttons[i][j].setBackground(white);
							else buttons[i][j].setBackground(gray);
							banco[i][j]=1;
							dsNgua.add(new OCo(i,j)); //danh sach ngua da di
							List<OCo> dsO = TaoDsO(new OCo(i,j));
							top++;
							soln[i][j]=top;
						}
					toMau(banco);
					}	
				}	
		}
	}
}
