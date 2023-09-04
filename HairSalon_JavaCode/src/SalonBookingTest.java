import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.net.ssl.SSLEngineResult.Status;

public class SalonBookingTest {
	
		static Scanner input = new Scanner(System.in);
		static ArrayList <Customer> customer = new ArrayList<Customer> ();
		static ArrayList <Booking> booking = new ArrayList<Booking> ();
		static ArrayList <HairService> hairService = new ArrayList<HairService> ();
		static ArrayList <HairStylist> hairStylist = new ArrayList<HairStylist> ();
		static ArrayList <Admin> admin = new ArrayList<Admin> ();
		public static void main(String[] args) throws FileNotFoundException 
		{
			//read all the file needed
			readCustomerFile();
			readHairService();
			readHairStylist();
			readAdminFile();
			readBookingDetails();

			boolean found = false;
			int no = -1;
			do 
			{
				try 
				{
					System.out.println("------------------------------------------------------------------");
					System.out.println("\t   ~ WELCOME TO SALON SYSTEM ~");
					System.out.println("------------------------------------------------------------------");
					System.out.println("		         | LOGIN ACCOUNT |");
					System.out.println("------------------------------------------------------------------\n");
					System.out.println("		         1. STAFF\n");
					System.out.println("		         2. CUSTOMER\n");
					System.out.println("		         3. EXIT SYSTEM\n");
					System.out.println("==================================================================");
					System.out.print("		        Option: ");
					int loginOpt = input.nextInt(); //get input from user
					input.nextLine();

					if (loginOpt == 3)
					{
						no = 0;
						saveToBookingFile();
						saveBooking();
						saveToCustomerFile();
						System.out.println(" The information has been saved to file...");
						System.out.println("============== Exiting Salon Booking System ==============");
						System.exit(0);
					}
					else if (loginOpt == 1 || loginOpt == 2)
					{
						System.out.println("------------------------------------------------------------------");
						System.out.println("\t   ~ WELCOME TO SALON BOOKING SYSTEM ~");
						System.out.println("------------------------------------------------------------------");
						System.out.println("		         | LOGIN ACCOUNT |");
						System.out.println("------------------------------------------------------------------");
						System.out.println("		   Please provide your...\n");
						System.out.print("	   User ID       : ");
						String userID = input.nextLine(); //get userID
						System.out.print("	   User password : ");
						String userPassword = input.nextLine(); //get userPassword
						System.out.println("==================================================================");

						if (loginOpt == 1) //if option 1 chosen, proceed to administrator login
						{
							no = loginAdmin(userID, userPassword);
							if (no >= 0) //if the ID found in database
							{
								adminMenu(no); //display administrator menu
							}
							else
								System.out.println(" Please enter valid ID.");
						}
						else if (loginOpt == 2) //proceed option 2 which is customer login
						{
							no = loginCustomer(userID, userPassword); //if found, will return no >= 0

							int cusMenuOpt = 0;
							if (no >= 0)
							{
								do 
								{
									try 
									{
										Customer cc = new Customer(customer.get(no));
										cc.customerMenu(); //display customer menu
										cusMenuOpt = input.nextInt();
										if (cusMenuOpt == 0) //if option 0 chosen, exit customer interface
										{
											System.out.println(" Logout customer account...");
											no = -1;
										}
										if (cusMenuOpt == 1) //if option 1 chosen, display customer personal details
										{
											cc.displayCustomerDetails(); //display personal details

										}
										else if (cusMenuOpt == 2) //if option 2 chosen, update customer personal details
										{	
											updateCustomerPersDetails(no); //update personal details						
										}
										else if (cusMenuOpt == 3) //if option 3 chosen, display hair service list
										{							
											input.nextLine();
											viewHairServiceList();
										}
										
										else if (cusMenuOpt == 4) //if option 3 chosen, display hair service list
										{							
											input.nextLine();
											viewHairStylistList();
										}
										else if (cusMenuOpt == 5) //if option 4 chosen, place booking order
										{
											placeBooking(no);
										}
										else if (cusMenuOpt == 6) //if option 5 chosen, display the booking history
										{
											displayCustomerBookingHistory(no);

											boolean valid = true;
											do 
											{
												try 
												{
													System.out.println(" ( 0-Quit	1-Search booking )");
													System.out.print(" Option: ");
													int opt = input.nextInt();
													if (opt == 1)
													{
														searchBooking(); //if option 1 chosen, proceed search booking function
													}
													if(opt != 0 && opt != 1)
													{
														valid = false;
														System.out.println(" Please enter correct option.");
													}
													else
														valid = true;
												}
												catch(InputMismatchException exp)
												{
													System.out.println(" Please enter correct option."); 
													valid = false;
													input.nextLine();
												}
											}while(valid == false);								
										}
									}
									catch(InputMismatchException exp)
									{
										System.out.println(" Please enter correct option.");
										cusMenuOpt = 1;
										input.nextLine();
									}

								}while(cusMenuOpt != 0);
							}
							else
							{
								int regOpt = 0;
								System.out.println(" No customer found.");
								System.out.println(" Would you like to register a new account? ( 1 = Yes  0 = No)\n");
								System.out.print(" Option: ");
								regOpt = input.nextInt();

								if(regOpt == 1)
								{
									registerCustomer();
								}
								else
									no = -1;
							}
						}					
					}
					else
					{
						System.out.println(" Error option entered.");
						no = -1;
					}
				}
				catch(InputMismatchException i)
				{
					System.out.println(" Please enter correct option.");
					input.nextLine();
					no = -1;
				}
			}while(found == false || no == -1 );
		}

		private static void readHairStylist() {
			try
			{
				FileInputStream hairStylistFileIn = new FileInputStream("HairStylist.ser");
				ObjectInputStream in = new ObjectInputStream(hairStylistFileIn);		
				hairStylist = (ArrayList<HairStylist>)in.readObject();
				in.close();
				hairStylistFileIn.close();

			}
			catch (IOException i) 
			{
				i.printStackTrace();
				return;
			}
			catch (ClassNotFoundException c) 
			{
				System.out.println("Hair Service class not found");
				c.printStackTrace();
				return;
			}
			
		}

		//Read Administrator information from the file
		public static void readAdminFile()
		{
			String source="Admin.txt"; //put file name here
			String line = "";
			try 
			{
				BufferedReader br = new BufferedReader (new FileReader (source)); //read in file--create obj
				while ((line = br.readLine()) != null) //file line not empty--go into end
				{
					String [] adminList = line.split("#"); //split line by '#'
					Admin a1 = null;
					String adminName = adminList[0];
					String adminID = adminList[1];
					String password = adminList[2];
					String jobTitle = adminList[3];

					a1 = new Admin(adminName, adminID, password, jobTitle);
					admin.add(a1);
				}	
				br.close();
			} 
			catch (FileNotFoundException exp) //print out error if the file is not found
			{
				exp.printStackTrace();
			}
			catch (IOException exp) //line end
			{
				exp.printStackTrace();
			}
		}

		//Read in all booking data from file
		public static void readBookingDetails() 
		{
			String source = "Booking.txt"; //put file name here
			String line = "";
			try 
			{
				BufferedReader br = new BufferedReader (new FileReader (source)); //read in file--create object
				while ((line = br.readLine()) != null) //file line not empty--go into end
				{
					String [] bookingLine = line.split("#");//split line by '#'
					Booking b = null;

					String placeBookingDate = bookingLine[0];
					int bookingID = Integer.parseInt(bookingLine[1]);
					int bookingHour = Integer.parseInt(bookingLine[2]);
					String bookingDate = bookingLine[3];
					String customerID = bookingLine[4];

					Customer c = findCustomer(customerID);
					String adminID = bookingLine[5];
					Admin a = findStaff(adminID);
					String status = bookingLine[6];
					int noOfHairServiceOrder = Integer.parseInt(bookingLine[7]);
					int noOfHairStylistOrder = Integer.parseInt(bookingLine[8]);
					b = new Booking(placeBookingDate, bookingID, bookingHour, bookingDate, c, status,adminID, noOfHairServiceOrder,noOfHairStylistOrder);
					b.setAdmin(a);
					booking.add(b);

					String [] hairServiceListID = new String[noOfHairServiceOrder];
					for(int j = 0; j < noOfHairServiceOrder; j++)
					{
						hairServiceListID[j] = bookingLine[8+j];
					}

					HairService hs = null;
					for(int j = 0; j < noOfHairServiceOrder; j++)
					{
						boolean found = false;
						do
						{
							int y = 0;
							while(y < hairService.size() && !found)
							{
								hs = hairService.get(y);
								if(hs.getHairServiceID().equals(hairServiceListID[j]))
								{
									found = true;
									break;
								}
								else
									y++;
							}

						}while(found != true);
						b.addHairServiceFromFile(j,hs);
					}			
				}	
				br.close();
			} 
			catch (FileNotFoundException exp) //print out error if the file is not found
			{
				exp.printStackTrace();
			}
			catch (IOException exp) //line end
			{
				exp.printStackTrace();
			}
		}

