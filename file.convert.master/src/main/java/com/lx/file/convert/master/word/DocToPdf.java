package com.lx.file.convert.master.word;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToFoConverter;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.xmlgraphics.io.URIResolverAdapter;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.lx.file.convert.master.utils.PathMaster;

public class DocToPdf {
	//配置信息地址
	private static final String  CONFIG="C:\\Users\\Administrator\\Desktop\\新建文件夹\\fop-2.3\\fop\\conf\\fop.xconf";
	private static final String  OUTFILEFO=PathMaster.getWebRootPath()  + java.io.File.separator + "temp"+java.io.File.separator+"test.xml";
	private static final String  OUTFILEPDF=PathMaster.getWebRootPath()  + java.io.File.separator + "temp"+java.io.File.separator+"test.pdf";
	/**
	 * doc转xml
	 */
	public String toXML(String filePath){
		
	try{
		
		POIFSFileSystem nPOIFSFileSystem = new POIFSFileSystem(new File(filePath));

		HWPFDocument nHWPFDocument = new HWPFDocument(nPOIFSFileSystem);
		WordToFoConverter nWordToHtmlConverter = new WordToFoConverter(
				DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
		PicturesManager nPicturesManager = new PicturesManager() {
			
			public String savePicture(byte[] arg0, PictureType arg1,String arg2, float arg3, float arg4) {
				//file:///F://20.vscode//iWorkP//temp//images//0.jpg
				//System.out.println("file:///"+PathMaster.getWebRootPath()+ java.io.File.separator + "temp"+java.io.File.separator+"images" + java.io.File.separator + arg2);
//				return  "file:///"+PathMaster.getWebRootPath()+java.io.File.separator +"temp"+java.io.File.separator+"images" + java.io.File.separator + arg2;
				return  "file:///"+PathMaster.getWebRootPath()+java.io.File.separator +"temp"+java.io.File.separator+"images" + java.io.File.separator + arg2;
			}
		};

		nWordToHtmlConverter.setPicturesManager(nPicturesManager);
		nWordToHtmlConverter.processDocument(nHWPFDocument);
		String nTempPath = PathMaster.getWebRootPath()  + java.io.File.separator + "temp" + java.io.File.separator + "images" + java.io.File.separator;
		File nFile = new File(nTempPath);
		
		if (!nFile.exists()) {
			nFile.mkdirs();
		}
		for (Picture nPicture : nHWPFDocument.getPicturesTable().getAllPictures()) {
			nPicture.writeImageContent(new FileOutputStream(nTempPath + nPicture.suggestFullFileName()));
		}
		Document nHtmlDocument = nWordToHtmlConverter.getDocument();
		OutputStream nByteArrayOutputStream = new FileOutputStream(OUTFILEFO);
		DOMSource nDOMSource = new DOMSource(nHtmlDocument);
		StreamResult nStreamResult = new StreamResult(nByteArrayOutputStream);

		
		TransformerFactory nTransformerFactory = TransformerFactory.newInstance();
		Transformer nTransformer = nTransformerFactory.newTransformer();
		
		nTransformer.setOutputProperty(OutputKeys.ENCODING, "GBK");
		nTransformer.setOutputProperty(OutputKeys.INDENT, "YES");
		nTransformer.setOutputProperty(OutputKeys.METHOD, "xml");
		
		nTransformer.transform(nDOMSource, nStreamResult);

		nByteArrayOutputStream.close();

		return "";
		
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}
	/*
	 * xml 转pdf
	 */
	public void xmlToPDF() throws SAXException, TransformerException{
		// Step 1: Construct a FopFactory by specifying a reference to the configuration file
		// (reuse if you plan to render multiple documents!)
		FopFactory fopFactory = null;
		new URIResolverAdapter(new URIResolver(){
			public Source resolve(String href, String base) throws TransformerException {
				try {
		            URL url = new URL(href);
		            URLConnection connection = url.openConnection();
		            connection.setRequestProperty("User-Agent", "whatever");
		            return new StreamSource(connection.getInputStream());
		        } catch (IOException e) {
		            throw new RuntimeException(e);
		        }
			}
		});
		OutputStream out = null;
		try {
			
			fopFactory = FopFactory.newInstance(new File(CONFIG));
			
			// Step 2: Set up output stream.
			// Note: Using BufferedOutputStream for performance reasons (helpful with FileOutputStreams).
			
			out = new BufferedOutputStream(new FileOutputStream(OUTFILEPDF));
		    
			// Step 3: Construct fop with desired output format
		    Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);

		    // Step 4: Setup JAXP using identity transformer
		    TransformerFactory factory = TransformerFactory.newInstance();
		    Transformer transformer = factory.newTransformer(); // identity transformer
		    
		    // Step 5: Setup input and output for XSLT transformation
		    // Setup input stream
		    Source src = new StreamSource(OUTFILEFO);

		    // Resulting SAX events (the generated FO) must be piped through to FOP
		    Result res = new SAXResult(fop.getDefaultHandler());
		    // Step 6: Start XSLT transformation and FOP processing
		    transformer.transform(src, res);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		    //Clean-up
		    try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}}
	
	public static void main(String[] args) throws SAXException, TransformerException {
		 new DocToPdf().toXML("C:\\Users\\Administrator\\Desktop\\doc\\附件四：提取住房公积金支付房屋租赁费用申请表.doc");
		 new DocToPdf().xmlToPDF();
	}
}
