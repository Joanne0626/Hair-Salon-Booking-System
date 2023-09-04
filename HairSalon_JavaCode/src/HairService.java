
public class HairService {
	
	private  String HairServiceName;
	private String HairServiceID;
	private String HairServiceDesc;
	private double HairServicePrice;
	
	public HairService(String HairServiceName, String HairServiceID, String HairServiceDesc, double HairServicePrice )
	{
		this.HairServiceName = HairServiceName;
		this.HairServiceID = HairServiceID;
		this.HairServiceDesc = HairServiceDesc;
		this.HairServicePrice = HairServicePrice;
	}
	
	public String getHairServiceName()
	{
		return HairServiceName;
	}
	
	public String getHairServiceID()
	{
		return HairServiceID;
	}
	
	public String getHairServiceDesc()
	{
		return HairServiceDesc;
	}
	
	public double getHairServicePrice()
	{
		return HairServicePrice;
	}
	
	public void setHairServiceName(String HairServiceName)
	{
		this.HairServiceName = HairServiceName;
	}
	
	public void setHairServiceID(String HairServiceID)
	{
		this.HairServiceID = HairServiceID;
	}
	
	public void setHairServiceDesc(String HairServiceDesc)
	{
		this.HairServiceDesc = HairServiceDesc;
	}
	
	public void setHairServicePrice(double HairServicePrice)
	{
		this.HairServicePrice = HairServicePrice;
	}
	
	public void displayHairServiceDetails()
	{
		System.out.println("------------------------------------------------------------------");
		System.out.println("\t\t    | HAIR SERVICE DETAILS |" );
		System.out.println("------------------------------------------------------------------");
		System.out.printf(" Hair Service Name  : " +HairServiceName);
		System.out.printf("\n Hair Service ID    : " +HairServiceID);
		System.out.println("\n Description   : \n\n " + HairServiceDesc);
		System.out.println("\n  Price : " + HairServicePrice);
		System.out.println("==================================================================\n");
	}


}