		//read in all bicycle data from file
		@SuppressWarnings("unchecked")
		public static void readHairService() 
		{
			try
			{
				FileInputStream hairServiceFileIn = new FileInputStream("HairService.ser");
				ObjectInputStream in = new ObjectInputStream(hairServiceFileIn);		
				hairService = (ArrayList<HairService>)in.readObject();
				in.close();
				hairServiceFileIn.close();

			}
			catch (IOException i) 
			{
				i.printStackTrace();
				return;
			}
			catch (ClassNotFoundException c) 
			{
				System.out.println("Hair Service class not found");
				c.printStackTrace();
				return;
			}
		}

		//read customer data from file
		public static void readCustomerFile()
		{
			String source = "Customer.txt"; //put file name here
			String line = "";
			try 
			{
				BufferedReader br = new BufferedReader (new FileReader (source)); //read in file--create obj
				while ((line = br.readLine()) != null) //file line not empty--go into end
				{				
					String [] c=line.split("#");//split line by comma
					Customer c1 = null;				
					String customerID = c[0];
					String customerName = c[1];
					String password = c[2];
					char gender = c[3].charAt(0);
					String date = c[4];
					int phNo = Integer.parseInt(c[5]);
					String email = c[6];
					String address = c[7];
					c1 = new Customer(customerID, customerName, password, gender, date, phNo, email, address);				

					customer.add(c1);
				}	
				br.close();
			} 
			catch (FileNotFoundException exp) //print out error if the file is not found
			{
				exp.printStackTrace();
			}
			catch (IOException exp) //line end
			{
				exp.printStackTrace();
			}
		}

		//login to the system with administrator ID and password
		public static int loginAdmin(String userID, String userPassword)
		{
			boolean found = false;
			int no = -100;
			int i = 0;
			Admin ad = null;
			while(found == false && i < admin.size())
			{
				ad = admin.get(i); //get the first administrator for matching
				if(ad.getId().equals(userID) && ad.getPassword().equals(userPassword)) //find whether the input match with database or not
				{
					found = true; //return true if match with input(for checking only)
					System.out.println(" Valid user. WELCOME TO SALON BOOKING SYSTEM. ");
					no = i;
				}
				else i++;
			}
			return no;
		}

		//login to the system with customer ID and password
		public static int loginCustomer(String userID,String userPassword)
		{
			boolean found = false;
			int i = 0;
			Customer c = null;
			while(found == false && i < customer.size())
			{
				c = customer.get(i); //get the first customer for matching
				if(c.getId().equals(userID) && c.getPassword().equals(userPassword)) //find whether the input match with database or not
				{
					found = true; //return true if match
					System.out.println(" Valid user. WELCOME TO SALON BOOKING SYSTEM. ");
				}
				else i++; //repeat until match
			}
			if(found)
				return i; //return the number
			else return -1;
		}

		// Register new customer into the system
		public static void registerCustomer() 
		{
			System.out.println("------------------------------------------------------------------");
			System.out.println("\t   ~ WELCOME TO SALON BOOKING SYSTEM ~");
			System.out.println("------------------------------------------------------------------");
			System.out.println("\t\t   | CUSTOMER REGISTRATION |");
			System.out.println("------------------------------------------------------------------");
			Scanner input = new Scanner(System.in);
			//request customer ID from user with validation
			boolean validID = false;
			String customerID;
			do 
			{
				System.out.printf(" (CXXXX\tX - Digits)\n Enter customer ID : ");
				customerID = input.nextLine().toUpperCase();
				validID = isValidID(customerID); //customerID validation
			} while (validID == false);
			//request customer name from user with validation
			boolean validName = false;
			String customerName;
			do 
			{
				System.out.printf(" Enter customer name : ");
				customerName = input.nextLine();
				validName = isValidName(customerName); //customer name validation
			} while (validName == false);
			//request customer password from user with validation
			boolean validPassword = false;
			String customerPassword;
			do 
			{
				System.out.printf(" Password Can Only Be 6-22 Characters.\n Enter customer password  : ");
				customerPassword = input.nextLine();
				validPassword = isValidPassword(customerPassword); //password validation
			} while (validPassword == false);
			//request customer gender from user with validation
			boolean validGender = false;
			char gender;
			do 
			{
				System.out.printf(" (M-Male\tF-Female\tO-Others/Unspecified)\n Enter customer gender   : ");
				gender = input.nextLine().toUpperCase().charAt(0);
				validGender = isValidGender(gender); //gender validation
			} while (validGender == false);
			input.nextLine();
			//request customer DOB from user with validation
			boolean validDate = false;
			String dateOfBirth = null;
			do {
				System.out.printf(" Enter customer birthday date (dd/mm/yyyy): ");
				dateOfBirth = input.nextLine();
				validDate = isValidDate(dateOfBirth); //date validation
			} while (validDate == false);
			//request customer TelNo from user with validation
			boolean validPhNo = false;
			int phNo = 0;
			do 
			{
				System.out.printf(" Enter customer Tel.No (+60)  : ");
				try 
				{
					phNo = Integer.parseInt(input.nextLine());
					validPhNo = isValidTeleNo(phNo); //telephone no validation
				}
				catch (Exception e) 
				{
					System.out.println(" Please Only Enter Digits");
				}
			} while (validPhNo == false);
			//request customer email from user with validation
			boolean validEmail = false;
			String email;
			do 
			{
				System.out.printf(" Enter customer email address : ");
				email = input.nextLine();
				validEmail = isValidEmail(email); //email validation
			} while (validEmail == false);
			//request customer address from user with validation
			boolean validAddress = false;
			String address;
			do 
			{
				System.out.printf(" Enter customer address  : ");
				address = input.nextLine();
				validAddress = isValidAddress(address); //address validation
			} while (validAddress == false);
			System.out.println("==================================================================");

			Customer c1 = new Customer(customerID, customerName, customerPassword, gender, dateOfBirth, phNo, email, address);
			customer.add(c1);
		}

		//Display administrator menu
		public static void adminMenu(int no)
		{
			Scanner input = new Scanner(System.in);
			int adminOpt = 0;
			do
			{
				try 
				{
					System.out.println("------------------------------------------------------------------");
					System.out.println("\t   ~ WELCOME TO SALON BOOKING SYSTEM ~");
					System.out.println("------------------------------------------------------------------");
					System.out.println("\t\t\t| ADMIN MENU |");
					System.out.println("------------------------------------------------------------------\n");
					System.out.println("\t\t   1.  View Admin List\n");
					System.out.println("\t\t   2.  View Customer List\n");
					System.out.println("\t\t   3.  Delete Customer\n");
					System.out.println("\t\t   4.  View Booking Order List\n");
					System.out.println("\t\t   5.  Update Booking Order Details\n");
					System.out.println("\t\t   6.  View Hair Service List\n");
					System.out.println("\t\t   7.  Add Hair Service Data\n");
					System.out.println("\t\t   8.  Delete Hair Service Data\n");
					System.out.println("\t\t   9.  Update Hair Service Data\n");
					System.out.println("\t\t   10. View Hair Stylist List\n");
					System.out.println("\t\t   11. Add Hair Stylist Data\n");
					System.out.println("\t\t   12. Delete Hair Stylist Data\n");
					System.out.println("\t\t   13. Update Hair Stylist Data\n");
					System.out.println("==================================================================");
					System.out.println(" Enter 0 to quit. ");
					System.out.print(" Option: ");
					adminOpt = input.nextInt();
					if (adminOpt == 0) //if option 0 chosen, exit administrator interface
					{
						System.out.println(" Logout admin account...");
					}
					if (adminOpt == 1) //if option 1 chosen, display administrator list
					{
						viewAdminList(); //display administrator list
					}
					else if (adminOpt == 2) //if option 2 chosen, display customer list
					{
						viewCustomerList(); //display customer list
					}
					else if (adminOpt == 3) //if option 3 chosen, delete customer 
					{
						deleteCustomer();
					}
					else if (adminOpt == 4) //if option 4 chosen, display booking list of all customer
					{
						viewBookingList();
					}
					else if (adminOpt == 5) //if option 5 chosen, update the booking order
					{
						updateBooking(no);
					}
					else if (adminOpt == 6) //if option 6 chosen, display hair service list
					{
						viewHairServiceList();
					}
					else if (adminOpt == 7) //if option 7 chosen, add hair service data
					{
						addHairService();
					}
					else if (adminOpt == 8) //if option 8 chosen, delete hair service data
					{
						deleteHairService();
					}
					else if (adminOpt == 9) //if option 9 chosen, update hair service details
					{
						updateHairService();
					}
					else if (adminOpt == 10) //if option 10 chosen, display hair stylist list
					{
						viewHairStylistList();
					}
					else if (adminOpt == 11) //if option 11 chosen, add hair stylist data
					{
						addHairStylist();
					}
					else if (adminOpt == 12) //if option 12 chosen, delete hair stylist data
					{
						deleteHairStylist();
					}
					else if (adminOpt == 13) //if option 13 chosen, update hair stylist details
					{
						updateHairStylist();
					}
				}
				catch(InputMismatchException i)
				{
					System.out.println(" Please enter correct option.");
					input.nextLine();
					adminOpt = 1;
				}
			}while(adminOpt != 0);
		}

