package rs.etf.igor;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.IllegalFormatException;

import javax.swing.JProgressBar;

public class Simulation extends Thread implements Constants, Runnable {

	private SimulationProgress progress;
	private UI ui;
	private boolean flag=true;
	private long iterationTime=0;

	Simulation(SimulationProgress progress, UI ui){
		super();
		this.progress=progress;
		this.ui=ui;
	}
	
	@Override
	public void run() {
		JProgressBar progressBar = progress.getProgressBar();
		Network network=Network.getInstance();
		int numberOfIterations=network.getNumberOfIterations();
		Statistics statistics=Statistics.getInstance();
		statistics.resetAll();
		
		for(int i=0;i<numberOfIterations && flag;i++){
			long helper=System.currentTimeMillis();
			network.randomizeUserPositions();
			getValues();
			ui.drawNetworkPicture();
			progressBar.setValue(i);
			helper=System.currentTimeMillis()-helper;
			iterationTime+=helper;
			long meanIterationTime=iterationTime/(i+1);
			long timeLeft=meanIterationTime*(numberOfIterations-i);
			progress.printTimeLeft(timeLeft/1000);
		}
		if(flag){
			statistics.calculateMeanValues();
			printValues();
		}
		progress.dispose();
	}

	private void printValues() {
		FileOutputStream out; 
		PrintStream ps; 
		try {
			Network network=Network.getInstance();			
			double helper=0;//3*((double)(network.getDelta()*network.getAlpha())+(double)(1-network.getDelta())*(double)(2*network.getGamma()+network.getBeta())/3);
			String string="a"+(int)(network.getAlpha()*100)+"b"+(int)(network.getBeta()*100)+"g"+(int)(network.getGamma()*100)+"d"+(int)(network.getDelta()*100)+"ir"+(int)network.getInnerCellRadius()+"iir"+(int)network.getInnerInnerCellRadius();//String.format("%3.3f",helper);
			string=string.replace(',','.');
			out = new FileOutputStream(string+".txt");
			ps = new PrintStream(out);
			Statistics statistics=Statistics.getInstance();
			ps.print("Distance: \n");
			for(int i=0;i<statistics.getDistance().length-1;i++){
				helper =(statistics.getDistance()[i+1]+statistics.getDistance()[i])/2;
				string=String.format("%3.3f ",helper);
				string=string.replace(',','.');
				ps.print(string);
			}

			ps.print("\n\nSNR: \n");
			for(int i=0;i<statistics.getSnr().length;i++){
				string=String.format("%3.3f ",statistics.getSnr()[i]);
				string=string.replace(',','.');
				ps.print(string);
			}

			String stringHelper=String.format("\nMean SNR: %3.3f", statistics.getSnrMean());
			stringHelper=stringHelper.replace(',','.');
			ps.print(stringHelper);

			ps.print("\n\nThroughput per PRB: \n");
			for(int i=0;i<statistics.getThroughputPRB().length;i++){
				string=String.format("%5.3f ",statistics.getThroughputPRB()[i]);
				string=string.replace(',','.');
				ps.print(string);
			}

			stringHelper=String.format("\nMean throughput per PRB: %5.3f", statistics.getThroughputMeanPRB());
			stringHelper=stringHelper.replace(',','.');
			ps.print(stringHelper);

			ps.print("\n\nThroughput: \n");
			for(int i=0;i<statistics.getThroughput().length;i++){
				string=String.format("%5.3f ",statistics.getThroughput()[i]);
				string=string.replace(',','.');
				ps.print(string);
			}
			stringHelper=String.format("\nMean throughput: %5.3f", statistics.getThroughputMean());
			stringHelper=stringHelper.replace(',','.');
			ps.printf(stringHelper);

			stringHelper=String.format("\nCell throughput: %5.3f", statistics.getThroughputSum());
			stringHelper=stringHelper.replace(',','.');
			ps.printf(stringHelper);

			ps.print("\n\nNumber of PRBs: \n");
			for(int i=0;i<statistics.getThroughputCounter().length;i++){
				string=String.format("%5.3f ",statistics.getThroughputCounter()[i]);
				string=string.replace(',','.');
				ps.print(string);
			}

			stringHelper=String.format("\nCell PRBs: %5.3f", statistics.getNumberOfPRBs());
			stringHelper=stringHelper.replace(',','.');
			ps.printf(stringHelper);
			
			ps.print("\n\nSNR for CDF: \n");
			for(int i=0;i<statistics.getSNRforCDF().length;i++){
				string=String.format("%5.3f ",statistics.getSNRforCDF()[i]);
				string=string.replace(',','.');
				ps.print(string);
			}

			ps.print("\n\nProbability for SNR(CDF): \n");
			for(int i=0;i<statistics.getProbabilityCDFSNR().length;i++){
				string=String.format("%5.5f ",statistics.getProbabilityCDFSNR()[i]);
				string=string.replace(',','.');
				ps.print(string);
			}

			ps.print("\n\nThroughput for CDF: \n");
			for(int i=0;i<statistics.getThroughputForCDF().length;i++){
				string=String.format("%5.3f ",statistics.getThroughputForCDF()[i]);
				string=string.replace(',','.');
				ps.print(string);
			}

			ps.print("\n\nProbability for throughput(CDF): \n");
			for(int i=0;i<statistics.getProbabilityCDFThroughput().length;i++){
				string=String.format("%5.5f ",statistics.getProbabilityCDFThroughput()[i]);
				string=string.replace(',','.');
				ps.print(string);
			}

			ps.close();
			out.close();
		}
		catch (FileNotFoundException e){
			e.printStackTrace();
		} catch (IllegalFormatException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {		

		}
	}

	private void getValues() {
		Statistics statistics=Statistics.getInstance();
		Network network=Network.getInstance();
		eNodeB node=null;

		for(int i=0;i<network.getCells().size();i++){
			if(network.getCells().get(i).getId()==0){
				node=network.getCells().get(i);
				break;
			}
		}

		if(node!=null){
			for(int i=0;i<node.getSectors().size();i++){
				Sector sector=node.getSectors().get(i);
				for(int k=0;k<sector.getRadioResources().size();k++){
					RadioResourceBlock resource=sector.getRadioResources().get(k);
					if(resource.getUser()!=null){
						User user=resource.getUser();
						int numberOfPosition=-1;
						double radius=Math.sqrt(Math.pow(user.getPosition().x - node.getCellCenter().x, 2)+Math.pow(user.getPosition().y - node.getCellCenter().y, 2));
						for(int l=1;l<statistics.getDistance().length;l++){
							if(radius<=statistics.getDistance()[l]){
								numberOfPosition=l-1;
								break;
							}
						}
						statistics.addSnrValue(resource.getSNR(), numberOfPosition);
						statistics.addThroughputPRBValue(resource.getModulation().getThroughput(), numberOfPosition);						
					} 
				}
			}
		}

	}

	@Override
	public void destroy(){
		Statistics.getInstance().resetAll();
		flag=false;
	}

}