package projectBE;

/*<applet code=AppletDemo.class
 * width=700
 * height=700>
 *</applet> */

import java.applet.*;
import java.awt.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.io.StringReader;

import java.util.Scanner;
import java.util.StringTokenizer;

@SuppressWarnings( { "serial", "unused", "static-access" })
public class AppletDemo extends Applet {
	int x, y;
	int i = 0;

	public void paint(Graphics g) {
		String s = readFile1();
		StreamTokenizer st = new StreamTokenizer(new StringReader(s));
		this.setForeground(Color.RED);
		try {
			while (st.nextToken() != st.TT_EOF) {
				x = (int) st.nval;
				st.nextToken();
				y = (int) st.nval;
				g.fillOval(x, y, 1, 1);
			}
			g.drawString("Out of first while", 100, 100);
		} catch (Exception e) {
			e.printStackTrace();
		}
		/********************************************************/

		s = readFile2();
		st = new StreamTokenizer(new StringReader(s));
		this.setForeground(Color.GREEN);
		try {
			while (st.nextToken() != st.TT_EOF) {
				x = (int) st.nval;
				st.nextToken();
				y = (int) st.nval;
				g.fillOval(x, y, 2, 2);
			}
			g.drawString("Out of Second while", 100, 100);
		} catch (Exception e) {
			e.printStackTrace();
		}
		/************************************************************/

		s = readFile3();
		st = new StreamTokenizer(new StringReader(s));
		this.setForeground(Color.BLUE);
		try {
			while (st.nextToken() != st.TT_EOF) {
				x = (int) st.nval;
				st.nextToken();
				y = (int) st.nval;
				g.fillOval(x, y, 2, 2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		i++;
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
				f.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return ret;
	}
}