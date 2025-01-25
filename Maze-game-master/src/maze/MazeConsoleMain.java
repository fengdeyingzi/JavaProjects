package maze;

import javax.swing.*;
import java.awt.*;
import java.util.Stack;

public class MazeConsoleMain {
    int col = 10;
    int row = 10;
    final char DepthFirstSearchSolveMaze = 0;
    final char BreadthFirstSearchSolveMaze = 1;
    char solveMaze = 0;
    final char DepthFirstSearchCreateMaze = 0;
    final char RandomizedPrimCreateMaze = 1;
    final char RecursiveDivisionCreateMaze = 2;
    char createMaze = 0;


    Maze maze;
    boolean isPause = false;
    boolean bgmStart = true;

    /**
     * @return the isPause
     */
    public boolean isPause() {
        return isPause;
    }

    /**
     * @param isPause the isPause to set
     */
    public void setPause(boolean isPause) {
        this.isPause = isPause;
    }

    /**
     * @return the solveMaze
     */
    public char getSolveMaze() {
        return solveMaze;
    }

    /**
     * @param solveMaze the solveMaze to set
     */
    public void setSolveMaze(char solveMaze) {
        this.solveMaze = solveMaze;
    }

    /**
     * @return the createMaze
     */
    public char getCreateMaze() {
        return createMaze;
    }

    /**
     * @param createMaze the createMaze to set
     */
    public void setCreateMaze(char createMaze) {
        this.createMaze = createMaze;
    }

    /**
     * @return the bgmStart
     */
    public boolean isBgmStart() {
        return bgmStart;
    }

    /**
     * @param bgmStart the bgmStart to set
     */
    public void setBgmStart(boolean bgmStart) {
        this.bgmStart = bgmStart;
    }



    void go(){
        maze = new Maze(row, col);
        if (getSolveMaze() != maze.getSolveMaze())
            maze.setSolveMaze(getSolveMaze());
        maze.setPromptSolveMaze(false);


        if (maze.getColNumber() == col && maze.getRowNumber() == row) {
            if (getCreateMaze() != maze.getCreateMaze()) {
                maze.setCreateMaze(getCreateMaze());
                maze.createMaze();
            } else {
                maze.setComputerDo(false);
                maze.resetStepNumber();
                maze.resetTimer();
                maze.setThreadStop();
                maze.setBallPosition(maze.getEntrance());
            }
            maze.requestFocus();
            maze.repaint();
            maze.paint();
        } else {
            maze.setColNumber(col);
            maze.setRowNumber(row);
            if (getCreateMaze() != maze.getCreateMaze()) {
                maze.setCreateMaze(getCreateMaze());
            }
            maze.createMaze();
            maze.requestFocus();
            maze.paint();
        }
    }

    public static void main(String[] args) {
        MazeConsoleMain mazeConsoleMain = new MazeConsoleMain();
        mazeConsoleMain.go();

    }
}
