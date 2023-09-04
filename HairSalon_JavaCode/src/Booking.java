import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Booking {

	private int bookingID;
	private String placeBookingDate;
	private String placeBookingTime;
	private String bookingDate;
	private String bookingTime;
	private int noOfHairServiceOrder;
	private int noOfHairStylistOrder;
	private Customer customer;
	private Admin admin;
	private HairStylist hairStylist;
	private double totalHairServicePrice;
	private double totalHairStylistPrice;
	private String totalHairServiceHour;
	private HairService [] hairServiceBookingList;
	private HairStylist [] hairStylistBookingList;
	private static int max_no_of_hair_service_order = 5; // only maximum 5 hair service
	private static int max_no_of_hair_stylist_order = 3; // only max 2 hair stylist
	String status;
	String adminID;
	String [] hairServiceIDList;
	String [] hairStylistIDList;
	
	public Booking (String placeBookingDate, String placeBookingTime,String bookingID, String totalHairServiceHour, String BookingDate, Admin admin, Customer customer)
	{
		this.placeBookingDate = placeBookingDate;
		this.placeBookingTime = placeBookingTime;
		this.bookingID = generateBookingID();
		this.totalHairServiceHour = totalHairServiceHour;
		this.bookingDate = BookingDate;
		this.admin = admin;
		this.customer = customer;
		hairServiceBookingList = new HairService[max_no_of_hair_service_order];
		hairStylistBookingList = new HairStylist[max_no_of_hair_stylist_order];
		status = "Processing";
		noOfHairServiceOrder = 0;
		noOfHairStylistOrder = 0;
	}
	
	public Booking (String placeBookingDate, String placeBookingTime, int bookingID, String totalHairServiceHour, String bookingDate, String adminID, Admin admin, Customer customer, int noOfHairServiceOrder, int noOfHairStylistOrder)
	{
		this.placeBookingDate = placeBookingDate;
		this.placeBookingTime = placeBookingTime;
		this.bookingID = bookingID;
		this.totalHairServiceHour = totalHairServiceHour;
		this.bookingDate = bookingDate;
		this.admin = admin;
		this.customer = customer;
		this.noOfHairServiceOrder = noOfHairServiceOrder;
		this.noOfHairStylistOrder = noOfHairStylistOrder;
		hairServiceBookingList = new HairService[max_no_of_hair_service_order];
		hairStylistBookingList = new HairStylist[max_no_of_hair_stylist_order];
	}
	
	public Booking (Booking booking)
	{
		hairServiceBookingList = new HairService[max_no_of_hair_service_order];
		hairStylistBookingList = new HairStylist[max_no_of_hair_stylist_order];
	}
	
	public Booking(String placeBookingDate2, int bookingID2, int bookingHour, String bookingDate2, Customer c,
			String status2, String adminID2, int noOfHairServiceOrder2, int noOfHairStylistOrder2) {
	
	}

	public int generateBookingID()
	{
		// generate ID randomly
		int min = 1000;
		int max = 9999;
		int bookID = (int)(Math.random()*(max-min+1)+min);
		return bookID;
	}
	
	public void setAdmin(Admin admin)
	{
		this.admin = admin;
	}
	
	public void setStatus(String status)
	{
		this.status = status;
	}
	
	public Customer getCustomer()
	{
		return customer;
	}
	
	public HairStylist getHairStylist()
	{
		return hairStylist;
	}
	public String getPlaceBookingDate()
	{
		return placeBookingDate;
	}
	
	public String getPlaceBookingTime()
	{
		return placeBookingTime;
	}
	
	public int getBookingID()
	{
		return bookingID;
	}
	
	public String getBookingDate()
	{
		return bookingDate;
	}
	
	public String getBookingTime()
	{
		return bookingTime;
	}
	
	public Admin getAdmin()
	{
		return admin;
	}
	
	public String getStatus()
	{
		return status;
	}
	
	public HairService[] getHairServiceBookingList()
	{
		return hairServiceBookingList;
	}
	
	public HairStylist[] getHairStylistBookingList()
	{
		return hairStylistBookingList;
	}
	
	public int getNoOfHairService()
	{
		return noOfHairServiceOrder;
	}
	
	public int getNoOfHairStylist()
	{
		return noOfHairStylistOrder;
	}
	
	public void addHairService (HairService service)
	{
		if (noOfHairServiceOrder < max_no_of_hair_service_order)
		{
			hairServiceBookingList[noOfHairServiceOrder] = service;
			noOfHairServiceOrder++;
		}
	}
	
	public void addHairStylist (HairStylist stylist)
	{
		if (noOfHairStylistOrder < max_no_of_hair_stylist_order)
		{
			hairStylistBookingList[noOfHairStylistOrder] = stylist;
			noOfHairStylistOrder++;
		}
	}
	
	public double computeAmount()
	{
		return totalHairServicePrice + totalHairStylistPrice;
	}
	
	public void calculateTotalHairServicePrice()
	{
		double service = 0.0 ;
		for (int i=0; i<noOfHairServiceOrder; i++)
		{
			service =service+ (hairServiceBookingList[i].getHairServicePrice());
			totalHairServicePrice = service;
		}
	}
	
	public void calculateTotalHairStylistPrice()
	{
		double stylist = 0;
		for (int i=0; i<noOfHairStylistOrder; i++)
		{
			stylist += (hairStylistBookingList[i].getHairStylistPrice());
			totalHairStylistPrice = stylist;
		}
	}
	
	public void calculateTotalHairServiceHour()
	{
		String hour = null ;
		for (int i=0; i<noOfHairServiceOrder; i++)
		{
			hour += (hairServiceBookingList[i].getHairServiceDesc());
			totalHairServiceHour = hour;
		}
	}
	
	public void displayHairServiceBookingOrderList()
	{
		System.out.printf("Number of hair service ordered: ",noOfHairServiceOrder);
		System.out.printf("--------------------------------------------------------------------------------------");
		System.out.printf("	No.	|		ID		| 		Hair Service		|	Unit Price	|	Service Hour	|");
		System.out.printf("--------------------------------------------------------------------------------------");
		for (int i=0; i < noOfHairServiceOrder; i++)
		{
			System.out.printf("	%-2d.\t\t%-6s\t\t\t\t-15s\t\t\t%6.2f\t\t\t-2d.\t\n",i+1,
					hairServiceBookingList[i].getHairServiceID(), 
					hairServiceBookingList[i].getHairServiceName(), 
					hairServiceBookingList[i].getHairServicePrice(),
					hairServiceBookingList[i].getHairServiceDesc());
		}
	}
	
	public void displayHairStylistBookingOrderList()
	{
		System.out.printf("Number of hair stylist ordered: ",noOfHairStylistOrder);
		System.out.printf("------------------------------------------------------------------");
		System.out.printf("	No.	|		ID		| 		Hair Stylist		|	Unit Price	|");
		System.out.printf("------------------------------------------------------------------");
		for (int i=0; i < noOfHairStylistOrder; i++)
		{
			System.out.printf("	%-2d.\t\t%-6s\t\t\t\t-15s\t\t\t%6.2f\t\n",i+1,
					hairStylistBookingList[i].getHairStylistID(), 
					hairStylistBookingList[i].getHairStylistName(), 
					hairStylistBookingList[i].getHairStylistPrice());
		}
	}
	
	public void displayBookingOrderDetails()
	{
		calculateTotalHairServicePrice();
		calculateTotalHairStylistPrice();
		calculateTotalHairServiceHour();
		System.out.printf("------------------------------------------------------------------");
		System.out.printf("\t\t\t\t\t         Salon Management System");
		System.out.printf("\t\t\t\t\t     Contact No: 011-2213467");
		System.out.printf("------------------------------------------------------------------");
		System.out.printf("\t\t\t\t\t  | BOOKING ORDER DETAILS |\n\n");
		System.out.printf("------------------------------------------------------------------");
		System.out.printf("Customer ID        : %5\t\tAdmin ID		: %5s\n", customer.getId(), admin.getId());
		System.out.printf("Order Date	      : %10s\tBooking ID   : %4d\n",placeBookingDate, bookingID);
		System.out.printf("Date 		      : ", bookingDate);
		System.out.printf("Time 		      : ", bookingTime);
		System.out.println('\n');
		displayHairServiceBookingOrderList();
		System.out.println('\n');
		displayHairStylistBookingOrderList();
		System.out.println('\n');
		System.out.printf("Total Stylist      : ", noOfHairStylistOrder);
		System.out.printf("Total Service      : ", noOfHairServiceOrder);
		System.out.printf("Total Service Hour : ", totalHairServiceHour);
		System.out.printf("Hair Service Price : RM%.2f", totalHairServicePrice);
		System.out.printf("Hair Stylist Price : RM%.2f", totalHairStylistPrice);
		System.out.printf("Amount			  : RM%.2f",computeAmount());
		System.out.printf("Order Status   	  : ", status);
		System.out.printf("------------------------Order Closed -----------------------------");
		
	}
	
	// generate a receipt in a text file
	public void generateBookingReceipt(Boolean append)
	{
		String receipt = "Receipt";
		String receipt1 = ".txt";
		int receiptID = bookingID;
		String id = Integer.toString(bookingID);
		String file = receipt + id + receipt1;
		
		try 
		{
			File f = new File(file);
			FileWriter fileWriter = new FileWriter(f,append);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			
			printWriter.printf("----------------------------------------------------------------------------------------");
			printWriter.printf("\t\t\t\t\t       Salon Management System");
			printWriter.printf("\t\t\t\t\t     Contact No: 011-2213467");
			printWriter.printf("----------------------------------------------------------------------------------------");
			printWriter.printf("\t\t\t\t\t  | BOOKING ORDER DETAILS |\n\n");
			printWriter.printf("----------------------------------------------------------------------------------------");
			printWriter.printf("Booking ID   : ", bookingID);
			printWriter.printf(placeBookingDate,"\t\t\t\t",placeBookingTime);
			printWriter.printf("----------------------------------------------------------------------------------------");
			printWriter.printf("Customer ID     : ", customer.getId());
			printWriter.printf("Date : ", bookingDate);
			printWriter.printf("Time : ", bookingTime);
			printWriter.printf("Total Service Hour :",totalHairServiceHour);
			printWriter.printf("Amount : RM%.2f\n", computeAmount());
			printWriter.printf("Hair Stylist : ", hairStylist.getHairStylistName());
			printWriter.printf("=========================================================================================");
			printWriter.printf("	No.	|		ID		| 		Hair Service		|	Unit Price	|	Service Hour	|");
		
			for (int i = 0; i < noOfHairServiceOrder; i++)
			{
				String line = String.format("	%-2d.\\\\t\\\\t%-6s\\\\t\\\\t\\\\t\\\\t-15s\\\\t\\\\t\\\\t%6.2f\\\\t\\\\n\\",i+1,
						hairServiceBookingList[i].getHairServiceID(),
						hairServiceBookingList[i].getHairServiceName(), 
						hairServiceBookingList[i].getHairServicePrice(),
						hairServiceBookingList[i].getHairServiceDesc());
				printWriter.println(line);
			}
			
			printWriter.printf(" Total Hair Service: ",noOfHairServiceOrder);
			printWriter.close();
		
		}catch(IOException e)
		{
			System.out.println("Error");
		}
	}

	public void addHairServiceFromFile(int no, HairService hs) {
		hairServiceBookingList[no] = hs;
		
	}
}
