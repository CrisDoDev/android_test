package com.example.mobileapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.mobileapp.R;
import com.example.mobileapp.activity.AdminMainActivity;
import com.example.mobileapp.activity.AuthActivity;
import com.example.mobileapp.activity.MainActivity;
import com.example.mobileapp.api.RetrofitClient;
import com.example.mobileapp.database.UserDatastore;
import com.example.mobileapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class LoginFragment extends Fragment {

    private FirebaseAuth mAuth;
    private AuthActivity authActivity;
    private UserDatastore userDatastore;

    private EditText etEmail, etPassword;
    private Button btnLogin, btnGoogleSignIn;
    private TextView tvForgotPassword, tvRegister;

    // Build executor service goi api dong bo cho fragment
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public LoginFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mAuth = FirebaseAuth.getInstance();
        authActivity = (AuthActivity) getActivity();
        userDatastore = new UserDatastore(requireContext());

        etEmail = view.findViewById(R.id.et_email);
        etPassword = view.findViewById(R.id.et_password);
        btnLogin = view.findViewById(R.id.btn_login);
        btnGoogleSignIn = view.findViewById(R.id.btn_google_sign_in);
        tvForgotPassword = view.findViewById(R.id.tv_forgot_password);
        tvRegister = view.findViewById(R.id.tv_register);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getActivity(), "Dien thong tin vao!", Toast.LENGTH_SHORT).show();
                return;
            }
            signInWithEmailPassword(email, password);
        });

        btnGoogleSignIn.setOnClickListener(v -> authActivity.startGoogleSignIn());

        tvForgotPassword.setOnClickListener(v -> requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.auth_fragment_container, new ForgotPasswordFragment()).addToBackStack(null).commit());

        tvRegister.setOnClickListener(v -> requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.auth_fragment_container, new RegisterFragment()).addToBackStack(null).commit());

        return view;
    }

    private void signInWithEmailPassword(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity(), task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) syncUserWithBackend(user);
            } else {
                Toast.makeText(getActivity(), "Email hoac mat khau sai roi!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Goi luong ngam de sync
    private void syncUserWithBackend(FirebaseUser firebaseUser) {
        User reqUser = new User();
        reqUser.setFirebaseUid(firebaseUser.getUid());
        reqUser.setFullName(firebaseUser.getDisplayName() != null ? firebaseUser.getDisplayName() : "User");
        reqUser.setEmail(firebaseUser.getEmail());
        reqUser.setPhoneNumber(firebaseUser.getPhoneNumber() != null ? firebaseUser.getPhoneNumber() : "");

        executorService.execute(() -> {
            try {
                Response<User> response = RetrofitClient.getApiService().syncUser(reqUser).execute();
                mainHandler.post(() -> {
                    if (response.isSuccessful() && response.body() != null) {
                        User resUser = response.body();
                        userDatastore.saveUser(resUser.getIdUser(), resUser.getFirebaseUid(), resUser.getFullName(), resUser.getEmail(), resUser.getRoleId());
                        Toast.makeText(getActivity(), "Dang nhap thanh cong!", Toast.LENGTH_SHORT).show();
                        navigateToHome(resUser.getRoleId());
                    } else {
                        Toast.makeText(getActivity(), "Loi dong bo db backend!", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                mainHandler.post(() -> Toast.makeText(getActivity(), "Loi ket noi server!", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void navigateToHome(int roleId) {
        Intent intent = new Intent(getActivity(), (roleId == 1) ? AdminMainActivity.class : MainActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}