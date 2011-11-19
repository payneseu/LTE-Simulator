/*
 * Distributed under the terms of the GNU GPL version 3.
 * 
 * Copyright (c) 2011 Igor Maraviæ <igorm@etf.rs>
 */

package rs.etf.igor;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.util.List;

public class UI extends JFrame implements Constants {

	private static final long serialVersionUID = 1L;

	private JRadioButton FFRButton;
	private JLabel alphaLabel;
	private JSlider alphaSlider;
	private JLabel bandwidthLabel;
	private JLabel betaLabel;
	private JSlider betaSlider;
	private JLabel deltaLabel;
	private JSlider deltaSlider;
	private JLabel gammaLabel;
	private JSlider gammaSlider;
	private NetworkPicture networkPicture;
	private JLabel reuseLabel;
	private PowerGraf powerGraf;
	private JLabel radiusLabelName;
	private JRadioButton reuse1Button;
	private JRadioButton reuse3Button;
	private JSeparator separator;
	private JButton drawButton;
	private JButton startButton;
	private JLabel innerRadiusLabel;
	private JSlider innerRadiusSlider;
	private JLabel iterationsLabel;
	private JSlider iterationsSlider;
	private JLabel innerInnerRadiusLabel;
    private JSlider innerInnerRadiusSlider;
    private JPanel jPanel1;
    private JScrollPane jScrollPane1;

	private static final int SLIDER_MAX_TICK = 50;
	private static final int SLIDER_MIN_TICK = 5;
	private static final int SLIDER_MAX = 100;
	private static final int SLIDER_MIN = 0;
	private static final int[] REUSE1_VALUES = {100,0,0,100};
	private static final int[] REUSE3_VALUES = {0,100,0,0};
	private static final int[] FFR_VALUES = {0,100,50,0};
	private static final int ITERATIONS_STEP = 500;
	private static final int MAX_ITERATIONS = 10000;
	private static final int MIN_ITERATIONS = 500;
	private static final int DEFAULT_ITERATIONS = 5000;
	private static final int MAX_INNER_RADIUS_STEP = 100;
	private static final int MIN_INNER_RADIUS_STEP = 10;
	private static final int DEFAULT_INNER_CELL_RADIUS = 230;
	
	private UI myFrame;

	private boolean entireNetwork=true; //flag for drawing the network


	/** 
	 * Creates new form of LTESimulatorGUI
	 */
	public UI() {
		myFrame=this;
		initComponents();
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int X = (screen.width / 2) - (this.getWidth() / 2); // Center horizontally.
        int Y = (screen.height / 2) - (this.getHeight() / 2); // Center vertically.
        setBounds(X,Y , getWidth(),getHeight());
	}

