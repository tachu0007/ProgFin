package temperatura;

import java.sql.*;
import java.util.Scanner;

public class Periodico {

	private Scanner teclado= new Scanner(System.in);
	
	//Constructor
	public Periodico () throws SQLException 
	{
		this.conectar();
	}
	
	//Conexion
	private Connection conectar() throws SQLException
	{
		String CadenaConexion = "jdbc:mysql://localhost:3306/periodico"
								+"?useUnicode=true&characterEncoding=8";
		String usuario="peri";
		String passwd="peri";
		
		return DriverManager.getConnection(CadenaConexion, usuario, passwd);
	}
	
	//Calculos(Statement)
	public boolean estaVacia()
	{
		String consulta= "SELECT count(*) as total FROM localidades";
		boolean sw=true; 
		try (
				Connection cn=this.conectar();
				Statement st=cn.createStatement();
				ResultSet rs= st.executeQuery(consulta);
			)
		{
			int total=0;
			if(rs.next())
				total=rs.getInt("total");
			sw =(total==0);
		}
		catch (SQLException e)
		{
			System.out.println("Error: "+ e.getMessage());
		}
		catch (Exception e)
		{
			System.out.println("Error: "+e.getMessage());
		}
		return sw;
	}
	
	//Insercci�n con m�todos (Statement)
	public void a�adir() 
	{
		String consulta="SELECT * FROM Localidades ORDER BY Localidad";
		try(
				 Connection cn = this.conectar();
				Statement st=cn.createStatement(
						ResultSet.TYPE_SCROLL_SENSITIVE,
						ResultSet.CONCUR_UPDATABLE);
				ResultSet rs= st.executeQuery(consulta);
			)
		{
			System.out.println("\n [Nuevo]");
			String localidad=teclado.nextLine().trim();
			
			System.out.print("M�nima: ");
			double minima=Double.parseDouble(teclado.nextLine().trim());
			
			System.out.print("M�xima: ");
			double maxima=Double.parseDouble(teclado.nextLine().trim());
			
			System.out.print("Pronostico: " );
			String pronostico=teclado.nextLine().trim();
			
			rs.moveToInsertRow();
			rs.updateString("Localidad", localidad);
			rs.updateDouble("M�nima", minima);
			rs.updateDouble("M�xima", maxima);
			rs.updateString("Pron�stico", pronostico);
			
			rs.insertRow();
			
			System.out.println("ok!");
		}
		catch(SQLException e)
		{
			System.out.println("Error BD: "+e.getMessage());
		}
		catch (Exception e)
		{
			System.out.println("Error: "+e.getMessage());
		}
	}
	
	//Recorrido(ResulSet)
	private int verDatos(ResultSet rs)
	{
		int cont=0;
		try
		{
			while(rs.next())
			{
				//acceso a los campos
				if(cont==0)
					encabezado(); 
				cont++;
				String localidad=rs.getString("Localidad");
				String pronostico=rs.getString("Pr�nostico");
				double maxima=rs.getDouble("M�xima");
				double minima=rs.getDouble("M�nima");
				
				//Salida en consola
				System.out.printf("%-20s %6.2f %6.2f %-20s\n", localidad, maxima, minima, pronostico);
			}
		}
		catch (SQLException e)
		{
			System.out.print("Error: "+e.getMessage());
		}
		catch (Exception e)
		{
			System.out.print("Error : "+e.getMessage());
		}
		return cont;
	}
	
	private void encabezado()
	{
		System.out.printf("%-20s %6s %6s %-20s\n", "Localidad", "Max", "min","Pronostico");
		System.out.printf("%-20s %6s %6s %-20s\n", "_________", "______", "______", "__________");
	}
	
	private void cabecera (String titulo)
	{
		System.out.printf("\n [%s]\n", titulo);
	}
	
