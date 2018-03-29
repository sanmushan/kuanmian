package com.benxiang.noodles.utils;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import com.benxiang.noodles.AppApplication;

/**
 * Created by Tairong Chan on 2017/3/3.
 * Connect:
 */

public class TTSHelper {

  public static final String TTS_ENGINE_IFLYTEK = "com.iflytek.tts";
  private static TextToSpeech textToSpeech;
  private static boolean inited = false;

  public static void init(Context context, String engine) {
    TextToSpeech.OnInitListener onInitListener = new TextToSpeech.OnInitListener() {
      @Override
      public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
          inited = true;
        }
      }
    };
    textToSpeech = new TextToSpeech(context.getApplicationContext(), onInitListener, engine);
  }

  public static void speak(String string) {
    if (textToSpeech == null && !inited) {
      TTSHelper.init(AppApplication.getAppContext(),TTSHelper.TTS_ENGINE_IFLYTEK);
    }
    if (textToSpeech.isSpeaking()) {
      textToSpeech.stop();
    }
    textToSpeech.speak(string, TextToSpeech.QUEUE_ADD, null);
//    textToSpeech.speak(string, TextToSpeech.QUEUE_FLUSH, null);QUEUE_ADD
  }

  public static void stop() {
    if (textToSpeech == null && !inited) return;
    if (textToSpeech.isSpeaking()) {
      textToSpeech.stop();
    }
  }


  /*public static final String TTS_ENGINE_IFLYTEK = "com.iflytek.tts";
  private static TextToSpeech textToSpeech;
  private static boolean inited = false;

  public static void init(Context context, String engine) {
    TextToSpeech.OnInitListener onInitListener = new TextToSpeech.OnInitListener() {
      @Override
      public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
          inited = true;
        }
      }
    };
    textToSpeech = new TextToSpeech(context.getApplicationContext(), onInitListener, engine);
  }

  public static void speak(String string) {
    if (textToSpeech == null && !inited) return;
    if (textToSpeech.isSpeaking()) {
      textToSpeech.stop();
    }
    textToSpeech.speak(string, TextToSpeech.QUEUE_ADD, null);
//    textToSpeech.speak(string, TextToSpeech.QUEUE_FLUSH, null);QUEUE_ADD
  }

  public static void stop() {
    if (textToSpeech == null && !inited) return;
    if (textToSpeech.isSpeaking()) {
      textToSpeech.stop();
    }
  }*/
}
