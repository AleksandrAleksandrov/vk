package com.aleksandr.aleksandrov.vk.vk.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.aleksandr.aleksandrov.vk.vk.MyApplication;
import com.aleksandr.aleksandrov.vk.vk.R;
import com.aleksandr.aleksandrov.vk.vk.common.utils.VKListHelper;
import com.aleksandr.aleksandrov.vk.vk.model.WallItem;
import com.aleksandr.aleksandrov.vk.vk.model.view.BaseViewModel;
import com.aleksandr.aleksandrov.vk.vk.model.view.NewsItemBodyViewModel;
import com.aleksandr.aleksandrov.vk.vk.model.view.NewsItemFooterViewModel;
import com.aleksandr.aleksandrov.vk.vk.model.view.NewsItemHeaderViewModel;
import com.aleksandr.aleksandrov.vk.vk.rest.api.WallApi;
import com.aleksandr.aleksandrov.vk.vk.rest.model.request.WallGetRequestModel;
import com.aleksandr.aleksandrov.vk.vk.rest.model.response.WallGetResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFeedFragment extends BaseFeedFragment {

    @Inject
    WallApi mWallApi;


    public NewsFeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getApplicationComponent().inject(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mWallApi.get(new WallGetRequestModel(-86529522).toMap()).enqueue(new Callback<WallGetResponse>() {
            @Override
            public void onResponse(Call<WallGetResponse> call, Response<WallGetResponse> response) {

                List<WallItem> wallItems = VKListHelper.getWallList(response.body().response);
                List<BaseViewModel> list = new ArrayList<>();

                for (WallItem item : wallItems) {
                    list.add(new NewsItemHeaderViewModel(item));
                    list.add(new NewsItemBodyViewModel(item));
                    list.add(new NewsItemFooterViewModel(item));
                }

                mBaseAdapter.addItems(list);

                Toast.makeText(getActivity(), "Likes: " + response.body().response.getItems().get(0).getLikes().getCount(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<WallGetResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public int onCreateToolbarTitle() {
        return R.string.screen_name_news;
    }
}
