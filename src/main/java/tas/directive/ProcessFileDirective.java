package tas.directive;

import java.io.PrintWriter;
import java.nio.file.*;
import java.util.concurrent.ExecutorService;
import javax.inject.Inject;
import tas.*;
import tas.io.*;
import tas.task.*;

public class ProcessFileDirective implements Directive {
  private final PrintWriter err;
  private final FileSystem fileSystem;
  private final FileMethods fileMethods;
  private final ExecutorService tasks;
  private final ProcessFileTaskProvider taskProvider;

  @Inject
  ProcessFileDirective(@StandardError PrintWriter err,
                       FileSystem fileSystem,
                       FileMethods fileMethods,
                       ExecutorService tasks,
                       ProcessFileTaskProvider taskProvider) {
    this.err = err;
    this.fileSystem = fileSystem;
    this.fileMethods = fileMethods;
    this.tasks = tasks;
    this.taskProvider = taskProvider;
  }

  public final String name() {
    return "process-file";
  }

  public DirectiveResult apply(String parameters) {
    Path path = fileSystem.getPath(parameters);
    if (fileMethods.exists(path)) {
      tasks.submit(taskProvider.process(path));
    } else {
      err.println("No such file: " + parameters);
    }
    return DirectiveResult.CONTINUE;
  }
}
