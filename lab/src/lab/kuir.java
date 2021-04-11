package lab;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class kuir {

	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException {
		switch(args[0]) {
		case "-c":
			makeCollection.makeXML(args[1]);
			break;
		case "-k":
			makeKeyword.makeIndex(args[1]);
			break;
		case "-i":
			indexer.makeInvert(args[1]);
			break;
		case "-s":
			if(args[2].equals("-q"))
				searcher.searchIndex(args[1], args[3]);
			else
				System.out.println("잘못된 인자가 입력되었습니다");
			break;
		}
	}

}
