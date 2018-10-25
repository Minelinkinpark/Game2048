package game2048.jungle.com.game2048;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;

import android.widget.TextView;


public class Card extends FrameLayout {

    private TextView label;

    public Card(Context context){
        super(context);

        label = new TextView(getContext());
        label.setTextSize(32);
        label.setGravity(Gravity.CENTER);//使得生成的卡片位于中间
        label.setBackgroundColor(0x33ffffff);

        LayoutParams lp = new LayoutParams(-1,-1);//-1 -1 就是填充满父容器
        lp.setMargins(15,15,0,0);
        addView(label,lp);
        setNum(0);
    }

    private int num = 0;

    public int getNum(){
        return num;
    }

    public void setNum(int num){
        this.num = num;
        label.setBackgroundColor(getBackground(num));
        if(num<=0){
            label.setText("");
        }else{
            label.setText(num+"");
        }
    }

    private int getBackground(int num) {
        int bgcolor ;
        switch (num) {
            case 0:
                bgcolor = 0xffCCC0B3;
                break;
            case 2:
                bgcolor = 0xffEEE4DA;
                break;
            case 4:
                bgcolor = 0xffEDE0C8;
                break;
            case 8:
                bgcolor = 0xffF2B179;// #F2B179
                break;
            case 16:
                bgcolor = 0xffF49563;
                break;
            case 32:
                bgcolor = 0xffF5794D;
                break;
            case 64:
                bgcolor = 0xffF55D37;
                break;
            case 128:
                bgcolor = 0xffEEE863;
                break;
            case 256:
                bgcolor = 0xffEDB04D;
                break;
            case 512:
                bgcolor = 0xffECB04D;
                break;
            case 1024:
                bgcolor = 0xffEB9437;
                break;
            case 2048:
                bgcolor = 0xffEA7821;
                break;
            default:
                bgcolor = 0xffEA7821;
                break;
        }
        return bgcolor;
    }



    //判断数字是否相同是否可以重叠
    public boolean equals(Card o) {
        return getNum() == o.getNum();
    }
}
