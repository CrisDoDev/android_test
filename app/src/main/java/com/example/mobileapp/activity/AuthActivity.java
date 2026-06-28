package com.example.mobileapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileapp.R;
import com.example.mobileapp.api.RetrofitClient;
import com.example.mobileapp.database.UserDatastore;
import com.example.mobileapp.fragment.LoginFragment;
import com.example.mobileapp.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class AuthActivity extends AppCompatActivity {

    private static final String TAG = "AuthActivity";
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private UserDatastore userDatastore;

    // Dung threadpool de chay API ngam
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    try {
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        firebaseAuthWithGoogle(account.getIdToken());
                    } catch (ApiException e) {
                        Toast.makeText(AuthActivity.this, "Loi link Google: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mAuth = FirebaseAuth.getInstance();
        userDatastore = new UserDatastore(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.auth_fragment_container, new LoginFragment())
                    .commit();
        }
    }

    public GoogleSignInClient getGoogleSignInClient() { return mGoogleSignInClient; }
    public ActivityResultLauncher<Intent> getSignInLauncher() { return signInLauncher; }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) syncGoogleUserWithBackend(user);
            } else {
                Toast.makeText(AuthActivity.this, "Firebase tu choi Google!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Dung luon doi tuong User de truyen len api dong bo ngam
    private void syncGoogleUserWithBackend(FirebaseUser firebaseUser) {
        User reqUser = new User();
        reqUser.setFirebaseUid(firebaseUser.getUid());
        reqUser.setFullName(firebaseUser.getDisplayName() != null ? firebaseUser.getDisplayName() : "Google User");
        reqUser.setEmail(firebaseUser.getEmail());
        reqUser.setPhoneNumber(firebaseUser.getPhoneNumber() != null ? firebaseUser.getPhoneNumber() : "");

        executorService.execute(() -> {
            try {
                Response<User> response = RetrofitClient.getApiService().syncUser(reqUser).execute();
                mainHandler.post(() -> {
                    if (response.isSuccessful() && response.body() != null) {
                        User resUser = response.body();
                        userDatastore.saveUser(resUser.getIdUser(), resUser.getFirebaseUid(), resUser.getFullName(), resUser.getEmail(), resUser.getPhoneNumber(), resUser.getRoleId());
                        Toast.makeText(AuthActivity.this, "Dang nhap Google thanh cong!", Toast.LENGTH_SHORT).show();
                        navigateToHome(resUser.getRoleId());
                    }
                });
            } catch (Exception e) {
                mainHandler.post(() -> Toast.makeText(AuthActivity.this, "Loi ket noi server backend!", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void navigateToHome(int roleId) {
        Intent intent = new Intent(AuthActivity.this, (roleId == 1) ? AdminMainActivity.class : MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void startGoogleSignIn() {
        signInLauncher.launch(mGoogleSignInClient.getSignInIntent());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}