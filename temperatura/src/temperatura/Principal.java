package temperatura;

public class Principal {

	public static void main(String[] args) {
		
		Periodico per=null;
		try
		{
			per=new Periodico();
			System.out.println("Conexi�n con la base de datos> �OK!");
		}
		catch (Exception e)
		{
			System.err.println("Error: "e.getMessage());
			//fin del programa
			return;
		}
		
		//Men�
		MiMenu menu = new MiMenu();
		menu.a�adir("Listado");
		menu.a�adir("Nuevo");
		menu.a�adir("Modificar (uno a uno)");
		menu.a�adir("Eliminar (uno a uno)");
		menu.a�adir("Empieza por");
		menu.a�adir("Incluye");
		menu.a�adir("Medias");
		menu.a�adir("Salida");
		
		MiMenu menu reducido = new MiMenu();
		reducido.a�adir("Nuevo");
		reducido.a�adir("Salida");
		
		booblean salir = false;
		do
		{
			if (per. estaVacia()==true)
			{
				Systme.out.println("BD vac�a");
				switch(reducido.ver("Peri�dico DAM"))
				{
				case 1:per.a�adir();
				break;
				default:
					salir=true;
					break;
				}
			}
			else
			{
				switch(menu.ver("Periodico DAM"))
				{
					case 1: per.listado();break;
					case 2: per.a�adir();break;
					case 3: per.modificarUnoAUno();break;
					case 4: per.eliminarUnoAUno();break;
					case 5: per.empiezaPor();break;
					case 6: per.incluye();break;
					case 7: per.medias();break;
					default:
						salir=true;
					break;
				}
			}
		}
		while (!salir);
		System.out.println("CMA-DAM. Fin del programa");
	}//Fin del main
}//Fin de la clase
