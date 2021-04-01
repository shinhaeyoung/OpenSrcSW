package lab;

import java.io.*;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class searcher {
	
	static ArrayList<String> keyword = null;  //query keyword
	@SuppressWarnings("rawtypes")
	static HashMap hashMap = null; //from index.post
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void searchIndex(String postfile, String query) throws IOException, ClassNotFoundException, ParserConfigurationException, SAXException {
		if(postfile.length() < 0)
			postfile = "./index.post"; //by ��Ŭ����
//			postfile = "./bin/index.post"; //by cmd
		
		if(query.length() < 0) {
			System.out.println("query�� �Է����ּ���");
			return;
		}
		
		//index.post �о����
		FileInputStream fileInputStream = new FileInputStream("./index.post");
		ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
		Object object = objectInputStream.readObject();
		objectInputStream.close();
		hashMap = (HashMap)object;
		
		//query �м� - ���� ���¼� �м���
		String queryContent = query;
		keyword = new ArrayList<String>(); //query keyword
		KeywordExtractor ke = new KeywordExtractor();
		KeywordList kl = ke.extractKeyword(queryContent, true);
		Iterator<String> it = hashMap.keySet().iterator();
		while(it.hasNext()) {
			String key = it.next();
			for(int j=0; j<kl.size(); j++) {
				Keyword kwrd = kl.get(j);
				if(key.equals(kwrd.getString()))
					keyword.add(kwrd.getString());
			}
		}
		System.out.println(keyword);
		
		//query�� ���� ���� id�� ���絵 Array - index==id
		ArrayList<Double> similarArray = new ArrayList<Double>();
		int N = 5;
		for(int id=0; id<N; id++)
			similarArray.add(calSimilar(id));
//		System.out.println(similarArray);
		
		//��� ���
		int[] rank = new int[N];
		Arrays.fill(rank, 1);
		for(int i=0; i<similarArray.size(); i++) {
			for(int j=0; j<similarArray.size(); j++) {
				if(similarArray.get(i)<similarArray.get(j))
					rank[i]++;
			}
		}
		
		//xml ���� ����
		String file = "./collection.xml"; //by ��Ŭ����
//		String file = "./bin/collection.xml"; //by cmd
		File xmlfile = new File(file);
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		org.w3c.dom.Document doc = docBuilder.parse(xmlfile);
		doc.getDocumentElement().normalize();
		//title ��������
		NodeList nList = doc.getElementsByTagName("title");
		ArrayList<String> titles = new ArrayList<String>();
		for(int i=0; i<nList.getLength(); i++)
			titles.add(nList.item(i).getTextContent());

		//���� 3�� title ���
		System.out.println("<���� 3�� title>");
		int count = 0;
		for (int id=0; id<similarArray.size(); id++) {
			if(rank[id] >= 1 && rank[id] <= 3 && count < 3) {
				count++;
				//���� id�� title ���
				System.out.println("���� id: " + id + ", rank: " + rank[id] + ", title: " + titles.get(id));
			}	
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public static double calSimilar(int id) {
		double similarity = 0.0;
		for(int i=0; i<keyword.size(); i++) {
			String key = keyword.get(i);
			ArrayList<String> value = (ArrayList<String>) hashMap.get(key);
			for(int j=0 ;j<value.size(); j=j+2) {
				if(Integer.parseInt(value.get(j)) == id) {
					similarity += Double.parseDouble(value.get(j+1));
				}
			}
		}
		return similarity;
	}
}
