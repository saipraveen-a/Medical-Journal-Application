
Prerequisites:
	1. Java8
	2. Maven 3.3+
	3. MySQL 5.6+
	
Run Instructions:
	1. Configure the database connection data from Code/src/main/resources/application.properties
        2. Go to the Code folder and run "mvn spring-boot:run".
        3. Go to http://localhost:8080
        4. (Optional) By default, the application stores the uploaded PDFs in <User_home>/upload directory. If you want to change this directory, you can use the -Dupload-dir=<path> system property.
        5. (Optional) The PDFs for the predefined journals can be found in the PDFs folder. If you want to view the predefined journals, you should copy the contents of this folder to the upload folder defined in step 4.

Initialization of Database:

	The project is configured to automatically run the SQL scripts using Flyway. This allows us to keep updating the schema in future by just adding the necessary SQL scripts to the codebase. The
	SQL scripts are under src/main/resources/db/migration. For the tests, a different set of SQL scirpts are configured to run.
	
Email Server:

	The application uses Google SMTP server to send emails to users. You can set the value for the test account and the password in
	application.properties
	
There is no separate SQL folder added for the sql scripts as we are automating the schema changes with Flyway
