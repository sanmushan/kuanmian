package com.benxiang.noodles.view.model;

/**
 * Created by Tairong Chan on 2017/2/10.
 * Connect:
 */

public class SoftButtonModel {

  public static final int TYPE_NUMBER = 1;
  public static final int TYPE_LETTER_DELETE = 2;


  public int type;

  public String text;

  public SoftButtonModel(int type, String text) {
    this.type = type;
    this.text = text;
  }
}
