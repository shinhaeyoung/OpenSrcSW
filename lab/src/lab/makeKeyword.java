package lab;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class makeKeyword {

	public static void makeIndex(String file) throws ParserConfigurationException, SAXException, IOException {
		if(file.length() < 0)
			file = "./collection.xml";
		
		//xml 파일 접근
		File xmlfile = new File(file);
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		org.w3c.dom.Document doc = docBuilder.parse(xmlfile);
		doc.getDocumentElement().normalize();
		//body의 내용 수정
		String newbodyContent = "";
		NodeList nList = doc.getElementsByTagName("body");
		for(int i=0; i<nList.getLength(); i++) {
			Node nNode = nList.item(i);
//			System.out.println("\n현재 element: " + nNode.getNodeName());
//			System.out.println("element context: " + nNode.getTextContent());
			//꼬고마 형태소 분석기
			String bodyContent = nNode.getTextContent();
			KeywordExtractor ke = new KeywordExtractor();
			KeywordList kl = ke.extractKeyword(bodyContent, true);
			String newbodytemp = "";
			for(int j=0; j<kl.size(); j++) {
				Keyword kwrd = kl.get(j);
				newbodytemp = kwrd.getString() + ":" + kwrd.getCnt() + "#";
//				System.out.println(newbodyContent);
				newbodyContent += newbodytemp;
			}
			nNode.setTextContent(newbodyContent);
			newbodyContent = "";
		}
		
		//XML 파일로 쓰기
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		
		Transformer transformer = null;
		try {
			transformer = transformerFactory.newTransformer();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		}
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new FileOutputStream(new File("./index.xml")));
		
		try {
			transformer.transform(source, result);
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		
		System.out.println("끝");
		
	}

	
}
