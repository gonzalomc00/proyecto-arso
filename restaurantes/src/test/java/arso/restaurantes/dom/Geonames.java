package arso.restaurantes.dom;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Geonames {
	public static void main(String[] args) throws Exception {
		
		BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Introduce un código postal: ");
		String s=br.readLine();
		// 1. Obtener una factoría
		DocumentBuilderFactory factoria = DocumentBuilderFactory.newInstance();
		// 2. Pedir a la factoría la construcción del analizador
		DocumentBuilder analizador = factoria.newDocumentBuilder();
		// 3. Analizar el documento
		Document documento = analizador
				.parse(new URL("http://api.geonames.org/findNearbyWikipedia?postalcode="+s+"&country=ES&username=arso_gs").openStream());

		NodeList elementos=documento.getElementsByTagName("title");
		for(int i=0; i<elementos.getLength();i++) {
			
			Element entry= (Element) elementos.item(i);
						
			System.out.println(entry.getTextContent());
		}
		
		System.out.println("fin");
	}
}
