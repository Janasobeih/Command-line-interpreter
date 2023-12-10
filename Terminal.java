import java.sql.SQLOutput;
import java.util.Scanner;
import java.util.*;
import java.nio.file.*;
import java.io.*;

class Parser {
    String commandName;
    String[] args;
    ArrayList<String> function = new ArrayList<>();

    public boolean success;

    public Parser() {
        args = new String[0];
    }

    public void addFunction() {
        function.add("echo");
        function.add("history");
        function.add("ls");
        function.add("ls -r");
        function.add("mkdir");
        function.add("rmdir");
        function.add("rm");
        function.add("cp");
        function.add("cd");
        function.add("pwd");
        function.add("touch");
        function.add("cat");
        function.add("wc");
        function.add("cp-r");
        function.add("exit");

    }

    public boolean parse(String input) {
        addFunction();
        boolean exists = false;
        String[] argParts = input.split(" ", 2);
        commandName = argParts[0];

        for (int i = 0; i < function.size(); i++) {
            if (commandName.equals(function.get(i))) {
                exists = true;
                break;
            }
        }
        if (exists == true) {
            if (argParts.length > 1) {
                if (argParts[1].equals("-r")) {
                    commandName = argParts[0] + " " + argParts[1];
                } else {
                    args = argParts[1].split("[,\\s]+");
                }
            } else {
                args = new String[0];
            }

            return true;

        } else {
            System.out.println("Error command not found or invalid parameters are entered");
            return false;
        }
    }


    public void setCommandNames(String c) {
        commandName = c;
    }

    public String getCommandName() {
        return commandName;
    }

    public String[] getArgs() {
        return args;

    }


}

public class Terminal {
    public static Parser parser = new Parser();
    public Vector<String> history = new Vector<>();
    Scanner scanner = new Scanner(System.in);
    boolean flag = true;


    public void history(Vector<String> historyArr) {

        if (parser.args.length == 0) {
            for (int i = 0; i < historyArr.size(); i++) {

                System.out.println(historyArr.get(i));

            }
            parser.success = true;
        } else {
            System.out.println("Error");
            parser.success = false;
        }
    }

    public void echo(String[] args) {
        if (parser.args.length != 0) {
            for (int i = 0; i < args.length; i++) {
                System.out.print(args[i]);
                System.out.print(" ");
                parser.success = true;

            }

        } else {
            System.out.println("No arguments were added");
            parser.success = false;

        }

    }

    public static String pwd() {
        if (parser.args.length == 0) {
            String directory = System.getProperty("user.dir");
            parser.success = true;
            return directory;
        } else {
            String error = "invalid";
            parser.success = false;
            return error;

        }

    }

    public void exit() {
        scanner.close();
        flag = false;
        parser.success = true;

    }

