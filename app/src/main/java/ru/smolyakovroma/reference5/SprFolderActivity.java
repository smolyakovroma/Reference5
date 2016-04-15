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
import android.widget.ImageButton;

import ru.smolyakovroma.reference5.model.SprElement;

public class SprFolderActivity extends AppCompatActivity {

    public static final int RESULT_SAVE = 100;
    public static final int RESULT_DELETE = 101;

    private static final int NAME_LENGTH = 20;

    EditText etCode;
    EditText etName;
    EditText etGroup;
    ImageButton ibSelectGroup;

    private SprElement sprElement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spr_folder);

        etCode = (EditText) findViewById(R.id.et_code);
        etName = (EditText) findViewById(R.id.et_name);
        etGroup = (EditText) findViewById(R.id.et_group);

        ibSelectGroup = (ImageButton) findViewById(R.id.ib_select_group);
        ibSelectGroup.setOnClickListener(new SprOnClickListener());

        sprElement = (SprElement) getIntent().getSerializableExtra(
                SprActivity.SPR_ELEMENT);

        etCode.setText(sprElement.getCode());
        etName.setText(sprElement.getName());
        etGroup.setText(Integer.toString(sprElement.getParent_id()));
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
        sprElement.setParent_id(Integer.parseInt(etGroup.getText().toString()));
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
                builder.setMessage(sprElement.isRemove()?R.string.confirm_undelete:R.string.confirm_delete);

                builder.setPositiveButton(R.string.confirm,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                sprElement.setRemove(!sprElement.isRemove());

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

    class SprOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ib_select_group:{
                    Intent intentSprElement = new Intent(getApplicationContext(), SprActivitySelect.class);
                    startActivity(intentSprElement);
                    break;
                }
                default:
                    break;
            }
        }
    }
}
