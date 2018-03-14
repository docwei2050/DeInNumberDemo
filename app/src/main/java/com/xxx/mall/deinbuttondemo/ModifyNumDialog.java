package com.xxx.mall.deinbuttondemo;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by git on 2018/3/13.
 */

public class ModifyNumDialog extends DialogFragment implements View.OnClickListener {

    private static final String ARG_MAX       = "maxValue";
    private static final String ARG_CURRENT   = "currentValue";
    private static final int    DEFEALT_VALUE = 1;
    private int       mMaxValue;
    private int       mCurrentValue;
    private ImageView mIv_de;
    private ImageView mIv_in;
    private EditText  mEt_number;
    private Handler   mHandler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState)
    {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        mMaxValue = getArguments().getInt(ARG_MAX);
        mCurrentValue = getArguments().getInt(ARG_CURRENT);
        View view = inflater.inflate(R.layout.dialog_modify_number, container, false);
        initData(view);
        setCancelable(false);
        return view;
    }

    public static ModifyNumDialog newInstance(int max, int cur) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_MAX, max);
        bundle.putInt(ARG_CURRENT, cur);
        ModifyNumDialog dialog = new ModifyNumDialog();
        dialog.setArguments(bundle);
        return dialog;
    }
    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.75), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    private void initData(View view) {
        mIv_de = view.findViewById(R.id.number_de_iv);
        mIv_in = view.findViewById(R.id.number_in_iv);
        mEt_number = view.findViewById(R.id.number_et);
        mEt_number.setText(String.valueOf(mCurrentValue));
        Button btn_cancel = view.findViewById(R.id.btn_cancel);
        Button btn_sure   = view.findViewById(R.id.btn_sure);

        mIv_de.setOnClickListener(this);
        mIv_in.setOnClickListener(this);
        mEt_number.setFocusable(true);
        mEt_number.requestFocus();

        //弹出输入法
        mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showInputMethod(getActivity(), mEt_number);
            }
        }, 100);

        mEt_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String value = s.toString();

                //清空了输入框
                if(TextUtils.isEmpty(value)){
                    mCurrentValue=0;
                }else{
                    mCurrentValue=Integer.parseInt(value);
                    if (mCurrentValue > mMaxValue) {
                        mCurrentValue = mMaxValue;
                        mEt_number.setText(String.valueOf(mMaxValue));
                        mEt_number.setSelection(mEt_number.getText().toString().length());
                    }else{
                        mEt_number.setSelection(value.length());
                    }
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideKeyboard(mEt_number);
                dismiss();

            }
        });
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mModifyNumberListener != null) {
                    mModifyNumberListener.getModifyValue(mEt_number.getText()
                                                                   .toString()
                                                                   .trim());
                }
                HideKeyboard(mEt_number);
                dismiss();

            }
        });
    }


    public void showInputMethod(Context context, View view) {
        InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.showSoftInput(view, 0);
    }

    //隐藏虚拟键盘
    public void HideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext()
                                                       .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }

    public IModifyNumberListener mModifyNumberListener;

    public void setModifyNumberListener(IModifyNumberListener listener) {
        mModifyNumberListener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.number_de_iv:
                if (mCurrentValue > DEFEALT_VALUE) {
                    mCurrentValue--;
                } else {
                    mCurrentValue = DEFEALT_VALUE;
                }
                break;
            case R.id.number_in_iv:
                if (mCurrentValue < mMaxValue) {
                    mCurrentValue++;
                } else {
                    mCurrentValue = mMaxValue;
                    Toast.makeText(getActivity(), "亲， 该宝贝不能购买更多哦！", Toast.LENGTH_SHORT)
                         .show();
                }
                break;
        }
        mIv_de.setEnabled(mCurrentValue != DEFEALT_VALUE && mCurrentValue != 0);
        mEt_number.setText(String.valueOf(mCurrentValue));
    }

    public interface IModifyNumberListener {
        void getModifyValue(String value);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
