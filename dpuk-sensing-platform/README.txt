DPUK Sensing Platform
=====================
This is the server code for the DPUK Sensing Platform.  The platform implements 
a REST API, and has no web interface.  A separate web application is being 
developed to provide a UI to the platform.

MySQL Database
==============
1. Create a local MySQL user account with username 'dpuk' and password 'dpuk'. 
This can be done from the command line:

mysql --user=root -p mysql
CREATE USER 'dpuk'@'localhost' IDENTIFIED BY 'dpuk';

2. Create a MySQL database named 'dpuk_sensing_platform' and grant all 
privileges to user 'dpuk':

CREATE DATABASE dpuk_sensing_platform;
GRANT ALL ON dpuk_sensing_platform.* TO 'dpuk'@'localhost';

Building the Server
---------------------------------
Assuming your dpuk parent folder is at C:\Development\dpuk\

1. Change to the dpuk-sensing-platform folder:

   cd C:\Development\dpuk\dpuk-sensing-platform
   
2. Follow the following steps: (Alternatively you can refer to the https://gitserver.opencdms.org/trac/wiki/Eclipse-Maven-Build link on wiki page)

   a) Locate maven settings.xml file: (Ideally present in M2_HOME\conf folder. i.e; C:\Program Files\apache-maven-3.3.3\conf).
   b) Update settings.xml file to have the following entries: (The remaining entries can remain as is). (deploy.folder is path to the tomcat installation folder where .war is copied manually)
   
      <settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
	          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
	   
	   <profiles>
			<!-- 'dev' profile for development on a local machine -->
			<profile>
				<id>dev</id>
				<properties>
					<profile.name>dev</profile.name>
					<maven.test.skip>false</maven.test.skip>
					<deploy.folder>C:\Program Files\Apache Software Foundation\Tomcat 8.0\webapps</deploy.folder>
				</properties>
			</profile>
	
			<!-- 'live' profile for deployment to a remote server -->
			<profile>
				<id>live</id>
				<properties>
					<profile.name>live</profile.name>
					<maven.test.skip>true</maven.test.skip>
					<deploy.folder>C:\Program Files\Apache Software Foundation\Tomcat 8.0\webapps</deploy.folder>
				</properties>
			</profile>
		</profiles>
		
		</settings>
		
	c) Pull the latest pom.xml file from git server. It should already have the following entries:
	   
	    <profiles>
		<!-- 'dev' profile for development on a local machine -->
		<profile>
			<id>dev</id>
			<properties>
				<build.profile>${profile.name}</build.profile>
				<deploy.folder>${deploy.folder}</deploy.folder>
			</properties>
		</profile>

		<!-- 'live' profile for deployment to a remote server -->
		<profile>
			<id>live</id>
			<properties>
				<build.profile>${profile.name}</build.profile>
				<deploy.folder>${deploy.folder}</deploy.folder>
			</properties>
		</profile>
		</profiles>
		
		
		
	
2. Run 'mvn clean install' to build the file

3. Copy the built file:

    C:\Development\dpuk\dpuk-sensing-platform\target\dpuk-sensing-platform.war

To the local Tomcat server, e.g.:

    C:\Program Files\Apache Software Foundation\Tomcat 8.0\webapps\

4. Start Tomcat.
5. Using Postman a test query can be made to one of the end-points, for example to show all the users:

	http://localhost:8080/dpuk-sensing-platform/api/user/

The request will need to be made with administrator  user credentials with 
'basic authentication', and the hashed version of the password.

Note: When the server application is deployed it will generate the initial 
database schema.  By default a single user is also created:

Username: admin
Password: admin123