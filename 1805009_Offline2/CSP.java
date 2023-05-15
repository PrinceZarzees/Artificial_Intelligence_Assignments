import java.util.ArrayList;
import java.util.Random;

class CSP {
  int num;
  Variable[][] mat;
  ArrayList<Variable> Unassigned;

  CSP(int n) {
    num = n;
    mat = new Variable[n][n];
    for (int i = 0; i < n; i++)
      for (int j = 0; j < n; j++) {
        mat[i][j] = new Variable();
        for (int k = 1; k <= n; k++)
          mat[i][j].Add(k);
        mat[i][j].row = i;
        mat[i][j].col = j;

      }
    Unassigned = new ArrayList<Variable>();
  }

  CSP(CSP obj) {
    Unassigned = new ArrayList<Variable>();
    num = obj.num;
    mat = new Variable[num][num];
    for (int i = 0; i < num; i++)
      for (int j = 0; j < num; j++) {
        mat[i][j] = new Variable();
        mat[i][j].value = obj.mat[i][j].value;
        for (int k : obj.mat[i][j].list_values)
          mat[i][j].Add(k);
        mat[i][j].row = i;
        mat[i][j].col = j;
        if (mat[i][j].value == 0)
          Unassigned.add(mat[i][j]);
      }

  }

  void initialize(int arr[][], int method) {
    for (int i = 0; i < num; i++)
      for (int j = 0; j < num; j++) {
        if (method == 0)
          Assign_fc(mat[i][j], arr[i][j]);
        else
          Assign_simple_backtrack(mat[i][j], arr[i][j]);
      }
  }

  boolean Assign_fc(Variable var, int value) {
    if (var.value != 0)
      return false;
    if (!var.list_values.contains(value))
      return false;
    if (value == 0) {
      var.value = value;
      Unassigned.add(var);
      return true;
    }
    Unassigned.remove(var);
    var.list_values.clear();
    var.Add(value);
    var.value = value;
    int i = var.row;
    for (int k = 0; k < num; k++) {
      if (k == var.col)
        continue;
      mat[i][k].Remove(value);
      if (mat[i][k].list_values.isEmpty())
        return false;

    }
    int j = var.col;
    for (int k = 0; k < num; k++) {
      if (k == var.row)
        continue;
      mat[k][j].Remove(value);
      if (mat[k][j].list_values.isEmpty())
        return false;

    }

    return true;
  }

  boolean Assign_simple_backtrack(Variable var, int value) {
    if (var.value != 0)
      return false;
    if (!var.list_values.contains(value))
      return false;
    if (value == 0) {
      var.value = value;
      Unassigned.add(var);
      return true;
    }
    Unassigned.remove(var);
    int i = var.row;
    int j = var.col;
    for (int k = 0; k < num; k++) {

      if (mat[i][k].value == value)
        return false;
      if (mat[k][j].value == value)
        return false;

    }
    var.value = value;
    return true;
  }

  int degree_count_fc(int i, int j) {
    int deg_count = 0;
    for (int k = 0; k < num; k++) {
      if (k != j)
        if (mat[i][k].value == 0) {
          deg_count++;
        }
      if (k != i)
        if (mat[k][j].value == 0) {
          deg_count++;
        }
    }
    return deg_count;
  }

  int degree_count_bt(int i, int j) {
    int deg_count = 0;
    for (int k = 0; k < num; k++) {
      if (k != j)
        if (mat[i][k].value == 0) {
          deg_count++;
        }
      if (k != i)
      if (mat[k][j].value == 0) {
        deg_count++;
      }
    }
    return deg_count;

  }

  int domain_count_fc(int i, int j) {
    return mat[i][j].list_values.size();
  }

