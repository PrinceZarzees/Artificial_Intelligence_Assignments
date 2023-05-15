import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class node {
  int course_id;
  int num_of_students;
  ArrayList<Integer> student_list;
  ArrayList<node> edge_list;
  Set<Integer> neighbor_color;
  int unassiged_neighbor;
  int color;

  node(int c_id, int nos) {
    course_id = c_id;
    num_of_students = nos;
    student_list = new ArrayList<Integer>(nos);
    edge_list = new ArrayList<node>();
    neighbor_color = new HashSet<Integer>();
    color = -1;
    unassiged_neighbor = 0;
  }

  void Add_student(int st_id) {
    student_list.add(st_id);
  }

  void Add_edge(node nd) {
    for (node i : edge_list) {
      if (i.course_id == nd.course_id)
        return;
    }
    edge_list.add(nd);
    unassiged_neighbor++;
  }

  void Assign_color() {
    ArrayList<Integer> temp = new ArrayList<Integer>();
    for (node n : edge_list) {
      temp.add(n.color);
    }
    Collections.sort(temp);
    int prev = -1;
    for (int i : temp) {
      if ((i - prev) >= 2)
        break;
      prev = i;

    }
    color = prev + 1;
    for (node n : edge_list) {
      n.neighbor_color.add(color);
      n.unassiged_neighbor--;
    }

  }
}