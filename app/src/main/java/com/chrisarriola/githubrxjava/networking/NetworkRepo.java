package com.chrisarriola.githubrxjava.networking;

import android.support.annotation.NonNull;

import java.util.List;

import rx.Observable;

public class NetworkRepo {


    private static NetworkRepo instance;

    private NetworkRepo() {
    }


    public static NetworkRepo getInstance() {
        if (instance == null) {
            instance = new NetworkRepo();
        }

        return instance;

    }


    public Observable<List<GitHubRepo>> getStarredRepos(@NonNull String userName) {
        return GitHubClient.getInstance().getGitHubService().getStarredRepositories(userName);
    }
}
