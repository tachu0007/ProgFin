package temperatura;

public class Principal {

	public static void main(String[] args) {
		
		Periodico per=null;
		try
		{
			per=new Periodico();
			System.out.println("Conexión con la base de datos> ¡OK!");
		}
		catch (Exception e)
		{
			System.err.println("Error: "e.getMessage());
			//fin del programa
			return;
		}
		
		//Menú
		MiMenu menu = new MiMenu();
		menu.añadir("Listado");
		menu.añadir("Nuevo");
		menu.añadir("Modificar (uno a uno)");
		menu.añadir("Eliminar (uno a uno)");
		menu.añadir("Empieza por");
		menu.añadir("Incluye");
		menu.añadir("Medias");
		menu.añadir("Salida");
		
		MiMenu menu reducido = new MiMenu();
		reducido.añadir("Nuevo");
		reducido.añadir("Salida");
		
		booblean salir = false;
		do
		{
			if (per. estaVacia()==true)
			{
				Systme.out.println("BD vacía");
				switch(reducido.ver("Periódico DAM"))
				{
				case 1:per.añadir();
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
					case 2: per.añadir();break;
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
