package com.hitherejoe.androidboilerplate.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hitherejoe.androidboilerplate.AndroidBoilerplateApplication;
import com.hitherejoe.androidboilerplate.R;
import com.hitherejoe.androidboilerplate.data.DataManager;
import com.hitherejoe.androidboilerplate.data.SyncService;
import com.hitherejoe.androidboilerplate.data.model.Ribot;
import com.hitherejoe.androidboilerplate.ui.adapter.RibotItemViewHolder;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.android.app.AppObservable;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;
import uk.co.ribot.easyadapter.EasyRecyclerAdapter;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private DataManager mDataManager;
    private CompositeSubscription mSubscriptions;
    private EasyRecyclerAdapter<Ribot> mRecyclerAdapter;

    @InjectView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(SyncService.getStartIntent(this));
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        mSubscriptions = new CompositeSubscription();
        mDataManager = AndroidBoilerplateApplication.get().getDataManager();
        mRecyclerAdapter = new EasyRecyclerAdapter<>(this, RibotItemViewHolder.class);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadRibots();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }

    private void loadRibots() {
        mSubscriptions.add(AppObservable.bindActivity(this, mDataManager.getRibots())
                .subscribeOn(mDataManager.getScheduler())
                .subscribe(new Action1<List<Ribot>>() {
                    @Override
                    public void call(List<Ribot> ribots) {
                        mRecyclerAdapter.setItems(ribots);
                    }
                }));
    }

}