		//Display administrator list for administrator view only
		public static void viewAdminList()
		{
			System.out.println("------------------------------------------------------------------");
			System.out.println("\t   ~ WELCOME TO SALON BOOKING SYSTEM ~");
			System.out.println("------------------------------------------------------------------");
			System.out.println("\t\t\t| ADMIN LIST |\t\tTotal: " + admin.size());
			System.out.println("------------------------------------------------------------------");
			System.out.println("  No.  | Admin ID |\t   Name   	| Password |\tRole\t|");
			System.out.println("------------------------------------------------------------------");

			for(int i = 0; i < admin.size();i++)
			{
				System.out.printf("  %-2d.\t %-5s\t\t%-15s\t  %-8s\t%-10s\n", (i+1), admin.get(i).getId(), admin.get(i).getName(), admin.get(i).getPassword(),admin.get(i).getJobTitle());
			}
			System.out.println("==================================================================\n");
		}

		//Display customer list for administrator only
		public static void viewCustomerList()
		{
			Scanner input = new Scanner(System.in);
			System.out.println("------------------------------------------------------------------");
			System.out.println("\t   ~ WELCOME TO SALON BOOKING SYSTEM ~");
			System.out.println("------------------------------------------------------------------");
			System.out.println("\t\t       | CUSTOMER LIST |\t\tTotal: " + customer.size());
			System.out.println("------------------------------------------------------------------");
			System.out.println("  No.  |     ID     |        Customer Name        |  TelNo (+60) | ");
			System.out.println("------------------------------------------------------------------");
			for(int i = 0; i < customer.size();i++)
			{
				System.out.printf("  %-2d.     %-8s\t%-25s    %-11d\n" , i+1, customer.get(i).getId() , customer.get(i).getName(), customer.get(i).getPhNo());
			}
			System.out.println("==================================================================");

			boolean found = false;
			int option = 0;
			do 
			{
				found = searchCustomer();
				if(found == false)
					System.out.println(" Customer ID not found . Please enter again .");
				boolean valid = true;
				do 
				{
					try 
					{
						System.out.print(" Do you want to search Customer ID again ?( 1 = Yes  0 = No)\n");
						System.out.print(" Option: ");
						option = input.nextInt();
						if (option == 0||option == 1)
							valid = true;
						else
							valid = false;
					}
					catch(InputMismatchException exp)
					{
						System.out.println(" Please enter correct option.");
						input.nextLine();
						valid = false;
					}
				}while(valid == false);
			}while(option != 0);
		}

		//Delete customer
		public static void deleteCustomer()
		{

			String userID;
			boolean found = false;
			do 
			{
				System.out.println("------------------------------------------------------------------");
				System.out.println("\t   ~ WELCOME TO SALON BOOKING SYSTEM ~");
				System.out.println("------------------------------------------------------------------");
				System.out.println("\t\t   | Delete Customer Data |");
				System.out.println("------------------------------------------------------------------");
				System.out.print(" Enter customer ID to delete : ");
				userID = input.nextLine();

				int i = 0;
				Customer c = null;
				while(found == false && i < customer.size())
				{ 
					int delete=2;
					c = customer.get(i); //get the first customer for matching
					if(c.getId().equals(userID) ) //find whether the input match with database or not
					{
						found = true;
						c.displayCustomerDetails();
						boolean validOption = true;
						do {
							try {
								System.out.println(" Do you want to delete this user ?( 1 = Yes  0 = No)");
								System.out.print(" Option: ");
								delete = input.nextInt();
								if(delete != 0 && delete != 1)
								{
									validOption = false;
									System.out.println(" Error: Please enter correct input");
								}
								else
									validOption = true;
							}catch(InputMismatchException exp)
							{
								validOption = false;
								System.out.println(" Error: Please enter correct input");
								input.nextLine();
							}
						}while(validOption == false);
						//Check whether the customer ID is in any booking order or not, if yes, cannot delete customer
						boolean inBooking = false;
						int j = 0;
						Booking b = null;
						while(inBooking == false && j < booking.size())
						{
							b = booking.get(j);
							if(b.getCustomer().getId().contentEquals(userID))
							{
								inBooking = true;
								System.out.println(" You are not allowed to delete Customer ID that contains in booking.");
							}
							else
								j++;
						}

						if(delete == 1&&inBooking==false) //if option 1 chosen, delete the customer
						{	
							customer.remove(i); 
							System.out.println(" Customer data has been deleted.");
						}
						break;
					}
					else i++; //repeat until match
				}
				if(found != true)
				{
					System.out.println(" Customer ID is not in the list . Please enter again ");
					i = 100;
				}
			}while(found != true);
		}

		//display all the booking list 
		public static void viewBookingList()
		{
			System.out.println("------------------------------------------------------------------");
			System.out.println("\t   ~ WELCOME TO SALON BOOKING SYSTEM ~");
			System.out.println("------------------------------------------------------------------");
			System.out.println("\t\t    | BOOKING LIST |\t\t");
			System.out.println("------------------------------------------------------------------");
			System.out.println("  No.  |\tBooking ID\t|\tCustomer ID\t|");
			System.out.println("------------------------------------------------------------------");
			for(int i = 0;i < booking.size();i++)
			{		
				System.out.printf("  %-2d.\t\t%-4d\t\t\t%-6s\n", (i+1),  booking.get(i).getBookingID(), booking.get(i).getCustomer().getId());
			}

			System.out.println("==================================================================\n");

			int option = 1;
			do 
			{
				searchBooking();
				boolean valid = true;
				do 
				{
					try 
					{
						System.out.print(" Do you want to search Booking ID again ?( 1 = Yes  0 = No)\n");
						System.out.print(" Option: ");
						option = input.nextInt();
						if (option == 0 || option == 1)
							valid = true;
						else
							valid = false;
					}
					catch(InputMismatchException exp)
					{
						System.out.println(" Please enter correct option.");
						input.nextLine();
						valid = false;
					}
				}while(valid == false);
			}while(option == 1);
		}

		//update rental status
		public static void updateBooking(int no)
		{		
			boolean found = false;
			boolean valid = true;
			int bookingID = 0;
			do
			{
				do 
				{
					try 
					{
						viewBookingList();
						System.out.println(" You are allow to view booking details before update.");
						System.out.print(" Enter Booking ID to update details: ");
						bookingID = input.nextInt();
						valid = true;			
					}
					catch(InputMismatchException exp)
					{
						System.out.println(" Please enter valid Booking ID.");
						valid = false;
						input.nextLine();
					}	
				}while(valid == false);

				int i = 0;
				Booking b = null;
				while(found == false && i < booking.size())
				{
					b = booking.get(i); //get the first customer for matching
					String b1 = Integer.toString(bookingID);
					String b2 = Integer.toString(b.getBookingID());

					if(b2.equals(b1) ) //find whether the input match with database or not
					{
						int updateBookOpt = 0;
						do 
						{
							try
							{
								System.out.println("\n\t~ Update Booking Details ~ ");
								System.out.println(" -------------------------------------------");
								System.out.println(" 1. Update Booking Status\n ");
								System.out.println(" 2. Update Admin ID\n ");
								System.out.println(" -------------------------------------------");
								System.out.println(" Enter 0 to exit Update Booking Function. ");
								System.out.print(" Option: ");
								updateBookOpt = input.nextInt();
								input.nextLine();
								if(updateBookOpt == 1)
								{
									boolean isValidStatus = true;
									do 
									{
										found = true;
										b.displayBookingOrderDetails();;
										System.out.print(" Enter booking status : ");
										String bookingStatus = input.nextLine();
										if (isValidName(bookingStatus) == true)
											b.setStatus(bookingStatus);
										else
										{
											System.out.println(" Invalid status entered.");
											isValidStatus = false;
											//break;
										}
									}while(isValidStatus != true);
								}
								else if(updateBookOpt == 2)
								{
									found = true;
									b.displayBookingOrderDetails();
									b.setAdmin(admin.get(no));
									b.displayBookingOrderDetails();
									break;
								}
								else if(updateBookOpt == 0)
								{
									found = true;
									System.out.println(" Exit update booking function. ");
									booking.set(i,b);
									updateBookOpt = 0;
									break;
								}
								else
								{
									System.out.println(" Invalid update booking option entered. ");
									updateBookOpt = 1;
								}
							}
							catch(InputMismatchException e)
							{
								System.out.println(" Please enter a valid option.");
								input.nextLine();
							}
						}while (updateBookOpt != 0);
					}
					else i++; //repeat until match
				}
				if(found == false)
					System.out.println(" Can't found any booking order. Please enter again.");
			}while(found != true);	
		}

