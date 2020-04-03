package com.example.cab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class CustomerLoginRegisterActivity extends AppCompatActivity {


    private Button CustomerLoginButton;
    private Button CustomerRegisterButton;
    private TextView CustomerRegisterLink;
    private TextView CustomerStatus;
    private EditText EmailCustomer;
    private EditText PasswordCustomer;
    private ProgressDialog loadingBar;

    private FirebaseAuth mAuth;
    private DatabaseReference CustomerDatabaseRef;
    private String onlineCustomerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login_register);

        mAuth = FirebaseAuth.getInstance();



        CustomerLoginButton = (Button) findViewById(R.id.customer_login_button);
        CustomerRegisterButton = (Button) findViewById(R.id.customer_register_button);
        CustomerRegisterLink = (TextView) findViewById(R.id.dont_have_account_link);
        CustomerStatus = (TextView) findViewById(R.id.customer_login);
        EmailCustomer = (EditText) findViewById(R.id.customer_login_email);
        PasswordCustomer = (EditText) findViewById(R.id.customer_login_password);
        loadingBar = new ProgressDialog(this);


        CustomerRegisterButton.setVisibility(View.INVISIBLE);
        CustomerRegisterButton.setEnabled(false);


        CustomerRegisterLink.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                CustomerLoginButton.setVisibility(View.INVISIBLE);
                CustomerRegisterLink.setVisibility(View.INVISIBLE);
                CustomerStatus.setText("Register Customer");

                CustomerRegisterButton.setVisibility(View.VISIBLE);
                CustomerRegisterButton.setEnabled(true);

            }
        });

        CustomerRegisterButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String email = EmailCustomer.getText().toString();
                String password = PasswordCustomer.getText().toString();

                RegisterCustomer(email, password);

            }
        });

        CustomerLoginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String email = EmailCustomer.getText().toString();
                String password = PasswordCustomer.getText().toString();


                LoginCustomer(email, password);

            }
        });

    }

    private void LoginCustomer(String email, String password)
    {
        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(CustomerLoginRegisterActivity.this, "Please write your email...", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(CustomerLoginRegisterActivity.this, "Please write your password...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Customer login");
            loadingBar.setMessage("Please wait a while...");
            loadingBar.show();


            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful())
                            {
                                Intent customerLoginIntent = new Intent(CustomerLoginRegisterActivity.this, CustomerMapActivity.class);
                                startActivity(customerLoginIntent);

                                Toast.makeText(CustomerLoginRegisterActivity.this, "Customer login successful...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                            else
                            {
                                Toast.makeText(CustomerLoginRegisterActivity.this, "Login unsuccessful...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }

                        }
                    });

        }
    }

    private void RegisterCustomer(String email, String password)
    {
        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(CustomerLoginRegisterActivity.this, "Please write your email...", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(CustomerLoginRegisterActivity.this, "Please write your password...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Customer registration");
            loadingBar.setMessage("Please wait a while...");
            loadingBar.show();


            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful())
                            {

                                onlineCustomerID = mAuth.getCurrentUser().getUid();
                                CustomerDatabaseRef = FirebaseDatabase.getInstance().getReference()
                                        .child("Users").child("Customers").child(onlineCustomerID);

                                CustomerDatabaseRef.setValue(true);

                                Intent customerIntent = new Intent(CustomerLoginRegisterActivity.this,
                                        CustomerMapActivity.class);
                                startActivity(customerIntent);


                                Toast.makeText(CustomerLoginRegisterActivity.this, "Customer registration successful...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                            else
                                {
                                    Toast.makeText(CustomerLoginRegisterActivity.this, "Registration unsuccessful...", Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();
                                }

                        }
                    });

        }
    }
}
