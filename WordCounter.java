import java.io.*;
import java.util.*;
import java.util.regex.*;

public class WordCounter {

    public static StringBuffer processFile(String path) throws EmptyFileException {
        Scanner scanner = new Scanner(System.in);
        File file = new File(path);

        while(!file.exists() || !file.canRead()){
            System.out.println("Cannot open file. Please enter a valid file name:");
            path = scanner.nextLine();
            file = new File(path);
        }

        StringBuilder sb = new StringBuilder();
        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            String line;
            while((line = br.readLine()) != null){
                if (sb.length() > 0) {
                    sb.append(" ");
                }
                sb.append(line);
            }
        } catch(IOException e){
            System.out.println("Error reading file: " + e.getMessage());
        }

        if(sb.toString().trim().isEmpty()){
            // From test case: "EmptyFileException: <filename> was empty"
            throw new EmptyFileException(file.getName() + " was empty");
        }

        return new StringBuffer(sb.toString());
    }


    public static int processText(StringBuffer text, String stopword) throws InvalidStopwordException, TooSmallText {
        Pattern regex = Pattern.compile("[a-zA-Z0-9]+");
        Matcher matcher = regex.matcher(text.toString());

        int totalCount = 0;
        int stopPosition = -1; // index where stopword found
        int pos = 0;

        while(matcher.find()){
            pos++;
            totalCount++;
            if (stopword != null && stopPosition == -1 && matcher.group().equalsIgnoreCase(stopword)){
                stopPosition = pos;
            }
        }

        // If text too small (based on total words), throw TooSmallText
        if (totalCount < 5) {
            throw new TooSmallText("Only found " + totalCount + " words.");
        }

        if (stopword == null) {
            return totalCount;
        }

        if (stopPosition != -1) {
            return stopPosition;
        }

        throw new InvalidStopwordException("Couldn't find stopword: " + stopword);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringBuffer text = new StringBuffer();
        String stopword = null;
        int count = 0;

        try{
            int choice = 0;
            while(choice !=1 && choice != 2){
                System.out.println("Enter 1 to process a file or 2 to process text: ");
                if(scanner.hasNextInt()){
                    choice = scanner.nextInt();
                    // consume only if there is a next line
                    if (scanner.hasNextLine()) {
                        scanner.nextLine();
                    }
                }
                else{
                    if (scanner.hasNextLine()) {
                        scanner.nextLine();
                    }
                }
            }

            if(args.length > 1){
                stopword = args[1];
            }

            
            if (choice == 1) {
                String path = null;
                // If a fileman was provided as first arg, use it
                if (args.length > 0) {
                    path = args[0];
                } else {
                    System.out.println("Enter the filename:");
                    path = scanner.nextLine();
                }

                try {
                    text = processFile(path);
                } catch (EmptyFileException e) {
                    System.out.println(e.getMessage());
                    text = new StringBuffer(""); // continue with empty text
                }

            } else if (choice == 2) {
                System.out.println("Enter your text:");
                text = new StringBuffer(scanner.nextLine());
            }

            try {
                count = processText(text, stopword);
                System.out.println("Found " + count + " words.");
            } catch (InvalidStopwordException e) {
                // print the exception message 
                System.out.println(e.getMessage());
                System.out.println("Enter another stopword:");
                String newStop = null;
                if (scanner.hasNextLine()) {
                    newStop = scanner.nextLine();
                } else {
                    newStop = "";
                }
                try {
                    count = processText(text, newStop);
                    System.out.println("Found " + count + " words.");
                } catch (InvalidStopwordException ex) {
                    System.out.println("Stopword not found again. Exiting.");
                } catch (TooSmallText ex) {
                    System.out.println(ex.toString());
                }
            } catch (TooSmallText e) {
                System.out.println(e.toString());
            }

        } finally {
            scanner.close();
        }
    }
}