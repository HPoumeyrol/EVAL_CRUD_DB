package co.simplon.bnppf1.hugues;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ConsoleMenu {
	public static final String FORMAT_ERROR_MSG="Saisie invalide. Il faut saisir un nombre.";
	public static final String RANGE_ERROR_MSG="Saisie invalide. Il faut saisir un nombre entre 1 et ";
	private String[] options;
	private String header;
	private Scanner input;
	
	
	public ConsoleMenu(String header, String[] options, Scanner input) {
		this.header= header;
		this.options= options;
		this.input= input;
	}
	
	public ConsoleMenu(String[] options, Scanner input) {
		this.header= "";
		this.options= options;
		this.input= input;
	}
	private boolean isValid(int i) {
		return (i >0) && (i < options.length); 
	}
	
	
	public int askSelection() {
		
		String lineSeparator= repeat("-", header.length());
		
		int result= -1;
		do {
			System.out.format("%2$s\n%1$s\n%2$s\n",header,lineSeparator);
			for(int i=1; i != options.length; ++i) {
				System.out.format("\t%2d.  %s\n",i,options[i]);
			}
			System.out.print(options[0]);
			try {
				result= input.nextInt();
				if(!isValid(result)) {
					System.err.println(RANGE_ERROR_MSG+(options.length-1));
					result=-1;
				}
			}catch(InputMismatchException e) {
				System.err.println(FORMAT_ERROR_MSG);
			}
			input.nextLine();//flush remaining line feed in input buffer
		}while(result==-1);
		System.out.println(lineSeparator + "\n\n");
		return result;
	}
	
	
	
	public static void main(String[] args) {
		
		String[] options= {"title", "option 1", "option 2"};
		Scanner input= new Scanner(System.in);
		ConsoleMenu menu= new ConsoleMenu(options, input);
		System.out.println(menu.askSelection());
		
	}

	
	
	private String repeat(String str, int n) {
	    if (n < 0)
	        throw new IllegalArgumentException(
	                "the given repetition count is smaller than zero!");
	    else if (n == 0)
	        return "";
	    else if (n == 1)
	        return str;
	    else if (n % 2 == 0) {
	        String s = repeat(str, n / 2);
	        return s.concat(s);
	    } else
	        return str.concat(repeat(str, n - 1));
	}
	
	
}
