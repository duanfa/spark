package pageRank;

import java.util.ArrayList;
import java.util.List;

/**
 * 网页entity
 * 
 * @author afei
 * 
 */
class HtmlEntity implements Comparable<HtmlEntity>{

	private String path;
	private String content;
	/* 外链(本页面链接的其他页面) */
	private List<String> outLinks = new ArrayList<String>();

	/* 内链(另外页面链接本页面) */
	private List<String> inLinks = new ArrayList<String>();

	private double pr;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public double getPr() {
		return pr;
	}

	public void setPr(double pr) {
		this.pr = pr;
	}

	public List<String> getOutLinks() {
		return outLinks;
	}

	public void setOutLinks(List<String> outLinks) {
		this.outLinks = outLinks;
	}

	public List<String> getInLinks() {
		return inLinks;
	}

	public void setInLinks(List<String> inLinks) {
		this.inLinks = inLinks;
	}
	
	@Override
	public int compareTo(HtmlEntity o2) {
		int em=0;
		if(getPr()> o2.getPr()){
			em=-1;
		}else{
			em=1;
		}
		return em;
	}

}
