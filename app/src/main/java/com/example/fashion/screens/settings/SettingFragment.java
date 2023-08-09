package com.example.fashion.screens.settings;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.example.fashion.R;
import com.example.fashion.screens.login.LoginActivity;
import com.example.fashion.screens.orders.OrderActivity;
import com.example.fashion.data.adapter.SettingAdapter;
import com.example.fashion.data.datalocal.DataLocalManager;
import com.example.fashion.data.model.SettingItem;
import com.example.fashion.data.model.user.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SettingFragment extends Fragment {

    TextView tv_cartstatus, tv_logout, welcome, account_info, edit_profile_btn;
    User user;
    ImageView avt;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public SettingFragment() {
    }

    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setControl();
        setEvent();
    }

    @Override
    public void onResume() {
        System.out.println("onResume");
        user = DataLocalManager.getUser();
        welcome.setText(user.getLastname() + " " + user.getFirstname());

        super.onResume();
    }

    private void setEvent() {
        welcome.setText(user.getLastname() + " " + user.getFirstname());

        edit_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChangeInfoActivity.class);
                startActivity(intent);
            }
        });

        account_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ViewAccountInfo.class);
                startActivity(i);
            }
        });

        tv_cartstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), OrderActivity.class);
                startActivity(intent);
            }
        });

        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openConfirmDialog();
            }
        });
    }

    private void openConfirmDialog() {
        com.apachat.loadingbutton.core.customViews.CircularProgressButton btnConfirm, btnCancel;
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_confirm);

        btnConfirm = dialog.findViewById(R.id.btnConfirm);
        btnCancel = dialog.findViewById(R.id.btnCancel);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        } else {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams windowAttributes = window.getAttributes();
            windowAttributes.gravity = Gravity.CENTER;
            window.setAttributes(windowAttributes);
            dialog.setCancelable(false);
            dialog.show();
            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
            });
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }

    }
    private void setControl() {
        tv_cartstatus = getView().findViewById(R.id.cartstatus);
        tv_logout = getView().findViewById(R.id.logout);
        welcome = getView().findViewById(R.id.welcome);
        user = DataLocalManager.getUser();
        account_info = getView().findViewById(R.id.account_info);
        edit_profile_btn = getView().findViewById(R.id.editPersonalInfo);
        avt = getView().findViewById(R.id.avt);
    }

}
