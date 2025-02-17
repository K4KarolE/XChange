package karole;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogToFile {
    // Cheers all!
    // https://stackoverflow.com/questions/15758685/how-to-write-logs-in-text-file-when-using-java-util-logging-logger

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
