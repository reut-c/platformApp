package com.platform.supersonic.platformapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

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
            String url = "https://platform.supersonic.com/api/rest/v1/partners/statistics/promoteTopData?breakdowns[]=date&breakdowns[]=campaign&showAllPossibleRecords=1&top=5" + dateClause;
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

            try {
                JSONObject clicks = kpis.getJSONObject("clicks");
                KPI clickKPI = new KPI(
                        this.getTrendFromStr(clicks.getString("trend")),
                        "clicks",
                        clicks.getString("value")
                );
                kpiList.add(clickKPI);
            } catch (JSONException e) {
                kpiList.add(new KPI(
                        this.getTrendFromStr("same"),
                        "clicks",
                        "0"
                ));
            }

            try {
                JSONObject completionCount = kpis.getJSONObject("completionCount");
                KPI completionCountKPI = new KPI(
                        this.getTrendFromStr(completionCount.getString("trend")),
                        "completions",
                        completionCount.getString("value")
                );
                kpiList.add(completionCountKPI);
            } catch (JSONException e) {
                kpiList.add(new KPI(
                        this.getTrendFromStr("same"),
                        "completions",
                        "0"
                ));
            }

            try {
                JSONObject spend = kpis.getJSONObject("spendInLeadingCurrency");
                KPI spendKPI = new KPI(
                        this.getTrendFromStr(spend.getString("trend")),
                        "revenue",
                        spend.getString("value")
                );
                kpiList.add(spendKPI);
            } catch (JSONException e) {
                kpiList.add(new KPI(
                        this.getTrendFromStr("same"),
                        "revenue",
                        "0"
                ));
            }

            try {

                JSONObject impressions = kpis.getJSONObject("impressions");
                KPI impressionsKPI = new KPI(
                        this.getTrendFromStr(impressions.getString("trend")),
                        "impressions",
                        impressions.getString("value")
                );
                kpiList.add(impressionsKPI);
            } catch (JSONException e) {
                kpiList.add(new KPI(
                        this.getTrendFromStr("same"),
                        "impressions",
                        "0"
                ));
            }

            KPI[] kpiArr = new KPI[kpiList.size()];
            kpiArr = kpiList.toArray(kpiArr);

            KPIAdapter kpisadapter = new KPIAdapter(this.getContext(),
                    R.layout.kpi_row, kpiArr);




            kpiListView.setAdapter(kpisadapter);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}