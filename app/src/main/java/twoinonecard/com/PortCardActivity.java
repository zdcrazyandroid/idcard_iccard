package twoinonecard.com;

import android.app.Activity;

import android_serialport_api.SerialPortFinder;
import butterknife.BindView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.byid.android.IDCard;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.ButterKnife;
import twoinonecard.com.util.ConvertToString;
import twoinonecard.com.util.ReadUtil;


public class PortCardActivity extends Activity {

    @BindView(R.id.imageView1)
    ImageView cardPhoto;
    @BindView(R.id.textView1)
    TextView cardText;
    @BindView(R.id.radioGroup)
    RadioGroup rg;
    @BindView(R.id.etId)
    EditText etId;
    @BindView(R.id.etBlock)
    EditText etBlock;
    @BindView(R.id.etBlock2)
    EditText etBlock2;
    @BindView(R.id.etBlock3)
    EditText etBlock3;
    @BindView(R.id.etBlock4)
    EditText etBlock4;
    @BindView(R.id.btBreak)
    Button btBreak;
    @BindView(R.id.btread)
    Button btread;
    @BindView(R.id.icread)
    Button icRead;
    @BindView(R.id.icwrite)
    Button icWrite;
    @BindView(R.id.icpassword)
    Button icPassword;
    @BindView(R.id.uidread)
    Button uidread;
    @BindView(R.id.text_cishu)
    TextView text_cishu;
    @BindView(R.id.emptyBt)
    Button emptyBt;