		//Display hair service list for both customer and administrator view
		public static void viewHairServiceList()
		{

			System.out.println("------------------------------------------------------------------");
			System.out.println("\t   ~ WELCOME TO SALON BOOKING SYSTEM ~");
			System.out.println("------------------------------------------------------------------");
			System.out.println("\t\t    | HAIR SERVICE LIST |\t\tTotal: " + hairService.size());
			System.out.println("------------------------------------------------------------------");
			System.out.println("  No.  |\tHair Service ID\t|\tHair ServiceName\t\t|");
			System.out.println("------------------------------------------------------------------");

			for(int i = 0; i < hairService.size();i++)
			{
				System.out.printf("  %-2d.\t\t%-6s\t\t\t%-15s\n", (i+1), hairService.get(i).getHairServiceID(), hairService.get(i).getHairServiceName());
			}
			System.out.println("==================================================================\n");

			boolean found = false;
			int option = 0;
			do 
			{
				found = searchHairService();
				if(found == false)
					System.out.println(" Hair Service ID not found . Please enter again .");
				boolean valid = true;
				do 
				{
					try 
					{
						System.out.print(" Do you want to search Hair Service ID again ?( 1 = Yes  0 = No)\n");
						System.out.print(" Option: ");
						option = input.nextInt();
						input.nextLine();
						if (option == 0 || option == 1)
							valid = true;
						else
							valid = false;
					}
					catch(InputMismatchException exp)
					{
						System.out.println(" Please enter correct option.");
						input.nextLine();
						valid = false;
					}
				}while(valid == false);
			}while(option != 0);
		}

		//Add new hair service into the array list
		public static void addHairService()
		{
			Scanner inputHairService = new Scanner(System.in);
			String hairServiceName,hairServiceID, hairServiceDesc;
			double hairServicePrice = 0;
			System.out.println("------------------------------------------------------------------");
			System.out.println("\t   ~ WELCOME TO SALON BOOKING SYSTEM ~");
			System.out.println("------------------------------------------------------------------");
			System.out.println("\t\t   | Add Hair Service |");
			System.out.println("------------------------------------------------------------------");
			//request hair service ID from user with validation
			boolean validHairServiceID = false;
			do 
			{
				System.out.print(" ( XXXX- Digits)\n Enter Hair Service ID : ");
				hairServiceID = inputHairService.nextLine().toUpperCase();
				validHairServiceID = isValidHairServiceID(hairServiceID); //hairServiceID validation
			} while (validHairServiceID == false);
			//request customer name from user with validation
			boolean validHairServiceName = false;
			do 
			{
				System.out.print(" Enter Hair Service Name: ");
				hairServiceName = inputHairService.nextLine();
				validHairServiceName = isValidHairServiceName(hairServiceName); //hair service name validation
			} while (validHairServiceName == false);
			//request customer size from user with validation
			
			
			boolean validHairServicePrice = false;
			do
			{
				System.out.print(" Enter Hair Service Price (RM) : ");
				try
				{
					hairServicePrice = Double.parseDouble(input.nextLine());
					validHairServicePrice = true;
				}
				catch ( Exception e)
				{
					System.out.println(" Invalid Input. Please Enter a Number.");

				}
			}while (validHairServicePrice == false);

			boolean validDesc = false;
			do
			{
				System.out.print(" Enter Hair Service Description: ");
				hairServiceDesc = inputHairService.nextLine();
				validDesc = isValidDesc(hairServiceDesc);
			}while (validDesc == false);
			System.out.println("==================================================================");

			HairService addHairService = new HairService(hairServiceName,hairServiceID, hairServiceDesc, hairServicePrice);
			hairService.add(addHairService);
		}

		//Delete hairService
		public static void deleteHairService()
		{
			Scanner input = new Scanner (System.in);
			String hairServiceID;
			HairService hs = null;
			boolean found = false;
			do
			{
				int j = 0;
				System.out.println("------------------------------------------------------------------");
				System.out.println("\t   ~ WELCOME TO SALON BOOKING SYSTEM ~");
				System.out.println("------------------------------------------------------------------");
				System.out.println("\t\t   | Delete Hair Service Data |");
				System.out.println("------------------------------------------------------------------");
				System.out.println("\t\t    | Hair Service LIST |\t\tTotal: " + hairService.size());
				System.out.println("------------------------------------------------------------------");
				System.out.println("  No.  |\tHair Service ID\t|\tHair Service Name\t\t|");
				System.out.println("------------------------------------------------------------------");

				for(int i = 0; i < hairService.size();i++)
				{
					System.out.printf("  %-2d.\t\t%-6s\t\t\t%-15s\n", (i+1), hairService.get(i).getHairServiceID(), hairService.get(i).getHairServiceName());
				}
				System.out.println("==================================================================\n");
				System.out.print(" Enter Hair Service ID to delete : ");
				hairServiceID = input.nextLine().toUpperCase();
				while(j < hairService.size() && !found)
				{
					hs = hairService.get(j);
					if(hs.getHairServiceID().equals(hairServiceID)) //if the hairService ID match with database
					{
						found = true;
						int delete = 2;
						boolean validOption=true;
						do {
							try {
								System.out.println(" Do you want to delete this hair service ?( 1 = Yes  0 = No)");
								System.out.print(" Option: ");
								delete = input.nextInt();
								if(delete != 0 && delete != 1)
								{
									validOption = false;
									System.out.println(" Error: Please enter correct input");
								}
								else
									validOption = true;
							}catch(InputMismatchException exp)
							{
								validOption = false;
								System.out.println(" Error: Please enter correct input");
								input.nextLine();
							}
						}while(validOption == false);
						//Check whether the hair service ID is in any booking order or not, if yes, cannot delete customer
						boolean inBooking = false;
						int k = 0;
						Booking b = null;
						while(inBooking == false && k < booking.size())
						{
							b = booking.get(k);
							HairService [] list = new HairService[b.getNoOfHairService()];
							list = b.getHairServiceBookingList();
							for(int l = 0; l < b.getNoOfHairService(); l++)
							{
								if(list[l].getHairServiceID().contentEquals(hairServiceID))
								{
									inBooking = true;
									System.out.println(" You are not allowed to delete Hair Service ID that contains in booking.");
								}
							}
							k++;
						}
						if(delete == 1 && inBooking == false) //option 1 chosen to delete booking details
						{hairService.remove(j);
						System.out.println(" Delete Hair Service");
						}
					}
					else
						j++;
				}
				if(found != true)
				{
					System.out.println(" Hair Service  ID is not in the list . Please enter again ");
					j = 100;
				}
			}while(found != true);
		}

