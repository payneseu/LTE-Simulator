/*
 * Distributed under the terms of the GNU GPL version 3.
 * 
 * Copyright (c) 2011 Igor Maraviæ <igorm@etf.rs>
 */

package rs.etf.igor;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class Sector implements Constants{
	private List<RadioResourceBlock> radioReuse1= new LinkedList<RadioResourceBlock>();
	private List<RadioResourceBlock> radioReuse3LowP= new LinkedList<RadioResourceBlock>();
	private List<RadioResourceBlock> radioReuse3HighP= new LinkedList<RadioResourceBlock>();
	private int id;
	private eNodeB parent;		

	private double centralAngle;

	private int numberOfPRBs;
	private double sectorP=P_MAX;

	private List<Point2D.Double> additionalBoundries= new LinkedList<Point2D.Double>();
	private List<User> myUsers= new LinkedList<User>();

	/**
	 * 
	 * @param mainReuse3F
	 * @param parent
	 */
	Sector(int mainReuse3F, eNodeB parent){
		this.setParent(parent);
		id=mainReuse3F;
		switch(id){
		case 0: 
			additionalBoundries.add(new Point2D.Double(parent.getCellCenter().x+DEFAULT_RADIUS*3/4,parent.getCellCenter().y-DEFAULT_RADIUS*Math.sqrt(3)/4));
			additionalBoundries.add(new Point2D.Double(parent.getCellCenter().x,parent.getCellCenter().y+DEFAULT_RADIUS*Math.sqrt(3)/2));
			break;
		case 1:
			additionalBoundries.add(new Point2D.Double(parent.getCellCenter().x,parent.getCellCenter().y+DEFAULT_RADIUS*Math.sqrt(3)/2));
			additionalBoundries.add(new Point2D.Double(parent.getCellCenter().x-DEFAULT_RADIUS*3/4,parent.getCellCenter().y-DEFAULT_RADIUS*Math.sqrt(3)/4));
			break;
		case 2: 
			additionalBoundries.add(new Point2D.Double(parent.getCellCenter().x-DEFAULT_RADIUS*3/4,parent.getCellCenter().y-DEFAULT_RADIUS*Math.sqrt(3)/4));
			additionalBoundries.add(new Point2D.Double(parent.getCellCenter().x+DEFAULT_RADIUS*3/4,parent.getCellCenter().y-DEFAULT_RADIUS*Math.sqrt(3)/4));
			break;
		}
		int noPRBreuse3=(int)(MAX_No_PRB*(1-parent.getDelta()));
		noPRBreuse3=(int)(noPRBreuse3/3) * 3;
		int noPRBreuse1=MAX_No_PRB-noPRBreuse3;
		if(parent.getDelta()==0){
			noPRBreuse1=0;
		}

		int helper13=noPRBreuse3/3;
		int helper23=noPRBreuse3*2/3;
		int helper33=noPRBreuse3;
		double minDistanceHigh;
		double minDistanceLow=0;
		double minDistanceR1=0;
		
		minDistanceHigh=Network.getInstance().getInnerCellRadius();
		if(parent.getAlpha()>=parent.getGamma()){
			minDistanceR1=Network.getInstance().getInnerInnerCellRadius();
		} else {
			minDistanceLow=Network.getInstance().getInnerInnerCellRadius();
		}
		
		if(parent.getAlpha()==0 || parent.getGamma()==0){
			minDistanceLow=0;
			minDistanceR1=0;
		}
		
		if(parent.getAlpha()==0 && parent.getGamma()==0){
			minDistanceHigh=0;
		}

		double power=sectorP-10*Math.log10((double)noPRBreuse1+(double)helper23+(double)helper13);
		for(int i=0;i<(int)noPRBreuse1+noPRBreuse3;i++){
			if(i<noPRBreuse1){
				radioReuse1.add(new RadioResourceBlock(i,power+10*Math.log10(parent.getAlpha()),minDistanceR1,this));
			} else {
				if((((i-noPRBreuse1)<helper13)&&mainReuse3F==0) 
						|| (((i-noPRBreuse1)<helper23)&&((i-noPRBreuse1)>=helper13)&&mainReuse3F==1)
						|| (((i-noPRBreuse1)<helper33)&&((i-noPRBreuse1)>=helper23)&&mainReuse3F==2)){
					radioReuse3HighP.add(new RadioResourceBlock(i,power+10*Math.log10(parent.getBeta()),minDistanceHigh,this));
				} else {
					radioReuse3LowP.add(new RadioResourceBlock(i,power+10*Math.log10(parent.getGamma()),minDistanceLow,this));
				}
			}
		}
		if(parent.getBeta()==0){
			radioReuse3HighP.clear();				
		}
		if(parent.getAlpha()==0){
			radioReuse1.clear();
		}
		if(parent.getGamma()==0){
			radioReuse3LowP.clear();
		}

		numberOfPRBs=radioReuse1.size()+radioReuse3HighP.size()+radioReuse3LowP.size();
		setCentralAngle();

		if(parent.getId()==0){			
			generateUsers();
		}
	}

	/**
	 * 
	 * @return all resources
	 */
	public List<RadioResourceBlock> getRadioResources(){
		List<RadioResourceBlock> helper = new LinkedList<RadioResourceBlock>();
		helper.addAll(radioReuse3LowP);
		helper.addAll(radioReuse1);
		helper.addAll(radioReuse3HighP);
		return helper;
	}

	/**
	 * 
	 * @param userPosition
	 * @return returns propagation loss + shadowing
	 */
	public double propagationLoss(Point2D.Double userPosition){
		double radius;
		radius=getRadius(userPosition);
		radius/=1000;
		return 128.1+37.6*Math.log10(radius);
	}

	

	/**
	 * 
	 * @param angle angle in comparison with the center of the sector
	 * @return antenna gain
	 */
	public double antennaPattern(double angle){
		angle=angle-centralAngle;
		while(angle>180){
			angle-=360;
		}

		while (angle<-180){
			angle+=360;
		}
		double A=12*Math.pow(angle/70, 2);
		if(A<20){
			return -A;
		} else {
			return -20;
		}
	}

	/**
	 * 
	 * @param userPosition
	 * @return antenna gain
	 */
	public double antennaPattern(Point2D.Double userPosition){
		double radius=getRadius(userPosition);
		double angleC=Math.acos((userPosition.y-parent.getCellCenter().y)/radius)*360/(2*Math.PI);
		double angleS=Math.asin((userPosition.x-parent.getCellCenter().x)/radius)*360/(2*Math.PI);
		double angle;
		if(angleC<=90 && angleS<=90 && angleS>=0){
			angle=angleC;
		} else if (angleC<=90 && angleS<=0 && angleS>=-90){
			angle = 360 + angleS;
		} else if(angleC<=180 && angleC>=90 && angleS<=0 && angleS>=-90){
			angle = 360 - angleC;
		} else {
			angle = angleC;
		}
		return antennaPattern(angle);
	}

	private void setCentralAngle() {
		switch(id){
		case 0:
			centralAngle=60;
			break;
		case 1:
			centralAngle=300;
			break;
		case 2:
			centralAngle=180;
			break;
		}

	}

	private void generateUsers() {
		Statistics statistics = Statistics.getInstance();
		for(int i=0;i<NUMBER_OF_USERS_SECTOR;i++){
			Point2D.Double position= generateUserPosition();
			User user = new User(statistics.getUniqueUserID(), position, this);
			myUsers.add(user);
		}
	}

	public void setSNR() {

		List<RadioResourceBlock> resources=getRadioResources();

		setUserResources();

		allocateUnusedResources(resources);


	}

	/**
	 * 
	 * @param resources
	 */
	private void allocateUnusedResources(List<RadioResourceBlock> resources) {
		List<RadioResourceBlock> unusedBlocks=new LinkedList<RadioResourceBlock>();
		List<User> emptyUsers=new LinkedList<User>();

		for(int i=0;i<resources.size();i++){
			if(resources.get(i).getUser()==null){
				unusedBlocks.add(resources.get(i));
			}
		}

		for(int i=0;i<myUsers.size();i++){
			if(myUsers.get(i).getNumberOfPRBs()==0){
				emptyUsers.add(myUsers.get(i));
			}
		}
		//resources to users that have no resources
		allocateResources(emptyUsers,unusedBlocks);

		//resources to all users	
		int oldSize=unusedBlocks.size();
		while(unusedBlocks.size()!=0){
			allocateResources(myUsers,unusedBlocks);

			if(oldSize==unusedBlocks.size()){
				break;
			} else {
				oldSize=unusedBlocks.size();
			}
		}


	}

	private void allocateResources(List<User> myUsers,List<RadioResourceBlock> unusedBlocks){
		allocateResources(myUsers,unusedBlocks,0);
	}

	//by SNR
	private void allocateResources(List<User> myUsers,List<RadioResourceBlock> unusedBlocks,int counter) {

		int oldSize=unusedBlocks.size();
		while(true){

			List<User> users=new LinkedList<User>();
			for(int i=0;i<myUsers.size();i++){
				users.add(myUsers.get(i));
			}
			int[] randomizedIndex=randomizeResources(unusedBlocks,false);
			for(int k=0;k<unusedBlocks.size() && users.size()>0;k++){
				RadioResourceBlock resource=unusedBlocks.get(randomizedIndex[k]);
				int bestIndex=getBestSNRUser(users,resource,counter);
				if(bestIndex!=-1){
					myUsers.get(bestIndex).addResource(resource);
					resource.setSNR();
					if(resource.getUser()!=null){
						unusedBlocks.remove(randomizedIndex[k]);
						randomizedIndex= cleanRandomizedIndex(randomizedIndex,k);
						users.remove(bestIndex);
						k--;
					} 
				}
			}
			if(oldSize==unusedBlocks.size()){
				break;
			} else {
				oldSize=unusedBlocks.size();
			}
		}
	}

	/**
	 * 
	 * @param randomizedIndex
	 * @param k
	 * @return
	 */
	private int[] cleanRandomizedIndex(int[] randomizedIndex, int k) {
		int[] helper=new int[randomizedIndex.length-1];
		int borderIndex=randomizedIndex[k];
		for(int i=0;i<randomizedIndex.length;i++){
			if(i<k){
				if(randomizedIndex[i]>borderIndex){
					helper[i]=randomizedIndex[i]-1;
				} else {
					helper[i]=randomizedIndex[i];
				}
			} else if (i==k){
				continue;
			} else if (i>k){
				if(randomizedIndex[i]>borderIndex){
					helper[i-1]=randomizedIndex[i]-1;
				} else {
					helper[i-1]=randomizedIndex[i];
				}

			}
		}
		return helper;
	}


	//by SNR
	private void setUserResources() {		
		List<User> users=new LinkedList<User>();
		for(int i=0;i<myUsers.size();i++){
			users.add(myUsers.get(i));
		}
		int numberOfPRBsPerUser=(int)((double) numberOfPRBs/(double)NUMBER_OF_USERS_SECTOR);
		List<RadioResourceBlock> allResources=getRadioResources();
		int[] randomizedIndex=randomizeResources(allResources,false);
		for(int l=0;l<allResources.size() && users.size()>0;l++){
			if(allResources.get(randomizedIndex[l]).getUser()==null){
				RadioResourceBlock resource=allResources.get(randomizedIndex[l]);
				int bestIndex=getBestSNRUser(users,resource);
				if(bestIndex!=-1){
					users.get(bestIndex).addResource(resource);
					resource.setSNR();
					if(users.get(bestIndex).getNumberOfPRBs()>=numberOfPRBsPerUser){
						users.remove(bestIndex);
					}
				}
			}
		}
	}

	private int getBestSNRUser(List<User> users, RadioResourceBlock radioResourceBlock) {
		return getBestSNRUser(users, radioResourceBlock,0);
	}

	/**
	 * 
	 * @param users
	 * @param radioResourceBlock
	 * @return
	 */
	private int getBestSNRUser(List<User> users, RadioResourceBlock radioResourceBlock,int counter) {
		int bestIndex=-1;
		double maxSNR=-500;
		for(int k=0;k<users.size();k++){
			User user=users.get(k);
			double radius=getRadius(user);
			if(radius>=(radioResourceBlock.getMinDistance()-counter*10)){
				double helper=radioResourceBlock.getMaxSNR(user);
				if((helper)>maxSNR){
					bestIndex=k;
					maxSNR=helper;
				}
			}
		}		
		return bestIndex;
	}

	private double getRadius(User user) {
		return getRadius(user.getPosition());
	}

	private double getRadius(Point2D.Double userPosition) {
		return Math.sqrt(Math.pow(userPosition.x - parent.getCellCenter().x, 2)+Math.pow(userPosition.y- parent.getCellCenter().y, 2));
	}
	
	/**
	 * 
	 * @param allResources
	 * @param workFlag
	 * @return
	 */
	private int[] randomizeResources(List<RadioResourceBlock> allResources, boolean workFlag) {
		int[] randomizedIndex=new int[allResources.size()];
		for(int i=0;i<randomizedIndex.length;i++){
			randomizedIndex[i]=-1;
		}
		if(workFlag){
			int maxIndexLow3=radioReuse3LowP.size();
			int maxIndexHigh3=radioReuse3HighP.size();
			int maxIndex1=radioReuse1.size();

			Random generator=new Random();
			for(int i=0;i<maxIndexLow3;i++){
				do{
					int helper=generator.nextInt(maxIndexLow3);
					boolean flag=true;
					for(int l=0;l<maxIndexLow3;l++){
						if(randomizedIndex[l]==helper){
							flag=false;
							break;
						}
					}
					if(flag){
						randomizedIndex[i]=helper;
						break;
					}
				} while(true);
			}
			for(int i=maxIndexLow3;i<maxIndexLow3+maxIndex1;i++){
				do{
					int helper=generator.nextInt(maxIndex1)+maxIndexLow3;
					boolean flag=true;
					for(int l=maxIndexLow3;l<maxIndexLow3+maxIndex1;l++){
						if(randomizedIndex[l]==helper){
							flag=false;
							break;
						}
					}
					if(flag){
						randomizedIndex[i]=helper;
						break;
					}
				} while(true);
			}
			for(int i=maxIndexLow3+maxIndex1;i<maxIndexLow3+maxIndexHigh3+maxIndex1;i++){
				do{
					int helper=generator.nextInt(maxIndexHigh3)+maxIndexLow3+maxIndex1;
					boolean flag=true;
					for(int l=maxIndexLow3+maxIndex1;l<maxIndexLow3+maxIndexHigh3+maxIndex1;l++){
						if(randomizedIndex[l]==helper){
							flag=false;
							break;
						}
					}
					if(flag){
						randomizedIndex[i]=helper;
						break;
					}
				} while(true);
			}
		} else {
			Random generator=new Random();
			for(int i=0;i<randomizedIndex.length;i++){
				do{
					int helper=generator.nextInt(randomizedIndex.length);
					boolean flag=true;
					for(int l=0;l<randomizedIndex.length;l++){
						if(randomizedIndex[l]==helper){
							flag=false;
							break;
						}
					}
					if(flag){
						randomizedIndex[i]=helper;
						break;
					}
				} while(true);
			}
		}


		return randomizedIndex;
	}

	/**
	 * 
	 * @return user position in Decart's coordinates
	 */
	private Double generateUserPosition() {
		double x;
		double y;
		double[] helper;
		helper=generateRandomRadAngle();
		double radius=helper[0];
		double angle=helper[1];
		x=radius*Math.sin(angle*2*Math.PI/360);
		y=radius*Math.cos(angle*2*Math.PI/360);
		return new Point2D.Double(x, y);
	}

	//TODO for debugging 
	public List<Point2D.Double> debugAntennaPattern(){
		List<Point2D.Double> bla = new LinkedList<Point2D.Double>();
		for(int angle=0;angle<=360;angle+=10){
			double x=(DEFAULT_RADIUS+1000)*Math.sin(angle*2*Math.PI/360)+parent.getCellCenter().x;
			double y=(DEFAULT_RADIUS+1000)*Math.cos(angle*2*Math.PI/360)+parent.getCellCenter().y;
			double radius=(DEFAULT_RADIUS * Math.sqrt(3)/4 )*(40+antennaPattern(new Point2D.Double(x,y)))/20;

			bla.add(new Point2D.Double(radius*Math.sin(angle*2*Math.PI/360),radius*Math.cos(angle*2*Math.PI/360)));
		}
		return bla;
	}
	//TODO for debugging

	/**
	 * 
	 * @return array with radius and angle
	 */
	private double[] generateRandomRadAngle() {

		double radius;
		double angle;
		do {
			Random generator=new Random();
			radius=generator.nextDouble()*(DEFAULT_RADIUS - MINIMAL_DISTANCE) + MINIMAL_DISTANCE ;
			angle=generator.nextDouble()*120;

			checkAngle(angle,radius);

		} while(checkAngle(angle,radius));
		angle=roundAngle(angle);
		double[] helper={radius, angle};
		return helper;
	}

	private double roundAngle(double angle) {
		switch(id){
		case 0:
			angle-=0;
			break;
		case 1:
			angle-=120;
			break;
		case 2:
			angle+=120;
			break;
		}
		return angle;
	}

	private boolean checkAngle(double angle, double radius) {
		boolean flag;
		if(angle<=30 && radius<=(DEFAULT_RADIUS*Math.sqrt(3)/(2*Math.sin((90-angle)*2*Math.PI/360)))){
			flag=false;
		} else if((angle<=90 && angle>30) && radius<=(DEFAULT_RADIUS*Math.sqrt(3)/(2*Math.sin((120-(angle-30))*2*Math.PI/360)))) {
			flag=false;
		} else if((angle<=120 && angle > 90) && radius<=(DEFAULT_RADIUS*Math.sqrt(3)/(2*Math.sin((120-(angle-90))*2*Math.PI/360)))) {
			flag=false;
		} else {
			flag=true;
		}
		return flag;
	}

	public List<User> getUsers(){
		return myUsers;
	}

	public List<Point2D.Double> getAdditionalBoundries(){
		return additionalBoundries;
	}

	public int getID(){
		return id;
	}

	private void setParent(eNodeB parent) {
		this.parent = parent;
	}

	public eNodeB getParent() {
		return parent;
	}

	public void randomizeUserPositions() {
		for(int i=0;i<myUsers.size();i++){
			Point2D.Double position = generateUserPosition();
			myUsers.get(i).resetResources();			
			myUsers.get(i).setPosition(position);	
			myUsers.get(i).setShadowingValues();
		}
	}

	/**
	 * 
	 * @return unusedReources
	 */
	public List<RadioResourceBlock> getUnusedResources() {
		List<RadioResourceBlock> unusedResources=new LinkedList<RadioResourceBlock>();
		List<RadioResourceBlock> resources=getRadioResources();
		for(int i=0;i<resources.size();i++){
			if(resources.get(i).getUser()==null){
				unusedResources.add(resources.get(i));
			}
		}
		return unusedResources;
	}

	/**
	 * 
	 * @param unusedResources
	 * @param counter
	 */
	public void allocateResources(List<RadioResourceBlock> unusedResources,int counter) {
		allocateResources(myUsers, unusedResources,counter);

	}
}