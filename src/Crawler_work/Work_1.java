package Crawler_work;

/**
 * ���̶߳�ȡ51job���´�����ҳ�����ݣ������г���ɸѡ
 * ��ȡ���Ϊ2
 * ��ȥ���ݴ洢��E:/dashuju/Ŀ¼�£������д���
 */
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;

public class Work_1 {
	// ��ȡ�����ݴ�ŵ���Ŀ¼��
	private static String savepath = "G:/dashuju/";
	// �ȴ���ȡ��url
	private static List<String> allwaiturl = new ArrayList<String>();
	// ��ȡ����url
	private static Set<String> alloverurl = new HashSet<String>();
	// ��¼����url����Ƚ�����ȡ�ж�
	private static Map<String, Integer> allurldepth = new HashMap<String, Integer>();
	// ��ȡ�����
	private static int maxdepth = 2;
	// ��ȡ�������Ч����
	private static int maxvalidnumber = 600;
	// �������󣬰��������̵߳ĵȴ�����
	private static Object obj = new Object();
	// ��¼���߳���5��
	private static int MAX_THREAD = 2;
	// ��¼���е��߳���
	private static int count = MAX_THREAD;
	// ��¼ԭ����
	private static PrintWriter pwy;
	private static File filey;
	private static int number;
	// ����״̬
	private static boolean run;

	public static void main(String args[]) {
		// ȷ����ȡ����ҳ��ַ���˴�Ϊ51job���´�����ҳ
		// ��ַΪ http://book.dangdang.com/
		// String
		// strurl="http://search.dangdang.com/?key=%BB%FA%D0%B5%B1%ED&act=input";
		try {
			run = true;
			number = 0;
			File file1 = new File(savepath + "/ԭʼ����");
			file1.mkdir();
			file1 = new File(savepath + "/��Ч����(����)");
			file1.mkdir();
			filey = new File(savepath + "/ԭʼ����/" + "wangzhi.txt");
			pwy = new PrintWriter(filey);
			String strurl = "http://search.51job.com/list/030200,000000,0000,00,9,99,%25E5%25A4%25A7%25E6%2595%25B0%25E6%258D%25AE,2,1.html?lang=c&stype=1&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&lonlat=0%2C0&radius=-1&ord_field=0&confirmdate=9&fromType=&dibiaoid=0&address=&line=&specialarea=00&from=&welfare=";
			// workurl(strurl,1);
			addurl(strurl, 0);
			for (int i = 0; i < MAX_THREAD; i++) {
				new Work_1().new MyThread().start();
			}
			while (run) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			pwy.close();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println("�������");
			System.exit(0);
		}
	}

