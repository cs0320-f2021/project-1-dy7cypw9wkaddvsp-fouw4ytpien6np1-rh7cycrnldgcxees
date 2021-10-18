package edu.brown.cs.student.main;
import java.io.InputStreamReader;
import java.sql.SQLException;

import edu.brown.cs.student.api.main.ApiAggregator;
import edu.brown.cs.student.kdtree.coordinates.KdTree;
import edu.brown.cs.student.main.handlers.*;
import edu.brown.cs.student.main.repl.REPL;
import edu.brown.cs.student.orm.Database;
import edu.brown.cs.student.orm.table.ITable;
import joptsimple.OptionParser;

/**
 * The Main class of our project. This is where execution begins.
 */
public final class Main {
  private final String[] args;
  private final OptionParser parser = createOptionParser(4567);

  // state-specific information to store in Main
  // private Database db;
  private KdTree<ITable> kd = new KdTree<>();
  private ApiAggregator api = new ApiAggregator();
  private PartnerRecommender pr = new PartnerRecommender();

  // each ICommandHandler class has a keyword field
  private final ICommandHandler[] handlers = new ICommandHandler[] {
          new ChangePathHandler(),
          new UsersHandler(),
          new RecsysLoadHandler(),
          new RecsysRecHandler()
  };
  CommandHashmap keywordMap = new CommandHashmap(handlers);
  HandlerArguments handlerArgs;
  Database db = new Database(keywordMap);

  // constructor
  private Main(String[] args) throws SQLException, ClassNotFoundException {
    this.args = args;
    this.handlerArgs = new HandlerArguments(kd, db, pr, args);
  }

  /**
   * The initial method called when execution begins.
   * @param args An array of command line arguments
   */
  public static void main(String[] args) throws SQLException, ClassNotFoundException {
    new Main(args).run();
  }

  /**
   * Creates an OptionParser, used to parse command line flags when passed to REPL
   * @param DEFAULT_PORT default port for running the server
   * @return an OptionParser set to recognize "gui" and "port"
   */
  private OptionParser createOptionParser(int DEFAULT_PORT){
    OptionParser parser = new OptionParser(); // to parse command line flags
    parser.accepts("gui"); // "./run --gui" starts a web server
    parser.accepts("port").withRequiredArg().ofType(Integer.class).defaultsTo(DEFAULT_PORT); // "--port <n>" specifies which port the server runs on
    return parser;
  }


  private void run() throws SQLException, ClassNotFoundException {
    try{
      OptionParser op = new OptionParser();
      op.accepts("gui");
      op.accepts("port").withRequiredArg().ofType(Integer.class);
      new REPL(args, new InputStreamReader(System.in), parser, keywordMap).runAndHandle(this.handlerArgs);
    } catch (Exception e){
      e.printStackTrace();
      System.out.println("ERROR");
      // todo- deal with errors, add errors to REPL class
    }
  }
}
