package com.chrisarriola.githubrxjava.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


import com.chrisarriola.githubrxjava.networking.GitHubRepo;
import com.chrisarriola.githubrxjava.networking.NetworkRepo;
import com.chrisarriola.githubrxjava.ui.adapters.GitHubRepoAdapter;
import com.chrisarriola.githubrxjava.R;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private GitHubRepoAdapter adapter = new GitHubRepoAdapter();
    private Subscription subscription;
    private ListView listView;
    private EditText editTextUsername;
    private Button buttonSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewInit();
        listViewInit();
        gesturesInit();


    }

    private void gesturesInit() {
        buttonSearch.setOnClickListener(this);
    }


    private void viewInit() {
        listView = (ListView) findViewById(R.id.list_view_repos);
        editTextUsername = (EditText) findViewById(R.id.edit_text_username);
        buttonSearch = (Button) findViewById(R.id.button_search);
    }

    private void listViewInit() {

        listView.setAdapter(adapter);
    }


    @Override
    protected void onDestroy() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        super.onDestroy();
    }

    private void getStarredRepos(String username) {
        subscription = NetworkRepo.getInstance()
                .getStarredRepos(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<GitHubRepo>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "In onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d(TAG, "In onError()");
                    }

                    @Override
                    public void onNext(List<GitHubRepo> gitHubRepos) {
                        Log.d(TAG, "In onNext()");
                        adapter.setGitHubRepos(gitHubRepos);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        final String username = editTextUsername.getText().toString();
        if (!TextUtils.isEmpty(username)) {
            getStarredRepos(username);
        }
    }
}
