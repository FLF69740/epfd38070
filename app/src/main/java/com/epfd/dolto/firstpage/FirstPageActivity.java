package com.epfd.dolto.firstpage;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import com.epfd.dolto.R;
import com.epfd.dolto.api.NewsHelper;
import com.epfd.dolto.base.BaseActivity;
import com.epfd.dolto.firstpage.recyclerview.FirstPageAdapter;
import com.epfd.dolto.models.News;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FirstPageActivity extends BaseActivity {

    private List<News> mNewsList;
    private FirstPageAdapter mPageAdapter;

    @BindView(R.id.first_page_recycler) RecyclerView mRecyclerView;

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_first_page;
    }

    @Override
    public Boolean isAChildActivity() {
        return false;
    }

    @Override
    public void start(@Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this);
        mNewsList = new ArrayList<>();
        this.getNewsWindow(null);
    }

    //GET News windows
    private void getNewsWindow(@Nullable String classroomsString){
    //    List<String> classrooms = Arrays.asList(classroomsString.split(","));
        DateTime dateTime = new DateTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy");
        NewsHelper.getNewsCollection().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                if (task.getResult() != null) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        News news = documentSnapshot.toObject(News.class);

                        DateTime publicationNews = fmt.parseDateTime(news.getPublication());
                        if (publicationNews.getYear() < dateTime.getYear() || (publicationNews.getYear() == dateTime.getYear() && publicationNews.getDayOfYear() <= dateTime.getDayOfYear())) {
                            mNewsList.add(news);
                        }
                    }
                }
                this.getStorageListNews(mNewsList);

                Context recyclerContext = mRecyclerView.getContext();
                LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(recyclerContext, R.anim.layout_slide_from_right);
                mPageAdapter = new FirstPageAdapter(mNewsList);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                mRecyclerView.setAdapter(mPageAdapter);
                mRecyclerView.setLayoutAnimation(controller);
                mRecyclerView.scheduleLayoutAnimation();
            }
        });
    }

    // Get good chronology of news
    private void getStorageListNews(List<News> newsList){
        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy");
        DateTime origin = new DateTime(fmt.parseDateTime("01/01/2010"));
        List<Integer> dateTimeList = new ArrayList<>();
        for (News news : newsList){
            dateTimeList.add(Days.daysBetween(fmt.parseDateTime(news.getPublication()), origin).getDays());
        }

        boolean checkingEnd = false;

        while (!checkingEnd){
            checkingEnd = true;
            for (int i = 0 ; i < dateTimeList.size() - 1 ; i++){
                if (dateTimeList.get(i) > dateTimeList.get(i+1)){
                    dateTimeList.add(i+1, dateTimeList.remove(i));
                    newsList.add(i+1, newsList.remove(i));
                    checkingEnd = false;
                }
            }
        }
    }







}
