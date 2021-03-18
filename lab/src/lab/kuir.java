package lab;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class kuir {

	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
		switch(args[0]) {
		case "-c":
			makeCollection.makeXML(args[1]);
			break;
		case "-k":
			makeKeyword.makeIndex(args[1]);
			break;
		}

	}

}
