package ServerStuff.Commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

public class LoopAnalyzer {
    private String path;
    private HashSet<String> executeSet = new HashSet<>();
    private boolean bool = false;

    public LoopAnalyzer(){
        path = System.getenv("HOME") + System.getProperty("file.separator");

    }

    public boolean isLooped(String fileName) throws FileNotFoundException {
        fileName = path + fileName;
        executeSet.add(fileName);
        File script = new File(fileName);
        Scanner scanner = new Scanner(script);
        try {
            while (scanner.hasNext()) {
                String newLine = scanner.nextLine().trim();
                String[] arr = newLine.split(" ", 2);
                if (arr[0].equals("execute_script") && arr[1] != null) {
                    if (executeSet.contains(path + arr[1])) {
                        throw new LoopException("Loop");
                    } else {
                        this.isLooped(arr[1]);
                        executeSet.remove(path+arr[1]);
                    }
                }
            }
        }catch (LoopException e){
            bool = true;
        }

        return bool;
    }
}
