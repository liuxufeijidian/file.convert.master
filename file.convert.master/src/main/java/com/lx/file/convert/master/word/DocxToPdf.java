package com.lx.file.convert.master.word;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.poi.xwpf.converter.core.utils.StringUtils;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

public class DocxToPdf {

	   
    /** 
     * 将word文档， 转换成pdf, 中间替换掉变量 
     * @param source 源为word文档， 必须为docx文档 
     * @param target 目标输出 
     * @param params 需要替换的变量 
     * @throws Exception 
     */  
    public static void wordConverterToPdf(InputStream source,OutputStream target, Map<String, String> params) throws Exception {  
    	//org.apache.poi.openxml4j.pc.PackageRelationship
    	//org.apache.poi.xwpf.usermodel.IBody
    	wordConverterToPdf(source, target, null, params);      
    }  
   
    /** 
     * 将word文档， 转换成pdf, 中间替换掉变量 
     * @param source 源为word文档， 必须为docx文档 
     * @param target 目标输出 
     * @param params 需要替换的变量 
     * @param options PdfOptions.create().fontEncoding( "windows-1250" ) 或者其他 
     * @throws Exception 
     */  
    public static void wordConverterToPdf(InputStream source, OutputStream target,PdfOptions options,Map<String, String> params) throws Exception {  
         XWPFDocument doc = new XWPFDocument(source);  
         paragraphReplace(doc.getParagraphs(), params);  
         for (XWPFTable table : doc.getTables()) {  
           for (XWPFTableRow row : table.getRows()) {  
               for (XWPFTableCell cell : row.getTableCells()) {  
                   paragraphReplace(cell.getParagraphs(), params);  
               }  
           }  
       }  
       PdfConverter.getInstance().convert(doc, target, options);  
    }  
       
    /** 替换段落中内容 */  
    private static void paragraphReplace(List<XWPFParagraph> paragraphs, Map<String, String> params) {  
        if (MapUtils.isNotEmpty(params)) {  
            for (XWPFParagraph p : paragraphs){  
                for (XWPFRun r : p.getRuns()){  
                    String content = r.getText(r.getTextPosition());  
                    if(StringUtils.isNotEmpty(content) && params.containsKey(content)) {  
                        r.setText(params.get(content), 0);  
                    }  
                }  
            }  
        }  
    }  
       
    
    public static void main(String[] args) {
        String filepath = "C:\\Users\\Administrator\\Desktop\\知识管理（旗舰版）需求分析文档-2017-12-25.docx";  
        String outpath = "C:\\Users\\Administrator\\Desktop\\知识管理（旗舰版）需求分析文档-2017-12-25.pdf";   
           
        InputStream source;
		OutputStream target;
		try {
			source = new FileInputStream(filepath);  
			target = new FileOutputStream(outpath);
	        Map<String, String> params = new HashMap<String, String>();  
	           
	           
	        PdfOptions options = PdfOptions.create();  
	           
	        wordConverterToPdf(source, target, options, params);  
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
 
	}
}
