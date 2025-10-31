public class WordCounter {

    public static StringBuffer processFile(string path) throws EmptyFileException {
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
            while((line = br.readline()) != null){
                sb.append(line).append(" ");
            }
        }

        catch(IOException e){
            System.out.println("Error REading File: " + e.getMessage());
        }

        if(sb.toString().trim().isEmpty()){
            throw new EmptyFileException("Empth File: " + file.getPath());
        }

        return new StringBuffer(sb.toString());
    }


    public static int processText(StringBuffer text, String stopword) throws InvalidStopwordException, TooSmallText {
        Pattern regex = Pattern.compile("[a-zA-Z0-9]+");
        Matcher matcher = regex.matcher(text);

        int count = 0;
        boolean stopFound = (stopword == null);

        while(matcher.find()){
            count++;
            if(!stopFound && matcher.group().equalsIgnoreCase(stopword)){
                stopFound = true;
                break;
            }
        }

        if (count < 5) {
            throw new TooSmallText("Text too small. only " + count + " words");
        }

        if(!stopFound && stopword != null){
            throw new InvalidStopwordException("Stopword not found: " + stopword);
        }

        return count;
    }

}