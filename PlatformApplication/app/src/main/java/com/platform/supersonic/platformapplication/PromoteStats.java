package com.platform.supersonic.platformapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class PromoteStats extends StatsBase{

    public PromoteStats() {
        // Required empty public constructor
        this.type = "Promote";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.token = getArguments().getString(BasicActivity.TOKEN);

    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

        try {
            // An item was selected. You can retrieve the selected item using
            String str = (String) parent.getItemAtPosition(pos);
            String dateClause = this.getDateClauseFromDropdown(str);
            String url = BasicActivity.BASE_URL + "api/rest/v1/partners/statistics/promoteTopData?breakdowns[]=date&breakdowns[]=campaign&showAllPossibleRecords=1&top=5" + dateClause;
            Request request = new Request(url, this.token, "GET");
            HttpClient http = new HttpClient(request, null, this.getContext());
            AsyncTask<Void, Void, String> asyncTask = http.execute();
            String st = null;
            st = asyncTask.get();
            JSONObject kpis = new JSONObject();
            if (st != null){
                JSONObject response = new JSONObject(st);

                kpis = response.getJSONObject("kpis");
            }

            ArrayList<KPI> kpiList = new ArrayList<KPI>();

            this.addKPIToList(kpis, kpiList, "clicks", "Clicks", false);
            this.addKPIToList(kpis, kpiList, "completionCount", "Completions", false);
            this.addKPIToList(kpis, kpiList, "spendInLeadingCurrency", "Spend", true);
            this.addKPIToList(kpis, kpiList, "impressions", "Impressions", false);

            KPI[] kpiArr = new KPI[kpiList.size()];
            kpiArr = kpiList.toArray(kpiArr);

            KPIAdapter kpisadapter = new KPIAdapter(this.getContext(),
                    R.layout.kpi_row, kpiArr);

            kpiListView.setAdapter(kpisadapter);

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


}