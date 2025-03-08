package karole;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogToFile {
    // Cheers all! - https://stackoverflow.com/q/15758685

    static boolean beLoggedToFiles = true;
    static Logger logger = Logger.getLogger("");

    static void createLogger() {
        try {
            if (beLoggedToFiles) {
                SimpleDateFormat format = new SimpleDateFormat("MM-dd_HHmmss");
                FileHandler fileHandler = new FileHandler("./logs/log_" + format.format(Calendar.getInstance().getTime()) + ".log");
                SimpleFormatter formatter = new SimpleFormatter();
                fileHandler.setFormatter(formatter);
                logger.addHandler(fileHandler);
            }
            logger.info("Logger created");

        } catch (SecurityException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}