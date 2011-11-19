/*
 * Distributed under the terms of the GNU GPL version 3.
 * 
 * Copyright (c) 2011 Igor Maraviæ <igorm@etf.rs>
 */

package rs.etf.igor;

public final class Statistics implements Constants {

	private static Statistics instance=null;
	private static final double distanceStep=15;
	private static final double maxSNR=30;
	private static final double minSNR=-10;
	private static final double maxThroughputPRB=1000;
	private static final double minThroughputPRB=0;
	private static final double throughputCDFStep=12.5;
	private static final double SNRCDFStep=0.5;
	private int numberOfPoints;
	private int uniqueUserID;
	private double[] distance;
	private double[] SNRforCDF;
	private double[] throughputForCDF;
	private double[] snr;
	private double[] snrCounter;
	private double[] throughputPRB;
	private double[] throughputCounter;
	private double[] throughput; 
	private double[] probabilityCDFSNR;
	private double[] probabilityCDFThroughput;
	private int counterCDFSNR;
	private int counterCDFThroughput;
	private double snrMean;
	private double throughputMeanPRB;
	private double throughputMean;
	private double numberOfPRBs;
	private double throughputSum;

	private Statistics() {
		
		int helper=(int)((maxSNR-minSNR)/SNRCDFStep)+1;
		SNRforCDF=new double[helper];
		for(double i=(int)minSNR,k=0;i<=maxSNR;i+=SNRCDFStep,k++){
			SNRforCDF[(int)k]=i;
		}
		helper=(int)((maxThroughputPRB-minThroughputPRB)/throughputCDFStep)+1;
		throughputForCDF=new double[helper];
		for(double i=(int)minThroughputPRB,k=0;i<=maxThroughputPRB;i+=throughputCDFStep,k++){
			throughputForCDF[(int)k]=i;
		}
		numberOfPoints=(int)((DEFAULT_RADIUS-MINIMAL_DISTANCE)/distanceStep);
		distance=new double[numberOfPoints+1];
		for(int i=0;i<numberOfPoints+1;i++){
			distance[i]=MINIMAL_DISTANCE+i*distanceStep;
		}
		
		resetAll();
	}

	public static Statistics getInstance(){
		if(instance==null){
			instance=new Statistics();
		}
		return instance;
	}

	/**
	 * 
	 * @return value of current user id and after that it increments it
	 */
	public int getUniqueUserID(){
		return uniqueUserID++;
	}

	public void resetUserID(){
		uniqueUserID=0;
	}

	public void resetAll(){
		uniqueUserID=0;
		snr=new double[numberOfPoints];
		throughput=new double[numberOfPoints];
		throughputPRB=new double[numberOfPoints];
		snrCounter=new double[numberOfPoints];
		throughputCounter=new double[numberOfPoints];
		int helper=(int)((maxThroughputPRB-minThroughputPRB)/throughputCDFStep)+1;
		probabilityCDFThroughput=new double[helper];
		helper=(int)((maxSNR-minSNR)/SNRCDFStep)+1;
		probabilityCDFSNR=new double[helper];
		throughputMean=0;
		throughputMeanPRB=0;
		snrMean=0;
		numberOfPRBs=0;
		throughputSum=0;
		counterCDFSNR=0;
		counterCDFThroughput=0;

	}

	public double[] getDistance() {
		return distance;
	}

	public void addSnrValue(double snrValue, int i) {
		if(snrCounter[i]==0){
			snr[i]=snrValue;
		} else {
			snr[i]=10*Math.log10(Math.pow(10, snrValue/10)+Math.pow(10, snr[i]/10));
		}
		snrCounter[i]++;
		for(int k=0;k<(int)((maxSNR-minSNR)/SNRCDFStep)+1;k++){
			if(snrValue<=SNRforCDF[k]){
				counterCDFSNR++;
				for(int l=k;l<(int)((maxSNR-minSNR)/SNRCDFStep)+1;l++){
					probabilityCDFSNR[l]++;
				}
				break;
			}
		}
	}

	public double[] getSnr() {
		return snr;
	}

	public void addThroughputPRBValue(double throughputValue, int i) {
		throughputPRB[i]+=throughputValue;
		throughputCounter[i]++;
		for(int k=0;k<((maxThroughputPRB-minThroughputPRB)/throughputCDFStep)+1;k++){
				if(throughputValue<=throughputForCDF[k]){
				counterCDFThroughput++;
				for(int l=k;l<((maxThroughputPRB-minThroughputPRB)/throughputCDFStep)+1;l++){
					probabilityCDFThroughput[l]++;
				}
				break;
			}
		}
	}

	public double[] getThroughputPRB() {
		return throughputPRB;
	}

	public double[] getSnrCounter() {
		return snrCounter;
	}

	public double[] getThroughputCounter() {
		return throughputCounter;
	}

	public void calculateMeanValues() {
		double numberOfPRBsSum=0;
		int numberOfIterations=Network.getInstance().getNumberOfIterations();
		for(int i=0;i<numberOfPoints;i++){
			throughput[i]=throughputPRB[i]/numberOfIterations;
			throughputMean+=throughput[i];
			throughputMeanPRB+=throughputPRB[i];
			throughputSum+=throughputPRB[i];
			throughputPRB[i]/=throughputCounter[i];
			numberOfPRBsSum+=throughputCounter[i];
			snr[i]-=10*Math.log10((double)snrCounter[i]);			

			if(i==0){
				snrMean=snr[i];
			}else {
				snrMean=10*Math.log10(Math.pow(10, snrMean/10)+Math.pow(10, snr[i]/10));
			}

		}
		throughputSum/=numberOfIterations;
		throughputMean/=throughput.length;
		throughputMeanPRB/= numberOfPRBsSum;
		snrMean-=10*Math.log10((double)snr.length);

		for(int i=0;i<numberOfPoints;i++){
			numberOfPRBs+=throughputCounter[i];
			throughputCounter[i]/=numberOfIterations;
			snrCounter[i]/=numberOfIterations;
		}
		numberOfPRBs/=numberOfIterations;
		int helper=(int)((maxThroughputPRB-minThroughputPRB)/throughputCDFStep)+1;
		for(int k=0;k<helper;k++){
			probabilityCDFThroughput[k]/=counterCDFThroughput;
		}
		helper=(int)((maxSNR-minSNR)/SNRCDFStep)+1;
		for(int k=0;k<helper;k++){
			probabilityCDFSNR[k]/=counterCDFSNR;
		}

	}

	public double getThroughputSum(){
		return throughputSum;
	}

	public double getNumberOfPRBs(){
		return numberOfPRBs;
	}

	public double getSnrMean() {
		return snrMean;
	}

	public double getThroughputMeanPRB() {
		return throughputMeanPRB;
	}

	public double getThroughputMean() {
		return throughputMean;
	}

	public double[] getThroughput() {
		return throughput;
	}

	public double[] getProbabilityCDFSNR() {
		return probabilityCDFSNR;
	}
	
	public double[] getSNRforCDF(){
		return SNRforCDF;
	}
	
	public double[] getThroughputForCDF(){
		return throughputForCDF;
	}

	public double[] getProbabilityCDFThroughput() {
		return probabilityCDFThroughput;
	}
}