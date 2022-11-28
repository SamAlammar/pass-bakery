# Pass Bakery Inventory System

Before being able to work on the project, you'll need some environment setup. Your machine needs to be able to run:
* Java 8 through 11 inclusive
* Play Framework using sbt (If you can run the example they have with ```sbt run``` then you are good)
* Postgres server running version 14

For Postgres server, I would suggest downloading [Postgres.app](https://postgresapp.com/) and follow its installation, make sure
that when you create a server, it's version 14. Now using terminal, access the postgres app by entering ```psql``` then
create a database ```CREATE DATABASE bakery_db;```. Now you should be able to see the database in the Postgres.app application.
Access the bakery_db database by entering ```psql bakery_db``` then create the public schema ```CREATE SCHEMA public;```
After all of this, the program will do the rest!

This project also runs on pre-commit hooks that makes sure the code you add is formatted correctly. The project has
it installed, all you need is scalafmt. If you use IntelliJ then you can enable it in Preferences -> Editor -> Code Style -> Scala -> Formatter changed to Scalafmt

Everytime you try committing, it will first check the quality of the code, then lets you commit. If it backs the commit with quality issue then usually it fixes itself, so try committing again or use ```pre-commit run -a``` before committing.

If you want to compile then use ```sbt update```

To run the pass bakery program, enter this command:
```bash
sbt run
```
**Note**: Everytime you run the program, you need to make sure that the Postgres server is up and running. To do so, Make sure
you have Postgres.app Application running and press the ```start``` button on your PostgresSQL server. That will get the database running!

To run the test for the program, enter this command:
```bash
sbt test
```
