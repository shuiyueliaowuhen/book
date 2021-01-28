package com.sf.bookapplaction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.TextView;

import com.sf.bookapplaction.view.BookPageBezierHelper;
import com.sf.bookapplaction.view.BookPageView;

import java.io.File;
import java.io.IOException;

public class BookActivity extends AppCompatActivity {

    public static final String FILE_PATH = "file_path";

    private BookPageView mBookPageView;
    private TextView mProgressTextView;
    private int mWidth;
    private int mHeight;
    private Bitmap mCurrentPageBitmap;
    private Bitmap mNextPageBitmap;
    private String mFilePath;
    private int mTotalLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        // fullscreen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }


        if (getIntent() != null) {
            mFilePath = getIntent().getStringExtra(FILE_PATH);
            if(!TextUtils.isEmpty(mFilePath)){
                mTotalLength = (int) new File(mFilePath).length();
            }
        } else {
            // todo  can not find the book path
        }

        // init view
        mBookPageView = findViewById(R.id.book_page_view);
        mProgressTextView = findViewById(R.id.progress_text_view);

        // get size
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mWidth = displayMetrics.widthPixels;
        mHeight = displayMetrics.heightPixels;

        // set book helper
        BookPageBezierHelper mHelper = new BookPageBezierHelper(mWidth, mHeight);
        mBookPageView.setBookPageBezierHelper(mHelper);

        // current page , next page
        mCurrentPageBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        mNextPageBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        mBookPageView.setBitmaps(mCurrentPageBitmap, mNextPageBitmap);


        // open book
        if (!TextUtils.isEmpty(mFilePath)) {
            try {
                mHelper.openBook(mFilePath);
                mHelper.draw(new Canvas(mCurrentPageBitmap));
                mBookPageView.invalidate();
            } catch (IOException e) {
                e.printStackTrace();
                // todo  can not find the book path
            }
        } else {
            // todo  can not find the book path
        }
    }

    public static void start(Context context, String filePath) {
        Intent intent = new Intent(context, BookActivity.class);
        intent.putExtra(FILE_PATH, filePath);
        context.startActivity(intent);

    }
}
