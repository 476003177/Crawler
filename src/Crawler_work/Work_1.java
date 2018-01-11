package Crawler_work;

/**
 * 多线程读取51job网下大数据页的数据，并进行初步筛选
 * 爬取深度为2
 * 爬去数据存储到E:/dashuju/目录下，需自行创建
 */
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;

public class Work_1 {
	// 提取的数据存放到该目录下
	private static String savepath = "G:/dashuju/";
	// 等待爬取的url
	private static List<String> allwaiturl = new ArrayList<String>();
	// 爬取过的url
	private static Set<String> alloverurl = new HashSet<String>();
	// 记录所有url的深度进行爬取判断
	private static Map<String, Integer> allurldepth = new HashMap<String, Integer>();
	// 爬取得深度
	private static int maxdepth = 2;
	// 爬取的最大有效个数
	private static int maxvalidnumber = 600;
	// 生命对象，帮助进行线程的等待操作
	private static Object obj = new Object();
	// 记录总线程数5条
	private static int MAX_THREAD = 2;
	// 记录空闲的线程数
	private static int count = MAX_THREAD;
	// 记录原数据
	private static PrintWriter pwy;
	private static File filey;
	private static int number;
	// 运行状态
	private static boolean run;

	public static void main(String args[]) {
		// 确定爬取的网页地址，此处为51job网下大数据页
		// 网址为 http://book.dangdang.com/
		// String
		// strurl="http://search.dangdang.com/?key=%BB%FA%D0%B5%B1%ED&act=input";
		try {
			run = true;
			number = 0;
			File file1 = new File(savepath + "/原始数据");
			file1.mkdir();
			file1 = new File(savepath + "/有效数据(初步)");
			file1.mkdir();
			filey = new File(savepath + "/原始数据/" + "wangzhi.txt");
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
			System.out.println("程序结束");
			System.exit(0);
		}
	}

