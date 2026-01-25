Kornyezeti valtozo spring:
SPRING_PROFILES_ACTIVE=dev ./gradlew bootRun

-->
@Autowired
private Environment env;

public void printVar() {
System.out.println(env.getProperty("APP_GREETING_MESSAGE"));
}

------------------------------------------------------------------

