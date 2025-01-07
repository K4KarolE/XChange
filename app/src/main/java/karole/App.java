package karole;


public class App 
{
    
    static Functions fs = new Functions();

    public static void main( String[] args )
    {
        fs.appStartsGroupedActions();
        fs.generateLastUsedCurrencies();
        
        // fs.saveLastUsedCurrenciesGrouped("EUR", "USD");
        fs.saveLastUsedCurrenciesGrouped("GBP", "HUF");
        
        
        fs.generateLastUsedCurrencies();
        fs.generateRates();

        System.out.println(fs.rateFrom + " " + fs.rateTo);

    }
}