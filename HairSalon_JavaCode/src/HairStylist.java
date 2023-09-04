
public class HairStylist 
{
	//instance variables 
	private String stylistName;
	private String stylistID;
	private String stylistDesc;
	private double stylistPrice;
	
	//Constructor 
	public HairStylist(String stylistName, String stylistID, String stylistDesc, double stylistPrice)
	{
		this.stylistName = stylistName;
		this.stylistID = stylistID;
		this.stylistDesc = stylistDesc;
		this.stylistPrice = stylistPrice;
	}
	
	//method to get stylistName
	public String getHairStylistName() 
	{
		return stylistName;
	}
	
	//method to set stylistName
	public void setHairStylistName(String stylistName)
	{
		this.stylistName = stylistName;
	}
	
	//method to get stylistID
	public String getHairStylistID()
	{
		return stylistID;
	}
	
	//method to set stylistID
	public void setHairStylistID(String stylistID)
	{
		this.stylistID = stylistID;
	}
	
	//method to get rank
	public String getStylistDesc()
	{
		return stylistDesc;
	}
	
	//method to set rank
	public void setStylistDesc(String stylistDesc)
	{
		this.stylistDesc = stylistDesc;
	}
	
	//method to get stylistPrice
	public double getHairStylistPrice()
	{
		return stylistPrice;
	}
	
	//method to set stylistPrice
	public void setHairStylistPrice(double price)
	{
		this.stylistPrice = price;
	}
	
	//method to show Stylist details
	public void displayHairStylistDetails()
	{
		System.out.println("         Stylist Details        ");
		System.out.println("Stylist Name  : \t" + stylistName);
		System.out.println("Stylist ID    : \t" + stylistID);
		System.out.println("Stylist Rank  : \t " + stylistDesc);
	}
}