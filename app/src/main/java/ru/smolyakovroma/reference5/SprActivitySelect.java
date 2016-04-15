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

import ru.smolyakovroma.reference5.model.SprElement;

public class SprActivitySelect extends AppCompatActivity {

    public static String SPR_ELEMENT = "ru.smolyakovroma.reference5.model.SprElement";
    public static int SPR_ELEMENT_REQUEST = 1;

    private SprElementAdapter arrayAdapter;
    private static ArrayList<SprElement> listSprElement = new ArrayList<>();
    private ListView listView;

    int current_folder_id = 0;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spr);

        listSprElement = AppContext.getDbAdapter().getSprElements(0);

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new ListViewClickListener());
        listView.setOnItemLongClickListener(new ListViewLongClickListener());
        listView.setEmptyView(findViewById(R.id.emptyView));

        fillSprElement();

    }

    private void fillSprElement() {
        arrayAdapter = new SprElementAdapter(this, listSprElement);
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
//                TODO установим новый код
//                sprElement.setName(getResources()
//                        .getString(R.string.new_document));
                showElement(sprElement);
                return true;
            }
            case R.id.menu_add_folder: {
            SprElement sprElement = new SprElement();
                sprElement.setParent_id(current_folder_id);
//                TODO установим новый код
//                sprElement.setName(getResources()
//                        .getString(R.string.new_document));
            showFolder(sprElement);
            return true;
        }
            case android.R.id.home: {
                if (current_folder_id != 0) {
                    int parent_folder_id = AppContext.getDbAdapter().getParentFolder(current_folder_id);
                    listSprElement = AppContext.getDbAdapter().getSprElements(parent_folder_id);
                    arrayAdapter.clear();
                    arrayAdapter.addAll(listSprElement);
                    arrayAdapter.notifyDataSetChanged();
                    current_folder_id = parent_folder_id;
                }
                return true;
            }

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showElement(SprElement sprElement) {
        Intent intentSprElement = new Intent(this, SprElementActivity.class);
        intentSprElement.putExtra(SPR_ELEMENT, sprElement);
        startActivityForResult(intentSprElement, SPR_ELEMENT_REQUEST);
    }

    private void showFolder(SprElement sprElement) {
        Intent intentSprElement = new Intent(this, SprFolderActivity.class);
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
//                    sprElement = (SprElement) data.getSerializableExtra(SPR_ELEMENT);
//                    addElement(sprElement);
                    listSprElement = AppContext.getDbAdapter().getSprElements(current_folder_id);
                    arrayAdapter.clear();
                    arrayAdapter.addAll(listSprElement);
                    arrayAdapter.notifyDataSetChanged();

                    break;
                case SprElementActivity.RESULT_DELETE:
                    sprElement = (SprElement) data.getSerializableExtra(SPR_ELEMENT);
                    deleteElement(sprElement);
                    break;
                default:
                    break;
            }
        }
    }

    @SuppressLint("NewApi")
    private void deleteElement(SprElement sprElement) {
        listSprElement.remove(sprElement.getId().intValue());
        arrayAdapter.notifyDataSetChanged();
    }

    @SuppressLint("NewApi")
    private void addElement(SprElement sprElement) {
        if (sprElement.getId() == null) {
            listSprElement.add(sprElement);
        } else {
//            listSprElement.set()
        }
        arrayAdapter.notifyDataSetChanged();
    }

    class ListViewClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            SprElement sprElement = (SprElement) parent.getAdapter().getItem(position);
            if (sprElement.isFolder() && sprElement.isTopFolder()) {
                int parent_folder_id = AppContext.getDbAdapter().getParentFolder(current_folder_id);
                listSprElement = AppContext.getDbAdapter().getSprElements(parent_folder_id);
                arrayAdapter.clear();
                arrayAdapter.addAll(listSprElement);
                arrayAdapter.notifyDataSetChanged();
                current_folder_id = parent_folder_id;
            }

//            showElement(sprElement);
            else if (sprElement.isFolder()) {
                current_folder_id = sprElement.getId();
                listSprElement = AppContext.getDbAdapter().getSprElements(current_folder_id);
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
            if(sprElement.isFolder()){
                    showFolder(sprElement);
                return true;
            }
            else if (!sprElement.isFolder()){
                    showElement(sprElement);
                return true;
            }
            return false;
        }
    }
}
