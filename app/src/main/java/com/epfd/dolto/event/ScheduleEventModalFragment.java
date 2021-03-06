package com.epfd.dolto.event;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.epfd.dolto.R;
import com.epfd.dolto.api.StageRegistrationHelper;
import com.epfd.dolto.event.recyclerview.BottomSheetSchedulesAdapter;
import com.epfd.dolto.models.ModalUserTimeTable;
import com.epfd.dolto.models.SingleScheduleBottomSheet;
import com.epfd.dolto.models.Stage;
import com.epfd.dolto.models.StageRegistration;
import com.epfd.dolto.utils.AdminDialog;
import com.epfd.dolto.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ScheduleEventModalFragment extends BottomSheetDialogFragment implements BottomSheetSchedulesAdapter.ListenerBottomSheet, AdminDialog.ListenerAdminDialog {

    @BindView(R.id.modal_fragment_stage_title) TextView mTitle;
    @BindView(R.id.modal_fragment_recycler) RecyclerView mRecyclerView;

    private BottomSheetSchedulesAdapter mAdapter;
    private ArrayList<SingleScheduleBottomSheet> mPlanning;
    private String mUserName;
    private ModalUserTimeTable mTimeTable;
    private String mRegistration;
    private String mMailDev;
    private int mAdminPosition; // after dialog fragment callback define planning position
    private boolean mAdminAddAction; // after dialog fragment callback define if it'a add process or delete process

    private static final String KEY_SCHEDULE_ID = "KEY_SCHEDULE_ID";
    private static final String KEY_EVENT_ID = "KEY_EVENT_ID";
    private static final String KEY_TIMETABLE = "KEY_TIMETABLE";
    private static final String KEY_USERNAME = "KEY_USERNAME";
    private static final String KEY_MAIL = "KEY_MAIL";
    private static final String SAVE_INSTANCE_STATE_PLANNING = "SAVE_INSTANCE_STATE_PLANNING";
    private static final String SAVE_INSTANCE_STATE_ADMIN_POS = "SAVE_INSTANCE_STATE_ADMIN_POS";
    private static final String SAVE_INSTANCE_STATE_ADMIN_ACTION = "SAVE_INSTANCE_STATE_ADMIN_ACTION";

    static ScheduleEventModalFragment newInstance(Stage stage, String eventUid, ModalUserTimeTable timeTable, String userName, String mailDev){
        ScheduleEventModalFragment scheduleEventModalFragment = new ScheduleEventModalFragment();
        Bundle bundle = new Bundle(5);
        bundle.putParcelable(KEY_SCHEDULE_ID, stage);
        bundle.putString(KEY_EVENT_ID, eventUid);
        bundle.putParcelable(KEY_TIMETABLE, timeTable);
        bundle.putString(KEY_USERNAME, userName);
        bundle.putString(KEY_MAIL, mailDev);
        scheduleEventModalFragment.setArguments(bundle);
        return scheduleEventModalFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.modal_fragment_schedule_event, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Stage stage = getArguments().getParcelable(KEY_SCHEDULE_ID);
        mRegistration = getArguments().getString(KEY_EVENT_ID) + stage.getUid();
        mTimeTable = getArguments().getParcelable(KEY_TIMETABLE);
        mUserName = getArguments().getString(KEY_USERNAME);
        mMailDev = getArguments().getString(KEY_MAIL);

        mTitle.setText(stage.getTitle());

        List<String> scheduleStageString = Arrays.asList(stage.getSchedule().split(","));

        if (savedInstanceState != null){
            mPlanning = savedInstanceState.getParcelableArrayList(SAVE_INSTANCE_STATE_PLANNING);
        }else {
            mPlanning = new ArrayList<>();

            for (String schedule : scheduleStageString) {
                SingleScheduleBottomSheet singleScheduleBottomSheet = new SingleScheduleBottomSheet(schedule, mRegistration);
                mPlanning.add(singleScheduleBottomSheet);
                updatePeopleIntoPlanning(mPlanning, mRegistration);
            }

            EventBusiness.compareTimeTableAndStagePlanning(mPlanning, mTimeTable);
        }

        boolean adminAct = false;

        if (mMailDev.equals(Utils.DEV)){
            adminAct = true;
        }

        mAdapter = new BottomSheetSchedulesAdapter(mPlanning, this, adminAct);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SAVE_INSTANCE_STATE_PLANNING, mPlanning);
        outState.putInt(SAVE_INSTANCE_STATE_ADMIN_POS, mAdminPosition);
        outState.putBoolean(SAVE_INSTANCE_STATE_ADMIN_ACTION, mAdminAddAction);
    }

    private void updatePeopleIntoPlanning(List<SingleScheduleBottomSheet> planning, String uid){
        StageRegistrationHelper.getStageRegistration(uid).addOnSuccessListener(documentSnapshot -> {
            StageRegistration registration = documentSnapshot.toObject(StageRegistration.class);
            List<String> peopleGroup = Arrays.asList(registration.getParticipant().split(","));
            for (int i = 0 ; i < planning.size() ; i++){
                planning.get(i).setParticipantList(peopleGroup.get(i));
            }
            mAdapter.notifyDataSetChanged();
        });
    }

    private void addProcess(int position, boolean adminAct, @Nullable String adminName){
        String targetName = mUserName;
        if (adminAct) {
            targetName = adminName;
        }

        EventBusiness.addParticipantIntoPlanning(mPlanning, targetName, position);

        StageRegistrationHelper.updateStageRegistrationParticipant(mRegistration, EventBusiness.listPlanningToString(mPlanning))
                .addOnSuccessListener(aVoid -> {
                    mAdapter.notifyDataSetChanged();
                    mCallback.callbackModal(mTimeTable);
                });
    }

    private void deleteProcess(int position, boolean adminAct, @Nullable String adminName) {
        String targetName = mUserName;
        if (adminAct) {
            targetName = adminName;
        }

        EventBusiness.deleteParticipantIntoPlanning(mPlanning, targetName, position);

        StageRegistrationHelper.updateStageRegistrationParticipant(mRegistration, EventBusiness.listPlanningToString(mPlanning))
                .addOnSuccessListener(aVoid -> {
                    mAdapter.notifyDataSetChanged();
                    mCallback.callbackModal(mTimeTable);
                });
    }

    @Override
    public void activeParticipation(int position) {
        addProcess(position,false,null);
    }

    @Override
    public void deleteParticipation(int position) {
        deleteProcess(position, false, null);
    }

    @Override
    public void getAdminChoiceUsername(String name) {
        if (!name.equals(Utils.EMPTY) && !name.equals(mUserName)) addProcess(mAdminPosition, true, name);
    }

    @Override
    public void deleteAdminChoiceUsername(String name) {
        if (!name.equals(Utils.EMPTY) && !name.equals(mUserName)) deleteProcess(mAdminPosition, true, name);
    }

    @Override
    public void adminParticipation(int position) {
        mAdminPosition = position;
        mAdminAddAction = false;
        AdminDialog adminDialog = new AdminDialog();
        adminDialog.setTargetFragment(this, 1);
        adminDialog.show(getFragmentManager(), Utils.ADMIN_DIALOG_ASK);
    }

    /**
     *  CALLBACK
     */

    // interface for button clicked
    public interface modalFragmentListener{
        void callbackModal(ModalUserTimeTable modalUserTimeTable);
    }

    //callback for button clicked
    private modalFragmentListener mCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (modalFragmentListener) getActivity();
        } catch (ClassCastException e){
            throw new ClassCastException(e.toString() + " must implement ItemClickedListener");
        }
    }

}
