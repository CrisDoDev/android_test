package com.example.mobileapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.mobileapp.activity.MainActivity;
import com.example.mobileapp.api.RetrofitClient;
import com.example.mobileapp.database.UserDatastore;
import com.example.mobileapp.model.User;
import com.example.mobileapp.model.CheckResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class RegisterFragment extends Fragment {

    private FirebaseAuth mAuth;
    private UserDatastore userDatastore;

    private EditText etFullName, etEmail, etPhoneNumber, etPassword, etConfirmPassword;
    private Button btnRegister;
    private TextView tvLogin, tvEmailValidationError, tvPhoneValidationError;

    private final ExecutorService executorService = Executors.newFixedThreadPool(2);
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private boolean isEmailValid = false;
    private boolean isPhoneValid = false;
    private Runnable emailCheckRunnable, phoneCheckRunnable;

    public RegisterFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        mAuth = FirebaseAuth.getInstance();
        userDatastore = new UserDatastore(requireContext());

        etFullName = view.findViewById(R.id.et_full_name);
        etEmail = view.findViewById(R.id.et_email);
        etPhoneNumber = view.findViewById(R.id.et_phone_number);
        etPassword = view.findViewById(R.id.et_password);
        etConfirmPassword = view.findViewById(R.id.et_confirm_password);
        btnRegister = view.findViewById(R.id.btn_register);
        tvLogin = view.findViewById(R.id.tv_login);
        tvEmailValidationError = view.findViewById(R.id.tv_email_validation_error);
        tvPhoneValidationError = view.findViewById(R.id.tv_phone_validation_error);

        setupRealTimeValidation();
        setupPasswordFocusValidation();
        setupFormTextWatchers(); // Lang nghe de mo/khoa nut bấm hop ly

        btnRegister.setOnClickListener(v -> handleRegister());

        tvLogin.setOnClickListener(v -> requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.auth_fragment_container, new LoginFragment()).commit());
        validateForm();

        return view;
    }

    private void setupRealTimeValidation() {
        etEmail.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mainHandler.removeCallbacks(emailCheckRunnable);
            }
            public void afterTextChanged(Editable s) {
                String email = s.toString().trim();
                if (email.length() > 5 && email.contains("@")) {
                    emailCheckRunnable = () -> checkEmailExists(email);
                    mainHandler.postDelayed(emailCheckRunnable, 500);
                } else {
                    tvEmailValidationError.setVisibility(View.GONE);
                    isEmailValid = false;
                    validateForm();
                }
            }
        });

        etPhoneNumber.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mainHandler.removeCallbacks(phoneCheckRunnable);
            }
            public void afterTextChanged(Editable s) {
                String phone = s.toString().trim();
                if (phone.length() >= 10) {
                    phoneCheckRunnable = () -> checkPhoneExists(phone);
                    mainHandler.postDelayed(phoneCheckRunnable, 500);
                } else {
                    tvPhoneValidationError.setVisibility(View.GONE);
                    isPhoneValid = false;
                    validateForm();
                }
            }
        });
    }

    private void setupPasswordFocusValidation() {
        // Kiểm tra lỗi mật khẩu khi người dùng rời khỏi ô nhập Mật khẩu
        etPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) { // Khi người dùng bỏ focus ô này để bấm ô khác
                String pass = etPassword.getText().toString().trim();
                if (!pass.isEmpty() && pass.length() < 6) {
                    etPassword.setError("Mật khẩu phải từ 6 ký tự trở lên!");
                } else {
                    etPassword.setError(null);
                }
            }
        });

        // Kiểm tra lỗi khớp mật khẩu khi người dùng rời khỏi ô Xác nhận mật khẩu
        etConfirmPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String pass = etPassword.getText().toString().trim();
                String confirm = etConfirmPassword.getText().toString().trim();
                if (!confirm.isEmpty() && !pass.equals(confirm)) {
                    etConfirmPassword.setError("Mật khẩu xác nhận không trùng khớp!");
                } else {
                    etConfirmPassword.setError(null);
                }
            }
        });
    }

    // Tu dong chay lai ham check form moi khi nguoi dung go chu
    private void setupFormTextWatchers() {
        TextWatcher genericWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void afterTextChanged(Editable s) { validateForm(); }
        };
        etFullName.addTextChangedListener(genericWatcher);
        etPassword.addTextChangedListener(genericWatcher);
        etConfirmPassword.addTextChangedListener(genericWatcher);
    }

    private void checkEmailExists(String email) {
        executorService.execute(() -> {
            try {
                Response<CheckResponse> response = RetrofitClient.getApiService().checkEmail(email).execute();
                mainHandler.post(() -> {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().isExists()) {
                            tvEmailValidationError.setText("Email nay bi trung roi!");
                            tvEmailValidationError.setVisibility(View.VISIBLE);
                            isEmailValid = false;
                        } else {
                            tvEmailValidationError.setVisibility(View.GONE);
                            isEmailValid = true;
                        }
                    }
                    validateForm();
                });
            } catch (Exception ignored) {}
        });
    }

    private void checkPhoneExists(String phone) {
        executorService.execute(() -> {
            try {
                Response<CheckResponse> response = RetrofitClient.getApiService().checkPhone(phone).execute();
                mainHandler.post(() -> {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().isExists()) {
                            tvPhoneValidationError.setText("Sdt nay co nguoi dung roi!");
                            tvPhoneValidationError.setVisibility(View.VISIBLE);
                            isPhoneValid = false;
                        } else {
                            tvPhoneValidationError.setVisibility(View.GONE);
                            isPhoneValid = true;
                        }
                    }
                    validateForm();
                });
            } catch (Exception ignored) {}
        });
    }

    // Check loi form chat che, khong cho de trong o nhap
    private void validateForm() {
        String name = etFullName.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();
        String confirm = etConfirmPassword.getText().toString().trim();

        boolean isFieldsFilled = !name.isEmpty() && !pass.isEmpty() && !confirm.isEmpty();
        boolean isPasswordMatch = pass.equals(confirm) && pass.length() >= 6;

        btnRegister.setEnabled(isEmailValid && isPhoneValid && isFieldsFilled && isPasswordMatch);
        btnRegister.setAlpha(btnRegister.isEnabled() ? 1.0f : 0.5f);
    }

    private void handleRegister() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String phone = etPhoneNumber.getText().toString().trim();

        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
            Toast.makeText(getActivity(), "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity(), task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(fullName).build();

                    // Gan callback de cho firebase sua xong ten moi sync ve backend
                    user.updateProfile(profileUpdates).addOnCompleteListener(profileTask -> {
                        if (profileTask.isSuccessful()) {
                            user.sendEmailVerification();
                            syncRegisteredUserWithBackend(user, fullName, phone);
                        }
                    });
                }
            } else {
                Toast.makeText(getActivity(), "Loi dang ky Firebase!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Ban API luu tai khoan moi ve MySQL chu khong de hoang nua
    private void syncRegisteredUserWithBackend(FirebaseUser firebaseUser, String name, String phone) {
        User reqUser = new User();
        reqUser.setFirebaseUid(firebaseUser.getUid());
        reqUser.setFullName(name);
        reqUser.setEmail(firebaseUser.getEmail());
        reqUser.setPhoneNumber(phone);

        executorService.execute(() -> {
            try {
                Response<User> response = RetrofitClient.getApiService().syncUser(reqUser).execute();
                mainHandler.post(() -> {
                    if (response.isSuccessful() && response.body() != null) {
                        User resUser = response.body();
                        userDatastore.saveUser(resUser.getIdUser(), resUser.getFirebaseUid(), resUser.getFullName(), resUser.getEmail(), resUser.getRoleId());
                        Toast.makeText(getActivity(), "Dang ky hoan tat!", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        requireActivity().finish();
                    }
                });
            } catch (Exception e) {
                mainHandler.post(() -> Toast.makeText(getActivity(), "Loi sync backend!", Toast.LENGTH_SHORT).show());
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}