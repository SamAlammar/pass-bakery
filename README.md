# Pass Bakery Inventory System

Before being able to work on the project, you'll need some environment setup. Your machine needs to be able to run:
 * Java 8 through 11 inclusive
 * Play Framework using sbt (If you can run the example they have with ```sbt run``` then you are good)
 * Postgres server running version 14

For Postgres server, you will need to make sure you have a basic bakery_db database with a schema named public, the program does the rest.

This project also runs on pre-commit hooks that makes sure the code you add is formatted correctly. The project has
it installed, all you need is scalafmt. If you use IntelliJ then you can enable it in Preferences -> Editor -> Code Style -> Scala -> Formatter changed to Scalafmt

Everytime you try committing, it will first check the quality of the code, then lets you commit. If it backs the commit with quality issue then usually it fixes itself, so try committing again or use ```pre-commit run -a``` before committing.

If you want to compile then use ```sbt update```

To run the pass bakery program, enter this command (Make sure you have your Postgres Server running before running the project):
```bash
sbt run
```

To run the test for the program, enter this command:
```bash
sbt test
```
