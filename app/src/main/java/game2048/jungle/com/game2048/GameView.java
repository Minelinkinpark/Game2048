package game2048.jungle.com.game2048;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

public class GameView extends GridLayout {

    private static GameView gameView = null;


    public static GameView getGameView() {
        return gameView;

    }



    private Card[][] cardsMap = new Card[4][4];
    private List<Point> emptyPoints = new ArrayList<Point>();

    public GameView(Context context) {
        super(context);
        gameView =this;
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        gameView =this;
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        gameView =this;
        initGameView();

    }

    //初始化Game界面
    private void initGameView(){

        setColumnCount(4);
        setBackgroundColor(0xffbbada0);
        addCards(GetCardWidth(),GetCardWidth());   //向布局中加入卡片

        setOnTouchListener(new OnTouchListener() {
            // 定义变量
            private float startX,startY,offsetX,offsetY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        offsetX = event.getX() - startX;
                        offsetY = event.getY() - startY;

                        if(Math.abs(offsetX) > Math.abs(offsetY)){
                            if(offsetX < -5){
                                slipLeft();
                            }
                            else if(offsetX > 5){
                                slipRight();
                            }
                        }
                        else{
                            if(offsetY < -5){
                                slipUp();
                            }
                            else if(offsetY > 5){
                                slipDown();
                            }
                        }

                        break;

                }
                return true;
            }
        });

    }




    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        StartGame();
    }


    private void addCards(int cardWidth, int cardHeight){
        Card c;
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                c = new Card(getContext());
                c.setNum(0);
                addView(c, cardWidth, cardHeight);
                cardsMap[x][y]=c;
            }

        }
    }

    private int GetCardWidth()
    {

        //屏幕信息的对象
        DisplayMetrics displayMetrics;
        displayMetrics = getResources().getDisplayMetrics();

        //获取屏幕信息
        int cardWidth;
        cardWidth = displayMetrics.widthPixels;

        //一行有四个卡片，每个卡片占屏幕的四分之一
        return ( cardWidth - 10 ) / 4;

    }



    //添加随机数
    private void addRandomNum(){

        emptyPoints.clear();

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if(cardsMap[x][y].getNum() <= 0){
                    emptyPoints.add(new Point(x,y));
                }

            }

        }

        Point p = emptyPoints.remove((int)(Math.random()*emptyPoints.size()));	//随机移除一个点
        cardsMap[p.x][p.y].setNum(Math.random()>0.1?2:4);

    }

    public void StartGame(){
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                cardsMap[x][y].setNum(0);
            }
        }

        MainActivity.getMainActivity().clearScore();

        addRandomNum();
        addRandomNum();

    }

    private void checkComplete(){

        boolean complete = true;

        ALL:
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if(cardsMap[x][y].getNum() == 0 ||
                        (x>0 && cardsMap[x][y].equals(cardsMap[x-1][y])) ||
                        (x<3 && cardsMap[x][y].equals(cardsMap[x+1][y])) ||
                        (y>0 && cardsMap[x][y].equals(cardsMap[x][y-1])) ||
                        (y<3 && cardsMap[x][y].equals(cardsMap[x][y+1]))){

                    complete = false;
                    break ALL;
                }

            }

        }
        if(complete){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("游戏结束").setMessage("你已经无路可走");
            builder.setPositiveButton("重新开始", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    StartGame();
                }
            });
            builder.setNegativeButton("退出游戏", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.getMainActivity().endGame();
                }
            });
            builder.create().show();
        }

    }



    private void slipLeft(){

        boolean merge = false;

        for(int y=0;y<4;y++){
            for (int x=0;x<4;x++){
                for(int x1=x+1;x1<4;x1++){//从横轴比较。因为是想左滑动
                    if(cardsMap[x1][y].getNum()>0){
                        if(cardsMap[x][y].getNum()<=0){
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                            cardsMap[x1][y].setNum(0);
                            x--;
                            merge = true;
                        }else if(cardsMap[x][y].equals(cardsMap[x1][y])){
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
                            cardsMap[x1][y].setNum(0);
                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            merge = true;
                        }
                        break;
                    }
                }
            }
        }
        if(merge){
            addRandomNum();
            checkComplete();
        }


    }
    private void slipRight(){

        boolean merge = false;

        for(int y=0;y<4;y++){
            for (int x=3;x>=0;x--){
                for(int x1=x-1;x1>=0;x1--){
                    if(cardsMap[x1][y].getNum()>0){
                        if(cardsMap[x][y].getNum()<=0){
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                            cardsMap[x1][y].setNum(0);
                            x++;
                            merge = true;
                        }else if(cardsMap[x][y].equals(cardsMap[x1][y])){
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
                            cardsMap[x1][y].setNum(0);
                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            merge = true;
                        }
                        break;
                    }
                }
            }
        }
        if(merge){
            addRandomNum();
            checkComplete();
        }


    }
    private void slipUp(){

        boolean merge = false;

        for(int x=0;x<4;x++){
            for (int y=0;y<4;y++){
                for(int y1=y+1;y1<4;y1++){
                    if(cardsMap[x][y1].getNum()>0){
                        if(cardsMap[x][y].getNum()<=0){
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                            cardsMap[x][y1].setNum(0);
                            y--;
                            merge = true;
                        }else if(cardsMap[x][y].equals(cardsMap[x][y1])){
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
                            cardsMap[x][y1].setNum(0);
                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            merge = true;
                        }
                        break;
                    }
                }
            }
        }
        if (merge){
            addRandomNum();
            checkComplete();
        }


    }
    private void slipDown(){

        boolean merge = false;
        for(int x=0;x<4;x++){
            for (int y=3;y>=0;y--){
                for(int y1=y-1;y1>=0;y1--){
                    if(cardsMap[x][y1].getNum()>0){
                        if(cardsMap[x][y].getNum()<=0){
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                            cardsMap[x][y1].setNum(0);
                            y++;
                            merge = true;
                        }else if(cardsMap[x][y].equals(cardsMap[x][y1])){
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
                            cardsMap[x][y1].setNum(0);
                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            merge = true;
                        }
                        break;
                    }
                }
            }
        }
        if(merge){
            addRandomNum();
            checkComplete();
        }


    }



}
