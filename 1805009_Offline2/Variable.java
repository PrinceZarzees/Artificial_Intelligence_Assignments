import java.util.HashSet;
import java.util.Set;
class Variable
{

    Set<Integer>list_values;
    int value;
    int row;
    int col;
    Variable()
    {
        list_values=new HashSet<Integer>();
        value=0;
    }
    void Add(int value)
    {
       list_values.add(value);
    }
    void Remove(int value)
    {
        list_values.remove(value);
    }


}