    private ReadUtil readUtil;
    private IDCard idCard;
    private int isRead = 0;
    private int typeIc = 0, block = 0;
    private List<Integer> spList;
    private boolean isRun = true;
    private SharedPreferences sp;
    private SharedPreferences.Editor ed;
    private int cishu = 0;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_port_card);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        int age = intent.getIntExtra("id", 0);
        readUtil = new ReadUtil(this, age);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // 获取选中的RadioButton的id
                if (group.getCheckedRadioButtonId() == R.id.typea) {
                    typeIc = 0;
                } else {
                    typeIc = 1;
                }
            }
        });

        Spinner spinner = findViewById(R.id.spDwon);
        spList = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            spList.add(i);
        }
        ArrayAdapter spAdapter = new ArrayAdapter<>(this, R.layout.custom_spiner_text_item, spList);
        spinner.setAdapter(spAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                block = spList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        clickButtonID();
        btread.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                switch (isRead) {
                    case 0:
                        clickButtonID();
                        break;
                    case 1:
                        clickButtonUID();
                        break;
                    case 2:
                        clickButtonIC();
                        break;
                }
            }
        });
        uidread.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                cardPhoto.setImageBitmap(null);
                readUtil.idUid();
                cardText.setText("身份证UID：" + readUtil.idUid());
            }
        });
        if (readUtil.isPort()) {
            readUtil.icType(typeIc);
            etId.setText(readUtil.icID());
            if (readUtil.icPassWord(block, etBlock4.getText().toString())) {
                etBlock.setText(readUtil.icReadData(block * 4).substring(0, 18));
                etBlock2.setText(readUtil.icReadData(block * 4 + 1).substring(0, 16));
                etBlock3.setText(readUtil.icReadData(block * 4 + 2));
            } else {
                Toast.makeText(PortCardActivity.this, "扇区密码不正确", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(PortCardActivity.this, "设备未连接", Toast.LENGTH_SHORT).show();
        }
        icRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (readUtil.isPort()) {
                    readUtil.icType(typeIc);
                    etId.setText(readUtil.icID());
                    if (readUtil.icPassWord(block, etBlock4.getText().toString())) {
                        etBlock.setText(readUtil.icReadData(block * 4).substring(0, 18));
                        etBlock2.setText(readUtil.icReadData(block * 4 + 1).substring(0, 16));
                        //  etBlock3.setText(readUtil.icReadData(block * 4 + 2));
                        etBlock4.setText(readUtil.icReadData(block * 4 + 3));
                        String name = readUtil.icReadData(block * 4 + 2);
                        int endIndex = name.indexOf("0000000000");
                        if (endIndex == -1) {
                            int endIndex2 = name.indexOf("0000");
                            if (endIndex2 != -1) {
                                name = name.substring(0, endIndex);
                                Log.d("zd------", name);
                                name = ConvertToString.convertGbkToString(name);
                            }
                        }
                        etBlock3.setText(name);
                    } else {
                        Toast.makeText(PortCardActivity.this, "扇区密码不正确", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PortCardActivity.this, "设备未连接", Toast.LENGTH_SHORT).show();
                }
            }
        });
        icWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (readUtil.isPort()) {
                    readUtil.icType(typeIc);
                    readUtil.icID();
                    if (readUtil.icPassWord(block, etBlock4.getText().toString())) {
                        readUtil.icWriteData(block * 4, etBlock.getText().toString() + "ffffffffffffff");
                        readUtil.icWriteData(block * 4 + 1, etBlock2.getText().toString() + "ffffffffffffffff");
                        String name = etBlock3.getText().toString();
                        Log.d("zd------", name);
                        String hex = toHexString(name);
                        Log.d("zd", "name.length():" + name.length());
                        Log.d("zd------", name);
                        readUtil.icWriteData(block * 4 + 2, hex);

                        Toast.makeText(PortCardActivity.this, "写卡成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PortCardActivity.this, "扇区密码不正确", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PortCardActivity.this, "设备未连接", Toast.LENGTH_SHORT).show();
                }
            }
        });
        icPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (readUtil.icWritePassword(block * 4 + 3, etBlock4.getText().toString())) {
                    Toast.makeText(PortCardActivity.this, "修改密码成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PortCardActivity.this, "修改密码失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btBreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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
//                                else {
//                                    readUtil.icType(0);
//                                    str = readUtil.icID();
//                                    if (str != null) {
//                                        mHandler.sendEmptyMessage(2);
//                                    }
//                                }
                    }
                }
            }
        }));
        emptyBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cishu = 0;
                text_cishu.setText("身份证读卡次数：" + cishu);
            }
        });
    }


    private String toHexString(String hexInput) {
        hexInput = ConvertToString.convertStringToGbk(hexInput);
        if (hexInput.length() > 0 && hexInput.length() < 32) {
            int[] ints = new int[32 - hexInput.length()];
            for (int i = 0; i < ints.length; i++) {
                ints[i] = 0;
            }
            hexInput += arrToString(ints);
        }

        return hexInput;
    }

    private String arrToString(int[] ints) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ints.length; i++) {
            sb.append(ints[i]);
        }
        return sb.toString();
    }

    private String str;

    @SuppressLint("SetTextI18n")
    private void refreshIDcardInfoUI() {
        if (idCard != null) {
            cishu++;
            text_cishu.setText("身份证读卡次数：" + cishu);
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

    @SuppressLint("SetTextI18n")
    private void clickButtonID() {
        icPassword.setTextColor(this.getResources().getColor(R.color.gray));
        icRead.setTextColor(this.getResources().getColor(R.color.gray));
        icWrite.setTextColor(this.getResources().getColor(R.color.gray));
        uidread.setTextColor(this.getResources().getColor(R.color.gray));
        icRead.setEnabled(false);
        icWrite.setEnabled(false);
        icPassword.setEnabled(false);
        uidread.setEnabled(false);
        isRead = 1;
        btread.setText("模式1：身份证读取");
    }

    @SuppressLint("SetTextI18n")
    private void clickButtonUID() {
        icPassword.setTextColor(this.getResources().getColor(R.color.gray));
        icRead.setTextColor(this.getResources().getColor(R.color.gray));
        icWrite.setTextColor(this.getResources().getColor(R.color.gray));
        uidread.setTextColor(this.getResources().getColor(R.color.back));
        icRead.setEnabled(false);
        icWrite.setEnabled(false);
        icPassword.setEnabled(false);
        uidread.setEnabled(true);
        isRead = 2;
        btread.setText("模式2：身份证UID读取");
    }

    @SuppressLint("SetTextI18n")
    private void clickButtonIC() {
        icPassword.setTextColor(this.getResources().getColor(R.color.back));
        icRead.setTextColor(this.getResources().getColor(R.color.back));
        icWrite.setTextColor(this.getResources().getColor(R.color.back));
        uidread.setTextColor(this.getResources().getColor(R.color.gray));
        icRead.setEnabled(true);
        icWrite.setEnabled(true);
        icPassword.setEnabled(true);
        uidread.setEnabled(false);
        isRead = 0;
        btread.setText("模式3：IC卡读取");
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                refreshIDcardInfoUI();
            } else if (msg.what == 2) {
                cardPhoto.setImageBitmap(null);
                cardText.setText("IC卡ID：" + str);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRun = false;
    }
}

