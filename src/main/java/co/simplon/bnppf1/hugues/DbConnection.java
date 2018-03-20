package co.simplon.bnppf1.hugues;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection implements Serializable
{   
	
	private static final long serialVersionUID = 1426783525973463317L;
	/** Instance unique pré-initialisée */
    private static DbConnection INSTANCE = new DbConnection();
    private Connection dbConn= null;
	private Boolean IsDbConnected= false;
	
	

	/** Constructeur privé */
    private DbConnection(){
    	  
    	try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	// Open DataBase
		try {
			//PropertiesValues.display();
			this.dbConn = DriverManager.getConnection(PropertiesValues.getDbUrl(), PropertiesValues.getDbUser(), PropertiesValues.getDbPwd());
			this.dbConn.setSchema(PropertiesValues.getDbSchema());
			
			
			System.out.println("Catalog= " + dbConn.getCatalog());
			System.out.println("Schema= " + dbConn.getSchema());
			this.IsDbConnected= true;
		} catch (SQLException e) {
			System.err.println("DataBase Connexion error : ");
			e.printStackTrace();
			System.err.println("Program terminated on error.");
			System.exit(99);
		}
    }
 
     
    /** Point d'accès pour l'instance unique du singleton */
    public static DbConnection getInstance()
    {   return INSTANCE;
    }
 
    /** Sécurité anti-désérialisation */
    private Object readResolve() {
        return INSTANCE;
    }

    public static Connection getDbConn() {
		return INSTANCE.dbConn;
    }
    

	public static Boolean isDbConnected() {
		return INSTANCE.IsDbConnected;
	}

	
	public static boolean Close() {
		return INSTANCE.close();
	}
	
	
	private boolean close() {
		boolean result= false;
		try {
			this.dbConn.close();
			result= true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	public static boolean setAutoCommit(Boolean autoCommitDesiredValue) {
		Boolean res= false;
		if(INSTANCE.IsDbConnected) {
			
			try {
				INSTANCE.dbConn.setAutoCommit(autoCommitDesiredValue);
				//System.out.println("AutoCommit set to " + autoCommitDesiredValue);
				res= true;
			} catch (SQLException e )
			{
				System.err.println("Error when setting AutoCommit");
				e.printStackTrace();
			}
		} else {
			System.err.println("Error : database in not connected !");
		}
		return res;
	}

	
	
	
	
	public static boolean commit(){
		Boolean res= false;
		if(INSTANCE.IsDbConnected) {
			try {
				INSTANCE.dbConn.commit();
				System.out.println("Transaction commited OK");
				res= true;
			} catch (SQLException e )
			{
				System.err.println("Error when commiting transaction");
				e.printStackTrace();
			}
			
		} else {
			System.err.println("Error : database in not connected !");
		}
		return res;
		
	}
	
	public static boolean rollback(){
		Boolean res= false;
		if(INSTANCE.IsDbConnected) {
			try {
				INSTANCE.dbConn.rollback();
				System.out.println("Transaction rollbacked OK");
				res= true;
			} catch (SQLException e )
			{
				System.err.println("Error when rollbacking transaction");
				e.printStackTrace();
			}
			
		} else {
			System.err.println("Error : database in not connected !");
		}
		return res;
	}
	
}
