import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Scheduling sched;
        int num_of_courses = 0;
        int num_of_students = 0;
        int[] courses = null;
        String crs = "yor-f-83.crs";
        String stu = "yor-f-83.stu";
        ArrayList<Integer>[] students = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(crs));
            while (reader.readLine() != null) {
                num_of_courses++;
            }
            reader.close();
            courses = new int[num_of_courses + 1];
            courses[0] = 0;
            reader = new BufferedReader(new FileReader(crs));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                int cid = Integer.parseInt(parts[0]);
                int nos = Integer.parseInt(parts[1]);
                courses[cid] = nos;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(stu));
            while (reader.readLine() != null) {
                num_of_students++;
            }
            reader.close();
            students = new ArrayList[num_of_students + 1];
            for (int i = 0; i <= num_of_students; i++)
                students[i] = new ArrayList<Integer>();
            reader = new BufferedReader(new FileReader(stu));
            String line;
            int i = 1;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                for (String part : parts) {
                    students[i].add(Integer.parseInt(part));
                }
                i++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sched = new Scheduling(courses, students);
        int type = 0;
        // 0 exponential
        // 1 linear
        sched.largest_degree_heuristic();
        // sched.saturation_degree_heuristic();
        // sched.largest_enrollment_heuristic();
        // sched.random_heuristic();
        System.out.print(sched.number_of_color + " ");
        System.out.print(sched.calculate_penalty(type) + " ");
        // sched.print();
        sched.kempe_chain_heuristic(type);
        System.out.print(sched.calculate_penalty(type) + " ");
        // sched.print();
        sched.pair_swap_heuristic(type);
        System.out.print(sched.calculate_penalty(type) + " ");
        // sched.print();

    }
}
