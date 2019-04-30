package com.example.ngoctin.musicstreaming.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ngoctin.musicstreaming.R;
import com.example.ngoctin.musicstreaming.data.StaticConfig;
import com.example.ngoctin.musicstreaming.service.ServiceUtils;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FriendsFragment extends Fragment {

    private RecyclerView recyclerListFrends;
//    private ListFriendsAdapter adapter;
    public FragFriendClickFloatButton onClickFloatButton;
//    private ListFriend dataListFriend = null;
    private ArrayList<String> listFriendID = null;
    private LovelyProgressDialog dialogFindAllFriend;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CountDownTimer detectFriendOnline;
    public static int ACTION_START_CHAT = 1;

    public static final String ACTION_DELETE_FRIEND = ".DELETE_FRIEND";

    private BroadcastReceiver deleteFriendReceiver;

    public FriendsFragment() {
        onClickFloatButton = new FragFriendClickFloatButton();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        detectFriendOnline = new CountDownTimer(System.currentTimeMillis(), StaticConfig.TIME_TO_REFRESH) {
//            @Override
//            public void onTick(long l) {
//                ServiceUtils.updateFriendStatus(getContext(), dataListFriend);
//                ServiceUtils.updateUserStatus(getContext());
//            }
//
//            @Override
//            public void onFinish() {
//
//            }
//        };
//        if (dataListFriend == null) {
//            dataListFriend = FriendDB.getInstance(getContext()).getListFriend();
//            if (dataListFriend.getListFriend().size() > 0) {
//                listFriendID = new ArrayList<>();
//                for (Friend friend : dataListFriend.getListFriend()) {
//                    listFriendID.add(friend.id);
//                }
//                detectFriendOnline.start();
//            }
//        }
        View layout = inflater.inflate(R.layout.fragment_friends, container, false);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
//        recyclerListFrends = (RecyclerView) layout.findViewById(R.id.recycleListFriend);
//        recyclerListFrends.setLayoutManager(linearLayoutManager);
//        mSwipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.swipeRefreshLayout);
//        mSwipeRefreshLayout.setOnRefreshListener(this);
//        adapter = new ListFriendsAdapter(getContext(), dataListFriend, this);
//        recyclerListFrends.setAdapter(adapter);
//        dialogFindAllFriend = new LovelyProgressDialog(getContext());
//        if (listFriendID == null) {
//            listFriendID = new ArrayList<>();
//            dialogFindAllFriend.setCancelable(false)
//                    .setIcon(R.drawable.ic_add_friend)
//                    .setTitle("Get all friend....")
//                    .setTopColorRes(R.color.colorPrimary)
//                    .show();
//            getListFriendUId();
//        }
//
//        deleteFriendReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                String idDeleted = intent.getExtras().getString("idFriend");
//                for (Friend friend : dataListFriend.getListFriend()) {
//                    if(idDeleted.equals(friend.id)){
//                        ArrayList<Friend> friends = dataListFriend.getListFriend();
//                        friends.remove(friend);
//                        break;
//                    }
//                }
//                adapter.notifyDataSetChanged();
//            }
//        };

        IntentFilter intentFilter = new IntentFilter(ACTION_DELETE_FRIEND);
        getContext().registerReceiver(deleteFriendReceiver, intentFilter);

        return layout;
    }

    public class FragFriendClickFloatButton implements View.OnClickListener {
        Context context;
        LovelyProgressDialog dialogWait;

        @Override
        public void onClick(final View view) {
            new LovelyTextInputDialog(view.getContext(), R.style.EditTextTintTheme)
                .setTopColorRes(R.color.colorPrimary)
                .setTitle("Add friend")
                .setMessage("Enter friend email")
                .setIcon(R.drawable.ic_add_friend)
                .setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                .setInputFilter("Email not found", new LovelyTextInputDialog.TextFilter() {
                    // check valid email
                    @Override
                    public boolean check(String text) {
                        Pattern VALID_EMAIL_ADDRESS_REGEX =
                                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
                        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(text);
                        return matcher.find();
                    }
                })
                .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                    @Override
                    public void onTextInputConfirmed(String text) {
                        //Tim id user id
//                        findIDEmail(text);
                        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
        }

        public FragFriendClickFloatButton getInstance(Context context) {
            this.context = context;
            dialogWait = new LovelyProgressDialog(context);
            return this;
        }
    }
}
