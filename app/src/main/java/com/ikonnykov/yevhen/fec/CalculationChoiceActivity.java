package com.ikonnykov.yevhen.fec;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

public class CalculationChoiceActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    public static final String UNITS = "Units of measurement";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculation_choice);

        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(new ImageAdapter(this));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
//                Toast.makeText(CalculationChoiceActivity.this, "" + position,
//                        Toast.LENGTH_SHORT).show();
                switch (position) {
                    case 0: {
                        Intent intent = new Intent(CalculationChoiceActivity.this, IF97Activity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.units:
                showUnitsDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showUnitsDialog() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final MaterialDialog unitsDialog = new MaterialDialog.Builder(CalculationChoiceActivity.this)
                .title(R.string.unitsOfMeasurement)
                .items(R.array.unitsOfMeasurement)
                .itemsCallbackSingleChoice(sharedPreferences.getInt(UNITS, 0), new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        /**
                         * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                         * returning false here won't allow the newly selected radio button to actually be selected.
                         **/
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt(UNITS, which);
                        editor.commit();
                        return true;
                    }
                })
                .show();
//        View positive = unitsDialog.getActionButton(DialogAction.POSITIVE);
//        positive.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putInt(UNITS, unitsDialog.getSelectedIndex());
//                editor.commit();
//                unitsDialog.dismiss();
//            }
//        });
    }
}
