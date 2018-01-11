package Crawler_work;

import java.io.*;

public class Work_2 {
	public static File newfile;
	public static File oldfile;
	public static String newfilepath;
	public static String oldfilepath;
	private static FileWriter fw=null;
	private static BufferedWriter bw=null;
	private static FileReader fr=null;
	private static BufferedReader br=null;
	public static File[] list;
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Work_2 work = new Work_2();
		work.copy();

	}

	public void copy() throws Exception {
		int a=1;
		for (int i = 0; i < list.length; i++) {
			// System.out.println(list[i].getName());
			String name=list[i].getName();
			if (match(name, "数据")) {
				String[] name1=name.split(",");
				name=String.valueOf(a)+"@"+name1[0]+".txt";
				String oldPath = oldfilepath + "/" + list[i].getName();
				System.out.println(oldPath);
				String newPath = newfilepath + "/" + name;
				System.out.println(newPath);
				File newFile = new File(newPath);
				newfile.createNewFile();
				fr=new FileReader(oldPath);
				br=new BufferedReader(fr);
				fw=new FileWriter(newPath);
				bw=new BufferedWriter(fw);
				String n=null;
				while((n=br.readLine())!=null){
					bw.write(n+"\r\n");
				}
				a++;
				bw.close();
				fw.close();
				br.close();
				fr.close();
			}
		}
	}

	public Work_2() {
		newfilepath = "G:/dashuju/有效数据(第二步)";
		oldfilepath = "G:/dashuju/有效数据(初步)";
		newfile = new File(newfilepath);
		oldfile = new File(oldfilepath);
		newfile.mkdir();
		list = oldfile.listFiles();
		System.out.println(list.length);
	}

	public boolean match(String name, String valid) {
		if (name.indexOf(valid) == -1) {
			return false;
		} else {
			return true;
		}
	}
}
