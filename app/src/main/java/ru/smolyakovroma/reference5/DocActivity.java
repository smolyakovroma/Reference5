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

import ru.smolyakovroma.reference5.adapter.DocAdapter;
import ru.smolyakovroma.reference5.enums.DocumentType;
import ru.smolyakovroma.reference5.model.DocElement;

public class DocActivity extends AppCompatActivity {
    public static String DOC_ELEMENT = "ru.smolyakovroma.reference5.model.DocElement";
    public static int DOC_ELEMENT_REQUEST = 1;

    private DocAdapter arrayAdapter;
    private static ArrayList<DocElement> listDocElement = new ArrayList<>();
    private ListView listView;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc);

        listDocElement = AppContext.getDbAdapter().getDocElements(DocumentType.DOC_DISPLACEMENT_STOCKS);

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new ListViewClickListener());
        listView.setOnItemLongClickListener(new ListViewLongClickListener());
        listView.setEmptyView(findViewById(R.id.emptyView));

        fillDocElement();

    }

    private void fillDocElement() {
        arrayAdapter = new DocAdapter(this, listDocElement);
        listView.setAdapter(arrayAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.doc_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_element: {
                DocElement docElement = new DocElement();
//                docElement.setFolder(false);
//                TODO установим новый код
//                sprElement.setName(getResources()
//                        .getString(R.string.new_document));
                showDoc(docElement);
                return true;
            }

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDoc(DocElement docElement) {
        Intent intentDocElement = new Intent(this, DocElementActivity.class);
        intentDocElement.putExtra(DOC_ELEMENT, docElement);
        startActivityForResult(intentDocElement, DOC_ELEMENT_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DOC_ELEMENT_REQUEST) {
            DocElement docElement = null;
            switch (resultCode) {
                case RESULT_CANCELED:
                    break;
                case DocElementActivity.RESULT_SAVE:
                    docElement = (DocElement) data.getSerializableExtra(DOC_ELEMENT);
                    listDocElement = AppContext.getDbAdapter().getDocElements(DocumentType.DOC_DISPLACEMENT_STOCKS);
                    arrayAdapter.clear();
                    arrayAdapter.addAll(listDocElement);
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
            DocElement docElement = (DocElement) parent.getAdapter().getItem(position);
            showDoc(docElement);
        }
    }

    class ListViewLongClickListener implements AdapterView.OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            DocElement docElement = (DocElement) parent.getAdapter().getItem(position);
            showDoc(docElement);
            return false;
        }
    }
}
