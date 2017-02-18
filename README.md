# finetech-exercise
Before running the application, Run the migraions in the migrations file.

It is assumed that when running this program, database in question can be accessed by username :root | password : root. If not so please change the username+password in --> src/main/java/com/finetech/userauth/db/MySQLDataSourceHikari.java

After successfully running the migrations run the startup.sh file. ( make sure it has executable permissions).
If it does not work, simply run the following command.
mvn clean compile package; java -jar target/finetech-ex-0.1.0.jar
