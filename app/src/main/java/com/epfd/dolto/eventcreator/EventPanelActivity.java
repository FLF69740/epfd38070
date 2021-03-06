package com.epfd.dolto.eventcreator;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import com.epfd.dolto.R;
import com.epfd.dolto.base.BaseActivity;
import com.epfd.dolto.models.Event;
import com.google.android.material.tabs.TabLayout;

public class EventPanelActivity extends BaseActivity implements EventCreatorMainPageFragment.EventSaveClickedListener{

    private EventPanelAdapter mAdapter;

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_event_panel;
    }

    @Override
    public Boolean isAChildActivity() {
        return true;
    }

    @Override
    public void start(@Nullable Bundle savedInstanceState) {
        Event event = getIntent().getExtras().getParcelable(EventCreatorMenuActivity.INTENT_EVENT_CREATOR_MENU);
        ViewPager viewPager = findViewById(R.id.event_creator_panel_viewpager);
        // configure adapter for a new Event
        if (event.getName().equals("")) {
            mAdapter = new EventPanelAdapter(getSupportFragmentManager(), 2, event, false, this);
        }else {// configure adapter for an existing Event
            mAdapter = new EventPanelAdapter(getSupportFragmentManager(), 2, event, true, this);
        }
        viewPager.setAdapter(mAdapter);
        TabLayout tabLayout = findViewById(R.id.event_creator_panel_tablayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }


    @Override
    public void eventFirstPageValidate() {
        mAdapter.addStage(this);
        mAdapter.notifyDataSetChanged();
    }

}
