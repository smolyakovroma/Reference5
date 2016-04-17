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

public class SprActivity extends AppCompatActivity {

    public static String SPR_ELEMENT = "ru.smolyakovroma.reference5.model.SprElement";
    public static int SPR_ELEMENT_REQUEST = 1;

    private SprAdapter arrayAdapter;
    private static ArrayList<SprElement> listSprElement = new ArrayList<>();
    private ListView listView;

    int current_folder_id = 0;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spr);

        listSprElement = AppContext.getDbAdapter().getSprElements(0, SprTypeSelect.ALL);

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new ListViewClickListener());
        listView.setOnItemLongClickListener(new ListViewLongClickListener());
        listView.setEmptyView(findViewById(R.id.emptyView));

        fillSprElement();

    }

    private void fillSprElement() {
        arrayAdapter = new SprAdapter(this, listSprElement, false);
        listView.setAdapter(arrayAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.spr_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_element: {
                SprElement sprElement = new SprElement();
                sprElement.setParent_id(current_folder_id);
                sprElement.setFolder(false);
//                TODO установим новый код
//                sprElement.setName(getResources()
//                        .getString(R.string.new_document));
                showSpr(sprElement);
                return true;
            }
            case R.id.menu_add_folder: {
            SprElement sprElement = new SprElement();
                sprElement.setParent_id(current_folder_id);
                sprElement.setFolder(true);
//                TODO установим новый код
//                sprElement.setName(getResources()
//                        .getString(R.string.new_document));
            showSpr(sprElement);
            return true;
        }
            case android.R.id.home: {
                if (current_folder_id != 0) {
                    int parent_folder_id = AppContext.getDbAdapter().getParentFolder(current_folder_id);
                    listSprElement = AppContext.getDbAdapter().getSprElements(parent_folder_id, SprTypeSelect.ALL);
                    arrayAdapter.clear();
                    arrayAdapter.addAll(listSprElement);
                    arrayAdapter.notifyDataSetChanged();
                    current_folder_id = parent_folder_id;
                }
                return true;
            }
            case R.id.pick: {
                Intent intentSprElement = new Intent(this, SprActivityPicker.class);
                startActivityForResult(intentSprElement, SPR_ELEMENT_REQUEST);
                return true;
            }

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSpr(SprElement sprElement) {
        Intent intentSprElement = new Intent(this, SprElementActivity.class);
        intentSprElement.putExtra(SPR_ELEMENT, sprElement);
        startActivityForResult(intentSprElement, SPR_ELEMENT_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SPR_ELEMENT_REQUEST) {
            SprElement sprElement = null;
            switch (resultCode) {
                case RESULT_CANCELED:
                    break;
                case SprElementActivity.RESULT_SAVE:
                    sprElement = (SprElement) data.getSerializableExtra(SPR_ELEMENT);
                    current_folder_id = sprElement.getParent_id();
                    listSprElement = AppContext.getDbAdapter().getSprElements(current_folder_id, SprTypeSelect.ALL);
                    arrayAdapter.clear();
                    arrayAdapter.addAll(listSprElement);
                    arrayAdapter.notifyDataSetChanged();
                    break;
                case SprElementActivity.RESULT_DELETE:
//                    sprElement = (SprElement) data.getSerializableExtra(SPR_ELEMENT);
//                    deleteElement(sprElement);
                    break;
                default:
                    break;
            }
        }
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
        }
    }

    class ListViewLongClickListener implements AdapterView.OnItemLongClickListener{

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            SprElement sprElement = (SprElement) parent.getAdapter().getItem(position);
            showSpr(sprElement);
            return false;
        }
    }
}
