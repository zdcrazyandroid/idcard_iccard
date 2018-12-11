package twoinonecard.com.readCard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.by100.util.AppConfigs;
import com.byid.android.IDCard;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import twoinonecard.com.R;
import twoinonecard.com.util.FileStorageHelper;
import twoinonecard.com.util.ReadUtil;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.bt_init)
    Button btInit;
    @BindView(R.id.iv_card)
    ImageView cardPhoto;
    @BindView(R.id.info_tv)
    TextView cardText;
    @BindView(R.id.bt_read)
    Button btRead;
    @BindView(R.id.bt_write)
    Button btWrite;
    private ReadUtil readUtil;
    private boolean isRun = true;
    private int isRead = 0;
    private IDCard idCard;
    private SharedPreferences sp;

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                refreshIDcardInfoUI();
            }

        }
    };

    private void refreshIDcardInfoUI() {
        if (idCard != null) {
            cardPhoto.setImageBitmap(BitmapFactory.decodeFile(idCard.getIdPhoto()));
            cardText.setText("姓名：" + idCard.getIdName() + "\n" +
                    "性别：" + idCard.getIdSex() + "\n" +
                    "民族：" + idCard.getIdNation() + "\n" +
                    "出生年月：" + idCard.getIdDate() + "\n" +
                    "居住地：" + idCard.getIdAddress() + "\n" +
                    "身份证号：" + idCard.getIdNum() + "\n" +
                    "签发机关：" + idCard.getOffice() + "\n" +
                    "有效期：" + idCard.getCreateTime() + "-" + idCard.getValidDate() + "\n");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        ((Button) findViewById(R.id.bt_init)).setOnClickListener(this);
        ((Button) findViewById(R.id.bt_read)).setOnClickListener(this);
        ((Button) findViewById(R.id.bt_write)).setOnClickListener(this);

        initCrad();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_init:
                initCrad();
                break;
            case R.id.bt_read:
                doRead();
                break;
            case R.id.bt_write:
                doWrirte();
                break;
        }

    }

    private void doWrirte() {

    }

    private void doRead() {

    }

    private void initCrad() {
        sp = getSharedPreferences("setting", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("port", "/dev/ttyS4");
        edit.apply();

        FileStorageHelper.copyFilesFromRaw(this, R.raw.base, "base.dat", AppConfigs.RootFile);
        FileStorageHelper.copyFilesFromRaw(this, R.raw.license, "license.lic", AppConfigs.RootFile);
        //端口
        readUtil = new ReadUtil(this, 0);


        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        singleThreadExecutor.execute(new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRun) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (readUtil.isPort() && isRead == 1) {
                        readUtil.startProtocol();//要同时轮询身份证和IC卡，才要在读身份证前面加上这个方法
                        idCard = readUtil.readCard();
                        if (idCard != null) {
                            mHandler.sendEmptyMessage(1);
                        }
                    }
                }
            }
        }));
    }
}
