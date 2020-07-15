package com.lavan.pdfviewer;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.shockwave.pdfium.PdfDocument;

import java.io.File;

public class PdfViewActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener
        , OnPageErrorListener {

    private static final String TAG = PdfViewActivity.class.getSimpleName();
    private String FILE_PATH = "";
    private Uri FILE_URI;
    private Integer pageNumber = 0;
    private String pdfFileName;
    private PDFView pdfView;
    private ImageView ivBack;
    private TextView tv_title_fv;
    private Button tv_title_fv_pdfadd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);

        pdfView = findViewById(R.id.pdfView);
        ivBack = (ImageView) findViewById(R.id.iv_back_pdfview);
        tv_title_fv = findViewById(R.id.tv_title_fv_pdfview);
        findViewById(R.id.tv_title_fv_pdfadd).setVisibility(View.INVISIBLE);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            FILE_PATH = bundle.getString("FilePath", "");
        //ILE_PATH = bundle.getString("FilePath", "");

        FILE_URI = Uri.fromFile(new File(FILE_PATH));
        displayFromUri(FILE_URI);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void displayFromUri(Uri uri) {
        pdfFileName = getFileName(uri);
        pdfView.fromUri(uri)
                .defaultPage(pageNumber)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .spacing(10) // in dp
                .onPageError(this)
                .load();
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        tv_title_fv.setText(String.format("%s (%s / %s)", pdfFileName, page + 1, pageCount));
        //setTitle(String.format("%s (%s / %s)", pdfFileName, page + 1, pageCount));
    }

    @Override
    public void loadComplete(int nbPages) {
      /*  PdfDocument.Meta meta = pdfView.getDocumentMeta();
        Log.e(TAG, "title = " + meta.getTitle());
        Log.e(TAG, "author = " + meta.getAuthor());
        Log.e(TAG, "subject = " + meta.getSubject());
        Log.e(TAG, "keywords = " + meta.getKeywords());
        Log.e(TAG, "creator = " + meta.getCreator());
        Log.e(TAG, "producer = " + meta.getProducer());
        Log.e(TAG, "creationDate = " + meta.getCreationDate());
        Log.e(TAG, "modDate = " + meta.getModDate());*/

        // printBookmarksTree(pdfView.getTableOfContents(), "-");
    }

    @Override
    public void onPageError(int page, Throwable t) {

        String message = t.getMessage();
        Toast.makeText(PdfViewActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
