package ServerStuff.Commands;

import CityStructure.CityTree;
import ClientStuff.User;
import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

public class Save implements Command {
    private Gson gson = new Gson();
    private String msg;
    static Logger logger = Logger.getLogger("SaveLogger");

    @Override
    public void execute(CityTree cityTree, User user) {
        BufferedWriter bw = null;
        FileWriter fw = null;
        String path = System.getenv("HOME");
        String strCollection = new String();
        path = path + System.getProperty("file.separator") + "SavedCollection.json";


        try {
            strCollection = gson.toJson(cityTree);

            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
                fw = new FileWriter(file);
                bw = new BufferedWriter(fw);
                bw.write(strCollection);
                msg = ("Collection saved successfully");
                logger.info("Collection saved successfully");
            }else if (!file.canRead() || !file.canWrite()){
                logger.severe("File is unreachable");
                msg = ("File is unreachable");
            }else {
                fw = new FileWriter(file);
                bw = new BufferedWriter(fw);
                bw.write(strCollection);
                logger.info("Collection saved successfully");
                msg = ("Collection saved successfully");
            }
        } catch (IOException e) {
            msg = ("Error occurred");
            logger.warning("Error occurred");
        } catch (OutOfMemoryError e) {
            msg = ("Collection is too big, out of memory");
            logger.warning("Collection is too big, out of memory");
        }catch (SecurityException e){
            msg = ("Impossible to create file in this directory");
            logger.warning("Impossible to create file in this directory");
        }finally {
            try{
                if (bw != null) {
                    bw.close();
                }
                if (fw != null){
                    fw.close();
                }
            }catch(IOException e) {
                msg = ("Error while closing the BufferedWriter");
                logger.warning("Error while closing the BufferedWriter");
            }
        }


    }

    @Override
    public String getMessage() {
        return this.msg;
    }

}
