package com.platform.supersonic.platformapplication;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Spinner;

import org.json.JSONArray;

import java.util.ArrayList;

public class WidgetConfigurationActivity extends AppCompatActivity {

    private Button saveButton;
    private CheckBox[] cbArray = new CheckBox[5];
    private Spinner spinner;
    private int selectedRadioButton;
    private int appWidgetId;
    public static final String PREFSFILE = "widgetConfigurations";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.loadSharedPrefs(PREFSFILE);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        this.appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        setContentView(R.layout.activity_widget_configuration);
        saveButton = (Button) findViewById(R.id.saveButton);
        spinner = (Spinner) findViewById(R.id.dates_spinner_widget);
        this.cbArray[0] = (CheckBox) findViewById(R.id.checkboxOne);
        this.cbArray[1] = (CheckBox) findViewById(R.id.checkboxTwo);
        this.cbArray[2] = (CheckBox) findViewById(R.id.checkboxThree);
        this.cbArray[3] = (CheckBox) findViewById(R.id.checkboxFour);
        this.cbArray[4] = (CheckBox) findViewById(R.id.checkboxFive);
        this.cbArray[0].setText("Impressions");
        this.cbArray[1].setText("eCPM");
        this.cbArray[2].setText("Requests");
        this.cbArray[3].setText("Fulfillments");
        this.cbArray[4].setVisibility(View.VISIBLE);
        this.cbArray[4].setText("Revenue");
        this.selectedRadioButton = R.id.radioMonetize;

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getBaseContext(),
                R.array.dates_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.radioMonetize:
                if (checked)
                    this.selectedRadioButton = R.id.radioMonetize;
                    this.cbArray[0].setText("Impressions");
                    this.cbArray[1].setText("eCPM");
                    this.cbArray[2].setText("Requests");
                    this.cbArray[3].setText("Fulfillments");
                    this.cbArray[4].setVisibility(View.VISIBLE);
                    this.cbArray[4].setText("Revenue");
                    break;
            case R.id.radioPromote:
                if (checked)
                    this.selectedRadioButton = R.id.radioPromote;
                    this.cbArray[0].setText("Clicks");
                    this.cbArray[1].setText("Impressions");
                    this.cbArray[2].setText("Completions");
                    this.cbArray[3].setText("Spend");
                    this.cbArray[4].setVisibility(View.GONE);

                    break;
        }
    }

    public void saveConfiguration(View view){
        ArrayList<String> selectedKPIs = new ArrayList<>();
        String type = "";
        switch(selectedRadioButton) {
            case R.id.radioMonetize:
                type = "Monetize";
                if (cbArray[0].isChecked())
                    selectedKPIs.add("Impressions");
                if (cbArray[1].isChecked())
                    selectedKPIs.add("eCPM");
                if (cbArray[2].isChecked())
                    selectedKPIs.add("Requests");
                if (cbArray[3].isChecked())
                    selectedKPIs.add("Fulfillments");
                if (cbArray[4].isChecked())
                    selectedKPIs.add("Revenue");
                break;
            case R.id.radioPromote:
                type = "Promote";
                if (cbArray[0].isChecked())
                    selectedKPIs.add("Clicks");
                if (cbArray[1].isChecked())
                    selectedKPIs.add("Impressions");
                if (cbArray[2].isChecked())
                    selectedKPIs.add("Completions");
                if (cbArray[3].isChecked())
                    selectedKPIs.add("Spend");
                break;
        }

        SharedPreferences settings = getSharedPreferences(PREFSFILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(this.appWidgetId + "-Type", type);
        editor.putString(this.appWidgetId + "-KPIs", new JSONArray(selectedKPIs).toString());
        editor.putString(this.appWidgetId + "-DateRange",(String) spinner.getSelectedItem());
        editor.apply();

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, this.appWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();

    }

    public void loadSharedPrefs(String ... prefs) {

        // Logging messages left in to view Shared Preferences. I filter out all logs except for ERROR; hence why I am printing error messages.

        Log.i("Loading Shared Prefs", "-----------------------------------");
        Log.i("----------------", "---------------------------------------");

        for (String pref_name: prefs) {

            SharedPreferences preference = getSharedPreferences(pref_name, MODE_PRIVATE);
            for (String key : preference.getAll().keySet()) {

                Log.i(String.format("Shared Preference : %s - %s", pref_name, key),
                        preference.getString(key, "error!"));

            }

            Log.i("----------------", "---------------------------------------");

        }

        Log.i("Finished Shared Prefs", "----------------------------------");

    }

}
