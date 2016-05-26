package temperatura.FicherosMiMenu;

public class PrincipalMenu {
	public static void main(String[] args) {
		
		// Creacion del objeto MiMenu
		MiMenu menu = new MiMenu();
		// Aniadimos las opciones del men�
		menu.add("uno",100);
		menu.add("dos",200);
		menu.add("tres",300);
		menu.add("cuatro",400);
		menu.add("cinco");
		// La ultima la opcion es de salida
		menu.add("salir",500);
		
		// Bucle de gestion del menu
		boolean salir = false;
		while(!salir) 
		{
			switch(menu.ver("Demo 15-16"))
			{
				case 1: System.out.println("uno----->"+menu.getCodigo()); break;
				case 2: System.out.println("dos----->"+menu.getCodigo()); break;
				case 3: System.out.println("tres---->"+menu.getCodigo()); break;
				case 4: System.out.println("cuatro-->"+menu.getCodigo()); break;
				case 5: 
					System.out.println("El usuario ha seleccionado cinco"); 
					break;
				default:
					salir = true;
					break;
			}
		}
		System.out.println("Fin de la aplicaci�n (Prueba men�)");
	}	
}
