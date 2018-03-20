package co.simplon.bnppf1.hugues;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import java.util.HashSet;
import java.util.Scanner;

public class Aliment extends Tables {
	private static Scanner input = new Scanner(System.in);

	Long pk_id;
	String nom;
	Integer energie;
	Double proteines;
	Double glucides;
	Double lipides;
	Long fk_categorie_id;

	public Long getPk_id() {
		return pk_id;
	}

	public void setPk_id(Long pk_id) {
		this.pk_id = pk_id;
		update();
	}

	public String getNom() {
		return nom;

	}

	public void setNom(String nom) {
		this.nom = nom;
		update();
	}

	public Integer getEnergie() {
		return energie;
	}

	public void setEnergie(Integer energie) {
		this.energie = energie;
		update();
	}

	public Double getProteines() {
		return proteines;
	}

	public void setProteines(Double proteines) {
		this.proteines = proteines;
		update();
	}

	public Double getGlucides() {
		return glucides;
	}

	public void setGlucides(Double glucides) {
		this.glucides = glucides;
		update();
	}

	public Double getLipides() {
		return lipides;
	}

	public void setLipides(Double lipides) {
		this.lipides = lipides;
		update();
	}

	public Long getFk_categorie_id() {
		return fk_categorie_id;

	}

	public void setFk_categorie_id(Long fk_categorie_id) {
		this.fk_categorie_id = fk_categorie_id;
		update();
	}

	public Aliment(Long pk_id, String nom, Integer energie, Double proteines, Double glucides, Double lipides,
			Long fk_categorie_id) {
		this.pk_id = pk_id;
		this.nom = nom;
		this.energie = energie;
		this.proteines = proteines;
		this.glucides = glucides;
		this.lipides = lipides;
		this.fk_categorie_id = fk_categorie_id;
	}

	public Aliment() {
		this.pk_id = -1L;
		this.nom = "";
		this.energie = 0;
		this.proteines = 0.0;
		this.glucides = 0.0;
		this.lipides = 0.0;
		this.fk_categorie_id = -1L;
	}

	@Override
	public String toString() {
		// return "Aliment [pk_id=" + pk_id + ", nom=" + nom + ", energie=" + energie +
		// ", proteines=" + proteines
		// + ", glucides=" + glucides + ", lipides=" + lipides + ", fk_categorie_id=" +
		// fk_categorie_id + "]";
		
		String sep = String.format("%s\n",
				"-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------"
						.substring(0, 193));
		
		String entete2 = sep + String.format("| %1$-3s | %2$-60s | %3$-90s | %4$-3s | %5$-5s | %6$-5s | %7$-5s |\n",
				"Id", "(Cat Id) Categorie", "Nom", "Eng", "Prot", "Gluc", "Lipi") + sep;
			
		Categorie categorie = Categorie.findCategorieByPk_id(fk_categorie_id);
		String categorieName = (categorie == null) ? "" : categorie.getNom();
		categorieName = categorieName.substring(0, Math.min(60, categorieName.length()));
		String al_name= nom.substring(0, Math.min(90, nom.length()));
		return entete2 +
				String.format("|%1$4d | %2$-60s | %3$-90s | %4$3d | %5$5.2f | %6$5.2f | %7$5.2f |\n",
						pk_id,
						categorieName,
						al_name, 
						energie, proteines, glucides, lipides
						) +
				sep;
	}

