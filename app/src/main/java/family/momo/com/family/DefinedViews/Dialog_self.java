package family.momo.com.family.DefinedViews;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import family.momo.com.family.R;

public class Dialog_self extends Dialog {

    final int RIGHT = 0;
    final int WRONG = -1;
    final int UNFOUND = 1;
    final int ALREADY = 2;
    final int SUCCESS = 3;
    Handler handler;
    //上下文
    Activity context;
    String msg;

    //弹窗按钮组件
    TextView dialog_btn_know,dialog_text_msg;

    public Dialog_self(@NonNull Context context) {
        super(context);
        this.context = (Activity) context;
    }
    public void setMsg(String msg){
        this.msg = msg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //指定布局
        setContentView(R.layout.dialog_self);
        dialog_text_msg = findViewById(R.id.dialog_text_msg);
        dialog_btn_know = findViewById(R.id.dialog_btn_know);
        dialog_text_msg.setText(msg);

        //获取窗口
        final Window window = this.getWindow();
        WindowManager w = context.getWindowManager();
        //获取屏幕宽、高
        Display d = w.getDefaultDisplay();
        //获取当前对话框参数值
        WindowManager.LayoutParams p = window.getAttributes();
        //设置对话框宽度为屏幕0.8
        //p.height = (int)(d.getHeight()*0.8);
        p.width = (int)(d.getWidth()*0.9);
        window.setAttributes(p);

        //设置是否可以点击空白处取消
        this.setCancelable(true);
        dialog_btn_know.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
