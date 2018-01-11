package Crawler_work;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

public class Work_3 {

	public static File newfile;
	public static File oldfile;
	public static File characterfile;
	public static String newfilepath;
	public static String oldfilepath;
	public static String characterfilepath;
	private static FileWriter fw = null;
	private static BufferedWriter bw = null;
	private static FileReader fr = null;
	private static BufferedReader br = null;
	public static File[] list;
	private static HashMap<String, String> character;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Work_3 work_3=new Work_3();
		work_3.work();
	}

	public void work() {
		for (int i = 0; i < list.length; i++) {
			// System.out.println(list[i].getName());
			String name = list[i].getName();
			String oldPath = oldfilepath + "/" + list[i].getName();
			System.out.println("当前文件是：" + name);
			try {
				fr = new FileReader(oldPath);
				br = new BufferedReader(fr);
				String n = null;
				boolean jump = false;
				n = br.readLine();
				while ((n = br.readLine()) != null && jump == false) {
					checkcharacter(n);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					br.close();
					fr.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
	}

	// 检查特征
	public boolean checkcharacter(String s) {
		boolean result = false;
		s = s.toLowerCase();// 转换成小写
		Iterator iter = character.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = (String) entry.getKey();
			if (s.indexOf(key) != -1) {
				try {
					//单项增加
					result = true;
					fr=new FileReader(character.get(key)+ "/" + key + ".txt");
					br=new BufferedReader(fr);
					int num=1;
					String n=br.readLine();
					if(n!=null&&!n.equals(""))
					{
						num=Integer.parseInt(n.trim());
						num++;
					}
					fw = new FileWriter(character.get(key)+ "/" + key + ".txt");
					bw = new BufferedWriter(fw);
					bw.write(num+"");
					System.out.println("检查到特征："+key);
					bw.close();
					fw.close();
					//总增加
					fr=new FileReader(character.get(key)+ "/" + "总.txt");
					br=new BufferedReader(fr);
					int num1=1;
					String n1=br.readLine();
					if(n1!=null&&!n1.equals(""))
					{
						num1=Integer.parseInt(n1.trim());
						num1++;
					}
					fw = new FileWriter(character.get(key)+ "/" + "总.txt");
					bw = new BufferedWriter(fw);
					bw.write(num1+"");
					System.out.println("检查到特征："+key);
					bw.close();
					fw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public Work_3() {
		// 初始化路径
		newfilepath = "G:/dashuju/有效数据(第三步)";
		oldfilepath = "G:/dashuju/有效数据(第二步)";
		characterfilepath = newfilepath + "/特征.txt";
		newfile = new File(newfilepath);
		oldfile = new File(oldfilepath);
		characterfile = new File(characterfilepath);
		newfile.mkdir();
		try {
			characterfile.createNewFile();
			// 初始化文件列表
			list = oldfile.listFiles();
			// 初始化特征
			fr = new FileReader(characterfile);
			br = new BufferedReader(fr);
			String n = null;
			character = new HashMap<String, String>();
			while ((n = br.readLine()) != null) {
				String[] n1 = n.split("：");
				String newdirpath=n1[0];
				File newdir =new File(newfilepath +"/"+newdirpath);
				newdir.mkdir();
				File sum = new File(newdir+"/总.txt");
				sum.createNewFile();
				String[] n2 = n1[1].split("、");
				for (int i = 0; i < n2.length; i++) {
					String charactername = n2[i];
					charactername = charactername.toLowerCase();// 转换成小写
					String path = newfilepath +"/"+newdirpath;
					File newcharacter = new File(path+ "/" + charactername + ".txt");
					newcharacter.createNewFile();
					character.put(charactername, path);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				br.close();
				fr.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