	public boolean save() {
		boolean res = false;
		String sqlCmd = "INSERT INTO aliment (nom, energie, proteines, glucides, lipides, fk_categorie_id) VALUES(?, ?, ?, ?, ?, ?);";

		try (PreparedStatement preparedStatement = DbConnection.getDbConn().prepareStatement(sqlCmd,
				Statement.RETURN_GENERATED_KEYS)) {

			try {
				preparedStatement.setString(1, this.nom);
				preparedStatement.setInt(2, this.energie);
				preparedStatement.setDouble(3, this.proteines);
				preparedStatement.setDouble(4, this.glucides);
				preparedStatement.setDouble(5, this.lipides);
				preparedStatement.setLong(6, this.fk_categorie_id);

				// System.out.println("sqlCmd= " + preparedStatement);
				preparedStatement.execute();
				long key = -1L;
				ResultSet rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					key = rs.getLong("pk_id");
				}
				this.pk_id = key;
				// System.out.println("Enregistrement en base OK : " + key + " : " + this);
				res = true;

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.err.println("erreur lors de l'enregistrement en base de " + this);
				e.printStackTrace();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	private boolean update() {
		boolean res = false;
		String sqlCmd = "update aliment set nom = ?, energie = ?, proteines = ?, glucides = ?, lipides = ?, fk_categorie_id = ? where pk_id = ?;";

		try (PreparedStatement preparedStatement = DbConnection.getDbConn().prepareStatement(sqlCmd)) {

			try {
				preparedStatement.setString(1, this.nom);
				preparedStatement.setInt(2, this.energie);
				preparedStatement.setDouble(3, this.proteines);
				preparedStatement.setDouble(4, this.glucides);
				preparedStatement.setDouble(5, this.lipides);
				preparedStatement.setLong(6, this.fk_categorie_id);
				preparedStatement.setLong(7, this.pk_id);

				// System.out.println("sqlCmd= " + preparedStatement);
				preparedStatement.execute();
				// System.out.println("Mise a jour en base OK de " + this);
				res = true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.err.println(this + " erreur lors de la mise a jour en base !");
				e.printStackTrace();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	public void readFromDB() {

		String sqlCmd = "select nom, energie, proteines, glucides, lipides, fk_categorie_id from aliment where pk_id = ?;";

		try (PreparedStatement preparedStatement = DbConnection.getDbConn().prepareStatement(sqlCmd)) {

			try {
				preparedStatement.setLong(1, this.pk_id);
				// System.out.println("sqlCmd= " + preparedStatement);
				ResultSet rs = preparedStatement.executeQuery();
				if (rs.next()) {
					this.nom = rs.getString("nom");
					this.energie = rs.getInt("energie");
					this.proteines = rs.getDouble("proteines");
					this.glucides = rs.getDouble("glucides");
					this.lipides = rs.getDouble("lipides");
					this.fk_categorie_id = rs.getLong("fk_categorie_id");
					// System.out.println("Lecture OK de " + this);
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.err.println(this + " erreur lors de la mise a jour en base !");
				e.printStackTrace();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// public static Aliment findAlimentByName(String nom) {
	// Aliment result = null;
	// String sqlCmd = "select pk_id, nom, energie, proteines, glucides, lipides,
	// fk_categorie_id from aliment where upper(nom) like upper(?);";
	//
	// try (PreparedStatement preparedStatement =
	// DbConnection.getDbConn().prepareStatement(sqlCmd)) {
	//
	// try {
	// preparedStatement.setString(1, nom);
	// // System.out.println("sqlCmd= " + preparedStatement);
	// ResultSet rs = preparedStatement.executeQuery();
	// if (rs.next()) {
	// result = new Aliment(rs.getLong("pk_id"), rs.getString("nom"),
	// rs.getInt("energie"),
	// rs.getDouble("proteines"), rs.getDouble("glucides"), rs.getDouble("lipides"),
	// rs.getLong("fk_categorie_id"));
	// // System.out.println("Lecture OK de " + result);
	// }
	//
	// } catch (SQLException e) {
	// // TODO Auto-generated catch block
	// System.err.println("Erreur lors de la recherche de " + nom);
	// e.printStackTrace();
	// }
	//
	// } catch (SQLException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// return result;
	// }

	// private static HashSet<Aliment> readAllFromDB() {
	// HashSet<Aliment> result = new HashSet<Aliment>();
	// String sqlCmd = "select aliment.pk_id, aliment.nom, aliment.energie,
	// aliment.proteines, aliment.glucides, aliment.lipides, aliment.fk_categorie_id
	// from aliment "
	// + "left join categorie on aliment.fk_categorie_id = categorie.pk_id "
	// + "order by categorie.nom, aliment.nom;";
	//
	// try (PreparedStatement preparedStatement =
	// DbConnection.getDbConn().prepareStatement(sqlCmd)) {
	//
	// try {
	// // System.out.println("sqlCmd= " + preparedStatement);
	// ResultSet rs = preparedStatement.executeQuery();
	// while (rs.next()) {
	// result.add(new Aliment(rs.getLong("pk_id"), rs.getString("nom"),
	// rs.getInt("energie"),
	// rs.getDouble("proteines"), rs.getDouble("glucides"), rs.getDouble("lipides"),
	// rs.getLong("fk_categorie_id")));
	// }
	//
	// } catch (SQLException e) {
	// // TODO Auto-generated catch block
	// System.err.println("Erreur lors de la recherche de tous les aliments !");
	// e.printStackTrace();
	// }
	// } catch (SQLException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// return result;
	// }

	public static void displayByName(String nameToSearch) {
		String sqlCmd = "select aliment.pk_id al_pk_id, categorie.nom categ_nom, aliment.nom al_nom, aliment.energie, aliment.proteines, aliment.glucides, aliment.lipides, aliment.fk_categorie_id from aliment "
				+ "left join categorie on aliment.fk_categorie_id = categorie.pk_id "
				+ "where upper(aliment.nom) like upper(?)" 
				+ "order by categorie.nom, aliment.nom;";

		try (PreparedStatement preparedStatement = DbConnection.getDbConn().prepareStatement(sqlCmd)) {

			try {
				preparedStatement.setString(1, "%" + nameToSearch + "%");
				// System.out.println("sqlCmd= " + preparedStatement);
				ResultSet rs = preparedStatement.executeQuery();
				displayRS(rs, "Liste des Aliments de la base correspondant à *" + nameToSearch + "*");

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.err.println("Erreur lors de la recherche de " + nameToSearch);
				e.printStackTrace();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
	public static void displayAll() {

		String sqlCmd = "select aliment.pk_id al_pk_id, categorie.nom categ_nom, aliment.nom al_nom, aliment.energie, aliment.proteines, aliment.glucides, aliment.lipides, aliment.fk_categorie_id from aliment "
				+ "left join categorie on aliment.fk_categorie_id = categorie.pk_id "
				+ "order by categorie.nom, aliment.nom;";

		try (PreparedStatement preparedStatement = DbConnection.getDbConn().prepareStatement(sqlCmd)) {
			try {
				// System.out.println("sqlCmd= " + preparedStatement);
				ResultSet rs = preparedStatement.executeQuery();
				displayRS(rs, "Liste des Aliments de la base");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.err.println("Erreur lors de la recherche de tous les aliments !");
				e.printStackTrace();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	private static void displayRS(ResultSet rs, String title) {
		int nbLines = 0;
		String sep = String.format("%s\n",
				"-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------"
						.substring(0, 193));
		String formattedTitle = title.substring(0, Math.min((sep.length() - 4), title.length()));
		String entete1 = sep + String.format("| %1$-" + (sep.length() - 4) + "s|\n", formattedTitle);

		String entete2 = sep + String.format("| %1$-3s | %2$-60s | %3$-90s | %4$-3s | %5$-5s | %6$-5s | %7$-5s |\n",
				"Id", "(Cat Id) Categorie", "Nom", "Eng", "Prot", "Gluc", "Lipi") + sep;

		System.out.print(entete1);
		System.out.print(entete2);

		try {
			while (rs.next()) {
				nbLines++;
				String categ_name = "(" + rs.getLong("fk_categorie_id") + ") " + rs.getString("categ_nom");
				categ_name = categ_name.substring(0, Math.min(60, categ_name.length()));
				String al_name = rs.getString("al_nom");
				al_name = al_name.substring(0, Math.min(90, al_name.length()));
				System.out.format("|%1$4d | %2$-60s | %3$-90s | %4$3d | %5$5.2f | %6$5.2f | %7$5.2f |\n",
						rs.getLong("al_pk_id"), categ_name, al_name, rs.getInt("energie"), rs.getDouble("proteines"),
						rs.getDouble("glucides"), rs.getDouble("lipides"));
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
