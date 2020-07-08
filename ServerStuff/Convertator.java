package ServerStuff;

import CityStructure.City;
import CityStructure.CityTree;
import ServerStuff.Checkers.NullPointerChecker;
import ServerStuff.Checkers.WrongFieldChecker;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.logging.Logger;

/**
 * класс, конвертирующий файл в коллекцию
 */
public class Convertator {
    private CityTree collection = new CityTree();
    private Gson gson = new Gson();
    static Logger logger = Logger.getLogger("ConvertatingLogger");

    /**
     * @param jsonPath путь до файла
     * @return коллекцию объектов класса City
     */
    public CityTree toCollection(String jsonPath) {
        File jsonFile = new File(jsonPath);

        if(!jsonFile.exists()){
            logger.severe("File does not exist");
            System.exit(0);
        }
        if(!jsonFile.isFile()){
            logger.severe("This is not a file");
            System.exit(0);
        }
        if(!jsonFile.canRead()){
            logger.severe("File is unreachable");
            System.exit(0);
        }

        NullPointerChecker np = new NullPointerChecker();
        WrongFieldChecker wf = new WrongFieldChecker();
        int counter = 0;

            /*
            while (scanner.hasNext()) {
                data.append(scanner.nextLine());
            }*/
        String data = new String();


        Type collectionType = new TypeToken<CityTree>() {
        }.getType();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(jsonPath), StandardCharsets.UTF_8));
            String line;
            while ((line = br.readLine()) != null) {
                data = data + line;
            }

            CityTree newCity = gson.fromJson(data, collectionType);

            for (City c : newCity) {
                if (!collection.contains(c)) {
                    np.checkEverything(c);
                    wf.checkEverything(c);
                    collection.add(c);
                }
            }


        } catch (JsonSyntaxException e) {
            logger.severe("JSON syntax error. Program will stop automatically");
            System.exit(0);
        } catch (IOException e) {
            //e.printStackTrace();
            logger.severe("Error occurred while reading this file");
            System.exit(0);
        }  catch (NumberFormatException e){
            logger.severe("Some number-fields have incorrect values. Try changing them");
            System.exit(0);
        }

        collection.setAuthDateTime(LocalDateTime.now());
        counter = collection.size();
        logger.info("JSON loaded successfully. " + counter + " new Cities were added to the Tree");

        return collection;
    }

}

