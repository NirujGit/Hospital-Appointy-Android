package com.example.aman.hospitalappointy;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.security.Key;

/**
 * Created by Aman on 14-Feb-18.
 */

public class Fragment_Specialization extends Fragment {

    private TextInputLayout mSearch;
    private EditText searchtext;

    private RecyclerView mRecylerView;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public Fragment_Specialization(){
        //Required Empty public constructor otherwise app will crash
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_specialization,container,false);

        mSearch = (TextInputLayout) rootView.findViewById(R.id.search_by_specialization);
        searchtext = (EditText) rootView.findViewById(R.id.special_searchtxt);

        searchtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                onStart();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                onStart();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                onStart();
            }
        });

        mRecylerView = (RecyclerView) rootView.findViewById(R.id.specialization_recyclerView);
        mRecylerView.setHasFixedSize(true);
        mRecylerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();


        String search = mSearch.getEditText().getText().toString();

        Query query = mDatabase.child("Specialization");

        FirebaseRecyclerOptions<DoctorList> firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<DoctorList>()
                .setQuery(query, DoctorList.class)
                .build();

        FirebaseRecyclerAdapter<DoctorList,SpecializationViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<DoctorList, SpecializationViewHolder>(firebaseRecyclerOptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull SpecializationViewHolder holder, final int position, @NonNull final DoctorList model) {

                        final String Special = getRef(position).getKey().toString();
                        holder.setSpecialization(Special);
                        holder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getContext(), position+" == "+Special, Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    @Override
                    public SpecializationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.doctorlist_item_layout, parent, false);

                        return new SpecializationViewHolder(view);
                    }
                };
        mRecylerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();

    }

    public class SpecializationViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public SpecializationViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setSpecialization(String special) {
            TextView userName = (TextView) mView.findViewById(R.id.doctor_name);
            userName.setText(special);
        }
    }
}
