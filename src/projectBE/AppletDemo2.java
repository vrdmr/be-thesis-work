package projectBE;

/*<applet Code="AppletDemo.class" width=400 height=400>
 </applet>*/
import java.applet.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.io.StringReader;

@SuppressWarnings("serial")
public class AppletDemo2 extends Applet {
	int x, y;
	int i = 0;

	public void paint(Graphics g) {

		filloval1(g);
		filloval2(g);
		filloval3(g);
		// i++
	}// end of paint()

	@SuppressWarnings("static-access")
	private void filloval3(Graphics g) {
		// TODO Plot points from file 3
		g.setColor(Color.BLUE);
		int x3, y3;
		String s = readFile3();
		StreamTokenizer st = new StreamTokenizer(new StringReader(s));
		try {
			while (st.nextToken() != st.TT_EOF) {
				x3 = (int) st.nval;
				st.nextToken();
				y3 = (int) st.nval;
				g.fillOval(x3 * 5, y3 * 5, 5, 5);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("static-access")
	private void filloval2(Graphics g) {
		// TODO Plot points from file2
		g.setColor(Color.DARK_GRAY);
		int x2, y2;
		String s = readFile2();
		StreamTokenizer st = new StreamTokenizer(new StringReader(s));
		try {
			while (st.nextToken() != st.TT_EOF) {
				x2 = (int) st.nval;
				st.nextToken();
				y2 = (int) st.nval;
				g.fillOval(x2 * 5, y2 * 5, 5, 5);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("static-access")
	private void filloval1(Graphics g) {
		// TODO Plot points from file1
		g.setColor(Color.RED);
		int x1, y1;
		String s = readFile1();
		StreamTokenizer st = new StreamTokenizer(new StringReader(s));
		try {
			while (st.nextToken() != st.TT_EOF) {
				x1 = (int) st.nval;
				st.nextToken();
				y1 = (int) st.nval;
				g.fillOval(x1 * 5, y1 * 5, 5, 5);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String readFile1() {
		String line, ret = "";
		try {
			FileInputStream f = new FileInputStream(
					"/Users/varadmeru/Documents/JavaWorkspace/CollegeProject/Cluster0.txt");
			BufferedReader myInput = new BufferedReader(
					new InputStreamReader(f));
			try {
				while ((line = myInput.readLine()) != null)
					ret += line + "\n";
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return ret;

	}

	public String readFile2() {
		String line, ret = "";

		try {
			FileInputStream f = new FileInputStream(
					"/Users/varadmeru/Documents/JavaWorkspace/CollegeProject/Cluster1.txt");
			BufferedReader myInput = new BufferedReader(
					new InputStreamReader(f));
			try {
				while ((line = myInput.readLine()) != null)
					ret += line + "\n";
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return ret;

	}

	public String readFile3() {
		String line, ret = "";

		try {
			FileInputStream f = new FileInputStream(
					"/Users/varadmeru/Documents/JavaWorkspace/CollegeProject/Cluster2.txt");
			BufferedReader myInput = new BufferedReader(
					new InputStreamReader(f));
			try {
				while ((line = myInput.readLine()) != null)
					ret += line + "\n";
				// f.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return ret;

	}
}