import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.ArrayList;

public class Breakout extends JPanel implements Runnable{
	int state=0;
	int pos_x = 0;
	int pos_y = 0;
        int ballx = 0;
        int bally = 0;
        int mark=0;
        int speed=6;
        int vx=speed*8/10;
        int vy=-speed*7/10;
        int fps=30;
	boolean color1 = false;
	boolean color2 = false;
        boolean start=false;
        float dist=0;
        int width=800;
        int height=600;
//class for a single brick
	public class brick{
            int x1,y1,x2,y2;
            boolean appear;
            
            //ctor
            public brick(int x1,int y1,int x2,int y2){
               this.x1=x1;
               this.y1=y1;
               this.x2=x2;
               this.y2=y2;
               this.appear=true;
            };//endofctor



            //call when hit the brick
            //disappear the brick and return direction
            public char hit(int x,int y,int maxw){
               appear=false;
               if(y > this.y1 && x > x1-3*maxw/100 && x < x2+3*maxw/100){
                  return 'u';
               }else if(y < this.y1 && x > x1-3*maxw/100 && x < x2+3*maxw/100){
                  return 'd';
               }else if(x < x1){
                  return 'l';
               }else{
                  return 'r';
               }//endif
            };//endofhit

        };//endof brick class

//variables for constructing bricks
        int px=33*800/100;
        int py=9*600/100;
        brick[] list = new brick[20]; 


	public void setenv(int fr,int s){
            speed=s;
            fps=fr;
            return;
        };
	public Breakout(){
                this.addKeyListener(new KeyAdapter(){
                        public void keyPressed(KeyEvent e){
                           int keyCode =e.getKeyCode(); 
                           if(keyCode==KeyEvent.VK_ENTER){
                              if(0==state){
                                 state=1;
                              }else if(1==state){
                                 ballx=50;
                                 bally=92;
                                 start=true;
                                 vx=speed*8/10;
                                 vy=-speed*7/10;
                              };//endif
                           };
                           if(keyCode==KeyEvent.VK_LEFT && pos_x> getWidth()/15){
    			         pos_x = pos_x - 5*getWidth()/100;
                           };
                           if(keyCode==KeyEvent.VK_RIGHT && pos_x < 14*getWidth()/15){
    			         pos_x = pos_x + 5*getWidth()/100;
                           };
                           if(keyCode==KeyEvent.VK_ESCAPE){
                              System.exit(0);
                           };
				 repaint();
                        };
                });//key listenner end


		this.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
                                if(!start&&1==state){
                                   ballx=50;
                                   bally=92;
                                   start=true;
                                   vx=speed*8/10;
                                   vy=-speed*7/10;
            if(start) System.out.println("Start !\n");
                                };//endif
                                if(0==state){state=1;};
                                if(start && e.isMetaDown()){
                                   ballx=e.getX()*100/getWidth();
                                   bally=e.getY()*100/getHeight();
                                };

				pos_x = e.getX();
				pos_y = e.getY();
				testContainment();
				repaint();
			};

		}); //mouse listenner end
                this.addMouseMotionListener(new MouseAdapter(){
                        public void mouseMoved(MouseEvent e){
                                pos_x = e.getX();
				pos_y = e.getY();
				testContainment();
				repaint();

                        };

		}); //mouse motion listenner end


                 
