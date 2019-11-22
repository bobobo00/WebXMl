package Servelet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;




public class WebXml {
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		//SAX解析
		//1、获取解析工厂
		SAXParserFactory factory=SAXParserFactory.newInstance();
		//2、从解析工厂获取解析器
		SAXParser parse=factory.newSAXParser();
		//3、编写处理器
				
		//4、加载文档 Document 注册处理器
		Handler handler=new Handler();
		//5、解析
		parse.parse(Thread.currentThread().getContextClassLoader().getResourceAsStream("Servelet\\web.xml"),handler);
		//获取数据
		List<Entity> l=handler.getEntitys();
		for(Entity e:l) {
			System.out.println(e.getName()+"->"+e.getClz());
		}
		List<Mapping> ll=handler.getMappings();
		for(Mapping m:ll) {
			System.out.println(m.getName()+"->"+m.getPatterns());
		}
	}

}
class Handler extends DefaultHandler{
	private List<Entity> entitys;
	private List<Mapping> mappings;
	private Entity entity;
	private Mapping mapping;
	private String tag;
	private boolean isMapping;
	
	public List<Entity> getEntitys() {
		return entitys;
	}

	public List<Mapping> getMappings() {
		return mappings;
	}

	@Override
	public void startDocument() throws SAXException {
		entitys=new ArrayList<>();
		mappings=new ArrayList<>();
	}
	
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if(null!=qName) {
			tag=qName;
			if(tag.equals("servlet")) {
				entity=new Entity();
			}
			if(tag.equals("servlet-mapping")) {
				mapping=new Mapping();
				isMapping=true;
			}
		}
	}
	
	public void characters(char[] ch, int start, int length) throws SAXException {
		String contents = new String(ch,start,length).trim();
		if(null!=tag) {
			if(!isMapping) {
				if(tag.equals("servlet-name")) {
					entity.setName(contents);
				}else if(tag.equals("servlet-class")) {
					entity.setClz(contents);
				}
			}else {
				if(tag.equals("servlet-name")) {
					mapping.setName(contents);
				}else if(tag.equals("url-pattern")) {
					mapping.addPattern(contents);
				}
			}
		}
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if(null!=qName) {
			if(qName.equals("servlet")) {
				entitys.add(entity);
			}else if(qName.equals("servlet-mapping")) {
				mappings.add(mapping);
				isMapping=false;
			}
		}
		tag=null;
	}
	public void endDocument() throws SAXException {
		
	}
}
