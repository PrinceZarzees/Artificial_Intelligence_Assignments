import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

public class Scheduling {

    node[] node_list;
    ArrayList<Integer>[] day;
    ArrayList<Integer>[] students;
    int number_of_color;

    Scheduling(int[] courses, ArrayList<Integer>[] students) {
        node_list = new node[courses.length];
        this.students = students;
        number_of_color = 0;
        for (int i = 0; i < courses.length; i++) {
            node_list[i] = new node(i, courses[i]);
        }
        for (int i = 0; i < students.length; i++) {
            for (int j : students[i]) {
                node_list[j].Add_student(i);
            }
            for (int j = 0; j < students[i].size() - 1; j++) {
                for (int k = j + 1; k < students[i].size(); k++) {
                    node_list[students[i].get(j)].Add_edge(node_list[students[i].get(k)]);
                    node_list[students[i].get(k)].Add_edge(node_list[students[i].get(j)]);

                }
            }
        }

    }

    void daywise_assign_exam() {
        day = new ArrayList[node_list.length];
        for (int i = 1; i < node_list.length; i++) {
            if (day[node_list[i].color] == null)
                day[node_list[i].color] = new ArrayList<Integer>();
            day[node_list[i].color].add(node_list[i].course_id);
            number_of_color = Math.max(number_of_color, node_list[i].color + 1);
        }

    }

    void print() {
        daywise_assign_exam();
        // for (int i = 1; i < node_list.length; i++)
        // {
        // System.out.println(i+" "+node_list[i].color);
        // }
        for (int i = 0; i < number_of_color; i++) {
            System.out.print(i + ": ");
            for (int j : day[i]) {
                System.out.print(j + " ");
                // ArrayList<Integer> temp = new ArrayList<Integer>();
                // for (int k : node_list[j].student_list)
                // temp.add(k);

                // if (new HashSet<Integer>(temp).size() != temp.size())
                // System.out.println("Error");
            }
            System.out.println();
        }

    }

    void start_coloring(PriorityQueue<node> queue) {
        for (node n : node_list) {
            queue.add(n);
        }
        while (queue.size() > 0) {
            node temp = queue.poll();
            temp.Assign_color();

        }
        daywise_assign_exam();

    }

    double calculate_penalty(int type) {
        double s = 0;
        for (int i = 0; i < students.length; i++) {
            for (int j = 0; j < students[i].size() - 1; j++)
                for (int k = j + 1; k < students[i].size(); k++) {
                    int gap = Math.abs(node_list[students[i].get(j)].color - node_list[students[i].get(k)].color);
                    if (gap <= 5 && type == 0)
                        s = s + (1 << (5 - gap));
                    else if (gap <= 5 && type == 1)
                        s = s + ((5 - gap) << 1);

                }

        }
        return s / (students.length - 1);

    }

    ArrayList<node> finding_kempe_chain(node n, int i, int j) {
        Queue<node> q = new LinkedList<node>();
        int[] queued = new int[node_list.length];
        ArrayList<node> kempe = new ArrayList<node>();
        q.add(n);
        queued[n.course_id] = 1;
        kempe.add(n);
        while (!q.isEmpty()) {
            node temp = q.poll();
            for (node t : temp.edge_list) {
                if (queued[t.course_id] != 1) {
                    if (t.color == i || t.color == j) {
                        q.add(t);
                        queued[t.course_id] = 1;
                        kempe.add(t);
                    }

                }
            }

        }
        return kempe;

    }

    double kempe_chain_heuristic(int type) {
        double min_penalty = calculate_penalty(type);
        int c = 0;
        while (true) {
            for (int i = 1; i < node_list.length; i++) {
                node temp = node_list[i];
                for (int j = 0; j < number_of_color; j++) {
                    c++;
                    ArrayList<node> t = finding_kempe_chain(temp, temp.color, j);
                    if (t.size() == 1)
                        continue;
                    int color0 = temp.color;
                    int color1 = j;
                    for (node n : t) {
                        if (n.color == color0)
                            n.color = color1;
                        else
                            n.color = color0;
                    }
                    double p = calculate_penalty(type);
                    if (min_penalty < p) {
                        for (node n : t) {
                            if (n.color == color0)
                                n.color = color1;
                            else
                                n.color = color0;
                        }

                    } else {
                        min_penalty = p;
                    }

                }
            }
            if (c > 1000)
                break;
        }
        return min_penalty;

    }

    boolean pair_swap(node n1, node n2) {
        for (node n : n1.edge_list) {
            if (n.color == n2.color)
                return false;
        }
        for (node n : n2.edge_list) {
            if (n.color == n1.color)
                return false;
        }
        n1.color = n1.color + n2.color;
        n2.color = n1.color - n2.color;
        n1.color = n1.color - n2.color;
        return true;
    }

    double pair_swap_heuristic(int type) {
        double min_penalty = calculate_penalty(type);
        int c = 0;
        while (true) {
            for (int i = 1; i < node_list.length - 1; i++) {
                for (int j = i + 1; j < node_list.length; j++) {
                    c++;
                    if (pair_swap(node_list[i], node_list[j])) {
                        double t = calculate_penalty(type);
                        if (t > min_penalty) {
                            pair_swap(node_list[i], node_list[j]);
                        } else {
                            min_penalty = t;
                        }
                    }

                }
            }
            if (c > 1000)
                break;

        }
        return min_penalty;
    }

    void largest_degree_heuristic() {
        PriorityQueue<node> queue = new PriorityQueue<>(new Comparator<node>() {
            @Override
            public int compare(node n1, node n2) {
                return n2.edge_list.size() - n1.edge_list.size();
            }
        });

        start_coloring(queue);

    }

    void saturation_degree_heuristic() {
        while (true) {
            node max = null;
            for (node n : node_list) {
                if (n.color != -1)
                    continue;
                if (max == null) {
                    max = n;
                } else if (n.neighbor_color.size() > max.neighbor_color.size()) {
                    max = n;
                } else if (n.neighbor_color.size() == max.neighbor_color.size()) {
                    if (n.unassiged_neighbor > max.unassiged_neighbor) {
                        max = n;
                    }
                }
            }
            if (max == null)
                break;
            max.Assign_color();
        }

        daywise_assign_exam();

    }

    void largest_enrollment_heuristic() {
        PriorityQueue<node> queue = new PriorityQueue<>(new Comparator<node>() {
            @Override
            public int compare(node n1, node n2) {
                return n2.num_of_students - n1.num_of_students;
            }
        });

        start_coloring(queue);

    }

    void random_heuristic() {
        Queue<node> q = new LinkedList<node>();
        int[] queued = new int[node_list.length];
        for (int i = 0; i < node_list.length; i++) {
            queued[i] = 0;
            if (node_list[i].color != -1)
                continue;
            q.add(node_list[i]);
            queued[i] = 1;
            while (!q.isEmpty()) {
                node temp = q.poll();
                temp.Assign_color();
                for (node n : temp.edge_list) {
                    if (n.color == -1 && queued[n.course_id] == 0) {
                        q.add(n);
                        queued[n.course_id] = 1;
                    }
                }
            }

        }
        daywise_assign_exam();

    }

}
