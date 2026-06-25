package com.example.mobileapp.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mobileapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordFragment extends Fragment {

    private static final String TAG = "ForgotPasswordFragment";
    private FirebaseAuth mAuth;

    private EditText etEmail;
    private Button btnResetPassword;
    private TextView tvBackToLogin;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Get references to UI elements
        etEmail = view.findViewById(R.id.et_email);
        btnResetPassword = view.findViewById(R.id.btn_reset_password);
        tvBackToLogin = view.findViewById(R.id.tv_back_to_login);

        // Set click listener for Reset Password button
        btnResetPassword.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(getActivity(), "Vui lòng nhập địa chỉ email của bạn.", Toast.LENGTH_SHORT).show();
                return;
            }

            sendPasswordResetEmail(email);
        });

        // Set click listener for Back to Login TextView
        tvBackToLogin.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.auth_fragment_container, new LoginFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    private void sendPasswordResetEmail(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Email sent.");
                        Toast.makeText(getActivity(), "Email đặt lại mật khẩu đã được gửi đến " + email, Toast.LENGTH_LONG).show();
                        // Optionally, navigate back to LoginFragment after sending email
                        if (getActivity() != null) {
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.auth_fragment_container, new LoginFragment())
                                    .commit();
                        }
                    } else {
                        Log.w(TAG, "sendPasswordResetEmail:failure", task.getException());
                        Toast.makeText(getActivity(), "Không thể gửi email đặt lại mật khẩu. " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
