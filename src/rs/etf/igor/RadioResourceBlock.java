/*
 * Distributed under the terms of the GNU GPL version 3.
 * 
 * Copyright (c) 2011 Igor Maraviæ <igorm@etf.rs>
 */

package rs.etf.igor;

import java.util.LinkedList;
import java.util.List;

import rs.etf.igor.ModulationParameters.Modulation;

public class RadioResourceBlock implements Constants{


	private int id; 
	private double PmaxPRB;
	private double workingP;
	private double minDistance;
	private User user;
	private Sector parent;
	private Modulation modulation;

	/**
	 * 
	 * @param id
	 * @param P
	 * @param minDistance
	 * @param parent
	 */
	RadioResourceBlock(int id, double P, double minDistance, Sector parent){
		this.id=id;
		this.PmaxPRB=P;
		workingP=P;
		user=null;
		this.parent=parent;
		this.minDistance=minDistance;
		modulation=null;
	}

	public Sector getParent(){
		return parent;
	}

	public void setSNR(){
		setSNR(true);
	}

	/**
	 * 
	 * @param flag
	 */
	private void setSNR(boolean flag){
		if(user!=null){
			boolean workingFlag=true;
			int counter=0;
			do{
				double oldWorkingP=workingP;
				workingP=PmaxPRB;				

				double SNR= getSNR();
				double minSNR=-300;
				double maxSNR=1000;
				List<RadioResourceBlock> neighbors=getNeighbors();
				List<Modulation> modulations= ModulationParameters.getInstance().getModulationParameters();
				for(int i=modulations.size()-1;i>=0;i--){
					minSNR= modulations.get(i).getMinSNR();
					if(i!=modulations.size()-1){
						maxSNR=modulations.get(i+1).getMinSNR();
					}
					if(minSNR>SNR){
						if(i==0){
							user.removeResource(this);
							workingP=-3000;
						}
						continue;
					} else {
						double helper = SNR - minSNR;
						double pOverhead;
						if(i!=modulations.size()-1){
							pOverhead=(maxSNR-minSNR)/2;
						} else {
							pOverhead=1;
						}
						helper-=pOverhead;
						workingP-=helper;
						if(workingP>=PmaxPRB){
							workingP=PmaxPRB;
						}
						modulation=modulations.get(i);
						break;
					}
				}
				if(!(oldWorkingP<workingP+0.1 && oldWorkingP>workingP-0.1) && flag){
					for (int i=0;i<neighbors.size();i++){
						neighbors.get(i).setSNR(false);
					}
				}
				SNR=getSNR();
				if((SNR>=minSNR && SNR<=maxSNR)||user==null){
					workingFlag=false;
				}
				counter++;
				if(counter>700){
					flag=false;
				}
			} while(workingFlag);
		}
	}

	private List<RadioResourceBlock> getNeighbors() {
		List<eNodeB> cells=Network.getInstance().getCells();
		List<RadioResourceBlock> neighbors=new LinkedList<RadioResourceBlock>();
		for(int i=0;i<cells.size();i++){
			if(cells.get(i).equals(parent.getParent())){
				List<Sector> sectors=cells.get(i).getSectors();
				for(int k=0;k<sectors.size();k++){
					if(sectors.get(k).equals(parent)){
						continue;
					} else {
						List<RadioResourceBlock> resources= sectors.get(k).getRadioResources();
						for(int l=0;l<resources.size();l++){

							if(resources.get(l).getId()==id){
								neighbors.add(resources.get(l));
								break;
							}									

						}
					}
				}
				break;
			}
		}
		return neighbors;
	}

	/**
	 * 
	 * @param user
	 * @return max SNR
	 */
	public double getMaxSNR(User user){
		double oldWorkingP=workingP;
		workingP=PmaxPRB;
		double SNR =getSNR(user);
		workingP=oldWorkingP;
		return SNR;

	}

	/**
	 * 
	 * @param user
	 * @return SNR
	 */
	private double getSNR(User user){
		if(user!=null){
			double signal=getP()-user.getPropagationLoss()+user.getAntennaPattern()+user.getShadowingValues()[user.getParent().getParent().getId()];
			double noise=-174+10*Math.log10(B_PRB*1000);
			double interference=0;
			List<eNodeB> cells = Network.getInstance().getCells();
			for(int i=0;i<cells.size();i++){
				eNodeB node= cells.get(i);
				List<Sector> sectors = node.getSectors();
				for(int j=0;j<sectors.size();j++){
					Sector sector=sectors.get(j);
					if(sector.equals(user.getParent())){
						continue;
					} else {
						List<RadioResourceBlock> resources=sector.getRadioResources();
						RadioResourceBlock ourPRB=null;
						for(int k=0;k<resources.size();k++){
							if(id==resources.get(k).getId()){
								ourPRB=resources.get(k);
								break;
							}
						}
						if(ourPRB!=null){
							interference+=Math.pow(10, (ourPRB.getP()-ourPRB.getParent().propagationLoss(user.getPosition())+ourPRB.getParent().antennaPattern(user.getPosition())+user.getShadowingValues()[ourPRB.getParent().getParent().getId()])/10);
						}
					}
				}					
			}
			interference=10*Math.log10(interference);
			double interferenceNoise=10*Math.log10(Math.pow(10, noise/10) + Math.pow(10, interference/10));
			return signal - interferenceNoise;
		} else {
			return -300;
		}
	}


	public double getSNR(){
		return getSNR(this.user);

	}

	public void setP(double P){
		workingP=P;
		if(workingP>PmaxPRB){
			workingP=PmaxPRB;
		}
	}

	public double getP(){
		return workingP;
	}

	public double getPmax(){
		return PmaxPRB;
	}

	public int getId(){
		return id;
	}

	public void setUser(User user){
		this.user=user;
		if(user==null){
			modulation=null;
		}
	}

	public User getUser(){
		return user;
	}

	public Modulation getModulation() {
		return modulation;
	}

	public double getMinDistance() {
		return minDistance;
	}
}