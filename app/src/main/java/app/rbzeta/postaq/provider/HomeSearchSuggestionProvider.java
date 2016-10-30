package app.rbzeta.postaq.provider;

import android.content.SearchRecentSuggestionsProvider;

import app.rbzeta.postaq.app.AppConfig;

/**
 * Created by Robyn on 29/10/2016.
 */

public class HomeSearchSuggestionProvider extends SearchRecentSuggestionsProvider {

    public final static String AUTHORITY = AppConfig.SEARCH_SUGGESTION_AUTHORITY;
    public final static int MODE = DATABASE_MODE_QUERIES;

    public HomeSearchSuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
