package com.example.ngoctin.musicstreaming.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ngoctin.musicstreaming.R;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import java.security.acl.Group;
import java.util.ArrayList;


public class GroupFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerListGroups;
//    public FragGroupClickFloatButton onClickFloatButton;
    private ArrayList<Group> listGroup;
//    private ListGroupsAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    public static final int CONTEXT_MENU_DELETE = 1;
    public static final int CONTEXT_MENU_EDIT = 2;
    public static final int CONTEXT_MENU_LEAVE = 3;
    public static final int REQUEST_EDIT_GROUP = 0;
    public static final String CONTEXT_MENU_KEY_INTENT_DATA_POS = "pos";

    public GroupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_group, container, false);

//        listGroup = GroupDB.getInstance(getContext()).getListGroups();
//        recyclerListGroups = (RecyclerView) layout.findViewById(R.id.recycleListGroup);
//        mSwipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.swipeRefreshLayout);
//        mSwipeRefreshLayout.setOnRefreshListener(this);
//        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
//        recyclerListGroups.setLayoutManager(layoutManager);
//        adapter = new ListGroupsAdapter(getContext(), listGroup);
//        recyclerListGroups.setAdapter(adapter);
//        onClickFloatButton = new FragGroupClickFloatButton();
//        progressDialog = new LovelyProgressDialog(getContext())
//                .setCancelable(false)
//                .setIcon(R.drawable.ic_dialog_delete_group)
//                .setTitle("Deleting....")
//                .setTopColorRes(R.color.colorAccent);
//
//        waitingLeavingGroup = new LovelyProgressDialog(getContext())
//                .setCancelable(false)
//                .setIcon(R.drawable.ic_dialog_delete_group)
//                .setTitle("Group leaving....")
//                .setTopColorRes(R.color.colorAccent);
//
//        if(listGroup.size() == 0){
//            //Ket noi server hien thi group
//            mSwipeRefreshLayout.setRefreshing(true);
//            getListGroup();
//        }
        return layout;
    }

    @Override
    public void onRefresh() {

    }
}
