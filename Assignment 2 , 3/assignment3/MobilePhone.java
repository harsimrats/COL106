public class MobilePhone
{
	private int id;
	private boolean status = false;
	Exchange base;
	MobilePhone(int number)
	{
		id = number;
		status = false;
	}
	
	public int number()
	{
		return id;
	}
	
	public boolean Status()
	{
		return status;
	}
	
	public void switchon()
	{
		status = true;
	}
	
	public void switchoff()
	{
		status = false;
	}
	
	public Exchange location() throws CustomException
	{
		if(status == true)
		{
			return base;
		}
		else
		{
			throw new CustomException("Mobile Phone is switched OFF");
		}
	}
}