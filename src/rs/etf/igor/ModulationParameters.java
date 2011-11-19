package rs.etf.igor;

import java.util.LinkedList;
import java.util.List;

public final class ModulationParameters implements Constants {

	private static ModulationParameters instance=null;
	private List<Modulation> modulationParameters=new LinkedList<Modulation>();

	//SNR values are from Rupp's simulator

	private ModulationParameters(){
		//1
		modulationParameters.add(new Modulation(4,-6.95,78.0/1024.0,0.1));		
		//2		
		modulationParameters.add(new Modulation(4,-5.15,120.0/1024.0,0.1));		
		//3		
		modulationParameters.add(new Modulation(4,-3.177,193.0/1024.0,0.1));		
		//4		
		modulationParameters.add(new Modulation(4,-1.2366,308.0/1024.0,0.1));		
		//5		
		modulationParameters.add(new Modulation(4,0.713,449.0/1024.0,0.1));		
		//6		
		modulationParameters.add(new Modulation(4,2.5865,602.0/1024.0,0.1));		
		//7		
		modulationParameters.add(new Modulation(16,4.623,378.0/1024.0,0.1));		
		//8		
		modulationParameters.add(new Modulation(16,6.45,490.0/1024.0,0.1));		
		//9		
		modulationParameters.add(new Modulation(16,8.362,616.0/1024.0,0.1));		
		//10		
		modulationParameters.add(new Modulation(64,10.363,466.0/1024.0,0.1));		
		//11		
		modulationParameters.add(new Modulation(64,12.216,567.0/1024.0,0.1));		
		//12		
		modulationParameters.add(new Modulation(64,14.047,666.0/1024.0,0.1));		
		//13		
		modulationParameters.add(new Modulation(64,15.84,772.0/1024.0,0.1));		
		//14		
		modulationParameters.add(new Modulation(64,17.70,873.0/1024.0,0.1));		
		//15		
		modulationParameters.add(new Modulation(64,19.654,948.0/1024.0,0.1));		
		//16
		modulationParameters.add(new Modulation(64,20.45,948.0/1024.0,0.00001));
	}

	public static ModulationParameters getInstance(){
		if(instance==null){
			instance=new ModulationParameters();
		} 
		return instance;
	}

	public List<Modulation> getModulationParameters(){
		return modulationParameters;
	}
	
	public class Modulation implements Constants {
		private double minSNR;
		private int noPoints;
		private double codeRatio;
		private double throughput;//in kbps

		private Modulation( int n, double s, double c,double BLER){
			minSNR=s;
			setNoPoints(n);
			setCodeRatio(c);
			throughput=B_PRB*1000*getEfficiency()*(1-BLER);
		}

		public double getEfficiency(){
			return Math.log10((double)noPoints)/Math.log10(2)*codeRatio;
		}

		private void setCodeRatio(double codeRatio) {
			this.codeRatio = codeRatio;
		}

		public double getCodeRatio() {
			return codeRatio;
		}

		private void setNoPoints(int noPoints) {
			this.noPoints = noPoints;
		}

		public double getNoPoints() {
			return noPoints;
		}

		protected void setMinSNR(double minSNR) {
			this.minSNR = minSNR;
		}

		public double getMinSNR() {
			return minSNR;
		}


		public void setThroughput(double throughput) {
			this.throughput = throughput;
		}


		public double getThroughput() {
			return throughput;
		}
	}
}