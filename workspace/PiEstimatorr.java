import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class PiEstimatorr{
	private int hit = 0;
	private int total = 0;
	private volatile double  estimation = 0;
	
	
	public PiEstimatorr(){
	}

	private synchronized void tryPoint(){
		double x = Math.random();
		double y = Math.random();
		if(x*x+y*y <= 1){
			hit++;
			total++;
		}
		else{
			total++;
		}
		estimation = (double)hit/total*4;
	}


	private class EstimatorThread extends Thread{
		private boolean running = false;
		public EstimatorThread(){
		}
		public void setReady(){
			running = true;
			synchronized(this){
				notify();
			}
		}
		public boolean isRunning(){
			return running;
		}
		public void setPaused(){
			running = false;
		}
		public double getEstimation(){
			return estimation;
		}
		public void run(){
			while(true){
			while(!running){
				try{
					synchronized(this){
					this.wait();
					}
				}
				catch(Exception e){}
			}
				tryPoint();
			}
		}
	}
//the following code is just to jog your memory about how labels and buttons work!
//implement your Pi Estimator as described in the project. You may do it all in main below or you 
//may implement additional functions if you feel it necessary.
	
public static void main(String[] args) { 
		PiEstimatorr p = new PiEstimatorr();
		EstimatorThread t = p.new EstimatorThread();
		t.start();
	    JFrame f=new JFrame("Button Example");  
	    JButton b=new JButton("Click Here");  
	    JLabel example = new JLabel(Double.toString(t.getEstimation()));
	    f.add(example);
	    f.add(b);  
	    f.setSize(300,300);  
	    f.setLayout(new GridLayout(4, 1));  
	    f.setVisible(true);
		b.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e){
				if (t.isRunning()){
					t.setPaused();
				}
				else if (!t.isRunning()){
					t.setReady();
				}
			}
		});
		long timer = System.currentTimeMillis();
		while(true){
			if(System.currentTimeMillis()-timer >= 2000){
				//update screen
				example.setText(Double.toString(t.getEstimation()));
				timer = System.currentTimeMillis();
			}
		}   
	}  
}
