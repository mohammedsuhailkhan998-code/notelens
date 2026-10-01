package com.notelens.app;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.*;

public class MainActivity extends Activity {
    private static final int PICK_FILE = 100;
    private TextView status;
    private String fileName = "";

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        showHome();
    }

    private TextView text(String value, float size) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(Color.rgb(23,32,51));
        t.setPadding(0, 8, 0, 8);
        return t;
    }

    private Button button(String value) {
        Button b = new Button(this);
        b.setText(value);
        b.setAllCaps(false);
        return b;
    }

    private LinearLayout stat(String title, String value) {
        LinearLayout c = new LinearLayout(this);
        c.setOrientation(LinearLayout.VERTICAL);
        c.setGravity(Gravity.CENTER);
        c.setPadding(10, 12, 10, 12);

        GradientDrawable bg = new GradientDrawable();
        bg.setColor(Color.WHITE);
        bg.setCornerRadius(28);
        c.setBackground(bg);

        TextView a = text(title, 13);
        a.setTextColor(Color.DKGRAY);
        TextView b = text(value, 20);
        b.setTypeface(null, 1);
        c.addView(a);
        c.addView(b);

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0, -2, 1f);
        p.setMargins(5, 0, 5, 0);
        c.setLayoutParams(p);
        return c;
    }

    private void showHome() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(28, 32, 28, 24);
        root.setBackgroundColor(Color.rgb(245,247,251));

        TextView brand = text("NoteLens", 30);
        brand.setTextColor(Color.rgb(14,165,233));
        brand.setTypeface(null, 1);
        root.addView(brand);

        root.addView(text("Smart Study Assistant", 17));

        Space gap = new Space(this);
        root.addView(gap, new LinearLayout.LayoutParams(1, 18));

        LinearLayout stats = new LinearLayout(this);
        stats.setOrientation(LinearLayout.HORIZONTAL);
        stats.addView(stat("Materials", "0"));
        stats.addView(stat("Quiz Score", "0%"));
        stats.addView(stat("Streak", "0 days"));
        root.addView(stats);

        Space gap2 = new Space(this);
        root.addView(gap2, new LinearLayout.LayoutParams(1, 20));

        Button upload = button("📄  Upload Study Material");
        upload.setOnClickListener(v -> pickFile());
        root.addView(upload);

        status = text("No material selected. Choose a PDF or text file.", 16);
        root.addView(status);

        Button quiz = button("✨  Generate AI Quiz");
        quiz.setOnClickListener(v -> {
            if (fileName.isEmpty()) {
                Toast.makeText(this, "Upload a study material first.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Material selected. Connect your NoteLens AI endpoint to generate the quiz.", Toast.LENGTH_LONG).show();
            }
        });
        root.addView(quiz);

        TextView info = text("This is the native Android build of NoteLens. Your AI quiz backend can be connected without exposing an API key in the app.", 14);
        info.setTextColor(Color.DKGRAY);
        info.setPadding(0, 24, 0, 0);
        root.addView(info);

        setContentView(root);
    }

    private void pickFile() {
        Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        startActivityForResult(i, PICK_FILE);
    }

    @Override protected void onActivityResult(int request, int result, Intent data) {
        super.onActivityResult(request, result, data);
        if (request == PICK_FILE && result == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                String s = uri.getLastPathSegment();
                fileName = (s == null) ? "Study material" : s;
                if (status != null) status.setText("Selected: " + fileName + "\nTap Generate AI Quiz.");
            }
        }
    }
}