	//Consulta sql(statemant)
	public void listado()
	{
		String s="SELECT *FROM localidades ORDER BY Localidad";
		
		try
		(
				//TYPE_FORWARD_ONLY CONCUR_READ_ONLY
				Connection cn=this.conectar();
				Statement st=cn.createStatement();
				ResultSet rs=st.executeQuery(s);
		)
		{
			cabecera("Listado");
			int cont=verDatos(rs);
			System.out.printf("En lista: %2d localidades\n", cont);
		}
		catch(SQLException e)
		{
			System.out.println("\nError BD: "+e.getMessage());
		}
		catch(Exception e)
		{
			System.out.print("\nError: "+e.getMessage());
		}
	}
	
	//Consulta Sql parametrizada (PreparedStatement)
	public int empiezaPor()
	{
		System.out.println("\n[Buscar (Empieza por)]");
		System.out.println("�Localidad?(Empieza por)");
		String buscar = teclado.nextLine().trim().toLowerCase();
		
		String s="select * from localidades where Localidad like ? order by Localidad";
		
		
		int cont=0;
		try(
			Connection cn=this.conectar();
			PreparedStatement ps=cn.prepareStatement(s);
			)
		{
			//Valor para los par�metros
			ps.setString(1, buscar+"%");
			
			//Lanzamos la consulta
			try (ResultSet rs=ps.executeQuery())
			{
				cabecera("Buscar> empieza por");
				cont= verDatos(rs);
			}
			System.out.printf("En lista: %2d localidades\n", cont);
		}
		catch(SQLException e)
		{
			System.out.println("\nError BD: "+e.getMessage());
		}
		catch(Exception e)
		{
			System.out.print("\nError: "+e.getMessage());
		}
		return cont;
	}
	
	//Consulta SQL parametrizada (por concatenaci�n)
	public int incluye()
	{
		System.out.println("\n[Buscar (Empieza por)]");
		System.out.println("�Localidad?(Empieza por)");
		String buscar = teclado.nextLine().trim().toLowerCase();
		
		String s="select * from localidades where Localidad like '%"+buscar+"%' order by Localidad";
		
		int cont=0;
		try
		(
			Connection cn=this.conectar();
			Statement st=cn.createStatement();
			ResultSet rs=st.executeQuery(s);
		)
		{
			cabecera("Buscar> incluye");
			cont = verDatos(rs);
			System.out.printf("En lista: %2d localidades\n", cont);
		}
		catch(SQLException e)
		{
			System.out.println("\nError BD: "+e.getMessage());
		}
		catch(Exception e)
		{
			System.out.print("\nError: "+e.getMessage());
		}
		return cont;
	}
	
	//Consulta SQL parametrizada por concatenacion (Statement)
	public void eliminarUnoAUno()
	{
		int cont=0, borrados=0;
		
		System.out.println("\n[Eliminar (uno a uno)/(Empiezza por)]");
		
		System.out.print("�Localidad? ");
		String buscar=teclado.nextLine().trim().toLowerCase();
		
		String s="SELECT * FROM Localidades WHERE localidad like '"+buscar+"%'ORDER BY localidad";
		
		try
		(
				Connection cn=this.conectar();
				Statement st=cn.createStatement(
						ResultSet.TYPE_FORWARD_ONLY,
						ResultSet.CONCUR_UPDATABLE);
				ResultSet rs=st.executeQuery(s);
		)
		{
			while (rs.next())
			{
				if (cont==0)
				{
					cabecera("Eliminar por nombre de localidad");
					encabezado();
				}
				cont++;
				
				//Ver datos
				String localidad = rs.getString("Localidad");
				String pronostico = rs.getString("Pron�stico");
				double maxima = rs.getDouble("M�xima");
				double minima = rs.getDouble("M�nima");
				
				//Salida en consola
				System.out.printf("%-20s %6.2f %6.2f %-20s\n", localidad, maxima, minima, pronostico);
				
				System.out.print("�Eliminar? (s/n)");
				char resp=teclado.nextLine().toLowerCase().trim().chartAt(0);
				if(resp=='s')
				{
					rs.deleteRow();
					System.out.print("Localidad borrada");
					borrados++;
				}
			}
			if (cont==0)
				System.out.println("No se han encontrado datos");
		}
		catch(SQLException e)
		{
			System.out.println("\nError BD: "+e.getMessage());
		}
		catch(Exception e)
		{
			System.out.print("\nError: "+e.getMessage());
			e.printStackTrace();
		}
		System.out.printf("Borrados: %2d localidades de %2d\n", borrados, cont);
	}
	