    // Cat command print the content of one file or concatenate the content of two files and print them.
    public static void cat(String[] Files) {
        String firstArg = "";
        String secondArg = "";
        Boolean correct = true;
        int j = 0;
        for (int i = 0; i < Files.length; i++) {
            if (!Files[i].contains(".txt")) {
                firstArg += Files[i] + " ";
            } else {
                firstArg += Files[i];
                j = i + 1;
                break;
            }
        }
        if (j != Files.length - 1) {
            for (int i = j; i < Files.length; i++) {
                if (!Files[i].contains(".txt")) {
                    secondArg += Files[i] + " ";
                } else {
                    secondArg += Files[i];
                    if (i != Files.length - 1) {
                        System.out.println("The function only takes two arguments!");
                        correct = false;
                        break;
                    }
                }
            }
        }
        if (correct) {
            if (secondArg.equals("")) {
                Path currentDirectory = Paths.get(System.getProperty("user.dir"));
                Path absolutePath = currentDirectory.resolve(firstArg).toAbsolutePath();
                try (BufferedReader lineReadFromFile = new BufferedReader(new FileReader(absolutePath.toString()))) {
                    String FileLine1;
                    while ((FileLine1 = lineReadFromFile.readLine()) != null)  //file reading
                    {
                        System.out.println(FileLine1);
                        parser.success = true;
                    }
                } catch (IOException problem)  //problems
                {
                    System.err.println("Error reading the file: " + problem.getMessage());
                    parser.success = false;
                }

            }
            //handling the case of two files passed, so concatenating then printing the two files content.
            else {
                // Concatenate and print the content of two files
                Path currentDirectory = Paths.get(System.getProperty("user.dir"));
                Path absolutePath1 = currentDirectory.resolve(firstArg).toAbsolutePath();
                Path absolutePath2 = currentDirectory.resolve(secondArg).toAbsolutePath();
                try (BufferedReader lineReadFromFileOne = new BufferedReader(new FileReader(absolutePath1.toString()));   //for fist file
                     BufferedReader lineReadFromFileTwo = new BufferedReader(new FileReader(absolutePath2.toString())))     //for second file
                {
                    String FileLine1;
                    // Read and print the content of the first file
                    while ((FileLine1 = lineReadFromFileOne.readLine()) != null) {
                        System.out.println(FileLine1);
                    }

                    // Read and print the content of the second file
                    while ((FileLine1 = lineReadFromFileTwo.readLine()) != null) {
                        System.out.println(FileLine1);
                    }
                    parser.success = true;
                } catch (IOException isssue) {
                    System.err.println("Error reading the file: " + isssue.getMessage());
                    parser.success = false;
                }

            }
        } else {
            // Handling input problems
            //no arguments passed and cat command must take atleast one argument (file)
            if (Files.length == 0) {
                System.out.println("There are no file names provided. Please retry."); //error  message prompted.
                parser.success = false;
            }
            // Handling input problems
            //more than 2 arguments passed and cat command max takes 2 files
            else {
                System.out.println("Error!");  //error  message prompted.
                parser.success = false;
            }
            //handling the case of one file, so printing the file content.
        }
    }

    public void mkdir(String[] args)
    //know the parser.success=false or true hal tethat felhistory low fel wala la
    {
        if (args.length == 0) {                                                           //checks if it takes arguments
            System.out.println("No arguments were provided.");
            parser.success = false;
            return;
        }


        for (String arg : args)                                         //loops on all arguments
        {
            if (arg.contains("/") || arg.contains("\\")) {
                //checks the argument is a path
                Path currentDirectory = Paths.get(System.getProperty("user.dir"));
                Path absolutePath = currentDirectory.resolve(arg).toAbsolutePath().normalize();
                File directory = absolutePath.toFile();
                if (!directory.exists()) {                              //All possibilities of creating a directory from a path
                    boolean success = directory.mkdirs();
                    if (success) {
                        System.out.println("A new directory is created : " + directory.getAbsolutePath());
                        parser.success = true;
                    } else {
                        System.err.println("Couldn't create a new directory: " + directory.getAbsolutePath());
                        parser.success = false;
                    }
                } else {
                    System.err.println("This directory name already exists: " + directory.getAbsolutePath());
                    parser.success = true;

                }
            } else {
                //the given argument is a directory name
                //File currentDirectory = new File(".");

                Path currentDirectory = Paths.get(System.getProperty("user.dir"));
                Path absolutePath = currentDirectory.resolve(arg).toAbsolutePath();
                File newDirectory = absolutePath.toFile();
                ;

                if (!newDirectory.exists()) {
                    File directory = absolutePath.toFile();
                    boolean success = newDirectory.mkdir();
                    if (success) {
                        System.out.println("A new directory is created: " + newDirectory.getAbsolutePath());
                        parser.success = true;

                    } else {
                        System.err.println("Couldn't create a new directory: " + newDirectory.getAbsolutePath());
                        parser.success = false;

                    }
                } else {
                    System.err.println("This directory name already exists: " + newDirectory.getAbsolutePath());
                    parser.success = true;

                }
            }
        }
    }


