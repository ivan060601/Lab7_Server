package ServerStuff.Commands;

public class LoopException extends Exception {
    public LoopException(String error){
        super(error);
    }

    public LoopException(){
        super();
    }
}