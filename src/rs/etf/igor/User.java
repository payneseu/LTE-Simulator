package rs.etf.igor;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class User implements Constants {
	private int id;
	private Sector parent;
	private List<RadioResourceBlock> myResources= new LinkedList<RadioResourceBlock>();
	private Point2D.Double position;
	private double[] shadowingValues=new double[NUMBER_OF_CELLS];
	private double propagationLoss;
	private double antennaPattern;

	/**
	 * Constructor
	 * @param id
	 * @param resources
	 * @param position
	 * @param parent
	 */
	public User(int id, List<RadioResourceBlock> resources,Point2D.Double position, Sector parent) {
		this.id=id;
		setResources(resources);
		this.setParent(parent);
		this.position = position;
		setShadowingValues();
		propagationLoss=parent.propagationLoss(position);
		antennaPattern=parent.antennaPattern(position);

	}

	public User(int id, Point2D.Double position, Sector parent) {
		this.id=id;
		this.setParent(parent);
		this.position = position;
		setShadowingValues();
		propagationLoss=parent.propagationLoss(position);
		antennaPattern=parent.antennaPattern(position);

	}	

	public void setShadowingValues() {
		Random generator=new Random();
		shadowingValues[parent.getParent().getId()]=generator.nextGaussian()*SHADOWING_DEVIATION;
		for(int i=0;i<NUMBER_OF_CELLS;i++){
			if(i==parent.getParent().getId()){
				continue;
			} else {
				int helper=generator.nextInt(2);
				if(helper==0){
					shadowingValues[i]=shadowingValues[parent.getParent().getId()];
				} else {
					shadowingValues[i]=generator.nextGaussian()*SHADOWING_DEVIATION;
				}
			}
		}

	}

	public double[] getShadowingValues(){
		return shadowingValues;
	}

	public Point2D.Double getPosition(){
		return position;
	}

	public void setPosition(Point2D.Double position){
		this.position=position;
		propagationLoss=parent.propagationLoss(position);
		antennaPattern=parent.antennaPattern(position);
	}

	public void resetResources(){
		for(int i=0;i<myResources.size();i++){
			myResources.get(i).setP(myResources.get(i).getPmax());
			myResources.get(i).setUser(null);
		}
		myResources.clear();
	}

	public int getId(){
		return id;
	}

	private void setParent(Sector parent) {
		this.parent = parent;
	}

	public Sector getParent() {
		return parent;
	}

	public List<RadioResourceBlock> getMyResources() {
		return myResources;
	}

	public int getNumberOfPRBs() {
		return myResources.size();
	}

	public void addResource(RadioResourceBlock resource){
		resource.setUser(this);
		myResources.add(resource);
	}

	public void setResources(List<RadioResourceBlock> resources){
		myResources=resources;
		for(int i=0;i<myResources.size();i++){
			myResources.get(i).setUser(this);
		}
	}

	public void removeResource(RadioResourceBlock radioResourceBlock) {
		for(int i=0;i<myResources.size();i++){
			if(myResources.get(i).equals(radioResourceBlock)){
				myResources.get(i).setUser(null);
				myResources.remove(i);
				break;
			}
		}		
	}

	public double getAntennaPattern() {
		return antennaPattern;
	}

	public double getPropagationLoss() {
		return propagationLoss;
	}
}