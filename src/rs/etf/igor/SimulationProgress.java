package rs.etf.igor;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class SimulationProgress extends JFrame implements Constants{

	private static final long serialVersionUID = 1L;

	private JButton abortButton;
	private JProgressBar simulationProgress;
	private JLabel simulationProgressLabel;
	private SinusPicture sinusPicture;

	private Simulation simulation;

	private UI parent;
	private JFrame myFrame;
	private Timer timer;
	private long osciloscopeFrequency=5;

	public SimulationProgress(UI parent) {
		this.setParentFrame(parent);
		initComponents();
		setLocation(parent.getWidth()/2-getWidth()/2, parent.getHeight()/2-getHeight()/2);
		myFrame=this;
		timer = new Timer();
		timer.schedule(new Osciloscop(sinusPicture), 0, osciloscopeFrequency);
		super.setResizable(false);
		simulation=new Simulation(this, parent);
		simulation.start();

	}

	private void initComponents() {

		sinusPicture = new SinusPicture();
		simulationProgress = new JProgressBar();
		simulationProgressLabel = new JLabel();
		abortButton = new JButton();

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setVisible(true);

		sinusPicture.setBackground(new Color(255, 255, 255));
		sinusPicture.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));

		GroupLayout sinusPictureLayout = new GroupLayout(sinusPicture);
		sinusPicture.setLayout(sinusPictureLayout);
		sinusPictureLayout.setHorizontalGroup(
				sinusPictureLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGap(0, 378, Short.MAX_VALUE)
		);
		sinusPictureLayout.setVerticalGroup(
				sinusPictureLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGap(0, 225, Short.MAX_VALUE)
		);

		simulationProgress.setForeground(new Color(51, 255, 51));
		simulationProgress.setMaximum(Network.getInstance().getNumberOfIterations()-1);
		simulationProgress.setMinimum(0);

		simulationProgressLabel.setHorizontalAlignment(SwingConstants.CENTER);
		simulationProgressLabel.setText("Simulation in progress");

		abortButton.setText("Abort");
		abortButton.setHorizontalTextPosition(SwingConstants.CENTER);
		abortButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				showDialog();
			}
		});

		parent.setEnabled(false);
		parent.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		addWindowListener(new WindowListener(){

			@Override
			public void windowActivated(WindowEvent arg0) {


			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				parent.setEnabled(true);
				parent.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				parent.requestFocus();
				simulation.destroy();
				timer.cancel();

			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				showDialog();

			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {


			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {


			}

			@Override
			public void windowIconified(WindowEvent arg0) {


			}

			@Override
			public void windowOpened(WindowEvent arg0) {


			}

		});
		setTitle("Simulation in progress");		

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addGroup(layout.createSequentialGroup()
										.addContainerGap()
										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
												.addComponent(sinusPicture, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(simulationProgressLabel, GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
												.addComponent(simulationProgress, GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)))
												.addGroup(layout.createSequentialGroup()
														.addGap(165, 165, 165)
														.addComponent(abortButton)))
														.addContainerGap())
		);
		layout.setVerticalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addContainerGap()
						.addComponent(sinusPicture, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(simulationProgressLabel)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(simulationProgress, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(abortButton)
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);

		pack();
	}

	protected void showDialog() {
		int n = JOptionPane.showConfirmDialog(myFrame, 
				"If you click yes, the simultaion will be stoped!",
				"Warning",
				JOptionPane.YES_NO_OPTION, 
				JOptionPane.WARNING_MESSAGE);
		if (n==JOptionPane.CLOSED_OPTION||n==JOptionPane.NO_OPTION){

		} else if (n==JOptionPane.YES_OPTION){
			myFrame.dispose();
		}


	}

	private void setParentFrame(UI parent) {
		this.parent = parent;
	}

	public JFrame getParentFrame() {
		return parent;
	}

	public JProgressBar getProgressBar(){
		return simulationProgress;
	}

	private class SinusPicture extends JPanel {

		private static final long serialVersionUID = 1L;
		private double[] sinusoid;
		private int noPoints=2000;
		private int noCicles=10;
		private int[] points;

		public SinusPicture(){

			drawPicture(0);
		}

		private double[] getSinusoid(int startingInt) {
			double[] sinusoid=new double[noPoints];
			int k=0;
			for(int i=startingInt;i<noPoints;i++,k++){
				double radians= (Math.PI/(noPoints/(2*noCicles)))*k;
				sinusoid[i]=Math.sin(radians);
			}
			for(int i=0;i<startingInt;i++,k++){
				double radians= (Math.PI/(noPoints/(2*noCicles)))*k;
				sinusoid[i]=Math.sin(radians);
			}
			return sinusoid;
		}

		@Override
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			int maxHeight=getHeight();
			int maxWidth=getWidth();	
			double step=(double)maxWidth/noPoints;
			Random generator=new Random();
			for(int i=0;i<noPoints;i++){
				points[i]=(int)(sinusoid[i]*maxHeight/2*0.80+maxHeight*0.4+maxHeight*0.3*generator.nextDouble());
			}
			g.setColor(Color.blue);
			for(int i=1;i<noPoints;i++){
				int x1 = (int) ((i-1)*step);
				int y1 = points[i-1];
				int x2 = (int) (i*step);
				int y2 = points[i];
				g.drawLine(x1, y1, x2, y2);
			}		
		}

		public int getNoPoints(){
			return noPoints;
		}
		public void drawPicture(int startingInt){
			sinusoid=getSinusoid(startingInt);
			points= new int[noPoints];
			repaint();
		}

	}

	private class Osciloscop extends TimerTask{
		private SinusPicture sinus;
		int start;

		public Osciloscop(SinusPicture s){
			super();
			start=0;
			sinus=s;			
		}

		@Override
		public void run() {
			sinus.drawPicture(start);
			start++;
			if(start==sinus.getNoPoints()){
				start=0;
			}

		}

	}

	public void printTimeLeft(long l) {
		long hours;
		long min;
		long sec;
		String label="Estimated time left: ";		
		min=l/60;
		l-=min*60;
		sec=l;
		hours=min/60;
		min-=hours*60;
		if(hours>0){
			label+=hours+"h ";
		}
		if(min>0){
			label+=min+"m ";
		}
		label+=sec+"s";
		simulationProgressLabel.setText(label);
	}
}