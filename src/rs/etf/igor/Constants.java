package rs.etf.igor;

public interface Constants {

	public static final int DEFAULT_RADIUS = 500;
	public static final int MINIMAL_DISTANCE = 35;
	public static final int DEFAULT_B = 10;
	public static final int NUMBER_OF_CELLS = 19;
	public static final int NoSECTORS=3;
	public static final double P_MAX=46; //maximal power in dBm
	public static final double SHADOWING_DEVIATION = 8;//generator.nextGaussian()*SHADOWING_DEVIATION
	public static final int NUMBER_OF_USERS_SECTOR= 50 / NoSECTORS; //36.104 3gpp
	public static final int MAX_No_PRB= 50;//36.212 3GPP for 10MHz B	
	public static final double B_PRB=0.18; //bandwidth of one resource block in MHz 36.212 3gpp
}
