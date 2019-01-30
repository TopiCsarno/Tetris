package topi.tetris;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class Tetris extends Activity implements GestureDetector.OnGestureListener {

    //settings:
    int speed;

    TetrisView myTetrisView;
    RelativeLayout SetupPanel;
    RelativeLayout GamePanel;
    TextView EndScoreTV;
    TextView CurrentScoreTV;
    Button PauseButton;
    ImageView NextShape;
    GestureDetector gDetector;

    final int OVER = 0;
    final int RUNNING = 1;
    final int PAUSE = 2;
    int gameState = OVER;

    Timer myTimer = new Timer();
    final Handler myHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        myTetrisView = (TetrisView) findViewById(R.id.view);

        NextShape = (ImageView) findViewById(R.id.nextShapeView);
        PauseButton = (Button) findViewById(R.id.pauseButton);
        GamePanel = (RelativeLayout) findViewById(R.id.Game);
        SetupPanel = (RelativeLayout) findViewById(R.id.Setup);
        EndScoreTV = (TextView) findViewById(R.id.gameOverTextView);
        CurrentScoreTV = (TextView) findViewById(R.id.scoreTextView);
        gDetector = new GestureDetector(this);
}

    public void gameOver() {
        if (gameState == RUNNING) {

            GamePanel.setVisibility(View.INVISIBLE);
            EndScoreTV.setText(String.format(Locale.getDefault(),
                    "  Game Over!\nYour Score: %2d", myTetrisView.scoreLanded));
            gameState = OVER;
        }
    }

    public void newGame() {
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                myHandler.post(Fall);
            }
        }, 0, speed);
        SetupPanel.setVisibility(View.INVISIBLE);
        GamePanel.setVisibility(View.VISIBLE);
        gameState = RUNNING;
    }

    public void getNextShape() {
        switch (myTetrisView.myShape.nextType) {
            case 1: NextShape.setImageResource(R.drawable.s_o);
                break;
            case 2: NextShape.setImageResource(R.drawable.s_i);
                break;
            case 3: NextShape.setImageResource(R.drawable.s_l1);
                break;
            case 4: NextShape.setImageResource(R.drawable.s_l2);
                break;
            case 5: NextShape.setImageResource(R.drawable.s_s1);
                break;
            case 6: NextShape.setImageResource(R.drawable.s_s2);
                break;
            case 7: NextShape.setImageResource(R.drawable.s_e);
                break;
        }
    }

    final Runnable Fall = new Runnable() {
        public void run() {
        if (gameState == RUNNING) {
            myTetrisView.myShape.Fall();
            if (myTetrisView.myShape.checkNewShape()) {
                gameOver();
            }
            getNextShape();
            CurrentScoreTV.setText(String.format(Locale.getDefault(),"% 3d",myTetrisView.scoreLanded));
            myTetrisView.invalidate();
            myTetrisView.myShape.deleteRow();
        }
        }
    };

    final Runnable Side = new Runnable() {
        public void run() {
            myTetrisView.myShape.Move();
            myTetrisView.invalidate();
        }
    };

    final Runnable Rotate = new Runnable() {
        public void run() {
            myTetrisView.myShape.Rotate();
            myTetrisView.invalidate();

        }
    };

    public void Controls(View view) {
        if (gameState == PAUSE) {
            if (view.getId() == R.id.pauseButton) {
                gameState = RUNNING;
                PauseButton.setBackground(getResources().getDrawable(R.drawable.pause));
            }
        } else if (gameState == RUNNING) {
            switch (view.getId()) {
                case R.id.moveLeft: {
                    myTetrisView.myShape.potLeft();
                    myHandler.post(Side);
                    break;
                }

                case R.id.moveRight: {
                    myTetrisView.myShape.potRight();
                    myHandler.post(Side);
                    break;
                }

                case R.id.Rotate: {
                    myTetrisView.myShape.potRotRight();
                    myHandler.post(Rotate);
                    break;
                }
                case R.id.pauseButton: {
                    gameState = PAUSE;
                    PauseButton.setBackground(getResources().getDrawable(R.drawable.resume));
                    break;
                }
            }
        }
    }

    public void Setup(View view) {
        switch (view.getId()) {
            case R.id.easy: {
                speed = 700;
                newGame();
                break;
            }

            case R.id.medium: {
                speed = 500;
                newGame();
                break;
            }

            case R.id.hard: {
                speed = 300;
                newGame();
                break;
            }
        }
    }

    //swipe controls

    @Override
    public boolean onTouchEvent(MotionEvent me) {
        return gDetector.onTouchEvent(me);
    }


    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    float sum = 0;
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        sum += distanceY;
        if (sum > 0) sum = 0;
        if (sum < -120) {
            sum += 120;
            myTetrisView.myShape.Fall();
            myTetrisView.invalidate();
        }
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onFling(MotionEvent start, MotionEvent finish, float velocityX, float velocityY) {
        return false;
    }

}