package com.platform.supersonic.platformapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class StatsBase extends Fragment implements AdapterView.OnItemSelectedListener{

    String type;
    protected String token;

    protected ListView kpiListView;

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

        kpiListView = (ListView) view.findViewById(R.id.kpiListView);


//        TextView type = (TextView) view.findViewById(R.id.type);
//        type.setText(this.type);

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
        // An item was selected. You can retrieve the selected item using
        String str = (String) parent.getItemAtPosition(pos);

    }

    protected String getDateClauseFromDropdown(String chosenDate) {
        String today = new SimpleDateFormat("yyyy-M-dd").format(new java.util.Date());
        switch (chosenDate){
            case "today":
                return "&fromDate=" + today + "&toDate=" + today;
            case "Yesterday":
                String yesterday = this.getDaysBeforeDateString(1);
                return "&fromDate=" + yesterday + "&toDate=" + yesterday;
            case "Last 7 Days":
                String lastWeek = this.getDaysBeforeDateString(6);
                return "&fromDate=" + lastWeek + "&toDate=" + today;
            case "Last 14 Days":
                String lastTwoWeek = this.getDaysBeforeDateString(13);
                return "&fromDate=" + lastTwoWeek + "&toDate=" + today;
        }
        return "&fromDate=" + today + "&toDate=" + today;
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback

    }

    protected KPI.TREND getTrendFromStr(String trend){
        if (trend.equals("same")){
            return  KPI.TREND.SAME;
        }
        if (trend.equals("down")){
            return  KPI.TREND.DOWN;
        }
        if (trend.equals("up")){
            return KPI.TREND.UP;
        }

        return KPI.TREND.SAME;
    }

    private String getDaysBeforeDateString(int numberOfDays) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -numberOfDays);
        return dateFormat.format(cal.getTime());
    }

    protected void addKPIToList(JSONObject kpis, ArrayList<KPI> kpiList, String kpiName, String presentingName, boolean addCurrency) {
        try {
            JSONObject kpi = kpis.getJSONObject(kpiName);
            String value = kpi.getString("value");
            KPI clickKPI = new KPI(
                    this.getTrendFromStr(kpi.getString("trend")),
                    presentingName,
                    addCurrency ? this.formatKPINumber(value)  + " $" : this.formatKPINumber(value)
            );
            kpiList.add(clickKPI);
        } catch (JSONException e) {
            kpiList.add(new KPI(
                    this.getTrendFromStr("same"),
                    presentingName,
                    "N/A"
            ));
        }
    }

    private String formatKPINumber(String value){
        double amount = Double.parseDouble(value);
        DecimalFormat formatter = new DecimalFormat("#,###.##");
        return formatter.format(amount);
    }

}