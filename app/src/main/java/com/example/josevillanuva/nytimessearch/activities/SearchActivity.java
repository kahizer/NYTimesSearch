package com.example.josevillanuva.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.josevillanuva.nytimessearch.R;
import com.example.josevillanuva.nytimessearch.adapters.ArticleArrayAdapter;
import com.example.josevillanuva.nytimessearch.extras.EndlessScrollListener;
import com.example.josevillanuva.nytimessearch.models.AdvancedSearch;
import com.example.josevillanuva.nytimessearch.models.Article;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {
    GridView gvResults;
    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;
    AdvancedSearch filters;
    String searchQuery;
    int nextPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setViews();
    }

    public void setViews(){
        gvResults = (GridView) findViewById(R.id.gvResults);
        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                //nextPage = nextPage++;
                //Log.d("DEBUGER", "page number is " + page);
                //Log.d("DEBUGER", "next page number is " + nextPage);
                customLoadMoreDataFromAppApi(nextPage);
                nextPage++;
                return false;
            }
        });

        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(this, articles);
        gvResults.setAdapter(adapter);

        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);
                Article article = articles.get(position);
                intent.putExtra("article", article);
                startActivity(intent);
            }
        });
    }

    public void customLoadMoreDataFromAppApi(int page){
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
        RequestParams params = new RequestParams();
        params.put("api-key", "1344734c6c1390524c90a4dc93dee826:8:74388652");
        params.put("page", page);
        params.put("q", searchQuery);

        if(filters != null){
            List<String> newsfed = filters.getNewsDesk();
            params.put("begin_date", filters.getStartDate());
            params.put("end_date", filters.getEndDate());
            params.put("sort", filters.getOrder());
            params.put("news_desk", newsfed.get(0));
            if(newsfed.size() > 1){
                params.put("news_desk", newsfed.get(1));
            }
        }

        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //Log.d("DEBUG", response.toString());
                JSONArray articleJsonResult = null;
                try {
                    articleJsonResult = response.getJSONObject("response").getJSONArray("docs");
                    adapter.addAll(Article.fromJSONArray(articleJsonResult));
                    //adapter.notifyDataSetChanged();
                    //Log.d("DEBUG", articleJsonResult.toString());
                } catch (JSONException ex) {

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery = query;
                customLoadMoreDataFromAppApi(nextPage);
                nextPage++;
//                AsyncHttpClient client = new AsyncHttpClient();
//                String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
//                RequestParams params = new RequestParams();
//                params.put("api-key", "1344734c6c1390524c90a4dc93dee826:8:74388652");
//                params.put("page", 0);
//                params.put("q", query);
//
//
//
//                if(filters != null){
//                    List<String> newsfed = filters.getNewsDesk();
//                    params.put("begin_date", filters.getStartDate());
//                    params.put("end_date", filters.getEndDate());
//                    params.put("sort", filters.getOrder());
//                    params.put("news_desk", newsfed.get(0));
//                }
//
//                client.get(url, params, new JsonHttpResponseHandler() {
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                        Log.d("DEBUG", response.toString());
//                        JSONArray articleJsonResult = null;
//                        try {
//                            articleJsonResult = response.getJSONObject("response").getJSONArray("docs");
//                            adapter.addAll(Article.fromJSONArray(articleJsonResult));
//                            //adapter.notifyDataSetChanged();
//                            Log.d("DEBUG", articleJsonResult.toString());
//                        } catch (JSONException ex) {
//
//                        }
//                    }
//                });
                adapter.clear();
                adapter.notifyDataSetChanged();

                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                return true;

            case R.id.action_search:
                return true;

            case R.id.action_advanced_search:
                Intent i = new Intent(SearchActivity.this, AdvancedSearchActivity.class);
                startActivityForResult(i, 10);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCocde, Intent data){
        if(resultCocde == 20 && requestCode == 10){
            Bundle bundle = data.getExtras();
            filters = bundle.getParcelable("AdvancedSearch");
            //Log.d("DEBUG", filters.toString());
        }
    }

//    public void onArticleSearch(View view) {
//        String query = etQuery.getText().toString();
//
//        AsyncHttpClient client = new AsyncHttpClient();
//        String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
//        RequestParams params = new RequestParams();
//        params.put("api-key", "1344734c6c1390524c90a4dc93dee826:8:74388652");
//        params.put("page", 0);
//        params.put("q", query);
//
//        client.get(url, params, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                Log.d("DEBUG", response.toString());
//                JSONArray articleJsonResult = null;
//                try {
//                    articleJsonResult = response.getJSONObject("response").getJSONArray("docs");
//                    adapter.addAll(Article.fromJSONArray(articleJsonResult));
//                    //adapter.notifyDataSetChanged();
//                    Log.d("DEBUG", articleJsonResult.toString());
//                } catch (JSONException ex) {
//
//                }
//            }
//        });
//
//        //Toast.makeText(this, query, Toast.LENGTH_LONG).show();
//    }
}
