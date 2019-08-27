package final_project;

import java.util.ArrayList;

//In the main class,should decide the number of the card 
//what to eat first no matter what the color of it. 
//for example, if the card is 
//"B1",take one
//"B2",take two
//"B3-B7", take three
//"B8",take eight
//"B9",take night


public class eat {
	
	public eat() {};
	
	//one
	public void one(String thrown,card players[]) {
		int n0 = thrown.charAt(0); //input color
		int n1    = thrown.charAt(1); //number 1
		int c2=0;
		int c3=0;
		for (int i =0;i<16;i++) {
			int  color= players[i].str_generate().charAt(0);//color
			int  number= players[i].str_generate().charAt(1);
			if (color==n0) {
				if (number==n1+1) {
					c2 +=1;}
				else if (number==n1+2) {
					c3 +=1;
				}
				if (c2+c3==2) {
					System.out.println("i want to eat");
					System.out.println("123");
					break;
				}
			}
	}
	}

		
	//two
	public void two(String thrown,card players[]) {
		int n0 = thrown.charAt(0); //input color
		int n1    = thrown.charAt(1); //number 2
		int c3=0;
		int c4=0;
		int c1=0;
		for (int i =0;i<16;i++) {
			int  color= players[i].str_generate().charAt(0);//color
			int  number= players[i].str_generate().charAt(1);
			if (color==n0) { //color matches first
				if (number==n1-1) {
					c1 +=1;}
				else if (number==n1+1){
					c3 +=1;}
				else if (number==n1+2) {
					c4 +=1;}
				
				if (c1+c3==2){
					System.out.println(" i want to eat");
					System.out.println("123");
					break;
				}
				else if (c3+c4==2) {
					System.out.println(" i want to eat");
					System.out.println("234");
					break;
				}
				}
			}
		}
		
	
	//three_to_seven
	public void three(String thrown,card players[]) {
		int n0 = thrown.charAt(0); //input color
		int n1    = thrown.charAt(1); //number 3-7
		int b2=0;
		int b1=0;
		int f1=0;
		int f2=0;
		for (int i =0;i<16;i++) {
			int  color= players[i].str_generate().charAt(0);//color
			int  number= players[i].str_generate().charAt(1);
			if (color==n0) { //color matches first
				if (number==n1-1) {
					b1 +=1;}
				else if (number==n1+1){
					f1 +=1;}
				else if (number==n1-2) {
					b2 +=1;}
				else if (number==n1+2) {
					f2 +=1;}
				
				if (b1+b2==2){
					System.out.println(" i want to eat");
					break;
				}
				else if (b1+f1==2) {
					System.out.println(" i want to eat");
					break;
				}
				else if (f1+f2==2) {
					System.out.println(" i want to eat");
					break;
					}}}
	}
	
	//eight
	public void eight(String thrown,card players[]) {
		int n0 = thrown.charAt(0); //input color
		int n1    = thrown.charAt(1); //number 8
		int c9=0;
		int c7=0;
		int c6=0;
		for (int i =0;i<16;i++) {
			int  color= players[i].str_generate().charAt(0);//color
			int  number= players[i].str_generate().charAt(1);
			if (color==n0) { //color matches first
				if (number==n1-2) {
					c6 +=1;}
				else if (number==n1-1){
					c7 +=1;}
				else if (number==n1+1) {
					c9 +=1;}
				
				if (c6+c7==2){
					System.out.println(" i want to eat");
					System.out.println("678");
					break;
				}
				else if (c7+c9==2) {
					System.out.println(" i want to eat");
					System.out.println("789888888");
					break;
				}}}
	}
	
	//nine
	public void nine(String thrown,card players[]) {
		int n0 = thrown.charAt(0); //input color
		int n1    = thrown.charAt(1); // number 9
		int c7=0;
		int c8=0;
		for (int i =0;i<16;i++) {
			int  color= players[i].str_generate().charAt(0);//color
			int  number= players[i].str_generate().charAt(1);
			if (color==n0) {
				if (number==n1-1) {
					c8 +=1;}
				else if (number==n1-2) {
					c7 +=1;
				}
				if (c7+c8==2) {
					System.out.println(" i want to eat");
					System.out.println("7899999999");
					break;
				}
			}
	}
	}

}	

	
	
	
	
	
			
	
		
		
		
		
		
		
	
	

