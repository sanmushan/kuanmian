package com.benxiang.noodles.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.benxiang.noodles.R;

import timber.log.Timber;


//

/**
 * Created by Tairong Chan on 2017/3/17.
 * Connect:
 */

public class ErrorDialogFragment extends DialogFragment {

  public final static String TITLE = "title";
  public final static String BUTTON_TEXT = "button_text";
  public final static String EXCEPTION_TEXT = "exception_text";
  public final static String EXCEPTION_TEXT_SPANNED = "exception_isSpanned";
  public final static String CANCEL_BUTTON = "cancel_button";

  private String tipTitle;
  private String buttonText;
  private String exception;
  boolean isSpanned = false;
  boolean iscancel = false;
  public static ErrorDialogFragment newInstance(String title, String buttonText, String exception,boolean spanned,boolean cancel) {
    Bundle args = new Bundle();
    args.putString(TITLE,title);
    args.putString(BUTTON_TEXT, buttonText);
    args.putString(EXCEPTION_TEXT, exception);
    args.putBoolean(EXCEPTION_TEXT_SPANNED, spanned);
    args.putBoolean(CANCEL_BUTTON, cancel);
    ErrorDialogFragment fragment = new ErrorDialogFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Bundle arguments = getArguments();
    tipTitle = arguments.getString(TITLE);
    buttonText = arguments.getString(BUTTON_TEXT);
    exception = arguments.getString(EXCEPTION_TEXT);
    isSpanned=arguments.getBoolean(EXCEPTION_TEXT_SPANNED);
    iscancel=arguments.getBoolean(CANCEL_BUTTON);
      Timber.e("内容是"+exception);
      Timber.e("isSpanned内容是"+isSpanned);
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_error, null, false);
    builder.setView(view);
    final AlertDialog alertDialog = builder.create();
    Window window = alertDialog.getWindow();
    if (window != null) {
      window.setBackgroundDrawableResource(R.drawable.border_error_dialog);
    }
    ((TextView)view.findViewById(R.id.tv_title)).setText(tipTitle);
//    AutoSplitTextView exception_tip= (AutoSplitTextView) view.findViewById(R.id.tv_exception_tip);
    TextView exception_tip= (TextView) view.findViewById(R.id.tv_exception_tip);
//    exception_tip.setAutoSplitEnabled(true);
    if (isSpanned) {
//      Spanned spanned = (Spanned) exception;
      exception_tip.setText(Html.fromHtml(exception));
    }else {
      exception_tip.setText( exception);
    }
//    ((AutoSplitTextView)view.findViewById(R.id.取消)).setText(exception);
    Button error = (Button) view.findViewById(R.id.btn_error);
    Button cancel = (Button) view.findViewById(R.id.btn_cancel);
    cancel.setText("取消");
    if (iscancel){
      cancel.setVisibility(View.VISIBLE);
    }
    cancel.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ErrorDialogFragment.this.dismiss();
      }
    });
    error.setText(buttonText);
    error.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (listener != null) {
          listener.onClick(ErrorDialogFragment.this);
        }
        ErrorDialogFragment.this.dismiss();
      }
    });
    alertDialog.setCanceledOnTouchOutside(false);
    return alertDialog;
  }

  private OnErrorClickListener listener;

  public void setOnErrorClickListener(OnErrorClickListener listener) {
    this.listener = listener;
  }

  public interface OnErrorClickListener {
    void onClick(ErrorDialogFragment dialog);
  }
}
