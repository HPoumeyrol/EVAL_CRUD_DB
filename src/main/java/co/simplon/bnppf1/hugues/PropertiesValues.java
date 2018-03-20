package co.simplon.bnppf1.hugues;

import java.io.Serializable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesValues implements Serializable {
	private static final long serialVersionUID = 1426764925973463317L;
	private static PropertiesValues INSTANCE = new PropertiesValues(); 	/** Instance unique pré-initialisée */
	private static final String PROP_FILE_NAME= "config.properties";
	private String env= "";
	private String dbUrl= "";
	private String dbUser= "";
	private String dbPwd= "";
	private String dbSchema= "";

	private static InputStream inputStream;
	 
	/** Constructeur privé */
	private PropertiesValues() {
		Properties prop = new Properties();
		try {
			inputStream = getClass().getClassLoader().getResourceAsStream(PROP_FILE_NAME);

			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				System.out.println("Error when reading properties file : " + PROP_FILE_NAME);
				System.exit(99);
			}

			// get the property value and print it out
			env = prop.getProperty("Environment");
			dbUrl = prop.getProperty("dbUrl");
			dbUser = prop.getProperty("dbUser");
			dbPwd = prop.getProperty("dbPwd");
			dbSchema = prop.getProperty("dbSchema");
		} catch (Exception e) {
			System.out.println("Error when parsing properties file " + PROP_FILE_NAME + " : " + e);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	
	/** Point d'accès pour l'instance unique du singleton */
    public static PropertiesValues getInstance()
    {   
    	return INSTANCE;
    }

	/** Sécurité anti-désérialisation */
	private Object readResolve() {
		return INSTANCE;
	}

	public static String getDbUrl() {
		return INSTANCE.dbUrl;
	}

	public static String getDbUser() {
		return INSTANCE.dbUser;
	}

	public static String getDbPwd() {
		return INSTANCE.dbPwd;
	}

	public static String getEnv() {
		return INSTANCE.env;
	}

	public static String getDbSchema() {
		return INSTANCE.dbSchema;
	}

	@Override
	public String toString() {
		return "Params :\n\tEnv= " + env + "\n\tdbUrl=" + dbUrl + "\n\tdbUser=" + dbUser + "\n\tdbPwd=" + dbPwd
				+ "\n\tdbSchema= " + dbSchema;
	}

	public static void display() {
		System.out.println(INSTANCE.toString());
	}

}
