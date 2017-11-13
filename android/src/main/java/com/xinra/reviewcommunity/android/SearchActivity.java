package com.xinra.reviewcommunity.android;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SearchView;

public class SearchActivity extends AbstractActivity {

  // Current behaviour: if you use the getProductList widget in the action bar you get here. If you then
  // change the query using the getProductList widget embedded in this activity, you stay in this activity
  // and fire a new intent. If you then press the back button, this activity is finished (you don't
  // go back to the original query). This is due to android:launchMode="singleTop".
  // Be careful when trying to change this. The automatic creation of intents is somehow
  // inconsistent and sometimes generates multiple ones. You can see this by logging calls of
  // handleIntent(). This pollutes the activity stack when removing singleTop. Maybe the best
  // approach would be to handle intent creation ourselves.

  // This also means that currently, sometimes multiple requests are made to the REST API when
  // using the action bar getProductList. This could be fixed by omitting onNewIntent() and using a
  // custom onQueryTextListener for the embedded widget.

  private SearchView searchWidget;
  private ProductListView resultsView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);

    resultsView = findViewById(R.id.searchResults);
    subscriptions.add(getState().categoryMap.subscribe(resultsView::setCategoryMap));

    searchWidget = findViewById(R.id.searchWidget);
    configureSearchView(searchWidget);

    handleIntent(getIntent());
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    handleIntent(intent);
  }

  private void handleIntent(Intent intent) {
    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
      String query = intent.getStringExtra(SearchManager.QUERY);
      searchWidget.setQuery(query, false);
      searchWidget.clearFocus();
      performSearch(query);
    }
  }

  private void performSearch(String query) {
    // Technically it may be possible to get here before initialization. We'll ignore this for now.
    getApi().getProductList(query).subscribe(resultsView::setContent, this::handleError);
  }
}
