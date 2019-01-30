package topi.tetris;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import java.util.Arrays;

public class TetrisView extends View {

    Shape myShape = new Shape(this);

    int TileSize;
    int XTileCount;
    int YTileCount;

    int scoreLanded = 0;
    int scoreClearedLines = 0;

    Bitmap[] TileArray;
    int[][] TileGrid;

    public final Paint paint = new Paint();

    public TetrisView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TetrisView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        float density = getResources().getDisplayMetrics().density;

        XTileCount = 10;
        YTileCount = 16;
        TileSize = (int) (25*density);

        TileGrid = new int[XTileCount][YTileCount];

        TileArray = new Bitmap[8];

        Resources r = this.getResources();
        loadTileArray(1, r.getDrawable(R.drawable.tetris_yellow));
        loadTileArray(2, r.getDrawable(R.drawable.tetris_teal));
        loadTileArray(3, r.getDrawable(R.drawable.tetris_orange));
        loadTileArray(4, r.getDrawable(R.drawable.tetris_blue));
        loadTileArray(5, r.getDrawable(R.drawable.tetris_green));
        loadTileArray(6, r.getDrawable(R.drawable.tetris_red));
        loadTileArray(7, r.getDrawable(R.drawable.tetris_purple));
    }

    public void loadTileArray(int key, Drawable tile) {
        Bitmap bitmap = Bitmap.createBitmap(TileSize, TileSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        tile.setBounds(0, 0, TileSize, TileSize);
        tile.draw(canvas);

        TileArray[key] = bitmap;
    }

    public void drawMe(Canvas canvas, int[][] TheGrid, int Xoffset, int Yoffset) {
        for (int row = 0; row < TheGrid.length; row++) {
            for (int col = 0; col < TheGrid[row].length; col++) {
                if (TheGrid[row][col] > 0) {
                    canvas.drawBitmap(TileArray[TheGrid[row][col]], (Xoffset+row)*TileSize, (Yoffset+col)*TileSize,  paint);
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // landed shapes
        drawMe(canvas, TileGrid, 0, 0);

        // active shape
        drawMe(canvas, myShape.ShapeGrid, myShape.X, myShape.Y);
    }
}
