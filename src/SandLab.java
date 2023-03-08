import java.awt.*;
import java.util.*;

public class SandLab
{
    public static void main(String[] args)
    {
        SandLab lab = new SandLab(120, 80);
        lab.run();
    }

    //add constants for particle types here
    public static final int EMPTY = 0;
    public static final int METAL = 1;
    public static final int SAND = 2;
    public static final int WATER = 3;
    public static final int LAVA = 4;
    public static final int INVERT = 5;
    public static final int STEAM = 6;
    static int VALUE = 1;

    //do not add any more fields
    private int[][] grid;
    private SandDisplay display;

    public SandLab(int numRows, int numCols)
    {
        String[] names;
        names = new String[6];
        names[EMPTY] = "Empty";
        names[METAL] = "Metal";
        names[SAND] = "Sand";
        names[WATER] = "Water";
        names[LAVA] = "Lava";
        names[INVERT] = "Invert";

        display = new SandDisplay("Falling Sand", numRows, numCols, names);
        grid = new int[numRows][numCols];
    }

    //called when the user clicks on a location using the given tool
    private void locationClicked(int row, int col, int tool)
    {
        if(tool != INVERT) {
            if (grid[row][col] == EMPTY && tool != EMPTY) {
                grid[row][col] = tool;
            } else if (tool == EMPTY) {
                grid[row][col] = EMPTY;
            }
        }
        else {
            VALUE *= -1;
            for (int i = 0; i < display.getSpeed(); i++) {
                for (int j = 0; j < 70; j++) {
                    step();
                }
            }
        }
    }

