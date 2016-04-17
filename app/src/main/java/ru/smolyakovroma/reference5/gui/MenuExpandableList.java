package ru.smolyakovroma.reference5.gui;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ru.smolyakovroma.reference5.DocActivity;
import ru.smolyakovroma.reference5.adapter.ExpandableListAdapter;
import ru.smolyakovroma.reference5.R;
import ru.smolyakovroma.reference5.SprActivity;


public class MenuExpandableList {

    public static final String OPERATION_TYPE = "ru.javabegin.training.android.money.gui.MenuExpandableList.operationType";

    private Activity context;

    private DrawerLayout navDrawer;

    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;

    private List<String> listGroup;// родительские пункты
    private HashMap<String, List<String>> mapChild; // дочерние пункты

    private static final int OPERATIONS_ALL = 0;

    public MenuExpandableList(final Activity context) {
	this.context = context;
	expListView = (ExpandableListView) context.findViewById(R.id.expLvMenu);
	navDrawer = (DrawerLayout) context.findViewById(R.id.drawer_layout);

	fillMenu();

	listAdapter = new ExpandableListAdapter(context, listGroup, mapChild);

	expListView.setAdapter(listAdapter);

	expListView.setOnGroupClickListener(new OnGroupClickListener() {
	    @Override
	    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
		return false;
	    }
	});

	expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
	    @Override
	    public void onGroupExpand(int groupPosition) {
	    }
	});

	expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
	    @Override
	    public void onGroupCollapse(int groupPosition) {
	    }
	});

	expListView.setOnChildClickListener(new OnChildClickListener() {

	    @Override
	    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

		switch (groupPosition) {
		case 0: {// документы
			if (childPosition == 0){
				Intent intent = new Intent(context, DocActivity.class);
				context.startActivity(intent);
			}
		    break;
		}

		case 1: { // справочники
			if (childPosition == 0){
				Intent intent = new Intent(context, SprActivity.class);
				context.startActivity(intent);
			}
				break;
		}

		case 2: {// настройки

		    if (childPosition == 0) {// обмены

				//тут запустить активити

		    }

		    if (childPosition == 2) {// пользователь



		    }

		    break;
		}

		default:
		    break;
		}

		navDrawer.closeDrawer(Gravity.LEFT);
		return true;
	    }

	});

//		тут можно показать рабочий стол
    }



    private void fillMenu() {
	listGroup = new ArrayList<String>();
	mapChild = new HashMap<String, List<String>>();

	listGroup.add(context.getResources().getString(R.string.menu_documents));

	listGroup.add(context.getResources().getString(R.string.menu_references));

	listGroup.add(context.getResources().getString(R.string.menu_setting));

	List<String> menu1 = new ArrayList<String>();

	for (String child : context.getResources().getStringArray(R.array.child_menu_documents)) {
	    menu1.add(child);
	}

	List<String> menu2 = new ArrayList<String>();
	for (String child : context.getResources().getStringArray(R.array.child_menu_references)) {
	    menu2.add(child);
	}

	List<String> menu3 = new ArrayList<String>();
	for (String child : context.getResources().getStringArray(R.array.child_menu_settings)) {
	    menu3.add(child);
	}

	mapChild.put(listGroup.get(0), menu1);
	mapChild.put(listGroup.get(1), menu2);
	mapChild.put(listGroup.get(2), menu3);

    }

}
