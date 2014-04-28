package pageRank;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.visitors.HtmlPage;




/**
 * pagerank�㷨ʵ��
 * 
 * @author afei
 * 
 */
public class HtmlPageRank {

	/* ��ֵ */
	public static double MAX = 0.00000000001;

	/* ����ϵ�� */
	public static double alpha = 0.85;

	public static String htmldoc = "html";

	public static Map<String, HtmlEntity> map = new HashMap<String, HtmlEntity>();

	public static List<HtmlEntity> list = new ArrayList<HtmlEntity>();

	public static double[] init;

	public static double[] pr;

	public static void main(String[] args) throws Exception {
		loadHtml();
		pr = doPageRank();
		while (!(checkMax())) {
			System.arraycopy(pr, 0, init, 0, init.length);
			pr = doPageRank();
		}
		for (int i = 0; i < pr.length; i++) {
			HtmlEntity he=list.get(i);
			he.setPr(pr[i]);
		}
		
		Collections.sort(list);
		
		for(HtmlEntity he:list){
			System.out.println(he.getPath()+" : "+he.getPr());
		}

	}

	/* pagerank���� */

	/**
	 * �����ļ����µ���ҳ�ļ������ҳ�ʼ��prֵ(��init����)������ÿ����ҳ������������
	 */
	public static void loadHtml() throws Exception {
		File file = new File(htmldoc);
		File[] htmlfiles = file.listFiles(new FileFilter() {

			public boolean accept(File pathname) {
				if (pathname.getPath().endsWith(".html")) {
					return true;
				}
				return false;
			}

		});
		init = new double[htmlfiles.length];
		for (int i = 0; i < htmlfiles.length; i++) {
			File f = htmlfiles[i];
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(f)));
			String line = br.readLine();
			StringBuffer html = new StringBuffer();
			while (line != null) {
				line = br.readLine();
				html.append(line);
			}
			HtmlEntity he = new HtmlEntity();
			he.setPath(f.getAbsolutePath());
			he.setContent(html.toString());
			Parser parser = Parser.createParser(html.toString());
			HtmlPage page = new HtmlPage(parser);
			parser.visitAllNodesWith(page);
			NodeList nodelist = page.getBody();
			nodelist = nodelist.extractAllNodesThatMatch(
					new TagNameFilter("A"), true);
			for (int j = 0; j < nodelist.size(); j++) {
				LinkTag outlink = (LinkTag) nodelist.elementAt(j);
				he.getOutLinks().add(outlink.getAttribute("href"));
			}

			map.put(he.getPath(), he);
			list.add(he);
			init[i] = 0.0;

		}
		for (int i = 0; i < list.size(); i++) {
			HtmlEntity he = list.get(i);
			List<String> outlink = he.getOutLinks();
			for (String ol : outlink) {
				HtmlEntity he0 = map.get(ol);
				he0.getInLinks().add(he.getPath());
			}
		}

	}

	/**
	 * ����pagerank
	 * 
	 * @param init
	 * @param alpho
	 * @return
	 */
	private static double[] doPageRank() {
		double[] pr = new double[init.length];
		for (int i = 0; i < init.length; i++) {
			double temp = 0;
			HtmlEntity he0 = list.get(i);
			for (int j = 0; j < init.length; j++) {
				HtmlEntity he = list.get(j);
				// ����Ա�ҳ�����������ֵ
				if (i != j && he.getOutLinks().size() != 0
						&& he.getOutLinks().contains(he0.getPath())/*he0.getInLinks().contains(he.getPath())*/) {
					temp = temp + init[j] / he.getOutLinks().size();
				}

			}
			//�����pr��ʽ
			pr[i] = alpha + (1 - alpha) * temp;
		}
		return pr;
	}

	/**
	 * �ж�ǰ�����ε�pr����֮��Ĳ���Ƿ�������Ƕ���ķ�ֵ ������ڣ���ô����false��������������pr
	 * 
	 * @param pr
	 * @param init
	 * @param max
	 * @return
	 */
	private static boolean checkMax() {
		boolean flag = true;
		for (int i = 0; i < pr.length; i++) {
			if (Math.abs(pr[i] - init[i]) > MAX) {
				flag = false;
				break;
			}
		}
		return flag;
	}
	
	
	

}

