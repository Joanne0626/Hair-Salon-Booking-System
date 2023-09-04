import java.util.ArrayList;

public class Customer extends User
{
	private char gender;
	private String dateOfBirth;
	private int phNo;
	private String email;
	private String address;
	private Booking[] bookingList;
	
	// Getter
	public char getGender()
	{
		return gender;
	}
	
	public String getDateOfBirth()
	{
		return dateOfBirth;
	}
	
	public int getPhNo()
	{
		return phNo;
	}
	
	public String getEmail()
	{
		return email;
	}
	
	public String getAddress()
	{
		return address;
	}
	
	// Setter
	public void setGender (char gender)
	{
		this.gender = gender;
	}
	
	public void setDateOfBirth(String dateOfBirth)
	{
		this.dateOfBirth = dateOfBirth;
	}
	
	public void setPhNo (int phNo)
	{
		this.phNo = phNo;
	}
	
	public void setEmail (String email)
	{
		this.email = email;
	}
	
	public void setAddress (String address)
	{
		this.address = address;
	}
	
	public Booking[] getBookingList()
	{
		
		return bookingList;
	}
	
	// constructor
	public Customer (String customerID, String customerName, String customerPassword,char aGender, String aDateOfBirth, int aPhNo, String aEmail, String aAddress)
	{
		super(customerName,customerID,customerPassword);
		gender = aGender;
		dateOfBirth = aDateOfBirth;
		phNo = aPhNo;
		email = aEmail;
		address = aAddress;
		bookingList = new Booking[50];
		
	}
	
	public Customer (Customer c)
	{
		super(c.getName(),c.getId(),c.getPassword());
		this.gender = c.gender;
		this.phNo = c.phNo;
		this.email = c.email;
		this.address = c.address;
		bookingList = new Booking[50];
	}
	
	public void customerMenu()
	{
		System.out.println("---------------------------------------------------------");
		System.out.println("\t	~ WELCOME TO HAIR SALON BOOKING SYSTEM ~ ");
		System.out.println("\t\t		| CUSTOMER MENU |");
		System.out.println("---------------------------------------------------------");
		System.out.println("\t\t	1. View Personal Details\n");
		System.out.println("\t\t	2. Update Customer Personal Details\n");
		System.out.println("\t\t	3. Hair Service Menu\n");
		System.out.println("\t\t	4. Hair Stylist Menu\n");
		System.out.println("\t\t	5. Place Booking order\n");
		System.out.println("\t\t	6. Check Booking History\n");
		System.out.println("\t\t	7. Payment\n");
	}
	
	public void displayCustomerDetails()
	{
		System.out.println("---------------------------------------------------------");
		System.out.println("\t	~ WELCOME TO HAIR SALON BOOKING SYSTEM ~ ");
		System.out.println("\t\t	| CUSTOMER DETAILS |\t\tTotal: ");
		System.out.println("---------------------------------------------------------");
		System.out.printf(" Customer ID     : %-10s\t\tPassword: %-10s\n",super.getId(),super.getPassword());
		System.out.printf(" \n Name	  	    : %-20s\t\tGender  : %s\n",super.getName(),gender);
		System.out.printf(" \n Date of Birth: ", dateOfBirth);
		System.out.printf(" \n Phone No.	: ", phNo);
		System.out.printf(" \n Email		: ", email);
		System.out.printf(" \n Address		: \n\n", address);	
		System.out.println("=========================================================");
	}
	
	public void placeBooking (Booking book)
	{
		for (int i =0;i <bookingList.length;i++)
			bookingList[i] = book;
	}
}