//construct the brick list
                for(int i=0;i<20;++i){
                   if(0==i%5&&i!=0){
                      px= px+16*width/100;
                      py=9*height/100;
                   };
                   list[i] = new brick(px,py,px+6*width/100,py);
                   py=py+10*height/100;
                };//endfor


	}
	
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;             // cast to get 2D drawing methods
switch(state){
case 0:
//draw splash screen
       g2.setColor(Color.YELLOW);
       Font font = new Font("alias",Font.PLAIN,getWidth()/20);
       g2.setFont(font);
       g2.drawString("Written By Caleb Liu",getWidth()/4,getHeight()*3/10);
       g2.drawString("Student num: 20521697",getWidth()/4,getHeight()*4/10);
       g2.drawString("Username: b69liu",getWidth()/4,getHeight()*1/2);
       Font font3 = new Font("alias",Font.PLAIN,getWidth()/40);
       g2.setFont(font3);
       g2.drawString("play via  <-  -> or mouse",getWidth()/4,getHeight()*12/20);
       g2.drawString("ENTRE/click to start. Esc exit",getWidth()/4,getHeight()*13/20);
       g2.setColor(Color.GRAY);
       g2.setFont(new Font("alias",Font.PLAIN,getWidth()/100));
       g2.drawString("Right-Click shuttle the ball!!!",getWidth()/4,getHeight()*19/20);
       break;
case 1:
//draw a plat
       g2.setStroke(new BasicStroke(getHeight()/50)); 
       g2.setColor(Color.PINK);
       g2.drawLine(pos_x-(getWidth()/15),getHeight()-(getHeight()/25),pos_x+(getWidth()/15),getHeight()-(getHeight()/25));


//draw the bricks
       g2.setStroke(new BasicStroke(6*getWidth()/100)); 
       for(int j=0;j<20;++j){
          if(j%5==0){
             g2.setColor(Color.BLUE);
          }else if(j%5==1){
             g2.setColor(Color.CYAN);
          }else if(j%5==2){
             g2.setColor(Color.GRAY);
          }else if(j%5==3){
             g2.setColor(Color.GREEN);
          }else{
             g2.setColor(Color.LIGHT_GRAY);
          }
          if(list[j].appear){
            g2.drawLine(list[j].x1*getWidth()/width,list[j].y1*getHeight()/height,list[j].x2*getWidth()/width,list[j].y2*getHeight()/height);
          };
       };//endfor
//draw the ball
       g2.setColor(Color.RED);
       if(start)
       g2.fillOval(ballx*getWidth()/100, bally*getHeight()/100, 15, 15);
       else
       g2.fillOval(getWidth()/2, getHeight()-(getHeight()/13), 2*getWidth()/100, 2*getWidth()/100);

//draw the board
              g2.setColor(Color.YELLOW);
              Font font2 = new Font("alias",Font.PLAIN,20*getWidth()/800);
              g2.setFont(font2);
              g2.drawString("Score: "+mark,2*100/getWidth(),30*getHeight()/600);
              g2.drawString("FPS: "+fps,2*100/getWidth(),50*getHeight()/600);
//              g2.drawString("("+ pos_x + "," + pos_y + ")",20,50);
break;

case 2:       g2.setColor(Color.RED);
              g2.setFont(new Font("alias",Font.PLAIN,50*getWidth()/800));
              g2.drawString("YOU WIN!",getWidth()/3,getHeight()/2);
break;
default:
              g2.setFont(new Font("alias",Font.PLAIN,50*getWidth()/800));
              g2.setColor(Color.RED);
              g2.drawString("GAME OVER",getWidth()/3,getHeight()/2);


};//endof switch
	}


	


	protected void testContainment(){
                //collide plat
		double d1 = Line2D.ptSegDist(pos_x-(getWidth()/15),getHeight()-(getHeight()/25),pos_x+(getWidth()/15),
                                             getHeight()-(getHeight()/25), ballx*getWidth()/100, bally*getHeight()/100);

		if(d1 < 20.0){
                   vy=-5;
                };


            //check for colliding brick
                for(int k=0;k<20;++k){
		    double d2 = Line2D.ptSegDist(list[k].x1*getWidth()/width,list[k].y1*getHeight()/height,list[k].x2*getWidth()/width,list[k].y2*getHeight()/height, ballx*getWidth()/100, bally*getHeight()/100);
                    if(d2 < 4*getWidth()/100 && start && list[k].appear) {
                        mark = mark + 5;
                        if(100==mark){state=2;};
                        switch(list[k].hit(ballx*getWidth()/100,bally*getHeight()/100, getWidth()) ){
                            case 'u':  vy=-vy;
                            break;
                            case 'd':  vy=-vy;
                            break;
                            case 'l':  vx=-vx;
                            break;
                            default :  vx=-vx;

                        };//endswitch
                    };//endif

                };//endfor


	}
	
	public static void main(String args[]){
		JFrame window = new JFrame("Breakout");
		window.setSize(800, 600);
                Breakout tl = new Breakout();
                tl.setFocusable(true);
		window.setContentPane(tl);
                window.getContentPane().setBackground(new Color(0,0,0));
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
              
                if(args.length==2){
                 tl.setenv(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
                };
                Thread thread = new Thread(tl);
                thread.start();
	}

        @Override
        public void run(){

            while(true){
               if(ballx<=0){
                   vx=speed*8/10;
               }else if(ballx>=95){
                   vx=-speed*8/10;
               };
               if(bally<=0){   //drop
                   vy= speed*7/10;
               }else if(bally>=95&&!start){
                   vy=-speed*7/10;
               }else if(bally>=100&&start&&1==state){
                   state=3;
               };
               ballx=ballx+vx*getWidth()/2000;
               bally=bally+vy*getHeight()/2000;
               testContainment();
               repaint();

               try{
                 Thread.sleep(1000/fps);
               }catch(InterruptedException ex){
//                  ex.printStackTrace();
               }
            } //endwhile
        };
}
