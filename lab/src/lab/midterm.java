package lab;

import java.io.IOException;

public class midterm {

	public static void main(String[] args) throws IOException {
		if(args[0].equals("-f")) {
			if(args[2].equals("-q"))
				genSnippet.midexam(args[1], args[3]);
			else
				System.out.println("잘못된 인자가 입력되었습니다");
		}
	}

}