    public void rmdir(String[] args) {
        String WholeArgument = new String();
        for (int i = 0; i < args.length; i++) {
            WholeArgument += args[i];
            if (i != args.length - 1) {
                WholeArgument += ' ';
            }

        }

        if (WholeArgument.equals("*")) {
            File currentDirectory = new File(System.getProperty("user.dir"));
            File[] files = currentDirectory.listFiles();
            if (files.length != 0) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        if (files[i].list().length == 0) {
                            if (files[i].delete()) {
                                System.out.println("Directory " + files[i].getAbsolutePath() + " has been deleted: ");
                                parser.success = true;
                            } else {
                                System.err.println("Couldn't delete directory: " + files[i].getAbsolutePath());
                                parser.success = false;
                            }
                        }
                    }
                }
            }
        } else {
            Path currentDirectory = Paths.get(System.getProperty("user.dir"));
            Path absolutePath = currentDirectory.resolve(WholeArgument).toAbsolutePath().normalize();
            if (Files.exists(absolutePath) && Files.isDirectory(absolutePath)) {
                File directory = absolutePath.toFile();
                //System.out.println(absolutePath);

                if (directory.list().length == 0) {
                    if (directory.delete()) {
                        System.out.println("The given directory has been deleted: " + absolutePath);
                        parser.success = true;
                    } else {
                        System.err.println("Couldn't delete the given directory: " + absolutePath);
                        parser.success = false;
                    }
                } else {
                    System.err.println("Directory is not empty");
                    parser.success = true; // Set parser.success to false
                }
            } else {
                System.err.println("The given directory is invalid");
                parser.success = false;
            }
        }
    }

    public static void cp(String[] args) throws IOException {
        String firstArg = new String();
        String secondArg = new String();
        Boolean Two = true;
        int j = 0;
        for (int i = 0; i < args.length; i++) {
            if (!args[i].contains(".txt")) {
                firstArg += args[i] + " ";
            } else {
                firstArg += args[i];
                j = i + 1;
                break;
            }
        }
        for (int i = j; i < args.length; i++) {
            if (!args[i].contains(".txt")) {
                secondArg += args[i] + " ";
            } else {
                secondArg += args[i];
                if (i != args.length - 1) {
                    System.out.println("The function only takes two arguments!");
                    Two = false;
                    break;
                }
            }
        }
        if (Two) {
            Path currentDirectory = Paths.get(System.getProperty("user.dir"));
            Path p1 = currentDirectory.resolve(firstArg).toAbsolutePath();
            Path p2 = currentDirectory.resolve(secondArg).toAbsolutePath();
            File src = new File(p1.toString());
            File dest = new File(p2.toString());
            Path currentDir = Paths.get(System.getProperty("user.dir"));
            System.out.println(currentDir);
            if (src.exists()) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(dest, false)); //destination
                Scanner scanner = new Scanner(src); //source
                String line;
                while (scanner.hasNextLine()) {
                    line = scanner.nextLine();
                    writer.write(line);
                    writer.newLine();

                }
                System.out.println("wrote successfully");
                parser.success = true;
                writer.close();
            } else {
                parser.success = false;
                System.out.println("invalid filename");
            }
        }
    }

    public void cd(String Args[]) {
        if (Args[0].equals("..") && Args.length == 1) {
            Path currentDir = Paths.get(System.getProperty("user.dir"));
            if (currentDir.getParent() != null) {
                System.setProperty("user.dir", currentDir.getParent().toString());
                parser.success = true;
                Path NewDir = Paths.get(System.getProperty("user.dir"));
                System.out.println("Directory changed from:" + currentDir + " to " + NewDir);
            } else {
                System.out.println("No parent directory");
                parser.success = false;
            }
        } else {
            String WholeArgument = new String();
            for (int i = 0; i < Args.length; i++) {
                WholeArgument += Args[i];
                if (i != Args.length - 1) {
                    WholeArgument += ' ';
                }
            }
            Path currentDirectory = Paths.get(System.getProperty("user.dir"));
            Path AbsPath = currentDirectory.resolve(WholeArgument).toAbsolutePath().normalize();
            if (AbsPath.toFile().isDirectory()) {
                Path currentDir = Paths.get(System.getProperty("user.dir"));
                System.setProperty("user.dir", AbsPath.toString());
                parser.success = true;
                Path newPath = Paths.get(System.getProperty("user.dir"));
                System.out.println("Directory changed from: " + currentDir + " to " + newPath);
            } else {
                System.out.println("The path isn't a directory.");
                System.out.println(AbsPath);
                parser.success = false;
            }
        }
    }

    public void cd() {
        String initialPath = System.getProperty("user.dir");

        String homeDir = System.getProperty("user.home");
        System.setProperty("user.dir", homeDir);

        String currentPath = System.getProperty("user.dir");
        System.out.println("Directory changed from:" + initialPath + " to " + currentPath);
        parser.success = true;
    }


    public void ls() {
        String[] argsArr = parser.getArgs();
        if (argsArr.length != 0) {
            System.out.println("ERROR! ls command does not take any arguments");
            parser.success = false;
        } else {
            File currDirFile = new File(System.getProperty("user.dir"));
            File[] FilesArr = currDirFile.listFiles();
            if (FilesArr == null) {
                System.out.println("The Directory is Empty");
            } else {
                for (int i = 0; i < FilesArr.length; i++) {
                    System.out.println(FilesArr[i].getName());
                }
            }
            parser.success = true;

        }
    }

    public static void ls_r() {
        String[] argsArr = parser.getArgs();
        if (argsArr.length != 0) {
            System.out.println("ERROR! ls -r command does not take any arguments");
            parser.success = false;
        } else {
            File currDirFile = new File(System.getProperty("user.dir"));
            File[] FilesArr = currDirFile.listFiles();

            if (FilesArr == null) {
                System.out.println("The Directory is Empty");
            } else {
                for (int i = FilesArr.length - 1; i >= 0; i--) {
                    System.out.println(FilesArr[i].getName());
                }
            }
            parser.success = true;
        }
    }

    public void wc(String Args[]) throws FileNotFoundException {
        String WholeArgument = new String();
        for (int i = 0; i < Args.length; i++) {
            WholeArgument += Args[i];
            if (Args.length > 1 && i != Args.length - 1) {
                WholeArgument += ' ';
            }
        }
        Path currentDirectory = Paths.get(System.getProperty("user.dir"));
        Path p1 = currentDirectory.resolve(WholeArgument).toAbsolutePath();
        File file = new File(p1.toString());
        Scanner scanner = new Scanner(file);
        if (file.exists()) {
            int lineCounter = 0, wordCounter = 0, charCounter = 0;
            while (scanner.hasNextLine()) {
                lineCounter++;
                wordCounter++;
                String sentence = scanner.nextLine();
                charCounter += sentence.length();
                for (int i = 0; i < sentence.length(); i++) {
                    if (sentence.charAt(i) == ' ') {
                        wordCounter++;
                    }
                }
            }
            System.out.println(lineCounter + " " + wordCounter + " " + charCounter + " " + WholeArgument);
            parser.success = true;
        }
    }
    // Takes one argument (full or relative path ending with a file name) and creates the specified file in the PC.
    public static void touch(String[] args) {
        if (args.length != 1) {
            System.out.println("Error: touch command takes exactly one argument ");
            parser.success = false;

        } else {

            String filePath = args[0];  //the file

            Path path;
            if (filePath.contains(":") || filePath.startsWith("\\\\"))               //indication for full path
            {
                path = Paths.get(filePath);
            } else {
                path = Paths.get(System.getProperty("user.dir"), filePath);          //indication for short path
            }

            if (Files.notExists(path))            //in case of file doesnot exist so no problem
            {
                if (fileCreation(path))                 //no problem arised
                {
                    System.out.println("File created successfully: ");
                    System.out.println(path.toAbsolutePath());
                    //   startagain = false;           //false so terminate the command, no error
                } else {
                    System.err.println("Error to create the file. Please try again.");
                }
            } else {
                System.out.println("File already exists: ");
                System.out.println(path.toAbsolutePath());  //showing the path of existing file
            }

            parser.success = true;

        }
    }

    private static boolean fileCreation(Path path) {
        if (Files.exists(path.getParent())) {
            try {
                Files.createFile(path);
                return true;         //successful
            } catch (Exception exceptionnnnnn) {
                return false;          //unsucsessful
            }
        }
        return false;
    }

    public void rm(String Args[]) {
        String WholeArgument = new String();
        Boolean one = true;
        for (int i = 0; i < Args.length; i++) {
            WholeArgument += Args[i];
            if (Args.length > 1 && i != Args.length - 1) {
                WholeArgument += ' ';
            }
            if (Args[i].contains(".txt") && i != Args.length - 1) {
                System.out.println("This function takes only one argument!");
                one = false;
            }
        }
        if (one) {
            Path currentDirectory = Paths.get(System.getProperty("user.dir"));
            Path p1 = currentDirectory.resolve(WholeArgument).toAbsolutePath();
            File file = new File(p1.toString());
            if (file.exists()) {
                if (file.delete()) {
                    parser.success = true;
                    System.out.println("File:" + WholeArgument + " got deleted");


                } else {
                    System.out.println("failed.");
                    parser.success = false;
                }
            } else {
                System.out.println("File:" + WholeArgument + " doesn't exist.");
                parser.success = false;
            }
        }
    }


    public void chooseCommandAction(String input)           //checks the user input to run the command
    {
        if (input.equals("echo"))
            echo(parser.args);

        else if (input.equals("ls -r"))
            ls_r();

        else if (input.equals("ls"))
            ls();

        else if (input.equals("history"))
            history(this.history);

        else if (input.equals("exit"))
            exit();

        else if (input.equals("mkdir"))
            mkdir(parser.args);

        else if (input.equals("rmdir"))
            rmdir(parser.args);

        else if (input.equals("cp")) {
            try {
                cp(parser.args);
            } catch (IOException e) {
                //throw new RuntimeException(e);
                System.out.println("ERROR!");
            }
        } else if (input.equals("cd")) {
            if (parser.getArgs().length > 0) {
                cd(parser.getArgs());
            } else {
                cd();
            }
        } else if (input.equals("rm"))
            rm(parser.args);


        else if (input.equals("wc")) {
            try {
                wc(parser.getArgs());
            } catch (FileNotFoundException e) {
                parser.success = false;
                System.out.println("file doesn't exist");
            }
        } else if (input.equals("pwd")) {
            System.out.println(pwd());
        } else if (input.equals("cat"))
            cat(parser.args);

        else if (input.equals("touch"))
            touch(parser.args);

        else
            System.out.println("Invalid command");

    }

    public static void main(String[] args) {
        Terminal terminal = new Terminal();
        while (terminal.flag) {
            System.out.println("Enter your command");
            String userInput = terminal.scanner.nextLine();

            if (terminal.parser.parse(userInput) == true) {
                //System.out.println(terminal.parser.getCommandName());
                terminal.chooseCommandAction(terminal.parser.getCommandName());
                System.out.println();
                if (terminal.parser.success == true)
                    terminal.history.add(terminal.parser.getCommandName());
            }
        }
    }
}
/*
selective set OR bakhtar set 3ayza anawar feha biits  mo3yana
selective complement bakhtar set 3ayza ageb el compliment beta3 selected bits (XOR)
el fo2 howa elbghyr feh w el taht howa my reference
momken ay operaation msh shart el standard
el 3ayz a3mel 3aleh el operation de bahot tahto one
el mask 3aks el clear
el clear el haga el osad el 1 btetsafar laken el mask el haga el osad el zero heya el btetsafar
a xor a = 0
fa a = a xor a heya heya akeny 3malt clear lel a
el cases el el dr kan byshra7ha kan msabbet taht 1100


*?
 */