		//update bicycle data
		public static void updateHairService()
		{
			String hairServiceID;	
			HairService b = null;
			boolean found = false;
			do
			{
				int j = 0;
				System.out.println("------------------------------------------------------------------");
				System.out.println("\t   ~ WELCOME TO SALON BOOKING SYSTEM ~");
				System.out.println("------------------------------------------------------------------");
				System.out.println("\t\t   | Update Hair Service Data |");
				System.out.println("\t\t    | HAIR SERVICE LIST |\t\tTotal: " + hairService.size());
				System.out.println("------------------------------------------------------------------");
				System.out.println("  No.  |\tHair Service ID\t|\tHair Service Name\t\t|");
				System.out.println("------------------------------------------------------------------");

				for(int i = 0; i < hairService.size();i++)
				{
					System.out.printf("  %-2d.\t\t%-6s\t\t\t%-15s\n", (i+1), hairService.get(i).getHairServiceID(), hairService.get(i).getHairServiceName());
				}
				System.out.println("==================================================================\n");
				System.out.print(" Enter Hair Service ID to update: ");
				hairServiceID = input.nextLine().toUpperCase();
				while(j < hairService.size() && !found)
				{
					b = hairService.get(j);
					if(b.getHairServiceID().equals(hairServiceID))
					{
						b.displayHairServiceDetails();
						found = true;
						boolean again = true;
						boolean valid = true;
						int option;
						do 
						{
							System.out.println("\t  Update hair service details ");
							System.out.println("\t--------------------------");
							System.out.println(" 1. Update Hair Service Name\n ");
							System.out.println(" 2. Update Hair Service Description\n ");
							System.out.println(" 3. Update Hair Service Price \n");
							System.out.println(" 4. Exit update\n");
							do 
							{
								try 
								{
									System.out.print(" Please enter your option : ");
									option = input.nextInt();
									switch (option)
									{
									case 1:
									{
										boolean validHairServiceName = false;
										do
										{
											input.nextLine();
											System.out.println(" Update Hair Service Name");
											System.out.println(" Original Hair Service Name : "+b.getHairServiceName());
											System.out.print(" New Hair Service Name      : ");
											String hairServiceName = input.nextLine();
											validHairServiceName = isValidHairServiceName(hairServiceName);

											if (validHairServiceName == true)
											{	
												b.setHairServiceName(hairServiceName);
											}

										}while (validHairServiceName == false);					
										break;

									}
									
									case 2:
									{
										boolean validDesc = false;
										do
										{
											input.nextLine();
											System.out.println(" Update Hair Service Description");
											System.out.println(" Original Hair Service Description : "+b.getHairServiceDesc());
											System.out.print(" New Hair Service Description      : ");
											String hairServiceDesc = input.nextLine();
											validDesc = isValidDesc(hairServiceDesc);
											if (validDesc == true)
											{
												b.setHairServiceDesc(hairServiceDesc );										}

										}while (validDesc == false);
										break;

									} 

									case 3:
									{
										boolean validPrice = false;
										double price;
										do
										{
											input.nextLine();
											System.out.println(" Update Hair Service Price");
											System.out.println(" Original Hair Service Price: "+b.getHairServicePrice());
											System.out.print(" New Hair Service Price       : ");
											try
											{
												price = Double.parseDouble(input.nextLine());
												if (price < 15)
												{
													validPrice = true;
													b.setHairServicePrice(price);
												}
												else
												{
													System.out.println(" Unrelevant price, please enter again");
													validPrice = false;
												}
											}
											catch (Exception e)
											{
												System.out.println(" Invalid input. Please enter a number.");

												System.out.print(" New Hair Service Price         : ");
												validPrice = false;
											}
										}while (validPrice == false);

										break;
									}
									case 4:
									{
										System.out.println(" Exit updating");
										again = false;
										break;
									}
									default:
									{
										System.out.println(" Wrong option");
										break;
									}
									}
									valid = true;	
								}
								catch(InputMismatchException exp)
								{
									System.out.println(" Please enter correct option.");
									input.nextLine();
									valid = false;
								}  
							}while(valid == false);
						}while(again == true);

						hairService.set(j,b);
						break;
					}
					else
						j++;
				}
				if(found != true)
				{
					System.out.println(" Hair Service ID is not in the list . Please enter again ");
					j = 100;
				}	
			}while(found != true);
		}
		//Display hair service list for both customer and administrator view
		public static void viewHairStylistList()
		{

					System.out.println("------------------------------------------------------------------");
					System.out.println("\t   ~ WELCOME TO SALON BOOKING SYSTEM ~");
					System.out.println("------------------------------------------------------------------");
					System.out.println("\t\t    | HAIR STYLIST LIST |\t\tTotal: " + hairStylist.size());
					System.out.println("------------------------------------------------------------------");
					System.out.println("  No.  |\tHair Stylist ID\t|\tHair Stylist Name\t\t|");
					System.out.println("------------------------------------------------------------------");

					for(int i = 0; i < hairStylist.size();i++)
					{
						System.out.printf("  %-2d.\t\t%-6s\t\t\t%-15s\n", (i+1), hairStylist.get(i).getHairStylistID(),hairStylist.get(i).getHairStylistName());
					}
					System.out.println("==================================================================\n");

					boolean found = false;
					int option = 0;
					do 
					{
						found = searchHairStylist();
						if(found == false)
							System.out.println(" Hair Stylist ID not found . Please enter again .");
						boolean valid = true;
						do 
						{
							try 
							{
								System.out.print(" Do you want to search Hair Stylist ID again ?( 1 = Yes  0 = No)\n");
								System.out.print(" Option: ");
								option = input.nextInt();
								input.nextLine();
								if (option == 0 || option == 1)
									valid = true;
								else
									valid = false;
							}
							catch(InputMismatchException exp)
							{
								System.out.println(" Please enter correct option.");
								input.nextLine();
								valid = false;
							}
						}while(valid == false);
					}while(option != 0);
		}


				//Add new hair stylist into the array list
		public static void addHairStylist()
	    {
					Scanner inputHairStylist = new Scanner(System.in);
					String hairStylistName,hairStylistID, hairStylistDesc;
					double hairStylistPrice = 0;
					System.out.println("------------------------------------------------------------------");
					System.out.println("\t   ~ WELCOME TO SALON BOOKING SYSTEM ~");
					System.out.println("------------------------------------------------------------------");
					System.out.println("\t\t   | Add Hair Stylist |");
					System.out.println("------------------------------------------------------------------");
					//request hair stylist ID from user with validation
					boolean validHairStylistID = false;
					do 
					{
						System.out.print(" ( XXXX- Digits)\n Enter Hair Stylist ID : ");
						hairStylistID = inputHairStylist.nextLine().toUpperCase();
						validHairStylistID = isValidHairServiceID(hairStylistID); //hairStylistID validation
					} while (validHairStylistID == false);
					//request customer name from user with validation
					boolean validHairStylistName = false;
					do 
					{
						System.out.print(" Enter Hair Stylist Name: ");
						hairStylistName = inputHairStylist.nextLine();
						validHairStylistID = isValidHairServiceName(hairStylistName); //hair stylist name validation
					} while (validHairStylistID == false);
					//request customer size from user with validation
					
					
					boolean validHairStylistPrice = false;
					do
					{
						System.out.print(" Enter Hair Stylist Price (RM) : ");
						try
						{
							hairStylistPrice = Double.parseDouble(input.nextLine());
							validHairStylistPrice = true;
						}
						catch ( Exception e)
						{
							System.out.println(" Invalid Input. Please Enter a Number.");

						}
					}while (validHairStylistPrice == false);

					boolean validDesc = false;
					do
					{
						System.out.print(" Enter Hair Stylist Description: ");
						hairStylistDesc = inputHairStylist.nextLine();
						validDesc = isValidDesc(hairStylistDesc);
					}while (validDesc == false);
					System.out.println("==================================================================");

					HairStylist addHairStylist = new HairStylist(hairStylistName,hairStylistID, hairStylistDesc, hairStylistPrice);
					hairStylist.add(addHairStylist);
		}

				//Delete hairService
		public static void deleteHairStylist()
		{
					Scanner input = new Scanner (System.in);
					String hairStylistID;
					HairStylist hs = null;
					boolean found = false;
					do
					{
						int j = 0;
						System.out.println("------------------------------------------------------------------");
						System.out.println("\t   ~ WELCOME TO SALON BOOKING SYSTEM ~");
						System.out.println("------------------------------------------------------------------");
						System.out.println("\t\t   | Delete Hair Stylist Data |");
						System.out.println("------------------------------------------------------------------");
						System.out.println("\t\t    | Hair Stylist LIST |\t\tTotal: " + hairStylist.size());
						System.out.println("------------------------------------------------------------------");
						System.out.println("  No.  |\tHair Stylist ID\t|\tHair Stylist Name\t\t|");
						System.out.println("------------------------------------------------------------------");

						for(int i = 0; i < hairStylist.size();i++)
						{
							System.out.printf("  %-2d.\t\t%-6s\t\t\t%-15s\n", (i+1), hairStylist.get(i).getHairStylistID(), hairStylist.get(i).getHairStylistName());
						}
						System.out.println("==================================================================\n");
						System.out.print(" Enter Hair Stylist ID to delete : ");
						hairStylistID = input.nextLine().toUpperCase();
						while(j < hairStylist.size() && !found)
						{
							hs = hairStylist.get(j);
							if(hs.getHairStylistID().equals(hairStylistID)) //if the hairStylist ID match with database
							{
								found = true;
								int delete = 2;
								boolean validOption=true;
								do {
									try {
										System.out.println(" Do you want to delete this hair stylist ?( 1 = Yes  0 = No)");
										System.out.print(" Option: ");
										delete = input.nextInt();
										if(delete != 0 && delete != 1)
										{
											validOption = false;
											System.out.println(" Error: Please enter correct input");
										}
										else
											validOption = true;
									}catch(InputMismatchException exp)
									{
										validOption = false;
										System.out.println(" Error: Please enter correct input");
										input.nextLine();
									}
								}while(validOption == false);
								//Check whether the hair service ID is in any booking order or not, if yes, cannot delete customer
								boolean inBooking = false;
								int k = 0;
								Booking b = null;
								while(inBooking == false && k < booking.size())
								{
									b = booking.get(k);
									HairStylist[] list = new HairStylist[b.getNoOfHairStylist()];
									list = b.getHairStylistBookingList();
									for(int l = 0; l < b.getNoOfHairStylist(); l++)
									{
										if(list[l].getHairStylistID().contentEquals(hairStylistID))
										{
											inBooking = true;
											System.out.println(" You are not allowed to delete Hair Stylist ID that contains in booking.");
										}
									}
									k++;
								}
								if(delete == 1 && inBooking == false) //option 1 chosen to delete booking details
								{hairStylist.remove(j);
								System.out.println(" Delete Hair Stylist");
								}
							}
							else
								j++;
						}
						if(found != true)
						{
							System.out.println(" Hair Stylist  ID is not in the list . Please enter again ");
							j = 100;
						}
					}while(found != true);
		}

