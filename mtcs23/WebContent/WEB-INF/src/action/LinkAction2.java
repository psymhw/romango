package com.romango.action;

import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.romango.db.Db;

public class LinkAction2 extends ActionSupport {

	private static final long serialVersionUID = -2613425890762568273L;

	public String welcome()
	{
		return "welcome";		
	}
	
	public String friends()
	{
	  System.out.println("a change to test");
  
    
		return "friends";		
		
	}
	
	public String office()
	{
		return "office";		
	}
}
