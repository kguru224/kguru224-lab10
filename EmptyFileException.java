import java.io.IOexception;

public class EmptyFileException extends IOexception{
    public EmptyFileException(String message) {
        super(message);
    }

}