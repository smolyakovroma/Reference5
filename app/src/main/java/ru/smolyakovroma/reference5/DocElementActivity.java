package ru.smolyakovroma.reference5;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import ru.smolyakovroma.reference5.model.DocElement;

public class DocElementActivity extends AppCompatActivity {

    public static final int RESULT_SAVE = 100;
    public static final int RESULT_DELETE = 101;

    private static final int NAME_LENGTH = 20;

    EditText etCode;
    EditText etName;


    private DocElement docElement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_element);


        etCode = (EditText) findViewById(R.id.et_code);
        etName = (EditText) findViewById(R.id.et_name);


        docElement = (DocElement) getIntent().getSerializableExtra(
                DocActivity.DOC_ELEMENT);

        if (docElement.getId() == null) {

            setTitle(getString(R.string.add_document_title));
        } else {

            setTitle(getString(R.string.edit_document_title));
        }
        etCode.setText(Long.toString(docElement.getDoc_datetime()));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.doc_element_menu, menu);
        return true;
    }

    private void saveElement() {

        docElement.setDoc_datetime(Long.parseLong(etCode.getText().toString()));
//        sprElement.setFolder(false);
        AppContext.getDbAdapter().updateDocElement(docElement);
        setResult(RESULT_SAVE, getIntent());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                saveElement();
                finish();
                return true;
            }
            case R.id.save: {
                saveElement();
                finish();
                return true;
            }

            case R.id.delete: {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(docElement.getStatus() == 2 ? R.string.confirm_undelete : R.string.confirm_delete);

                builder.setPositiveButton(R.string.confirm,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (docElement.getStatus() == 2) {
                                    docElement.setStatus(0);
                                } else {
                                    docElement.setStatus(2);
                                }

                            }
                        });
                builder.setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            }

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    class SprOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }
}
