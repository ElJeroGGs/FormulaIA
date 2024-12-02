package Interfaz;

import java.util.Scanner;

public class InterfazPiloto {

    public InterfazPiloto() {
    }

    public void MenuPiloto() {
        System.out.println("1. Ver estado del coche");
        System.out.println("2. Ver estado de las ruedas");
        System.out.println("3. Cambiar ruedas");
        System.out.println("4. Salir");

        Scanner sc = new Scanner(System.in);
        int opcion = sc.nextInt();
        
    }


}