  int domain_count_bt(int i, int j) {
    for (int k = 0; k < num; k++) {
      if (mat[i][k].value != 0)
        mat[i][j].Remove(mat[i][k].value);
      if (mat[k][j].value != 0)
        mat[i][j].Remove(mat[k][j].value);
    }
    int temp = mat[i][j].list_values.size();
    for (int k = 0; k < num; k++) {
      if (mat[i][k].value != 0)
        mat[i][j].Add(mat[i][k].value);
      if (mat[k][j].value != 0)
        mat[i][j].Add(mat[k][j].value);
    }
    return temp;

  }

  Variable VAH1(int method) {
    int domain_size = num + 1;
    Variable temp = mat[0][0];
    for (Variable var : Unassigned) {
      int i = var.row;
      int j = var.col;
      if (mat[i][j].value != 0)
        continue;
      int domain_count;
      if (method == 0)
        domain_count = domain_count_fc(i, j);
      else
        domain_count = domain_count_bt(i, j);
      if (domain_count < domain_size) {
        temp = mat[i][j];
        domain_size = domain_count;
      }
    }
    return temp;
  }

  Variable VAH2(int method) {
    int degree = 0;
    Variable temp = mat[0][0];
    for (Variable var : Unassigned) {
      int i = var.row;
      int j = var.col;
      if (mat[i][j].value != 0)
        continue;
      int deg_count;
      if (method == 0)
        deg_count = degree_count_fc(i, j);
      else
        deg_count = degree_count_bt(i, j);
      if (deg_count > degree || degree == 0) {
        degree = deg_count;
        temp = mat[i][j];
      }

    }
    return temp;

  }

  Variable VAH3(int method) {
    int domain_size = num + 1;
    Variable temp = mat[0][0];
    int degree_size = -1;
    for (Variable var : Unassigned) {
      int i = var.row;
      int j = var.col;
      if (mat[i][j].value != 0)
        continue;
      int domain_count;
      int degree_count;
      if (method == 0) {
        domain_count = domain_count_fc(i, j);
        degree_count = degree_count_fc(i, j);
      } else {
        domain_count = domain_count_bt(i, j);
        degree_count = degree_count_bt(i, j);
      }
      if (domain_count < domain_size) {
        temp = mat[i][j];
        domain_size = domain_count;
        degree_size = degree_count;
      } else if (domain_count == domain_size) {
        if (degree_count >= degree_size) {
          temp = mat[i][j];
          domain_size = domain_count;
          degree_size = degree_count;

        }
      }
    }
    return temp;

  }

  Variable VAH4(int method) {
    double ratio = -1;
    Variable temp = mat[0][0];
    for (Variable var : Unassigned) {
      int i = var.row;
      int j = var.col;
      if (mat[i][j].value != 0)
        continue;
      int domain_count;
      int degree_count;
      if (method == 0) {
        domain_count = domain_count_fc(i, j);
        degree_count = degree_count_fc(i, j);
      } else {
        domain_count = domain_count_bt(i, j);
        degree_count = degree_count_bt(i, j);
      }
      double t_ratio = domain_count / ((double) (degree_count) + 1);

      if (ratio == -1 || t_ratio < ratio) {
        temp = mat[i][j];
        ratio = t_ratio;
      }
    }
    return temp;

  }

  Variable VAH5(int method) {
    Random rand = new Random();

    int idx = rand.nextInt(Unassigned.size());

    return Unassigned.get(idx);

  }

  Variable get_next_Variable(int method, int vah_method) {
    if (vah_method == 1) {
      return VAH1(method);
    } else if (vah_method == 2) {
      return VAH2(method);
    } else if (vah_method == 3) {
      return VAH3(method);
    } else if (vah_method == 4)
      return VAH4(method);
    else if (vah_method == 5)
      return VAH5(method);

    return null;
  }

  boolean isSolved() {
    if (Unassigned.size() == 0)
      return true;
    return false;

  }

  void print() {
    for (int i = 0; i < num; i++) {
      for (int j = 0; j < num; j++) {
        System.out.print(String.format("%-2d", mat[i][j].value) + " ");

      }
      System.out.println();
    }
  }

}