package com.defvest.devfestnorth.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.defvest.devfestnorth.R;
import com.defvest.devfestnorth.activities.SchedulesDetail;
import com.defvest.devfestnorth.models.schedules_model;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Schedules extends Fragment {
    View rootView;
    Context context;

    private FirebaseRecyclerAdapter<schedules_model, ScheduleViews> firebaserecyclerAdapter;
    private RecyclerView recyclerView;
    private DatabaseReference myref;
    TextView mEmptyListView;
    LinearLayoutManager layoutManager;

    public static Schedules newInstance() {
        Schedules fragment = new Schedules();
        return fragment;
    }

    public Schedules() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_schedules, container, false);
        recyclerView = rootView.findViewById(R.id.recycle_schedules);
        mEmptyListView = rootView.findViewById(R.id.list_schedules_error);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        //recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(mLayoutManager);

        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        //recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setNestedScrollingEnabled(false);
        myref = FirebaseDatabase.getInstance().getReference().child("Agenda");
        myref.keepSynced(true);

        if (isNetworkConnected() || isWifiConnected()) {
            /*Toast.makeText(this, "Network is Available", Toast.LENGTH_SHORT).show();*/
        } else {
            mEmptyListView.setVisibility(View.VISIBLE);
        }

        FirebaseRecyclerOptions<schedules_model> options = new FirebaseRecyclerOptions.Builder<schedules_model>().setQuery(myref, schedules_model.class).build();

        firebaserecyclerAdapter = new FirebaseRecyclerAdapter<schedules_model, ScheduleViews>(options) {


            @SuppressLint("CheckResult")
            @Override
            protected void onBindViewHolder(ScheduleViews viewholder, final int position, final schedules_model model) {
                viewholder.setTitle(model.getTitle());
                viewholder.setTime(model.getTime());
                viewholder.setWhen(model.getWhen());
                viewholder.setWhere(model.getWhere());
                viewholder.setSpeaker(model.getSpeaker());
                RequestOptions placeholderRequest = new RequestOptions();
                placeholderRequest.placeholder(R.drawable.warning);

                switch (model.getCategory()) {
                    case "Android":
                        viewholder.category.setBackground(getResources().getDrawable(R.drawable.android_category));
                        break;
                    case "Web":
                        viewholder.category.setBackground(getResources().getDrawable(R.drawable.web_category));
                        break;
                    case "Machine Learning":
                        viewholder.category.setBackground(getResources().getDrawable(R.drawable.machine_learning_category));
                        break;
                    case "Cloud":
                        viewholder.category.setBackground(getResources().getDrawable(R.drawable.cloud_category));
                        break;
                    case "UI/UX":
                        viewholder.category.setBackground(getResources().getDrawable(R.drawable.uiux_category));
                        break;
                    default:
                        viewholder.category.setBackground(getResources().getDrawable(R.drawable.general_category));
                        break;

                }
                viewholder.setCategory(model.getCategory());
                viewholder.setImage(model.getImage());
                viewholder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle extras = new Bundle();
                        Intent nextactivity = new Intent(v.getContext(), SchedulesDetail.class);

                        nextactivity.putExtra("title", model.getTitle());
                        nextactivity.putExtra("time", model.getTime());
                        nextactivity.putExtra("when", model.getWhen());
                        nextactivity.putExtra("where", model.getWhere());
                        nextactivity.putExtra("speaker", model.getSpeaker());
                        nextactivity.putExtra("uiux_category", model.getCategory());

                        nextactivity.putExtra("image", model.getImage());


                        nextactivity.putExtras(extras);
                        v.getContext().startActivity(nextactivity);

                    }
                });

            }

            @Override
            public ScheduleViews onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedules_custom_list, parent, false);
                return new ScheduleViews(v);
            }
        };
        recyclerView.setAdapter(firebaserecyclerAdapter);

        return rootView;
    }

    public static class ScheduleViews extends RecyclerView.ViewHolder {
        View mView;
        TextView title;
        TextView time;
        TextView when;
        TextView where;
        TextView speaker;
        TextView category;

        CircleImageView image;


        public ScheduleViews(View itemView) {
            super(itemView);
            mView = itemView;
            title = itemView.findViewById(R.id.Stitle);
            time = itemView.findViewById(R.id.Stime);
            when = itemView.findViewById(R.id.Swhen);
            where = itemView.findViewById(R.id.Swhere);
            speaker = itemView.findViewById(R.id.Sspeaker);
            category = itemView.findViewById(R.id.Scategory);

            image = itemView.findViewById(R.id.Sphoto);
        }

        public void setTitle(String titles) {
            title.setText(titles);
        }

        public void setTime(String times) {
            time.setText(times);
        }

        public void setWhen(String whens) {
            when.setText(whens);
        }

        public void setWhere(String wheres) {
            where.setText(wheres);
        }

        public void setSpeaker(String speakers) {
            speaker.setText(speakers);
        }

        public void setCategory(String categories) {
            category.setText(categories);
        }

        @SuppressLint("CheckResult")
        public void setImage(String images) {
            RequestOptions placeholderRequest = new RequestOptions();
            placeholderRequest.placeholder(R.drawable.warning);
            Glide.with(mView.getContext()).load(images).into(image);
        }


    }


    @Override
    public void onStart() {
        super.onStart();
        firebaserecyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaserecyclerAdapter.stopListening();
    }

    private boolean isWifiConnected() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE); // 1
        assert connMgr != null;
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo(); // 2
        return networkInfo != null && networkInfo.isConnected(); // 3
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connMgr != null;
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && (ConnectivityManager.TYPE_WIFI == networkInfo.getType()) && networkInfo.isConnected();
    }


}