	//Consulta SQL parametrizada por concatenacion (statement)
	 public void modificarUnoAUno()
	 {
		 int cont=0;,modificados=0; 
		 System.out.println("\n [Modificar(uno a uno)/(Empieza por)]");
		 
		 System.out.print("�Localidad? (Empieza por) ");
		 String buscar = teclado.nextLine().trim().toLowerCase();
		 
		 String s ="SELECT * FROM Localidades WHERE localidad LIKE '"+buscar+"%' ORDER BY localidad";
		 
		 try
		 (
				 Connection cn = this.conectar();
					Statement st=cn.createStatement(
							ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_UPDATABLE);
					ResultSet rs= st.executeQuery(consulta);
		 )
		 {
			 while(rs.next())
			 {
				 if(cont==0)
					 cabecera("Modificador");
				 cont++;
				 
				 //Ver datos
				 String localidad=rs.getString("localidad");
				 String pronostico= rs.getString("pron�stico");
				 String maxima=rs.getDouble("m�xima");
				 String minima=rs.getDouble("m�nima");
				 
				 //Salida en consola
				 System.out.printf("Localidad: %s\nM�xima: %6.2f\nM�nima: %6.2f\nPr�nostico: %-20s\n", localidad, maxima, minima, pronostico);
				 
				 System.out.print("�Modificar? (s/n)");
				 char resp=teclado.nextLine().toLowerCase().trim().charAt();
				 if(resp=='s')
				 {
					 System.out.print("�Localidad?["+localidad+"]");
					 localidad=teclado.nextLine().trim();
					 	if (localidad.length()>0)
					 	{
					 		rs.updateString("localidad", localidad);
					 	}
					 	
					 System.out.print("�M�xima? ["+maxima+"]");
					 String smax=teclado.nextLine().trim();
					 if(smax.length()>0)
					 {
						 double valor=Double.parseDouble(smax);
						 rs.updateDouble("m�xima", maxima);
					 }
					 
					 System.out.print("�M�nima? ["+minima+"]");
					 String smin = teclado.nextLine().trim();
					 if(smin.length()>0)
						 rs.updateDouble("m�nima", Double.parseDouble(smin));
						 
						 System.out.print("�Pr�nostico? ["+Pronostico+"]");
						 pronostico=teclado.nextLine().trim();
						 if(pronostico.length()>0)
							 rs.updateString("Pron�stico", pronostico);
						 
						 rs.updateRow();
						 modificados++;
				 }
			 }
		 }
		 	catch(SQLException e)
			{
				System.out.println("Error BD: "+e.getMessage());
			}
			catch(Exception e)
			{
				System.out.print("Error: "+e.getMessage());
				e.printStackTrace();
			}
			System.out.printf("Modificados: %2d localidades de %2d\n", modificados, cont);
	 }
	 
	 //Consulta SQL. Procedimiento almacenado (CallableStatement)
	 
	 // Medias()
	 //SELECT AVG(Localidades.M�nima) AS MediaMin, AVG(Localidades.M�xima) AS MediaMax
	 //FROM Localidades;
	 
	 public void medias()
	 {
		 try(
				 Connection cn=this.conectar();
				 CallableStatement cs=cn.prepareCall("{call Medias}");
				 ResultSet rs=cs.executeQuery(); 
				 )
		 {
			 if (rs.next())
			 {
				 System.out.println("\n [Medkas de temperatura]");
				 
				 System.out.printf("\nM�xima> Media: %5.2f", rs.getDouble("MediaMax"));
				 System.out.printf("\nM�nimas> Media: %5.2f\n", rs.getDouble("MediaMin"));
			 }
			 else
			 {
				 System.out.println("Sin resultados");
			 }
		 }
		 catch(SQLException e)
			{
				System.out.println("Error BD: "+e.getMessage());
			}
		 catch(Exception e)
			{
				System.out.print("Error: "+e.getMessage());
				e.printStackTrace();
			}
	 }
	 
}