	/** 
	 * initializing the UI and responds to Start button events
	 */
	private void initComponents() {

		//Initialization of variables
		separator = new JSeparator();
		bandwidthLabel = new JLabel();
		reuseLabel = new JLabel();
		reuse1Button = new JRadioButton();
		reuse3Button = new JRadioButton();
		FFRButton = new JRadioButton();
		drawButton = new JButton();
		deltaSlider = new JSlider();
		alphaSlider = new JSlider();
		gammaSlider = new JSlider();
		betaSlider = new JSlider();
		powerGraf = new PowerGraf();
		startButton = new javax.swing.JButton();
		networkPicture = new NetworkPicture();
		radiusLabelName = new JLabel();
		alphaLabel = new JLabel();
		gammaLabel = new JLabel();
		betaLabel = new JLabel();
		deltaLabel = new JLabel();
		iterationsSlider = new JSlider();
        iterationsLabel = new JLabel();
        innerRadiusLabel = new JLabel();
        innerRadiusSlider = new JSlider();
        innerInnerRadiusSlider = new JSlider();
        innerInnerRadiusLabel = new JLabel();
        jScrollPane1 = new JScrollPane();
        jPanel1 = new JPanel();


		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("LTE Simulator");

		separator.setOrientation(SwingConstants.VERTICAL);

		//radio buttons
		ButtonGroup group= new ButtonGroup();        
		FFRButton.setSelected(true);
		group.add(reuse1Button);
		group.add(reuse3Button);
		group.add(FFRButton);

		reuse1Button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				alphaSlider.setValue(REUSE1_VALUES[0]);
				alphaSlider.setEnabled(false);
				betaSlider.setValue(REUSE1_VALUES[1]);
				betaSlider.setEnabled(false);
				gammaSlider.setValue(REUSE1_VALUES[2]);
				gammaSlider.setEnabled(false);
				deltaSlider.setValue(REUSE1_VALUES[3]);
				deltaSlider.setEnabled(false);
				innerRadiusSlider.setEnabled(false);
				innerRadiusSlider.setValue(0);
				innerInnerRadiusSlider.setEnabled(false);
				innerInnerRadiusSlider.setValue(0);

				Network.getInstance().setupParameters((double)alphaSlider.getValue()/100, (double)betaSlider.getValue()/100,(double)gammaSlider.getValue()/100, (double)deltaSlider.getValue()/100);

				networkPicture.drawPicture();
			}
		});

		reuse3Button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				alphaSlider.setValue(REUSE3_VALUES[0]);
				alphaSlider.setEnabled(false);
				betaSlider.setValue(REUSE3_VALUES[1]);
				betaSlider.setEnabled(false);
				gammaSlider.setValue(REUSE3_VALUES[2]);
				gammaSlider.setEnabled(false);
				deltaSlider.setValue(REUSE3_VALUES[3]);
				deltaSlider.setEnabled(false);
				innerRadiusSlider.setEnabled(false);
				innerRadiusSlider.setValue(0);
				innerInnerRadiusSlider.setEnabled(false);
				innerInnerRadiusSlider.setValue(0);

				Network.getInstance().setupParameters((double)alphaSlider.getValue()/100, (double)betaSlider.getValue()/100,(double)gammaSlider.getValue()/100, (double)deltaSlider.getValue()/100);

				networkPicture.drawPicture();
			}
		});

		FFRButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				alphaSlider.setValue(FFR_VALUES[0]);
				alphaSlider.setEnabled(true);
				betaSlider.setValue(FFR_VALUES[1]);
				betaSlider.setEnabled(false);
				gammaSlider.setValue(FFR_VALUES[2]);
				gammaSlider.setEnabled(true);
				deltaSlider.setValue(FFR_VALUES[3]);
				deltaSlider.setEnabled(true);
				innerRadiusSlider.setEnabled(true);
				innerRadiusSlider.setValue(DEFAULT_INNER_CELL_RADIUS);
				innerInnerRadiusSlider.setEnabled(false);
				innerInnerRadiusSlider.setValue(0);
				Network.getInstance().setupParameters((double)alphaSlider.getValue()/100, (double)betaSlider.getValue()/100,(double)gammaSlider.getValue()/100, (double)deltaSlider.getValue()/100);

				networkPicture.drawPicture();

			}
		});

		reuse1Button.setText("Reuse 1");        

		reuse3Button.setText("Reuse 3");

		FFRButton.setText("FFR");

		//labels
		bandwidthLabel.setHorizontalAlignment(SwingConstants.LEFT);
		bandwidthLabel.setText("Bandwidth is "+DEFAULT_B+" MHz");

		reuseLabel.setHorizontalAlignment(SwingConstants.LEFT);

		radiusLabelName.setHorizontalAlignment(SwingConstants.LEFT);
		radiusLabelName.setText("Radius is "+DEFAULT_RADIUS+" m");

		//buttons
		drawButton.setText("Draw one cell");
		drawButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(entireNetwork){
					entireNetwork=false;
					drawButton.setText("Draw entire network");
				} else {
					entireNetwork=true;
					drawButton.setText("Draw one cell");
				}

				Network.getInstance().setupParameters((double)alphaSlider.getValue()/100, (double)betaSlider.getValue()/100,(double)gammaSlider.getValue()/100, (double)deltaSlider.getValue()/100);


				networkPicture.drawPicture();
			}

		});

		startButton.setText("Simulate");
		startButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {

				Network.getInstance().setupParameters((double)alphaSlider.getValue()/100, (double)betaSlider.getValue()/100,(double)gammaSlider.getValue()/100, (double)deltaSlider.getValue()/100);

				new SimulationProgress(myFrame);								
			}

		});

		//sliders
		deltaSlider.setMajorTickSpacing(SLIDER_MAX_TICK);
		deltaSlider.setMinorTickSpacing(SLIDER_MIN_TICK);
		deltaSlider.setMaximum(SLIDER_MAX);
		deltaSlider.setMinimum(SLIDER_MIN);
		deltaSlider.setPaintTicks(true);
		deltaSlider.setSnapToTicks(false);
		deltaSlider.setValue(FFR_VALUES[3]);
		deltaSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				deltaLabel.setText("delta="+deltaSlider.getValue());
				powerGraf.drawGraf();
				printReuse();
				if(deltaSlider.getValue()>0 && alphaSlider.getValue()==0 && !deltaSlider.getValueIsAdjusting()){
					alphaSlider.setValue(5);
				}

				if(deltaSlider.getValue()==0 && !deltaSlider.getValueIsAdjusting()){
					alphaSlider.setValue(0);
				}

				if(deltaSlider.getValue()==100 && !deltaSlider.getValueIsAdjusting()){
					betaSlider.setValue(0);
					gammaSlider.setValue(0);
				}

				if(deltaSlider.getValue()<100 && betaSlider.getValue()==0 && gammaSlider.getValue()==0 && !deltaSlider.getValueIsAdjusting()){
					betaSlider.setValue(100);
					gammaSlider.setValue(5);
				}
			}
		});

		deltaLabel.setText("delta="+deltaSlider.getValue());

		alphaSlider.setMajorTickSpacing(SLIDER_MAX_TICK);
		alphaSlider.setMinorTickSpacing(SLIDER_MIN_TICK);
		alphaSlider.setMaximum(SLIDER_MAX);
		alphaSlider.setMinimum(SLIDER_MIN);
		alphaSlider.setOrientation(JSlider.VERTICAL);
		alphaSlider.setPaintTicks(true);
		alphaSlider.setSnapToTicks(false);
		alphaSlider.setValue(FFR_VALUES[0]);
		alphaSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				alphaLabel.setText("alpha="+alphaSlider.getValue());

				if(alphaSlider.getValue()==0){
					deltaSlider.setValue(0);
				}

				if(alphaSlider.getValue()>0 && deltaSlider.getValue()==0 && !alphaSlider.getValueIsAdjusting()){
					deltaSlider.setValue(5);
				}
				printReuse();
				powerGraf.drawGraf();
				networkPicture.drawPicture();
				if(alphaSlider.getValue()==0 || gammaSlider.getValue()==0){
					innerInnerRadiusSlider.setEnabled(false);
					innerInnerRadiusSlider.setValue(0);
				} else if(alphaSlider.getValue()!=0 && gammaSlider.getValue()!=0){
					innerInnerRadiusSlider.setEnabled(true);
					//innerInnerRadiusSlider.setValue((int)(innerRadiusSlider.getValue()/2));
				}
				
				if(alphaSlider.getValue()==0 && gammaSlider.getValue()==0){
					innerRadiusSlider.setEnabled(false);
					innerRadiusSlider.setValue(0);
				} else {
					innerRadiusSlider.setEnabled(true);
					if(innerRadiusSlider.getValue()==0){
						innerRadiusSlider.setValue(DEFAULT_INNER_CELL_RADIUS);
					}
				}
			}
		});

		alphaLabel.setText("alpha="+alphaSlider.getValue());
		alphaLabel.setMinimumSize(new Dimension(60,14));
		alphaLabel.setMaximumSize(new Dimension(60,14));
		alphaLabel.setPreferredSize(new Dimension(60,14));
		gammaSlider.setMajorTickSpacing(SLIDER_MAX_TICK);
		gammaSlider.setMinorTickSpacing(SLIDER_MIN_TICK);
		gammaSlider.setMaximum(SLIDER_MAX);
		gammaSlider.setMinimum(SLIDER_MIN);
		gammaSlider.setOrientation(JSlider.VERTICAL);
		gammaSlider.setPaintTicks(true);
		gammaSlider.setSnapToTicks(false);
		gammaSlider.setValue(FFR_VALUES[2]);
		gammaSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				gammaLabel.setText("gamma="+gammaSlider.getValue());

				if(gammaSlider.getValue()==0 && betaSlider.getValue()==0){
					deltaSlider.setValue(100);
				}

				if(gammaSlider.getValue()>0 && deltaSlider.getValue()==100 && !gammaSlider.getValueIsAdjusting() && !betaSlider.getValueIsAdjusting()){
					deltaSlider.setValue(95);
				}
				
				if(alphaSlider.getValue()==0 || gammaSlider.getValue()==0){
					innerInnerRadiusSlider.setEnabled(false);
					innerInnerRadiusSlider.setValue(0);
				} else if(alphaSlider.getValue()!=0 && gammaSlider.getValue()!=0){
					innerInnerRadiusSlider.setEnabled(true);
					//innerInnerRadiusSlider.setValue((int)(innerRadiusSlider.getValue()/2));
				} 
				if(alphaSlider.getValue()==0 && gammaSlider.getValue()==0){
					innerRadiusSlider.setEnabled(false);
					innerRadiusSlider.setValue(0);
				} else {
					innerRadiusSlider.setEnabled(true);
					if(innerRadiusSlider.getValue()==0){
						innerRadiusSlider.setValue(DEFAULT_INNER_CELL_RADIUS);
					}
				}
				
				printReuse();
				powerGraf.drawGraf();
				networkPicture.drawPicture();
			}
		});

		gammaLabel.setText("gamma="+gammaSlider.getValue());
		gammaLabel.setMinimumSize(new Dimension(72,14));
		gammaLabel.setMaximumSize(new Dimension(72,14));
		gammaLabel.setPreferredSize(new Dimension(72,14));

		betaSlider.setMajorTickSpacing(SLIDER_MAX_TICK);
		betaSlider.setMinorTickSpacing(SLIDER_MIN_TICK);
		betaSlider.setMaximum(SLIDER_MAX);
		betaSlider.setMinimum(SLIDER_MIN);
		betaSlider.setOrientation(JSlider.VERTICAL);
		betaSlider.setPaintTicks(true);
		betaSlider.setSnapToTicks(true);
		betaSlider.setEnabled(false);
		betaSlider.setValue(FFR_VALUES[1]);
		betaSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				betaLabel.setText("beta="+betaSlider.getValue());

				if(gammaSlider.getValue()==0 && betaSlider.getValue()==0){
					deltaSlider.setValue(100);
				}

				if(betaSlider.getValue()>0 && deltaSlider.getValue()==100 && !betaSlider.getValueIsAdjusting() && !gammaSlider.getValueIsAdjusting()){
					deltaSlider.setValue(95);
				}

				printReuse();

				powerGraf.drawGraf();
				networkPicture.drawPicture();
			}


		});

		betaLabel.setText("beta="+betaSlider.getValue());
		betaLabel.setMinimumSize(new Dimension(60,14));
		betaLabel.setMaximumSize(new Dimension(60,14));
		betaLabel.setPreferredSize(new Dimension(60,14));
		
		iterationsSlider.setMajorTickSpacing(ITERATIONS_STEP);
        iterationsSlider.setMaximum(MAX_ITERATIONS);
        iterationsSlider.setMinimum(MIN_ITERATIONS);
        iterationsSlider.setMinorTickSpacing(ITERATIONS_STEP);
        iterationsSlider.setPaintTicks(true);
        iterationsSlider.setSnapToTicks(true);
        iterationsSlider.setValue(DEFAULT_ITERATIONS);
        iterationsSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				iterationsLabel.setText("Number of iterations is "+iterationsSlider.getValue());
				Network.getInstance().setNumberOfIterations(iterationsSlider.getValue());
				
			}
        	
        });
        
        iterationsLabel.setText("Number of iterations is "+iterationsSlider.getValue());

        

        innerRadiusSlider.setMajorTickSpacing(MAX_INNER_RADIUS_STEP);
        innerRadiusSlider.setMaximum(DEFAULT_RADIUS);
        innerRadiusSlider.setMinorTickSpacing(MIN_INNER_RADIUS_STEP);
        innerRadiusSlider.setPaintTicks(true);
        innerRadiusSlider.setSnapToTicks(true);
        innerRadiusSlider.setValue(DEFAULT_INNER_CELL_RADIUS);
        innerRadiusSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				innerRadiusLabel.setText("Inner cell radius is "+innerRadiusSlider.getValue());
				if(innerRadiusSlider.getValue()<innerInnerRadiusSlider.getValue()){
					innerInnerRadiusSlider.setValue(innerRadiusSlider.getValue());
				}
				innerInnerRadiusSlider.setMaximum(innerRadiusSlider.getValue());
				Network.getInstance().setInnerCellRadius(innerRadiusSlider.getValue());
				
			}
        	
        });
        
        innerRadiusLabel.setText("Inner cell radius is "+innerRadiusSlider.getValue());
        
        innerInnerRadiusSlider.setPaintTicks(true);
        innerInnerRadiusSlider.setMaximum(innerRadiusSlider.getValue());
        innerInnerRadiusSlider.setMajorTickSpacing(MAX_INNER_RADIUS_STEP);
        innerInnerRadiusSlider.setMinorTickSpacing(MIN_INNER_RADIUS_STEP);
        innerInnerRadiusSlider.setEnabled(false);
		innerInnerRadiusSlider.setValue(0);
        innerInnerRadiusSlider.setSnapToTicks(true);
        innerInnerRadiusSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				innerInnerRadiusLabel.setText("Inner inner cell radius is "+innerInnerRadiusSlider.getValue());
				Network.getInstance().setInnerInnerCellRadius(innerInnerRadiusSlider.getValue());
			}
        	
        });

        innerInnerRadiusLabel.setText("Inner inner cell radius is "+innerInnerRadiusSlider.getValue());

		//Network parameters initialization
		Network network=Network.getInstance();
		network.setupParameters((double)alphaSlider.getValue()/100, (double)betaSlider.getValue()/100,(double)gammaSlider.getValue()/100, (double)deltaSlider.getValue()/100,
				iterationsSlider.getValue(),innerRadiusSlider.getValue(),innerInnerRadiusSlider.getValue());
		printReuse();

		//grafs
		powerGraf.setBackground(new java.awt.Color(255, 255, 255));
		powerGraf.setBorder(new LineBorder(new java.awt.Color(0, 0, 0), 1, true));
		powerGraf.setMaximumSize(new java.awt.Dimension(174, 140));
		powerGraf.setMinimumSize(new java.awt.Dimension(174, 140));

		GroupLayout powerGrafLayout = new GroupLayout(powerGraf);
		powerGraf.setLayout(powerGrafLayout);
		powerGrafLayout.setHorizontalGroup(
				powerGrafLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGap(0, 221, Short.MAX_VALUE)
		);
		powerGrafLayout.setVerticalGroup(
				powerGrafLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGap(0, 138, Short.MAX_VALUE)
		);

		networkPicture.setBackground(Color.white);

		GroupLayout networkPictureLayout = new GroupLayout(networkPicture);
		networkPicture.setLayout(networkPictureLayout);
		networkPicture.setBorder(new LineBorder(new java.awt.Color(0, 0, 0), 1, true));
		networkPictureLayout.setHorizontalGroup(
				networkPictureLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGap(0, 487, Short.MAX_VALUE)
		);
		networkPictureLayout.setVerticalGroup(
				networkPictureLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGap(0, 552, Short.MAX_VALUE)
		);

		//Visual appearance of UI

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bandwidthLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(reuseLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(radiusLabelName, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(innerRadiusSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(innerRadiusLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(iterationsLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(iterationsSlider, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(innerInnerRadiusLabel)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(reuse3Button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(FFRButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(reuse1Button))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(alphaLabel)
                            .addComponent(alphaSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(deltaSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(deltaLabel))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(powerGraf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(gammaLabel)
                                    .addComponent(gammaSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(betaSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(betaLabel)))
                    .addComponent(innerInnerRadiusSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addComponent(separator, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(networkPicture, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(startButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(drawButton)))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {bandwidthLabel, radiusLabelName, reuseLabel});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(networkPicture, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(drawButton)
                                    .addComponent(startButton)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(radiusLabelName)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bandwidthLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(reuseLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(iterationsLabel)
                                .addGap(12, 12, 12)
                                .addComponent(iterationsSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(innerRadiusLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(innerRadiusSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(17, 17, 17)
                                .addComponent(innerInnerRadiusLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(innerInnerRadiusSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addComponent(reuse1Button)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(reuse3Button)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(FFRButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(gammaLabel)
                                    .addComponent(betaLabel)
                                    .addComponent(alphaLabel))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(powerGraf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(betaSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(gammaSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(alphaSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(deltaSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(deltaLabel))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addComponent(separator, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 605, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {alphaSlider, betaSlider, gammaSlider});

        jScrollPane1.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1020, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 798, Short.MAX_VALUE)
        );


        pack();
	}

	private void printReuse() {
		double helper = 3*((double)(deltaSlider.getValue()*alphaSlider.getValue())+(double)(100-deltaSlider.getValue())*(double)(2*gammaSlider.getValue()+betaSlider.getValue())/3)/10000;
		DecimalFormat num;
		if(helper>=10.0){
			num=new DecimalFormat("##.###");
		} else {
			num=new DecimalFormat("#.###");
		}

		String string=num.format(helper);
		string=string.replace(',','.');
		helper=Double.valueOf(string);
		reuseLabel.setText("Reuse ratio: "+helper+"/3");
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new UI().setVisible(true);
			}
		});
	}

	private class PowerGraf extends JPanel{
		
		private static final long serialVersionUID = 1L;

		public PowerGraf(){

			drawGraf();
		}
		@Override
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			int maxHeight=getHeight();
			int maxWidth=getWidth();
			int rectangleWidth=150;
			int rectangleHeight=109;
			g.setColor(Color.black);
			g.drawLine(15, maxHeight-15, 15, 15 );
			g.drawLine(15, maxHeight-15, maxWidth-15, maxHeight-15);
			String axisName="Pmax";
			g.drawChars(axisName.toCharArray(), 0, 4, 3, 14);
			axisName="B";
			g.drawChars(axisName.toCharArray(), 0, 1, maxWidth-10, maxHeight-10);
			//reuse 1
			g.setColor(Color.orange);
			int reuse1Width=(int)((double)rectangleWidth*(double)deltaSlider.getValue()/100.00);
			int reuse1Height=(int)((double)rectangleHeight*(double)alphaSlider.getValue()/100.00);
			g.fillRect(16, 16+rectangleHeight-reuse1Height, reuse1Width , reuse1Height);

			//reuse 3 1/3
			g.setColor(Color.magenta);
			int reuse3aWidth=(int)(((double)rectangleWidth-(double)reuse1Width)/3);
			int reuse3aHeight=(int)((double)rectangleHeight*(double)gammaSlider.getValue()/100.00);
			g.fillRect(16+reuse1Width, 16+rectangleHeight-reuse3aHeight, reuse3aWidth , reuse3aHeight);

			//reuse 3 1/3
			g.setColor(Color.green);
			int reuse3bWidth=(int)(((double)rectangleWidth-(double)reuse1Width)/3);
			int reuse3bHeight=(int)((double)rectangleHeight*(double)gammaSlider.getValue()/100.00);
			g.fillRect(16+reuse1Width+reuse3aWidth, 16+rectangleHeight-reuse3bHeight, reuse3bWidth , reuse3bHeight);

			//reuse 3 1/3
			g.setColor(Color.blue);
			int reuse3cWidth=(int)(((double)rectangleWidth-(double)reuse1Width)/3);
			int reuse3cHeight=(int)((double)rectangleHeight*(double)betaSlider.getValue()/100.00);
			g.fillRect(16+reuse1Width+reuse3aWidth+reuse3bWidth, 16+rectangleHeight-reuse3cHeight, reuse3cWidth , reuse3cHeight);

		}
		public void drawGraf(){

			repaint();
		}
	}

	private class NetworkPicture extends JPanel {

		private static final long serialVersionUID = 1L;


		public NetworkPicture(){

			drawPicture();
		}
		@Override
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			int maxHeight=getHeight();
			int maxWidth=getWidth();

			int pixelRadius;

			int radUser;
			int rad;
			if(entireNetwork){
				if(!((double)maxWidth/(8)>(double)maxHeight/(5*Math.sqrt(3)))){
					pixelRadius=(int)((double)maxWidth/(8));
				} else {
					pixelRadius=(int)((double)maxHeight/(5*Math.sqrt(3)));
				}
				radUser=2;


			} else {
				//if we want to draw one cell only
				if(!((double)maxWidth/(2)>(double)maxHeight/(Math.sqrt(3)))){
					pixelRadius=(int)((double)maxWidth/(2));
				} else {
					pixelRadius=(int)((double)maxHeight/(Math.sqrt(3)));
				}
				radUser=6;


			}
			pixelRadius-=5;
			rad=((int)(MINIMAL_DISTANCE*pixelRadius/DEFAULT_RADIUS));
			if(rad<8){
				rad=8;
			}

			int centralX=maxWidth/2;
			int centralY=maxHeight/2;

			Network network=Network.getInstance();
			List<eNodeB> cells=network.getCells();
			for(int i=0;i<cells.size();i++){
				eNodeB currentNode = cells.get(i);
				Point2D.Double cellCenter = currentNode.getCellCenter();

				int x=((int)(cellCenter.x*pixelRadius/DEFAULT_RADIUS))+centralX;
				int y=((int)(cellCenter.y*pixelRadius/DEFAULT_RADIUS))+centralY;


				List<Point2D.Double> boundries=currentNode.getBoundries();
				List<Sector> nodeSectors = currentNode.getSectors();

				int[] poligonBorderX = new int[5];
				int[] poligonBorderY = new int[5];
				//draw outer cell
				for(int k=0;k<nodeSectors.size();k++){
					switch(nodeSectors.get(k).getID()){
					case 0: 
						g.setColor(Color.blue); 
						poligonBorderX[2]=((int)(boundries.get(0).x*pixelRadius/DEFAULT_RADIUS))+centralX;
						poligonBorderY[2]=((int)(boundries.get(0).y*pixelRadius/DEFAULT_RADIUS))+centralY;
						poligonBorderX[3]=((int)(boundries.get(1).x*pixelRadius/DEFAULT_RADIUS))+centralX;
						poligonBorderY[3]=((int)(boundries.get(1).y*pixelRadius/DEFAULT_RADIUS))+centralY;
						break;
					case 1:
						poligonBorderX[2]=((int)(boundries.get(2).x*pixelRadius/DEFAULT_RADIUS))+centralX;
						poligonBorderY[2]=((int)(boundries.get(2).y*pixelRadius/DEFAULT_RADIUS))+centralY;
						poligonBorderX[3]=((int)(boundries.get(3).x*pixelRadius/DEFAULT_RADIUS))+centralX;
						poligonBorderY[3]=((int)(boundries.get(3).y*pixelRadius/DEFAULT_RADIUS))+centralY;
						g.setColor(Color.magenta); 
						break;
					case 2:
						poligonBorderX[2]=((int)(boundries.get(4).x*pixelRadius/DEFAULT_RADIUS))+centralX;
						poligonBorderY[2]=((int)(boundries.get(4).y*pixelRadius/DEFAULT_RADIUS))+centralY;
						poligonBorderX[3]=((int)(boundries.get(5).x*pixelRadius/DEFAULT_RADIUS))+centralX;
						poligonBorderY[3]=((int)(boundries.get(5).y*pixelRadius/DEFAULT_RADIUS))+centralY;
						g.setColor(Color.green); 
						break;
					}
					poligonBorderX[0]=x;
					poligonBorderY[0]=y;
					poligonBorderX[1]=((int)(nodeSectors.get(k).getAdditionalBoundries().get(0).x*pixelRadius/DEFAULT_RADIUS))+centralX;
					poligonBorderY[1]=((int)(nodeSectors.get(k).getAdditionalBoundries().get(0).y*pixelRadius/DEFAULT_RADIUS))+centralY;
					poligonBorderX[4]=((int)(nodeSectors.get(k).getAdditionalBoundries().get(1).x*pixelRadius/DEFAULT_RADIUS))+centralX;
					poligonBorderY[4]=((int)(nodeSectors.get(k).getAdditionalBoundries().get(1).y*pixelRadius/DEFAULT_RADIUS))+centralY;

					g.fillPolygon(poligonBorderX, poligonBorderY, 5);
				}


				if(gammaSlider.getValue()>alphaSlider.getValue()){
					//draw inner inner cell
					if(gammaSlider.getValue()!=0){
						drawInnerInnerCell(cells, pixelRadius, x, y, g);
					}

					//draw inner cell
					if(alphaSlider.getValue()!=0){
						drawInnerCell(cells, pixelRadius, x, y, g);
					}
				} else {
					//draw inner cell
					if(alphaSlider.getValue()!=0){
						drawInnerCell(cells, pixelRadius, x, y, g);
					}

					//draw inner inner cell
					if(gammaSlider.getValue()!=0){
						drawInnerInnerCell(cells, pixelRadius, x, y, g);
					}
				}

				//draw center of the cell
				g.setColor(Color.black);
				g.fillOval(x-rad/2, y-rad/2, rad, rad);

				//draw border lines
				g.setColor(Color.red);				
				for(int k=0;k<boundries.size();k++){

					int helper=k+1;
					if(helper==boundries.size()){
						helper=0;
					}
					Point2D.Double point1=boundries.get(k);
					int x1 = ((int)(point1.x*pixelRadius/DEFAULT_RADIUS))+centralX;
					int y1 = ((int)(point1.y*pixelRadius/DEFAULT_RADIUS))+centralY;
					Point2D.Double point2=boundries.get(helper);
					int x2 = ((int)(point2.x*pixelRadius/DEFAULT_RADIUS))+centralX;
					int y2 = ((int)(point2.y*pixelRadius/DEFAULT_RADIUS))+centralY;
					g.drawLine(x1, y1, x2, y2);
				}

				//draw sector borders
				for(int k=0;k<nodeSectors.size();k++){
					List<Point2D.Double> additionalBoundries=nodeSectors.get(k).getAdditionalBoundries();
					for(int l=0;l<additionalBoundries.size();l++){
						g.setColor(Color.cyan);
						Point2D.Double point1=additionalBoundries.get(l);
						int x1 = ((int)(point1.x*pixelRadius/DEFAULT_RADIUS))+centralX;
						int y1 = ((int)(point1.y*pixelRadius/DEFAULT_RADIUS))+centralY;
						g.drawLine(x1, y1, x, y);
					}
				}

				//draw users
				for(int k=0;k<nodeSectors.size();k++){
					List<User> users=nodeSectors.get(k).getUsers();

					//TODO draw antenna Pattern for debugging
					/*
					List<Point2D.Double> debug=nodeSectors.get(k).debugAntennaPattern();
					for(int z=0;z<debug.size();z++){
					int xD =  ((int)(debug.get(z).x*pixelRadius/DEFAULT_RADIUS))+x;
					int yD = ((int)(debug.get(z).y*pixelRadius/DEFAULT_RADIUS))+y;

					g.fillOval(xD-radUser*2, yD-radUser*2, radUser*4, radUser*4);
					}
					 */					
					//TODO for debugging
					for(int l=0;l<users.size();l++){
						switch(k){
						case 0:
							g.setColor(Color.lightGray);
							break;
						case 1:
							g.setColor(Color.yellow);
							break;
						case 2:
							g.setColor(Color.white);
							break;
						}
						Point2D.Double point1=users.get(l).getPosition();
						int x1 = ((int)(point1.x*pixelRadius/DEFAULT_RADIUS))+x;
						int y1 = ((int)(point1.y*pixelRadius/DEFAULT_RADIUS))+y;
						g.fillOval(x1-radUser/2, y1-radUser/2, radUser, radUser);
					}
				}

			}

		}

		/**
		 * 
		 * @param cells
		 * @param pixelRadius
		 * @param x
		 * @param y
		 * @param g
		 */
		private void drawInnerCell(List<eNodeB> cells, double pixelRadius, int x, int y, Graphics g) {
			g.setColor(Color.orange);
			int[] poligonBorderX = new int[6];
			int[] poligonBorderY = new int[6];
			for(int k=0;k<cells.get(0).getBoundries().size();k++){
				Point2D.Double point1=cells.get(0).getBoundries().get(k);
				poligonBorderX[k] = ((int)(point1.x*((double)alphaSlider.getValue()/100)*pixelRadius/DEFAULT_RADIUS))+x;
				poligonBorderY[k] = ((int)(point1.y*((double)alphaSlider.getValue()/100)*pixelRadius/DEFAULT_RADIUS))+y;
			}
			g.fillPolygon(poligonBorderX, poligonBorderY, 6);

		}
		/**
		 * 
		 * @param cells
		 * @param pixelRadius
		 * @param x
		 * @param y
		 * @param g
		 */
		private void drawInnerInnerCell(List<eNodeB> cells, double pixelRadius, int x, int y, Graphics g) {
			for(int k=0;k<cells.get(0).getSectors().size();k++){
				List<Point2D.Double> boundries0=cells.get(0).getBoundries();
				int[] poligonBorderX = new int[3];
				int[] poligonBorderY = new int[3];
				switch(cells.get(0).getSectors().get(k).getID()){
				case 0: 
					g.setColor(Color.green); //green
					poligonBorderX[2]=((int)(boundries0.get(0).x*pixelRadius*((double)gammaSlider.getValue()/100)/DEFAULT_RADIUS))+x;
					poligonBorderY[2]=((int)(boundries0.get(0).y*pixelRadius*((double)gammaSlider.getValue()/100)/DEFAULT_RADIUS))+y;
					break;
				case 1:
					poligonBorderX[2]=((int)(boundries0.get(2).x*pixelRadius*((double)gammaSlider.getValue()/100)/DEFAULT_RADIUS))+x;
					poligonBorderY[2]=((int)(boundries0.get(2).y*pixelRadius*((double)gammaSlider.getValue()/100)/DEFAULT_RADIUS))+y;
					g.setColor(Color.green); //blue
					break;
				case 2:							
					poligonBorderX[2]=((int)(boundries0.get(4).x*pixelRadius*((double)gammaSlider.getValue()/100)/DEFAULT_RADIUS))+x;
					poligonBorderY[2]=((int)(boundries0.get(4).y*pixelRadius*((double)gammaSlider.getValue()/100)/DEFAULT_RADIUS))+y;
					g.setColor(Color.magenta); //magenta
					break;
				}
				poligonBorderX[0]=x;
				poligonBorderY[0]=y;
				poligonBorderX[1]=((int)(cells.get(0).getSectors().get(k).getAdditionalBoundries().get(0).x*pixelRadius*((double)gammaSlider.getValue()/100)/DEFAULT_RADIUS))+x;
				poligonBorderY[1]=((int)(cells.get(0).getSectors().get(k).getAdditionalBoundries().get(0).y*pixelRadius*((double)gammaSlider.getValue()/100)/DEFAULT_RADIUS))+y;
				
				g.fillPolygon(poligonBorderX, poligonBorderY, 3);
			}

			for(int k=0;k<cells.get(0).getSectors().size();k++){
				int[] poligonBorderX = new int[3];
				int[] poligonBorderY = new int[3];
				List<Point2D.Double> boundries0=cells.get(0).getBoundries();
				double centralAngle=0;
				switch(cells.get(0).getSectors().get(k).getID()){
				case 0: 
					centralAngle=60;
					g.setColor(Color.green); //green
					poligonBorderX[2]=((int)((DEFAULT_RADIUS*Math.sqrt(3)/2)*Math.sin(centralAngle*2*Math.PI/360)*pixelRadius*((double)gammaSlider.getValue()/100)/DEFAULT_RADIUS))+x;
					poligonBorderY[2]=((int)((DEFAULT_RADIUS*Math.sqrt(3)/2)*Math.cos(centralAngle*2*Math.PI/360)*pixelRadius*((double)gammaSlider.getValue()/100)/DEFAULT_RADIUS))+y;
					poligonBorderX[1]=((int)(boundries0.get(1).x*pixelRadius*((double)gammaSlider.getValue()/100)/DEFAULT_RADIUS))+x;
					poligonBorderY[1]=((int)(boundries0.get(1).y*pixelRadius*((double)gammaSlider.getValue()/100)/DEFAULT_RADIUS))+y;

					break;
				case 1:
					centralAngle=300;
					poligonBorderX[2]=((int)((DEFAULT_RADIUS*Math.sqrt(3)/2)*Math.sin(centralAngle*2*Math.PI/360)*pixelRadius*((double)gammaSlider.getValue()/100)/DEFAULT_RADIUS))+x;
					poligonBorderY[2]=((int)((DEFAULT_RADIUS*Math.sqrt(3)/2)*Math.cos(centralAngle*2*Math.PI/360)*pixelRadius*((double)gammaSlider.getValue()/100)/DEFAULT_RADIUS))+y;
					poligonBorderX[1]=((int)(boundries0.get(3).x*pixelRadius*((double)gammaSlider.getValue()/100)/DEFAULT_RADIUS))+x;
					poligonBorderY[1]=((int)(boundries0.get(3).y*((double)gammaSlider.getValue()/100)*pixelRadius/DEFAULT_RADIUS))+y;
					g.setColor(Color.green); //blue
					break;
				case 2:
					centralAngle=180;
					poligonBorderX[2]=((int)((DEFAULT_RADIUS*Math.sqrt(3)/2)*Math.sin(centralAngle*2*Math.PI/360)*pixelRadius*((double)gammaSlider.getValue()/100)/DEFAULT_RADIUS))+x;
					poligonBorderY[2]=((int)((DEFAULT_RADIUS*Math.sqrt(3)/2)*Math.cos(centralAngle*2*Math.PI/360)*pixelRadius*((double)gammaSlider.getValue()/100)/DEFAULT_RADIUS))+y;
					poligonBorderX[1]=((int)(boundries0.get(5).x*pixelRadius*((double)gammaSlider.getValue()/100)/DEFAULT_RADIUS))+x;
					poligonBorderY[1]=((int)(boundries0.get(5).y*pixelRadius*((double)gammaSlider.getValue()/100)/DEFAULT_RADIUS))+y;
					g.setColor(Color.magenta); //magenta
					break;
				}
				poligonBorderX[0]=x;
				poligonBorderY[0]=y;

				g.fillPolygon(poligonBorderX, poligonBorderY, 3);
			}

			for(int k=0;k<cells.get(0).getSectors().size();k++){
				List<Point2D.Double> boundries0=cells.get(0).getBoundries();
				int[] poligonBorderX = new int[3];
				int[] poligonBorderY = new int[3];
				double centralAngle=0;
				switch(cells.get(0).getSectors().get(k).getID()){
				case 0:
					centralAngle=60;
					g.setColor(Color.magenta); //green
					poligonBorderX[1]=((int)(boundries0.get(0).x*pixelRadius*((double)gammaSlider.getValue()/100)/DEFAULT_RADIUS))+x;
					poligonBorderY[1]=((int)(boundries0.get(0).y*pixelRadius*((double)gammaSlider.getValue()/100)/DEFAULT_RADIUS))+y;
					poligonBorderX[2]=((int)((DEFAULT_RADIUS*Math.sqrt(3)/2)*Math.sin(centralAngle*2*Math.PI/360)*pixelRadius*((double)gammaSlider.getValue()/100)/DEFAULT_RADIUS))+x;
					poligonBorderY[2]=((int)((DEFAULT_RADIUS*Math.sqrt(3)/2)*Math.cos(centralAngle*2*Math.PI/360)*pixelRadius*((double)gammaSlider.getValue()/100)/DEFAULT_RADIUS))+y;
					break;
				case 1:
					centralAngle=300;
					poligonBorderX[2]=((int)(boundries0.get(2).x*pixelRadius*((double)gammaSlider.getValue()/100)/DEFAULT_RADIUS))+x;
					poligonBorderY[2]=((int)(boundries0.get(2).y*pixelRadius*((double)gammaSlider.getValue()/100)/DEFAULT_RADIUS))+y;
					poligonBorderX[1]=((int)((DEFAULT_RADIUS*Math.sqrt(3)/2)*Math.sin(centralAngle*2*Math.PI/360)*pixelRadius*((double)gammaSlider.getValue()/100)/DEFAULT_RADIUS))+x;
					poligonBorderY[1]=((int)((DEFAULT_RADIUS*Math.sqrt(3)/2)*Math.cos(centralAngle*2*Math.PI/360)*pixelRadius*((double)gammaSlider.getValue()/100)/DEFAULT_RADIUS))+y;
					g.setColor(Color.blue); //blue
					break;
				case 2:
					centralAngle=180;
					poligonBorderX[1]=((int)(boundries0.get(4).x*pixelRadius*((double)gammaSlider.getValue()/100)/DEFAULT_RADIUS))+x;
					poligonBorderY[1]=((int)(boundries0.get(4).y*pixelRadius*((double)gammaSlider.getValue()/100)/DEFAULT_RADIUS))+y;
					poligonBorderX[2]=((int)((DEFAULT_RADIUS*Math.sqrt(3)/2)*Math.sin(centralAngle*2*Math.PI/360)*pixelRadius*((double)gammaSlider.getValue()/100)/DEFAULT_RADIUS))+x;
					poligonBorderY[2]=((int)((DEFAULT_RADIUS*Math.sqrt(3)/2)*Math.cos(centralAngle*2*Math.PI/360)*pixelRadius*((double)gammaSlider.getValue()/100)/DEFAULT_RADIUS))+y;
					g.setColor(Color.blue); //magenta
					break;

				}
				poligonBorderX[0]=x;
				poligonBorderY[0]=y;

				g.fillPolygon(poligonBorderX, poligonBorderY, 3);
			}
			for(int k=0;k<cells.get(0).getSectors().size();k++){
				List<Point2D.Double> boundries0=cells.get(0).getBoundries();
				int[] poligonBorderX = new int[3];
				int[] poligonBorderY = new int[3];
				switch(cells.get(0).getSectors().get(k).getID()){
				case 0: 							
					g.setColor(Color.magenta);
					poligonBorderX[2]=((int)(boundries0.get(1).x*pixelRadius*((double)gammaSlider.getValue()/100)/DEFAULT_RADIUS))+x;
					poligonBorderY[2]=((int)(boundries0.get(1).y*pixelRadius*((double)gammaSlider.getValue()/100)/DEFAULT_RADIUS))+y;
					break;
				case 1:
					poligonBorderX[2]=((int)(boundries0.get(3).x*pixelRadius*((double)gammaSlider.getValue()/100)/DEFAULT_RADIUS))+x;
					poligonBorderY[2]=((int)(boundries0.get(3).y*((double)gammaSlider.getValue()/100)*pixelRadius/DEFAULT_RADIUS))+y;
					g.setColor(Color.blue); 
					break;
				case 2:	
					poligonBorderX[2]=((int)(boundries0.get(5).x*pixelRadius*((double)gammaSlider.getValue()/100)/DEFAULT_RADIUS))+x;
					poligonBorderY[2]=((int)(boundries0.get(5).y*pixelRadius*((double)gammaSlider.getValue()/100)/DEFAULT_RADIUS))+y;
					g.setColor(Color.blue); 
					break;
				}
				poligonBorderX[0]=x;
				poligonBorderY[0]=y;
				poligonBorderX[1]=((int)(cells.get(0).getSectors().get(k).getAdditionalBoundries().get(1).x*pixelRadius*((double)gammaSlider.getValue()/100)/DEFAULT_RADIUS))+x;
				poligonBorderY[1]=((int)(cells.get(0).getSectors().get(k).getAdditionalBoundries().get(1).y*pixelRadius*((double)gammaSlider.getValue()/100)/DEFAULT_RADIUS))+y;

				g.fillPolygon(poligonBorderX, poligonBorderY, 3);
			}
		}
		public void drawPicture(){			
			repaint();
		}
	}
	public void drawNetworkPicture() {
		networkPicture.drawPicture();
	}
}