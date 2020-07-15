package com.lavan.pdfviewer;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ShowPdfListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    PdfAdapter mAdapter;
    TextView tv_no_files_found, tv_select_pdf_title;
    int fileSize;
    String fileName;
    PdfFile pdfFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pdf_list);
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_pdf);
        tv_no_files_found = (TextView) findViewById(R.id.tv_no_files_found);
        tv_select_pdf_title = (TextView) findViewById(R.id.tv_select_pdf_title);
        ImageView iv_back_cpdf = (ImageView) findViewById(R.id.iv_back_cpdf);
        ImageView iv_refresh = (ImageView) findViewById(R.id.iv_refresh);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            fileSize = bundle.getInt("size");
            fileName = bundle.getString("filename");
        }
      //  tv_select_pdf_title.setText("Select " + fileName);

        if (Constants.pdfFileArrayList == null) {
            Constants.pdfFileArrayList = new ArrayList<>();
        }
        mAdapter = new PdfAdapter(Constants.pdfFileArrayList, ShowPdfListActivity.this);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                pdfFile = Constants.pdfFileArrayList.get(position);


                    Intent intent = new Intent(ShowPdfListActivity.this, PdfViewActivity.class);
                    intent.putExtra("FileName", pdfFile.getName());
                    intent.putExtra("FilePath", pdfFile.getPath());
                    startActivityForResult(intent, 123);
                  /*  Intent i = new Intent();
                    i.putExtra("FileName", pdfFile.getName());
                    i.putExtra("FilePath", pdfFile.getPath());
                    setResult(RESULT_OK, i);
                    finish();*/

            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
        if (Constants.pdfFileArrayList.size() == 0) {
            new ReadPdfFilesTask().execute();
        } else {
            tv_no_files_found.setVisibility(View.GONE);
        }
        iv_back_cpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ReadPdfFilesTask().execute();
            }
        });
        requestPermissions();
    }


    public class ReadPdfFilesTask extends AsyncTask<String, Void, String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Constants.pdfFileArrayList.clear();
            pd = new ProgressDialog(ShowPdfListActivity.this);
            pd.setMessage("Fetching files...");
            pd.setIndeterminate(false);
            pd.setCancelable(false);
            pd.show();
        }

        public ReadPdfFilesTask() {

        }

        @Override
        protected String doInBackground(String... strings) {
            initList(Constants.RootFolder);
            return "";
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            pd.dismiss();
            if (Constants.pdfFileArrayList.size() == 0) {
                tv_no_files_found.setVisibility(View.VISIBLE);
            } else {
                tv_no_files_found.setVisibility(View.GONE);
            }
            mAdapter.notifyDataSetChanged();

        }
    }

    //initializing the ArrayList
    private void initList(String path) {
        try {
            File file = new File(path);
            File[] fileArr = file.listFiles();
            String fileName;
            for (File file1 : fileArr) {
                if (file1.isDirectory()) {
                    initList(file1.getAbsolutePath());
                } else {
                    fileName = file1.getName();
                    //choose only the pdf files

                    if (fileName.endsWith(".pdf")) {
                        long length = file1.length();
                        String Size = getSize(file1.length());
                        Constants.pdfFileArrayList.add(new PdfFile(fileName, file1.getAbsolutePath(), Size, length));
                    }
                }
            }
        } catch (Exception e) {
            Log.i("show", Constants.error_snthing);
        }
        //return Constants.pdfFileArrayList;
    }

    public String getSize(long size) {
        String hrSize = "";
        double m = size / 1024.0;
        DecimalFormat dec = new DecimalFormat("0.00");
        if (m > 10) {
            m = m / 1024.0;
            hrSize = dec.format(m).concat(" MB");
        } else {
            hrSize = dec.format(size).concat(" KB");
        }
        return hrSize;
    }



    private  void  requestPermissions()
    {
        if (ActivityCompat.checkSelfPermission(ShowPdfListActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                ActivityCompat.checkSelfPermission(ShowPdfListActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(ShowPdfListActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length <= 0) {
                requestPermissions();
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new ReadPdfFilesTask().execute();
            }
        }
    }
}
