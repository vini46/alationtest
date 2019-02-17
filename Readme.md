Selenium WebDriver, test automation for web applications
A getting started test for Amazon website using Selenium WebDriver, test automation for web applications.
Test Scenarios for this workflow is in "amazon scenarios.xlsx" file.

Language used: Java
Framework: Page Object Model
Test Runner: TestNg
Build Tool: Maven


It contains a Maven project.

Development environment
Java
You must have Java installed. The recommended version is Java 8. Download it from Oracle http://www.oracle.com/technetwork/java/javase/downloads/index.html and install it.

Build tools
Build tools are strictly not needed, but hey simplifies getting started so you should use one. Use Gradle or Maven. Gradle is easier to get started with. Maven is more complicated to get started with, it must be installed locally.

Maven
Build the project using

mvn clean install

executing mvn test
mvn verify -PrunTests -DSuiteXmlFile=/testsuite/firstsuite.xml

maven options:
browser(optional, default value is chrome):chrome,firefox,ie,edge
category(optional, default value is Books): specifiy the category of products u need to perform test on
product(optional, default value is "Product Catalog"): specify the product for which search has to be performed


Maven must be installed for this to work. Maven can be downloaded from http://maven.apache.org/download.cgi.

Examples
You will work with a web application that you can run local. You can also connect to an online version at http://selenium.thinkcode.se and use it. The online version will be slower. Running it on your local host is the recommended way.