				//update bicycle data
		public static void updateHairStylist()
		{
					String hairStylistID;	
					HairStylist b = null;
					boolean found = false;
					do
					{
						int j = 0;
						System.out.println("------------------------------------------------------------------");
						System.out.println("\t   ~ WELCOME TO SALON BOOKING SYSTEM ~");
						System.out.println("------------------------------------------------------------------");
						System.out.println("\t\t   | Update Hair Stylist Data |");
						System.out.println("\t\t    | HAIR STYLIST LIST |\t\tTotal: " + hairStylist.size());
						System.out.println("------------------------------------------------------------------");
						System.out.println("  No.  |\tHair StylistID\t|\tHair Stylist Name\t\t|");
						System.out.println("------------------------------------------------------------------");

						for(int i = 0; i < hairStylist.size();i++)
						{
							System.out.printf("  %-2d.\t\t%-6s\t\t\t%-15s\n", (i+1), hairStylist.get(i).getHairStylistID(), hairStylist.get(i).getHairStylistName());
						}
						System.out.println("==================================================================\n");
						System.out.print(" Enter Hair Stylist ID to update: ");
						hairStylistID = input.nextLine().toUpperCase();
						while(j < hairStylist.size() && !found)
						{
							b = hairStylist.get(j);
							if(b.getHairStylistID().equals(hairStylistID))
							{
								b.displayHairStylistDetails();;
								found = true;
								boolean again = true;
								boolean valid = true;
								int option;
								do 
								{
									System.out.println("\t  Update hair stylist details ");
									System.out.println("\t--------------------------");
									System.out.println(" 1. Update Hair Stylist Name\n ");
									System.out.println(" 2. Update Hair Stylist Description\n ");
									System.out.println(" 3. Update Hair Stylist Price \n");
									System.out.println(" 4. Exit update\n");
									do 
									{
										try 
										{
											System.out.print(" Please enter your option : ");
											option = input.nextInt();
											switch (option)
											{
											case 1:
											{
												boolean validHairStylistName = false;
												do
												{
													input.nextLine();
													System.out.println(" Update Hair Stylist Name");
													System.out.println(" Original Hair Stylist Name : "+b.getHairStylistName());
													System.out.print(" New Hair Stylist Name      : ");
													String hairStylistName = input.nextLine();
													validHairStylistName = isValidHairStylistName(hairStylistName);

													if (validHairStylistName == true)
													{	
														b.setHairStylistName(hairStylistName);
													}

												}while (validHairStylistName == false);					
												break;

											}
											
											case 2:
											{
												boolean validDesc = false;
												do
												{
													input.nextLine();
													System.out.println(" Update Hair Stylist Description");
													System.out.println(" Original Hair Stylist Description : "+b.getStylistDesc());
													System.out.print(" New Hair Stylist Description      : ");
													String hairStylistDesc = input.nextLine();
													validDesc = isValidDesc(hairStylistDesc);
													if (validDesc == true)
													{
														b.setStylistDesc(hairStylistDesc );										}

												}while (validDesc == false);
												break;

											} 

											case 3:
											{
												boolean validPrice = false;
												double price;
												do
												{
													input.nextLine();
													System.out.println(" Update Hair Stylist Price ");
													System.out.println(" Original Hair Stylist Price : "+b.getHairStylistPrice());
													System.out.print(" New Hair Stylist Price       : ");
													try
													{
														price = Double.parseDouble(input.nextLine());
														if (price < 15)
														{
															validPrice = true;
															b.setHairStylistPrice(price);
														}
														else
														{
															System.out.println(" Unrelevant price, please enter again");
															validPrice = false;
														}
													}
													catch (Exception e)
													{
														System.out.println(" Invalid input. Please enter a number.");

														System.out.print(" New Hair Stylist Price        : ");
														validPrice = false;
													}
												}while (validPrice == false);

												break;
											}
											case 4:
											{
												System.out.println(" Exit updating");
												again = false;
												break;
											}
											default:
											{
												System.out.println(" Wrong option");
												break;
											}
											}
											valid = true;	
										}
										catch(InputMismatchException exp)
										{
											System.out.println(" Please enter correct option.");
											input.nextLine();
											valid = false;
										}  
									}while(valid == false);
								}while(again == true);

								hairStylist.set(j,b);
								break;
							}
							else
								j++;
						}
						if(found != true)
						{
							System.out.println(" Hair Stylist ID is not in the list . Please enter again ");
							j = 100;
						}	
					}while(found != true);
		}

		//Search customer with customer ID
		public static boolean searchCustomer()
		{
			Scanner input = new Scanner(System.in);
			String userID;
			System.out.print(" Enter customer ID to look into details: ");
			userID = input.nextLine();
			boolean found = false;
			int i = 0;
			Customer c = null;
			while(found == false && i < customer.size())
			{
				c = customer.get(i); //get the first customer for matching
				if(c.getId().equals(userID) ) //find whether the input match with database or not
				{
					found = true; 
					System.out.println(" Customer found!");
					c.displayCustomerDetails();
					break;
				}
				else i++; //repeat until match
			}
			return found;
		}

		//search  bicycle with bicycle ID
		public static boolean searchHairService()
		{
			Scanner input = new Scanner(System.in);
			String hairServiceID;
			System.out.print(" Enter hair service ID to look into details: ");
			hairServiceID = input.nextLine();
			boolean found = false;
			int i = 0;
			HairService hs = null;
			while(found == false && i < hairService.size())
			{
				hs = hairService.get(i); //get the first customer for matching
				if(hs.getHairServiceID().equals(hairServiceID) ) //find whether the input match with database or not
				{
					found = true;
					System.out.println(" Hair Service found!");
					hs.displayHairServiceDetails();;
					break;
				}
				else i++; //repeat until match
			}
			return found;
		}
		
		private static boolean searchHairStylist() {
	
			Scanner input = new Scanner(System.in);
			String hairStylistID;
			System.out.print(" Enter hair stylist ID to look into details: ");
			hairStylistID = input.nextLine();
			boolean found = false;
			int i = 0;
			HairStylist hs = null;
			while(found == false && i < hairStylist.size())
			{
				hs = hairStylist.get(i); //get the first customer for matching
				if(hs.getHairStylistID().equals(hairStylistID) ) //find whether the input match with database or not
				{
					found = true;
					System.out.println(" Hair Stylist found!");
					hs.displayHairStylistDetails();;
					break;
				}
				else i++; //repeat until match
			}
			return found;
		}

		//search rental order by rental ID   
		public static boolean searchBooking()
		{
			boolean found = false;
			boolean valid = true;
			int bookingID = 0;
			do
			{
				do 
				{
					try 
					{
						System.out.print(" Enter Booking ID to look into details : ");
						bookingID = input.nextInt();
						valid = true;

					}
					catch(InputMismatchException exp)
					{
						System.out.println(" Please enter valid Booking ID.");
						valid = false;
						input.nextLine();
					}
				}while(valid == false);

				int i = 0;
				Booking b = null;
				while(found == false && i < booking.size())
				{
					b = booking.get(i); //get the first customer for matching
					String r1 = Integer.toString(bookingID);
					String r2 = Integer.toString(b.getBookingID());
					if(r2.equals(r1) ) //find whether the input match with database or not
					{
						found = true;
						b.displayBookingOrderDetails();;
						break;
					}
					else i++; //repeat until match
				}
				if(found == false)
					System.out.println(" Can't found any booking order. Please enter again.");
			}while(found != true);
			return found;
		}

		//find a customer which match customer ID for finding customer object details after read in rental file
		public static Customer findCustomer(String customerID)
		{
			boolean found = false; //must find a customer obj
			int i = 0;

			Customer c = null;
			while(found == false && i < customer.size())
			{
				c = customer.get(i); //get the first customer for matching
				if(c.getId().equals(customerID) ) //find whether the input match with database or not
				{
					found = true;
					break;
				}
				else i++; //repeat until match
			}
			return c;
		}

		//find administrator ID
		public static Admin findStaff(String staffID)
		{
			boolean found = false;
			int i = 0;

			Admin a = null;
			while(found == false && i < admin.size())
			{
				a = admin.get(i); //get the first customer for matching
				if(a.getId().equals(staffID) ) //find whether the input match with database or not
				{
					found = true;
					break;
				}
				else 
				{i++; //repeat until match
				a = admin.get(0); //if not found, null
				}
			}
			return a; 
		}

