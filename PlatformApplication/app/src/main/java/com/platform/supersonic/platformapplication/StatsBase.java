package com.platform.supersonic.platformapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class StatsBase extends Fragment implements AdapterView.OnItemSelectedListener{

    String type;
    protected String token;

    public StatsBase() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.stats, container, false);

        TextView type = (TextView) view.findViewById(R.id.type);
        type.setText(this.type);

        Spinner spinner = (Spinner) view.findViewById(R.id.dates_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.dates_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setOnItemSelectedListener(this);
        spinner.setAdapter(adapter);

        return view;
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

        try {
            // An item was selected. You can retrieve the selected item using
            String str = (String) parent.getItemAtPosition(pos);
            String dateClause = this.getDateClauseFromDropdown(str);
            String url = "https://platform.supersonic.com/api/rest/v1/partners/statistics/promoteTopData?breakdowns[]=date&breakdowns[]=campaign&showAllPossibleRecords=1&top=5&fromDate=2016-6-16&toDate=2016-6-22";
            Request request = new Request(url, this.token, "GET");
            HttpClient http = new HttpClient(request, null);
            AsyncTask<Void, Void, String> asyncTask = http.execute();
            String st = null;
            st = asyncTask.get();
            JSONObject response = new JSONObject(st);

            JSONObject kpis = new JSONObject(response.getString("kpis"));

            


            Toast toast = Toast.makeText(this.getContext(),str,Toast.LENGTH_SHORT);
            toast.show();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private String getDateClauseFromDropdown(String chosenDate) {
        return "";
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
        String str = "Nothing was called";
        Toast toast = Toast.makeText(this.getContext(),str,Toast.LENGTH_SHORT);
        toast.show();
    }

}