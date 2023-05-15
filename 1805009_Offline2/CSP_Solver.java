public class CSP_Solver {
    CSP obj;
    int method;
    int vah;
    int num_of_node;
    int num_of_bt;

    CSP_Solver(CSP o, int m, int v) {
        obj = new CSP(o);
        method = m;
        vah = v;
        num_of_node = 0;
        num_of_bt = 0;
    }

    boolean Backtrack(CSP o) {
        if (o.isSolved()) {
            return true;
        }
        Variable var;

        var = o.get_next_Variable(method, vah);

        boolean result;
        for (int value : var.list_values) {
            obj = new CSP(o);
            num_of_node++;
            if (method == 0 && obj.Assign_fc(obj.mat[var.row][var.col], value)
                    || method == 1 && obj.Assign_simple_backtrack(obj.mat[var.row][var.col], value)) {
                result = Backtrack(obj);
                if (result)
                    return true;
            } else
                num_of_bt++;

        }

        return false;
    }

    void solve() {
        Backtrack(obj);
    }

    void print() {
        System.out.println("Number of node: " + num_of_node);
        System.out.println("Number of backtrack: " + num_of_bt);
        obj.print();

    }

}