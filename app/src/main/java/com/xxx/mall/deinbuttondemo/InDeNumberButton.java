package com.xxx.mall.deinbuttondemo;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by git on 2018/3/13.
 */

public class InDeNumberButton extends LinearLayout implements View.OnClickListener {
    private static final int DEFEALT_VALUE    =1;//默认商品的值为1
    private static final int DEFEALT_MAXVALUE =10000;//默认商品的库存值为10000
    private ImageView mIv_de;
    private ImageView mIv_in;
    private EditText mEt_number;
    private int mMaxNumber= DEFEALT_MAXVALUE;
    private int mCurrentNumber=DEFEALT_VALUE;
    private boolean mIsSupport=true;//默认支持弹窗显示
    private Activity mActivity;
    public InDeNumberButton(Context context) {
        this(context,null);
    }

    public InDeNumberButton(Context context, @Nullable AttributeSet attrs)
    {
        this(context, attrs,0);
    }

    public InDeNumberButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);
        initContext(context);
    }

    public InDeNumberButton(Context context,
                            AttributeSet attrs,
                            int defStyleAttr,
                            int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);

    }

    private void initContext(Context context) {
        View      view   = LayoutInflater.from(context).inflate(R.layout.common_in_de_number, this, true);
        mIv_de = view.findViewById(R.id.number_de_iv);
        mIv_in = view.findViewById(R.id.number_in_iv);
        mEt_number = view.findViewById(R.id.number_et);
        mEt_number.setText(String.valueOf(mCurrentNumber));

        initEvent();
    }

    private void initEvent() {
        // 初始处理减号的显示
        mIv_de.setEnabled(mCurrentNumber!=DEFEALT_VALUE&&mCurrentNumber!=0);
        mEt_number.setFocusable(!mIsSupport);// 支持弹窗的话就不支持获取焦点
        mIv_de.setOnClickListener(this);
        mIv_in.setOnClickListener(this);
        mEt_number.setOnClickListener(this);

    }

    /**
     *  如果数量为1那么减号不可点击
     * @param number
     * @return
     */
    public InDeNumberButton setInitValue(int number){
        mCurrentNumber=number>0?number:mCurrentNumber;
        mEt_number.setText(String.valueOf(mCurrentNumber));
        mIv_de.setEnabled(mCurrentNumber!=DEFEALT_VALUE);
        return this;
    }
    public InDeNumberButton setMaxValue(int number){
        mMaxNumber=number;
        return this;
    }

    @Override
    public  void onClick(final View v) {
        synchronized (InDeNumberButton.class){
            switch (v.getId()){
                case R.id.number_de_iv:
                    // 大于1可以继续减下去
                    if(mCurrentNumber>DEFEALT_VALUE){
                        mCurrentNumber--;
                    }else{
                        mCurrentNumber=DEFEALT_VALUE;
                    }
                    break;
                case R.id.number_in_iv:
                    // 小于库存可以继续加上去
                    if(mCurrentNumber<mMaxNumber){
                        mCurrentNumber++;
                    }else{
                        mCurrentNumber=mMaxNumber;
                        Toast.makeText(getContext(),"亲， 该宝贝不能购买更多哦！",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.number_et:
                    //弹窗提示
                    if(mIsSupport){
                        // 弹窗显示
                        ModifyNumDialog dialog=ModifyNumDialog.newInstance(mMaxNumber,mCurrentNumber);
                        dialog.show(mActivity.getFragmentManager(),"modify_number");
                        dialog.setModifyNumberListener(new ModifyNumDialog.IModifyNumberListener() {
                            @Override
                            public void getModifyValue(String value) {
                                if(!TextUtils.isEmpty(value)) {
                                    mCurrentNumber = Integer.parseInt(value);
                                    mEt_number.setText(value);
                                }
                            }
                        });
                    }
                    break;
            }
            mIv_de.setEnabled(mCurrentNumber!=DEFEALT_VALUE);
            mEt_number.setText(String.valueOf(mCurrentNumber));
        }
    }

    public InDeNumberButton setDialogActivity(Activity activity){
        mActivity=activity;
        return this;
    }
    public int getCurrentValue(){
        return mCurrentNumber;
    }
}
