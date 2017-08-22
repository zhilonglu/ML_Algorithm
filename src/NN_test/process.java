package NN_test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class process {
	static SimpleDateFormat sdf_day=new SimpleDateFormat("yyyy-MM-dd");  
	static SimpleDateFormat sdf_min=new SimpleDateFormat("HH:mm:ss"); 
	static double[][] tensor = new double[92][90];
	static double[][] trainX = new double[85][60];
	static double[][] trainY = new double[85][30];
	static double[][] preX = new double[7][60];
	static String[] linkID = new String[132];
	static HashMap<String,String> relink = new HashMap<String,String>();
	static HashMap<String,String[]> link_key = new HashMap<String,String[]>();
	public static void loadData(String file){
		BufferedReader br;
		String[] content;
		String record;
		int row = 0;
		int col = 0;
		try {
			br = new BufferedReader(new FileReader(new File(file)));
			try {
				
				while((record = br.readLine())!=null){
					content = record.split(",");
					col = content.length;
					for(int i=0;i<col;i++){
						tensor[row][i] = Double.valueOf((content[i]));
					}
					row += 1;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i=0;i<tensor.length;i++){
			if(i<=84){
				for(int j=0;j<tensor[0].length;j++){
					if(j<=59)
						trainX[i][j] = tensor[i][j];
					else
						trainY[i][j-60] = tensor[i][j];
				}
			}
			else{
				for(int j=0;j<60;j++){
					preX[i-85][j] = tensor[i][j];
				}
			}
		}
	}
	public static void readLink(){
		BufferedReader br;
		String[] content;
		String record;
		int row = 0;
		try {
			br = new BufferedReader(new FileReader(new File("C:\\Users\\NLSDE\\Desktop\\GZ_kdd\\gy_contest_link_info.txt")));
			try {
				br.readLine();//跳过第一行
				while((record = br.readLine())!=null){
					content = record.split(";");
					linkID[row] = content[0];
					row += 1;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void readJson(){
		BufferedReader br;
		String record;
		String[] content;
		try {
			br = new BufferedReader(new FileReader(new File("C:\\Users\\NLSDE\\Desktop\\GZ_kdd\\"
					+ "reDict.json")));
			try {
				while((record = br.readLine())!=null){
					content = record.replace("{", "").replace("}", "").replace(" ","").split(",");
					for (String string : content) {
						String[] tm = string.split(":");
						relink.put(tm[0].replace("\"",""), tm[1]);
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void initOutputKey(){
		readLink();
		String[] dayT = initDay();
		String[] minT = initMin();
		for(int i=0;i<linkID.length;i++){
			String[] output = new String[210];
			for(int j=0;j<dayT.length;j++){
				for(int k=0;k<minT.length-1;k++){
					String timeInterval = "["+dayT[j]+" "+minT[k]+","+dayT[j]+" "+minT[k+1]+")";
					output[j*30+k] = linkID[i]+"#"+dayT[j]+"#"+timeInterval;
				}
			}
			String temp = relink.get(linkID[i]);
			link_key.put(temp, output);
		}
	}
	public static String[] initDay(){
		String[] dayTime= new String[7];
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(sdf_day.parse("2016-05-24"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i=0;i<dayTime.length;i++){
			c.add(Calendar.DATE, 1); //日期加1天  
			dayTime[i] = sdf_day.format(c.getTime());  
		}
		return dayTime;
	}
	public static String[] initMin(){
		String[] ktime= new String[31];
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(sdf_min.parse("07:58:00"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i=0;i<31;i++){
			c.add(Calendar.MINUTE, 2);  
			ktime[i] = sdf_min.format(c.getTime());
		}
		return ktime;
	}
}
