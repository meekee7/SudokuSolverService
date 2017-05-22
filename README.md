# SaaS - Sudoku as a Service

This project provides a web service that is capable of validating and solving sudoku puzzles.
All code is written in Java.

# Build
The Maven build system is used, but not mandatory.
Some of the defined Maven Goals are:
  - `clean` - delete all artifacts
  - `compile` - compile the source code
  - `exec:client-gui` - start GUI client
  - `exec:client-cmd` - start CMD client
  - `exec:server` - start server
  - `package` - create a .jar package containing both the client and the server
  - `war:war` - create a .war file to deploy on Glassfish/Tomcat/...
  - `cxf-java2ws:java2ws` - create WSDL file for the webservice that also contains the XSDs for the data exchange
  - `docker:build` - build a docker container
  - `deploy` - deploy to a repository system like Nexus (localhost is preconfigured) 

A regular build (including the tests) can be started like this:
```sh
mvn compile package war:war
```
This will also automatically run the unit tests and the integration test.
To run only the tests execute
```sh
mvn test-compile test
```
# Execution
The jar package has no default entry point. Three classes have main functions: 
  - de.mfwk.sudokuservice.server.SudokuServer
  - de.mfwk.sudokuservice.client.SudokuClient
  - de.mfwk.sudokuservice.client.SudokuGUI

## Server
When started via the command line the server will publish itself on all network interfaces it can find.
The default port for the server is 1337. You can specify a different port as the first cmd argument. All other arguments are ignored.
### Deployment on Glassfish
TODO write a guide for this
## CMD-Client
Arguments for the cmd client:
  - -solvebt - Solve with backtracking
  - -solve - Solve with pure logic
  - -valid|-status - Validate the sudoku
  - -ping - Check if the server is accessible
  - 
  - -host X - Host name or IP of the server. Use "selfhosted" to bypass the regular server and run the service locally.
  - -port X - Port of the service on the server. If no port is specified the port 1337 will be used.
  - -subdir X - Subdir of the service in the URI of the server
  - 
  - -file|-src X - Source file of the sudoku to solve or validate
  - -out|-dest X - Target file for the solved sudoku. If no target file is specified the solved sudoku will be printed on the console 
## GUI-Client
The GUI-Client ignores all command line arguments. The UI is self-explanatory. 
"selfhosted" is set as the default server which bypasses the regular server and runs the service locally.
