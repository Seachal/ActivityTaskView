package cc.rome753.demo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import cc.rome753.seachal.FirstActivity;
import cc.rome753.seachal.SecondActivity;

/**
 * Created by rome753@163.com on 2017/3/31.
 * <p>
 * 测试  onNewIntent  方法的调用时机。
 * <p>
 * 以及 Android activity 的生命周期
 * <p>
 * 已经整理出笔记  http://note.youdao.com/noteshare?id=44b342e9570b3bc61f894a23d9934db6
 */

public abstract class BaseActivity extends AppCompatActivity {

    private String TAG;


    String[] NAMES = {
            "BROUGHT_TO_FRONT",
            "CLEAR_TASK",
            "CLEAR_TOP",
            "CLEAR_WHEN_TASK_RESET",
            "EXCLUDE_FROM_RECENTS",
            "FORWARD_RESULT",
            "LAUNCHED_FROM_HISTORY",
            "LAUNCH_ADJACENT",
            "MULTIPLE_TASK",
            "NEW_DOCUMENT",
            "NEW_TASK",
            "NO_ANIMATION",
            "NO_HISTORY",
            "NO_USER_ACTION",
            "PREVIOUS_IS_TOP",
            "REORDER_TO_FRONT",
            "RESET_TASK_IF_NEEDED",
            "RETAIN_IN_RECENTS",
            "SINGLE_TOP",
            "TASK_ON_HOME",
    };

    int[] FLAGS = {
            Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT,
            Intent.FLAG_ACTIVITY_CLEAR_TASK,
            Intent.FLAG_ACTIVITY_CLEAR_TOP,
            Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET,
            Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS,
            Intent.FLAG_ACTIVITY_FORWARD_RESULT,
            Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY,
            Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT,
            Intent.FLAG_ACTIVITY_MULTIPLE_TASK,
            Intent.FLAG_ACTIVITY_NEW_DOCUMENT,
            Intent.FLAG_ACTIVITY_NEW_TASK,
            Intent.FLAG_ACTIVITY_NO_ANIMATION,
            Intent.FLAG_ACTIVITY_NO_HISTORY,
            Intent.FLAG_ACTIVITY_NO_USER_ACTION,
            Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP,
            Intent.FLAG_ACTIVITY_REORDER_TO_FRONT,
            Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED,
            Intent.FLAG_ACTIVITY_RETAIN_IN_RECENTS,
            Intent.FLAG_ACTIVITY_SINGLE_TOP,
            Intent.FLAG_ACTIVITY_TASK_ON_HOME,
    };

    private LinearLayout llContainer;


    protected String getTAG() {
        return TAG;
    }

    protected void setTAG() {
        this.TAG = "#--#" + this.getClass().getSimpleName() + " :#------";
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTAG();
        Log.i(getTAG(), "onCreate()");
        init();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(getTAG(), "onRestart()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(getTAG(), "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(getTAG(), "onResume()");
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.i(getTAG(), "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(getTAG(), "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(getTAG(), "onDestroy()");
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i(getTAG(), "onNewIntent()");
        setIntent(new Intent(BaseActivity.this
                , FirstActivity.class));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(getTAG(), "onSaveInstanceState()");
    }


    protected void init() {
        setContentView(R.layout.activity_base);
        setTitle(getClass().getSimpleName());
        setActionBarBack();
        addCheckBoxes();
    }

    private void setActionBarBack() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void addCheckBoxes() {
        llContainer = (LinearLayout) findViewById(R.id.ll_container);
        for (String name : NAMES) {
            CheckBox cb = new CheckBox(this);
            cb.setText(name);
            llContainer.addView(cb);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void addFlags(Intent intent) {
        for (int i = 0; i < llContainer.getChildCount(); i++) {
            CheckBox cb = (CheckBox) llContainer.getChildAt(i);
            if (cb.isChecked()) {
                intent.addFlags(FLAGS[i]);
            }
        }
    }

    public void startFragmentActivity(View v) {
        Intent intent = new Intent(this, DemoFragmentActivity.class);
        addFlags(intent);
        startActivity(intent);
    }

    public void startDialogActivity(View v) {
        Intent intent = new Intent(this, DialogActivity.class);
        addFlags(intent);
        startActivity(intent);
    }

    public void startStandard(View v) {
        Intent intent = new Intent(this, StandardActivity.class);
        addFlags(intent);
        startActivity(intent);
    }

    public void startSingleTop(View v) {
        Intent intent = new Intent(this, SingleTopActivity.class);
        addFlags(intent);
        startActivity(intent);
    }

    public void startSingleTask(View v) {
        Intent intent = new Intent(this, SingleTaskActivity.class);
        addFlags(intent);
        startActivity(intent);
    }

    public void startSingleInstance(View v) {
        Intent intent = new Intent(this, SingleInstanceActivity.class);
        addFlags(intent);
        startActivity(intent);
    }


    public void startActivityFirst(View v) {
        Intent intent = new Intent(this, FirstActivity.class);
        addFlags(intent);
        startActivity(intent);
    }

    public void startActivitySecond(View v) {
        Intent intent = new Intent(this, SecondActivity.class);
        addFlags(intent);
        startActivity(intent);
    }


    public void backFirstActivity(View v) {
        Intent intent = new Intent(this, FirstActivity.class);
//        addFlags(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    public void showAlterDialog(View v) {


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("AlterDialog1").setMessage("AlterDialog2").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }


}
