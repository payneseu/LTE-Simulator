package rs.etf.igor;

import java.util.LinkedList;
import java.util.List;

public class Network implements Constants{
	/**
	 * attributes
	 */
	int numberOfCells=NUMBER_OF_CELLS; 	//here we define number of our cells, allowed values are 7, 13 and 18

	private List<eNodeB> myCells = new LinkedList<eNodeB>();
	private int numberOfIterations=0;

	private double alpha;	//parameter for percentage of maximal strength in Reuse 1 part of spectrum
	private double beta;	//parameter for percentage of maximal strength of 1/3 of Reuse 3 part of spectrum
	private double gamma;	//parameter for percentage of maximal strength of 2/3 of Reuse 3 part of spectrum
	private double delta;   //percentage of Reuse 1 part of the spectrum

	private double R=DEFAULT_RADIUS;		//cell radius in m

	private double innerCellRadius=0;
	private double innerInnerCellRadius=0;

	private static Network instance= null;

	/**
	 * class constructor
	 */
	private Network() {

	}

	/**
	 * This class is realized as Singleton DP
	 * @return instance of class object
	 */
	public static Network getInstance(){
		if(instance==null) {
			instance=new Network();
		}
		return instance;
	}

	public double getAlpha(){
		return alpha;
	}

	public double getBeta(){
		return beta;
	}

	public double getGamma(){
		return gamma;
	}

	public double getDelta(){
		return delta;
	}

	/**
	 *
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 */
	public void setupParameters(double a, double b, double c, double d){
		reset();
		if (numberOfCells!=7 && numberOfCells!=13 && numberOfCells!=19){
			throw new Error("Number of cells must be 7 or 13 or 19"); 
		}

		if(d>1){
			d=1;
		} else if(d<0){
			d=0;
		}
		if(a>1){
			a=1;
		} else if(a<0){
			a=0;
		}
		if(b>1){
			b=1;
		} else if(b<0){
			b=0;
		}
		if(c>1){
			c=1;
		} else if(c<0){
			c=0;
		}

		alpha=a;
		beta=b;
		gamma=c;
		delta=d;
		createCellGrid();
	}
	
	/**
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @param iterations
	 * @param innerRadius
	 * @param innerInnerRadius
	 */
	public void setupParameters(double a, double b, double c, double d,int iterations,double innerRadius, double innerInnerRadius){
		numberOfIterations=iterations;
		innerCellRadius=innerRadius;
		innerInnerCellRadius=innerInnerRadius;
		setupParameters(a,b,c,d);
	}

	/**
	 * 
	 * @param nCells
	 * @param radius
	 * @param bandwidth
	 */
	public void setupParametersReuse1(int nCells,double radius,double bandwidth){
		setupParameters(1,0,0,1);
	}

	/**
	 * 
	 * @param nCells
	 * @param radius
	 * @param bandwidth
	 */
	public void setupParametersReuse3(int nCells,double radius,double bandwidth){
		setupParameters(0,1,0,0);
	}

	private void createCellGrid() {
		//first cell has x=0 and y=0 coordinates!

		for(int i=0;i<numberOfCells;i++){
			double x=0;
			double y=0;

			switch(i){
			case 0:
				x=0;
				y=0;

				break;
			case 1: 
				x=3*R/2;
				y=R*Math.sqrt(3)/2;

				break;
			case 2: 
				x=0;
				y=R*Math.sqrt(3);

				break;
			case 3: 
				x=-3*R/2;
				y=R*Math.sqrt(3)/2;

				break;
			case 4: 
				x=-3*R/2;
				y=-R*Math.sqrt(3)/2;

				break;
			case 5: 
				x=0;
				y=-R*Math.sqrt(3);

				break;
			case 6:
				x=3*R/2;
				y=-R*Math.sqrt(3)/2;

				break;
			case 7: 
				x=3*R;
				y=0;

				break;
			case 8:
				x=3*R/2;
				y=3*R*Math.sqrt(3)/2;

				break;
			case 9: 
				x=-3*R/2;
				y=3*R*Math.sqrt(3)/2;

				break;
			case 10:
				x=-3*R;
				y=0;

				break;
			case 11: 
				x=-3*R/2;
				y=-3*R*Math.sqrt(3)/2;

				break;
			case 12: 
				x=3*R/2;
				y=-3*R*Math.sqrt(3)/2;

				break;
			case 13:
				x=3*R;
				y=R*Math.sqrt(3);

				break;
			case 14: 
				x=0;
				y=2*R*Math.sqrt(3);

				break;
			case 15: 
				x=-3*R;
				y=R*Math.sqrt(3);

				break;
			case 16:
				x=-3*R;
				y=-R*Math.sqrt(3);

				break;
			case 17:
				x=0;
				y=-2*R*Math.sqrt(3);

				break;
			case 18: 
				x=3*R;
				y=-R*Math.sqrt(3);

				break;
			default:
				throw new Error("Number of cells must be 7 or 13 or 19");
			}
			myCells.add(new eNodeB(x,y,alpha,beta,gamma,delta,i));
		}
		setSNR();
	}

	private void setSNR() {
		for(int i=0;i<myCells.size();i++){
			if(myCells.get(i).getId()==0){
				myCells.get(i).setSNR();
				break;
			}
		}

	}

	public void randomizeUserPositions(){
		for(int i=0;i<myCells.size();i++){
			if(myCells.get(i).getId()==0){
				myCells.get(i).randomizeUserPositions();
				break;
			}
		}
		setSNR();
	}
	/**
	 * for reseting instance of Network
	 */
	public void reset(){
		Statistics.getInstance().resetUserID();
		myCells.clear();

	}

	public List<eNodeB> getCells(){
		return myCells;
	}

	public void setNumberOfIterations(int i) {
		numberOfIterations=i;
		
	}
	
	public int getNumberOfIterations(){
		return numberOfIterations;
	}

	public void setInnerCellRadius(double value) {
		innerCellRadius=value;
		
	}
	
	public double getInnerCellRadius(){
		return innerCellRadius;
	}

	public void setInnerInnerCellRadius(double innerInnerCellRadius) {
		this.innerInnerCellRadius = innerInnerCellRadius;
	}

	public double getInnerInnerCellRadius() {
		return innerInnerCellRadius;
	}
}