		//update customer personal details
		public static void updateCustomerPersDetails(int no)
		{
			Customer cc = new Customer(customer.get(no));
			boolean again = true;
			boolean valid = true;
			int option;
			cc.displayCustomerDetails();
			do 
			{
				System.out.println("\t Update customer details ");
				System.out.println("\t-------------------------");
				System.out.println(" 1. Customer Name");
				System.out.println(" 2. Customer Gender");
				System.out.println(" 3. Customer Birthday Date");
				System.out.println(" 4. Customer Tel.No (+60)");
				System.out.println(" 5. Customer email address");
				System.out.println(" 6. Exit update");
				System.out.println("\t-------------------------");
				do 
				{
					try 
					{
						System.out.print(" Please enter your option : ");
						option = input.nextInt();
						switch (option)
						{
						case 1:
						{
							input.nextLine();
							System.out.println(" Update Customer Name");
							System.out.println(" Original Customer Name : "+cc.getName());
							//request customer name from user with validation
							boolean validName = false;
							String name;
							do 
							{
								System.out.print(" New Customer Name      : ");
								name = input.nextLine();
								validName = isValidName(name); //customer name validation
							} while (validName == false);

							cc.setName(name);
							break;
						}
						case 2:
						{
							System.out.println(" Update Customer Gender");
							System.out.println(" Original Customer Gender : "+cc.getGender());
							boolean validGender = false;
							char gender;
							do 
							{
								System.out.print(" (M-Male\tF-Female\tO-Others/Unspecified)\n New Customer Gender      : ");
								gender = input.next().toUpperCase().charAt(0);
								validGender = isValidGender(gender); //gender validation
							} while (validGender == false);
							input.nextLine();
							cc.setGender(gender);
							break;
						}
						case 3:
						{
							input.nextLine();
							System.out.println(" Update Customer Birthday Date");
							System.out.println(" Original Customer Birthday Date\t : "+cc.getDateOfBirth());
							boolean validDate = false;
							String date = null;
							do 
							{	
								System.out.print(" New Customer Birthday Date (dd/mm/yyyy) : ");
								date = input.nextLine();
								validDate = isValidDate(date); //date validation
							}while(validDate == false);
							cc.setDateOfBirth(date);
							break;
						} 
						case 4:
						{
							input.nextLine();
							System.out.println(" Update Customer  Tel.No (+60)");
							System.out.println(" Original Customer  Tel.No (+60) : "+cc.getPhNo());
							boolean validTelNo = false;
							int telNo = 0;
							do 
							{
								System.out.print(" New Customer Tel.No (+60)       : ");
								try 
								{
									telNo = Integer.parseInt(input.nextLine());
									validTelNo = isValidTeleNo(telNo); //telephone no validation
								}
								catch (Exception e) 
								{
									System.out.println(" Please Only Enter Digits");
								}
							} while (validTelNo == false);
							cc.setPhNo(telNo);
							break;
						} 
						case 5:
						{
							input.nextLine();
							System.out.println(" Update Customer email address");
							System.out.println(" Original Customer email address : "+cc.getEmail());
							boolean validEmail = false;
							String email;
							do 
							{
								System.out.print(" New Customer email address      : ");
								email = input.nextLine();
								validEmail = isValidEmail(email); //email validation
							} while (validEmail == false);
							cc.setEmail(email);
							break;
						}
						case 6:
						{
							System.out.println(" Exit updating");
							again = false;
							break;
						}
						default:
						{
							System.out.println(" Wrong option");
							break;
						}
						}
						valid = true;
					}
					catch(InputMismatchException exp)
					{
						System.out.println(" Please enter correct option.");
						input.nextLine();
						valid = false;
					}
				}while(valid == false);
			}while(again == true);

			customer.set(no,cc);
		}
		//Place rental order
		public static void placeBooking(int i)
		{
			System.out.println("------------------------------------------------------------------");
			System.out.println("\t   ~ WELCOME TO SALON BOOKING SYSTEM ~");
			System.out.println("------------------------------------------------------------------");
			System.out.println("\t\t  | PLACE BOOKING ORDER |\t\t");
			System.out.println("------------------------------------------------------------------");
			

			boolean validBookingTime = false;
			int bookingTime = 0;
			do 
			{
				System.out.print(" Enter booking timeslot : ");
				try 
				{
					bookingTime = Integer.parseInt(input.nextLine());
					validBookingTime = true;
				} 
				catch (Exception e) 
				{
					System.out.println(" Invalid Input. Please Enter a Whole Number.");
				}
			} while (validBookingTime == false);

			boolean validBookingDate = false;
			String bookingDate =  null;
			do 
			{
				System.out.print(" Enter Booking Date : ");
				bookingDate = input.nextLine();
				validBookingDate  = isValidDate(bookingDate); //date validation
			}while(validBookingDate  == false);

			
			System.out.println("==================================================================");
			Booking newBooking = new Booking(bookingDate, bookingDate, bookingTime, bookingDate, bookingDate,bookingDate, admin.get(i),customer.get(i), bookingTime, bookingTime);
			booking.add(newBooking);

			String hairServiceID;
			String hairStylistID;
			boolean found = false;
			int add = 1;
			do 
			{
				System.out.print("\t\t   ~ Choose Your Hair Service ~\n");
				HairService hs = null;
				System.out.println("------------------------------------------------------------------");
				System.out.println("\t\t    | Hair Service LIST |\t\tTotal: " + hairService.size());
				System.out.println("------------------------------------------------------------------");
				System.out.println("  No.  |\tHair Service ID\t|\tHair Service Name\t\t|");
				System.out.println("------------------------------------------------------------------");

				for(int t = 0; t < hairService.size();t++)
				{
					System.out.printf("  %-2d.\t\t%-6s\t\t\t%-15s\n", (t+1), hairService.get(t).getHairServiceID(), hairService.get(t).getHairServiceName());
				}
				System.out.println("==================================================================\n");
				System.out.print(" Enter Hair Service ID: ");
				do
				{
					found = false;
					int j = 0;
					int count = hairService.size();
					hairServiceID = input.nextLine();
					while(j < count && !found)
					{
						hs = hairService.get(j);
						if(hs.getHairServiceID().equals(hairServiceID)) //match hair service ID entered with data set
						{
							found = true;
							
							
							System.out.print("\t\t   ~ Choose Your Hair Stylist ~\n");
							HairStylist h = null;
							System.out.println("------------------------------------------------------------------");
							System.out.println("\t\t    | Hair Stylist LIST |\t\tTotal: " + hairStylist.size());
							System.out.println("------------------------------------------------------------------");
							System.out.println("  No.  |\tHair Stylist ID\t|\tHair Stylist Name\t\t|");
							System.out.println("------------------------------------------------------------------");
							for(int t = 0; t < hairStylist.size();t++)
							{
								System.out.printf("  %-2d.\t\t%-6s\t\t\t%-15s\n", (t+1), hairStylist.get(t).getHairStylistID(), hairStylist.get(t).getHairStylistName());
							}
							System.out.println("==================================================================\n");
							System.out.print(" Enter Hair Stylist ID: ");
							hairStylistID = input.nextLine();
							System.out.println();
							
						}
						else
							j++;
					}
					if(found != true)
					{
						System.out.println(" Hair Service ID is not in the list . Please enter again ");
						System.out.print(" Enter Hair Service ID:");
					}
				}while(found != true);

				newBooking.addHairService(hs);

				do
				{boolean validOpt = false;
				do
				{
					try
					{
						System.out.print(" [ MAXIMUM 5 hair service can be added ]\n");
						System.out.print(" Do you want to add hair service again ?( 1 = Yes  0 = No)\n");
						System.out.print(" Option: ");
						add = input.nextInt();
						validOpt = true;
					}
					catch (Exception e)
					{
						System.out.println(" Invalid Input. Please only enter 1 or 0.");
						input.nextLine();
					}
				}while (validOpt == false);
				if (add != 1 && add != 0)
				{
					System.out.println(" Wrong Input.Please only choose 1 or 0.");
					input.nextLine();
				}
				}while (add != 1 && add!=0);
				input.nextLine();

			}while(add == 1); //terminate when user choose 0

			newBooking.displayBookingOrderDetails();;
			newBooking.generateBookingReceipt(false);
		}
		//display only customer rental list
		public static void displayCustomerBookingHistory(int no)
		{
			ArrayList <Booking> bb = new ArrayList <Booking> ();
			int i = 0;
			Booking rent = null;
			while( i < booking.size())
			{
				rent = booking.get(i); //get the first customer for matching
				if(rent.getCustomer().getId().equals(customer.get(no).getId()) ) //find whether the input match with database or not
				{
					bb.add(rent);
				}
				i++; //repeat until match
			}
			System.out.println("------------------------------------------------------------------");
			System.out.println("\t   ~ WELCOME TO SALON BOOKING SYSTEM ~");
			System.out.println("------------------------------------------------------------------");
			System.out.println("\t\t   | CHECK BOOKING HISTORY |\t\t");
			System.out.println("------------------------------------------------------------------");
			System.out.println("  No.  |\tBooking ID\t|  Date order  |");
			for(int z = 0; z < bb.size(); z++)
			{
				System.out.printf("  %2d.\t\t%4d\t\t   %10s\n", (z+1), bb.get(z).getBookingID(), bb.get(z).getBookingDate());
			}
			System.out.println("==================================================================");
		}

