package ru.smolyakovroma.reference5;


import android.app.Application;

import ru.smolyakovroma.reference5.database.DbAdapter;


public class AppContext extends Application {

	public static final int SPR_SELECT_GROUP_REQUEST = 100;
	public static String SPR_CURRENT_PARENT_FOLDER_ID = "ru.smolyakovroma.reference5.current_parent_folder_id";

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
