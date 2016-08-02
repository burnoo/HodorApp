package pl.droidsonroids.hodor.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.droidsonroids.hodor.HodorApplication;
import pl.droidsonroids.hodor.R;
import pl.droidsonroids.hodor.adapter.UsersListAdapter;
import pl.droidsonroids.hodor.util.DatabaseHelper;
import pl.droidsonroids.hodor.util.DialogUtil;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler_view_users)
    RecyclerView mRecyclerViewUsers;

    private List<String> mFriendsList = new ArrayList<>();
    private UsersListAdapter mAdapter;
    private DatabaseHelper mDatabaseHelper;

    public static void startActivity(final Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mDatabaseHelper = HodorApplication.getInstance().getDatabaseHelper();

        setSupportActionBar(mToolbar);
        setView();
        initFriendsList();
    }

    private void initFriendsList() {
        mDatabaseHelper.getAllFriendsFromDatabase(users -> {
            mFriendsList.clear();
            mFriendsList.addAll(users);
            mAdapter.notifyDataSetChanged();
        });
    }

    private void setView() {
        mRecyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new UsersListAdapter(mFriendsList, mDatabaseHelper);
        mRecyclerViewUsers.setAdapter(mAdapter);
    }

    @SuppressLint("InflateParams")
    @OnClick(R.id.fab)
    public void onAddUserClick() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add, null, false);
        final EditText editTextUsername = (EditText) dialogView.findViewById(R.id.edit_text_username);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.add_friend_dialog_title));
        builder.setView(dialogView);
        builder.setPositiveButton(getString(R.string.add_friend_dialog_positive), (dialog, which) -> {
            final String username = editTextUsername.getText().toString();
            onAddFriendClick(username);
        });
        builder.setNegativeButton(getString(R.string.add_friend_dialog_negative), (dialog, which) -> {
            dialog.cancel();
        });

        builder.show();
    }

    private void onAddFriendClick(final String username) {
        mDatabaseHelper.getUserFromDatabase(username, user -> {
            if (user != null) {
                mDatabaseHelper.addFriendToDatabase(user);
            } else {
                DialogUtil.showUserDoesNotExist(MainActivity.this);
            }
        });
    }
}
