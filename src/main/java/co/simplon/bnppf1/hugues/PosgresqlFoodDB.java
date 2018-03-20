package co.simplon.bnppf1.hugues;
import java.io.IOException;
import java.util.Scanner;
import co.simplon.bnppf1.hugues.ConsoleMenu;

public class PosgresqlFoodDB {
	private static Scanner input= new Scanner(System.in);
	static final Long ABORT_VALUE = -99L;
	
    
	public static void main(String[] args) throws NumberFormatException, IOException{
		 String header= "Eval_CRUD_DB";
	     int exitCode= 0;
	     
	     
		 if(! DbConnection.isDbConnected()) {
			 System.err.println("Erreur de connection a la base de données !");
			 exitCode= 99;
		 }
		 else
		 {
			  String quitOption= "Quitter";
		      String prompt="Votre choix : ";
		      String[] menuOptions={prompt,
		    		  				"Lister tous les Aliments",
					    			"Lister toutes les Catégories (" + String.format("%d",Categorie.Count()) + ")",
					    			"Rechercher un aliment par son nom",
					    			"Rechercher une catégorie par son nom",
					    			"Saisir un nouvel aliment",
					    			quitOption};
		      ConsoleMenu menu= new ConsoleMenu(header, menuOptions, input);
		      for(int choice= menu.askSelection();   ! menuOptions[choice].equals(quitOption);   choice= menu.askSelection()){
				  switch (choice){
					  case 1:{
						  		listAllFoods();
					            break;
					  		 }
					  case 2:{
						  		listAllCategories();
					      		break;
					  		 }
					  case 3:{
						  		findFoodByName();
						  		break;
					  		 }
					  case 4:{
						  		findCategorieByName();
						  		break;
				  		 }
					  case 5:{
						  		addNewFood();
						  		break;
				  		 }
					  
				  }
  
		      }
		      
		      if(!DbConnection.Close()) {
		    	  System.out.println("Erreur lors de la fermeture de la base !");
		      };
		      System.out.println("");
		      System.out.println("Au revoir !");
		      System.out.println("");
		      
		      
		 }
	      
	      System.exit(exitCode);
	  }
	
	private static void listAllFoods() {
		Aliment.displayAll();
	}
	
	private static void listAllCategories() {
		Categorie.displayAll();
	}
	
	private static void findFoodByName() {
		String nameToSearch=getUserInput("\"Saisissez le nom ou une partie du nom a de l'aliment désiré : ");
		Aliment.displayByName(nameToSearch);
	}	
	
	
	private static void findCategorieByName() {
		String nameToSearch=getUserInput("Saisissez le nom ou une partie du nom a de la catégorie désirée : ");
		Categorie.displayByName(nameToSearch);
		
	}
	
	
	
