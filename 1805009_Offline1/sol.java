import java.util.*;

class Node {
    Board board;
    Node parent;
    int num_of_move;

    Node(Board b, Node p, int nom) {
        board = new Board(b.size, b.mat, b.flag);
        parent = p;
        num_of_move = nom;
    }

    int f() {

        return board.distance + num_of_move;

    }

    void print_path() {

        if (parent != null) {
            parent.print_path();
        }
        board.print();
        return;

    }

}

class Board {
    int size;
    int[][] mat;
    int curr_space_x;
    int curr_space_y;
    int distance;
    int flag;

    Board(int n, int[][] input_mat, int f) {
        size = n;
        mat = new int[size][size];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                mat[i][j] = input_mat[i][j];
                if (mat[i][j] == 0) {
                    curr_space_x = i;
                    curr_space_y = j;
                }
            }
        this.flag = f;
        if (flag == 0)
            distance = hamming_distance();
        else
            distance = manhatton_distance();

        // goal=new int[size][size];

        // int c=1;
        // for(int i=0;i<n;i++)
        // for(int j=0;j<n;j++)
        // goal[i][j]=c++;

        // goal[n-1][n-1]=0;

    }

    boolean move_left() {
        if (curr_space_y + 1 < size) {
            mat[curr_space_x][curr_space_y] = mat[curr_space_x][curr_space_y + 1];
            curr_space_y++;
            mat[curr_space_x][curr_space_y] = 0;
            return true;
        } else
            return false;
    }

    boolean move_right() {
        if (curr_space_y - 1 >= 0) {
            mat[curr_space_x][curr_space_y] = mat[curr_space_x][curr_space_y - 1];
            curr_space_y--;
            mat[curr_space_x][curr_space_y] = 0;
            return true;
        } else
            return false;
    }

    boolean move_up() {
        if (curr_space_x + 1 < size) {
            mat[curr_space_x][curr_space_y] = mat[curr_space_x + 1][curr_space_y];
            curr_space_x++;
            mat[curr_space_x][curr_space_y] = 0;
            return true;
        } else
            return false;
    }

    boolean move_down() {
        if (curr_space_x - 1 >= 0) {
            mat[curr_space_x][curr_space_y] = mat[curr_space_x - 1][curr_space_y];
            curr_space_x--;
            mat[curr_space_x][curr_space_y] = 0;
            return true;
        } else
            return false;
    }

    boolean issolvable() {
        int inv = 0;
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                if (mat[i][j] != 0)
                    for (int k = i; k < i + 1; k++)
                        for (int l = j + 1; l < size; l++) {
                            if (mat[k][l] != 0)
                                if ((mat[i][j]) > (mat[k][l])) {
                                    inv++;
                                }

                        }
                for (int k = i + 1; k < size; k++)
                    for (int l = 0; l < size; l++) {
                        if (mat[k][l] != 0)
                            if ((mat[i][j]) > (mat[k][l])) {
                                inv++;
                            }

                    }

            }
        if (size % 2 == 1) {
            if (inv % 2 == 0)
                return true;
            return false;
        }
        if ((size - curr_space_x) % 2 == 0) {
            if (inv % 2 == 0)
                return false;
            return true;
        }
        if (inv % 2 == 0)
            return true;
        return false;

    }

    boolean issolved() {
        if (distance == 0)
            return true;
        return false;
    }

    int hamming_distance() {
        int c = 0;
        int val = 1;
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                if (i == size - 1 && j == size - 1)
                    val = 0;
                if (mat[i][j] != 0 && mat[i][j] != val)
                    c++;
                val++;
            }

        return c;

    }

    int manhatton_distance() {
        int d = 0;
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                if (mat[i][j] != 0) {
                    int temp = mat[i][j] - 1;
                    int row = temp / size;
                    int col = temp % size;
                    d = d + Math.abs(row - i) + Math.abs(col - j);

                }

            }
        return d;

    }

    void print() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++)
                if (mat[i][j] != 0)
                    System.out.print(mat[i][j] + " ");
                else
                    System.out.print("* ");

            System.out.println();
        }
        System.out.println("-----------------------");
    }

    @Override
    public boolean equals(Object o) {

        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                if (mat[i][j] != ((Board) o).mat[i][j])
                    return false;
            }
        return true;

    }

    @Override
    public int hashCode() {

        return Arrays.deepHashCode(mat);
    }

}

public class sol {
    public static class comparator implements Comparator<Node> {

        @Override
        public int compare(Node o1, Node o2) {
            if (o1.f() > o2.f())
                return 1;

            return -1;
        }
    }

    public static void search(Board board) {
        if (!board.issolvable()) {
            System.out.println("Can not solve");
            return;
        }
        int expanded = 0;
        int explored = 0;
        PriorityQueue<Node> pq = new PriorityQueue<>(new comparator());
        pq.add(new Node(board, null, 0));
        expanded++;
        HashSet<Board> closed_list = new HashSet<Board>();

        while (!pq.isEmpty()) {
            Node node = pq.peek();
            pq.poll();
            closed_list.add(new Board(node.board.size, node.board.mat, node.board.flag));
            explored++;

            if (node.board.issolved()) {
                System.out.println("Optimal number of moves: " + node.num_of_move);
                System.out.println("Number of Explored Nodes: " + explored);
                System.out.println("Number of Expanded Nodes: " + expanded);
                node.print_path();
                return;
            }

            if (node.board.move_right()) {
                if (!closed_list.contains(node.board)) {
                    pq.add(new Node(node.board, node, node.num_of_move + 1));
                    expanded++;
                }
                node.board.move_left();
            }
            if (node.board.move_left()) {
                if (!closed_list.contains(node.board)) {
                    pq.add(new Node(node.board, node, node.num_of_move + 1));
                    expanded++;
                }
                node.board.move_right();
            }
            if (node.board.move_up()) {
                if (!closed_list.contains(node.board)) {
                    pq.add(new Node(node.board, node, node.num_of_move + 1));
                    expanded++;
                }
                node.board.move_down();
            }
            if (node.board.move_down()) {
                if (!closed_list.contains(node.board)) {
                    pq.add(new Node(node.board, node, node.num_of_move + 1));
                    expanded++;
                }
                node.board.move_up();
            }
        }

    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        int k;
        k = scan.nextInt();
        int[][] input_mat = new int[k][k];
        for (int i = 0; i < k; i++)
            for (int j = 0; j < k; j++) {
                String temp = scan.next();
                input_mat[i][j] = temp.equals("*") ? 0 : Integer.valueOf(temp);
            }
        int flag;
        // flag=0 hamming
        // flag=1 manhatton

        System.out.println("Using Hamming Distance");
        flag = 0;
        search(new Board(k, input_mat, flag));
        System.out.println("-----------------------");
        System.out.println("Using Manhattan Distance");
        flag = 1;
        search(new Board(k, input_mat, flag));
    }
}