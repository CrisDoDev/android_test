package com.example.mobileapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.example.mobileapp.R;
import com.example.mobileapp.activity.AuthActivity;
import com.example.mobileapp.database.UserDatastore;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//: Thư viện Google để xóa bộ nhớ đệm tài khoản khi Đăng xuất
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class AccountFragment extends Fragment {

    private LinearLayout layoutAuth, layoutGuest;
    private ImageView imgAvatar;
    private TextView txtName, txtEmail, txtPhone;
    private UserDatastore userDatastore;
    private CompositeDisposable disposables = new CompositeDisposable();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        userDatastore = new UserDatastore(requireContext());
        layoutAuth = view.findViewById(R.id.layoutAuthenticated);
        layoutGuest = view.findViewById(R.id.layoutGuest);

        imgAvatar = view.findViewById(R.id.imgUserAvatar);
        txtName = view.findViewById(R.id.txtProfileFullName);
        txtEmail = view.findViewById(R.id.txtProfileEmail);
        txtPhone = view.findViewById(R.id.txtProfilePhone);

        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fbUser != null) {
            layoutAuth.setVisibility(View.VISIBLE);
            layoutGuest.setVisibility(View.GONE);

            disposables.add(userDatastore.getUser()
                    .subscribe(userLocal -> {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                txtName.setText(userLocal.getFullName() != null && !userLocal.getFullName().isEmpty() ? userLocal.getFullName() : fbUser.getDisplayName());
                                txtEmail.setText(userLocal.getEmail() != null && !userLocal.getEmail().isEmpty() ? userLocal.getEmail() : fbUser.getEmail());

                                if (userLocal.getPhoneNumber() != null && !userLocal.getPhoneNumber().trim().isEmpty()) {
                                    txtPhone.setText(userLocal.getPhoneNumber());
                                } else {
                                    txtPhone.setText("Chưa cập nhật số điện thoại");
                                }
                            });
                        }
                    }, throwable -> Log.e("AccountFragment", "Lỗi đọc Datastore: " + throwable.getMessage())));

            if (fbUser.getPhotoUrl() != null) {
                Glide.with(this).load(fbUser.getPhotoUrl()).into(imgAvatar);
            }
        } else {
            layoutAuth.setVisibility(View.GONE);
            layoutGuest.setVisibility(View.VISIBLE);
        }

        // Bấm nút chuyển hướng sang Auth đăng nhập
        view.findViewById(R.id.btnGoToAuth).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AuthActivity.class);
            startActivity(intent);
        });

        // Khách vãng lai bấm quay về trang chủ
        view.findViewById(R.id.txtBackToHomeFromAccount).setOnClickListener(v -> {
            navigateToHomeMenu();
        });

        // =========================================================================
        // NÚT ĐĂNG XUẤT
        // =========================================================================
        view.findViewById(R.id.btnLogout).setOnClickListener(v -> {
            disposables.clear(); // 1. Ngắt luồng RxJava tránh crash app
            userDatastore.clear(); // 2. Xóa sạch dữ liệu đăng nhập trong máy
            FirebaseAuth.getInstance().signOut(); // 3. Đăng xuất khỏi hệ thống Firebase

            // 4. Khởi tạo nhanh Client và ép Google xóa sạch bộ nhớ tạm tài khoản trên máy
            try {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
                GoogleSignIn.getClient(requireContext(), gso).signOut().addOnCompleteListener(task -> {
                    Log.d("AccountFragment", "Đã xóa sạch session bộ nhớ đệm của Google thành công!");
                });
            } catch (Exception e) {
                Log.e("AccountFragment", "Lỗi khi xóa cache Google: " + e.getMessage());
            }

            // 5. Quay về thẳng màn hình Trang Chủ và cập nhật trạng thái
            navigateToHomeMenu();
        });

        return view;
    }

    private void navigateToHomeMenu() {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment())
                .commit();

        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_navigation);
        if (bottomNav != null) {
            bottomNav.setSelectedItemId(R.id.nav_home);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposables.clear();
    }
}