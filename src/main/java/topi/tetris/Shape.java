package topi.tetris;

import java.util.Arrays;
import java.util.Random;

class Shape {

    int[][] ShapeGrid;
    private int[][] ShapeGridPot;

    private int ShapeIndex;
    private int[][][] Rotations;

    int X;
    int Y;

    private int XPot;
    private int YPot;

    private TetrisView TV;
    private Random RNG = new Random();
    int nextType = RNG.nextInt(7)+1;

    // constructor

    Shape(TetrisView tiv) {
        this.TV = tiv;
        setNewShape();
    }

    private void setNewShape() {

        X = 3;
        Y = 0;

        int type = nextType;
        nextType = RNG.nextInt(7)+1;

        switch (type) {
            case 1:
                X++;

                ShapeGrid = new int[][]{
                        {1, 1},
                        {1, 1}};

                Rotations = new int[][][] {
                        {   {1, 1},
                            {1, 1}}};
                break;

            case 2:
                ShapeGrid = new int[][] {
                        {2},
                        {2},
                        {2},
                        {2}};

                Rotations = new int[][][] {
                        {   {2, 2, 2, 2}},

                        {   {2},
                            {2},
                            {2},
                            {2}}};
                break;

            case 3:
                ShapeGrid = new int[][]
                {{3, 3},
                 {0, 3},
                 {0, 3}};

                Rotations = new int[][][] {
                        {   {0, 0, 3},
                            {3, 3, 3}},

                        {   {3, 0},
                            {3, 0},
                            {3, 3}},

                        {   {3, 3, 3},
                            {3, 0, 0}},

                        {   {3, 3},
                            {0, 3},
                            {0, 3}}};
                break;

            case 4:
                ShapeGrid = new int[][]
                {{0, 4},
                 {0, 4},
                 {4, 4}};

                Rotations = new int[][][] {
                        {   {4, 0, 0},
                            {4, 4, 4}},

                        {   {4, 4},
                            {4, 0},
                            {4, 0}},

                        {   {4, 4, 4},
                            {0, 0, 4}},

                        {   {0, 4},
                            {0, 4},
                            {4, 4}}};
                break;

            case 5:
                ShapeGrid = new int[][]
                {{0, 5},
                 {5, 5},
                 {5, 0}};

                Rotations = new int[][][] {
                        {   {5, 5, 0},
                            {0, 5, 5}},

                        {   {0, 5},
                            {5, 5},
                            {5, 0}}};
                break;

            case 6:
                ShapeGrid = new int[][]
                {{6, 0},
                 {6, 6},
                 {0, 6}};

                Rotations = new int[][][] {
                        {   {0, 6, 6},
                            {6, 6, 0}},

                        {   {6, 0},
                            {6, 6},
                            {0, 6}}};
                break;

            case 7:
                ShapeGrid = new int[][]
                {{0, 7},
                 {7, 7},
                 {0, 7}};

                Rotations = new int[][][] {
                        {   {0, 7, 0},
                            {7, 7, 7}},

                        {   {7, 0},
                            {7, 7},
                            {7, 0}},

                        {   {7, 7, 7},
                            {0, 7, 0}},

                        {   {0, 7},
                            {7, 7},
                            {0, 7}}};
                break;

        }
        ShapeIndex = Rotations.length-1;
    }

    boolean checkNewShape() {
        for (int row = 0; row < ShapeGrid.length; row++) {
            for (int col = 0; col < ShapeGrid[row].length; col++) {
                if (ShapeGrid[row][col] > 0) {
                    // spot already taken
                    if (TV.TileGrid[row+X][col+Y] > 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // button press

    void potLeft() {
        XPot = X-1;
        YPot = Y;
    }

    void potRight() {
        XPot = X+1;
        YPot = Y;
    }

    void potRotRight() {
        if (ShapeIndex <= 0) {
            ShapeIndex = Rotations.length-1;
        } else {
            ShapeIndex--;
        }
        ShapeGridPot = Rotations[ShapeIndex];
    }

    // Shape Movement

    private boolean CollisionFall() {
        for (int row = 0; row < ShapeGrid.length; row++) {
            for (int col = 0; col < ShapeGrid[row].length; col++) {
                if (ShapeGrid[row][col] > 0) {

                    // would be below the playing field
                    if (((col+1) + YPot) > TV.YTileCount) {
                        return true;

                    } else

                    // spot already taken
                    if (TV.TileGrid[row+XPot][col+YPot] > 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean CollisionMove() {
        for (int row = 0; row < ShapeGrid.length; row++) {
            for (int col = 0; col < ShapeGrid[row].length; col++) {
                if (ShapeGrid[row][col] > 0) {

                    if (XPot < 0){
                        return true;
                    }

                    if ((row+1) + XPot > TV.XTileCount){
                        return true;
                    }

                    // spot already taken
                    if (TV.TileGrid[row+XPot][col+YPot] > 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean CollisionRotate() {
        for (int row = 0; row < ShapeGridPot.length; row++) {
            for (int col = 0; col < ShapeGridPot[row].length; col++) {
                if (ShapeGridPot[row][col] > 0) {

                    // left
                    if (X < 0){
                        return true;
                    }

                    // right
                    if ((row+1) + X > TV.XTileCount){
                        return true;
                    }

                    //top
                    if (Y < 0){
                        return true;
                    }

                    //bot
                    if ((col+1) + Y > TV.YTileCount){
                        return true;
                    }

                    // spot already taken
                    if (TV.TileGrid[row+X][col+Y] > 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void landShape() {
        for (int row = 0; row < ShapeGrid.length; row++) {
            for (int col = 0; col < ShapeGrid[row].length; col++) {
                if (ShapeGrid[row][col] > 0) {
                    TV.TileGrid[row+X][col+Y] = ShapeGrid[row][col];
                }
            }
        }
    }

    void Fall() {
        XPot = X;
        YPot = Y+1;

        if (!CollisionFall()) {
            X = XPot;
            Y = YPot;
        } else {
            landShape();
            setNewShape();
            TV.scoreLanded++;
        }
    }

    void Move() {
        if (!CollisionMove()) {
            X = XPot;
            Y = YPot;
        }
    }

    void Rotate() {
        if (!CollisionRotate()) {
            ShapeGrid = ShapeGridPot;
        }
    }

    // delete full row

    void deleteRow() {
        int[][] check = flip(TV.TileGrid);

        for (int i = 0; i < check.length; i++) {
            if (checkRow(check[i])) {
                clearLine(check, i);
                TV.scoreClearedLines++;
            }
        }

        TV.TileGrid = flip(check);
    }

    private int[][] flip(int[][] flipme){
        int[][] temp = new int[flipme[0].length][flipme.length];
        for (int i = 0; i < flipme.length; i++)
            for (int j = 0; j < flipme[0].length; j++)
                temp[j][i] = flipme[i][j];
        return temp;
    }

    private void clearLine(int[][] grid, int index) {
        Arrays.fill(grid[index],0);
        for (int i = index-1; i >= 0; i--) {
            grid[i+1] = Arrays.copyOf(grid[i],grid[0].length);
        }
        Arrays.fill(grid[0], 0);
    }

    private boolean checkRow(int[] row) {
        for (int num : row) {
            if (num == 0) {
                return false;
            }
        }
        return true;
    }
}
