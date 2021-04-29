package lab;

import java.io.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import org.w3c.dom.Element;

public class makeCollection {
	
	public static void makeXML(String path) throws IOException, ParserConfigurationException {
		if(path.length() < 0)
			path = "./data/";
		
		//.html ���� ����
		String pattern = ".html";
		File files = new File(path);
		String fileList[] = files.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(pattern); //.html �������� ������ ����
			}
		});
		
		//xml ����
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		
		org.w3c.dom.Document doc = docBuilder.newDocument();
		
		//docs element
		Element docs = doc.createElement("docs");
		doc.appendChild(docs);
		
		//xml ����
		if(fileList.length > 0) {
			for(int i=0; i<fileList.length; i++) {
//				System.out.println(fileList[i]);
				
				//doc element
				Element ele_doc = doc.createElement("doc");
				docs.appendChild(ele_doc);
				//doc id �Ӽ�
				ele_doc.setAttribute("id", Integer.toString(i));
				
				//parsing
				Document document = Jsoup.parse(new File(path + "/" + fileList[i]), "UTF-8");
				
				//title element
				Element title = doc.createElement("title");
				//html�� title->xml�� title
				Elements titles = document.select("title");
//				System.out.println("title: " + titles.html());
				title.appendChild(doc.createTextNode(titles.html()));
				ele_doc.appendChild(title);
				
				//body element
				Element body = doc.createElement("body");
				//html�� p->xml�� body
				Elements ps = document.select("#content p");
				for(org.jsoup.nodes.Element e: ps)
//					System.out.println(e.html());
					body.appendChild(doc.createTextNode(e.html()));
				ele_doc.appendChild(body);
			}
		}
		
		//XML ���Ϸ� ����
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		
		Transformer transformer = null;
		try {
			transformer = transformerFactory.newTransformer();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		}
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new FileOutputStream(new File("./collection.xml")));
		
		try {
			transformer.transform(source, result);
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		
		System.out.println("��");
	}

}
