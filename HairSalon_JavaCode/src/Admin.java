//subclass of User - Admin

public class Admin extends User{
	
	private String jobTitle;
	
	public Admin(String adminName, String adminId, String adminPassword, String adminJobTitle) {
		super(adminName, adminId, adminPassword);
		this.jobTitle = adminJobTitle;
		
	}

	public String getJobTitle() 
	{
		return jobTitle;
	}

	
	

}
