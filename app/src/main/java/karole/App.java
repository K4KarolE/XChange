package karole;


public class App 
{
    
    static Functions fs = new Functions();

    public static void main( String[] args )
    {
        fs.appStartsGroupedActions();
        System.out.println(fs.canGetNewApiRequest());
    }
}