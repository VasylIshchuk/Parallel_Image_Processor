Another way to create a package:
 *Add in settings.xml:
 
 <settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.">
    <activeProfiles>
        <activeProfile>github</activeProfile>
    </activeProfiles>
    <profiles>
        <profile>
            <id>github</id>
            <repositories>
                   <repository>
                        <id>central</id>
                        <url>https://repo1.maven.org/maven2</url>
                   </repository>
                   <repository>
                        <id>github</id>
                       <url>https://maven.pkg.github.com/VasylIshchuk/c</url>
                        <snapshots>
                            <enabled>true</enabled>
                        </snapshots>
                   </repository>
            </repositories>
       </profile>
    </profiles>
    <servers>
        <server>
            <id>github</id>
            <username>VasylIshchuk</username>
            <password>TOKEN</password>
        </server>
    </servers>
</settings>

  *Add in pom.xml:
  
<distributionManagement>
        <repository>
            <id>github</id>
            <name>GitHub VasylIshchuk Apache Maven Packages</name>
            <url>https://maven.pkg.github.com/OWNER/REPOSITORY</url>
        </repository>
</distributionManagement>

   *add press the "deploy" in the tab "Maven".     
