package org.DFS;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

public class luoguP1238 {

    /*
     * 当前路径中的一个格子
     *
     * x、y 使用 0-based 下标。
     * nextDirection 表示下一个要尝试的方向。
     */
    static class Cell {
        int x;
        int y;
        int nextDirection;

        Cell(int x, int y) {
            this.x = x;
            this.y = y;
            this.nextDirection = 0;
        }
    }

    /*
     * 题目要求的优先顺序：
     *
     * 0：左
     * 1：上
     * 2：右
     * 3：下
     */
    static final int[] DX = {0, -1, 0, 1};
    static final int[] DY = {-1, 0, 1, 0};

    /*
     * 输出所有路径。
     *
     * maze 中：
     * 1 表示可以走；
     * 0 表示不能走。
     */
    static void findAllPaths(
            int[][] maze,
            int startX,
            int startY,
            int endX,
            int endY,
            PrintWriter out) {

        int rows = maze.length;
        int cols = maze[0].length;

        /*
         * 检查起点、终点是否越界，
         * 以及起点、终点是否可以走。
         */
        if (startX < 0 || startX >= rows
                || startY < 0 || startY >= cols
                || endX < 0 || endX >= rows
                || endY < 0 || endY >= cols
                || maze[startX][startY] == 0
                || maze[endX][endY] == 0) {

            out.println("-1");
            return;
        }

        /*
         * used 表示当前路径中是否已经使用了某个格子。
         *
         * 注意：
         * 这个标记不是永久标记。
         * 回溯时要恢复为 false，
         * 这样这个格子才能出现在其他路径中。
         */
        boolean[][] used = new boolean[rows][cols];

        /*
         * path 的队尾作为栈顶：
         *
         * addLast()    入栈
         * peekLast()   查看栈顶
         * removeLast() 出栈
         *
         * path 从队首到队尾保存：
         *
         * 起点 -> 当前格子
         */
        Deque<Cell> path = new ArrayDeque<>();

        path.addLast(new Cell(startX, startY));
        used[startX][startY] = true;

        boolean found = false;

        while (!path.isEmpty()) {
            Cell current = path.peekLast();

            /*
             * 到达终点，输出当前完整路径。
             */
            if (current.x == endX
                    && current.y == endY) {

                StringBuilder line = new StringBuilder();

                for (Cell cell : path) {
                    if (line.length() > 0) {
                        line.append("->");
                    }

                    // 输出时改回 1-based 坐标
                    line.append("(")
                            .append(cell.x + 1)
                            .append(",")
                            .append(cell.y + 1)
                            .append(")");
                }

                out.println(line);
                found = true;

                /*
                 * 当前路径已经输出完毕。
                 * 弹出终点，继续寻找其他路径。
                 */
                used[current.x][current.y] = false;
                path.removeLast();

                continue;
            }

            /*
             * 四个方向都尝试过，
             * 当前格子无法继续前进，进行回溯。
             */
            if (current.nextDirection >= 4) {
                used[current.x][current.y] = false;
                path.removeLast();

                continue;
            }

            /*
             * 取出当前还没有尝试的方向。
             */
            int direction = current.nextDirection++;

            int nextX = current.x + DX[direction];
            int nextY = current.y + DY[direction];

            /*
             * 判断下一个格子：
             *
             * 1. 没有越界；
             * 2. 是可走的格子；
             * 3. 不在当前路径中。
             */
            if (nextX >= 0
                    && nextX < rows
                    && nextY >= 0
                    && nextY < cols
                    && maze[nextX][nextY] == 1
                    && !used[nextX][nextY]) {

                used[nextX][nextY] = true;
                path.addLast(new Cell(nextX, nextY));
            }
        }

        if (!found) {
            out.println("-1");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int rows = scanner.nextInt();
        int cols = scanner.nextInt();

        int[][] maze = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maze[i][j] = scanner.nextInt();
            }
        }

        /*
         * 题目中的坐标是 1-based，
         * 程序内部使用 0-based，
         * 所以输入后要减 1。
         */
        int startX = scanner.nextInt() - 1;
        int startY = scanner.nextInt() - 1;

        int endX = scanner.nextInt() - 1;
        int endY = scanner.nextInt() - 1;

        PrintWriter out = new PrintWriter(
                new BufferedWriter(
                        new OutputStreamWriter(System.out)
                )
        );

        findAllPaths(
                maze,
                startX,
                startY,
                endX,
                endY,
                out
        );

        out.flush();
        scanner.close();
    }
}