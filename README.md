LMS - Learning Management System on Spring Boot project with Apache Maven.
Setup the environment:
1. Spring Boot Setup
- Install JDK 17 or newer.
- Configure JAVA_HOME and PATH.
- Verify with java -version.

2. Install IDE
- Choose: IntelliJ IDEA CE / VS Code (Java extensions) / Eclipse.
- Ensure Java + Spring Boot plugins/extensions are installed.
- Install Build Tool
- Choose Maven or Gradle.
- Configure environment variables (M2_HOME or GRADLE_HOME).
- Verify with mvn -v or gradle -v.
- Generate Spring Boot Project (Spring Initializr)
- Use start.spring.io

3. Add dependencies for SpringBoot:
- Spring Web
- Thymeleaf
- H2 Database
- Lombok
- Spring Data JPA
- Set proper Group, Artifact, Package names.

4. Download project with chosen build tool.
- Import & Run Project
- Import project into IDE.
- Create a simple endpoint (/hello).
- Run app → verify at http://localhost:8080/hello
