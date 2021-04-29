package lab;

import java.io.*;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class indexer {
	
	static ArrayList<ArrayList<String>> indexList = null;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void makeInvert(String indexfile) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException {
		if(indexfile.length() < 0)
			indexfile = "./index.xml"; //by 이클립스
//			indexfile = "./bin/index.xml"; //by cmd
		
		//index.xml 파일 접근
		File xmlfile = new File(indexfile);
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		org.w3c.dom.Document doc = docBuilder.parse(xmlfile);
		doc.getDocumentElement().normalize();
		
		//body 읽기 - 단어:빈도수 -> ArrayList<(id=y, word=x, tf)>
		indexList = new ArrayList<ArrayList<String>>();
		ArrayList<String> tempList = null;
		NodeList nList = doc.getElementsByTagName("body");
		for(int i=0; i<nList.getLength(); i++) {
			Node nNode = nList.item(i);
			String bodyContent = nNode.getTextContent();
			String[] temp = bodyContent.split(":|#");
			for(int j=0; j<temp.length; j=j+2) {
				tempList = new ArrayList<String>();
				tempList.add(Integer.toString(i));
				tempList.add(temp[j]);
				tempList.add(temp[j+1]);
				indexList.add(tempList);
			}
		}
//		System.out.println(indexList);
		
		//hashmap생성
		HashMap indexMap = new HashMap();
		
		//key 중복X
		Set<String> keySet = new HashSet<>();
		for(int i=0; i<indexList.size(); i++) {
			keySet.add(indexList.get(i).get(1));
		}
		ArrayList<String> keyArray = new ArrayList<>(keySet);
		
		for(int i=0; i<keyArray.size(); i++) {
			indexMap.put(keyArray.get(i), getWeight(keyArray.get(i))); //value => [id, weight, ...]
		}
		
		//check
//		Iterator<String> it = indexMap.keySet().iterator();
//		while(it.hasNext()) {
//			String keyCheck = it.next();
//			Object valueCheck = indexMap.get(keyCheck);
//			System.out.println(keyCheck + "->" + valueCheck);
//		}
		
		//index.post 생성 - 파일에 indexMap 저장
		FileOutputStream fileOutputStream = new FileOutputStream("./index.post");
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
		objectOutputStream.writeObject(indexMap);
		objectOutputStream.close();
		
		//확인용 - index.post 읽어오기
//		FileInputStream fileInputStream = new FileInputStream("./index.post");
//		ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
//		Object object = objectInputStream.readObject();
//		objectInputStream.close();
//		HashMap hashMap = (HashMap)object;
//		Iterator<String> it = hashMap.keySet().iterator();
//		while(it.hasNext()) {
//			String key = it.next();
//			Object value = hashMap.get(key);
//			System.out.println(key + "->" + value);
//		}
	}

	//각 문서에서 가중치 구하기 - 가중치=tf*idf => indexMap의 value
	public static ArrayList<String> getWeight(String word) {
		int N = 5;
		String id = null;
		int tf = 0;
		int df = 0;
		double tfidf = 0.0;
		String weight = null;
		ArrayList<String> value = new ArrayList<>();
		for(int i=0; i<indexList.size(); i++) {
			if(word.equals(indexList.get(i).get(1)))
				df++;
		}
		for(int i=0; i<indexList.size(); i++) {
			//문서에 존재하는 키워드만 - 전체 list에서 입력 word 비교
			if(word.equals(indexList.get(i).get(1))) {
				id = indexList.get(i).get(0);
				tf = Integer.parseInt(indexList.get(i).get(2));
				tfidf = Double.parseDouble(String.format("%.2f",tf*Math.log((double)N/df)));
				weight = Double.toString(tfidf);
				value.add(id);
				value.add(weight);
//				System.out.println(word + ", id: " + id + ", df: " + df + ", tf: " + tf +
//						", tf*idf전: " + tf*Math.log(N/df) + ", tf*idf후: " + tfidf + ", weight: " + weight);
			}
		}
		return value;
	}
	
}