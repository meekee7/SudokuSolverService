# SaaS - Sudoku as a Service

This project provides a web service that is capable of validating and solving sudoku puzzles.
All code is written in Java.

# Build
The Maven build system is used, but not mandatory.
Some of the defined Maven Goals are:
  - clean - delete all artifacts
  - compile - compile the source code
  - exec:client-gui - start GUI client
  - exec:client-cmd - start CMD client
  - exec:server - start server
  - package - create a jar package containing the client and the server
  - war:war - create .war file to deploy on Glassfish/Tomcat/...
  - docker:build - build a docker container

A regular build can be started like this:
```sh
mvn compile package war:war
```
This will also automatically run the unit tests and the integration test.

# Execution
The jar package has no default entry point. Three classes have main functions: 
  - de.mfwk.sudokuservice.server.SudokuServer
  - de.mfwk.sudokuservice.client.SudokuClient
  - de.mfwk.sudokuservice.client.SudokuGUI

## Server
The server 
When started via the command line the server will publish itself on all network interfaces it can find.
The default port for the server is 1337. You can specify a different port as the first cmd argument. All other ports are ignored.

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
  - -out|-dest X - Target. 
## GUI-Client
The GUI-Client ignores all command line arguments. The UI is self-explanatory. 
"Selfhosted" is set as the default server which bypasses the regular server and runs the service locally.
