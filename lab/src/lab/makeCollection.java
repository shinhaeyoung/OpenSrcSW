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
		
		//.html 파일 접근
		String pattern = ".html";
		File files = new File(path);
		String fileList[] = files.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(pattern); //.html 형식으로 끝나는 파일
			}
		});
		
		//xml 파일
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		
		org.w3c.dom.Document doc = docBuilder.newDocument();
		
		//docs element
		Element docs = doc.createElement("docs");
		doc.appendChild(docs);
		
		//xml 생성
		if(fileList.length > 0) {
			for(int i=0; i<fileList.length; i++) {
//				System.out.println(fileList[i]);
				
				//doc element
				Element ele_doc = doc.createElement("doc");
				docs.appendChild(ele_doc);
				//doc id 속성
				ele_doc.setAttribute("id", Integer.toString(i));
				
				//parsing
				Document document = Jsoup.parse(new File(path + "/" + fileList[i]), "UTF-8");
				
				//title element
				Element title = doc.createElement("title");
				//html의 title->xml의 title
				Elements titles = document.select("title");
//				System.out.println("title: " + titles.html());
				title.appendChild(doc.createTextNode(titles.html()));
				ele_doc.appendChild(title);
				
				//body element
				Element body = doc.createElement("body");
				//html의 p->xml의 body
				Elements ps = document.select("#content p");
				for(org.jsoup.nodes.Element e: ps)
//					System.out.println(e.html());
					body.appendChild(doc.createTextNode(e.html()));
				ele_doc.appendChild(body);
			}
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
		StreamResult result = new StreamResult(new FileOutputStream(new File("./collection.xml")));
		
		try {
			transformer.transform(source, result);
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		
		System.out.println("끝");
	}

}
