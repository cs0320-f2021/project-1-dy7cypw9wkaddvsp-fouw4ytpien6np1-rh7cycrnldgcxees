package edu.brown.cs.student.main.repl;
import com.google.common.collect.ImmutableMap;
import edu.brown.cs.student.main.CommandHashmap;
import edu.brown.cs.student.main.handlers.HandlerArguments;
import edu.brown.cs.student.main.handlers.ICommandHandler;
import freemarker.template.Configuration;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.*;
import spark.template.freemarker.FreeMarkerEngine;
import java.io.*;
import java.util.Map;

public class REPL{
    private BufferedReader bufferedReader;
    private CommandHashmap commandHashmap;
    private OptionParser optionParser;

    /**
     * @param args arguments from main
     * @param in: a Reader object
     * @param oP: an OptionParser (command line parser object) that may have accepted options
     * @param cH: a hashmap of keywords to their associated ICommandHandler class
     */ // pass in HandlerArgs set in Main
    public REPL(String[] args, InputStreamReader in, OptionParser oP, CommandHashmap cH){
        // OptionParser: parses command line input
        // ^input because pre-set defaults, required type args like optionParser.accepts("port").withRequiredArg().ofType(Integer.class);
        this.bufferedReader = new BufferedReader(in);
        this.optionParser = oP;
        this.commandHashmap = cH;
        OptionSet optionSet = optionParser.parse(args);
        if (optionSet.has("gui") && optionSet.has("port"))
            runSparkServer((int) optionSet.valueOf("port"));
    } // command hashmap

    /**
     * Allows users to add commands to the REPL
     * @param handler: an instance of an ICommandHandler class
     */
    public void addCommand(ICommandHandler handler){
        optionParser.accepts(handler.keyWord());
        commandHashmap.addHandler(handler.keyWord(), handler);
    }

    public void runAndHandle(HandlerArguments handlerArgs){
        try {
            String line;
            while ((line = this.bufferedReader.readLine()) != null) {
                try {
                    String[] cleanLine = line.trim().split(" ");
                    for (String str : cleanLine) {
                        str.trim();
                    }
                    commandHashmap.getHandler(cleanLine[0]).handle(handlerArgs);
                } catch (Exception e) {
                    System.out.println("ERROR- cannot process: "+line+"\n"+e+":");
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR: Invalid input for REPL");
        }

    }
    // todo- handler args don't need kdTree, kdt doesn't have specific keywords/handlers

    public void run() throws IOException {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(inputStreamReader);
            while (br.readLine() != null) {
                String line = br.readLine();
                try {
                    String[] cleanLine = line.trim().split(" ");
                    for (String str : cleanLine) {
                        str.trim();
                    }

                } catch (Exception e) {
                    System.out.println("ERROR- cannot process: "+line+"\n"+e+":");

                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR: Invalid input for REPL");
        }

    }

    private void runSparkServer(int port) {
        // set port to run the server on
        Spark.port(port);
        // location of static resources (HTML, CSS, JS, images, etc.)
        Spark.externalStaticFileLocation("src/main/resources/static");
        // ExceptionPrinter to display service error on GUI if encountered
        Spark.exception(Exception.class, new ExceptionPrinter());
        // FM template engine converts .ftl templates to HTML
        FreeMarkerEngine freeMarker = createEngine();
        // set up Spark Routes
        Spark.get("/", new MainHandler(), freeMarker);
    }

    private static FreeMarkerEngine createEngine() {
        Configuration config = new Configuration(Configuration.VERSION_2_3_0);
        File templates = new File("src/main/resources/spark/template/freemarker"); // location of FreeMarker template directory
        try {
            config.setDirectoryForTemplateLoading(templates);
        } catch (IOException ioe) {
            System.out.printf("ERROR: Unable use %s for template loading.%n", templates);
            System.exit(1);
        }
        return new FreeMarkerEngine(config);
    }

    /**
     * Display an error page when an exception occurs in the server.
     */
    private static class ExceptionPrinter implements ExceptionHandler<Exception> {
        @Override
        public void handle(Exception e, Request req, Response res) {
            res.status(500); // status 500 usually for internal server error
            // write stack trace to GUI
            StringWriter stacktrace = new StringWriter();
            try (PrintWriter pw = new PrintWriter(stacktrace)) {
                pw.println("<pre>");
                e.printStackTrace(pw);
                pw.println("</pre>");
            }
            res.body(stacktrace.toString());
        }
    }

    /**
     * A handler to serve the site's main page.
     *
     * ModelAndView to render.
     * (main.ftl).
     */
    private static class MainHandler implements TemplateViewRoute {
        @Override
        public ModelAndView handle(Request req, Response res) {
            // map FreeMarker template's variables names to objects
            Map<String, Object> variables = ImmutableMap.of("title", "Go go GUI");
            return new ModelAndView(variables, "main.ftl");
        }
    }

    public static class Main {
        public static void main(String[] args){

        }
    }
}
