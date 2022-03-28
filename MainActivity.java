package com.example.mybrowser;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    ImageButton btnrefresh, btnmic, searchbtn, back;
    ImageButton more, forward, stop, refbtn, home, btnBookmarks;
    Button btnForTab;
    EditText edittexturl;
    WebView webView;
    ProgressBar progressBar;
    private int desktopmodesvalue=0;
    MydbHandler dbHandler;
    myDbHandlerBook dbHandlerBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webView);
        btnmic = findViewById(R.id.btnmic);
        btnrefresh = findViewById(R.id.btnrefresh);
        edittexturl = findViewById(R.id.edittexturl);
        searchbtn = findViewById(R.id.searchbtn);
        back = findViewById(R.id.back);
        forward = findViewById(R.id.forward);
        stop = findViewById(R.id.stop);
        refbtn = findViewById(R.id.refbtn);
        home = findViewById(R.id.home);
        more = findViewById(R.id.more);
        btnForTab = findViewById(R.id.btnTextViewForTab);
        btnBookmarks = findViewById(R.id.btnAddtobookmarks);
        progressBar = findViewById(R.id.ProgressBar);

        dbHandler= new MydbHandler(this, null, null, 1);
        dbHandlerBook= new myDbHandlerBook(this, null, null, 1);
        saveData();

        progressBar.setMax(100);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setDisplayZoomControls(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.loadUrl("https://googl.com");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                saveData();
                progressBar.setVisibility(View.VISIBLE);
            }
        });

        progressBar.setProgress(0);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                if (newProgress == 100)
                    progressBar.setVisibility(View.INVISIBLE);
                else
                    progressBar.setVisibility(View.VISIBLE);
                super.onProgressChanged(view, newProgress);
            }
        });

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!edittexturl.getText().toString().isEmpty()) {
                    String address = edittexturl.getText().toString();
                    if (address.contains("http") || address.contains("https")) {
                        if (address.contains(".com") || address.contains(".in")) {
                            webView.loadUrl(address);
                        } else {
                            webView.loadUrl("https://www.google.com/search?q=" + address.replace("http", "").replace("https", ""));
                        }
                    } else {
                        if (address.contains(".com") || address.contains(".net") || address.contains(".in")) {
                            webView.loadUrl("http://" + address);
                        } else {
                            webView.loadUrl("https://www.google.com/search?q=" + address.replace("http", "").replace("https", ""));
                        }
                    }
                }
            }
        });

        more.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                BottomSheetDialog sheetDialog = new BottomSheetDialog(MainActivity.this, R.style.BottomSheetDialogTheme);
                View sheetView = LayoutInflater.from(getApplicationContext())
                        .inflate(R.layout.reorderdialog, (LinearLayout) findViewById(R.id.dialog_container));
                sheetView.findViewById(R.id.filemenucancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sheetDialog.dismiss();
                    }
                });

                sheetView.findViewById(R.id.refresh).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MainActivity.this, "history done", Toast.LENGTH_SHORT).show();
                    }
                });

                sheetView.findViewById(R.id.history).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        webView.reload();
                        Intent his = new Intent(MainActivity.this,history.class);
                        startActivity(his);
                    }
                });

                sheetView.findViewById(R.id.setting).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MainActivity.this, "setting", Toast.LENGTH_SHORT).show();

                    }
                });

                sheetView.findViewById(R.id.favouritestar).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent book = new Intent(MainActivity.this, bookmarks.class);
                        startActivity(book);

                    }
                });

                sheetView.findViewById(R.id.download).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MainActivity.this, "download", Toast.LENGTH_SHORT).show();

                    }
                });

                sheetView.findViewById(R.id.desktopMode).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       if(desktopmodesvalue==0){
                           setDesktopMode(webView,true);
                       }else{
                           setDesktopMode(webView,false);
                           desktopmodesvalue=0;
                       }

                    }
                });

                sheetView.findViewById(R.id.ScanBarCode).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MainActivity.this, "ScanBarCode", Toast.LENGTH_SHORT).show();

                    }
                });

                sheetView.findViewById(R.id.exitbrowser).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AlertDialog.Builder(MainActivity.this)
                                .setIcon(R.drawable.ic_baseline_exit_to_app_24)
                                .setTitle("Exit")
                                .setMessage("Are you sure you want to exit")
                                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                }).setNeutralButton("Help", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(MainActivity.this, "Press yes to exit", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();

                    }
                });

                sheetView.findViewById(R.id.aboutDeveloper).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       Intent i = new Intent (MainActivity.this, AboutDeveloper.class);
                       startActivity(i);

                    }
                });
                sheetDialog.setContentView(sheetView);
                sheetDialog.show();
            }
        });

        btnForTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "btnForTab", Toast.LENGTH_SHORT).show();
            }
        });

        btnrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edittexturl.selectAll();
                edittexturl.setText("");

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                edittexturl.requestFocus();
            }
        });

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {

                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

                request.setMimeType(mimeType);
                String cookies = CookieManager.getInstance ().getCookie(url);
                request.addRequestHeader("cookie", cookies);

                request.addRequestHeader("User-Agent", userAgent);
                request.setDescription("Download File....");
                request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType));
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,URLUtil.guessFileName(url, contentDisposition, mimeType));
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
                Toast.makeText(MainActivity.this, "Downloading File", Toast.LENGTH_SHORT).show();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (webView.canGoBack()){
                    webView.goBack();
                }
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (webView.canGoForward()){
                    saveData();
                    webView.goForward();
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    webView.stopLoading();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.loadUrl("https://google.com");
            }
        });

        btnBookmarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressednew();
                Toast.makeText(MainActivity.this,"Page added to Bookmark",Toast.LENGTH_SHORT).show();
            }
        });

        if(getIntent().getStringExtra("urls")!=null){
            webView.loadUrl(getIntent().getStringExtra("urls"));
        }

        btnmic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speak();
            }
        });

        Uri uri = getIntent().getData();
        if(uri != null){
            String path = uri.toString();
            webView.loadUrl(path);
        }
    }

    private void onBackPressednew() {
        Websites web = new Websites(webView.getUrl());
        dbHandler.addUrl(web);
        saveData();
    }

    private void saveData() {
        Websites webv = new Websites(webView.getUrl());
        dbHandler.addUrl(webv);
    }


    private void speak() {
        Intent intent = new Intent (RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra (RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi!!! Speak something");

        try{
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        }catch (Exception e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    edittexturl.setText(result.get(0));
                    String urls = edittexturl.getText().toString();
                }
            }
        }
    }

        private void setDesktopMode (WebView webView,boolean enabled){
            String newUserAgent = webView.getSettings().getUserAgentString();
            if (enabled) {
                try {
                    String ua = webView.getSettings().getUserAgentString();
                    String androidDosString = webView.getSettings().getUserAgentString().substring(ua.indexOf("("), ua.indexOf(")") + 1);

                    newUserAgent = webView.getSettings().getUserAgentString().replace(androidDosString, "Lenovo");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                newUserAgent = null;
            }
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setDisplayZoomControls(true);
            webView.getSettings().setSupportMultipleWindows(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setUserAgentString(newUserAgent);
            webView.getSettings().setLoadWithOverviewMode(enabled);
            webView.reload();
        }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage(" Are you sure you wantto exit.");

            builder.setCancelable(false);
            builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.super.onBackPressed();
                }
            });
            builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
}
