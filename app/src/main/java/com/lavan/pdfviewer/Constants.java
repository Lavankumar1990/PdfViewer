package com.lavan.pdfviewer;

import android.os.Environment;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static List<PdfFile> pdfFileArrayList = new ArrayList<>();
    public static String RootFolder = Environment.getExternalStorageDirectory().getAbsolutePath();

    public static String error_snthing= "Something went wrong try agian";

}
