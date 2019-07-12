package com.epfd.csandroid.formulary.recyclerview;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.epfd.csandroid.R;
import com.epfd.csandroid.models.Kid;
import com.epfd.csandroid.utils.Utils;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FormularyViewHolder extends RecyclerView.ViewHolder {

    private View mItemView;
    private WeakReference<FormularyAdapter.Listener> callbackWeakRef;

    @BindView(R.id.formulary_recycler_name) EditText mName;
    @BindView(R.id.formulary_recycler_forname) EditText mForname;
    @BindView(R.id.formulary_recycler_classe) EditText mClasse;
    @BindView(R.id.formulary_recycler_delete) ImageView mImageViewDelete;
    @BindView(R.id.formulary_recycler_item_man_logo) ImageView mBoyLogo;
    @BindView(R.id.formulary_recycler_item_woman_logo) ImageView mGirlLogo;

    public FormularyViewHolder(@NonNull View itemView) {
        super(itemView);
        mItemView = itemView;
        ButterKnife.bind(this, mItemView);
    }

    public void updateWithAdapterInformation(Kid kid, int position, FormularyAdapter.Listener callback){

        mName.setText(kid.getNom());
        mForname.setText(kid.getPrenom());
        mClasse.setText(kid.getClasse());

        if (position == 0){
            mImageViewDelete.setVisibility(View.INVISIBLE);
        }

        switch (kid.getGenre()){
            case Utils.BOY : mBoyLogo.setAlpha(1.0f); break;
            case Utils.GIRL : mGirlLogo.setAlpha(1.0f); break;
        }

        mName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateName(position, s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        mForname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateForname(position, s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        mClasse.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateClassroom(position, s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        mImageViewDelete.setOnClickListener(v -> onClickDeleteBtn());

        mBoyLogo.setOnClickListener(v -> {
            mBoyLogo.setAlpha(1.0f);
            mGirlLogo.setAlpha(0.1f);
            updateGender(position, Utils.BOY);
        });

        mGirlLogo.setOnClickListener(v -> {
            mBoyLogo.setAlpha(0.1f);
            mGirlLogo.setAlpha(1.0f);
            updateGender(position, Utils.GIRL);
        });

        this.callbackWeakRef = new WeakReference<>(callback);
    }

    //CALLBACK for NAME
    private void updateName(int position, String result){
        FormularyAdapter.Listener callback = callbackWeakRef.get();
        if (callback != null) callback.updateName(result, position);
    }

    //CALLBACK for FORNAME
    private void updateForname(int position, String result){
        FormularyAdapter.Listener callback = callbackWeakRef.get();
        if (callback != null) callback.updateForname(result, position);
    }

    //CALLBACK for CLASSROOM
    private void updateClassroom(int position, String result){
        FormularyAdapter.Listener callback = callbackWeakRef.get();
        if (callback != null) callback.updateClassroom(result, position);
    }

    //CALLBACK for DELETE BUTTON
    private void onClickDeleteBtn(){
        FormularyAdapter.Listener callback = callbackWeakRef.get();
        if (callback != null) callback.onClickDeleteButton(getAdapterPosition());
    }

    //CALLBACK for GENDER
    private void updateGender(int position, String result){
        FormularyAdapter.Listener callback = callbackWeakRef.get();
        if (callback != null) callback.updateGender(result, position);
    }

}
