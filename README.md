# Data Crawling Service

This is the README file for the Data Crawling Service application, should fetch some information
from some website and store them in dedicated db.

### MySqlSQL Configuration

- **Variable Name**: `MYSQL_DRIVER`,`MYSQL_USERNAME`, `MYSQL_PASSWORD`, `MYSQL_URL`
- **Description**: Configures the MySqlSQL datasource properties.
    - `MYSQL_DRIVER`: Specifies the username for connecting to MySqlSQL. Default value is `com.mysql.jdbc.Driver`.
    - `MYSQL_USERNAME`: Specifies the username for connecting to MySqlSQL. Default value is `root`.
    - `MYSQL_PASSWORD`: Specifies the password for connecting to MySqlSQL. Default value is `*****`.
    - `MYSQL_URL`: Specifies the URL for connecting to MySqlSQL. Default value
      is `jdbc:mysql://localhost:3306/test_db`.

## Starting the Application

### Single threaded service

## Notes

- Ensure to set the database configuration is accurate and the connection is on before running the Data Crawling Service application.