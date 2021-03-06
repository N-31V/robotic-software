package test;

import java.util.*;
import java.util.regex.Matcher; 
import java.util.regex.Pattern; 

public class calculat {

	public static void main (String[] args) throws java.lang.Exception
	{
		Scanner in = new Scanner(System.in);
		System.out.print("fu");
		String str = in.next();
		RD rd = new RD(str);
		double d1 = rd.getD();
		str = rd.getStr();
		rd = new RD(str);
		double d2 = rd.getD();
		str = rd.getStr();
		System.out.println(d1);
		System.out.println(d2);
		System.out.println(str);
	}
}

class RD
{
	double d;
	String str;
	RD()
	{
		d=0;
		str="";
	}
	RD(String s)
	{
		Pattern pattern = Pattern.compile("\\d+\\.?\\d+");
		Matcher matcher = pattern.matcher(s);
		if(matcher.find()){
			d=Double.parseDouble(s.substring(matcher.start(), matcher.end()));
			int end = matcher.end();
			str=s.substring(end);			
		}
		else {
			d=0;
			str="";
		}
	}
	public double getD()
	{
		return d;
	}
	public String getStr()
	{
		return str;
	}
}