	/**
	 * 网页数据爬取
	 * @param strurl
	 * @param depth
	 */
	public static void workurl(String strurl, int depth) {
		// 判断当前url是否爬取过
		// System.out.println(alloverurl.contains(strurl));
		if (!(alloverurl.contains(strurl) || depth > maxdepth)) {
			// 检测线程是否执行
			System.out.println("当前执行：" + Thread.currentThread().getName() + " 爬取线程处理爬取：" + strurl);
			// 建立url爬取核心对象
			long nowtime = 0;
			try {
				URL url = new URL(strurl);
				// 通过url建立与网页的连接
				URLConnection conn = url.openConnection();
				// 通过链接取得网页返回的数据
				InputStream is = conn.getInputStream();
				// 提取text类型的数据
				if (conn.getContentType().startsWith("text")) {

				}
				System.out.println(conn.getContentEncoding());
				// 一般按行读取网页数据，并进行内容分析
				// 因此用BufferedReader和InputStreamReader把字节流转化为字符流的缓冲流
				// 进行转换时，需要处理编码格式问题
				BufferedReader br = new BufferedReader(new InputStreamReader(is, "GB2312"));

				// 按行读取并打印
				String line = null;
				// 正则表达式的匹配规则提取该网页的链接
				Pattern p1 = Pattern.compile("<p class=\"t1 \">");// 网页特征位置
				Pattern p2 = Pattern.compile("</a></li><li class=\"bk\"><a href=.*下一页");// 下一页特征位置
				Pattern p3 = Pattern.compile("href=\".*\" ");// 网页正则式
				Pattern p4 = Pattern.compile("<title>.*数据");// 大数据标题
				Pattern p5 = Pattern.compile("<div class=\"bmsg job_msg inbox\">");// 有效信息前一行
				// 建立一个输出流，用于保存文件,文件名为执行时间，以防重复
				nowtime = System.currentTimeMillis();
				PrintWriter pw = new PrintWriter(new File(savepath + "/原始数据/" + nowtime + ".txt"));
				PrintWriter pw1 = null;
				boolean valid = false;// true含义：下一行为有效信息
				int wy = 0;// 匹配正则，1为网页特征位置，2为下一页特征位置,3为有效网址
				while ((line = br.readLine()) != null) {
					// System.out.println(line);
					// 编写正则，匹配超链接地址
					pw.println(line);
					Matcher m = p1.matcher(line);
					if (m.find() == true) {
						wy = 1;
					} else if ((m = p2.matcher(line)).find() == true) {
						wy = 2;
						// System.out.println(m.group());
					} else if ((m = p4.matcher(line)).find() == true && line.indexOf("求职") == -1) {
						wy = 3;
						String[] title1 = line.split("【");
						String[] title2 = title1[1].split("_");
						String[] title3 = title2[0].split("-");
						String[] title4 = title3[0].split("/");
						String title = title4[0] + "," + nowtime;
						pw1 = new PrintWriter(new File(savepath + "/有效数据(初步)/" + title + ".txt"));
						pw1.println(title4[0]);
					}
					if (wy == 1 || wy == 2) {// 有下级网址
						if (wy == 1) {
							m = p3.matcher(line);
						}
						// 若wy==2，直接匹配，调用find函数会出错，猜想：字符串太长导致溢出
						if (wy == 2 || m.find()) {
							String href = m.group();
							// 找到超链接地址并截取字符串
							// 有无引号
							href = href.substring(href.indexOf("href="));
							if (href.charAt(5) == '\"') {
								href = href.substring(6);
							} else {
								href = href.substring(5);
							}
							// 截取到引号或者空格或者到">"结束
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
								 * //输出该网页存在的链接 //System.out.println(href);
								 * //将url地址放到队列中 allwaiturl.add(href);
								 * allurldepth.put(href,depth+1);
								 */
								// 调用addurl方法
								if (wy == 1) {
									addurl(href, depth);
									number++;
									pwy.println(href + "  文件名为" + nowtime + "  这是第" + number + "个");
								} else if (wy == 2) {
									addurl(href, depth - 1);
								}
							}
							wy = 0;
						}
					} else if (wy == 3) {// 有效网址
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
			// 将当前url归列到alloverurl中
			alloverurl.add(strurl);
			System.out.println(
					strurl + "网页爬取完成，文件名为" + nowtime + "已爬取数量：" + alloverurl.size() + "，剩余爬取有效数量：" + maxvalidnumber);
		}

		/*
		 * //用递归的方法继续爬取其他链接 String nexturl=allwaiturl.get(0);
		 * allwaiturl.remove(0); workurl(nexturl,allurldepth.get(nexturl));
		 */
	}

	/**
	 * 将获取的url放入等待队列中，同时判断是否已经放过
	 * 
	 * @param href
	 * @param depth
	 */
	public static synchronized void addurl(String href, int depth) {
		// 将url放到队列中
		allwaiturl.add(href);
		// 判断url是否放过
		if (!allurldepth.containsKey(href)) {
			allurldepth.put(href, depth + 1);
		}
	}

	/**
	 * 移除爬取完成的url，获取下一个未爬取得url
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
	 * 线程分配任务
	 */
	public class MyThread extends Thread {
		@Override
		public void run() {
			// 设定一个死循环，让线程一直存在
			while (run) {
				// 判断是否新链接，有则获取
				if (allwaiturl.size() > 0 && maxvalidnumber > 0) {
					count--;
//					System.out.println("线程" + Thread.currentThread().getName()+"  领取任务");
					// 获取url进行处理
					String url = geturl();
					// 调用workurl方法爬取
					workurl(url, allurldepth.get(url));
					// System.out.println("这里"+allwaiturl.size());
					count++;
				} else if (MAX_THREAD == count) {
					// System.out.println("当前线程准备就绪，等待连接爬取：" + this.getName());
					// System.out.println(allwaiturl.size());
					System.out.println("爬取结束.......");
					run = false;
				}
			}
		}

	}
}