    //copies each element of grid into the display
    public void updateDisplay()
    {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == EMPTY){
                    display.setColor(i,j,new Color(0,0,0));
                }
                if (grid[i][j] == METAL){
                    display.setColor(i,j,new Color(88, 88, 88));
                }
                if (grid[i][j] == SAND){
                    display.setColor(i,j,new Color(255,255,0));
                }
                if (grid[i][j] == WATER){
                    display.setColor(i,j,new Color(0, 212, 255));
                }
                if (grid[i][j] == LAVA){
                    display.setColor(i,j,new Color(204, 32, 4));
                }
                if (grid[i][j] == STEAM){
                    display.setColor(i,j,new Color(228, 228, 228));
                }
            }
        }
    }

    //called repeatedly.
    //causes one random particle to maybe do something.
    public void step()
    {
        if(VALUE == -1){
            step_neg();
            return;
        }
        Random a = new Random();
        int row = a.nextInt(grid.length-1);
        int col = a.nextInt(grid[0].length);

        if(grid[row][col] == SAND) {
            if (grid[row + 1][col] == LAVA) {
                grid[row][col] = LAVA;
                grid[row + 1][col] = SAND;
            }
            else if (grid[row + 1][col] == EMPTY) {
                grid[row][col] = EMPTY;
                grid[row + 1][col] = SAND;
            }
            else if (grid[row + 1][col] == WATER) {
                grid[row][col] = WATER;
                grid[row + 1][col] = SAND;
            }
            else if (grid[row + 1][col] == SAND) {
                if (col == 0) {
                    if (grid[row + 1][col + 1] == EMPTY) {
                        grid[row + 1][col + 1] = SAND;
                        grid[row][col] = EMPTY;
                    }
                } //left boundary
                else if (col == grid[0].length-1){
                    if (grid[row + 1][col - 1] == EMPTY) {
                        grid[row + 1][col - 1] = SAND;
                        grid[row][col] = EMPTY;
                    }
                } //right boundary
                else {
                    if(grid[row + 1][col + 1] == EMPTY){ //if down right is empty
                        if(grid[row + 1][col - 1] == EMPTY){
                            grid[row+1][col + (int)Math.pow(-1,a.nextInt(10))] = SAND;
                            grid[row][col] = EMPTY;
                        } //if both are empty
                        else {
                            grid[row+1][col+1] = SAND;
                            grid[row][col] = EMPTY;
                        } //if only down right is empty
                    }
                    else if(grid[row + 1][col - 1] == EMPTY){
                        grid[row+1][col-1] = SAND;
                        grid[row][col] = EMPTY;
                    } //both cannot be empty, so if down left is empty
                } //general case
            }
        }
        else if(grid[row][col] == WATER){
            int lr = a.nextInt(0,3); //decide which way to move
            if(col == 0){
                lr = a.nextInt(1,3);
            } //edge case left
            if(col == grid[0].length-1){
                lr = 1 + (int) Math.pow(-1,a.nextInt(10));
            } //edge case right
            switch (lr) {
                case 0 -> { //move left
                    if (grid[row][col - 1] == LAVA) {
                        grid[row][col - 1] = STEAM;
                        grid[row][col] = SAND;
                    } else if (grid[row][col - 1] == EMPTY || grid[row][col - 1] == STEAM) {
                        grid[row][col - 1] = WATER;
                        grid[row][col] = EMPTY;
                    }
                }
                case 1 -> {
                    if (grid[row][col + 1] == LAVA) {
                        grid[row][col + 1] = STEAM;
                        grid[row][col] = SAND;
                    } else if (grid[row][col + 1] == EMPTY || grid[row][col + 1] == STEAM) {
                        grid[row][col + 1] = WATER;
                        grid[row][col] = EMPTY;
                    }
                }
                case 2 -> {
                    if (grid[row + 1][col] == LAVA) {
                        grid[row + 1][col] = STEAM;
                        grid[row][col] = SAND;
                    } else if (grid[row + 1][col] == EMPTY || grid[row + 1][col] == STEAM) {
                        grid[row + 1][col] = WATER;
                        grid[row][col] = EMPTY;
                    }
                }
            }
        }
        else if(grid[row][col] == LAVA) {
            int lr = a.nextInt(0, 3); //decide which way to move
            if (col == 0) {
                lr = a.nextInt(1, 3);
            } //edge case left
            if (col == grid[0].length - 1) {
                lr = 1 + (int) Math.pow(-1, a.nextInt(10));
            } //edge case right
            switch (lr) {
                case 0 -> { //move left
                    if (grid[row][col - 1] == EMPTY) {
                        grid[row][col - 1] = LAVA;
                        grid[row][col] = EMPTY;
                    }
                }
                case 1 -> {
                    if (grid[row][col + 1] == EMPTY) {
                        grid[row][col + 1] = LAVA;
                        grid[row][col] = EMPTY;
                    }
                }
                case 2 -> {
                    if (grid[row + 1][col] == EMPTY) {
                        grid[row + 1][col] = LAVA;
                        grid[row][col] = EMPTY;
                    }
                }
            }

            /*if (grid[row + 1][col] == EMPTY) {
                grid[row][col] = EMPTY;
                grid[row + 1][col] = LAVA;
            }*/
            if (grid[row + 1][col] == WATER) {
                grid[row][col] = STEAM;
                grid[row + 1][col] = SAND;
            }
            else if (grid[row + 1][col] == METAL) {
                grid[row + 1][col] = LAVA;
            }
            else if(grid[row+1][col] == STEAM){
                grid[row][col] = STEAM;
                grid[row + 1][col] = LAVA;
            }
        }
        else if(grid[row][col] == STEAM){
            /*if(row != 0) {
                int temp = grid[row - 1][col];
                grid[row - 1][col] = STEAM;
                grid[row][col] = temp;
            }*/
            if(row != 0) {
                int lr = a.nextInt(0, 3); //generate where to go
                if (col == 0) {
                    lr = a.nextInt(1, 3);
                } //left edge case
                if (col == grid[0].length - 1) {
                    lr = 1 + (int) Math.pow(-1, a.nextInt(10));
                } //right edge case
                switch (lr) {
                    case 0 -> { //move left
                        if (grid[row - 1][col - 1] == EMPTY) {
                            grid[row - 1][col - 1] = STEAM;
                            grid[row][col] = EMPTY;
                        }
                    }
                    case 1 -> { //move right
                        if (grid[row - 1][col + 1] == EMPTY) {
                            grid[row - 1][col + 1] = STEAM;
                            grid[row][col] = EMPTY;
                        }
                    }
                    case 2 -> { //move up
                        if (grid[row - 1][col] == EMPTY) {
                            grid[row - 1][col] = STEAM;
                            grid[row][col] = EMPTY;
                        }
                    }
                }
            }
        }

        int c = 0; //counter for steam in top row
        for (int i = 0; i < grid[0].length; i++) {
            if(grid[0][i] == STEAM){
                c++;
            }
            if(c > grid[0].length * 0.25){
                for (int j = 0; j < grid[0].length; j++) {
                    if(grid[0][j] == STEAM){
                        grid[0][j] = WATER;
                    }
                }
            }
        } //reduce steam
    }

    public void step_neg() {
        int val = -1;
        Random a = new Random(); int row, col;
        //row = a.nextInt(1,grid.length-1);
        row = a.nextInt(1,grid.length-1);
        col = a.nextInt(grid[0].length);
        if(grid[row][col] == SAND) {
            if (grid[row + val][col] == LAVA) {
                grid[row][col] = LAVA;
                grid[row + val][col] = SAND;
            }
            else if (grid[row + val][col] == EMPTY) {
                grid[row][col] = EMPTY;
                grid[row + val][col] = SAND;
            }
            else if (grid[row + val][col] == WATER) {
                grid[row][col] = WATER;
                grid[row + val][col] = SAND;
            }
            else if (grid[row + val][col] == SAND) {
                if (col == 0) {
                    if (grid[row + val][col + 1] == EMPTY) {
                        grid[row + val][col + 1] = SAND;
                        grid[row][col] = EMPTY;
                    }
                } //left boundary
                else if (col == grid[0].length-1){
                    if (grid[row + val][col - 1] == EMPTY) {
                        grid[row + val][col - 1] = SAND;
                        grid[row][col] = EMPTY;
                    }
                } //right boundary
                else {
                    if(grid[row + val][col + 1] == EMPTY){ //if down right is empty
                        if(grid[row + val][col - 1] == EMPTY){
                            grid[row+val][col + (int)Math.pow(-1,a.nextInt(10))] = SAND;
                            grid[row][col] = EMPTY;
                        } //if both are empty
                        else {
                            grid[row+val][col+1] = SAND;
                            grid[row][col] = EMPTY;
                        } //if only down right is empty
                    }
                    else if(grid[row + val][col - 1] == EMPTY){
                        grid[row+val][col-1] = SAND;
                        grid[row][col] = EMPTY;
                    } //both cannot be empty, so if down left is empty
                } //general case
            }
        }

        row = a.nextInt(1,grid.length);
        if(grid[row][col] == WATER){
            int lr = a.nextInt(0, 3); //decide which way to move
            if (col == 0) {
                lr = a.nextInt(1, 3);
            } //edge case left
            if (col == grid[0].length - 1) {
                lr = 1 + (int) Math.pow(-1, a.nextInt(10));
            } //edge case right
            switch (lr) {
                case 0 -> { //move left
                    if (grid[row][col - 1] == LAVA) {
                        grid[row][col - 1] = STEAM;
                        grid[row][col] = SAND;
                    } else if (grid[row][col - 1] == EMPTY || grid[row][col - 1] == STEAM) {
                        grid[row][col - 1] = WATER;
                        grid[row][col] = EMPTY;
                    }
                }
                case 1 -> {
                    if (grid[row][col + 1] == LAVA) {
                        grid[row][col + 1] = STEAM;
                        grid[row][col] = SAND;
                    } else if (grid[row][col + 1] == EMPTY || grid[row][col + 1] == STEAM) {
                        grid[row][col + 1] = WATER;
                        grid[row][col] = EMPTY;
                    }
                }
                case 2 -> {
                    if (grid[row - 1][col] == LAVA) {
                        grid[row][col] = STEAM;
                        grid[row - 1][col] = SAND;
                    } else if (grid[row - 1][col] == EMPTY || grid[row - 1][col] == STEAM) {
                        grid[row - 1][col] = WATER;
                        grid[row][col] = EMPTY;
                    }
                }
            }
        }

        row = a.nextInt(1,grid.length-1);
        if(grid[row][col] == LAVA) {
            int lr = a.nextInt(0, 3); //decide which way to move
            if (col == 0) {
                lr = a.nextInt(1, 3);
            } //edge case left
            if (col == grid[0].length - 1) {
                lr = 1 + (int) Math.pow(-1, a.nextInt(10));
            } //edge case right
            switch (lr) {
                case 0 -> { //move left
                    if (grid[row][col - 1] == EMPTY) {
                        grid[row][col - 1] = LAVA;
                        grid[row][col] = EMPTY;
                    }
                }
                case 1 -> {
                    if (grid[row][col + 1] == EMPTY) {
                        grid[row][col + 1] = LAVA;
                        grid[row][col] = EMPTY;
                    }
                }
                case 2 -> {
                    if (grid[row + val][col] == EMPTY) {
                        grid[row + val][col] = LAVA;
                        grid[row][col] = EMPTY;
                    }
                }
            }

            /*if (grid[row + 1][col] == EMPTY) {
                grid[row][col] = EMPTY;
                grid[row + 1][col] = LAVA;
            }*/
            if (grid[row + val][col] == WATER) {
                grid[row][col] = STEAM;
                grid[row + val][col] = SAND;
            }
            else if (grid[row + val][col] == METAL) {
                grid[row + val][col] = LAVA;
            }
            else if(grid[row+val][col] == STEAM){
                grid[row][col] = STEAM;
                grid[row + val][col] = LAVA;
            }
        }
        if(row == grid.length){
            row = a.nextInt(1,grid.length-1);
        }
        row = a.nextInt(1,grid.length-1);
        if(grid[row][col] == STEAM){
            /*if(row != 0) {
                int temp = grid[row - 1][col];
                grid[row - 1][col] = STEAM;
                grid[row][col] = temp;
            }*/
            if(row != 0) {
                int lr = a.nextInt(0, 3); //generate where to go
                if (col == 0) {
                    lr = a.nextInt(1, 3);
                } //left edge case
                if (col == grid[0].length - 1) {
                    lr = 1 + (int) Math.pow(-1, a.nextInt(10));
                } //right edge case
                switch (lr) {
                    case 0 -> { //move left
                        if (grid[row + 1][col - 1] == EMPTY) {
                            grid[row + 1][col - 1] = STEAM;
                            grid[row][col] = EMPTY;
                        }
                    }
                    case 1 -> { //move right
                        if (grid[row + 1][col + 1] == EMPTY) {
                            grid[row + 1][col + 1] = STEAM;
                            grid[row][col] = EMPTY;
                        }
                    }
                    case 2 -> { //move up
                        if (grid[row + 1][col] == EMPTY || grid[row+1][col] == WATER) {
                            int temp = grid[row + 1][col];
                            grid[row + 1][col] = STEAM;
                            grid[row][col] = temp;
                        }
                    }
                }
            }
        }

        int c = 0; //counter for steam in top row
        for (int i = 0; i < grid[0].length; i++) {
            if (grid[grid.length - 1][i] == STEAM) {
                c++;
            }
            if (c > grid[0].length * 0.25) {
                for (int j = 0; j < grid[0].length; j++) {
                    if (grid[grid.length - 1][j] == STEAM) {
                        grid[grid.length - 1][j] = WATER;
                    }
                }
            }
        } //reduce steam
    } //invert stuff

    //do not modify
    public void run()
    {
        while (true)
        {
            for (int i = 0; i < display.getSpeed(); i++)
                step();
            updateDisplay();
            display.repaint();
            display.pause(1);  //wait for redrawing and for mouse
            int[] mouseLoc = display.getMouseLocation();
            if (mouseLoc != null)  //test if mouse clicked
                locationClicked(mouseLoc[0], mouseLoc[1], display.getTool());
        }
    }
}