	/**
	 * ��ҳ������ȡ
	 * @param strurl
	 * @param depth
	 */
	public static void workurl(String strurl, int depth) {
		// �жϵ�ǰurl�Ƿ���ȡ��
		// System.out.println(alloverurl.contains(strurl));
		if (!(alloverurl.contains(strurl) || depth > maxdepth)) {
			// ����߳��Ƿ�ִ��
			System.out.println("��ǰִ�У�" + Thread.currentThread().getName() + " ��ȡ�̴߳�����ȡ��" + strurl);
			// ����url��ȡ���Ķ���
			long nowtime = 0;
			try {
				URL url = new URL(strurl);
				// ͨ��url��������ҳ������
				URLConnection conn = url.openConnection();
				// ͨ������ȡ����ҳ���ص�����
				InputStream is = conn.getInputStream();
				// ��ȡtext���͵�����
				if (conn.getContentType().startsWith("text")) {

				}
				System.out.println(conn.getContentEncoding());
				// һ�㰴�ж�ȡ��ҳ���ݣ����������ݷ���
				// �����BufferedReader��InputStreamReader���ֽ���ת��Ϊ�ַ����Ļ�����
				// ����ת��ʱ����Ҫ��������ʽ����
				BufferedReader br = new BufferedReader(new InputStreamReader(is, "GB2312"));

				// ���ж�ȡ����ӡ
				String line = null;
				// ������ʽ��ƥ�������ȡ����ҳ������
				Pattern p1 = Pattern.compile("<p class=\"t1 \">");// ��ҳ����λ��
				Pattern p2 = Pattern.compile("</a></li><li class=\"bk\"><a href=.*��һҳ");// ��һҳ����λ��
				Pattern p3 = Pattern.compile("href=\".*\" ");// ��ҳ����ʽ
				Pattern p4 = Pattern.compile("<title>.*����");// �����ݱ���
				Pattern p5 = Pattern.compile("<div class=\"bmsg job_msg inbox\">");// ��Ч��Ϣǰһ��
				// ����һ������������ڱ����ļ�,�ļ���Ϊִ��ʱ�䣬�Է��ظ�
				nowtime = System.currentTimeMillis();
				PrintWriter pw = new PrintWriter(new File(savepath + "/ԭʼ����/" + nowtime + ".txt"));
				PrintWriter pw1 = null;
				boolean valid = false;// true���壺��һ��Ϊ��Ч��Ϣ
				int wy = 0;// ƥ������1Ϊ��ҳ����λ�ã�2Ϊ��һҳ����λ��,3Ϊ��Ч��ַ
				while ((line = br.readLine()) != null) {
					// System.out.println(line);
					// ��д����ƥ�䳬���ӵ�ַ
					pw.println(line);
					Matcher m = p1.matcher(line);
					if (m.find() == true) {
						wy = 1;
					} else if ((m = p2.matcher(line)).find() == true) {
						wy = 2;
						// System.out.println(m.group());
					} else if ((m = p4.matcher(line)).find() == true && line.indexOf("��ְ") == -1) {
						wy = 3;
						String[] title1 = line.split("��");
						String[] title2 = title1[1].split("_");
						String[] title3 = title2[0].split("-");
						String[] title4 = title3[0].split("/");
						String title = title4[0] + "," + nowtime;
						pw1 = new PrintWriter(new File(savepath + "/��Ч����(����)/" + title + ".txt"));
						pw1.println(title4[0]);
					}
					if (wy == 1 || wy == 2) {// ���¼���ַ
						if (wy == 1) {
							m = p3.matcher(line);
						}
						// ��wy==2��ֱ��ƥ�䣬����find������������룺�ַ���̫���������
						if (wy == 2 || m.find()) {
							String href = m.group();
							// �ҵ������ӵ�ַ����ȡ�ַ���
							// ��������
							href = href.substring(href.indexOf("href="));
							if (href.charAt(5) == '\"') {
								href = href.substring(6);
							} else {
								href = href.substring(5);
							}
							// ��ȡ�����Ż��߿ո���ߵ�">"����
							try {
								href = href.substring(0, href.indexOf("\""));
							} catch (Exception e) {
								try {
									href = href.substring(0, href.indexOf(" "));
								} catch (Exception e1) {
									href = href.substring(0, href.indexOf(">"));
								}
							}
							if (href.startsWith("http:") || href.startsWith("https:")) {
								/*
								 * //�������ҳ���ڵ����� //System.out.println(href);
								 * //��url��ַ�ŵ������� allwaiturl.add(href);
								 * allurldepth.put(href,depth+1);
								 */
								// ����addurl����
								if (wy == 1) {
									addurl(href, depth);
									number++;
									pwy.println(href + "  �ļ���Ϊ" + nowtime + "  ���ǵ�" + number + "��");
								} else if (wy == 2) {
									addurl(href, depth - 1);
								}
							}
							wy = 0;
						}
					} else if (wy == 3) {// ��Ч��ַ
						if (valid == false) {
							Matcher m1 = p5.matcher(line);
							if (m1.find() == true)
								valid = true;
						} else {
							String[] message = line.split("<br>");
							for (int i = 0; i < message.length; i++) {
								pw1.println(message[i].trim());
							}
							pw1.close();
							maxvalidnumber--;
							valid = false;
						}
					}
				}
				pw.close();
				br.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// ����ǰurl���е�alloverurl��
			alloverurl.add(strurl);
			System.out.println(
					strurl + "��ҳ��ȡ��ɣ��ļ���Ϊ" + nowtime + "����ȡ������" + alloverurl.size() + "��ʣ����ȡ��Ч������" + maxvalidnumber);
		}

		/*
		 * //�õݹ�ķ���������ȡ�������� String nexturl=allwaiturl.get(0);
		 * allwaiturl.remove(0); workurl(nexturl,allurldepth.get(nexturl));
		 */
	}

	/**
	 * ����ȡ��url����ȴ������У�ͬʱ�ж��Ƿ��Ѿ��Ź�
	 * 
	 * @param href
	 * @param depth
	 */
	public static synchronized void addurl(String href, int depth) {
		// ��url�ŵ�������
		allwaiturl.add(href);
		// �ж�url�Ƿ�Ź�
		if (!allurldepth.containsKey(href)) {
			allurldepth.put(href, depth + 1);
		}
	}

	/**
	 * �Ƴ���ȡ��ɵ�url����ȡ��һ��δ��ȡ��url
	 * 
	 * @return
	 */
	public static synchronized String geturl() {
		String nexturl = allwaiturl.get(0);
		// System.out.println(alloverurl.contains(nexturl));
		allwaiturl.remove(0);
		return nexturl;
	}

	/**
	 * �̷߳�������
	 */
	public class MyThread extends Thread {
		@Override
		public void run() {
			// �趨һ����ѭ�������߳�һֱ����
			while (run) {
				// �ж��Ƿ������ӣ������ȡ
				if (allwaiturl.size() > 0 && maxvalidnumber > 0) {
					count--;
//					System.out.println("�߳�" + Thread.currentThread().getName()+"  ��ȡ����");
					// ��ȡurl���д���
					String url = geturl();
					// ����workurl������ȡ
					workurl(url, allurldepth.get(url));
					// System.out.println("����"+allwaiturl.size());
					count++;
				} else if (MAX_THREAD == count) {
					// System.out.println("��ǰ�߳�׼���������ȴ�������ȡ��" + this.getName());
					// System.out.println(allwaiturl.size());
					System.out.println("��ȡ����.......");
					run = false;
				}
			}
		}

	}
}