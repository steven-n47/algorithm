package org.DFS;


import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Scanner;

public class MazeSampleDFS {
    /*
    一个路径单元格


    x、y使用0-based下标。
    nextDirection表示单元格下一个要尝试的方向。
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
    使用栈实现DFS+回溯。

    maze中：0表是可以走；非0表示墙。

    找到路径时返回从起点到终点的路径；
    找不到路径时返回null。
     */
    static List<Cell> findPath(int[][] maze) {
        int rows = maze.length;
        int cols = maze[0].length;

        // 方向顺序：右、下、左、上
        int[] dx = {0, 1, 0, -1};
        int[] dy = {1, 0, -1, 0};

        boolean[][] visited = new boolean[rows][cols];

        /*
        使用ArrayDeque作为栈。

        path的队尾作为栈顶：
        入栈 addLast()
        查看栈顶 peekLast()
        出栈 removeLast()

        因为路径总是从队尾加入，
        所以path从队首到队尾正好是：

        起点->当前单元格
         */
        Deque<Cell> path = new ArrayDeque<>();

        path.addLast(new Cell(0, 0));
        visited[0][0] = true;

        while (!path.isEmpty()) {
            Cell current = path.peekLast();

            /*
            如果当前单元格是终点，
            path中已经包含完整路径。
             */
            if (current.x == rows - 1 && current.y == cols - 1) {
                return new ArrayList<>(path);
            }

            /*
            四个方向全部尝试过，
            当前单元格走不通，进行回溯。
             */
            if (current.nextDirection == 4) {
                path.removeLast();
                continue;
            }

            /*
            取出当前还没有尝试的方向，
            然后将nextDirection加一。
             */
            int direction = current.nextDirection++;

            int nextX = current.x + dx[direction];
            int nextY = current.y + dy[direction];

            /*
            判断下一个位置：

            1.没有越界；
            2.是可以走的格子；
            3.还没有访问过。
             */
            if (nextX >= 0 && nextX < rows
                    && nextY >= 0 && nextY < cols
                    && maze[nextX][nextY] == 0
                    && !visited[nextX][nextY]) {

                visited[nextX][nextY] = true;
                path.addLast(new Cell(nextX, nextY));
            }
        }

        // 栈为空，说明没有找到路径
        return null;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.printf("请输入迷宫数组的行数和列数：");

        int rows = scanner.nextInt();
        int cols = scanner.nextInt();

        int[][] maze = new int[rows][cols];

        System.out.println("请输入迷宫矩阵：");

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maze[i][j] = scanner.nextInt();
            }
        }

        List<Cell> path = findPath(maze);

        boolean found = path != null;

        System.out.println(found);
        System.out.println();

        if (found) {
            for (Cell cell : path) {
                /*
                程序内部使用0-based下标，
                输出时转换为原程序中的1-based坐标。
                 */
                System.out.printf("(%d,%d)%n", cell.x + 1, cell.y + 1);
            }
        } else {
            System.out.println("你被困住迷宫中了");
        }

        scanner.close();
    }
}
