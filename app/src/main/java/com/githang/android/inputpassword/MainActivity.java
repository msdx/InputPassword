package com.githang.android.inputpassword;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.githang.android.inputpassowrd.AuthDialog;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final AuthDialog dialog = new AuthDialog(this);
        dialog.setOnEnterListener(new AuthDialog.OnEnterListener() {
            @Override
            public void onInput(AuthDialog dialog, String input) {
                if(input.equals("135795")) {
                    dialog.dismiss();
                } else {
                    dialog.showError("密码是135795");
                }
            }
        });
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

}
