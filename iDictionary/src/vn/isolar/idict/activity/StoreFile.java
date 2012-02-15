package vn.isolar.idict.activity;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

public class StoreFile {
	Context context;

	File file;
	boolean checkLimit;

	StoreFile(Context context, File filexml, boolean checkLimit) {
		this.context = context;
		this.file = filexml;
		this.checkLimit = checkLimit;
	}
	
	ArrayList<Integer> listID;
	ArrayList<Newwords> listNewWord;
	ArrayList<String> ListDateTime;
	/**
	 * append cho history
	 * @param key
	 * @param value
	 * @param strdate
	 */
	public void appendText(String key, String value, String strdate) {
		try {
			Document document;
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory
					.newDocumentBuilder();
			if (!file.exists()) {
				document = documentBuilder.newDocument();
				Element rootelement = document.createElement("listdict");
				document.appendChild(rootelement);

				Element dictElement = document.createElement("dict");
				dictElement.setAttribute("key", key);
				dictElement.setTextContent(value);
				dictElement.setAttribute("date", strdate);
				rootelement.appendChild(dictElement);

			} else {
				document = documentBuilder.parse(file);

				int maxHistory = Integer.parseInt(PreferenceManager
						.getDefaultSharedPreferences(context).getString(
								"setMaxHistory", "20"));

				Node rootNode = document.getFirstChild();
				Log.d("node", rootNode.getNodeName());
				NodeList list = rootNode.getChildNodes();
				

				// kiểm tra xem có vượt quá max History cho phép không
				if (checkLimit == true)
				{Log.d("limit", "co l:"+list.getLength());
				Log.d("limit", "co l:"+maxHistory);
					if (list.getLength() >= maxHistory) {
						Log.d("limit", "co1");
						rootNode = rootNode.removeChild(rootNode
								.getFirstChild());
					}
				}
				Element tempdict = document.createElement("dict");
				tempdict.setAttribute("key", key);
				tempdict.setAttribute("date", strdate);
				tempdict.setTextContent(value);
				rootNode.appendChild(tempdict);

			}

			// save file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();

			DOMSource sourc = new DOMSource(document);
			StreamResult result = new StreamResult(file);
			transformer.transform(sourc, result);

		} catch (Exception e) {
		    Log.e("kiem trA", e.getMessage());
		}
	}
	
	public void appendText(String key, String value) {
		try {
			Document document;
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory
					.newDocumentBuilder();
			if (!file.exists()) {
				document = documentBuilder.newDocument();
				Element rootelement = document.createElement("listdict");
				document.appendChild(rootelement);

				Element dictElement = document.createElement("dict");
				dictElement.setAttribute("key", key);
				dictElement.setTextContent(value);

				rootelement.appendChild(dictElement);

			} else {
				document = documentBuilder.parse(file);

				int maxHistory = Integer.parseInt(PreferenceManager
						.getDefaultSharedPreferences(context).getString(
								"setMaxHistory", "20"));

				Node rootNode = document.getFirstChild();
				Log.d("node", rootNode.getNodeName());
				NodeList list = rootNode.getChildNodes();

				

				// kiểm tra xem có vượt quá max History cho phép không
				if (checkLimit == true)
				{Log.d("limit", "co l:"+list.getLength());
				Log.d("limit", "co l:"+maxHistory);
					if (list.getLength() >= maxHistory) {
						Log.d("limit", "co1");
						rootNode = rootNode.removeChild(rootNode
								.getFirstChild());
					}
				}
				Element tempdict = document.createElement("dict");
				tempdict.setAttribute("key", key);
				tempdict.setTextContent(value);
				rootNode.appendChild(tempdict);

			}

			// save file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();

			DOMSource sourc = new DOMSource(document);
			StreamResult result = new StreamResult(file);
			transformer.transform(sourc, result);

		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	/*
	 * Thuc hien phuong thuc nay sau do goi 
	 * gesListNewWord
	 * getListID
	 */
	public void getAllContext() {
		
		listID= new ArrayList<Integer>();
		listNewWord= new ArrayList<Newwords>();		
		Newwords temp_word;
		if (!file.exists()) {
			return ;
		} else
			try {
				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder documentBuilder = documentBuilderFactory
						.newDocumentBuilder();

				Document document = documentBuilder.parse(file);

				Node rootNode = document.getFirstChild();

				NodeList list = rootNode.getChildNodes();
				if(checkLimit==false)
				for (int i = list.getLength() - 1; i >= 0; i--) {
					listID.add(Integer.parseInt(list.item(i).getAttributes().getNamedItem("key").getTextContent()));					
					temp_word= new Newwords(list.item(i).getTextContent(),null);
					listNewWord.add(temp_word);
					
				}
				else
				{	ListDateTime= new ArrayList<String>();
					for (int i = list.getLength() - 1; i >= 0; i--) {
						listID.add(Integer.parseInt(list.item(i).getAttributes().getNamedItem("key").getTextContent()));					
						temp_word= new Newwords(list.item(i).getTextContent(),null);
						listNewWord.add(temp_word);
						ListDateTime.add(list.item(i).getAttributes().getNamedItem("date").getTextContent());
						
						}
				}
			} catch (Exception e) {
			}
		
	}
	public ArrayList<Integer> getListID() {
		return listID;
	}
	public ArrayList<Newwords> getListNewWord() {
		return listNewWord;
	}
	public ArrayList<String> getListDateTime() {
		return ListDateTime;
	}
	public boolean checkKey(String key) {
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory
					.newDocumentBuilder();

			Document document = documentBuilder.parse(file);
			Node rootNode = document.getFirstChild();
			NodeList list = rootNode.getChildNodes();
			for (int i = 0; i < list.getLength(); i++) {
				if (list.item(i).getAttributes().getNamedItem("key")
						.getTextContent().equals(key)) {
					return true;
				}
			}
		} catch (Exception e) {
		}
		return false;
	}

	public void removeById(String id) {
		try {
			Log.d("da delete", "de");
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory
					.newDocumentBuilder();

			Document document = documentBuilder.parse(file);

			Node rootNode = document.getFirstChild();

			NodeList list = rootNode.getChildNodes();
			for (int i = 0; i < list.getLength(); i++) {
				if (list.item(i).getAttributes().getNamedItem("key")
						.getTextContent().equals(id)) {
					rootNode.removeChild(list.item(i));
					break;
				}
			}
			// save file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();

			DOMSource sourc = new DOMSource(document);
			StreamResult result = new StreamResult(file);
			transformer.transform(sourc, result);
			Log.d("da delete", "delete");
		} catch (Exception e) {
			Log.e("delete",e.getMessage());
		}
	}

	public void clear() {
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory
					.newDocumentBuilder();

			Document document = documentBuilder.parse(file);

			Node rootNode = document.getFirstChild();	
						

			while (rootNode.hasChildNodes()) {
				 rootNode.removeChild(rootNode.getFirstChild());
			}
			
			// save file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();

			DOMSource sourc = new DOMSource(document);
			StreamResult result = new StreamResult(file);
			transformer.transform(sourc, result);

		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
}
