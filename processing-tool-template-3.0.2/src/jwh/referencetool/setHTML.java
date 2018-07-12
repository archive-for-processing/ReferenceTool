package jwh.referencetool;

import javax.swing.JEditorPane;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import java.io.*;
import java.net.URL;
import java.util.*;

public class setHTML extends JEditorPane {
	HTMLEditorKit editorkit;
	StyleSheet css = new StyleSheet();
	String name = "";
	String returns = "";
	String description = "";
	String syntax = "";
	
	ArrayList<String> parameterNames = new ArrayList<String>();
	ArrayList<String> parameterDescs = new ArrayList<String>();
	ArrayList<String> exampleImages = new ArrayList<String>();
	ArrayList<String> exampleCodes = new ArrayList<String>();
	
	public setHTML() {
		// not sure how this is used just yet
		this.setContentType("text/html");
		editorkit = new HTMLEditorKit();
		css = editorkit.getStyleSheet();
		setCSS();
		editorkit.setAutoFormSubmission(false);
		this.setEditorKit(editorkit);
	}
	
	public void setCSS() {
		css.addRule("body {font-family: raleway; font-size:9px}");
		css.addRule(".sectionStyle {padding-top: 20px}");
		css.addRule(".widthStyle {width : 70px}");
		css.addRule(".sectionheaderStyle {width : 70px; valign: top}");
	}
	
	public void parseHTML(URL urlLink) {
		RegEx regexer = new RegEx(urlLink);
		name = regexer.parseName();
		System.out.println(name);
		regexer.parseExamples();
		exampleImages = regexer.get_exampleImages();
		System.out.println(exampleImages);
		exampleCodes = regexer.get_exampleCodes();
		System.out.println(exampleCodes);
		description = regexer.parseDescription();
		regexer.parseParameters();
		parameterNames = regexer.get_parameterNames();
		System.out.println(parameterNames);
		parameterDescs = regexer.get_parameterDescs();
		System.out.println(parameterDescs);
		syntax = regexer.parseSyntax();
		returns = regexer.parseReturns();
		System.out.println(returns);
		
		fillIn();
	}
	
	public void fillIn() {
		String finalexampleString = exampleString();
		String descriptionString = descriptionString();
		String syntaxString = syntaxString();
		String parameterString = parameterString();
		String returnString = returnString();
		
		String namestring = "<table>"
				+ "<tr valign= \"top\">"
				+ "<td class=\"widthStyle\"><u>Name</u></td>"
				+ "<td><b>" + name + "</b></td>"
				+ "</tr>" 
				+ "</table>"
				+ "<table class=\"sectionStyle\">" 
				+ "<tr valign = \"top\"><td class=\"sectionheaderStyle\"><u>Examples</u></td>";
		
		String total = namestring + finalexampleString + descriptionString + syntaxString + parameterString + returnString;
//		System.out.println(total);
		this.setText(total);
	}
	
	public String exampleString() {
		String hr = "<tr valign=\"top\"><td class=\"widthStyle\">&nbsp;</td><td><hr></td></tr>";
		String examples = "";
		String finalexampleString = "";
		for(int i = 0; i < exampleCodes.size(); i++) {
			String exampletr = "";

			if(i > 0) {
				exampletr = "<tr><td class=\"widthStyle\">&nbsp;</td>";
			}
			
			String imageLocation = "";
			if(exampleImages.size() != 0) {
				imageLocation = exampleImages.get(i);
				imageLocation = new StringBuilder(imageLocation).insert(1, "../").toString();
				imageLocation = "<td><img src="+imageLocation+"></td>";
//				System.out.println(imageLocation);
			}
			
			String code = "";
			code = exampleCodes.get(i).trim().replaceAll("\\n", "<br>");
			
			code = "<td class=\"widthStyle\"><pre >"+ code + "</pre></td></tr>";
			
			if(i < (exampleCodes.size()-1) && imageLocation.equals("")) {
				code = code + hr;
			}
//			System.out.println(code);
			
			if(!imageLocation.equals("")) {
				finalexampleString = finalexampleString + exampletr+imageLocation+code;
			} else {
				finalexampleString = finalexampleString + exampletr+code;
			}
		}

		finalexampleString = finalexampleString + "</table>";
//		System.out.println(finalexampleString);
		return finalexampleString;
	}
	
	public String descriptionString() {
		String descriptionstring = "<table class=\"sectionStyle\"><tr valign=\"top\"><td class=\"widthStyle\"><u>Description</u></td><td>";
		
		descriptionstring = descriptionstring + description + "</td></tr><table>";
		
		return descriptionstring;
	}
	
	public String syntaxString() {
		String syntaxstring = "<table class=\"sectionStyle\"><tr valign=\"top\"><td class=\"widthStyle\"><u>Syntax</u></td>";
		
		if(!syntax.equals("")) {
			syntax = syntax.trim().replaceAll("\\n", "<br>");
			
			syntaxstring = syntaxstring + syntax + "</td></tr></table>";
	
		} else {
			syntaxstring = "";
		}
		
		return syntaxstring;
	}
	
	public String parameterString() {
		String parameterstring = "<table class=\"sectionStyle\"><tr valign=\"top\"><td class=\"widthStyle\"><u>Parameters</u></td>";
		String finalparamstring = "";
		if(parameterNames.size() != 0) {
			for(int i = 0; i < parameterNames.size(); i++) {
				String addon = "";
				if(i > 0) {
					addon = "<tr class=\"widthStyle\">&nbsp;</td>";
				}
				
				String name = parameterNames.get(i);
				name = "<td class = \"widthStyle\"><b>"+name+"</b></td>";
				String description = "<td>"+parameterDescs.get(i)+"</td></tr>";
				
				finalparamstring = finalparamstring + addon + name + description;
			}
		}
		
		finalparamstring = finalparamstring + "</table>";
		
		return finalparamstring;
	}
	
	public String returnString() {
		String returnstring = "";
		if(!returns.equals("")) {
			returnstring = "<table class=\"sectionStyle\"><tr valign=\"top\"><td class=\"widthStyle\"><u><Returns</u></td><td>"+returns+"</td></tr></table>";
		}

		return returnstring;
	}
}
