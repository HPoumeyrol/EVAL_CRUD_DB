package co.simplon.bnppf1.hugues;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Categorie extends Tables{
	private static Scanner input= new Scanner(System.in);
    
	Long pk_id;
	String nom;
	public Long getPk_id() {
		return pk_id;
	}
	
	public void setPk_id(Long pk_id) {
		this.pk_id = pk_id;
	}
	public String getNom() {
		return nom;
	}
	
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public Categorie(Long pk_id, String nom) {
		super();
		this.pk_id = pk_id;
		this.nom = nom;
	}
	
	@Override
	public String toString() {
		//return "Categorie [pk_id=" + pk_id + ", nom=" + nom + "]";
		return String.format("\t%1$4d : %2$s", pk_id, nom);
	}
	
	
	public static Categorie findCategorieByPk_id(Long pk_id) {
		Categorie result= null;
		String sqlCmd = "select pk_id, nom from categorie where pk_id = ? ;";

		try (PreparedStatement preparedStatement = DbConnection.getDbConn().prepareStatement(sqlCmd)) {

			try {
				preparedStatement.setLong(1, pk_id);
				//System.out.println("sqlCmd= " + preparedStatement);
				ResultSet rs = preparedStatement.executeQuery();
				if (rs.next()) {
					result= new Categorie(rs.getLong("pk_id"), rs.getString("nom"));
					//System.out.println("Lecture OK de " + result);
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.err.println("Erreur lors de la recherche de la categorie " + pk_id);
				e.printStackTrace();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return  result;
	}
	
	
	public static void displayByName(String nameToSearch) {
		String sqlCmd = "select pk_id, nom from categorie  where upper(nom) like upper(?) order by nom;";
		try (PreparedStatement preparedStatement = DbConnection.getDbConn().prepareStatement(sqlCmd)) {

			try {
				preparedStatement.setString(1, "%" + nameToSearch + "%");
				//System.out.println("sqlCmd= " + preparedStatement);
				ResultSet rs = preparedStatement.executeQuery();
				displayRS(rs,"Liste des catégories correspondant à *" + nameToSearch + "*");
								
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.err.println("Erreur lors de la recherche des catégories !");
				e.printStackTrace();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public static void  displayAll() {
		String sqlCmd = "select pk_id, nom from categorie order by nom;";
		try (PreparedStatement preparedStatement = DbConnection.getDbConn().prepareStatement(sqlCmd)) {

			try {
				//System.out.println("sqlCmd= " + preparedStatement);
				ResultSet rs = preparedStatement.executeQuery();
				displayRS(rs,"Liste des catégories");
								
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.err.println("Erreur lors de la recherche de toutes les catégories !");
				e.printStackTrace();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public static long Count() {
		long result=0;
		String sqlCmd = "select count(pk_id) nb from categorie;";

		try (PreparedStatement preparedStatement = DbConnection.getDbConn().prepareStatement(sqlCmd)) {

			try {
				//System.out.println("sqlCmd= " + preparedStatement);
				ResultSet rs = preparedStatement.executeQuery();
				if (rs.next()) 
				{
					result= rs.getLong("nb");
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.err.println("Erreur pour retrouver le nombre de catégories !");
				e.printStackTrace();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}


	
	
	private static void displayRS(ResultSet rs, String title) {
		int nbLines = 0;
		String sep = String.format("%s\n",
				"-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------"
						.substring(0, 193));
		int desiredTitleWith= sep.length() - 4;
		String formattedTitle = title.substring(0, Math.min(desiredTitleWith, title.length()));
		String entete1 = sep + String.format("| %1$-" + desiredTitleWith + "s|\n", formattedTitle);

		int desiredCategNameWidh=sep.length() - 12;
		String entete2 = sep + String.format("| %1$4s | %2$-" + desiredCategNameWidh + "s |\n",
				"Id", "Categorie") + sep;

		System.out.print(entete1);
		System.out.print(entete2);

		try {
			while (rs.next()) {
				nbLines++;
				String categ_name = rs.getString("nom");
				categ_name = categ_name.substring(0, Math.min(desiredCategNameWidh, categ_name.length()));
				System.out.format("| %1$4d | %2$-" + desiredCategNameWidh + "s |\n",rs.getLong("pk_id"), categ_name);
				if (nbLines > 20) {
					nbLines = 0;
					System.out.print(sep);

					System.out.print("Appuyez sur ENTER pour voir la suite ou sur Q pour arreter l'affichage : ");
					String userInput = input.nextLine();
					System.out.println("");
					System.out.println("");
					if (userInput.toUpperCase().equals("Q")) {
						break;
					} else {
						System.out.print(entete2);
					}
				}
			}
			if (nbLines < 20) {
				System.out.print(sep);
				System.out.println("");
				System.out.println("");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
