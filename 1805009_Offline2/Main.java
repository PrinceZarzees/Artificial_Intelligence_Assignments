import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Main {
    public static void main(String[] args) throws Exception {

        File file = new File("d-10-07.txt");

        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        st = br.readLine();
        int i = 0;
        int[][] arr = new int[Integer.valueOf(st)][Integer.valueOf(st)];
        while ((st = br.readLine()) != null) {
            st = st.trim();
            String[] temp = st.split("[ ]*,[ ]*");
            for (int j = 0; j < temp.length; j++)
                arr[i][j] = Integer.valueOf(temp[j]);
            i++;
        }

        CSP csp = new CSP(arr[0].length);
        int f = 1; // 0 forward checking
                   // 1 backtracking
        int vah_method = 1;
        csp.initialize(arr, f);
        CSP_Solver csp_solver = new CSP_Solver(csp, f, vah_method);
        long start = System.currentTimeMillis();
        csp_solver.solve();
        long end = System.currentTimeMillis();
        System.out.println((end - start) / 1000.0);
        csp_solver.print();

    }
}