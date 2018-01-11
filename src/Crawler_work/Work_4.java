package Crawler_work;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.Iterator;

public class Work_4 {

	public static File newfile;
	public static File oldfile;
	public static File characterfile;
	public static File nocharacterfile;
	public static String newfilepath;
	public static String oldfilepath;
	public static String characterfilepath;
	public static String nocharacterfilepath;
	private static FileWriter fw = null;
	private static BufferedWriter bw = null;
	private static FileReader fr = null;
	private static BufferedReader br = null;
	public static File[] list;
	private static HashMap<String, String> character;
	private static HashSet<String> nocharacter;

	public static void main(String[] args)  {
		// TODO Auto-generated method stub
		Work_4 work4 = new Work_4();
		try {
			work4.work();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void work() {
		for (int i = 0; i < list.length; i++) {
			// System.out.println(list[i].getName());
			String name = list[i].getName();
			String oldPath = oldfilepath + "/" + list[i].getName();
			System.out.println("当前文件是："+name);
			try {
				fr = new FileReader(oldPath);
				br = new BufferedReader(fr);
				String n = null;
				boolean jump=false;
				n = br.readLine();
				while ((n = br.readLine()) != null&&jump==false) {
					String[] n1 = n.split("、");
					for (int j = 0; j < n1.length; j++) {
						String n2 = n1[j];
						if (checknocharacter(n2) == false) {//检查不到忽略特征
							if (checkcharacter(n2) == false) {
								System.out.println(n1[j]);
								System.out.print("检测不到特征、请输入（以、隔开、直接输入为添加特征、2为添加忽略特征、3为跳过此条、4为跳过该文件、）：");
								Scanner scan = new Scanner(System.in);
								String read = scan.nextLine().trim();
//								System.out.println("检查到输入："+read);
								if (read.equals("4")) {
									jump=true;
									j = n1.length;
								} else if (read.equals("3")) {
									continue;
								}else if (read.equals("2")) {
									System.out.print("请输入（以、隔开）：");
									Scanner scan1 = new Scanner(System.in);
									String read1 = scan1.nextLine();
									addnocharacter(read1);
								} else {
									String[] read1 = read.split("、");
									for (int k = 0; k < read1.length; k++) {
										addcharacter(read1[k]);
									}
								}
							}
						}
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
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

	// 检查忽略特征
	public boolean checknocharacter(String s) {
		boolean result = false;
		Iterator iter = nocharacter.iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			if (s.indexOf(key) != -1) {
				return true;
			}
		}
		return result;
	}

	// 检查特征
	public boolean checkcharacter(String s) {
		boolean result = false;
		Iterator iter = character.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = (String) entry.getKey();
			if (s.indexOf(key) != -1) {
				try {
					result = true;
					fw = new FileWriter(character.get(s));
					bw = new BufferedWriter(fw);
					bw.write("1");
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

	// 添加忽略特征
	public void addnocharacter(String nocharactername) {
		nocharacter.add(nocharactername);
		FileWriter fw1;
		try {
			fw1 = new FileWriter(nocharacterfile);
			BufferedWriter bw1 = new BufferedWriter(fw1);
			bw1.write(nocharactername);
			bw1.close();
			fw1.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 添加特征
	public void addcharacter(String charactername) {
		File newcharacter = new File(newfilepath + "/" + charactername + ".txt");
		try {
			newcharacter.createNewFile();
			character.put(charactername, newfilepath + "/" + charactername + ".txt");
			FileWriter fw1 = new FileWriter(characterfile);
			BufferedWriter bw1 = new BufferedWriter(fw1);
			bw1.write(charactername);
			fw = new FileWriter(character.get(charactername));
			bw = new BufferedWriter(fw);
			bw.write("1");
			bw1.close();
			fw1.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Work_4()   {
		// 初始化路径
		newfilepath = "G:/dashuju/有效数据(第三步)";
		oldfilepath = "G:/dashuju/有效数据(第二步)";
		characterfilepath = newfilepath + "/特征.txt";
		nocharacterfilepath = newfilepath + "/忽略特征.txt";
		newfile = new File(newfilepath);
		oldfile = new File(oldfilepath);
		characterfile = new File(characterfilepath);
		nocharacterfile = new File(nocharacterfilepath);
		newfile.mkdir();
		try {
			characterfile.createNewFile();
			nocharacterfile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 初始化文件列表
		list = oldfile.listFiles();
		File[] list1 = newfile.listFiles();
		// 初始化特征
		character = new HashMap<String, String>();
		nocharacter = new HashSet<String>();
		for (int i = 0; i < list1.length; i++) {
			String charactername = list1[i].getName();
			character.put(charactername, newfilepath + "/" + charactername + ".txt");
		}
		// 初始化忽略特征
		String n = null;
//		System.out.println("到");
		try {
			fr = new FileReader(nocharacterfilepath);
			br = new BufferedReader(fr);
			while ((n = br.readLine()) != null) {
				nocharacter.add(n.trim());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				br.close();
				fr.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		
	}
}
