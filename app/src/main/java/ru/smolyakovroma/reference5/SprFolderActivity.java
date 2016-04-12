package ru.smolyakovroma.reference5;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import ru.smolyakovroma.reference5.model.SprElement;

public class SprFolderActivity extends AppCompatActivity {

    public static final int RESULT_SAVE = 100;
    public static final int RESULT_DELETE = 101;

    private static final int NAME_LENGTH = 20;

    EditText etCode;
    EditText etName;
    EditText etGroup;

    private SprElement sprElement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spr_folder);

        etCode = (EditText) findViewById(R.id.et_code);
        etName = (EditText) findViewById(R.id.et_name);
        etGroup = (EditText) findViewById(R.id.et_group);

        sprElement = (SprElement) getIntent().getSerializableExtra(
                SprActivity.SPR_ELEMENT);

        etCode.setText(sprElement.getCode());
        etName.setText(sprElement.getName());
//        etGroup.setText(Integer.toString(sprElement.getParent_id()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.spr_folder_menu, menu);
        return true;
    }

    private void saveElement() {

        sprElement.setCode(etCode.getText().toString());
        sprElement.setName(etName.getText().toString());
        sprElement.setFolder(true);
        AppContext.getDbAdapter().addNewElement(sprElement);
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
                builder.setMessage(R.string.confirm_delete);

                builder.setPositiveButton(R.string.delete,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                setResult(RESULT_DELETE, getIntent());
                                finish();

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
}
