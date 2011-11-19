/*
 * Distributed under the terms of the GNU GPL version 3.
 * 
 * Copyright (c) 2011 Igor Maraviæ <igorm@etf.rs>
 */

package rs.etf.igor;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

public class eNodeB implements Constants{

	//cell should know what is the maximum of users that it can serve
	private double x;	//x coordinate of cell
	private double y;	//y coordinate of cell
	private double B=DEFAULT_B;	//bandwidth in MHz
	private int id;		//unique cell id

	private double alpha;
	private double beta;
	private double gamma;
	private double delta;
	private double R=DEFAULT_RADIUS;

	private List<Sector> mySectors=new LinkedList<Sector>();
	private List<Point2D.Double> boundries=new LinkedList<Point2D.Double>();


	/**
	 * Constructor
	 * @param xCo
	 * @param yCo
	 * @param alpha
	 * @param beta
	 * @param gamma
	 * @param delta
	 * @param id
	 */
	public eNodeB(double xCo, double yCo, double alpha, double beta, double gamma, double delta, int id) {
		x=xCo;
		y=yCo;

		this.id=id;
		this.setAlpha(alpha);
		this.setBeta(beta);
		this.setGamma(gamma);
		this.setDelta(delta);


		for(int j=0;j<6;j++){
			switch(j){
			case 0: 
				boundries.add(new Point2D.Double(x+R, y));
				break;
			case 1: 
				boundries.add(new Point2D.Double(x+R/2, y+R*Math.sqrt(3)/2));
				break;
			case 2:
				boundries.add(new Point2D.Double(x-R/2, y+R*Math.sqrt(3)/2));
				break;
			case 3: 
				boundries.add(new Point2D.Double(x-R, y));
				break;
			case 4: 
				boundries.add(new Point2D.Double(x-R/2, y-R*Math.sqrt(3)/2));
				break;
			case 5: 
				boundries.add(new Point2D.Double(x+R/2, y-R*Math.sqrt(3)/2));
				break;
			}
		}

		for(int j=0;j<NoSECTORS;j++){		
			mySectors.add(new Sector(j,this));
		}
	}

	public List<Sector> getSectors(){
		return mySectors;
	}

	public List<Point2D.Double> getBoundries(){
		return boundries;
	}

	public Point2D.Double getCellCenter(){
		return new Point2D.Double(x, y);
	}

	public int getId() {
		return id;
	}

	public void randomizeUserPositions() {
		for(int i=0;i<mySectors.size();i++){
			mySectors.get(i).randomizeUserPositions();
		}		
	}	

	private void setAlpha(double alpha) {
		this.alpha = alpha;
	}

	public double getAlpha() {
		return alpha;
	}



	private void setBeta(double beta) {
		this.beta = beta;
	}

	public double getBeta() {
		return beta;
	}

	private void setGamma(double gamma) {
		this.gamma = gamma;
	}

	public double getGamma() {
		return gamma;
	}



	private void setDelta(double delta) {
		this.delta = delta;
	}

	public double getDelta() {
		return delta;
	}

	public double getB() {
		return B;
	}

	
	public void setSNR() {
		for(int i=0;i<mySectors.size();i++){
			mySectors.get(i).setSNR();
		}	
		allocateUnusedResources();
	}

	private void allocateUnusedResources() {
		boolean flag;
		int[] counter=new int[mySectors.size()];
		do{
			flag=false;
			for(int i=0;i<mySectors.size();i++){
				List<RadioResourceBlock> unusedResources;
				Sector sector = mySectors.get(i);
				unusedResources=sector.getUnusedResources();
				if(unusedResources.size()!=0){
					flag=true;
					sector.allocateResources(unusedResources,counter[i]++);

				}

			}
		}while (flag);
	}	
}
