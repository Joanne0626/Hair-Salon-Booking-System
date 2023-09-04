
public class User {
	
	// Instance Variable for User
		private String name;
		private String id;
		private String password;
		
		// Constructor
		public User(String name, String id, String password)
		{
			this.name = name;
			this.id = id;
			this.password=password;
		}
		
		// Getter
		public String getName() 
		{
			return name;
		}
		
		// Setter
		public void setName(String name) 
		{
			this.name = name;
		}
		
		// Getter
		public String getId() 
		{
			return id;
		}
		
		// Setter- to verify the validation of Id
		public void setId(String id) 
		{
			boolean validId = false;
			// only Admin or Customer can access
			if(id.length() == 5)
			{
				char idCase = id.charAt(0);
				if(idCase == 'A' || idCase == 'C')
				{
					String idDigit = id.substring(2);
					boolean allDigit = idDigit.matches("[0-9]+");
					if (allDigit == true)
					{
						validId = true;
						this.id = id;
					}		
				}
			}
			if(validId == false)
			{
				this.id = "INVALID USER ID";
			}
		}
		
		// Getter
		public String getPassword() 
		{
			return password;
		}

}