	private static void addNewFood() {
		Long userInputCategorie=-1L;
		String userInputFoodName="";
		Integer userInputEnergy=0;
		Double userInputProteines= 0.0;
		Double userInputGlucides= 0.0;
		Double userInputLipides= 0.0;
		
		
		String InputStage="input Categorie";
		
		while(!InputStage.equals("end") )
		{
			switch(InputStage)
			{
				case "input Categorie" :
					userInputCategorie= getUserCategId();
					if(userInputCategorie == ABORT_VALUE) 
						{ InputStage="inputAborted"; }
					else 
						{ InputStage="inputAlimentName"; }
					break;
					
				case "inputAlimentName" :
					userInputFoodName= getUserInput("Saisissez le nom du nouvel aliment\nou Q pour abandoner la création : ");
					if(userInputFoodName.toUpperCase().equals("Q")) 
						{ InputStage="inputAborted"; }
					else
						{ InputStage="inputEnergy"; }
					break;
					
				case "inputEnergy" : 
					userInputEnergy= getUserInt("Saisissez la valeur energétique enn KCal du nouvel aliment\nou Q pour abandoner la création : ");
					if(userInputEnergy == -99) 
						{ InputStage="inputAborted"; }
					else
						{ InputStage="inputProteines"; }
					break;
					
					
				case "inputProteines" : 
					userInputProteines= getUserDouble("Saisissez la valeur des protéines du nouvel aliment\nou Q pour abandoner la création : ");
					if(userInputProteines == -99.0) 
						{ InputStage="inputAborted"; }
					else
						{ InputStage="inputGlucides"; }
					break;
					
				case "inputGlucides" : 
					userInputGlucides= getUserDouble("Saisissez la valeur des glucides du nouvel aliment\nou Q pour abandoner la création : ");
					if(userInputGlucides == -99.0) 
						{ InputStage="inputAborted"; }
					else
						{ InputStage="inputLipides"; }
					break;
					
				case "inputLipides" : 
					userInputLipides= getUserDouble("Saisissez la valeur des lipides du nouvel aliment\nou Q pour abandoner la création : ");
					if(userInputLipides == -99.0) 
						{ InputStage="inputAborted"; }
					else
						{ InputStage="inputCompleted"; }
					break;
					
				case "inputAborted" :
					System.out.println("Vous avez choisi d\'abandonner la création d'un nouvel aliment");
					InputStage="end";
					break;
					
				case "inputCompleted" : 
					System.out.println("Vous avez choisi de créér l\'aliment suivant : ");
					Aliment aliment= new Aliment(-1L,userInputFoodName, userInputEnergy, userInputProteines, userInputGlucides, userInputLipides, userInputCategorie );
					aliment.display();
					if(getUserapproval("Confirmez vous l\'enregistrement en base ? (O/N) : ")) {
						if(aliment.save()) {
							System.out.println("Nouvel aliment créé :");
							aliment.display();
						}
					}
					else
					{
						System.out.println("Vous avez souhaité abandonner de l\'enregistrement en base.");
					}
					
					InputStage="end";
					break;
						
			}
		}
	}
	
	
	
	
	private static Long getUserCategId() {
		Long result=0L;
		while(result == 0L ) {
			String userInput= getUserInput("Saisissez l\'identifiant de la catégorie de l\'aliment\nou V pour lister les catégories\nou Q pour abandoner la création : ");
			switch(userInput.toUpperCase()) {
				case "V" :
					findCategorieByName();
					break;
					
				case "Q" :
					result=ABORT_VALUE; // abort value
					break;
					
				default :
					try {
						result= Long.parseLong(userInput);
						if(Categorie.findCategorieByPk_id(result) == null) {
							System.out.println("Saisie inccorrecte : Veuillez saisir un ID d\'une catégorie déjà existante !");
							result= 0L;
						}
						
					} catch (Exception e) {
						System.out.println("Saisie incorrecte : Veuillez saisie un ID c\'est un nombre entier !");
					}
			}
		}
		return result;
	}
	
	
	private static Integer getUserInt(String prompt) {
		Integer result=-1;
		while(result == -1 ) {
			String userInput= getUserInput(prompt);
			switch(userInput.toUpperCase()) {
				case "Q" :
					result=-99; // abort value
					break;
					
				default :
					try {
						result= Integer.parseInt(userInput);
						if(result <0) {
							System.out.println("Saisie incorrecte : Veuillez saisir un nombre entier positif !");
							result = -1;
						}
						
					} catch (Exception e) {
						System.out.println("Saisie incorrecte : Veuillez saisir un nombre entier positif !");
					}
			}
		}
		return result;
	}

	
	private static Double getUserDouble(String prompt) {
		Double result=-1.0;
		while(result == -1.0 ) {
			String userInput= getUserInput(prompt);
			switch(userInput.toUpperCase()) {
				case "Q" :
					result=-99.0; // abort value
					break;
					
				default :
					try {
						result= Double.parseDouble(userInput);
						if(result <0) {
							System.out.println("Saisie incorrecte : Veuillez saisir un nombre double positif !");
							result = -1.0;
						}
						
					} catch (Exception e) {
						System.out.println("Saisie incorrecte : Veuillez saisir un nombre double positif !");
					}
			}
		}
		return result;
	}
	
	private static boolean getUserapproval(String prompt) {
		boolean result=false;
		boolean inputChecked= false;
		while(!inputChecked) {
			String userInput= getUserInput(prompt);
			switch(userInput.toUpperCase()) {
				case "O" :
					result=true;
					inputChecked= true;
					break;
				case "N" :
					result=false;
					inputChecked= true;
					break;
				default :
					System.out.println("Saisie incorrecte : Veuillez saisir O ou N");
			}
		}
		return result;
	}
	
	
	private static String getUserInput(String prompt) {
		System.out.print(prompt);
		String userInput= input.nextLine();
		System.out.println("");
		System.out.println("");
		return userInput.trim();
	}
}
