package ru.smolyakovroma.reference5;


import android.app.Application;

import ru.smolyakovroma.reference5.database.DbAdapter;


public class AppContext extends Application {
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		dbAdapter = new DbAdapter(this);
	}
	

	private static DbAdapter dbAdapter;
	
	
	public static DbAdapter getDbAdapter() {

		return dbAdapter;
	}
	
	

}
