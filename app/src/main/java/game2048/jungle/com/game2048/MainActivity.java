package game2048.jungle.com.game2048;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView tvBestScore;
    private TextView tvScore;
    private static MainActivity mainActivity = null;
    private int score = 0;
    private int bestScores = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvScore = (TextView)findViewById(R.id.tv_score);
        tvBestScore = (TextView)findViewById(R.id.tv_bestscore);

        BastScode bastScode = new BastScode(this);
        bestScores = bastScode.getBestScode();
        tvBestScore.setText(bestScores+"");

        Button restart = (Button)findViewById(R.id.restart);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameView.getGameView().StartGame();
            }
        });

    }

    public MainActivity(){
        mainActivity = this;
    }

    public static MainActivity getMainActivity(){
        return mainActivity;
    }

    public void clearScore(){
        score = 0;
        showScore();
    }

    public void addScore(int s){
        score+=s;
        showScore();
        if(score>bestScores){
            bestScores = score;
            BastScode bs = new BastScode(this);
            bs.setBestScode(bestScores);
            tvBestScore.setText(bestScores+"");
        }
    }

    public void showScore(){
        tvScore.setText(score+"");
    }

    public void endGame(){
        finish();
    }

    private long exitTime = 0;


    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(this, "再按一次退出哈",1000).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
