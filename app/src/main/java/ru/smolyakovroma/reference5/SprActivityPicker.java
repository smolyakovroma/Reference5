package ru.smolyakovroma.reference5;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import ru.smolyakovroma.reference5.adapter.SprAdapter;
import ru.smolyakovroma.reference5.model.SprElement;

public class SprActivityPicker extends AppCompatActivity {

    public static String SPR_SELECT_GROUP_ID = "ru.smolyakovroma.reference5.select_group";

    private SprAdapter arrayAdapter;
    private SprAdapter arrayAdapterPicker;
    private static ArrayList<SprElement> listSprElement = new ArrayList<>();
    private static ArrayList<SprElement> listSprElementPick = new ArrayList<>();
    private ListView listView;
    private ListView listViewPick;

    int current_folder_id = 0;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spr_picker);

//        current_folder_id = getIntent().getExtras().getInt(AppContext.SPR_CURRENT_PARENT_FOLDER_ID);
        listSprElement = AppContext.getDbAdapter().getSprElements(current_folder_id, SprTypeSelect.ALL);

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new ListViewClickListener());
        listView.setOnItemLongClickListener(new ListViewLongClickListener());
        listView.setEmptyView(findViewById(R.id.emptyView));

        listViewPick = (ListView) findViewById(R.id.listViewPick);
        listViewPick.setEmptyView(findViewById(R.id.emptyViewPick));

        fillSprElement();

    }

    private void fillSprElement() {
        arrayAdapter = new SprAdapter(this, listSprElement, false);
        listView.setAdapter(arrayAdapter);

        arrayAdapterPicker = new SprAdapter(this, listSprElementPick, true);
        listViewPick.setAdapter(arrayAdapterPicker);
    }

    @Override
    protected void onRestart() {
        super.onRestart();


    }

    @Override
    protected void onResume() {
        super.onResume();
        listSprElementPick.clear();
        arrayAdapterPicker.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.spr_select_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cancel: {
                finish();
                return true;
            }


            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }



    class ListViewClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            SprElement sprElement = (SprElement) parent.getAdapter().getItem(position);
            if (sprElement.isFolder() && sprElement.isTopFolder()) {
                int parent_folder_id = AppContext.getDbAdapter().getParentFolder(current_folder_id);
                listSprElement = AppContext.getDbAdapter().getSprElements(parent_folder_id, SprTypeSelect.ALL);
                arrayAdapter.clear();
                arrayAdapter.addAll(listSprElement);
                arrayAdapter.notifyDataSetChanged();
                current_folder_id = parent_folder_id;
            }

            else if (sprElement.isFolder()) {
                current_folder_id = sprElement.getId();
                listSprElement = AppContext.getDbAdapter().getSprElements(current_folder_id, SprTypeSelect.ALL);
                arrayAdapter.clear();
                arrayAdapter.addAll(listSprElement);
                arrayAdapter.notifyDataSetChanged();
            }

            else if(!sprElement.isFolder()){
                if(listSprElementPick.indexOf(sprElement) == -1){
                    sprElement.incrAmount();
                    sprElement.setUnit("test");
                    listSprElementPick.add(sprElement);

                    arrayAdapterPicker.notifyDataSetChanged();
                } else {
                    sprElement.incrAmount();
                    arrayAdapterPicker.notifyDataSetChanged();
                }
            }

        }
    }

    class ListViewLongClickListener implements AdapterView.OnItemLongClickListener{

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            SprElement sprElement = (SprElement) parent.getAdapter().getItem(position);
            Intent intent = new Intent();
            intent.putExtra(SPR_SELECT_GROUP_ID, Integer.toString(sprElement.getId()));
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }
    }
}
