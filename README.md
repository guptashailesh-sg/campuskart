# CampusKart

Campus-only student marketplace built with Java, JSP, Servlets, JDBC, MySQL, HTML, CSS and JavaScript.

## Requirements

- JDK 11 or newer
- Maven
- MySQL Server 8.x
- Apache Tomcat 9.x (this project uses `javax.servlet`, so use Tomcat 9 rather than Tomcat 10)

## 1. Create the database

Run the SQL script at `database/schema.sql` in MySQL Workbench or the MySQL command line. It creates the `campuskart` database and required tables.

## 2. Configure the database connection

The app connects to `localhost:3306`, database `campuskart`, user `root` by default. Set the database password locally before starting Tomcat. Do not commit your password to GitHub.

In Windows Command Prompt, set these variables for the current terminal session (replace the example with your own password):

```bat
set CAMPUSKART_DB_USER=root
set CAMPUSKART_DB_PASSWORD=your_mysql_password
```

Then start Tomcat from that same terminal. Alternatively, configure these as Windows user environment variables and restart the terminal/Tomcat.

The default JDBC URL already includes `allowPublicKeyRetrieval=true`, which resolves the MySQL Connector/J error `Public Key Retrieval is not allowed` for this local development setup.

## 3. Build the application

From the project root, run:

```bat
mvn clean package
```

The WAR file is generated at `target/campuskart.war`.

## 4. Deploy to Tomcat 9

Copy `target/campuskart.war` into Tomcat's `webapps` folder, start Tomcat, and open:

`http://localhost:8080/campuskart/`

If Tomcat has already deployed an older version, stop Tomcat, replace the old WAR, remove the old extracted `webapps/campuskart` folder, then start Tomcat again.

## Troubleshooting

- **`No suitable driver found`**: rebuild from the latest repository source and redeploy the newly generated WAR. Confirm `WEB-INF/lib/mysql-connector-j-8.4.0.jar` is inside the WAR.
- **`Public Key Retrieval is not allowed`**: use the latest `DBConnection.java` from this repository and rebuild the WAR.
- **Database access denied**: verify the MySQL service is running and the local `CAMPUSKART_DB_USER` / `CAMPUSKART_DB_PASSWORD` values are correct.
- **Tomcat 10 errors involving servlet classes**: run this project on Tomcat 9 unless the source is migrated from `javax.servlet` to `jakarta.servlet`.
