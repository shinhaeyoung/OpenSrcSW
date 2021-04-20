package lab;

import java.io.*;

public class genSnippet {

	public static void midexam(String file, String query) throws IOException {
		if(file.length() < 0)
			file = "./input.txt";
		
		File txtfile = new File(file);
		FileReader reader = new FileReader(txtfile);
		int c = 0;
		String content = "";
		while((c = reader.read())!=-1) {
			content += Character.toString((char)c);
//			String[] temp = bodyContent.split(" ");
			System.out.println(content);
		}
		String[] temp = content.split(" ");
	}
}
