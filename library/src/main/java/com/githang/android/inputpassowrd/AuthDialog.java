/*
 * Date: 14-5-16
 * Project: Parking Lay-by
 */
package com.githang.android.inputpassowrd;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.StyleRes;
import android.support.v7.widget.GridLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

/**
 * 授权对话框
 *
 * @author msdx (645079761@qq.com)
 */
public class AuthDialog {
    private static final Random random = new Random();
    private Context mContext;
    private Resources mResources;
    private TextView mTips;
    private EditText mPassword;
    private GridLayout mKeyboard;
    private String[] keyText = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    private Button[] mNumKeys;
    private Button mDelKey;
    private Button mEnterKey;
    private OnEnterListener mEnterListener;

    private int mMaxLength;

    private boolean mCustomTitleColor;

    private Dialog mDialog;
    private AlertDialog.Builder mBuilder;

    public AuthDialog(Context context) {
        this(context, 0);
    }

    @TargetApi(11)
    public AuthDialog(Context context, @StyleRes int theme) {
        mBuilder = new AlertDialog.Builder(context, theme);
        mContext = context;
        mResources = context.getResources();
        mMaxLength = Integer.MAX_VALUE;
        initViews();
    }

    private void initViews() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_auth, null);
        mTips = (TextView) view.findViewById(R.id.auth_tips);
        mPassword = (EditText) view.findViewById(R.id.auth_edit);
        mKeyboard = (GridLayout) view.findViewById(R.id.auth_keyboard);

        mKeyboard.setColumnCount(3);
        mKeyboard.setRowCount(4);

        setupNumberKey();
        setupDeleteKey();
        setupEnterKey();

        mBuilder.setTitle("请输入授权密码");
        mBuilder.setView(view);
        mDialog = mBuilder.create();

    }

    private void setTitleColor(int color) {
        final int titleId = mResources.getIdentifier("alertTitle", "id", "android");
        final View title = mDialog.findViewById(titleId);
        if (title != null) {
            ((TextView) title).setTextColor(color);
        }
    }

    /**
     * 修改对话框标题蓝色线的颜色为主题颜色.
     */
    private void setTitleLineColor(int color) {
        int dividerId = mResources.getIdentifier("android:id/titleDivider", null, null);
        View divider = mDialog.findViewById(dividerId);
        if(divider != null) {
            divider.setBackgroundColor(color);
        }
    }

    public void setMaxLength(int length) {
        mMaxLength = length;
    }

    public void setCustomTitleColor(boolean isCustomColor) {
        this.mCustomTitleColor = isCustomColor;
    }

    private void setupEnterKey() {
        mEnterKey = (Button) mKeyboard.getChildAt(11);
        mEnterKey.setText("确定");
        mEnterKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEnterListener != null) {
                    mEnterListener.onInput(AuthDialog.this, mPassword.getText().toString());
                }
            }
        });
    }

    private void setupDeleteKey() {
        mDelKey = (Button) mKeyboard.getChildAt(10);
        mDelKey.setText("删除");
        mDelKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTips.setText("");
                String result = mPassword.getText().toString();
                if (result.length() > 0) {
                    mPassword.getText().delete(result.length() - 1, result.length());
                }
            }
        });
    }

    private void setupNumberKey() {
        View.OnClickListener numListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPassword.length() < mMaxLength) {
                    mTips.setText("");
                    String result = ((TextView) v).getText().toString();
                    mPassword.append(result);
                }
            }
        };
        mNumKeys = new Button[keyText.length];
        for (int i = 0; i < keyText.length; i++) {
            mNumKeys[i] = (Button) mKeyboard.getChildAt(i);
            mNumKeys[i].setOnClickListener(numListener);
        }
    }

    private void disorderKey() {
        String tmp;
        for (int pos = keyText.length - 1; pos > 0; pos--) {
            int index = random.nextInt(pos);
            tmp = keyText[pos];
            keyText[pos] = keyText[index];
            keyText[index] = tmp;
        }
        for (int i = 0; i < keyText.length; i++) {
            mNumKeys[i].setText(keyText[i]);
        }
    }

    public void showError(String error) {
        mTips.setText(error);
    }

    public void show() {
        mPassword.getText().clear();
        mTips.setText("");
        disorderKey();
        mDialog.show();
        if(mCustomTitleColor) {
            int titleColor = mResources.getColor(R.color.auth_title);
            setTitleColor(titleColor);
            setTitleLineColor(titleColor);
        }
    }

    public void dismiss() {
        mDialog.dismiss();
    }

    public void setOnEnterListener(OnEnterListener listener) {
        mEnterListener = listener;
    }

    public interface OnEnterListener {
        void onInput(AuthDialog dialog, String input);
    }

}