		//save customer data to file
		public static void saveToCustomerFile()
		{
			try 
			{ //try first
				File customerFile = new File("Customer.txt");
				FileWriter fileWriter = new FileWriter(customerFile,false);
				PrintWriter printWriter = new PrintWriter(fileWriter);
				for(int i = 0; i < customer.size(); i++)
				{
					printWriter.println(customer.get(i).getId()+"#"+customer.get(i).getName()+"#"+customer.get(i).getPassword()+"#"+customer.get(i).getGender()+"#"+customer.get(i).getDateOfBirth()+"#"+customer.get(i).getPhNo()+"#"+customer.get(i).getEmail()+"#"+customer.get(i).getAddress());
				}
				printWriter.close();		
			}
			catch (IOException e)
			{
				System.out.println(" Error");
			}
		}

		//write in all hair service data into the file
		public static void saveBooking() 
		{
			try
			{
				FileOutputStream fileOut = new  FileOutputStream("HairService.ser");
				ObjectOutputStream oos = new ObjectOutputStream(fileOut);
				oos.writeObject(booking);
				oos.close();
				fileOut.close();
				System.out.printf(" Serialized data for hair service is saved.\n");
			} 
			catch (IOException i) 
			{
				i.printStackTrace();
			}
		}

		//write in all rental details into the file
		public static void saveToBookingFile()
		{
			try 
			{ //try first
				File bookingFile = new File("Booking.txt");
				FileWriter fileWriter = new FileWriter(bookingFile,false);
				PrintWriter printWriter = new PrintWriter(fileWriter);
				for(int i = 0;i < booking.size();i++)
				{
					HairService [] hairServiceList = new HairService[booking.get(i).getNoOfHairService()];
					hairServiceList = booking.get(i).getHairServiceBookingList();
					String line = "";
					for(int j = 0; j < booking.get(i).getNoOfHairService(); j++)
					{
						if(j != booking.get(i).getNoOfHairService()-1)
							line += hairServiceList[j].getHairServiceID()+ "#";
						else
							line += hairServiceList[j].getHairServiceID();
					}
					printWriter.println(booking.get(i).getPlaceBookingDate()+'#'+ booking.get(i).getBookingID()+'#'+ booking.get(i).getBookingTime()
							+'#'+booking.get(i).getBookingDate()+'#'+booking.get(i).getCustomer().getId()+'#'+booking.get(i).getAdmin().getId()
							+'#'+booking.get(i).getStatus()+'#'+booking.get(i).getNoOfHairService()+'#'+booking.get(i).getNoOfHairStylist()+'#'+booking.get(i).getHairStylist().getHairStylistID()
							+'#'+booking.get(i).getHairStylist().getHairStylistName()+line);
				}
				printWriter.close();
				System.out.println(" Booking data saved.");
			}
			catch (IOException e)
			{
				System.out.println(" Error!");
			}
		}

		//validation for date
		@SuppressWarnings("finally")
		public static boolean  isValidDate(String d)
		{
			boolean validDate = true;
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu",Locale.CHINA).withResolverStyle(ResolverStyle.STRICT);
			try
			{
				formatter.parse(d);
			}
			catch (Exception e)
			{
				validDate = false;
				System.out.println(" Invalid Date. Please enter again.");
			}
			finally
			{
				return validDate;
			}
		}

		//customer ID validation
		public static boolean isValidID(String id) 
		{
			boolean validID;
			if (id.matches("[C]\\d{4}")) 
			{
				boolean conflict = false;
				int i = 0;
				while (conflict == false && i < customer.size()) 
				{
					if (id.equals(customer.get(i).getId()))
						conflict = true;
					else 
					{
						conflict = false;
						i++;
					}
				}
				if (conflict == false) 
				{
					validID = true;
				} 
				else 
				{
					validID = false;
					System.out.println(" ID is already in use. Try a different one.");
				}
			} 
			else 
			{
				validID = false;
				System.out.println(" Invalid ID. Enter again.");
			}
			return validID;
		}

		//customer name validation
		public static boolean isValidName(String name) 
		{
			boolean validName;
			if (name.matches("[a-zA-Z\\s\\.]+")) 
			{
				validName = true;
			} 
			else 
			{
				validName = false;
				System.out.println(" Please write in lowercase or uppercase letters only. Enter again.");
			}
			return validName;
		}

		//password validation
		public static boolean isValidPassword(String password) 
		{
			boolean validPassword;
			if (password.matches("\\S+")) 
			{
				if (password.length() >= 6 && password.length() <= 22)
					validPassword = true;
				else 
				{
					validPassword = false;
					System.out.println(" Password Invalid");
				}
			} 
			else 
			{
				validPassword = false;
				System.out.println(" No Spaces Are Allowed In The Password");
			}
			return validPassword;
		}

		//gender validation
		public static boolean isValidGender(char gender) 
		{
			boolean validGender;
			if (Character.toString(gender).matches("[MFO]")) 
			{
				validGender = true;
			} 
			else 
			{
				validGender = false;
				System.out.println(" Invalid Input");
			}
			return validGender;
		}

		//TelNo validation
		@SuppressWarnings("finally")
		public static boolean isValidTeleNo(int telNo) 
		{
			boolean validTeleNo;
			if (telNo >= 000000000 && telNo <= 1999999999) 
			{
				validTeleNo = true;
			} 
			else 
			{
				validTeleNo = false;
				System.out.println(" Invalid Telephone Number. Please Enter Again.");
			}
			return validTeleNo;
		}

		//email validation
		public static boolean isValidEmail(String email) 
		{
			boolean validEmail;
			if (email.matches("[a-zA-Z0-9\\.\\_]+\\@[a-z\\.]+")) 
			{
				validEmail = true;
			} 
			else 
			{
				validEmail = false;
				System.out.println(" Invalid E-mail. Please Enter Again.");
			}
			return validEmail;
		}

		//address validation
		public static boolean isValidAddress(String address) 
		{
			boolean validAddress;
			if (address.matches("[\\w\\s\\,\\.\\/\\-]+")) 
			{
				validAddress = true;
			} 
			else 
			{
				validAddress = false;
				System.out.println(" Invalid Address. Please Enter Again.");
			}
			return validAddress;
		}

		//HairService ID validation
		public static boolean isValidHairServiceID (String hairServiceID) 
		{
			boolean validHairServiceID;
			if (hairServiceID.matches("\\d{4}"))
			{
				boolean crash = false;
				int i = 0;
				while (crash == false && i < hairService.size()) 
				{
					if (hairServiceID.equals(hairService.get(i).getHairServiceID()))
						crash = true;
					else 
					{
						crash = false;
						i++;
					}
				}
				if (crash == false) 
				{
					validHairServiceID = true;
				} 
				else 
				{
					validHairServiceID = false;
					System.out.println(" ID is already in use. Try a different ID.");
				}
			}
			else
			{
				validHairServiceID = false;
				System.out.println(" Invalid ID Format. Please Key In Again");		
			}
			return validHairServiceID;
		}

		//Hair Service name validation
		public static boolean isValidHairServiceName (String hairServiceName) 
		{
			boolean validHairServiceName;
			if (hairServiceName.matches("[a-zA-Z]+")) 
			{
				validHairServiceName = true;
			} 
			else 
			{
				validHairServiceName = false;
				System.out.println(" Only Upper and Lower case Alphabets are allowed. Please Try Again ");
			}
			return validHairServiceName;
		}

		private static boolean isValidHairStylistName(String hairStylistName) {
			
			boolean validHairStylistName;
			if (hairStylistName.matches("[a-zA-Z]+")) 
			{
				validHairStylistName = true;
			} 
			else 
			{
				validHairStylistName = false;
				System.out.println(" Only Upper and Lower case Alphabets are allowed. Please Try Again ");
			}
			return validHairStylistName;
		}
		

		//hair service type validation
		public static boolean isValidType (String hairServiceType)
		{
			boolean validHairServiceType;
			if (hairServiceType.matches("[a-zA-z\\s]+"))
			{
				validHairServiceType = true;
			}
			else
			{
				validHairServiceType = false;
				System.out.println(" Please use only upper and lower case Alphabets");
			}
			return validHairServiceType;
		}

		//hair Service description validation
		public static boolean isValidDesc (String hairServiceDesc)
		{ 
			boolean validDesc;
			if (hairServiceDesc.matches("[a-zA-z\\s\\,?.]+"))
			{
				validDesc = true;
			}
			else
			{
				validDesc=false;
				System.out.println(" Invalid input, please enter in words.");
			}
			return validDesc;
		}
	}





