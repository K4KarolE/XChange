package karole;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogToFile {
    // Cheers all!
    // https://stackoverflow.com/q/15758685

    static Logger logger = Logger.getLogger("");
    static FileHandler fileHandler;

    static void createLogger() {
        try {
            SimpleDateFormat format = new SimpleDateFormat("MM-dd_HHmmss");
            fileHandler = new FileHandler("./logs/log_" + format.format(Calendar.getInstance().getTime()) + ".log");
            logger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            logger.info("Logger created");

        } catch (SecurityException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
