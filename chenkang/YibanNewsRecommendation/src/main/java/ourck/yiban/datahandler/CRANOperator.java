package ourck.yiban.datahandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CRANOperator {

	public void execScript(String cmd) throws IOException {
		Runtime terminal = Runtime.getRuntime();
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(
						terminal.exec(cmd).getInputStream()));
		
		StringBuilder stb = new StringBuilder();
		String line = null;
		while((line = reader.readLine()) != null)
			stb.append(line + "\n");
		
		System.out.println(stb.toString());
		
		reader.close();
	}
}
